package Hack.Assembler;

import Hack.ComputerParts.TextFileGUI;
import Hack.Translators.HackTranslatorGUI;

public interface HackAssemblerGUI extends HackTranslatorGUI {
   TextFileGUI getComparison();

   void setComparisonName(String var1);

   void enableLoadComparison();

   void disableLoadComparison();

   void showComparison();

   void hideComparison();
}
