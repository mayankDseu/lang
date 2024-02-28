package HackGUI;

import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.plaf.FontUIResource;

public class Utilities {
   public static final String imagesDir = "bin/images/";
   private static final String VALUE_FONT_NAME = "Monospaced";
   private static final String LABEL_FONT_NAME = "Dialog";
   public static final Font valueFont = new FontUIResource("Monospaced", 0, 12);
   public static final Font boldValueFont = new FontUIResource("Monospaced", 1, 12);
   public static final Font bigBoldValueFont = new FontUIResource("Monospaced", 1, 13);
   public static final Font labelsFont = new FontUIResource("Dialog", 1, 12);
   public static final Font thinLabelsFont = new FontUIResource("Dialog", 0, 12);
   public static final Font smallLabelsFont = new FontUIResource("Dialog", 0, 11);
   public static final Font bigLabelsFont = new FontUIResource("Dialog", 1, 14);
   public static final Font thinBigLabelsFont = new FontUIResource("Dialog", 0, 14);
   public static final Font statusLineFont = new FontUIResource("Dialog", 1, 16);

   public static Point getTopLevelLocation(Component var0, Component var1) {
      Point var2 = new Point();

      for(Object var3 = var1; ((Component)var3).getParent() != null && var3 != var0; var3 = ((Component)var3).getParent()) {
         var2.x = (int)((double)var2.x + ((Component)var3).getLocation().getX());
         var2.y = (int)((double)var2.y + ((Component)var3).getLocation().getY());
      }

      return var2;
   }

   public static void tableCenterScroll(JPanel var0, JTable var1, int var2) {
      JScrollPane var3 = (JScrollPane)var1.getParent().getParent();
      JScrollBar var4 = var3.getVerticalScrollBar();
      int var5 = var4.getValue();
      Rectangle var6 = var1.getCellRect(var2, 0, true);
      var1.scrollRectToVisible(var6);
      var0.repaint();
      int var7 = var4.getValue();
      double var8 = computeVisibleRowsCount(var1);
      Rectangle var10;
      if (var7 > var5) {
         var10 = var1.getCellRect((int)Math.min((double)var2 + var8 / 2.0D, (double)(var1.getRowCount() - 1)), 0, true);
         var1.scrollRectToVisible(var10);
         var0.repaint();
      } else if (var7 < var5) {
         var10 = var1.getCellRect((int)Math.max((double)var2 - var8 / 2.0D, 0.0D), 0, true);
         var1.scrollRectToVisible(var10);
         var0.repaint();
      }

   }

   public static double computeVisibleRowsCount(JTable var0) {
      return var0.getParent().getBounds().getHeight() / (double)var0.getRowHeight();
   }
}
