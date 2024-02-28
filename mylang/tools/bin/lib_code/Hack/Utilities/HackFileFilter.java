package Hack.Utilities;

import java.io.File;
import java.io.FilenameFilter;

public class HackFileFilter implements FilenameFilter {
   private String extension;

   public HackFileFilter(String var1) {
      this.extension = var1;
   }

   public boolean accept(File var1, String var2) {
      return var2.endsWith(this.extension);
   }

   public String getAcceptedExtension() {
      return this.extension;
   }
}
