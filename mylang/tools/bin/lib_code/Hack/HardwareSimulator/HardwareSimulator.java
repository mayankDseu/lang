package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPartErrorEvent;
import Hack.ComputerParts.TextFileEvent;
import Hack.ComputerParts.TextFileEventListener;
import Hack.Controller.CommandException;
import Hack.Controller.HackSimulator;
import Hack.Controller.HackSimulatorGUI;
import Hack.Controller.ProgramException;
import Hack.Controller.VariableException;
import Hack.Gates.BuiltInGateClass;
import Hack.Gates.BuiltInGateWithGUI;
import Hack.Gates.CompositeGate;
import Hack.Gates.CompositeGateClass;
import Hack.Gates.DirtyGateListener;
import Hack.Gates.Gate;
import Hack.Gates.GateClass;
import Hack.Gates.GateErrorEvent;
import Hack.Gates.GateErrorEventListener;
import Hack.Gates.GateException;
import Hack.Gates.GatesManager;
import Hack.Gates.HDLException;
import Hack.Gates.Node;
import Hack.Utilities.Conversions;
import java.io.File;

public class HardwareSimulator extends HackSimulator implements TextFileEventListener, GateErrorEventListener, DirtyGateListener {
   private static final String VAR_TIME = "time";
   private static final String COMMAND_TICK = "tick";
   private static final String COMMAND_TOCK = "tock";
   private static final String COMMAND_LOAD = "load";
   private static final String COMMAND_EVAL = "eval";
   private static final String COMMAND_SETVAR = "set";
   private static final File INITIAL_BUILTIN_DIR = new File("builtInChips");
   private static final short NULL_VALUE = 0;
   private HardwareSimulatorGUI gui;
   private Gate gate;
   private Pins inputPins;
   private Pins outputPins;
   private Pins internalPins;
   private PartPins partPins;
   private Parts parts;
   private boolean clockUp;
   private int time;
   private int animationMode;
   private String[] vars;

   public HardwareSimulator() {
      this.init();
      GatesManager.getInstance().enableChipsGUI(false);
   }

   public HardwareSimulator(HardwareSimulatorGUI var1) {
      this.gui = var1;
      this.init();
      if (var1.getGatesPanel() != null) {
         GatesManager.getInstance().setGatesPanel(var1.getGatesPanel());
      }

      this.inputPins = new Pins((byte)1, var1.getInputPins());
      this.outputPins = new Pins((byte)2, var1.getOutputPins());
      this.internalPins = new Pins((byte)3, var1.getInternalPins());
      this.partPins = new PartPins(var1.getPartPins());
      this.parts = new Parts(var1.getParts());
      this.inputPins.enableUserInput();
      this.inputPins.setNullValue((short)0, false);
      this.inputPins.addErrorListener(this);
      this.outputPins.disableUserInput();
      this.outputPins.setNullValue((short)0, false);
      this.internalPins.disableUserInput();
      this.internalPins.setNullValue((short)0, false);
      this.partPins.setNullValue((short)0, false);
      if (var1.getHDLView() != null) {
         var1.getHDLView().addTextFileListener(this);
      }

      if (var1.getGateInfo() != null) {
         var1.getGateInfo().reset();
      }

      var1.hideInternalPins();
      var1.hidePartPins();
      var1.hideParts();
   }

   private void init() {
      Gate.CLOCK_NODE.set((short)1);
      this.clockUp = false;
      this.time = 0;
      GatesManager.getInstance().setErrorHandler(this);
      GatesManager.getInstance().setBuiltInDir(INITIAL_BUILTIN_DIR);
      this.vars = new String[]{"time"};
   }

   public String getName() {
      return "Hardware Simulator";
   }

