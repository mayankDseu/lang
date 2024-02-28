package Hack.ComputerParts;

import java.util.EventObject;

public class TextFileEvent extends EventObject {
   private int rowIndex;
   private String rowString;

   public TextFileEvent(Object var1, String var2, int var3) {
      super(var1);
      this.rowString = var2;
      this.rowIndex = var3;
   }

   public String getRowString() {
      return this.rowString;
   }

   public int getRowIndex() {
      return this.rowIndex;
   }
}
