package Hack.Controller;

import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import Hack.Utilities.Conversions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.StringCharacterIterator;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.Timer;

public class HackController implements ControllerEventListener, ActionListener, ProgramEventListener {
   public static final int NUMBER_OF_SPEED_UNITS = 5;
   public static final float[] SPEED_FUNCTION = new float[]{0.0F, 0.35F, 0.63F, 0.87F, 1.0F};
   public static final int[] FASTFORWARD_SPEED_FUNCTION = new int[]{500, 1000, 2000, 4000, 15000};
   public static final int DISPLAY_CHANGES = 0;
   public static final int ANIMATION = 1;
   public static final int NO_DISPLAY_CHANGES = 2;
   public static final int DECIMAL_FORMAT = 0;
   public static final int HEXA_FORMAT = 1;
   public static final int BINARY_FORMAT = 2;
   public static final int SCRIPT_ADDITIONAL_DISPLAY = 0;
   public static final int OUTPUT_ADDITIONAL_DISPLAY = 1;
   public static final int COMPARISON_ADDITIONAL_DISPLAY = 2;
   public static final int NO_ADDITIONAL_DISPLAY = 3;
   private static final String INITIAL_SCRIPT_DIR = "scripts";
   private static final int MAX_MS = 2500;
   private static final int MIN_MS = 25;
   private static final int INITIAL_SPEED_UNIT = 3;
   private static final String SPACES = "                                        ";
   protected ControllerGUI gui;
   private File currentScriptFile;
   private String currentOutputName;
   private String currentComparisonName;
   private Script script;
   protected HackSimulator simulator;
   private int currentSpeedUnit;
   private int animationMode;
   private int currentCommandIndex;
   private PrintWriter output;
   private BufferedReader comparisonFile;
   private int loopCommandIndex;
   private int repeatCounter;
   private ScriptCondition whileCondititon;
   private VariableFormat[] varList;
   private Vector breakpoints;
   private int compareLinesCounter;
   private int outputLinesCounter;
   private Timer timer;
   protected boolean singleStepLocked;
   private HackController.SingleStepTask singleStepTask;
   private HackController.FastForwardTask fastForwardTask;
   private HackController.SetAnimationModeTask setAnimationModeTask;
   private HackController.SetNumericFormatTask setNumericFormatTask;
   private boolean fastForwardRunning;
   private boolean singleStepRunning;
   private boolean scriptEnded;
   private boolean programHalted;
   private int[] delays;
   private boolean comparisonFailed;
   private int comparisonFailureLine;
   private String lastEcho;
   private File defaultScriptFile;

   public HackController(HackSimulator var1, String var2) {
      File var3 = new File(var2);
      if (!var3.exists()) {
         this.displayMessage(var2 + " doesn't exist", true);
      }

      this.simulator = var1;
      this.animationMode = 2;
      var1.setAnimationMode(this.animationMode);
      var1.addListener(this);
      this.breakpoints = new Vector();

      try {
         this.loadNewScript(var3, false);
         this.saveWorkingDir(var3);
      } catch (ScriptException var5) {
         this.displayMessage(var5.getMessage(), true);
      } catch (ControllerException var6) {
         this.displayMessage(var6.getMessage(), true);
      }

      this.fastForwardRunning = true;

      while(this.fastForwardRunning) {
         this.singleStep();
      }

   }

