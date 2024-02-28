package HackGUI;

import java.awt.Point;
import javax.swing.table.TableModel;

public class AbsolutePointedMemorySegmentComponent extends PointedMemorySegmentComponent {
   protected TableModel getTableModel() {
      return new AbsolutePointedMemorySegmentComponent.AbsoluteTableModel();
   }

   public String getValueAsString(int var1) {
      return super.getValueAsString(var1 - this.startAddress);
   }

   public Point getCoordinates(int var1) {
      return super.getCoordinates(var1 - this.startAddress);
   }

   protected void scrollToPointer() {
      Utilities.tableCenterScroll(this, this.segmentTable, this.pointerAddress - this.startAddress);
   }

   public class AbsoluteTableModel extends MemorySegmentComponent.MemorySegmentTableModel {
      public AbsoluteTableModel() {
         super();
      }

      public Object getValueAt(int var1, int var2) {
         return var2 == 0 ? String.valueOf(var1 + AbsolutePointedMemorySegmentComponent.this.startAddress) : super.getValueAt(var1, var2);
      }
   }
}
