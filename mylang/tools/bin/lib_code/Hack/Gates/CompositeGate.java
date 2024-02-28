package Hack.Gates;

public class CompositeGate extends Gate {
   protected Node[] internalPins;
   protected Gate[] parts;

   protected void clockUp() {
      if (this.gateClass.isClocked) {
         for(int var1 = 0; var1 < this.parts.length; ++var1) {
            this.parts[var1].tick();
         }
      }

   }

   protected void clockDown() {
      if (this.gateClass.isClocked) {
         for(int var1 = 0; var1 < this.parts.length; ++var1) {
            this.parts[var1].tock();
         }
      }

   }

   protected void reCompute() {
      for(int var1 = 0; var1 < this.parts.length; ++var1) {
         this.parts[var1].eval();
      }

   }

   public Node getNode(String var1) {
      Node var2 = super.getNode(var1);
      if (var2 == null) {
         byte var3 = this.gateClass.getPinType(var1);
         int var4 = this.gateClass.getPinNumber(var1);
         if (var3 == 3) {
            var2 = this.internalPins[var4];
         }
      }

      return var2;
   }

   public Node[] getInternalNodes() {
      return this.internalPins;
   }

   public Gate[] getParts() {
      return this.parts;
   }

   public void init(Node[] var1, Node[] var2, Node[] var3, Gate[] var4, GateClass var5) {
      this.inputPins = var1;
      this.outputPins = var2;
      this.internalPins = var3;
      this.parts = var4;
      this.gateClass = var5;
      this.setDirty();
   }
}
