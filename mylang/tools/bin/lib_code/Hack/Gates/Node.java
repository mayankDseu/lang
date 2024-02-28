package Hack.Gates;

public class Node {
   protected short value;
   protected NodeSet listeners;

   public Node() {
   }

   public Node(short var1) {
      this.value = var1;
   }

   public void addListener(Node var1) {
      if (this.listeners == null) {
         this.listeners = new NodeSet();
      }

      this.listeners.add(var1);
   }

   public void removeListener(Node var1) {
      if (this.listeners != null) {
         this.listeners.remove(var1);
      }

   }

   public short get() {
      return this.value;
   }

   public void set(short var1) {
      if (this.value != var1) {
         this.value = var1;
         if (this.listeners != null) {
            for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
               this.listeners.getNodeAt(var2).set(this.get());
            }
         }
      }

   }
}
