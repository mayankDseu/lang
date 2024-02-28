package Hack.Gates;

import java.util.Vector;

public abstract class Gate {
   public static final Node TRUE_NODE = new Node((short)-1);
   public static final Node FALSE_NODE = new Node((short)0);
   public static final Node CLOCK_NODE = new Node();
   protected Node[] inputPins;
   protected Node[] outputPins;
   protected GateClass gateClass;
   protected boolean isDirty;
   private Vector dirtyGateListeners;

   public void addDirtyGateListener(DirtyGateListener var1) {
      if (this.dirtyGateListeners == null) {
         this.dirtyGateListeners = new Vector(1, 1);
      }

      this.dirtyGateListeners.add(var1);
   }

   public void removeDirtyGateListener(DirtyGateListener var1) {
      if (this.dirtyGateListeners != null) {
         this.dirtyGateListeners.remove(var1);
      }

   }

   protected abstract void reCompute();

   protected abstract void clockUp();

   protected abstract void clockDown();

   public void setDirty() {
      this.isDirty = true;
      if (this.dirtyGateListeners != null) {
         for(int var1 = 0; var1 < this.dirtyGateListeners.size(); ++var1) {
            ((DirtyGateListener)this.dirtyGateListeners.elementAt(var1)).gotDirty();
         }
      }

   }

   public GateClass getGateClass() {
      return this.gateClass;
   }

   public Node getNode(String var1) {
      Node var2 = null;
      byte var3 = this.gateClass.getPinType(var1);
      int var4 = this.gateClass.getPinNumber(var1);
      switch(var3) {
      case 1:
         var2 = this.inputPins[var4];
         break;
      case 2:
         var2 = this.outputPins[var4];
      }

      return var2;
   }

   public Node[] getInputNodes() {
      return this.inputPins;
   }

   public Node[] getOutputNodes() {
      return this.outputPins;
   }

   public void eval() {
      if (this.isDirty) {
         this.doEval();
      }

   }

   private void doEval() {
      if (this.isDirty) {
         this.isDirty = false;
         if (this.dirtyGateListeners != null) {
            for(int var1 = 0; var1 < this.dirtyGateListeners.size(); ++var1) {
               ((DirtyGateListener)this.dirtyGateListeners.elementAt(var1)).gotClean();
            }
         }
      }

      this.reCompute();
   }

   public void tick() {
      this.doEval();
      this.clockUp();
   }

   public void tock() {
      this.clockDown();
      this.doEval();
   }
}
