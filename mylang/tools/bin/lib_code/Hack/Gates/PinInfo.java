package Hack.Gates;

public class PinInfo {
   public String name;
   public byte width;
   public short value;
   private boolean[] initialized;

   public PinInfo() {
      this.initialized = new boolean[16];
   }

   public PinInfo(String var1, byte var2) {
      this.name = var1;
      this.width = var2;
      this.initialized = new boolean[var2];
   }

   public void initialize(byte[] var1) {
      byte var2;
      byte var3;
      if (var1 != null) {
         var2 = var1[0];
         var3 = var1[1];
      } else {
         var2 = 0;
         var3 = (byte)(this.width - 1);
      }

      for(byte var4 = var2; var4 <= var3; ++var4) {
         this.initialized[var4] = true;
      }

   }

   public boolean isInitialized(byte[] var1) {
      boolean var2 = false;
      byte var3;
      byte var4;
      if (var1 != null) {
         var3 = var1[0];
         var4 = var1[1];
      } else {
         var3 = 0;
         var4 = (byte)(this.width - 1);
      }

      for(byte var5 = var3; var5 <= var4 && !var2; ++var5) {
         var2 = this.initialized[var5];
      }

      return var2;
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   public boolean equals(Object var1) {
      return var1 instanceof PinInfo && this.name.equals(((PinInfo)var1).name);
   }
}
