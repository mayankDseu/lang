package Hack.VMEmulator;

import Hack.ComputerParts.InteractiveComputerPartGUI;
import Hack.Events.ProgramEventListener;

public interface VMProgramGUI extends InteractiveComputerPartGUI {
   void addProgramListener(ProgramEventListener var1);

   void removeProgramListener(ProgramEventListener var1);

   void notifyProgramListeners(byte var1, String var2);

   void setContents(VMEmulatorInstruction[] var1, int var2);

   void setCurrentInstruction(int var1);

   void showMessage(String var1);

   void hideMessage();

   boolean confirmBuiltInAccess();

   void notify(String var1);
}
