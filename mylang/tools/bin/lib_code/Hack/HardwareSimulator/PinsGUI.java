package Hack.HardwareSimulator;

import Hack.ComputerParts.InteractiveValueComputerPartGUI;
import Hack.Gates.PinInfo;

public interface PinsGUI extends InteractiveValueComputerPartGUI {
   void setContents(PinInfo[] var1);

   void setDimmed(boolean var1);
}
