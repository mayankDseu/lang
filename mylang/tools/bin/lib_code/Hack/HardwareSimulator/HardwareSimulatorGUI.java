package Hack.HardwareSimulator;

import Hack.ComputerParts.TextFileGUI;
import Hack.Controller.HackSimulatorGUI;
import Hack.Gates.GatesPanelGUI;

public interface HardwareSimulatorGUI extends HackSimulatorGUI {
   GatesPanelGUI getGatesPanel();

   TextFileGUI getHDLView();

   GateInfoGUI getGateInfo();

   PinsGUI getInputPins();

   PinsGUI getOutputPins();

   PinsGUI getInternalPins();

   PartPinsGUI getPartPins();

   PartsGUI getParts();

   void showInternalPins();

   void hideInternalPins();

   void showPartPins();

   void hidePartPins();

   void showParts();

   void hideParts();
}
