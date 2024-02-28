package Hack.CPUEmulator;

import Hack.ComputerParts.Bus;
import Hack.ComputerParts.BusGUI;
import Hack.ComputerParts.ComputerPartErrorEvent;
import Hack.ComputerParts.ComputerPartErrorEventListener;
import Hack.ComputerParts.MemorySegment;
import Hack.ComputerParts.PointedMemoryGUI;
import Hack.ComputerParts.Register;
import Hack.ComputerParts.RegisterGUI;
import Hack.Controller.CommandException;
import Hack.Controller.HackSimulator;
import Hack.Controller.HackSimulatorGUI;
import Hack.Controller.ProgramException;
import Hack.Controller.VariableException;
import Hack.Events.ProgramEvent;
import Hack.Utilities.Conversions;

public class CPUEmulator extends HackSimulator implements ComputerPartErrorEventListener {
   private static final String VAR_A = "A";
   private static final String VAR_D = "D";
   private static final String VAR_PC = "PC";
   private static final String VAR_RAM = "RAM";
   private static final String VAR_ROM = "ROM";
   private static final String VAR_TIME = "time";
   private static final String COMMAND_TICKTOCK = "ticktock";
   private static final String COMMAND_ROMLOAD = "load";
   private static final String COMMAND_SETVAR = "set";
   private CPU cpu;
   private CPUEmulatorGUI gui;
   private String[] vars;
   private Keyboard keyboard;
   private int animationMode;

   public CPUEmulator() {
      RAM var1 = new RAM((PointedMemoryGUI)null, (MemorySegment[][])null, (ScreenGUI)null);
      var1.reset();
      ROM var2 = new ROM((ROMGUI)null);
      var2.reset();
      PointerAddressRegisterAdapter var3 = new PointerAddressRegisterAdapter((RegisterGUI)null, var1);
      var3.reset();
      Register var4 = new Register((RegisterGUI)null);
      var4.reset();
      PointerAddressRegisterAdapter var5 = new PointerAddressRegisterAdapter((RegisterGUI)null, var2);
      var5.reset();
      this.keyboard = new Keyboard(var1, (KeyboardGUI)null);
      this.keyboard.reset();
      ALU var6 = new ALU((ALUGUI)null);
      var6.reset();
      Bus var7 = new Bus((BusGUI)null);
      var7.reset();
      this.cpu = new CPU(var1, var2, var3, var4, var5, var6, var7);
      this.init();
   }

   public CPUEmulator(CPUEmulatorGUI var1) {
      this.gui = var1;
      RAM var2 = new RAM(var1.getRAM(), (MemorySegment[][])null, var1.getScreen());
      var2.addErrorListener(this);
      var2.reset();
      ROM var3 = new ROM(var1.getROM());
      var3.addErrorListener(this);
      var3.addProgramListener(this);
      var3.reset();
      PointerAddressRegisterAdapter var4 = new PointerAddressRegisterAdapter(var1.getA(), var2);
      var4.addErrorListener(this);
      var4.reset();
      Register var5 = new Register(var1.getD());
      var5.addErrorListener(this);
      var5.reset();
      PointerAddressRegisterAdapter var6 = new PointerAddressRegisterAdapter(var1.getPC(), var3);
      var6.addErrorListener(this);
      var6.reset();
      this.keyboard = new Keyboard(var2, var1.getKeyboard());
      this.keyboard.reset();
      ALU var7 = new ALU(var1.getALU());
      var7.reset();
      Bus var8 = new Bus(var1.getBus());
      var8.reset();
      this.cpu = new CPU(var2, var3, var4, var5, var6, var7, var8);
      this.init();
   }

   private void init() {
      this.vars = new String[]{"A", "D", "PC", "RAM[]", "ROM[]", "time"};
   }

   public String getName() {
      return "CPU Emulator";
   }

   public String getValue(String var1) throws VariableException {
      if (var1.equals("A")) {
         return String.valueOf(this.cpu.getA().get());
      } else if (var1.equals("D")) {
         return String.valueOf(this.cpu.getD().get());
      } else if (var1.equals("PC")) {
         return String.valueOf(this.cpu.getPC().get());
      } else if (var1.equals("time")) {
         return String.valueOf(this.cpu.getTime());
      } else {
         short var2;
         if (var1.startsWith("RAM[")) {
            var2 = getRamIndex(var1);
            return String.valueOf(this.cpu.getRAM().getValueAt(var2));
         } else if (var1.startsWith("ROM[")) {
            var2 = getRomIndex(var1);
            return String.valueOf(this.cpu.getROM().getValueAt(var2));
         } else {
            throw new VariableException("Unknown variable", var1);
         }
      }
   }