   public String getValue(String var1) throws VariableException {
      String var2 = null;
      if (this.gate == null) {
         throw new VariableException("cannot get var's value since no gate is currently loaded", var1);
      } else {
         if (var1.equals("time")) {
            var2 = this.time + (this.clockUp ? "+" : " ");
         } else {
            Node var3 = this.gate.getNode(var1);
            if (var3 != null) {
               var2 = String.valueOf(var3.get());
            } else {
               String var4 = this.getVarChipName(var1);
               if (var4 != null) {
                  int var5 = this.getVarIndex(var1);
                  BuiltInGateWithGUI var6 = this.getGUIChip(var4);
                  if (var6 == null) {
                     throw new VariableException("No such built-in chip used", var4);
                  }

                  try {
                     var2 = String.valueOf(var6.getValueAt(var5));
                  } catch (GateException var8) {
                     throw new VariableException(var8.getMessage(), var1);
                  }
               }

               if (var2 == null) {
                  throw new VariableException("Unknown variable", var1);
               }
            }
         }

         return var2;
      }
   }

   private BuiltInGateWithGUI getGUIChip(String var1) {
      BuiltInGateWithGUI var2 = null;
      BuiltInGateWithGUI[] var3 = GatesManager.getInstance().getChips();

      for(int var4 = 0; var4 < var3.length && var2 == null; ++var4) {
         if (var3[var4].getGateClass().getName().equals(var1)) {
            var2 = var3[var4];
         }
      }

      return var2;
   }

   private String getVarChipName(String var1) {
      String var2 = null;
      int var3 = var1.indexOf("[");
      if (var3 >= 0) {
         var2 = var1.substring(0, var3);
      }

      return var2;
   }

   private int getVarIndex(String var1) throws VariableException {
      int var2;
      if (var1.endsWith("[]")) {
         var2 = 0;
      } else {
         try {
            var2 = Integer.parseInt(var1.substring(var1.indexOf("[") + 1, var1.indexOf("]")));
         } catch (Exception var4) {
            throw new VariableException("Illegal component index", var1);
         }
      }

      return var2;
   }

   public void setValue(String var1, String var2) throws VariableException {
      if (this.gate == null) {
         throw new VariableException("cannot get var's value since no gate is currently loaded", var1);
      } else {
         short var3;
         try {
            var2 = Conversions.toDecimalForm(var2);
            var3 = Short.parseShort(var2);
         } catch (NumberFormatException var12) {
            throw new VariableException("'" + var2 + "' is not a legal value for variable", var1);
         }

         boolean var4 = false;
         if (var1.equals("time")) {
            var4 = true;
         } else {
            Node var5 = this.gate.getNode(var1);
            if (var5 != null) {
               byte var6 = this.gate.getGateClass().getPinType(var1);
               if (var6 != 1) {
                  var4 = true;
               } else {
                  if (!this.isLegalWidth(var1, var3)) {
                     throw new VariableException(var2 + " doesn't fit in the pin's width", var1);
                  }

                  var5.set(var3);
               }
            } else {
               boolean var13 = false;
               String var7 = this.getVarChipName(var1);
               if (var7 != null) {
                  int var8 = this.getVarIndex(var1);
                  BuiltInGateWithGUI var9 = this.getGUIChip(var7);
                  if (var9 == null) {
                     throw new VariableException("No such built-in chip used", var7);
                  }

                  try {
                     var9.setValueAt(var8, var3);
                     var13 = true;
                  } catch (GateException var11) {
                     throw new VariableException(var11.getMessage(), var1);
                  }
               }

               if (!var13) {
                  throw new VariableException("Unknown variable", var1);
               }
            }
         }

         if (var4) {
            throw new VariableException("Read Only variable", var1);
         }
      }
   }

   private boolean isLegalWidth(String var1, short var2) {
      byte var3 = this.gate.getGateClass().getPinInfo(var1).width;
      byte var4 = (byte)(var2 > 0 ? (int)(Math.log((double)var2) / Math.log(2.0D)) + 1 : 1);
      return var4 <= var3;
   }

