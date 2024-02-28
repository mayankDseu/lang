package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPartGUI;

public interface GateInfoGUI extends ComputerPartGUI {
   void setTime(int var1);

   void setClocked(boolean var1);

   void setClock(boolean var1);

   void setChip(String var1);

   void enableTime();

   void disableTime();
}