   public void setValue(String var1, String var2) throws VariableException {
      try {
         var2 = Conversions.toDecimalForm(var2);
         int var3;
         if (var1.equals("A")) {
            var3 = Integer.parseInt(var2);
            this.check_ram_address(var1, var3);
            this.cpu.getA().store((short)var3);
         } else if (var1.equals("D")) {
            var3 = Integer.parseInt(var2);
            this.check_value(var1, var3);
            this.cpu.getD().store((short)var3);
         } else if (var1.equals("PC")) {
            var3 = Integer.parseInt(var2);
            this.check_rom_address(var1, var3);
            this.cpu.getPC().store((short)var3);
         } else {
            if (var1.equals("time")) {
               throw new VariableException("Read Only variable", var1);
            }

            short var4;
            if (var1.startsWith("RAM[")) {
               var4 = getRamIndex(var1);
               var3 = Integer.parseInt(var2);
               this.check_value(var1, var3);
               this.cpu.getRAM().setValueAt(var4, (short)var3, false);
            } else {
               if (!var1.startsWith("ROM[")) {
                  throw new VariableException("Unknown variable", var1);
               }

               var4 = getRomIndex(var1);
               var3 = Integer.parseInt(var2);
               this.check_value(var1, var3);
               this.cpu.getROM().setValueAt(var4, (short)var3, false);
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

         if (var1[0].equals("ticktock")) {
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

            if (var1.length != 2) {
               throw new CommandException("Illegal number of arguments to command", var1);
            }

            String var2 = this.workingDir.getAbsolutePath() + "/" + var1[1];
            this.cpu.getROM().loadProgram(var2);
            int var3 = this.animationMode;
            this.setAnimationMode(0);
            this.cpu.initProgram();
            this.setAnimationMode(var3);
         }

      }
   }

   private void hideHighlightes() {
      this.cpu.getRAM().hideHighlight();
      this.cpu.getROM().hideHighlight();
      this.cpu.getA().hideHighlight();
      this.cpu.getD().hideHighlight();
      this.cpu.getPC().hideHighlight();
      this.cpu.getALU().hideHighlight();
   }

   public void restart() {
      this.cpu.initProgram();
   }

   public void setAnimationMode(int var1) {
      if (this.gui != null) {
         ScreenGUI var2;
         if (var1 == 2 && this.animationMode != 2) {
            this.cpu.getRAM().disableUserInput();
            this.cpu.getROM().disableUserInput();
            this.cpu.getA().disableUserInput();
            this.cpu.getD().disableUserInput();
            this.cpu.getPC().disableUserInput();
            var2 = this.gui.getScreen();
            if (var2 != null) {
               var2.startAnimation();
            }
         }

         if (var1 != 2 && this.animationMode == 2) {
            this.cpu.getRAM().enableUserInput();
            this.cpu.getROM().enableUserInput();
            this.cpu.getA().enableUserInput();
            this.cpu.getD().enableUserInput();
            this.cpu.getPC().enableUserInput();
            var2 = this.gui.getScreen();
            if (var2 != null) {
               var2.stopAnimation();
            }
         }
      }

      this.animationMode = var1;
      boolean var4 = this.animationMode == 1;
      this.cpu.getBus().setAnimate(var4);
      this.cpu.getRAM().setAnimate(var4);
      this.cpu.getROM().setAnimate(var4);
      this.cpu.getA().setAnimate(var4);
      this.cpu.getD().setAnimate(var4);
      this.cpu.getPC().setAnimate(var4);
      this.cpu.getALU().setAnimate(var4);
      boolean var3 = this.animationMode != 2;
      this.cpu.getRAM().setDisplayChanges(var3);
      this.cpu.getROM().setDisplayChanges(var3);
      this.cpu.getA().setDisplayChanges(var3);
      this.cpu.getD().setDisplayChanges(var3);
      this.cpu.getPC().setDisplayChanges(var3);
      this.cpu.getALU().setDisplayChanges(var3);
   }

   public void setNumericFormat(int var1) {
      this.cpu.getRAM().setNumericFormat(var1);
      this.cpu.getA().setNumericFormat(var1);
      this.cpu.getD().setNumericFormat(var1);
      this.cpu.getPC().setNumericFormat(var1);
      this.cpu.getALU().setNumericFormat(var1);
   }

   public void setAnimationSpeed(int var1) {
      this.cpu.getBus().setAnimationSpeed(var1);
   }

   public int getInitialAnimationMode() {
      return 0;
   }

   public int getInitialNumericFormat() {
      return 0;
   }

   public void refresh() {
      this.cpu.getBus().refreshGUI();
      this.cpu.getRAM().refreshGUI();
      this.cpu.getROM().refreshGUI();
      this.cpu.getA().refreshGUI();
      this.cpu.getD().refreshGUI();
      this.cpu.getPC().refreshGUI();
      this.cpu.getALU().refreshGUI();
      ScreenGUI var1 = this.gui.getScreen();
      if (var1 != null) {
         var1.refresh();
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

   public void programChanged(ProgramEvent var1) {
      super.programChanged(var1);
      if (var1.getType() == 1) {
         int var2 = this.animationMode;
         this.setAnimationMode(0);
         this.refresh();
         this.notifyListeners((byte)24, (Object)null);
         this.cpu.initProgram();
         this.setAnimationMode(var2);
      }

   }

   public void computerPartErrorOccured(ComputerPartErrorEvent var1) {
      this.displayMessage(var1.getErrorMessage(), true);
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

   private static short getRomIndex(String var0) throws VariableException {
      if (var0.indexOf("]") == -1) {
         throw new VariableException("Missing ']'", var0);
      } else {
         String var1 = var0.substring(var0.indexOf("[") + 1, var0.indexOf("]"));
         int var2 = Integer.parseInt(var1);
         if (var2 >= 0 && var2 < 32768) {
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

   private void check_ram_address(String var1, int var2) throws VariableException {
      if (var2 < 0 || var2 >= 24577) {
         throw new VariableException(var2 + " is an illegal value for", var1);
      }
   }

   private void check_rom_address(String var1, int var2) throws VariableException {
      if (var2 < 0 || var2 >= 32768) {
         throw new VariableException(var2 + " is an illegal value for", var1);
      }
   }
}
