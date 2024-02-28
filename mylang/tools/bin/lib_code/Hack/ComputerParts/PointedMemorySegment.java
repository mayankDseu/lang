package Hack.ComputerParts;

public class PointedMemorySegment extends MemorySegment {
   public PointedMemorySegment(Memory var1, PointedMemorySegmentGUI var2) {
      super(var1, var2);
   }

   public PointedMemorySegment(Memory var1, PointedMemorySegmentGUI var2, short var3, short var4) {
      super(var1, var2, var3, var4);
   }

   public void setPointerAddress(int var1) {
      if (this.displayChanges) {
         ((PointedMemorySegmentGUI)this.gui).setPointer(var1);
      }

   }

   public void reset() {
      super.reset();
   }
}
