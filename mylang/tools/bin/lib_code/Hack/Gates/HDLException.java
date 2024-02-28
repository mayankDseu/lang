package Hack.Gates;

public class HDLException extends Exception {
   public HDLException(String var1, String var2, int var3) {
      super("In HDL file " + var2 + ", Line " + var3 + ", " + var1);
   }

   public HDLException(String var1, String var2) {
      super("In HDL file " + var2 + ", " + var1);
   }

   public HDLException(String var1) {
      super(var1);
   }
}
