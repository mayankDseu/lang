package Hack.VMEmulator;

import Hack.CPUEmulator.Keyboard;
import Hack.CPUEmulator.KeyboardGUI;
import Hack.CPUEmulator.RAM;
import Hack.CPUEmulator.ScreenGUI;
import Hack.ComputerParts.AbsolutePointedMemorySegment;
import Hack.ComputerParts.Bus;
import Hack.ComputerParts.BusGUI;
import Hack.ComputerParts.ComputerPartErrorEvent;
import Hack.ComputerParts.ComputerPartErrorEventListener;
import Hack.ComputerParts.LabeledPointedMemoryGUI;
import Hack.ComputerParts.MemorySegment;
import Hack.ComputerParts.MemorySegmentGUI;
import Hack.ComputerParts.PointedMemoryGUI;
import Hack.ComputerParts.PointedMemorySegmentGUI;
import Hack.ComputerParts.TrimmedAbsoluteMemorySegment;
import Hack.Controller.CommandException;
import Hack.Controller.HackSimulator;
import Hack.Controller.HackSimulatorGUI;
import Hack.Controller.ProgramException;
import Hack.Controller.VariableException;
import Hack.Events.ProgramEvent;
import Hack.Utilities.Conversions;
import java.io.File;

public class VMEmulator extends HackSimulator implements ComputerPartErrorEventListener {
   private static final File INITIAL_BUILTIN_DIR = new File("builtInVMCode");
   private static final String VAR_SP = "sp";
   private static final String VAR_RAM = "RAM";
   private static final String VAR_LOCAL = "local";
   private static final String VAR_ARGUMENT = "argument";
   private static final String VAR_THIS = "this";
   private static final String VAR_THAT = "that";
   private static final String VAR_TEMP = "temp";
   private static final String VAR_LINE = "line";
   private static final String VAR_CURRENT_FUNCTION = "currentFunction";
   private static final String COMMAND_VMSTEP = "vmstep";
   private static final String COMMAND_ROMLOAD = "load";
   private static final String COMMAND_SETVAR = "set";
   private CPU cpu;
   private VMEmulatorGUI gui;
   private String[] vars;
   private Keyboard keyboard;
   private int animationMode;

   public VMEmulator() {
      VMProgram var1 = new VMProgram((VMProgramGUI)null);
      MemorySegment[][] var2 = new MemorySegment[24577][];
      RAM var3 = new RAM((PointedMemoryGUI)null, var2, (ScreenGUI)null);
      var3.addErrorListener(this);
      var3.reset();
      AbsolutePointedMemorySegment var4 = new AbsolutePointedMemorySegment(var3, (PointedMemorySegmentGUI)null);
      TrimmedAbsoluteMemorySegment var5 = new TrimmedAbsoluteMemorySegment(var3, (PointedMemorySegmentGUI)null);
      MemorySegment var6 = new MemorySegment(var3, (MemorySegmentGUI)null);
      MemorySegment var7 = new MemorySegment(var3, (MemorySegmentGUI)null);
      MemorySegment var8 = new MemorySegment(var3, (MemorySegmentGUI)null);
      MemorySegment var9 = new MemorySegment(var3, (MemorySegmentGUI)null);
      MemorySegment var10 = new MemorySegment(var3, (MemorySegmentGUI)null);
      MemorySegment var11 = new MemorySegment(var3, (MemorySegmentGUI)null);
      var4.reset();
      var4.addErrorListener(this);
      var4.setEnabledRange(256, 2047, true);
      var5.reset();
      var5.addErrorListener(this);
      var5.setEnabledRange(256, 2047, true);
      var6.reset();
      var6.addErrorListener(this);
      var7.reset();
      var7.addErrorListener(this);
      var8.reset();
      var8.addErrorListener(this);
      var9.reset();
      var9.addErrorListener(this);
      var10.reset();
      var10.addErrorListener(this);
      var11.reset();
      var11.addErrorListener(this);
      var11.setStartAddress(5);
      var11.setEnabledRange(5, 12, true);
      var2[0] = new MemorySegment[]{var4, var5};
      var2[1] = new MemorySegment[]{var7};
      var2[2] = new MemorySegment[]{var8};
      var2[3] = new MemorySegment[]{var9};
      var2[4] = new MemorySegment[]{var10};
      this.keyboard = new Keyboard(var3, (KeyboardGUI)null);
      this.keyboard.reset();
      CallStack var12 = new CallStack((CallStackGUI)null);
      var12.reset();
      Calculator var13 = new Calculator((CalculatorGUI)null);
      var13.reset();
      Bus var14 = new Bus((BusGUI)null);
      var14.reset();
      this.cpu = new CPU(var1, var3, var12, var13, var14, var4, var5, var6, var7, var8, var9, var10, var11, INITIAL_BUILTIN_DIR);
      this.cpu.boot();
      this.init();
   }

