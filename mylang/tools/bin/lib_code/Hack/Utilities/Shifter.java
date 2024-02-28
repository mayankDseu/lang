package Hack.Utilities;

public class Shifter {
   public static final short[] powersOf2 = new short[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, -32768};

   public static short unsignedShiftRight(short var0, byte var1) {
      short var2;
      if (var0 >= 0) {
         var2 = (short)(var0 >> var1);
      } else {
         var0 = (short)(var0 & 32767);
         var2 = (short)(var0 >> var1);
         var2 |= powersOf2[15 - var1];
      }

      return var2;
   }
}
