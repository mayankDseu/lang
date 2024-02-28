package AssemblerGUI;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class HACKFileFilter extends FileFilter {
   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         return true;
      } else {
         String var2 = getExtension(var1);
         if (var2 != null) {
            return var2.equals("hack");
         } else {
            return false;
         }
      }
   }

   public static String getExtension(File var0) {
      String var1 = null;
      String var2 = var0.getName();
      int var3 = var2.lastIndexOf(46);
      if (var3 > 0 && var3 < var2.length() - 1) {
         var1 = var2.substring(var3 + 1).toLowerCase();
      }

      return var1;
   }

   public String getDescription() {
      return "HACK Files";
   }
}
