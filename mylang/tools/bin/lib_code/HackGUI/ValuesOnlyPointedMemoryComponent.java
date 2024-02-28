package HackGUI;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class ValuesOnlyPointedMemoryComponent extends PointedMemoryComponent {
   protected TableModel getTableModel() {
      return new ValuesOnlyPointedMemoryComponent.ValuesOnlyPointedMemoryTableModel();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new ValuesOnlyPointedMemoryComponent.ValuesOnlyPointedTableCellRenderer();
   }

   protected int getValueColumnIndex() {
      return 0;
   }

   protected void determineColumnWidth() {
   }

   public class ValuesOnlyPointedTableCellRenderer extends PointedMemoryComponent.PointedMemoryTableCellRenderer {
      public ValuesOnlyPointedTableCellRenderer() {
         super();
      }

      public void setRenderer(int var1, int var2) {
         super.setRenderer(var1, var2 + 1);
      }
   }

   public class ValuesOnlyPointedMemoryTableModel extends MemoryComponent.MemoryTableModel {
      public ValuesOnlyPointedMemoryTableModel() {
         super();
      }

      public int getColumnCount() {
         return 1;
      }

      public Object getValueAt(int var1, int var2) {
         return super.getValueAt(var1, var2 + 1);
      }

      public boolean isCellEditable(int var1, int var2) {
         return super.isCellEditable(var1, var2 + 1);
      }
   }
}
