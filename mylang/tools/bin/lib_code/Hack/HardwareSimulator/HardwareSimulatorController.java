package Hack.HardwareSimulator;

import Hack.Controller.ControllerException;
import Hack.Controller.HackController;
import Hack.Controller.ScriptException;
import Hack.Gates.GateException;
import Hack.Gates.GatesManager;
import java.io.File;

public class HardwareSimulatorController extends HackController {
   public HardwareSimulatorController(HardwareSimulatorControllerGUI var1, HardwareSimulator var2, String var3) throws ScriptException, ControllerException {
      super(var1, var2, var3);
      var1.disableEval();
      var1.disableTickTock();
   }

   protected void updateProgramFile(String var1) {
      super.updateProgramFile(var1);
      File var2 = (new File(var1)).getParentFile();
      GatesManager.getInstance().setWorkingDir(var2);
   }

   protected void doUnknownAction(byte var1, Object var2) {
      switch(var1) {
      case 100:
         ((HardwareSimulator)this.simulator).runTickTockTask();
         break;
      case 101:
         ((HardwareSimulator)this.simulator).runEvalTask();
         break;
      case 102:
         File var3 = (File)var2;
         this.updateProgramFile(var3.getPath());
         if (!this.singleStepLocked) {
            this.reloadDefaultScript();
         }

         HardwareSimulatorController.LoadChipTask var4 = new HardwareSimulatorController.LoadChipTask(var3.getPath());
         Thread var5 = new Thread(var4);
         var5.start();
      case 103:
      case 104:
      default:
         break;
      case 105:
         ((HardwareSimulatorControllerGUI)this.gui).disableTickTock();
         ((HardwareSimulatorControllerGUI)this.gui).disableTickTock();
         break;
      case 106:
         ((HardwareSimulatorControllerGUI)this.gui).enableTickTock();
         break;
      case 107:
         ((HardwareSimulatorControllerGUI)this.gui).disableEval();
         break;
      case 108:
         ((HardwareSimulatorControllerGUI)this.gui).enableEval();
      }

   }

   class LoadChipTask implements Runnable {
      private String chipName;

      public LoadChipTask(String var2) {
         this.chipName = var2;
      }

      public void run() {
         try {
            ((HardwareSimulator)HardwareSimulatorController.this.simulator).loadGate(this.chipName, true);
         } catch (GateException var2) {
            HardwareSimulatorController.this.gui.displayMessage(var2.getMessage(), true);
         }

      }
   }
}
