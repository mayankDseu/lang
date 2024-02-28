package SimulatorsGUI;

import java.util.EventObject;

public class PinValueEvent extends EventObject {
   private String valueStr;
   private boolean isOk;

   public PinValueEvent(Object var1, String var2, boolean var3) {
      super(var1);
      this.valueStr = var2;
      this.isOk = var3;
   }

   public String getValueStr() {
      return this.valueStr;
   }

   public boolean getIsOk() {
      return this.isOk;
   }
}
