package Hack.HardwareSimulator;

import Hack.Controller.ControllerGUI;

public interface HardwareSimulatorControllerGUI extends ControllerGUI {
   void enableTickTock();

   void disableTickTock();

   void enableEval();

   void disableEval();
}
