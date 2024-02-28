package Hack.ComputerParts;

public class PointedMemory extends Memory {
   public PointedMemory(int var1, PointedMemoryGUI var2) {
      super(var1, var2);
   }

   public PointedMemory(int var1, PointedMemoryGUI var2, short var3, short var4) {
      super(var1, var2, var3, var4);
   }

   public void setPointerAddress(int var1) {
      if (this.displayChanges) {
         ((PointedMemoryGUI)this.gui).setPointer(var1);
      }

   }

   public void reset() {
      this.setPointerAddress(0);
      super.reset();
   }
}
