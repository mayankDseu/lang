package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPartGUI;
import Hack.ComputerParts.ValueComputerPart;
import Hack.Gates.CompositeGateClass;
import Hack.Gates.Gate;
import Hack.Gates.GateClass;
import Hack.Gates.Node;
import Hack.Gates.PinInfo;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class PartPins extends ValueComputerPart {
   private PartPinsGUI gui;
   private Vector partPins;
   private Gate gate;
   private GateClass partGateClass;
   private Hashtable nodes;

   public PartPins(PartPinsGUI var1) {
      super(var1 != null);
      this.gui = var1;
      this.partPins = new Vector();
      this.nodes = new Hashtable();
      this.clearGate();
   }

   private void clearGate() {
      this.gate = null;
      this.clearPart();
   }

   private void clearPart() {
      this.partPins.removeAllElements();
      this.partGateClass = null;
      Enumeration var1 = this.nodes.keys();

      while(var1.hasMoreElements()) {
         Node var2 = (Node)var1.nextElement();
         Node var3 = (Node)this.nodes.get(var2);
         var2.removeListener(var3);
      }

      this.refreshGUI();
   }

   public void setGate(Gate var1) {
      this.clearGate();
      this.gate = var1;
   }

   public void setPart(GateClass var1, String var2) {
      this.clearPart();
      this.partGateClass = var1;
      if (this.hasGUI) {
         this.gui.setPartName(var2);
      }

   }

   public void addPin(String var1, String var2) {
      if (this.gate != null && this.partGateClass != null) {
         String var3 = var1;
         String var4 = var2;
         if (var1.indexOf("[") >= 0) {
            var3 = var1.substring(0, var1.indexOf("["));
         }

         if (var2.indexOf("[") >= 0) {
            var4 = var2.substring(0, var2.indexOf("["));
         }

         PinInfo var5 = this.partGateClass.getPinInfo(var3);
         PartPinInfo var6 = new PartPinInfo();
         var6.partPinName = var5.name;

         try {
            var6.partPinSubBus = CompositeGateClass.getSubBus(var1);
         } catch (Exception var11) {
         }

         boolean var8 = false;
         Node var7;
         PinInfo var9;
         if (var4.equals(CompositeGateClass.TRUE_NODE_INFO.name)) {
            var7 = Gate.TRUE_NODE;
            var6.gatePinName = CompositeGateClass.TRUE_NODE_INFO.name;
            var8 = true;
         } else if (var4.equals(CompositeGateClass.FALSE_NODE_INFO.name)) {
            var7 = Gate.FALSE_NODE;
            var6.gatePinName = CompositeGateClass.FALSE_NODE_INFO.name;
            var8 = true;
         } else if (var4.equals(CompositeGateClass.CLOCK_NODE_INFO.name)) {
            var7 = Gate.CLOCK_NODE;
            var6.gatePinName = CompositeGateClass.CLOCK_NODE_INFO.name;
         } else {
            var7 = this.gate.getNode(var4);
            var9 = this.gate.getGateClass().getPinInfo(var4);
            var6.gatePinName = var9.name;
         }

         if (var8) {
            var6.gatePinSubBus = new byte[]{0, (byte)(var5.width - 1)};
         } else {
            try {
               var6.gatePinSubBus = CompositeGateClass.getSubBus(var2);
            } catch (Exception var10) {
            }
         }

         var9 = null;
         Object var12;
         if (var6.gatePinSubBus == null) {
            var12 = new NodePartPinsAdapter(this, this.partPins.size());
         } else {
            var12 = new SubNodePartPinsAdapter(var6.gatePinSubBus[0], var6.gatePinSubBus[1], this, this.partPins.size());
         }

         var7.addListener((Node)var12);
         this.nodes.put(var7, var12);
         this.partPins.addElement(var6);
         this.refreshGUI();
         ((Node)var12).set(var7.get());
         this.reset();
      }

   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public short getValueAt(int var1) {
      return ((PartPinInfo)this.partPins.elementAt(var1)).value;
   }

   public void refreshGUI() {
      if (this.displayChanges) {
         this.gui.setContents(this.partPins);
      }

   }

   public void setValueAt(int var1, short var2, boolean var3) {
      if (this.getValueAt(var1) != var2) {
         super.setValueAt(var1, var2, var3);
      }

   }

   public void doSetValueAt(int var1, short var2) {
   }
}
