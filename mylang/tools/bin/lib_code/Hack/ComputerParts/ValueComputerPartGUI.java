package Hack.ComputerParts;

import java.awt.Point;

public interface ValueComputerPartGUI extends ComputerPartGUI {
   Point getCoordinates(int var1);

   void setValueAt(int var1, short var2);

   String getValueAsString(int var1);

   void highlight(int var1);

   void hideHighlight();

   void flash(int var1);

   void hideFlash();

   void setNumericFormat(int var1);

   void setNullValue(short var1, boolean var2);
}