   public HackController(ControllerGUI var1, HackSimulator var2, String var3) throws ScriptException, ControllerException {
      this.gui = var1;
      this.simulator = var2;
      this.singleStepTask = new HackController.SingleStepTask();
      this.fastForwardTask = new HackController.FastForwardTask();
      this.setAnimationModeTask = new HackController.SetAnimationModeTask();
      this.setNumericFormatTask = new HackController.SetNumericFormatTask();
      var2.addListener(this);
      var2.addProgramListener(this);
      this.breakpoints = new Vector();
      this.defaultScriptFile = new File(var3);
      this.loadNewScript(this.defaultScriptFile, false);
      this.delays = new int[5];

      for(int var4 = 0; var4 < 5; ++var4) {
         this.delays[var4] = (int)(2500.0F - SPEED_FUNCTION[var4] * 2475.0F);
      }

      this.currentSpeedUnit = 3;
      this.animationMode = var2.getInitialAnimationMode();
      var2.setAnimationMode(this.animationMode);
      var2.setAnimationSpeed(3);
      var2.setNumericFormat(var2.getInitialNumericFormat());
      this.timer = new Timer(this.delays[this.currentSpeedUnit - 1], this);
      var1.setSimulator(var2.getGUI());
      var1.setTitle(var2.getName() + getVersionString());
      File var5 = this.loadWorkingDir();
      var2.setWorkingDir(var5);
      var1.setWorkingDir(var5);
      var1.addControllerListener(this);
      var1.setSpeed(this.currentSpeedUnit);
      var1.setAnimationMode(this.animationMode);
      var1.setNumericFormat(var2.getInitialNumericFormat());
      var1.setAdditionalDisplay(var2.getInitialAdditionalDisplay());
      var1.setVariables(var2.getVariables());
      this.stopMode();
      var2.prepareGUI();
   }

   private void rewind() {
      try {
         if (this.scriptEnded || this.programHalted) {
            this.gui.enableSingleStep();
            this.gui.enableFastForward();
         }

         this.scriptEnded = false;
         this.programHalted = false;
         int var1 = this.animationMode;
         this.setAnimationMode(0);
         this.simulator.restart();
         this.refreshSimulator();
         this.setAnimationMode(var1);
         if (this.output != null) {
            this.resetOutputFile();
         }

         if (this.comparisonFile != null) {
            this.resetComparisonFile();
         }

         this.lastEcho = "";
         this.currentCommandIndex = 0;
         this.gui.setCurrentScriptLine(this.script.getLineNumberAt(0));
      } catch (ControllerException var2) {
         this.displayMessage(var2.getMessage(), true);
      }

   }

   private void stopMode() {
      if (this.fastForwardRunning) {
         if (this.gui != null) {
            this.timer.stop();
            this.gui.enableLoadProgram();
            this.gui.enableSpeedSlider();
         }

         this.fastForwardRunning = false;
      }

      this.singleStepRunning = false;
      if (this.gui != null) {
         this.gui.enableSingleStep();
         this.gui.enableFastForward();
         this.gui.enableScript();
         this.gui.enableRewind();
         this.gui.disableStop();
         this.gui.enableAnimationModes();
         if (this.animationMode == 2) {
            this.gui.setCurrentScriptLine(this.script.getLineNumberAt(this.currentCommandIndex));
         }

         this.refreshSimulator();
      }

   }

   private void fastForward() {
      this.gui.enableStop();
      this.gui.disableSingleStep();
      this.gui.disableRewind();
      this.gui.disableScript();
      this.gui.disableFastForward();
      this.gui.disableAnimationModes();
      this.gui.disableLoadProgram();
      this.fastForwardRunning = true;
      this.simulator.prepareFastForward();
      if (this.animationMode != 2) {
         this.timer.start();
      } else {
         this.displayMessage("Running...", false);
         this.gui.disableSpeedSlider();
         Thread var1 = new Thread(this.fastForwardTask);
         var1.start();
      }

   }

   private synchronized void singleStep() {
      this.singleStepLocked = true;

      try {
         this.singleStepRunning = true;

         byte var1;
         do {
            var1 = this.miniStep();
         } while(var1 == 1 && this.singleStepRunning);

         this.singleStepRunning = false;
         if (var1 == 3) {
            this.displayMessage("Script reached a '!' terminator", false);
            this.stopMode();
         }

         for(int var2 = 0; var2 < this.breakpoints.size(); ++var2) {
            Breakpoint var3 = (Breakpoint)this.breakpoints.elementAt(var2);
            String var4 = this.simulator.getValue(var3.getVarName());
            if (var4.equals(var3.getValue())) {
               if (!var3.isReached()) {
                  var3.on();
                  this.gui.setBreakpoints(this.breakpoints);
                  this.displayMessage("Breakpoint reached", false);
                  this.gui.showBreakpoints();
                  this.stopMode();
               }
            } else if (var3.isReached()) {
               var3.off();
               this.gui.setBreakpoints(this.breakpoints);
            }
         }
      } catch (ControllerException var5) {
         this.stopWithError(var5);
      } catch (ProgramException var6) {
         this.stopWithError(var6);
      } catch (CommandException var7) {
         this.stopWithError(var7);
      } catch (VariableException var8) {
         this.stopWithError(var8);
      }

      this.singleStepLocked = false;
      this.notifyAll();
   }

