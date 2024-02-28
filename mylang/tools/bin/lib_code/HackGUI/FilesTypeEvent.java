package HackGUI;

import java.util.EventObject;

public class FilesTypeEvent extends EventObject {
   private String firstFileName;
   private String secondFileName;
   private String thirdFileName;

   public FilesTypeEvent(Object var1, String var2, String var3, String var4) {
      super(var1);
      this.firstFileName = var2;
      this.secondFileName = var3;
      this.thirdFileName = var4;
   }

   public String getFirstFile() {
      return this.firstFileName;
   }

   public String getSecondFile() {
      return this.secondFileName;
   }

   public String getThirdFile() {
      return this.thirdFileName;
   }
}
