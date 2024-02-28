package Hack.HardwareSimulator;

import Hack.Gates.SubNode;

public class SubNodePartPinsAdapter extends SubNode {
   private PartPins partPins;
   private int index;

   public SubNodePartPinsAdapter(byte var1, byte var2, PartPins var3, int var4) {
      super(var1, var2);
      this.partPins = var3;
      this.index = var4;
   }

   public void set(short var1) {
      this.value = var1;
      this.partPins.quietUpdateGUI(this.index, this.get());
   }
}
