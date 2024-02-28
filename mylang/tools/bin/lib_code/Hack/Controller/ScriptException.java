package Hack.Controller;

public class ScriptException extends Exception {
   public ScriptException(String var1, String var2, int var3) {
      super("In script " + var2 + ", Line " + var3 + ", " + var1);
   }

   public ScriptException(String var1, String var2) {
      super("In script " + var2 + ", " + var1);
   }

   public ScriptException(String var1) {
      super(var1);
   }
}
