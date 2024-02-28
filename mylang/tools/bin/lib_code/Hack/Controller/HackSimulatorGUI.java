package Hack.Controller;

import java.io.File;
import javax.swing.JComponent;

public interface HackSimulatorGUI {
   void setAdditionalDisplay(JComponent var1);

   void loadProgram();

   void setUsageFileName(String var1);

   void setAboutFileName(String var1);

   String getUsageFileName();

   String getAboutFileName();

   void setWorkingDir(File var1);
}