   private void stopWithError(Exception var1) {
      this.displayMessage(var1.getMessage(), true);
      this.stopMode();
   }

   private byte miniStep() throws ControllerException, ProgramException, CommandException, VariableException {
      Command var1;
      boolean var2;
      do {
         var1 = this.script.getCommandAt(this.currentCommandIndex);
         var2 = false;
         switch(var1.getCode()) {
         case 1:
            this.simulator.doCommand((String[])var1.getArg());
            break;
         case 2:
            this.doOutputFileCommand(var1);
            break;
         case 3:
            this.doCompareToCommand(var1);
            break;
         case 4:
            this.doOutputListCommand(var1);
            break;
         case 5:
            this.doOutputCommand(var1);
            break;
         case 6:
            this.doBreakpointCommand(var1);
            break;
         case 7:
            this.doClearBreakpointsCommand(var1);
            break;
         case 8:
            this.repeatCounter = (Integer)var1.getArg();
            this.loopCommandIndex = this.currentCommandIndex + 1;
            var2 = true;
         case 9:
         case 11:
         default:
            break;
         case 10:
            this.whileCondititon = (ScriptCondition)var1.getArg();
            this.loopCommandIndex = this.currentCommandIndex + 1;
            if (!this.whileCondititon.compare(this.simulator)) {
               while(this.script.getCommandAt(this.currentCommandIndex).getCode() != 11) {
                  ++this.currentCommandIndex;
               }
            }

            var2 = true;
            break;
         case 12:
            this.scriptEnded = true;
            this.stopMode();
            if (this.gui != null) {
               this.gui.disableSingleStep();
               this.gui.disableFastForward();
            }

            try {
               if (this.output != null) {
                  this.output.close();
               }

               if (this.comparisonFile != null) {
                  if (this.comparisonFailed) {
                     this.displayMessage("End of script - Comparison failure at line " + this.comparisonFailureLine, true);
                  } else {
                     this.displayMessage("End of script - Comparison ended successfully", false);
                  }

                  this.comparisonFile.close();
               } else {
                  this.displayMessage("End of script", false);
               }
               break;
            } catch (IOException var4) {
               throw new ControllerException("Could not read comparison file");
            }
         case 13:
            this.doEchoCommand(var1);
            break;
         case 14:
            this.doClearEchoCommand(var1);
         }

         if (var1.getCode() != 12) {
            ++this.currentCommandIndex;
            Command var3 = this.script.getCommandAt(this.currentCommandIndex);
            if (var3.getCode() == 9) {
               if (this.repeatCounter != 0 && --this.repeatCounter <= 0) {
                  ++this.currentCommandIndex;
               } else {
                  this.currentCommandIndex = this.loopCommandIndex;
               }
            } else if (var3.getCode() == 11) {
               if (this.whileCondititon.compare(this.simulator)) {
                  this.currentCommandIndex = this.loopCommandIndex;
               } else {
                  ++this.currentCommandIndex;
               }
            }

            if (this.animationMode != 2) {
               this.gui.setCurrentScriptLine(this.script.getLineNumberAt(this.currentCommandIndex));
            }
         }
      } while(var2);

      return var1.getTerminator();
   }

   private void doOutputFileCommand(Command var1) throws ControllerException {
      this.currentOutputName = this.currentScriptFile.getParent() + "/" + (String)var1.getArg();
      this.resetOutputFile();
      if (this.gui != null) {
         this.gui.setOutputFile(this.currentOutputName);
      }

   }

