package Hack.CPUEmulator;

import Hack.ComputerParts.ComputerPartGUI;
import javax.swing.JComponent;

public interface KeyboardGUI extends ComputerPartGUI {
   void setKey(String var1);

   void clearKey();

   JComponent getKeyEventHandler();
}
