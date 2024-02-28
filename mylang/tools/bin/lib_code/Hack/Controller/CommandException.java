package Hack.Controller;

public class CommandException extends Exception {
   public CommandException(String var1, String[] var2) {
      super(var1 + ": " + commandString(var2));
   }

   private static String commandString(String[] var0) {
      StringBuffer var1 = new StringBuffer();

      for(int var2 = 0; var2 < var0.length; ++var2) {
         var1.append(var0[var2] + " ");
      }

      return var1.toString();
   }
}