   private void doCompareToCommand(Command var1) throws ControllerException {
      this.currentComparisonName = this.currentScriptFile.getParent() + "/" + (String)var1.getArg();
      this.resetComparisonFile();
      if (this.gui != null) {
         this.gui.setComparisonFile(this.currentComparisonName);
      }

   }

   private void doOutputListCommand(Command var1) throws ControllerException {
      if (this.output == null) {
         throw new ControllerException("No output file specified");
      } else {
         this.varList = (VariableFormat[])var1.getArg();
         StringBuffer var2 = new StringBuffer("|");

         for(int var3 = 0; var3 < this.varList.length; ++var3) {
            int var4 = this.varList[var3].padL + this.varList[var3].padR + this.varList[var3].len;
            String var5 = this.varList[var3].varName.length() > var4 ? this.varList[var3].varName.substring(0, var4) : this.varList[var3].varName;
            int var6 = (var4 - var5.length()) / 2;
            int var7 = var4 - var6 - var5.length();
            var2.append("                                        ".substring(0, var6) + var5 + "                                        ".substring(0, var7) + '|');
         }

         this.outputAndCompare(var2.toString());
      }
   }

   private void doOutputCommand(Command var1) throws ControllerException, VariableException {
      if (this.output == null) {
         throw new ControllerException("No output file specified");
      } else {
         StringBuffer var2 = new StringBuffer("|");

         for(int var3 = 0; var3 < this.varList.length; ++var3) {
            String var4 = this.simulator.getValue(this.varList[var3].varName);
            int var5;
            if (this.varList[var3].format != 'S') {
               try {
                  var5 = Integer.parseInt(var4);
               } catch (NumberFormatException var7) {
                  throw new VariableException("Variable is not numeric", this.varList[var3].varName);
               }

               if (this.varList[var3].format == 'X') {
                  var4 = Conversions.decimalToHex(var5, 4);
               } else if (this.varList[var3].format == 'B') {
                  var4 = Conversions.decimalToBinary(var5, 16);
               }
            }

            if (var4.length() > this.varList[var3].len) {
               var4 = var4.substring(var4.length() - this.varList[var3].len);
            }

            var5 = this.varList[var3].padL + (this.varList[var3].format == 'S' ? 0 : this.varList[var3].len - var4.length());
            int var6 = this.varList[var3].padR + (this.varList[var3].format == 'S' ? this.varList[var3].len - var4.length() : 0);
            var2.append("                                        ".substring(0, var5) + var4 + "                                        ".substring(0, var6) + '|');
         }

         this.outputAndCompare(var2.toString());
      }
   }

   private void doEchoCommand(Command var1) throws ControllerException {
      this.lastEcho = (String)var1.getArg();
      if (this.gui != null) {
         this.gui.displayMessage(this.lastEcho, false);
      }

   }

   private void doClearEchoCommand(Command var1) throws ControllerException {
      this.lastEcho = "";
      if (this.gui != null) {
         this.gui.displayMessage("", false);
      }

   }

   private void doBreakpointCommand(Command var1) throws ControllerException {
      Breakpoint var2 = (Breakpoint)var1.getArg();
      if (!this.breakpointExists(this.breakpoints, var2)) {
         this.breakpoints.addElement(var2);
         this.gui.setBreakpoints(this.breakpoints);
      }

   }

   private void doClearBreakpointsCommand(Command var1) throws ControllerException {
      this.breakpoints.removeAllElements();
      this.gui.setBreakpoints(this.breakpoints);
   }

   private static boolean compareLineWithTemplate(String var0, String var1) {
      if (var0.length() != var1.length()) {
         return false;
      } else {
         StringCharacterIterator var2 = new StringCharacterIterator(var0);
         StringCharacterIterator var3 = new StringCharacterIterator(var1);
         var2.first();
         var3.first();

         while(var2.current() != '\uffff') {
            if (var3.current() != '*' && var2.current() != var3.current()) {
               return false;
            }

            var2.next();
            var3.next();
         }

         return true;
      }
   }

