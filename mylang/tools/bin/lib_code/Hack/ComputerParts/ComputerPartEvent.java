package Hack.ComputerParts;

import java.util.EventObject;

public class ComputerPartEvent extends EventObject {
   private int index;
   private short value;

   public ComputerPartEvent(ComputerPartGUI var1) {
      super(var1);
   }

   public ComputerPartEvent(ComputerPartGUI var1, int var2, short var3) {
      super(var1);
      this.index = var2;
      this.value = var3;
   }

   public int getIndex() {
      return this.index;
   }

   public short getValue() {
      return this.value;
   }
}
