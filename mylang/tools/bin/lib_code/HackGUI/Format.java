package HackGUI;

import Hack.Utilities.Conversions;

public class Format {
   public static final int DEC_FORMAT = 0;
   public static final int HEX_FORMAT = 1;
   public static final int BIN_FORMAT = 2;

   public static short translateValueToShort(String var0, int var1) throws NumberFormatException {
      short var2 = 0;
      switch(var1) {
      case 0:
         var2 = Short.parseShort(var0);
         break;
      case 1:
         var2 = (short)Conversions.hexToInt(var0);
         break;
      case 2:
         var2 = (short)Conversions.binaryToInt(var0);
      }

      return var2;
   }

   public static String translateValueToString(short var0, int var1) {
      String var2 = null;
      switch(var1) {
      case 0:
         var2 = String.valueOf(var0);
         break;
      case 1:
         var2 = Conversions.decimalToHex(var0, 4);
         break;
      case 2:
         var2 = Conversions.decimalToBinary(var0, 16);
      }

      return var2;
   }
}
