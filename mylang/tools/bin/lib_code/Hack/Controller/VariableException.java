package Hack.Controller;

public class VariableException extends Exception {
   public VariableException(String var1, String var2) {
      super(var1 + ": " + var2);
   }
}
