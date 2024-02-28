package HackGUI;

import Hack.ComputerParts.LabeledPointedMemoryGUI;
import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class LabeledMemoryComponent extends PointedMemoryComponent implements LabeledPointedMemoryGUI {
   protected String[] labels;
   private int labelFlashIndex = -1;

   public LabeledMemoryComponent() {
      this.searchButton.setLocation(199, 2);
      this.clearButton.setLocation(168, 2);
      this.memoryTable.setGridColor(Color.lightGray);
      this.labels = new String[0];
   }

   public int getTableWidth() {
      return 233;
   }

   protected int getValueColumnIndex() {
      return 2;
   }

   public void setLabel(int var1, String var2) {
      this.labels[var1] = var2 + ":";
      this.repaint();
   }

   public void clearLabels() {
      for(int var1 = 0; var1 < this.labels.length; ++var1) {
         if (this.labels[var1] != null) {
            this.labels[var1] = null;
         }
      }

      this.repaint();
   }

   public void labelFlash(int var1) {
      this.labelFlashIndex = var1;
      this.repaint();
   }

   public void hideLabelFlash() {
      this.labelFlashIndex = -1;
      this.repaint();
   }

   public void setContents(short[] var1) {
      String[] var2 = this.labels;
      this.labels = new String[var1.length];
      System.arraycopy(var2, 0, this.labels, 0, Math.min(var2.length, this.labels.length));
      super.setContents(var1);
   }

   protected TableModel getTableModel() {
      return new LabeledMemoryComponent.LabeledMemoryTableModel();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new LabeledMemoryComponent.LabeledPointedMemoryTableCellRenderer();
   }

   protected void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 2; ++var2) {
         var1 = this.memoryTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setMinWidth(1);
            var1.setPreferredWidth(1);
         } else if (var2 == 1) {
            var1.setMinWidth(1);
            var1.setPreferredWidth(1);
         }
      }

   }

   public class LabeledPointedMemoryTableCellRenderer extends PointedMemoryComponent.PointedMemoryTableCellRenderer {
      public LabeledPointedMemoryTableCellRenderer() {
         super();
      }

      public void setRenderer(int var1, int var2) {
         super.setRenderer(var1, var2 - 1);
         if (var2 == 0) {
            this.setHorizontalAlignment(4);
            this.setFont(Utilities.boldValueFont);
            this.setBackground(Color.lightGray);
            if (var1 == LabeledMemoryComponent.this.labelFlashIndex) {
               this.setBackground(Color.orange);
            }
         }

      }
   }

   class LabeledMemoryTableModel extends MemoryComponent.MemoryTableModel {
      LabeledMemoryTableModel() {
         super();
      }

      public int getColumnCount() {
         return 3;
      }

      public Object getValueAt(int var1, int var2) {
         String var3 = "";
         if (var2 == 0) {
            var3 = LabeledMemoryComponent.this.labels[var1];
         } else {
            var3 = (String)super.getValueAt(var1, var2 - 1);
         }

         return var3;
      }

      public boolean isCellEditable(int var1, int var2) {
         return var2 == 0 ? false : super.isCellEditable(var1, var2 - 1);
      }
   }
}
