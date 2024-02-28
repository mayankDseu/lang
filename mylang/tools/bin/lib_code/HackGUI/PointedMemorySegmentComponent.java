package HackGUI;

import Hack.ComputerParts.PointedMemorySegmentGUI;
import java.awt.Color;
import java.awt.event.FocusEvent;
import javax.swing.table.DefaultTableCellRenderer;

public class PointedMemorySegmentComponent extends MemorySegmentComponent implements PointedMemorySegmentGUI {
   protected short pointerAddress = -1;
   protected boolean hasFocus = false;

   public void setStartAddress(int var1) {
      super.setStartAddress(var1);
      this.scrollToPointer();
   }

   public void setPointer(int var1) {
      this.pointerAddress = (short)var1;
      this.scrollToPointer();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new PointedMemorySegmentComponent.PointedMemorySegmentTableCellRenderer();
   }

   protected void scrollToPointer() {
      if (this.pointerAddress >= 0) {
         Utilities.tableCenterScroll(this, this.segmentTable, this.pointerAddress);
      }

   }

   public void segmentTable_focusGained(FocusEvent var1) {
      super.segmentTable_focusGained(var1);
      this.hasFocus = true;
   }

   public void segmentTable_focusLost(FocusEvent var1) {
      super.segmentTable_focusLost(var1);
      this.hasFocus = false;
   }

   public class PointedMemorySegmentTableCellRenderer extends MemorySegmentComponent.MemorySegmentTableCellRenderer {
      public PointedMemorySegmentTableCellRenderer() {
         super();
      }

      public void setRenderer(int var1, int var2) {
         if (var1 == PointedMemorySegmentComponent.this.pointerAddress - PointedMemorySegmentComponent.this.startAddress) {
            this.setBackground(Color.yellow);
         } else {
            this.setBackground((Color)null);
         }

         super.setRenderer(var1, var2);
      }
   }
}
