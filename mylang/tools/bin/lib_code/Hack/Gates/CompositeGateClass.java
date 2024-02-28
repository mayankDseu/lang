package Hack.Gates;

import Hack.Utilities.Graph;
import java.util.Iterator;
import java.util.Vector;

public class CompositeGateClass extends GateClass {
   public static final byte INTERNAL_PIN_TYPE = 3;
   public static final PinInfo TRUE_NODE_INFO = new PinInfo("true", (byte)16);
   public static final PinInfo FALSE_NODE_INFO = new PinInfo("false", (byte)16);
   public static final PinInfo CLOCK_NODE_INFO = new PinInfo("clk", (byte)1);
   protected Vector internalPinsInfo = new Vector();
   private Vector partsList = new Vector();
   private int[] partsOrder;
   private ConnectionSet connections = new ConnectionSet();

   public CompositeGateClass(String var1, HDLTokenizer var2, PinInfo[] var3, PinInfo[] var4) throws HDLException {
      super(var1, var3, var4);
      this.isInputClocked = new boolean[var3.length];
      this.isOutputClocked = new boolean[var4.length];
      this.readParts(var2);
      Graph var5 = this.createConnectionsGraph();
      Object[] var6 = var5.topologicalSort(this.partsList);
      if (var5.hasCircle()) {
         throw new HDLException("This chip has a circle in its parts connections");
      } else {
         this.partsOrder = new int[this.partsList.size()];
         int var7 = 0;

         int var8;
         for(var8 = 0; var8 < var6.length; ++var8) {
            if (var6[var8] instanceof Integer) {
               this.partsOrder[var7++] = (Integer)var6[var8];
            }
         }

         for(var8 = 0; var8 < var3.length; ++var8) {
            this.isInputClocked[var8] = !var5.pathExists(var3[var8], var4);
         }

         for(var8 = 0; var8 < var4.length; ++var8) {
            this.isOutputClocked[var8] = !var5.pathExists(var3, var4[var8]);
         }

      }
   }

   private void readParts(HDLTokenizer var1) throws HDLException {
      boolean var2 = false;

      int var5;
      while(var1.hasMoreTokens() && !var2) {
         var1.advance();
         if (var1.getTokenType() == 2 && var1.getSymbol() == '}') {
            var2 = true;
         } else {
            if (var1.getTokenType() != 3) {
               var1.HDLError("A GateClass name is expected");
            }

            String var3 = var1.getIdentifier();
            GateClass var4 = getGateClass(var3, false);
            this.partsList.addElement(var4);
            this.isClocked = this.isClocked || var4.isClocked;
            var5 = this.partsList.size() - 1;
            var1.advance();
            if (var1.getTokenType() != 2 || var1.getSymbol() != '(') {
               var1.HDLError("Missing '('");
            }

            this.readPinNames(var1, var5, var3);
            var1.advance();
            if (var1.getTokenType() != 2 || var1.getSymbol() != ';') {
               var1.HDLError("Missing ';'");
            }
         }
      }

      if (!var2) {
         var1.HDLError("Missing '}'");
      }

      if (var1.hasMoreTokens()) {
         var1.HDLError("Expected end-of-file after '}'");
      }

      boolean[] var6 = new boolean[this.internalPinsInfo.size()];
      Iterator var7 = this.connections.iterator();

      while(var7.hasNext()) {
         Connection var8 = (Connection)var7.next();
         if (var8.getType() == 2) {
            var6[var8.getGatePinNumber()] = true;
         }
      }

      for(var5 = 0; var5 < var6.length; ++var5) {
         if (!var6[var5]) {
            var1.HDLError(((PinInfo)this.internalPinsInfo.elementAt(var5)).name + " has no source pin");
         }
      }

   }

   private void readPinNames(HDLTokenizer var1, int var2, String var3) throws HDLException {
      boolean var4 = false;

      while(true) {
         while(!var4) {
            var1.advance();
            if (var1.getTokenType() != 3) {
               var1.HDLError("A pin name is expected");
            }

            String var5 = var1.getIdentifier();
            var1.advance();
            if (var1.getTokenType() != 2 || var1.getSymbol() != '=') {
               var1.HDLError("Missing '='");
            }

            var1.advance();
            if (var1.getTokenType() != 3) {
               var1.HDLError("A pin name is expected");
            }

            String var6 = var1.getIdentifier();
            this.addConnection(var1, var2, var3, var5, var6);
            var1.advance();
            if (var1.getTokenType() == 2 && var1.getSymbol() == ')') {
               var4 = true;
            } else if (var1.getTokenType() != 2 || var1.getSymbol() != ',') {
               var1.HDLError("',' or ')' are expected");
            }
         }

         return;
      }
   }

