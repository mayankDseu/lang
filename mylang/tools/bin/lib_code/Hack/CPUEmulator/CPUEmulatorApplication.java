package Hack.CPUEmulator;

import Hack.Controller.ControllerGUI;
import Hack.Controller.HackApplication;

public class CPUEmulatorApplication extends HackApplication {
   public CPUEmulatorApplication(ControllerGUI var1, CPUEmulatorGUI var2, String var3, String var4, String var5) {
      super(new CPUEmulator(var2), var1, var2, var3, var4, var5);
   }
}
