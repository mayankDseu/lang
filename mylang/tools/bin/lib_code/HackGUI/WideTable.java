package HackGUI;

import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class WideTable extends JTable {
   private int width;

   public WideTable(TableModel var1, int var2) {
      super(var1);
      this.width = var2;
   }

   public Dimension getSize() {
      return new Dimension(this.width, super.getHeight());
   }

   public Dimension getPreferredSize() {
      Dimension var1 = super.getPreferredSize();
      var1.width = this.width;
      return var1;
   }

   public Rectangle getBounds() {
      Rectangle var1 = super.getBounds();
      var1.width = this.width;
      return var1;
   }

   public int getWidth() {
      return this.width;
   }
}
