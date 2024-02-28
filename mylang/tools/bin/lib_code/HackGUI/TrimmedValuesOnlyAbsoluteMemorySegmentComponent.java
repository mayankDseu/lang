package HackGUI;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JScrollBar;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class TrimmedValuesOnlyAbsoluteMemorySegmentComponent extends AbsolutePointedMemorySegmentComponent {
   protected DefaultTableCellRenderer getCellRenderer() {
      return new TrimmedValuesOnlyAbsoluteMemorySegmentComponent.TrimmedValuesOnlyTableCellRenderer();
   }

   public int getTableWidth() {
      return 124;
   }

   protected int getColumnValue() {
      return 0;
   }

   public synchronized void setPointer(int var1) {
      this.pointerAddress = (short)var1;
      this.segmentTable.revalidate();

      try {
         this.wait(100L);
      } catch (InterruptedException var3) {
      }

      this.scrollToPointer();
   }

   protected void scrollToPointer() {
      JScrollBar var1 = this.scrollPane.getVerticalScrollBar();
      int var2 = var1.getValue();
      Rectangle var3 = this.segmentTable.getCellRect(this.pointerAddress - this.startAddress - 1, 0, true);
      this.segmentTable.scrollRectToVisible(var3);
      this.repaint();
   }

   public Point getCoordinates(int var1) {
      JScrollBar var2 = this.scrollPane.getVerticalScrollBar();
      double var3 = Utilities.computeVisibleRowsCount(this.segmentTable);
      int var5 = (int)Math.max(Math.min((double)(var1 - this.startAddress), var3 - 1.0D), 0.0D);
      Rectangle var6 = this.segmentTable.getCellRect(var5, 0, true);
      this.segmentTable.scrollRectToVisible(var6);
      this.setTopLevelLocation();
      return new Point((int)(var6.getX() + this.topLevelLocation.getX()), (int)(var6.getY() + this.topLevelLocation.getY()));
   }

   protected TableModel getTableModel() {
      return new TrimmedValuesOnlyAbsoluteMemorySegmentComponent.TrimmedValuesOnlyAbsoluteTableModel();
   }

   class TrimmedValuesOnlyTableCellRenderer extends PointedMemorySegmentComponent.PointedMemorySegmentTableCellRenderer {
      TrimmedValuesOnlyTableCellRenderer() {
         super();
      }

      public void setRenderer(int var1, int var2) {
         super.setRenderer(var1, var2 + 1);
      }
   }

   class TrimmedValuesOnlyAbsoluteTableModel extends AbsolutePointedMemorySegmentComponent.AbsoluteTableModel {
      TrimmedValuesOnlyAbsoluteTableModel() {
         super();
      }

      public int getColumnCount() {
         return 1;
      }

      public int getRowCount() {
         return Math.max(TrimmedValuesOnlyAbsoluteMemorySegmentComponent.this.pointerAddress - TrimmedValuesOnlyAbsoluteMemorySegmentComponent.this.startAddress, 0);
      }

      public Object getValueAt(int var1, int var2) {
         return super.getValueAt(var1, var2 + 1);
      }

      public boolean isCellEditable(int var1, int var2) {
         return super.isCellEditable(var1, var2 + 1);
      }
   }
}
