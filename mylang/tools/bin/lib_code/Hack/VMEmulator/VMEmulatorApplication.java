package Hack.VMEmulator;

import Hack.Controller.ControllerGUI;
import Hack.Controller.HackApplication;

public class VMEmulatorApplication extends HackApplication {
   public VMEmulatorApplication(ControllerGUI var1, VMEmulatorGUI var2, String var3, String var4, String var5) {
      super(new VMEmulator(var2), var1, var2, var3, var4, var5);
   }
}
