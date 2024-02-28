package Hack.Gates;

public class Connection {
   public static final byte FROM_INPUT = 1;
   public static final byte TO_INTERNAL = 2;
   public static final byte FROM_INTERNAL = 3;
   public static final byte TO_OUTPUT = 5;
   public static final byte FROM_TRUE = 6;
   public static final byte FROM_FALSE = 7;
   public static final byte FROM_CLOCK = 8;
   private byte type;
   private int gatePinNumber;
   private int partNumber;
   private String partPinName;
   private byte[] partSubBus;
   private byte[] gateSubBus;

   public Connection(byte var1, int var2, int var3, String var4, byte[] var5, byte[] var6) {
      this.type = var1;
      this.gatePinNumber = var2;
      this.partNumber = var3;
      this.partPinName = var4;
      this.gateSubBus = var5;
      this.partSubBus = var6;
   }

   public byte getType() {
      return this.type;
   }

   public int getGatePinNumber() {
      return this.gatePinNumber;
   }

   public int getPartNumber() {
      return this.partNumber;
   }

   public String getPartPinName() {
      return this.partPinName;
   }

   public byte[] getGateSubBus() {
      return this.gateSubBus;
   }

   public byte[] getPartSubBus() {
      return this.partSubBus;
   }
}
