package Hack.CPUEmulator;

import Hack.ComputerParts.ValueComputerPartGUI;

public interface ALUGUI extends ValueComputerPartGUI {
   void setCommand(String var1);

   void bodyFlash();

   void hideBodyFlash();

   void commandFlash();

   void hideCommandFlash();
}
