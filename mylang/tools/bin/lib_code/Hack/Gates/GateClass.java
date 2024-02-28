package Hack.Gates;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

public abstract class GateClass {
   public static final byte UNKNOWN_PIN_TYPE = 0;
   public static final byte INPUT_PIN_TYPE = 1;
   public static final byte OUTPUT_PIN_TYPE = 2;
   protected PinInfo[] inputPinsInfo;
   protected PinInfo[] outputPinsInfo;
   protected String name;
   protected boolean isClocked;
   protected boolean[] isInputClocked;
   protected boolean[] isOutputClocked;
   protected Hashtable namesToTypes = new Hashtable();
   protected Hashtable namesToNumbers = new Hashtable();
   protected static Hashtable GateClasses = new Hashtable();

   protected GateClass(String var1, PinInfo[] var2, PinInfo[] var3) {
      this.name = var1;
      this.inputPinsInfo = var2;
      this.registerPins(var2, (byte)1);
      this.outputPinsInfo = var3;
      this.registerPins(var3, (byte)2);
   }

   public static GateClass getGateClass(String var0, boolean var1) throws HDLException {
      String var2 = null;
      if (!var1) {
         var2 = GatesManager.getInstance().getHDLFileName(var0);
         if (var2 == null) {
            throw new HDLException("Chip " + var0 + " is not found in the working and built in folders");
         }
      } else {
         var2 = var0;
         File var3 = new File(var0);
         if (!var3.exists()) {
            throw new HDLException("Chip " + var0 + " doesn't exist");
         }

         var0 = var3.getName().substring(0, var3.getName().lastIndexOf("."));
      }

      GateClass var5 = (GateClass)GateClasses.get(var2);
      if (var5 == null) {
         HDLTokenizer var4 = new HDLTokenizer(var2);
         var5 = readHDL(var4, var0);
         GateClasses.put(var2, var5);
      }

      return var5;
   }

   public static void clearGateCache() {
      GateClasses.clear();
   }

   public static boolean gateClassExists(String var0) {
      String var1 = GatesManager.getInstance().getHDLFileName(var0);
      return GateClasses.get(var1) != null;
   }

   private static GateClass readHDL(HDLTokenizer var0, String var1) throws HDLException {
      var0.advance();
      if (var0.getTokenType() != 1 || var0.getKeywordType() != 1) {
         var0.HDLError("Missing 'CHIP' keyword");
      }

      var0.advance();
      if (var0.getTokenType() != 3) {
         var0.HDLError("Missing chip name");
      }

      String var2 = var0.getIdentifier();
      if (!var1.equals(var2)) {
         var0.HDLError("Chip name doesn't match the HDL name");
      }

      var0.advance();
      if (var0.getTokenType() != 2 || var0.getSymbol() != '{') {
         var0.HDLError("Missing '{'");
      }

      var0.advance();
      PinInfo[] var3;
      if (var0.getTokenType() == 1 && var0.getKeywordType() == 2) {
         var3 = getPinsInfo(var0, readPinNames(var0));
         var0.advance();
      } else {
         var3 = new PinInfo[0];
      }

      PinInfo[] var4;
      if (var0.getTokenType() == 1 && var0.getKeywordType() == 3) {
         var4 = getPinsInfo(var0, readPinNames(var0));
         var0.advance();
      } else {
         var4 = new PinInfo[0];
      }

      Object var5 = null;
      if (var0.getTokenType() == 1 && var0.getKeywordType() == 4) {
         var5 = new BuiltInGateClass(var1, var0, var3, var4);
      } else if (var0.getTokenType() == 1 && var0.getKeywordType() == 6) {
         var5 = new CompositeGateClass(var1, var0, var3, var4);
      } else {
         var0.HDLError("Keyword expected");
      }

      return (GateClass)var5;
   }

   protected static String[] readPinNames(HDLTokenizer var0) throws HDLException {
      Vector var1 = new Vector();
      boolean var2 = false;
      var0.advance();

      while(true) {
         while(!var2) {
            if (var0.getTokenType() == 2 && var0.getSymbol() == ';') {
               var2 = true;
            } else {
               if (var0.getTokenType() != 3) {
                  var0.HDLError("Pin name expected");
               }

               String var3 = var0.getIdentifier();
               var1.addElement(var3);
               var0.advance();
               if (var0.getTokenType() != 2 || var0.getSymbol() != ',' && var0.getSymbol() != ';') {
                  var0.HDLError("',' or ';' expected");
               }

               if (var0.getTokenType() == 2 && var0.getSymbol() == ',') {
                  var0.advance();
               }
            }
         }

         String[] var4 = new String[var1.size()];
         var1.toArray(var4);
         return var4;
      }
   }

   private static PinInfo[] getPinsInfo(HDLTokenizer var0, String[] var1) throws HDLException {
      PinInfo[] var2 = new PinInfo[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = new PinInfo();
         int var4 = var1[var3].indexOf("[");
         if (var4 >= 0) {
            try {
               String var5 = var1[var3].substring(var4 + 1, var1[var3].indexOf("]"));
               var2[var3].width = (byte)Integer.parseInt(var5);
               var2[var3].name = var1[var3].substring(0, var4);
            } catch (Exception var6) {
               var0.HDLError(var1[var3] + " has an invalid bus width");
            }
         } else {
            var2[var3].width = 1;
            var2[var3].name = var1[var3];
         }
      }

      return var2;
   }

   public PinInfo getPinInfo(byte var1, int var2) {
      PinInfo var3 = null;
      switch(var1) {
      case 1:
         if (var2 < this.inputPinsInfo.length) {
            var3 = this.inputPinsInfo[var2];
         }
         break;
      case 2:
         if (var2 < this.outputPinsInfo.length) {
            var3 = this.outputPinsInfo[var2];
         }
      }

      return var3;
   }

   public PinInfo getPinInfo(String var1) {
      byte var2 = this.getPinType(var1);
      int var3 = this.getPinNumber(var1);
      return this.getPinInfo(var2, var3);
   }

   protected void registerPins(PinInfo[] var1, byte var2) {
      for(int var3 = 0; var3 < var1.length; ++var3) {
         this.namesToTypes.put(var1[var3].name, new Byte(var2));
         this.namesToNumbers.put(var1[var3].name, new Integer(var3));
      }

   }

   protected void registerPin(PinInfo var1, byte var2, int var3) {
      this.namesToTypes.put(var1.name, new Byte(var2));
      this.namesToNumbers.put(var1.name, new Integer(var3));
   }

   public byte getPinType(String var1) {
      Byte var2 = (Byte)this.namesToTypes.get(var1);
      return var2 != null ? var2 : 0;
   }

   public int getPinNumber(String var1) {
      Integer var2 = (Integer)this.namesToNumbers.get(var1);
      return var2 != null ? var2 : -1;
   }

   public String getName() {
      return this.name;
   }

   public boolean isClocked() {
      return this.isClocked;
   }

   public abstract Gate newInstance() throws InstantiationException;
}
