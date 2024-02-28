package Hack.Assembler;

public class AssemblerException extends Exception {
   public AssemblerException(String var1) {
      super(var1);
   }

   public AssemblerException(String var1, int var2) {
      super("In line " + var2 + ", " + var1);
   }
}
