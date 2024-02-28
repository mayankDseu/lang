package Hack.ComputerParts;

import Hack.Events.ErrorEventListener;

public interface InteractiveComputerPartGUI extends ComputerPartGUI {
   void addErrorListener(ErrorEventListener var1);

   void removeErrorListener(ErrorEventListener var1);

   void notifyErrorListeners(String var1);
}
