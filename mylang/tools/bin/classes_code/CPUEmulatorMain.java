import Hack.CPUEmulator.CPUEmulator;
import Hack.CPUEmulator.CPUEmulatorApplication;
import Hack.Controller.HackController;
import HackGUI.ControllerComponent;
import SimulatorsGUI.CPUEmulatorComponent;
import javax.swing.UIManager;

public class CPUEmulatorMain {
   public static void main(String[] var0) {
      if (var0.length > 1) {
         System.err.println("Usage: java CPUEmulatorMain [script name]");
      } else if (var0.length == 0) {
         try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
         } catch (Exception var4) {
         }

         CPUEmulatorComponent var1 = new CPUEmulatorComponent();
         ControllerComponent var2 = new ControllerComponent();
         new CPUEmulatorApplication(var2, var1, "bin/scripts/defaultCPU.txt", "bin/help/cpuUsage.html", "bin/help/cpuAbout.html");
      } else {
         new HackController(new CPUEmulator(), var0[0]);
      }

   }
}
