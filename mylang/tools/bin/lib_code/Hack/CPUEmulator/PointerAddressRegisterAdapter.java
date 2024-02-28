package Hack.CPUEmulator;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.PointedMemory;
import Hack.ComputerParts.Register;
import Hack.ComputerParts.RegisterGUI;

public class PointerAddressRegisterAdapter extends Register {
   protected PointedMemory memory;
   protected boolean updatePointer;

   public PointerAddressRegisterAdapter(RegisterGUI var1, PointedMemory var2) {
      super(var1, (short)-32768, (short)32767);
      this.memory = var2;
      this.updatePointer = true;
   }

   public void setValueAt(int var1, short var2, boolean var3) {
      super.setValueAt(0, var2, var3);
      if (this.updatePointer) {
         this.memory.setPointerAddress(var2);
      }

   }

   public void valueChanged(ComputerPartEvent var1) {
      super.valueChanged(var1);
      if (this.updatePointer) {
         this.memory.setPointerAddress(var1.getValue());
      }

   }

   public void reset() {
      super.reset();
      if (this.updatePointer) {
         this.memory.setPointerAddress(0);
      }

   }

   public void refreshGUI() {
      this.quietUpdateGUI(0, this.value);
      if (this.updatePointer) {
         this.memory.setPointerAddress(this.value);
      }

   }

   public void setUpdatePointer(boolean var1) {
      this.updatePointer = var1;
      if (var1) {
         this.memory.setPointerAddress(this.value);
      }

   }
}
