package Hack.CPUEmulator;

import Hack.ComputerParts.BusGUI;
import Hack.ComputerParts.PointedMemoryGUI;
import Hack.ComputerParts.RegisterGUI;
import Hack.Controller.HackSimulatorGUI;
import java.awt.event.KeyListener;

public interface CPUEmulatorGUI extends HackSimulatorGUI {
   BusGUI getBus();

   ScreenGUI getScreen();

   KeyboardGUI getKeyboard();

   PointedMemoryGUI getRAM();

   ROMGUI getROM();

   RegisterGUI getA();

   RegisterGUI getD();

   RegisterGUI getPC();

   ALUGUI getALU();

   void addKeyListener(KeyListener var1);

   void requestFocus();
}
