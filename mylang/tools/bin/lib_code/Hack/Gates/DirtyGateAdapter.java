package Hack.Gates;

public class DirtyGateAdapter extends Node {
   private Gate affectedGate;

   public DirtyGateAdapter(Gate var1) {
      this.affectedGate = var1;
   }

   public void set(short var1) {
      super.set(var1);
      this.affectedGate.setDirty();
   }
}
