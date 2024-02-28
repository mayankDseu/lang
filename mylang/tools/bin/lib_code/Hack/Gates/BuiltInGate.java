package Hack.Gates;

public abstract class BuiltInGate extends Gate {
   protected void clockUp() {
   }

   protected void clockDown() {
   }

   protected void reCompute() {
   }

   public void init(Node[] var1, Node[] var2, GateClass var3) {
      this.inputPins = var1;
      this.outputPins = var2;
      this.gateClass = var3;
      this.setDirty();
   }
}
