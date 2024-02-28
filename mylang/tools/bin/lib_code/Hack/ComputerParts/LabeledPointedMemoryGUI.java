package Hack.ComputerParts;

public interface LabeledPointedMemoryGUI extends PointedMemoryGUI {
   void setLabel(int var1, String var2);

   void clearLabels();

   void labelFlash(int var1);

   void hideLabelFlash();
}
