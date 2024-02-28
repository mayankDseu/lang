package Hack.ComputerParts;

public interface InteractiveValueComputerPartGUI extends ValueComputerPartGUI, InteractiveComputerPartGUI {
   void addListener(ComputerPartEventListener var1);

   void removeListener(ComputerPartEventListener var1);

   void notifyListeners(int var1, short var2);

   void notifyListeners();

   void enableUserInput();

   void disableUserInput();

   void setEnabledRange(int var1, int var2, boolean var3);
}
