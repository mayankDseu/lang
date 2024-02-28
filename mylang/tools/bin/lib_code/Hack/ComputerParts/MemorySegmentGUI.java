package Hack.ComputerParts;

public interface MemorySegmentGUI extends InteractiveValueComputerPartGUI {
   void addListener(ComputerPartEventListener var1);

   void removeListener(ComputerPartEventListener var1);

   void notifyListeners(int var1, short var2);

   void setStartAddress(int var1);

   void hideSelect();

   void scrollTo(int var1);
}
