package Hack.HardwareSimulator;

import Hack.Gates.Node;

public class NodePartPinsAdapter extends Node {
   private PartPins partPins;
   private int index;

   public NodePartPinsAdapter(PartPins var1, int var2) {
      this.partPins = var1;
      this.index = var2;
   }

   public void set(short var1) {
      this.value = var1;
      this.partPins.quietUpdateGUI(this.index, this.get());
   }
}
