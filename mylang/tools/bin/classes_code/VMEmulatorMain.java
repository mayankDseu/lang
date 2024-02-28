import Hack.Controller.HackController;
import Hack.VMEmulator.VMEmulator;
import Hack.VMEmulator.VMEmulatorApplication;
import HackGUI.ControllerComponent;
import SimulatorsGUI.VMEmulatorComponent;
import javax.swing.UIManager;

public class VMEmulatorMain {
   public static void main(String[] var0) {
      if (var0.length > 1) {
         System.err.println("Usage: java CPUEmulatorMain [script name]");
      } else if (var0.length == 0) {
         try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
         } catch (Exception var4) {
         }

         VMEmulatorComponent var1 = new VMEmulatorComponent();
         ControllerComponent var2 = new ControllerComponent();
         new VMEmulatorApplication(var2, var1, "bin/scripts/defaultVM.txt", "bin/help/vmUsage.html", "bin/help/vmAbout.html");
      } else {
         new HackController(new VMEmulator(), var0[0]);
      }

   }
}
