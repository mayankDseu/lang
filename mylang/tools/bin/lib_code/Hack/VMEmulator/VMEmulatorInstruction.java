package Hack.VMEmulator;

import Hack.VirtualMachine.HVMInstruction;

public class VMEmulatorInstruction extends HVMInstruction {
   private short indexInFunction;

   public VMEmulatorInstruction(byte var1, short var2, short var3, short var4) {
      super(var1, var2, var3);
      this.indexInFunction = var4;
   }

   public VMEmulatorInstruction(byte var1, short var2, short var3) {
      super(var1, var2);
      this.indexInFunction = var3;
   }

   public VMEmulatorInstruction(byte var1, short var2) {
      super(var1);
      this.indexInFunction = var2;
   }

   public short getIndexInFunction() {
      return this.indexInFunction;
   }
}
