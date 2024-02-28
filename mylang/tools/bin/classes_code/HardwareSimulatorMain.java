import Hack.Controller.HackController;
import Hack.HardwareSimulator.HardwareSimulator;
import Hack.HardwareSimulator.HardwareSimulatorApplication;
import SimulatorsGUI.HardwareSimulatorComponent;
import SimulatorsGUI.HardwareSimulatorControllerComponent;
import javax.swing.UIManager;

public class HardwareSimulatorMain {
   public static void main(String[] var0) {
      if (var0.length > 1) {
         System.err.println("Usage: java HardwareSimulatorMain [script name]");
      } else if (var0.length == 0) {
         try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
         } catch (Exception var4) {
         }

         HardwareSimulatorComponent var1 = new HardwareSimulatorComponent();
         HardwareSimulatorControllerComponent var2 = new HardwareSimulatorControllerComponent();
         new HardwareSimulatorApplication(var2, var1, "bin/scripts/defaultHW.txt", "bin/help/hwUsage.html", "bin/help/hwAbout.html");
      } else {
         new HackController(new HardwareSimulator(), var0[0]);
      }

   }
}