   private static byte[] getSubBusAndCheck(HDLTokenizer var0, String var1, int var2) throws HDLException {
      byte[] var3 = null;

      try {
         var3 = getSubBus(var1);
      } catch (Exception var5) {
         var0.HDLError(var1 + " has an invalid sub bus specification");
      }

      if (var3 != null) {
         if (var3[0] >= 0 && var3[1] >= 0) {
            if (var3[0] > var3[1]) {
               var0.HDLError(var1 + ": left bit number should be lower than the right one");
            } else if (var3[1] >= var2) {
               var0.HDLError(var1 + ": the specified sub bus is not in the bus range");
            }
         } else {
            var0.HDLError(var1 + ": negative bit numbers are illegal");
         }
      }

      return var3;
   }

   public static byte[] getSubBus(String var0) throws Exception {
      byte[] var1 = null;
      int var2 = var0.indexOf("[");
      if (var2 >= 0) {
         var1 = new byte[2];
         String var3 = null;
         int var4 = var0.indexOf("..");
         if (var4 >= 0) {
            var3 = var0.substring(var2 + 1, var4);
            var1[0] = Byte.parseByte(var3);
            var3 = var0.substring(var4 + 2, var0.indexOf("]"));
            var1[1] = Byte.parseByte(var3);
         } else {
            var3 = var0.substring(var2 + 1, var0.indexOf("]"));
            var1[0] = Byte.parseByte(var3);
            var1[1] = var1[0];
         }
      }

      return var1;
   }

   private void addConnection(HDLTokenizer var1, int var2, String var3, String var4, String var5) throws HDLException {
      GateClass var6 = (GateClass)this.partsList.elementAt(var2);
      byte var9 = 0;
      int var10 = var4.indexOf("[");
      String var7 = var10 >= 0 ? var4.substring(0, var10) : var4;
      byte var11 = var6.getPinType(var7);
      if (var11 == 0) {
         var1.HDLError(var7 + " is not a pin in " + var3);
      }

      int var12 = var6.getPinNumber(var7);
      PinInfo var13 = var6.getPinInfo(var11, var12);
      byte[] var14 = getSubBusAndCheck(var1, var4, var13.width);
      byte var15 = var14 == null ? var13.width : (byte)(var14[1] - var14[0] + 1);
      var10 = var5.indexOf("[");
      String var8 = var10 >= 0 ? var5.substring(0, var10) : var5;
      int var17 = 0;
      byte var18 = 0;
      boolean var19 = false;
      PinInfo var16;
      if (var8.equals(TRUE_NODE_INFO.name)) {
         var16 = TRUE_NODE_INFO;
         var9 = 6;
         var19 = true;
      } else if (var8.equals(FALSE_NODE_INFO.name)) {
         var16 = FALSE_NODE_INFO;
         var9 = 7;
         var19 = true;
      } else if (var8.equals(CLOCK_NODE_INFO.name)) {
         var16 = CLOCK_NODE_INFO;
         var9 = 8;
      } else {
         var18 = this.getPinType(var8);
         if ((var18 == 0 || var18 == 3) && !var5.equals(var8)) {
            var1.HDLError(var5 + ": sub bus of an internal node may not be used");
         }

         if (var18 == 0) {
            var18 = 3;
            var16 = new PinInfo();
            var16.name = var8;
            var16.width = var15;
            this.internalPinsInfo.addElement(var16);
            var17 = this.internalPinsInfo.size() - 1;
            this.registerPin(var16, (byte)3, var17);
         } else {
            var17 = this.getPinNumber(var8);
            var16 = this.getPinInfo(var18, var17);
         }
      }

      byte[] var20;
      int var21;
      if (var19) {
         if (!var8.equals(var5)) {
            var1.HDLError(var8 + " may not be subscripted");
         }

         var21 = var15;
         var20 = new byte[]{0, (byte)(var15 - 1)};
      } else {
         var20 = getSubBusAndCheck(var1, var5, var16.width);
         var21 = var20 == null ? var16.width : var20[1] - var20[0] + 1;
      }

      if (var15 != var21) {
         var1.HDLError(var7 + "(" + var15 + ") and " + var8 + "(" + var21 + ") have different bus widths");
      }

      if (var18 == 3 && var11 == 2) {
         if (var16.isInitialized(var20)) {
            var1.HDLError("An internal pin may only be fed once by a part's output pin");
         } else {
            var16.initialize(var20);
         }
      }

      if (var18 == 2 && var11 == 2) {
         if (var16.isInitialized(var20)) {
            var1.HDLError("An output pin may only be fed once by a part's output pin");
         } else {
            var16.initialize(var20);
         }
      }

      label65:
      switch(var11) {
      case 1:
         switch(var18) {
         case 1:
            var9 = 1;
            break label65;
         case 2:
            var1.HDLError("Can't connect gate's output pin to part");
            break label65;
         case 3:
            var9 = 3;
         default:
            break label65;
         }
      case 2:
         switch(var18) {
         case 1:
            var1.HDLError("Can't connect part's output pin to gate's input pin");
         case 3:
            var9 = 2;
            break;
         case 2:
            var9 = 5;
         }
      }

      Connection var22 = new Connection(var9, var17, var2, var7, var20, var14);
      this.connections.add(var22);
   }

