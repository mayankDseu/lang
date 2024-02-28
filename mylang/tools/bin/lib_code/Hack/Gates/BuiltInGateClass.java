package Hack.Gates;

public class BuiltInGateClass extends GateClass {
   private Class javaGateClass;

   public BuiltInGateClass(String var1, HDLTokenizer var2, PinInfo[] var3, PinInfo[] var4) throws HDLException {
      super(var1, var3, var4);
      var2.advance();
      if (var2.getTokenType() != 3) {
         var2.HDLError("Missing java class name");
      }

      String var5 = var2.getIdentifier();
      String var6 = GatesManager.getInstance().getBuiltInDir() + "." + var5;

      try {
         this.javaGateClass = Class.forName(var6);
      } catch (ClassNotFoundException var14) {
         var2.HDLError("Can't find " + var5 + " java class");
      }

      Class var7 = this.javaGateClass;

      boolean var8;
      do {
         var7 = var7.getSuperclass();
         var8 = var7.getName().equals("Hack.Gates.BuiltInGate");
      } while(!var8 && !var7.getName().equals("java.lang.Object"));

      if (!var8) {
         var2.HDLError(var5 + " is not a subclass of BuiltInGate");
      }

      var2.advance();
      if (var2.getTokenType() != 2 || var2.getSymbol() != ';') {
         var2.HDLError("Missing ';'");
      }

      this.isInputClocked = new boolean[var3.length];
      this.isOutputClocked = new boolean[var4.length];
      var2.advance();
      if (var2.getTokenType() == 1) {
         if (var2.getKeywordType() != 5) {
            var2.HDLError("Unexpected keyword");
         }

         this.isClocked = true;
         String[] var9 = readPinNames(var2);

         for(int var10 = 0; var10 < var9.length; ++var10) {
            boolean var11 = false;
            boolean var12 = false;

            int var13;
            for(var13 = 0; var13 < this.isInputClocked.length && !var11; ++var13) {
               if (!this.isInputClocked[var13]) {
                  var11 = var3[var13].name.equals(var9[var10]);
                  this.isInputClocked[var13] = var11;
               }
            }

            if (!var11) {
               for(var13 = 0; var13 < this.isOutputClocked.length && !var12; ++var13) {
                  if (!this.isOutputClocked[var13]) {
                     var12 = var4[var13].name.equals(var9[var10]);
                     this.isOutputClocked[var13] = var12;
                  }
               }
            }
         }

         var2.advance();
      }

      if (var2.getTokenType() != 2 || var2.getSymbol() != '}') {
         var2.HDLError("Missing '}'");
      }

   }

   public Gate newInstance() throws InstantiationException {
      Node[] var2 = new Node[this.inputPinsInfo.length];
      Node[] var3 = new Node[this.outputPinsInfo.length];

      int var4;
      for(var4 = 0; var4 < var2.length; ++var4) {
         var2[var4] = new Node();
      }

      for(var4 = 0; var4 < var3.length; ++var4) {
         var3[var4] = new Node();
      }

      BuiltInGate var1;
      try {
         var1 = (BuiltInGate)this.javaGateClass.newInstance();
      } catch (IllegalAccessException var6) {
         throw new InstantiationException(var6.getMessage());
      }

      var1.init(var2, var3, this);
      if (var1 instanceof BuiltInGateWithGUI) {
         GatesManager.getInstance().addChip((BuiltInGateWithGUI)var1);
      }

      DirtyGateAdapter var7 = new DirtyGateAdapter(var1);

      for(int var5 = 0; var5 < this.isInputClocked.length; ++var5) {
         if (!this.isInputClocked[var5]) {
            var2[var5].addListener(var7);
         }
      }

      return var1;
   }
}
