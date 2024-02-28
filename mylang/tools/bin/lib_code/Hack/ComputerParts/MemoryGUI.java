package Hack.ComputerParts;

import Hack.Events.ClearEventListener;

public interface MemoryGUI extends InteractiveValueComputerPartGUI {
   void addClearListener(ClearEventListener var1);

   void removeClearListener(ClearEventListener var1);

   void notifyClearListeners();

   void setContents(short[] var1);

   void select(int var1, int var2);

   void hideSelect();

   void scrollTo(int var1);
}