   private Graph createConnectionsGraph() {
      Graph var1 = new Graph();
      Iterator var2 = this.connections.iterator();

      while(var2.hasNext()) {
         Connection var3 = (Connection)var2.next();
         Integer var4 = new Integer(var3.getPartNumber());
         int var5 = var3.getGatePinNumber();
         switch(var3.getType()) {
         case 1:
            if (this.isLegalToPartEdge(var3, var4)) {
               var1.addEdge(this.getPinInfo((byte)1, var5), var4);
            }
            break;
         case 2:
            if (this.isLegalFromPartEdge(var3, var4)) {
               var1.addEdge(var4, this.getPinInfo((byte)3, var5));
            }
            break;
         case 3:
            if (this.isLegalToPartEdge(var3, var4)) {
               var1.addEdge(this.getPinInfo((byte)3, var5), var4);
            }
         case 4:
         default:
            break;
         case 5:
            if (this.isLegalFromPartEdge(var3, var4)) {
               var1.addEdge(var4, this.getPinInfo((byte)2, var5));
            }
            break;
         case 6:
            if (this.isLegalToPartEdge(var3, var4)) {
               var1.addEdge(TRUE_NODE_INFO, var4);
            }
            break;
         case 7:
            if (this.isLegalToPartEdge(var3, var4)) {
               var1.addEdge(FALSE_NODE_INFO, var4);
            }
            break;
         case 8:
            if (this.isLegalToPartEdge(var3, var4)) {
               var1.addEdge(CLOCK_NODE_INFO, var4);
            }
         }
      }

      int var6;
      for(var6 = 0; var6 < this.partsList.size(); ++var6) {
         var1.addEdge(this.partsList, new Integer(var6));
      }

      for(var6 = 0; var6 < this.outputPinsInfo.length; ++var6) {
         var1.addEdge(this.outputPinsInfo[var6], this.outputPinsInfo);
      }

      for(var6 = 0; var6 < this.inputPinsInfo.length; ++var6) {
         var1.addEdge(this.inputPinsInfo, this.inputPinsInfo[var6]);
      }

      return var1;
   }

   private boolean isLegalToPartEdge(Connection var1, Integer var2) {
      GateClass var3 = (GateClass)this.partsList.elementAt(var2);
      int var4 = var3.getPinNumber(var1.getPartPinName());
      return !var3.isInputClocked[var4];
   }

   private boolean isLegalFromPartEdge(Connection var1, Integer var2) {
      GateClass var3 = (GateClass)this.partsList.elementAt(var2);
      int var4 = var3.getPinNumber(var1.getPartPinName());
      return !var3.isOutputClocked[var4];
   }

   public PinInfo getPinInfo(byte var1, int var2) {
      PinInfo var3 = null;
      if (var1 == 3) {
         if (var2 < this.internalPinsInfo.size()) {
            return (PinInfo)this.internalPinsInfo.elementAt(var2);
         }
      } else {
         var3 = super.getPinInfo(var1, var2);
      }

      return var3;
   }