   public VMEmulator(VMEmulatorGUI var1) {
      this.gui = var1;
      VMProgram var2 = new VMProgram(var1.getProgram());
      var2.addErrorListener(this);
      var2.addProgramListener(this);
      MemorySegment[][] var3 = new MemorySegment[24577][];
      RAM var4 = new RAM(var1.getRAM(), var3, var1.getScreen());
      var4.addErrorListener(this);
      var4.reset();
      LabeledPointedMemoryGUI var5 = var1.getRAM();
      var5.setLabel(0, "SP");
      var5.setLabel(1, "LCL");
      var5.setLabel(2, "ARG");
      var5.setLabel(3, "THIS");
      var5.setLabel(4, "THAT");
      var5.setLabel(5, "Temp0");
      var5.setLabel(6, "Temp1");
      var5.setLabel(7, "Temp2");
      var5.setLabel(8, "Temp3");
      var5.setLabel(9, "Temp4");
      var5.setLabel(10, "Temp5");
      var5.setLabel(11, "Temp6");
      var5.setLabel(12, "Temp7");
      var5.setLabel(13, "R13");
      var5.setLabel(14, "R14");
      var5.setLabel(15, "R15");
      AbsolutePointedMemorySegment var6 = new AbsolutePointedMemorySegment(var4, var1.getStack());
      TrimmedAbsoluteMemorySegment var7 = new TrimmedAbsoluteMemorySegment(var4, var1.getWorkingStack());
      MemorySegment var8 = new MemorySegment(var4, var1.getStaticSegment());
      MemorySegment var9 = new MemorySegment(var4, var1.getLocalSegment());
      MemorySegment var10 = new MemorySegment(var4, var1.getArgSegment());
      MemorySegment var11 = new MemorySegment(var4, var1.getThisSegment());
      MemorySegment var12 = new MemorySegment(var4, var1.getThatSegment());
      MemorySegment var13 = new MemorySegment(var4, var1.getTempSegment());
      var6.reset();
      var6.setEnabledRange(256, 2047, true);
      var6.addErrorListener(this);
      var7.reset();
      var7.setEnabledRange(256, 2047, true);
      var7.addErrorListener(this);
      var8.reset();
      var8.addErrorListener(this);
      var9.reset();
      var9.addErrorListener(this);
      var10.reset();
      var10.addErrorListener(this);
      var11.reset();
      var11.addErrorListener(this);
      var12.reset();
      var12.addErrorListener(this);
      var13.reset();
      var13.setStartAddress(5);
      var13.setEnabledRange(5, 12, true);
      var13.addErrorListener(this);
      var3[0] = new MemorySegment[]{var6, var7};
      var3[1] = new MemorySegment[]{var9};
      var3[2] = new MemorySegment[]{var10};
      var3[3] = new MemorySegment[]{var11};
      var3[4] = new MemorySegment[]{var12};
      this.keyboard = new Keyboard(var4, var1.getKeyboard());
      this.keyboard.reset();
      CallStack var14 = new CallStack(var1.getCallStack());
      var14.reset();
      Calculator var15 = new Calculator(var1.getCalculator());
      var15.hideCalculator();
      var15.reset();
      Bus var16 = new Bus(var1.getBus());
      var16.reset();
      this.cpu = new CPU(var2, var4, var14, var15, var16, var6, var7, var8, var9, var10, var11, var12, var13, INITIAL_BUILTIN_DIR);
      this.cpu.boot();
      this.init();
   }