   public void doCommand(String[] var1) throws CommandException, ProgramException, VariableException {
      if (var1.length == 0) {
         throw new CommandException("Empty command", var1);
      } else {
         if (var1[0].equals("tick")) {
            if (var1.length != 1) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            if (this.gate == null) {
               throw new CommandException("Illegal command since no gate is currently loaded", var1);
            }

            if (this.clockUp) {
               throw new CommandException("Illegal command since clock is already up", var1);
            }

            this.performTick();
         } else if (var1[0].equals("tock")) {
            if (var1.length != 1) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            if (this.gate == null) {
               throw new CommandException("Illegal command since no gate is currently loaded", var1);
            }

            if (!this.clockUp) {
               throw new CommandException("Illegal command since clock is already down", var1);
            }

            this.performTock();
         } else if (var1[0].equals("eval")) {
            if (var1.length != 1) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            if (this.gate == null) {
               throw new CommandException("Illegal command since no gate is currently loaded", var1);
            }

            this.performEval();
         } else if (var1[0].equals("set")) {
            if (var1.length != 3) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            this.setValue(var1[1], var1[2]);
         } else if (var1[0].equals("load")) {
            if (var1.length != 2) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            if (this.gui != null && this.gui.getGateInfo() != null) {
               this.gui.getGateInfo().setChip(var1[1]);
            }

            try {
               if (!var1[1].endsWith(".hdl")) {
                  throw new CommandException("A .hdl file is expected", var1);
               }

               if (var1[1].indexOf("/") >= 0) {
                  throw new CommandException("The gate name must not contain path specification", var1);
               }

               String var2 = var1[1].substring(0, var1[1].length() - 4);
               this.loadGate(var2, false);
               this.notifyProgramListeners((byte)1, GatesManager.getInstance().getHDLFileName(var2));
            } catch (GateException var7) {
               throw new CommandException(var7.getMessage(), var1);
            }
         } else {
            boolean var8 = false;
            BuiltInGateWithGUI var3 = this.getGUIChip(var1[0]);
            if (var3 != null) {
               var8 = true;
               String[] var4 = new String[var1.length - 1];
               System.arraycopy(var1, 1, var4, 0, var4.length);

               try {
                  var3.doCommand(var4);
               } catch (GateException var6) {
                  throw new CommandException(var6.getMessage(), var1);
               }
            }

            if (!var8) {
               throw new CommandException("Unknown command or component name", var1);
            }
         }

      }
   }

   public void setWorkingDir(File var1) {
      super.setWorkingDir(var1);
      GatesManager.getInstance().setWorkingDir(var1.getParentFile());
   }

   private void hideHighlightes() {
      this.inputPins.hideHighlight();
      this.outputPins.hideHighlight();
      this.internalPins.hideHighlight();
      this.partPins.hideHighlight();
   }

   public void setAnimationSpeed(int var1) {
   }

   public void refresh() {
      this.inputPins.refreshGUI();
      this.outputPins.refreshGUI();
      this.internalPins.refreshGUI();
      this.partPins.refreshGUI();
      this.parts.refreshGUI();
   }

   public void restart() {
      if (this.gui != null) {
         this.inputPins.reset();
         this.outputPins.reset();
         this.internalPins.reset();
         this.partPins.reset();
         this.parts.reset();
         if (this.gui.getHDLView() != null) {
            this.gui.getHDLView().hideSelect();
            this.gui.getHDLView().clearHighlights();
         }

         if (this.gui.getGateInfo() != null) {
            this.gui.getGateInfo().reset();
         }

         this.hideHighlightes();
      }

      if (this.gate != null) {
         this.gate.eval();
      }

      this.time = 0;
      Gate.CLOCK_NODE.set((short)1);
      this.clockUp = false;
   }

   public String[] getVariables() {
      return this.vars;
   }

   public void setAnimationMode(int var1) {
      if (this.gui != null) {
         if (var1 == 2 && this.animationMode != 2) {
            this.inputPins.disableUserInput();
         }

         if (var1 != 2 && this.animationMode == 2) {
            this.inputPins.enableUserInput();
         }

         this.animationMode = var1;
         boolean var2 = this.animationMode == 1;
         this.inputPins.setAnimate(var2);
         this.outputPins.setAnimate(var2);
         this.internalPins.setAnimate(var2);
         this.partPins.setAnimate(var2);
         boolean var3 = this.animationMode != 2;
         this.inputPins.setDisplayChanges(var3);
         this.outputPins.setDisplayChanges(var3);
         this.internalPins.setDisplayChanges(var3);
         this.partPins.setDisplayChanges(var3);
      }

   }

