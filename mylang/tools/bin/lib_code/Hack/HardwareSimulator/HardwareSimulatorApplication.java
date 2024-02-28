package Hack.HardwareSimulator;

import Hack.Controller.ControllerException;
import Hack.Controller.ControllerGUI;
import Hack.Controller.HackApplication;
import Hack.Controller.HackSimulator;
import Hack.Controller.ScriptException;

public class HardwareSimulatorApplication extends HackApplication {
   public HardwareSimulatorApplication(HardwareSimulatorControllerGUI var1, HardwareSimulatorGUI var2, String var3, String var4, String var5) {
      super(new HardwareSimulator(var2), var1, var2, var3, var4, var5);
   }

   protected void createController(HackSimulator var1, ControllerGUI var2, String var3) throws ScriptException, ControllerException {
      new HardwareSimulatorController((HardwareSimulatorControllerGUI)var2, (HardwareSimulator)var1, var3);
   }
}
