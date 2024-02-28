package Hack.VMEmulator;

import Hack.CPUEmulator.KeyboardGUI;
import Hack.CPUEmulator.ScreenGUI;
import Hack.ComputerParts.BusGUI;
import Hack.ComputerParts.LabeledPointedMemoryGUI;
import Hack.ComputerParts.MemorySegmentGUI;
import Hack.ComputerParts.PointedMemorySegmentGUI;
import Hack.Controller.HackSimulatorGUI;

public interface VMEmulatorGUI extends HackSimulatorGUI {
   BusGUI getBus();

   ScreenGUI getScreen();

   KeyboardGUI getKeyboard();

   LabeledPointedMemoryGUI getRAM();

   VMProgramGUI getProgram();

   CallStackGUI getCallStack();

   CalculatorGUI getCalculator();

   PointedMemorySegmentGUI getWorkingStack();

   PointedMemorySegmentGUI getStack();

   MemorySegmentGUI getStaticSegment();

   MemorySegmentGUI getLocalSegment();

   MemorySegmentGUI getArgSegment();

   MemorySegmentGUI getThisSegment();

   MemorySegmentGUI getThatSegment();

   MemorySegmentGUI getTempSegment();

   void requestFocus();
}
