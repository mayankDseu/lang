package Hack.Gates;

import Hack.Utilities.Shifter;

public class SubNode extends Node {
   private short mask;
   private byte shiftRight;

   public SubNode(byte var1, byte var2) {
      this.mask = getMask(var1, var2);
      this.shiftRight = var1;
   }

   public short get() {
      return Shifter.unsignedShiftRight((short)(this.value & this.mask), this.shiftRight);
   }

   public static short getMask(byte var0, byte var1) {
      short var2 = 0;
      short var3 = Shifter.powersOf2[var0];

      for(byte var4 = var0; var4 <= var1; ++var4) {
         var2 |= var3;
         var3 = (short)(var3 << 1);
      }

      return var2;
   }
}
