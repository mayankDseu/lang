package Hack.VMEmulator;

import Hack.CPUEmulator.RAM;
import Hack.ComputerParts.AbsolutePointedMemorySegment;
import Hack.ComputerParts.Bus;
import Hack.ComputerParts.MemorySegment;
import Hack.ComputerParts.PointedMemorySegment;
import Hack.ComputerParts.TrimmedAbsoluteMemorySegment;
import Hack.ComputerParts.ValueComputerPart;
import Hack.Controller.ProgramException;
import java.io.File;
import java.util.Vector;

public class CPU {
   private static final int MAIN_STACK = 1;
   private static final int METHOD_STACK = 2;
   private VMProgram program;
   private RAM ram;
   private CallStack callStack;
   private Calculator calculator;
   private Bus bus;
   private AbsolutePointedMemorySegment stackSegment;
   private TrimmedAbsoluteMemorySegment workingStackSegment;
   private MemorySegment staticSegment;
   private MemorySegment localSegment;
   private MemorySegment argSegment;
   private MemorySegment thisSegment;
   private MemorySegment thatSegment;
   private MemorySegment tempSegment;
   private MemorySegment[] segments;
   private Vector stackFrames;
   private VMEmulatorInstruction currentInstruction;
   private BuiltInFunctionsRunner builtInFunctionsRunner;

   public CPU(VMProgram var1, RAM var2, CallStack var3, Calculator var4, Bus var5, AbsolutePointedMemorySegment var6, TrimmedAbsoluteMemorySegment var7, MemorySegment var8, MemorySegment var9, MemorySegment var10, MemorySegment var11, MemorySegment var12, MemorySegment var13, File var14) {
      this.program = var1;
      this.ram = var2;
      this.callStack = var3;
      this.calculator = var4;
      this.bus = var5;
      this.stackSegment = var6;
      this.workingStackSegment = var7;
      this.staticSegment = var8;
      this.localSegment = var9;
      this.argSegment = var10;
      this.thisSegment = var11;
      this.thatSegment = var12;
      this.tempSegment = var13;
      this.segments = new MemorySegment[5];
      this.segments[0] = var9;
      this.segments[1] = var10;
      this.segments[2] = var11;
      this.segments[3] = var12;
      this.segments[4] = var13;
      this.stackFrames = new Vector();
      if (var1.getGUI() != null) {
         this.builtInFunctionsRunner = new BuiltInFunctionsRunner(this, var14);
      }

   }

   public void boot() {
      this.stackSegment.setStartAddress(256);
      this.workingStackSegment.setStartAddress(256);
      this.localSegment.setEnabledRange(256, 2047, true);
      this.argSegment.setEnabledRange(256, 2047, true);
      this.thisSegment.setEnabledRange(2048, 16383, true);
      this.thatSegment.setEnabledRange(2048, 24576, true);
      this.staticSegment.setStartAddress(16);
      this.staticSegment.setEnabledRange(16, 254, true);
      this.setSP((short)256);
      this.stackFrames.clear();
      if (this.builtInFunctionsRunner != null) {
         this.builtInFunctionsRunner.killAllRunningBuiltInFunctions();
      }

   }

   public Bus getBus() {
      return this.bus;
   }

   public RAM getRAM() {
      return this.ram;
   }

   public VMProgram getProgram() {
      return this.program;
   }

   public CallStack getCallStack() {
      return this.callStack;
   }

   public Calculator getCalculator() {
      return this.calculator;
   }

   public MemorySegment[] getMemorySegments() {
      return this.segments;
   }

   public PointedMemorySegment getStack() {
      return this.stackSegment;
   }

   public PointedMemorySegment getWorkingStack() {
      return this.workingStackSegment;
   }

   public VMEmulatorInstruction getCurrentInstruction() {
      return this.currentInstruction;
   }

