package Hack.Controller;

import java.io.File;
import java.util.Vector;
import javax.swing.JComponent;

public interface ControllerGUI {
   void addControllerListener(ControllerEventListener var1);

   void removeControllerListener(ControllerEventListener var1);

   void notifyControllerListeners(byte var1, Object var2);

   void setSimulator(HackSimulatorGUI var1);

   void setTitle(String var1);

   void displayMessage(String var1, boolean var2);

   void setWorkingDir(File var1);

   void setScriptFile(String var1);

   void setCurrentScriptLine(int var1);

   JComponent getScriptComponent();

   void setOutputFile(String var1);

   void setCurrentOutputLine(int var1);

   JComponent getOutputComponent();

   void setComparisonFile(String var1);

   void setCurrentComparisonLine(int var1);

   JComponent getComparisonComponent();

   void setAdditionalDisplay(int var1);

   void setBreakpoints(Vector var1);

   void setVariables(String[] var1);

   void setSpeed(int var1);

   void setAnimationMode(int var1);

   void setNumericFormat(int var1);

   void showBreakpoints();

   void outputFileUpdated();

   void enableSingleStep();

   void disableSingleStep();

   void enableFastForward();

   void disableFastForward();

   void enableStop();

   void disableStop();

   void enableScript();

   void disableScript();

   void enableRewind();

   void disableRewind();

   void enableLoadProgram();

   void disableLoadProgram();

   void enableSpeedSlider();

   void disableSpeedSlider();

   void enableAnimationModes();

   void disableAnimationModes();
}
