package Hack.VMEmulator;

import Hack.ComputerParts.ValueComputerPartGUI;

public interface CalculatorGUI extends ValueComputerPartGUI {
   void setOperator(char var1);

   void showCalculator();

   void hideCalculator();

   void showLeftInput();

   void hideLeftInput();
}
