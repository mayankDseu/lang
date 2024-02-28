package Hack.CPUEmulator;

import Hack.ComputerParts.ComputerPart;
import Hack.ComputerParts.ComputerPartGUI;
import Hack.Utilities.Definitions;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard extends ComputerPart implements KeyListener {
   private RAM ram;
   private KeyboardGUI gui;

   public Keyboard(RAM var1, KeyboardGUI var2) {
      super(var2 != null);
      this.ram = var1;
      this.gui = var2;
      if (this.hasGUI) {
         var2.getKeyEventHandler().addKeyListener(this);
      }

   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void keyPressed(KeyEvent var1) {
      short var2 = Definitions.getInstance().getKeyCode(var1);
      if (var2 > 0) {
         this.ram.setValueAt(24576, var2, true);
         if (this.hasGUI) {
            this.gui.setKey(Definitions.getInstance().getKeyName(var1));
         }
      }

   }

   public void keyReleased(KeyEvent var1) {
      this.ram.setValueAt(24576, (short)0, true);
      this.gui.clearKey();
   }

   public void keyTyped(KeyEvent var1) {
   }

   public void refreshGUI() {
   }

   public void requestFocus() {
      if (this.hasGUI) {
         this.gui.getKeyEventHandler().requestFocus();
      }

   }
}
