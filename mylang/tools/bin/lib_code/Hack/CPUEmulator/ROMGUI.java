package Hack.CPUEmulator;

import Hack.ComputerParts.PointedMemoryGUI;
import Hack.Events.ProgramEventListener;

public interface ROMGUI extends PointedMemoryGUI {
   void addProgramListener(ProgramEventListener var1);

   void removeProgramListener(ProgramEventListener var1);

   void notifyProgramListeners(byte var1, String var2);

   void setProgram(String var1);

   void showMessage(String var1);

   void hideMessage();
}
