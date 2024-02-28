package Hack.Controller;

public class VariableFormat {
   public static final char BINARY_FORMAT = 'B';
   public static final char DECIMAL_FORMAT = 'D';
   public static final char HEX_FORMAT = 'X';
   public static final char STRING_FORMAT = 'S';
   public String varName;
   public int padL;
   public int padR;
   public int len;
   public char format;

   public VariableFormat(String var1, char var2, int var3, int var4, int var5) {
      this.varName = var1;
      this.format = var2;
      this.padL = var3;
      this.padR = var4;
      this.len = var5;
   }
}