   public int getInitialAnimationMode() {
      return 0;
   }

   public int getInitialNumericFormat() {
      return 0;
   }

   public void setNumericFormat(int var1) {
      this.inputPins.setNumericFormat(var1);
      this.outputPins.setNumericFormat(var1);
      this.internalPins.setNumericFormat(var1);
      this.partPins.setNumericFormat(var1);
   }

   public void prepareFastForward() {
   }

   public void prepareGUI() {
   }

   protected HackSimulatorGUI getGUI() {
      return this.gui;
   }

   protected synchronized void loadGate(String var1, boolean var2) throws GateException {
      GateClass var3 = null;
      if (this.gui != null) {
         this.displayMessage("Loading chip...", false);
      }

      try {
         GateClass.clearGateCache();
         var3 = GateClass.getGateClass(var1, var2);
         GatesManager.getInstance().removeAllChips();
         Gate var4 = this.gate;
         this.gate = var3.newInstance();
         this.gate.addDirtyGateListener(this);
         if (var4 != null) {
            var4.removeDirtyGateListener(this);
         }

         if (this.gui != null) {
            this.inputPins.setNodes(this.gate.getInputNodes(), var3);
            this.outputPins.setNodes(this.gate.getOutputNodes(), var3);
            if (var3 instanceof CompositeGateClass) {
               this.internalPins.setNodes(((CompositeGate)this.gate).getInternalNodes(), var3);
               this.partPins.setGate(this.gate);
               this.parts.setParts(((CompositeGate)this.gate).getParts());
            }
         }

         this.restart();
         if (this.gui != null) {
            if (this.gui.getGateInfo() != null) {
               this.gui.getGateInfo().setChip(var3.getName());
            }

            this.notifyListeners((byte)107, (Object)null);
            this.gui.getOutputPins().setDimmed(false);
            this.gui.getInternalPins().setDimmed(false);
            if (var3.isClocked()) {
               this.notifyListeners((byte)106, (Object)null);
               if (this.gui.getGateInfo() != null) {
                  this.gui.getGateInfo().setClocked(var3.isClocked());
                  this.gui.getGateInfo().enableTime();
               }
            } else {
               this.notifyListeners((byte)105, (Object)null);
               if (this.gui.getGateInfo() != null) {
                  this.gui.getGateInfo().disableTime();
               }
            }

            if (this.gui.getHDLView() != null) {
               if (var2) {
                  this.gui.getHDLView().setContents(var1);
               } else {
                  this.gui.getHDLView().setContents(GatesManager.getInstance().getHDLFileName(var1));
               }
            }

            if (var3 instanceof BuiltInGateClass) {
               this.gui.hideInternalPins();
            } else {
               this.gui.showInternalPins();
            }

            this.gui.hidePartPins();
            this.gui.hideParts();
         }
      } catch (HDLException var5) {
         throw new GateException(var5.getMessage());
      } catch (InstantiationException var6) {
         throw new GateException(var6.getMessage());
      }

      if (this.gui != null) {
         this.clearMessage();
      }

   }

   public void rowSelected(TextFileEvent var1) {
      if (this.gate instanceof CompositeGate) {
         String var2 = var1.getRowString();
         boolean var3 = false;
         boolean var4 = false;

         try {
            HDLLineTokenizer var5 = new HDLLineTokenizer(var2);
            if (var5.hasMoreTokens()) {
               var5.advance();
               if (var5.getTokenType() == 1 && var5.getKeywordType() == 6) {
                  var4 = true;
               } else if (var5.getTokenType() == 3) {
                  String var6 = var5.getIdentifier();
                  var5.advance();
                  if (var5.getTokenType() == 2 && var5.getSymbol() == '(' && GateClass.gateClassExists(var6)) {
                     GateClass var7 = GateClass.getGateClass(var6, false);
                     this.partPins.setPart(var7, var6);
                     var3 = true;
                     boolean var8 = false;

                     while(!var8) {
                        var5.advance();
                        String var9 = var5.getIdentifier();
                        var5.advance();
                        var5.advance();
                        String var10 = var5.getIdentifier();
                        this.partPins.addPin(var9, var10);
                        var5.advance();
                        if (var5.getTokenType() == 2 && var5.getSymbol() == ')') {
                           var8 = true;
                        }
                     }
                  }
               }
            }
         } catch (HDLException var11) {
            this.displayMessage(var11.getMessage(), true);
         }

         if (var3) {
            this.gui.hideInternalPins();
            this.gui.hideParts();
            this.gui.showPartPins();
         } else if (var4) {
            this.gui.hideInternalPins();
            this.gui.hidePartPins();
            this.gui.showParts();
         } else {
            this.gui.hidePartPins();
            this.gui.hideParts();
            this.gui.showInternalPins();
         }
      }

   }

