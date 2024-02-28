package Hack.Utilities;

public class Conversions {
   private static final String ZEROS = "0000000000000000000000000000000000000000";
   private static final int[] powersOf2 = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE};
   private static final int[] powersOf16 = new int[]{1, 16, 256, 4096, 65536, 1048576, 16777216, 268435456};

   public static String toDecimalForm(String var0) {
      if (var0.startsWith("%B")) {
         var0 = String.valueOf(binaryToInt(var0.substring(2)));
      } else if (var0.startsWith("%X")) {
         if (var0.length() == 6) {
            var0 = String.valueOf(hex4ToInt(var0.substring(2)));
         } else {
            var0 = String.valueOf(hexToInt(var0.substring(2)));
         }
      } else if (var0.startsWith("%D")) {
         var0 = var0.substring(2);
      } else {
         try {
            int var1 = Integer.parseInt(var0);
            var0 = String.valueOf(var1);
         } catch (NumberFormatException var2) {
         }
      }

      return var0;
   }

   public static int binaryToInt(String var0) throws NumberFormatException {
      short var1 = 0;
      int var2 = var0.length() - 1;

      for(int var3 = 1; var2 >= 0; var3 <<= 1) {
         char var4 = var0.charAt(var2);
         if (var4 == '1') {
            var1 = (short)(var1 | var3);
         } else if (var4 != '0') {
            throw new NumberFormatException();
         }

         --var2;
      }

      return var1;
   }

   public static int hexToInt(String var0) throws NumberFormatException {
      int var1 = 0;
      int var2 = 1;

      for(int var3 = var0.length() - 1; var3 >= 0; var2 *= 16) {
         char var4 = var0.charAt(var3);
         if (var4 >= '0' && var4 <= '9') {
            var1 += (var4 - 48) * var2;
         } else if (var4 >= 'a' && var4 <= 'f') {
            var1 += (var4 - 97 + 10) * var2;
         } else {
            if (var4 < 'A' || var4 > 'F') {
               throw new NumberFormatException();
            }

            var1 += (var4 - 65 + 10) * var2;
         }

         --var3;
      }

      return var1;
   }

   public static int hex4ToInt(String var0) throws NumberFormatException {
      int var1 = hexToInt(var0);
      if (var1 > 32767) {
         var1 -= 65536;
      }

      return var1;
   }

   public static String decimalToBinary(int var0, int var1) {
      var0 &= powersOf2[var1] - 1;
      String var2 = Integer.toBinaryString(var0);
      if (var2.length() < var1) {
         var2 = "0000000000000000000000000000000000000000".substring(0, var1 - var2.length()) + var2;
      }

      return var2;
   }

   public static String decimalToHex(int var0, int var1) {
      var0 &= powersOf16[var1] - 1;
      String var2 = Integer.toHexString(var0);
      if (var2.length() < var1) {
         var2 = "0000000000000000000000000000000000000000".substring(0, var1 - var2.length()) + var2;
      }

      return var2;
   }
}
