package Hack.CPUEmulator;

import Hack.ComputerParts.ComputerPartGUI;

public interface ScreenGUI extends ComputerPartGUI {
   void setValueAt(int var1, short var2);

   void setContents(short[] var1);

   void refresh();

   void startAnimation();

   void stopAnimation();
}