   private void outputAndCompare(String var1) throws ControllerException {
      this.output.println(var1);
      this.output.flush();
      if (this.gui != null) {
         this.gui.outputFileUpdated();
         this.gui.setCurrentOutputLine(this.outputLinesCounter);
      }

      ++this.outputLinesCounter;
      if (this.comparisonFile != null) {
         try {
            String var2 = this.comparisonFile.readLine();
            if (this.gui != null) {
               this.gui.setCurrentComparisonLine(this.compareLinesCounter);
            }

            ++this.compareLinesCounter;
            if (!compareLineWithTemplate(var1, var2)) {
               this.comparisonFailed = true;
               this.comparisonFailureLine = this.compareLinesCounter;
               this.displayMessage("Comparison failure at line " + this.comparisonFailureLine, true);
               this.stopMode();
            }
         } catch (IOException var3) {
            throw new ControllerException("Could not read comparison file");
         }
      }

   }

   protected void loadNewScript(File var1, boolean var2) throws ControllerException, ScriptException {
      this.currentScriptFile = var1;
      this.script = new Script(var1.getPath());
      this.breakpoints.removeAllElements();
      this.currentCommandIndex = 0;
      this.output = null;
      this.currentOutputName = "";
      this.comparisonFile = null;
      this.currentComparisonName = "";
      if (this.gui != null) {
         this.gui.setOutputFile("");
         this.gui.setComparisonFile("");
         this.gui.setBreakpoints(this.breakpoints);
         this.gui.setScriptFile(var1.getPath());
         this.gui.setCurrentScriptLine(this.script.getLineNumberAt(0));
      }

      if (var2) {
         this.displayMessage("New script loaded: " + var1.getPath(), false);
      }

   }

   private void resetOutputFile() throws ControllerException {
      try {
         this.output = new PrintWriter(new FileWriter(this.currentOutputName));
         this.outputLinesCounter = 0;
         if (this.gui != null) {
            this.gui.setCurrentOutputLine(-1);
         }
      } catch (IOException var2) {
         throw new ControllerException("Could not create output file " + this.currentOutputName);
      }

      if (this.gui != null) {
         this.gui.setOutputFile(this.currentOutputName);
      }

   }

   private void resetComparisonFile() throws ControllerException {
      try {
         this.comparisonFile = new BufferedReader(new FileReader(this.currentComparisonName));
         this.compareLinesCounter = 0;
         this.comparisonFailed = false;
         if (this.gui != null) {
            this.gui.setCurrentComparisonLine(-1);
         }

      } catch (IOException var2) {
         throw new ControllerException("Could not open comparison file " + this.currentComparisonName);
      }
   }

   private void setSpeed(int var1) {
      this.currentSpeedUnit = var1;
      this.timer.setDelay(this.delays[this.currentSpeedUnit - 1]);
      this.simulator.setAnimationSpeed(var1);
   }

   private void setAnimationMode(int var1) {
      this.simulator.setAnimationMode(var1);
      if (this.animationMode == 2 && var1 != 2) {
         this.simulator.refresh();
         this.gui.setCurrentScriptLine(this.script.getLineNumberAt(this.currentCommandIndex));
      }

      this.gui.setAnimationMode(var1);
      this.animationMode = var1;
   }

   private void setNumericFormat(int var1) {
      this.simulator.setNumericFormat(var1);
      this.gui.setNumericFormat(var1);
   }

   private void setAdditionalDisplay(int var1) {
      switch(var1) {
      case 0:
         this.simulator.getGUI().setAdditionalDisplay(this.gui.getScriptComponent());
         break;
      case 1:
         this.simulator.getGUI().setAdditionalDisplay(this.gui.getOutputComponent());
         break;
      case 2:
         this.simulator.getGUI().setAdditionalDisplay(this.gui.getComparisonComponent());
         break;
      case 3:
         this.simulator.getGUI().setAdditionalDisplay((JComponent)null);
      }

      this.gui.setAdditionalDisplay(var1);
   }

   private void setBreakpoints(Vector var1) {
      this.breakpoints = new Vector();

      for(int var2 = 0; var2 < var1.size(); ++var2) {
         Breakpoint var3 = (Breakpoint)var1.elementAt(var2);
         if (!this.breakpointExists(this.breakpoints, var3)) {
            this.breakpoints.addElement(var3);
         }
      }

   }