   public void executeInstruction() throws ProgramException {
      this.currentInstruction = this.program.getNextInstruction();
      if (this.currentInstruction == null) {
         throw new ProgramException("No more instructions to execute");
      } else {
         switch(this.currentInstruction.getOpCode()) {
         case 1:
            this.add();
            break;
         case 2:
            this.substract();
            break;
         case 3:
            this.negate();
            break;
         case 4:
            this.equal();
            break;
         case 5:
            this.greaterThan();
            break;
         case 6:
            this.lessThan();
            break;
         case 7:
            this.and();
            break;
         case 8:
            this.or();
            break;
         case 9:
            this.not();
            break;
         case 10:
            this.push(this.currentInstruction.getArg0(), this.currentInstruction.getArg1());
            break;
         case 11:
            this.pop(this.currentInstruction.getArg0(), this.currentInstruction.getArg1());
         case 12:
         default:
            break;
         case 13:
            this.goTo(this.currentInstruction.getArg0());
            break;
         case 14:
            this.ifGoTo(this.currentInstruction.getArg0());
            break;
         case 15:
            if (this.program.getCurrentPC() == this.program.getPreviousPC() + 1) {
               throw new ProgramException("Missing return in " + this.callStack.getTopFunction());
            }

            this.function(this.currentInstruction.getArg0());
            break;
         case 16:
            this.returnFromFunction();
            break;
         case 17:
            this.callFunction(this.currentInstruction.getArg0(), this.currentInstruction.getArg1(), this.currentInstruction.getStringArg(), false);
         }

      }
   }

   public void add() throws ProgramException {
      this.calculate(2, 0);
   }

   public void substract() throws ProgramException {
      this.calculate(2, 1);
   }

   public void negate() throws ProgramException {
      this.calculate(1, 2);
   }

   public void equal() throws ProgramException {
      this.calculate(2, 6);
   }

   public void greaterThan() throws ProgramException {
      this.calculate(2, 7);
   }

   public void lessThan() throws ProgramException {
      this.calculate(2, 8);
   }

   public void and() throws ProgramException {
      this.calculate(2, 3);
   }

   public void or() throws ProgramException {
      this.calculate(2, 4);
   }

   public void not() throws ProgramException {
      this.calculate(1, 5);
   }

   public void push(short var1, short var2) throws ProgramException {
      if (var1 == 101) {
         this.pushValue(2, var2);
      } else if (var1 == 102) {
         switch(var2) {
         case 0:
            this.pushFromRAM(2, 3);
            break;
         case 1:
            this.pushFromRAM(2, 4);
         }
      } else {
         this.pushFromSegment(2, var1, var2);
      }

   }

   public void pop(short var1, short var2) throws ProgramException {
      if (var1 == 102) {
         switch(var2) {
         case 0:
            this.popToThisPointer(2);
            break;
         case 1:
            this.popToThatPointer(2);
         }
      } else {
         this.popToSegment(2, var1, var2);
      }

   }

   public void goTo(short var1) {
      this.program.setPC(var1);
   }

   public void ifGoTo(short var1) throws ProgramException {
      if (this.popAndReturn() != 0) {
         this.program.setPC(var1);
      }

   }

   public void function(short var1) throws ProgramException {
      short var2 = (short)(this.getSP() + var1);
      this.checkSP(var2);
      this.workingStackSegment.setStartAddress(var2);
      this.localSegment.setEnabledRange(this.getSP(), var2 - 1, true);

      for(int var3 = 0; var3 < var1; ++var3) {
         this.pushValue(1, (short)0);
      }

      String var4 = this.currentInstruction.getStringArg();
      this.callStack.pushFunction(var4);
      this.setStaticRange(var4);
   }

   public void infiniteLoopFromBuiltIn(String var1) {
      this.program.setPCToInfiniteLoopForBuiltIns(var1);
   }

   public void returnFromBuiltInFunction(short var1) throws ProgramException {
      this.pushValue(2, var1);
      this.returnFromFunction();
   }

