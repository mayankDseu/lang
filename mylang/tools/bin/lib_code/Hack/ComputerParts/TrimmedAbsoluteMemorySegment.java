package Hack.ComputerParts;

public class TrimmedAbsoluteMemorySegment extends AbsolutePointedMemorySegment {
   public TrimmedAbsoluteMemorySegment(Memory var1, PointedMemorySegmentGUI var2) {
      super(var1, var2);
   }

   public TrimmedAbsoluteMemorySegment(Memory var1, PointedMemorySegmentGUI var2, short var3, short var4) {
      super(var1, var2, var3, var4);
   }

   public void setValueAt(int var1, short var2, boolean var3) {
      if (this.displayChanges) {
         ((PointedMemorySegmentGUI)this.gui).setPointer(var1 + 1);
      }

      super.setValueAt(var1, var2, var3);
   }
}
