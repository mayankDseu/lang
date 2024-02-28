package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.ComputerPartGUI;
import Hack.ComputerParts.InteractiveValueComputerPart;
import Hack.Gates.GateClass;
import Hack.Gates.Node;
import Hack.Gates.PinInfo;

public class Pins extends InteractiveValueComputerPart {
   private PinsGUI gui;
   private byte type;
   private Node[] nodes;
   private PinInfo[] pins;

   public Pins(byte var1, PinsGUI var2) {
      super(var2 != null);
      this.gui = var2;
      this.type = var1;
      this.pins = new PinInfo[0];
      this.nodes = new Node[0];
      if (this.hasGUI) {
         var2.addListener(this);
         var2.addErrorListener(this);
         var2.setContents(this.pins);
      }

   }

   public void setNodes(Node[] var1, GateClass var2) {
      this.nodes = var1;
      this.pins = new PinInfo[var1.length];

      for(int var3 = 0; var3 < this.pins.length; ++var3) {
         this.pins[var3] = var2.getPinInfo(this.type, var3);
         this.pins[var3].value = var1[var3].get();
         var1[var3].addListener(new NodePinsAdapter(this, var3));
      }

      if (this.hasGUI) {
         this.gui.setContents(this.pins);
      }

   }

   public PinInfo getPinInfo(int var1) {
      return this.pins[var1];
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void doSetValueAt(int var1, short var2) {
      this.nodes[var1].set(var2);
   }

   public short getValueAt(int var1) {
      return this.nodes[var1].get();
   }

   public void refreshGUI() {
      if (this.displayChanges) {
         for(int var1 = 0; var1 < this.pins.length; ++var1) {
            this.pins[var1].value = this.nodes[var1].get();
         }

         this.gui.setContents(this.pins);
      }

   }

   public void reset() {
      this.gui.reset();

      for(int var1 = 0; var1 < this.nodes.length; ++var1) {
         this.nodes[var1].set((short)0);
      }

      this.refreshGUI();
   }

   public int getCount() {
      return this.nodes.length;
   }

   public boolean isLegalWidth(int var1, short var2) {
      byte var3 = this.pins[var1].width;
      int var4 = var2 > 0 ? (int)(Math.log((double)var2) / Math.log(2.0D)) + 1 : 1;
      return var4 <= var3;
   }

   public void valueChanged(ComputerPartEvent var1) {
      this.clearErrorListeners();
      int var2 = var1.getIndex();
      short var3 = var1.getValue();
      if (this.isLegalWidth(var2, var3)) {
         this.setValueAt(var2, var3, true);
      } else {
         this.notifyErrorListeners("Value doesn't match the pin's width");
         this.quietUpdateGUI(var2, this.nodes[var1.getIndex()].get());
      }

   }
}
