package HackGUI;

import Hack.ComputerParts.PointedMemoryGUI;
import java.awt.Color;
import java.awt.event.FocusEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class PointedMemoryComponent extends MemoryComponent implements PointedMemoryGUI {
   protected int pointerAddress = -1;
   protected boolean hasFocus = false;

   protected DefaultTableCellRenderer getCellRenderer() {
      return new PointedMemoryComponent.PointedMemoryTableCellRenderer();
   }

   public void setPointer(int var1) {
      this.pointerAddress = var1;
      if (var1 >= 0) {
         Utilities.tableCenterScroll(this, this.memoryTable, var1);
      }

   }

   public void memoryTable_focusGained(FocusEvent var1) {
      super.memoryTable_focusGained(var1);
      this.hasFocus = true;
   }

   public void memoryTable_focusLost(FocusEvent var1) {
      super.memoryTable_focusLost(var1);
      this.hasFocus = false;
   }

   public class PointedMemoryTableCellRenderer extends MemoryComponent.MemoryTableCellRenderer {
      public PointedMemoryTableCellRenderer() {
         super();
      }

      public void setRenderer(int var1, int var2) {
         if (var1 == PointedMemoryComponent.this.pointerAddress) {
            this.setBackground(Color.yellow);
         } else {
            this.setBackground((Color)null);
         }

         super.setRenderer(var1, var2);
      }
   }
}