   public Gate newInstance() throws InstantiationException {
      Node[] var1 = new Node[this.inputPinsInfo.length];
      Node[] var2 = new Node[this.outputPinsInfo.length];
      Node[] var3 = new Node[this.internalPinsInfo.size()];
      CompositeGate var4 = new CompositeGate();
      Gate[] var5 = new Gate[this.partsList.size()];

      for(int var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = ((GateClass)this.partsList.elementAt(var6)).newInstance();
         if (var5[var6] instanceof BuiltInGateWithGUI) {
            ((BuiltInGateWithGUI)var5[var6]).setParent(var4);
         }
      }

      Gate[] var19 = new Gate[var5.length];

      int var7;
      for(var7 = 0; var7 < var5.length; ++var7) {
         var19[var7] = var5[this.partsOrder[var7]];
      }

      for(var7 = 0; var7 < var1.length; ++var7) {
         var1[var7] = new Node();
      }

      for(var7 = 0; var7 < var2.length; ++var7) {
         var2[var7] = new Node();
      }

      DirtyGateAdapter var21 = new DirtyGateAdapter(var4);

      for(int var8 = 0; var8 < this.isInputClocked.length; ++var8) {
         if (!this.isInputClocked[var8]) {
            var1[var8].addListener(var21);
         }
      }

      ConnectionSet var20 = new ConnectionSet();
      Iterator var14 = this.connections.iterator();

      Node var9;
      byte[] var12;
      byte[] var13;
      while(var14.hasNext()) {
         Connection var15 = (Connection)var14.next();
         var12 = var15.getGateSubBus();
         var13 = var15.getPartSubBus();
         var9 = var5[var15.getPartNumber()].getNode(var15.getPartPinName());
         switch(var15.getType()) {
         case 1:
            this.connectGateToPart(var1[var15.getGatePinNumber()], var12, var9, var13);
            break;
         case 2:
            Object var11 = null;
            if (var13 == null) {
               var11 = new Node();
            } else {
               var11 = new SubNode(var13[0], var13[1]);
            }

            var9.addListener((Node)var11);
            var3[var15.getGatePinNumber()] = (Node)var11;
            break;
         case 3:
         case 6:
         case 7:
         case 8:
            var20.add(var15);
         case 4:
         default:
            break;
         case 5:
            this.connectGateToPart(var9, var13, var2[var15.getGatePinNumber()], var12);
         }
      }

      var14 = var20.iterator();
      boolean var22 = false;

      while(var14.hasNext()) {
         Connection var16 = (Connection)var14.next();
         var9 = var5[var16.getPartNumber()].getNode(var16.getPartPinName());
         var13 = var16.getPartSubBus();
         var12 = var16.getGateSubBus();
         Node var10 = null;
         SubNode var17;
         SubBusListeningAdapter var18;
         switch(var16.getType()) {
         case 3:
            var10 = var3[var16.getGatePinNumber()];
            if (var13 == null) {
               var10.addListener(var9);
            } else {
               SubBusListeningAdapter var23 = new SubBusListeningAdapter(var9, var13[0], var13[1]);
               var10.addListener(var23);
            }
         case 4:
         case 5:
         default:
            break;
         case 6:
            var17 = new SubNode(var12[0], var12[1]);
            var17.set(Gate.TRUE_NODE.get());
            if (var13 == null) {
               var9.set(var17.get());
            } else {
               var18 = new SubBusListeningAdapter(var9, var13[0], var13[1]);
               var18.set(var17.get());
            }
            break;
         case 7:
            var17 = new SubNode(var12[0], var12[1]);
            var17.set(Gate.FALSE_NODE.get());
            if (var13 == null) {
               var9.set(var17.get());
            } else {
               var18 = new SubBusListeningAdapter(var9, var13[0], var13[1]);
               var18.set(var17.get());
            }
            break;
         case 8:
            var9.set(Gate.CLOCK_NODE.get());
            Gate.CLOCK_NODE.addListener(var9);
            var22 = true;
         }
      }

      if (var22) {
         Gate.CLOCK_NODE.addListener(new DirtyGateAdapter(var4));
      }

      var4.init(var1, var2, var3, var19, this);
      return var4;
   }

   private void connectGateToPart(Node var1, byte[] var2, Node var3, byte[] var4) {
      Object var6 = var3;
      if (var4 != null) {
         var6 = new SubBusListeningAdapter(var3, var4[0], var4[1]);
      }

      if (var2 == null) {
         var1.addListener((Node)var6);
      } else {
         SubNode var7 = new SubNode(var2[0], var2[1]);
         var1.addListener(var7);
         var7.addListener((Node)var6);
      }

   }
}