   public void returnFromFunction() throws ProgramException {
      if (this.stackSegment.getValueAt(1) == 0) {
         throw new ProgramException("Nowhere to return to in " + this.getCallStack().getTopFunction() + "." + this.getCurrentInstruction().getIndexInFunction());
      } else {
         this.workingStackSegment.setStartAddress(this.getSP());
         this.bus.send(this.ram, 1, this.ram, 13);
         this.bus.send(this.stackSegment, this.stackSegment.getValueAt(1) - 5, this.ram, 14);
         this.bus.send(this.stackSegment, this.getSP() - 1, this.stackSegment, this.ram.getValueAt(2));
         this.setSP((short)(this.ram.getValueAt(2) + 1));
         this.bus.send(this.stackSegment, this.ram.getValueAt(13) - 1, this.ram, 4);
         this.bus.send(this.stackSegment, this.ram.getValueAt(13) - 2, this.ram, 3);
         this.bus.send(this.stackSegment, this.ram.getValueAt(13) - 3, this.ram, 2);
         this.bus.send(this.stackSegment, this.ram.getValueAt(13) - 4, this.ram, 1);
         this.callStack.popFunction();
         if (this.stackFrames.size() > 0) {
            int var1 = (Integer)this.stackFrames.lastElement();
            this.stackFrames.removeElementAt(this.stackFrames.size() - 1);
            this.workingStackSegment.setStartAddress(var1);
            this.localSegment.setEnabledRange(Math.max(this.localSegment.getStartAddress(), 256), var1 - 1, true);
            this.argSegment.setEnabledRange(this.argSegment.getStartAddress(), this.localSegment.getStartAddress() - 6, true);
            this.thisSegment.setEnabledRange(Math.max(this.thisSegment.getStartAddress(), 2048), 16383, true);
            this.thatSegment.setEnabledRange(Math.max(this.thatSegment.getStartAddress(), 2048), 24576, true);
         }

         short var2 = this.ram.getValueAt(14);
         if (var2 == -1) {
            this.staticSegment.setEnabledRange(0, -1, true);
            this.builtInFunctionsRunner.returnToBuiltInFunction(this.popValue(2));
         } else if (var2 >= 0 && var2 < this.program.getSize()) {
            if (this.stackFrames.size() > 0) {
               this.setStaticRange(this.callStack.getTopFunction());
            } else {
               this.staticSegment.setStartAddress(16);
               this.staticSegment.setEnabledRange(16, 254, true);
            }

            this.program.setPC((short)(var2 - 1));
            this.program.setPC(var2);
         } else {
            this.error("Illegal return address");
         }

      }
   }

   public void callFunctionFromBuiltIn(String var1, short[] var2) throws ProgramException {
      for(int var3 = 0; var3 < var2.length; ++var3) {
         this.pushValue(2, var2[var3]);
      }

      this.callFunction(this.program.getAddress(var1), (short)var2.length, var1, true);
   }

   public void callFunction(short var1, short var2, String var3, boolean var4) throws ProgramException {
      this.stackFrames.addElement(new Integer(this.workingStackSegment.getStartAddress()));
      this.workingStackSegment.setStartAddress(this.getSP() + 5);
      if (var4) {
         this.pushValue(1, (short)-1);
      } else {
         this.pushValue(1, this.program.getPC());
      }

      this.pushFromRAM(1, 1);
      this.pushFromRAM(1, 2);
      this.pushFromRAM(1, 3);
      this.pushFromRAM(1, 4);
      this.ram.setValueAt(2, (short)(this.getSP() - var2 - 5), false);
      this.ram.setValueAt(1, this.getSP(), false);
      this.argSegment.setEnabledRange(this.argSegment.getStartAddress(), this.argSegment.getStartAddress() + var2 - 1, true);
      if (var1 == -1) {
         this.localSegment.setEnabledRange(this.localSegment.getStartAddress(), this.localSegment.getStartAddress() - 1, true);
         this.callStack.pushFunction(var3 + " (built-in)");
         this.staticSegment.setEnabledRange(0, -1, true);
         short[] var5 = new short[var2];

         for(int var6 = 0; var6 < var2; ++var6) {
            var5[var6] = this.argSegment.getValueAt(var6);
         }

         this.builtInFunctionsRunner.callBuiltInFunction(var3, var5);
      } else if (var1 < 0 && var1 >= this.program.getSize()) {
         this.error("Illegal call address");
      } else {
         this.program.setPC(var1);
         this.program.setPC(var1);
      }

   }

   protected void setStaticRange(String var1) throws ProgramException {
      int var2 = var1.indexOf(".");
      if (var2 == -1) {
         throw new ProgramException("Illegal function name: " + var1);
      } else {
         String var3 = var1.substring(0, var2);
         int[] var4 = this.program.getStaticRange(var3);
         if (var4 == null) {
            throw new ProgramException("Function name doesn't match class name: " + var1);
         } else {
            this.staticSegment.setStartAddress(var4[0]);
            this.staticSegment.setEnabledRange(var4[0], var4[1], true);
         }
      }
   }

   private void calculate(int var1, int var2) throws ProgramException {
      this.calculator.showCalculator(var2, var1);
      this.popToCalculator(2, 1);
      if (var1 > 1) {
         this.popToCalculator(2, 0);
      }

      this.calculator.compute(var2);
      this.pushFromCalculator(2, 2);
      this.calculator.hideCalculator();
   }

   private void pushValue(int var1, short var2) throws ProgramException {
      short var3 = this.getSP();
      if (var1 == 1) {
         this.stackSegment.setValueAt(var3, var2, false);
      } else {
         this.workingStackSegment.setValueAt(var3, var2, false);
      }

      this.checkSP((short)(var3 + 1));
      this.setSP((short)(var3 + 1));
   }