   private boolean breakpointExists(Vector var1, Breakpoint var2) {
      boolean var3 = false;

      for(int var4 = 0; var4 < var1.size() && !var3; ++var4) {
         Breakpoint var5 = (Breakpoint)var1.elementAt(var4);
         if (var2.getVarName().equals(var5.getVarName()) && var2.getValue().equals(var5.getValue())) {
            var3 = true;
         }
      }

      return var3;
   }

   private void refreshSimulator() {
      if (this.animationMode == 2) {
         this.simulator.setAnimationMode(0);
         this.simulator.refresh();
         this.simulator.setAnimationMode(2);
      }

   }

   private void displayMessage(String var1, boolean var2) {
      if (this.gui != null) {
         this.gui.displayMessage(var1, var2);
      } else if (var2) {
         System.err.println(var1);
         System.exit(-1);
      } else {
         System.out.println(var1);
      }

   }

   protected File loadWorkingDir() {
      String var1 = ".";

      try {
         BufferedReader var2 = new BufferedReader(new FileReader("bin/" + this.simulator.getName() + ".dat"));
         var1 = var2.readLine();
         var2.close();
      } catch (IOException var3) {
      }

      return new File(var1);
   }

   protected void saveWorkingDir(File var1) {
      File var2 = var1.getParentFile();
      if (this.gui != null) {
         this.gui.setWorkingDir(var2);
      }

      this.simulator.setWorkingDir(var1);
      File var3 = var1.isDirectory() ? var1 : var2;

      try {
         PrintWriter var4 = new PrintWriter(new FileWriter("bin/" + this.simulator.getName() + ".dat"));
         var4.println(var3.getAbsolutePath());
         var4.close();
      } catch (IOException var5) {
      }

   }

   private static String getVersionString() {
      return " (2.5)";
   }

   protected void reloadDefaultScript() {
      if (!this.currentScriptFile.equals(this.defaultScriptFile)) {
         this.gui.setAdditionalDisplay(3);

         try {
            this.loadNewScript(this.defaultScriptFile, false);
            this.rewind();
         } catch (ScriptException var2) {
         } catch (ControllerException var3) {
         }
      }

   }

   protected void updateProgramFile(String var1) {
      this.gui.setTitle(this.simulator.getName() + getVersionString() + " - " + var1);
      File var2 = new File(var1);
      this.saveWorkingDir(var2);
   }

   public void actionPerformed(ActionEvent var1) {
      if (!this.singleStepLocked) {
         Thread var2 = new Thread(this.singleStepTask);
         var2.start();
      }

   }

   public void programChanged(ProgramEvent var1) {
      switch(var1.getType()) {
      case 1:
         this.updateProgramFile(var1.getProgramFileName());
         if (!this.singleStepLocked) {
            this.reloadDefaultScript();
         }
         break;
      case 2:
         this.updateProgramFile(var1.getProgramFileName());
         break;
      case 3:
         this.gui.setTitle(this.simulator.getName() + getVersionString());
      }

   }

