package Hack.Events;

import java.util.EventObject;

public class ErrorEvent extends EventObject {
   private String errorMessage;

   public ErrorEvent(Object var1, String var2) {
      super(var1);
      this.errorMessage = var2;
   }

   public String getErrorMessage() {
      return this.errorMessage;
   }
}