   private void init() {
      this.vars = new String[]{"sp", "currentFunction", "line", "RAM[]", "local", "local[]", "argument", "argument[]", "this", "this[]", "that", "that[]", "temp[]", "RAM[]"};
   }

   public String getName() {
      return "Virtual Machine Emulator";
   }

   public String getValue(String var1) throws VariableException {
      if (var1.equals("local")) {
         return String.valueOf(this.cpu.getRAM().getValueAt(1));
      } else if (var1.equals("argument")) {
         return String.valueOf(this.cpu.getRAM().getValueAt(2));
      } else if (var1.equals("this")) {
         return String.valueOf(this.cpu.getRAM().getValueAt(3));
      } else if (var1.equals("that")) {
         return String.valueOf(this.cpu.getRAM().getValueAt(4));
      } else if (var1.equals("sp")) {
         return String.valueOf(this.cpu.getSP());
      } else if (var1.equals("currentFunction")) {
         return this.cpu.getCallStack().getTopFunction();
      } else if (var1.equals("line")) {
         return String.valueOf(this.cpu.getCallStack().getTopFunction() + "." + this.cpu.getCurrentInstruction().getIndexInFunction());
      } else {
         short var2;
         if (var1.startsWith("local[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getSegmentAt((short)0, var2));
         } else if (var1.startsWith("argument[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getSegmentAt((short)1, var2));
         } else if (var1.startsWith("this[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getSegmentAt((short)2, var2));
         } else if (var1.startsWith("that[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getSegmentAt((short)3, var2));
         } else if (var1.startsWith("temp[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getSegmentAt((short)4, var2));
         } else if (var1.startsWith("RAM[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getRAM().getValueAt(var2));
         } else {
            throw new VariableException("Unknown variable", var1);
         }
      }
   }

   public void setValue(String var1, String var2) throws VariableException {
      try {
         var2 = Conversions.toDecimalForm(var2);
         int var3;
         if (var1.equals("local")) {
            var3 = Integer.parseInt(var2);
            this.check_address(var1, var3);
            this.cpu.getRAM().setValueAt(1, (short)var3, false);
            if (this.gui != null) {
               this.gui.getLocalSegment().setEnabledRange(var3, 2047, true);
            }
         } else if (var1.equals("argument")) {
            var3 = Integer.parseInt(var2);
            this.check_address(var1, var3);
            this.cpu.getRAM().setValueAt(2, (short)var3, false);
            if (this.gui != null) {
               this.gui.getArgSegment().setEnabledRange(var3, 2047, true);
            }
         } else if (var1.equals("this")) {
            var3 = Integer.parseInt(var2);
            this.check_address(var1, var3);
            this.cpu.getRAM().setValueAt(3, (short)var3, false);
            if (this.gui != null) {
               this.gui.getThisSegment().setEnabledRange(var3, 16383, true);
            }
         } else if (var1.equals("that")) {
            var3 = Integer.parseInt(var2);
            this.check_address(var1, var3);
            this.cpu.getRAM().setValueAt(4, (short)var3, false);
            if (this.gui != null) {
               this.gui.getThatSegment().setEnabledRange(var3, 24576, true);
            }
         } else if (var1.equals("sp")) {
            var3 = Integer.parseInt(var2);
            this.check_address(var1, var3);
            this.cpu.setSP((short)var3);
         } else {
            if (var1.equals("currentFunction")) {
               throw new VariableException("Read Only variable", var1);
            }

            if (var1.equals("line")) {
               var3 = Integer.parseInt(var2);
               if (var3 >= this.cpu.getProgram().getSize()) {
                  throw new VariableException("Line " + var2 + "is not within the program range", var1);
               }

               this.cpu.getProgram().setPC((short)var3);
            } else {
               short var4;
               if (var1.startsWith("local[")) {
                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_value(var1, var3);
                  this.cpu.setSegmentAt((short)0, var4, (short)var3);
               } else if (var1.startsWith("argument[")) {
                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_value(var1, var3);
                  this.cpu.setSegmentAt((short)1, var4, (short)var3);
               } else if (var1.startsWith("this[")) {
                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_value(var1, var3);
                  this.cpu.setSegmentAt((short)2, var4, (short)var3);
               } else if (var1.startsWith("that[")) {
                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_value(var1, var3);
                  this.cpu.setSegmentAt((short)3, var4, (short)var3);
               } else if (var1.startsWith("temp[")) {
                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_value(var1, var3);
                  this.cpu.setSegmentAt((short)4, var4, (short)var3);
               } else {
                  if (!var1.startsWith("RAM[")) {
                     throw new VariableException("Unknown variable", var1);
                  }

                  var4 = getRamIndex(var1);
                  var3 = Integer.parseInt(var2);
                  this.check_address(var1, var4);
                  this.cpu.getRAM().setValueAt(var4, (short)var3, false);
               }
            }
         }

      } catch (NumberFormatException var5) {
         throw new VariableException("'" + var2 + "' is not a legal value for variable", var1);
      }
   }

   public void doCommand(String[] var1) throws CommandException, ProgramException, VariableException {
      if (var1.length == 0) {
         throw new CommandException("Empty command", var1);
      } else {
         if (this.animationMode != 2) {
            this.hideHighlightes();
         }

         if (var1[0].equals("vmstep")) {
            if (var1.length != 1) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            this.cpu.executeInstruction();
         } else if (var1[0].equals("set")) {
            if (var1.length != 3) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            this.setValue(var1[1], var1[2]);
         } else {
            if (!var1[0].equals("load")) {
               throw new CommandException("Unknown simulator command", var1);
            }

            if (var1.length != 1 && var1.length != 2) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            String var2 = this.workingDir + (var1.length == 1 ? "" : "/" + var1[1]);
            this.cpu.getProgram().loadProgram(var2);
            this.cpu.boot();
         }

      }
   }

   private void hideHighlightes() {
      this.cpu.getRAM().hideHighlight();
      this.cpu.getStack().hideHighlight();
      this.cpu.getWorkingStack().hideHighlight();
      this.cpu.getCalculator().hideHighlight();
      this.cpu.getStaticSegment().hideHighlight();
      MemorySegment[] var1 = this.cpu.getMemorySegments();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].hideHighlight();
      }

   }

   public void restart() {
      this.cpu.getRAM().reset();
      this.cpu.getCallStack().reset();
      this.cpu.getProgram().restartProgram();
      this.cpu.getStack().reset();
      this.cpu.getWorkingStack().reset();
      this.cpu.getCalculator().hideCalculator();
      this.cpu.getCalculator().reset();
      this.cpu.getStaticSegment().reset();
      MemorySegment[] var1 = this.cpu.getMemorySegments();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].reset();
      }

      this.cpu.boot();
   }

   public void setAnimationMode(int var1) {
      if (this.gui != null) {
         MemorySegment[] var2;
         int var3;
         ScreenGUI var7;
         if (var1 == 2 && this.animationMode != 2) {
            this.cpu.getRAM().disableUserInput();
            this.cpu.getStack().disableUserInput();
            this.cpu.getWorkingStack().disableUserInput();
            this.cpu.getStaticSegment().disableUserInput();
            var2 = this.cpu.getMemorySegments();

            for(var3 = 0; var3 < var2.length; ++var3) {
               var2[var3].disableUserInput();
            }

            var7 = this.gui.getScreen();
            if (var7 != null) {
               var7.startAnimation();
            }
         }

         if (var1 != 2 && this.animationMode == 2) {
            this.cpu.getRAM().enableUserInput();
            this.cpu.getStack().enableUserInput();
            this.cpu.getWorkingStack().enableUserInput();
            this.cpu.getStaticSegment().enableUserInput();
            var2 = this.cpu.getMemorySegments();

            for(var3 = 0; var3 < var2.length; ++var3) {
               var2[var3].enableUserInput();
            }

            var7 = this.gui.getScreen();
            if (var7 != null) {
               var7.stopAnimation();
            }
         }
      }

      this.animationMode = var1;
      boolean var6 = this.animationMode == 1;
      this.cpu.getBus().setAnimate(var6);
      this.cpu.getRAM().setAnimate(var6);
      this.cpu.getCallStack().setAnimate(var6);
      this.cpu.getProgram().setAnimate(var6);
      this.cpu.getStack().setAnimate(var6);
      this.cpu.getWorkingStack().setAnimate(var6);
      this.cpu.getCalculator().setAnimate(var6);
      this.cpu.getStaticSegment().setAnimate(var6);
      boolean var8 = this.animationMode != 2;
      this.cpu.getRAM().setDisplayChanges(var8);
      this.cpu.getCallStack().setDisplayChanges(var8);
      this.cpu.getProgram().setDisplayChanges(var8);
      this.cpu.getStack().setDisplayChanges(var8);
      this.cpu.getWorkingStack().setDisplayChanges(var8);
      this.cpu.getCalculator().setDisplayChanges(var8);
      this.cpu.getStaticSegment().setDisplayChanges(var8);
      MemorySegment[] var4 = this.cpu.getMemorySegments();

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var4[var5].setDisplayChanges(var8);
         var4[var5].setAnimate(var6);
      }

   }

   public int getInitialAnimationMode() {
      return 0;
   }

   public int getInitialNumericFormat() {
      return 0;
   }

   public void setNumericFormat(int var1) {
      this.cpu.getRAM().setNumericFormat(var1);
      this.cpu.getStack().setNumericFormat(var1);
      this.cpu.getWorkingStack().setNumericFormat(var1);
      this.cpu.getCalculator().setNumericFormat(var1);
   }

   public void setAnimationSpeed(int var1) {
      this.cpu.getBus().setAnimationSpeed(var1);
   }

   public void refresh() {
      this.cpu.getRAM().refreshGUI();
      this.cpu.getCallStack().refreshGUI();
      this.cpu.getProgram().refreshGUI();
      this.cpu.getStack().refreshGUI();
      this.cpu.getWorkingStack().refreshGUI();
      this.cpu.getCalculator().refreshGUI();
      this.cpu.getStaticSegment().refreshGUI();
      MemorySegment[] var1 = this.cpu.getMemorySegments();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2].refreshGUI();
      }

   }

   public void prepareFastForward() {
      this.gui.requestFocus();
      this.keyboard.requestFocus();
   }

   public void prepareGUI() {
   }

   public String[] getVariables() {
      return this.vars;
   }

   protected HackSimulatorGUI getGUI() {
      return this.gui;
   }

   public void computerPartErrorOccured(ComputerPartErrorEvent var1) {
      this.displayMessage(var1.getErrorMessage(), true);
   }

   public void programChanged(ProgramEvent var1) {
      super.programChanged(var1);
      if (var1.getType() == 1) {
         int var2 = this.animationMode;
         this.setAnimationMode(0);
         this.refresh();
         this.notifyListeners((byte)24, (Object)null);
         this.restart();
         this.setAnimationMode(var2);
      }

   }

   private static short getRamIndex(String var0) throws VariableException {
      if (var0.indexOf("]") == -1) {
         throw new VariableException("Missing ']'", var0);
      } else {
         String var1 = var0.substring(var0.indexOf("[") + 1, var0.indexOf("]"));
         int var2 = Integer.parseInt(var1);
         if (var2 >= 0 && var2 < 24577) {
            return (short)var2;
         } else {
            throw new VariableException("Illegal variable index", var0);
         }
      }
   }

   private void check_value(String var1, int var2) throws VariableException {
      if (var2 < -32768 || var2 >= 32768) {
         throw new VariableException(var2 + " is an illegal value for variable", var1);
      }
   }

   private void check_address(String var1, int var2) throws VariableException {
      if (var2 < 0 || var2 >= 24577) {
         throw new VariableException(var2 + " is an illegal value for", var1);
      }
   }
}
