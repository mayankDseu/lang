package Hack.Gates;

import java.util.Vector;

public class NodeSet extends Vector {
   public NodeSet() {
      super(1, 1);
   }

   public boolean add(Node var1) {
      return super.add(var1);
   }

   public boolean remove(Node var1) {
      return super.remove(var1);
   }

   public boolean contains(Node var1) {
      return super.contains(var1);
   }

   public Node getNodeAt(int var1) {
      return (Node)this.elementAt(var1);
   }
}