   private void pushFromRAM(int var1, int var2) throws ProgramException {
      short var3 = this.getSP();
      this.bus.send(this.ram, var2, (ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var3);
      this.checkSP((short)(var3 + 1));
      this.setSP((short)(var3 + 1));
   }

   private void pushFromCalculator(int var1, int var2) throws ProgramException {
      short var3 = this.getSP();
      this.bus.send(this.calculator, var2, (ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var3);
      this.checkSP((short)(var3 + 1));
      this.setSP((short)(var3 + 1));
   }

   private void pushFromSegment(int var1, short var2, int var3) throws ProgramException {
      short var4 = this.getSP();
      MemorySegment var5 = var2 == 100 ? this.staticSegment : this.segments[var2];
      this.checkSegmentIndex(var5, var2, var3);
      this.bus.send(var5, var3, (ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var4);
      this.checkSP((short)(var4 + 1));
      this.setSP((short)(var4 + 1));
   }

   private void popToSegment(int var1, short var2, int var3) throws ProgramException {
      short var4 = (short)(this.getSP() - 1);
      MemorySegment var5 = var2 == 100 ? this.staticSegment : this.segments[var2];
      this.checkSegmentIndex(var5, var2, var3);
      this.bus.send((ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var4, var5, var3);
      this.checkSP(var4);
      this.setSP(var4);
   }

   private short popValue(int var1) throws ProgramException {
      short var2 = (short)(this.getSP() - 1);
      short var3;
      if (var1 == 1) {
         var3 = this.stackSegment.getValueAt(var2);
      } else {
         var3 = this.workingStackSegment.getValueAt(var2);
      }

      this.checkSP(var2);
      this.setSP(var2);
      return var3;
   }

   private void popToRAM(int var1, int var2) throws ProgramException {
      short var3 = (short)(this.getSP() - 1);
      this.bus.send((ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var3, this.ram, var2);
      this.checkSP(var3);
      this.setSP(var3);
   }

   private void popToThisPointer(int var1) throws ProgramException {
      short var2 = this.ram.getValueAt(this.getSP() - 1);
      if ((var2 < 2048 || var2 > 16383) && var2 > 0) {
         this.error("'This' segment must be in the Heap range");
      }

      this.popToRAM(var1, 3);
      this.thisSegment.setEnabledRange(var2, 16383, true);
   }

   private void popToThatPointer(int var1) throws ProgramException {
      short var2 = this.ram.getValueAt(this.getSP() - 1);
      if ((var2 < 2048 || var2 > 16383) && (var2 < 16384 || var2 > 24576) && var2 != 0) {
         this.error("'That' segment must be in the Heap or Screen range");
      }

      this.popToRAM(var1, 4);
      this.thatSegment.setEnabledRange(var2, 24576, true);
   }

   private void popToCalculator(int var1, int var2) throws ProgramException {
      short var3 = (short)(this.getSP() - 1);
      this.bus.send((ValueComputerPart)(var1 == 1 ? this.stackSegment : this.workingStackSegment), var3, this.calculator, var2);
      this.checkSP(var3);
      this.setSP(var3);
   }

   private short popAndReturn() throws ProgramException {
      short var1 = (short)(this.getSP() - 1);
      this.checkSP(var1);
      this.setSP(var1);
      return this.stackSegment.getValueAt(var1);
   }

   public MemorySegment getStaticSegment() {
      return this.staticSegment;
   }

   public short getSegmentAt(short var1, short var2) {
      return this.segments[var1].getValueAt(var2);
   }

   public void setSegmentAt(short var1, short var2, short var3) {
      this.segments[var1].setValueAt(var2, var3, false);
   }

   public short getSP() {
      return this.ram.getValueAt(0);
   }

   public void setSP(short var1) {
      this.ram.setValueAt(0, var1, true);
   }

   private void checkSP(short var1) throws ProgramException {
      if (var1 < 256 || var1 > 2047) {
         this.error("Stack overflow");
      }

   }

   private void checkSegmentIndex(MemorySegment var1, int var2, int var3) throws ProgramException {
      short var4 = (short)(var3 + var1.getStartAddress());
      if (var2 == 2) {
         if (var4 < 2048 || var4 > 16383) {
            this.error("Out of segment space");
         }
      } else {
         int[] var5 = var1.getEnabledRange();
         if (var4 < var5[0] || var4 > var5[1]) {
            this.error("Out of segment space");
         }
      }

   }

   private void error(String var1) throws ProgramException {
      throw new ProgramException(var1 + " in " + this.callStack.getTopFunction() + "." + this.currentInstruction.getIndexInFunction());
   }
}
