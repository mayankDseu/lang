package Hack.Controller;

public class ProgramException extends Exception {
   public ProgramException(String var1) {
      super(var1);
   }

   public ProgramException(String var1, int var2) {
      super("In line " + var2 + ", " + var1);
   }
}
