package Hack.HardwareSimulator;

import Hack.ComputerParts.ComputerPartGUI;
import Hack.Gates.Gate;

public interface PartsGUI extends ComputerPartGUI {
   void setContents(Gate[] var1);
}
