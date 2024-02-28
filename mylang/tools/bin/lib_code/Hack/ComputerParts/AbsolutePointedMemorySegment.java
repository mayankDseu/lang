package Hack.ComputerParts;

public class AbsolutePointedMemorySegment extends PointedMemorySegment {
   public AbsolutePointedMemorySegment(Memory var1, PointedMemorySegmentGUI var2) {
      super(var1, var2);
   }

   public AbsolutePointedMemorySegment(Memory var1, PointedMemorySegmentGUI var2, short var3, short var4) {
      super(var1, var2, var3, var4);
   }

   public void setValueAt(int var1, short var2, boolean var3) {
      super.setValueAt(var1 - this.startAddress, var2, var3);
   }

   public short getValueAt(int var1) {
      return this.mainMemory.getValueAt(var1);
   }

   public void valueChanged(ComputerPartEvent var1) {
      ComputerPartEvent var2 = new ComputerPartEvent((ComputerPartGUI)var1.getSource(), var1.getIndex() + this.startAddress, var1.getValue());
      super.valueChanged(var2);
   }
}
