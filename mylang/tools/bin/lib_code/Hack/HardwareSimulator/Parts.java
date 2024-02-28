package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPart;
import Hack.ComputerParts.ComputerPartGUI;
import Hack.Gates.Gate;

public class Parts extends ComputerPart {
   private PartsGUI gui;
   private Gate[] parts;

   public Parts(PartsGUI var1) {
      super(var1 != null);
      this.gui = var1;
      this.parts = new Gate[0];
      this.refreshGUI();
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void refreshGUI() {
      if (this.displayChanges) {
         this.gui.setContents(this.parts);
      }

   }

   public void setParts(Gate[] var1) {
      this.parts = var1;
      this.refreshGUI();
   }
}
