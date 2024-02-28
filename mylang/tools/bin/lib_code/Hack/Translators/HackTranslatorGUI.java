package Hack.Translators;

import Hack.ComputerParts.TextFileGUI;
import java.io.File;

public interface HackTranslatorGUI {
   void addHackTranslatorListener(HackTranslatorEventListener var1);

   void removeHackTranslatorListener(HackTranslatorEventListener var1);

   void notifyHackTranslatorListeners(byte var1, Object var2);

   void displayMessage(String var1, boolean var2);

   void setTitle(String var1);

   TextFileGUI getSource();

   TextFileGUI getDestination();

   void setSourceName(String var1);

   void setDestinationName(String var1);

   void setWorkingDir(File var1);

   void setUsageFileName(String var1);

   void setAboutFileName(String var1);

   void enableSingleStep();

   void disableSingleStep();

   void enableFastForward();

   void disableFastForward();

   void enableStop();

   void disableStop();

   void enableRewind();

   void disableRewind();

   void enableFullCompilation();

   void disableFullCompilation();

   void enableSave();

   void disableSave();

   void enableLoadSource();

   void disableLoadSource();

   void enableSourceRowSelection();

   void disableSourceRowSelection();
}