   public void computerPartErrorOccured(ComputerPartErrorEvent var1) {
      this.displayMessage(var1.getErrorMessage(), true);
   }

   public void gateErrorOccured(GateErrorEvent var1) {
      this.displayMessage(var1.getErrorMessage(), true);
   }

   public void gotDirty() {
      this.notifyListeners((byte)108, (Object)null);
      if (this.gui != null) {
         this.gui.getOutputPins().setDimmed(true);
         this.gui.getInternalPins().setDimmed(true);
      }

   }

   public void gotClean() {
      this.notifyListeners((byte)107, (Object)null);
      if (this.gui != null) {
         this.gui.getOutputPins().setDimmed(false);
         this.gui.getInternalPins().setDimmed(false);
      }

   }

   private void performEval() {
      this.gate.eval();
   }

   protected void runEvalTask() {
      Thread var1 = new Thread(new HardwareSimulator.EvalTask());
      var1.start();
   }

   protected void runTickTockTask() {
      Thread var1;
      if (this.clockUp) {
         var1 = new Thread(new HardwareSimulator.TockTask());
         var1.start();
      } else {
         var1 = new Thread(new HardwareSimulator.TickTask());
         var1.start();
      }

   }

   private void performTick() {
      Gate.CLOCK_NODE.set((short)0);
      this.gate.tick();
      this.clockUp = true;
      if (this.gui != null) {
         if (this.animationMode != 2) {
            this.hideHighlightes();
         }

         this.updateTime();
      }

   }

   private void performTock() {
      Gate.CLOCK_NODE.set((short)1);
      this.gate.tock();
      this.clockUp = false;
      ++this.time;
      if (this.gui != null) {
         this.updateTime();
      }

   }

   private void updateTime() {
      if (this.gui != null && this.gui.getGateInfo() != null) {
         this.gui.getGateInfo().setClock(this.clockUp);
         if (!this.clockUp) {
            this.gui.getGateInfo().setTime(this.time);
         }
      }

   }

   private static short getIndex(String var0) throws VariableException {
      if (var0.indexOf("]") == -1) {
         throw new VariableException("Missing ']'", var0);
      } else {
         String var1 = var0.substring(var0.indexOf("[") + 1, var0.indexOf("]"));
         int var2 = Integer.parseInt(var1);
         if (var2 < 0) {
            throw new VariableException("Illegal variable index", var0);
         } else {
            return (short)var2;
         }
      }
   }

   public static String getFullPinName(String var0, byte[] var1) {
      StringBuffer var2 = new StringBuffer(var0);
      if (var1 != null && !var0.equals(CompositeGateClass.TRUE_NODE_INFO.name) && !var0.equals(CompositeGateClass.FALSE_NODE_INFO.name)) {
         var2.append("[");
         var2.append(var1[0]);
         if (var1[0] != var1[1]) {
            var2.append(".." + var1[1]);
         }

         var2.append("]");
      }

      return var2.toString();
   }

   class TockTask implements Runnable {
      public void run() {
         HardwareSimulator.this.performTock();
      }
   }

   class TickTask implements Runnable {
      public void run() {
         HardwareSimulator.this.performTick();
      }
   }

   class EvalTask implements Runnable {
      public void run() {
         HardwareSimulator.this.performEval();
      }
   }
}