   public void actionPerformed(ControllerEvent var1) {
      try {
         Thread var2;
         switch(var1.getAction()) {
         case 1:
            this.displayMessage(this.lastEcho, true);
            this.gui.disableSingleStep();
            this.gui.disableFastForward();
            this.gui.disableScript();
            this.gui.disableRewind();
            this.gui.enableStop();
            var2 = new Thread(this.singleStepTask);
            var2.start();
            break;
         case 2:
            this.displayMessage(this.lastEcho, true);
            this.fastForward();
            break;
         case 3:
            this.setSpeed((Integer)var1.getData());
            break;
         case 4:
            if (this.animationMode == 2) {
               this.displayMessage("", false);
            }

            this.stopMode();
            break;
         case 5:
            this.setBreakpoints((Vector)var1.getData());
            break;
         case 6:
            File var3 = (File)var1.getData();
            this.loadNewScript(var3, true);
            this.setAdditionalDisplay(0);
            this.saveWorkingDir(var3);
            this.rewind();
            break;
         case 7:
         case 8:
         case 13:
         case 14:
         default:
            this.doUnknownAction(var1.getAction(), var1.getData());
            break;
         case 9:
            this.displayMessage("Script restarted", false);
            this.rewind();
            break;
         case 10:
            this.setAnimationModeTask.setMode((Integer)var1.getData());
            var2 = new Thread(this.setAnimationModeTask);
            var2.start();
            break;
         case 11:
            this.setNumericFormatTask.setFormat((Integer)var1.getData());
            var2 = new Thread(this.setNumericFormatTask);
            var2.start();
            break;
         case 12:
            this.setAdditionalDisplay((Integer)var1.getData());
            break;
         case 15:
            this.gui.disableAnimationModes();
            break;
         case 16:
            this.gui.enableAnimationModes();
            break;
         case 17:
            this.gui.disableSingleStep();
            break;
         case 18:
            this.gui.enableSingleStep();
            break;
         case 19:
            this.gui.disableFastForward();
            break;
         case 20:
            this.gui.enableFastForward();
            break;
         case 21:
            this.displayMessage("End of program", false);
            this.programHalted = true;
            if (this.fastForwardRunning) {
               this.stopMode();
            }

            this.gui.disableSingleStep();
            this.gui.disableFastForward();
            break;
         case 22:
            if (this.programHalted) {
               this.programHalted = false;
               this.gui.enableSingleStep();
               this.gui.enableFastForward();
            }
            break;
         case 23:
            this.gui.disableSingleStep();
            this.gui.disableFastForward();
            this.gui.disableRewind();
            break;
         case 24:
            this.gui.enableSingleStep();
            this.gui.enableFastForward();
            this.gui.enableRewind();
            break;
         case 25:
            this.displayMessage((String)var1.getData(), false);
            break;
         case 26:
            if (this.timer.isRunning()) {
               this.stopMode();
            }

            this.displayMessage((String)var1.getData(), true);
            break;
         case 27:
            this.simulator.loadProgram();
         }
      } catch (ScriptException var4) {
         this.displayMessage(var4.getMessage(), true);
         this.stopMode();
      } catch (ControllerException var5) {
         this.displayMessage(var5.getMessage(), true);
         this.stopMode();
      }

   }

   protected void doUnknownAction(byte var1, Object var2) throws ControllerException {
   }

   class SetNumericFormatTask implements Runnable {
      private int numericFormat;

      public void setFormat(int var1) {
         this.numericFormat = var1;
      }

      public void run() {
         HackController.this.setNumericFormat(this.numericFormat);
      }
   }

   class SetAnimationModeTask implements Runnable {
      private int animationMode;

      public void setMode(int var1) {
         this.animationMode = var1;
      }

      public void run() {
         HackController.this.setAnimationMode(this.animationMode);
      }
   }

   class FastForwardTask implements Runnable {
      public synchronized void run() {
         try {
            System.runFinalization();
            System.gc();
            this.wait(300L);
         } catch (InterruptedException var5) {
         }

         int var1 = 0;

         for(int var2 = HackController.FASTFORWARD_SPEED_FUNCTION[HackController.this.currentSpeedUnit - 1]; HackController.this.fastForwardRunning; ++var1) {
            HackController.this.singleStep();
            if (var1 == var2) {
               var1 = 0;

               try {
                  this.wait(1L);
               } catch (InterruptedException var4) {
               }
            }
         }

      }
   }

   class SingleStepTask implements Runnable {
      public void run() {
         HackController.this.singleStep();
         if (!HackController.this.fastForwardRunning) {
            if (!HackController.this.scriptEnded && !HackController.this.programHalted) {
               HackController.this.gui.enableSingleStep();
               HackController.this.gui.enableFastForward();
               HackController.this.gui.disableStop();
            }

            HackController.this.gui.enableScript();
            HackController.this.gui.enableRewind();
         }

         if (HackController.this.animationMode == 2) {
            HackController.this.refreshSimulator();
            HackController.this.gui.setCurrentScriptLine(HackController.this.script.getLineNumberAt(HackController.this.currentCommandIndex));
         }

      }
   }
}
