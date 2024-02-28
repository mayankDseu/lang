package Hack.VMEmulator;

import Hack.VirtualMachine.HVMInstruction;
import java.util.EventObject;

public class VMProgramRowSelectionEvent extends EventObject {
   private int rowIndex;
   private HVMInstruction rowInstruction;

   public VMProgramRowSelectionEvent(Object var1, HVMInstruction var2, int var3) {
      super(var1);
      this.rowInstruction = var2;
      this.rowIndex = var3;
   }

   public HVMInstruction getRowInstruction() {
      return this.rowInstruction;
   }

   public int getRowIndex() {
      return this.rowIndex;
   }
}
