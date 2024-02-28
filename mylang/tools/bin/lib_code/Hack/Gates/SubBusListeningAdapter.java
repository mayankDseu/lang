package Hack.Gates;

public class SubBusListeningAdapter extends Node {
   private short mask;
   private byte shiftLeft;
   private Node targetNode;

   public SubBusListeningAdapter(Node var1, byte var2, byte var3) {
      this.mask = SubNode.getMask(var2, var3);
      this.shiftLeft = var2;
      this.targetNode = var1;
   }

   public void set(short var1) {
      short var2 = (short)(this.targetNode.get() & ~this.mask);
      short var3 = (short)((short)(var1 << this.shiftLeft) & this.mask);
      this.targetNode.set((short)(var2 | var3));
   }
}
