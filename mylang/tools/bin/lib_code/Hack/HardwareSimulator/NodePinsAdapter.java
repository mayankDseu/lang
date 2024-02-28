package Hack.HardwareSimulator;

import Hack.Gates.Node;

public class NodePinsAdapter extends Node {
   private Pins pins;
   private int index;

   public NodePinsAdapter(Pins var1, int var2) {
      this.pins = var1;
      this.index = var2;
   }

   public void set(short var1) {
      this.pins.updateGUI(this.index, var1);
   }
}
