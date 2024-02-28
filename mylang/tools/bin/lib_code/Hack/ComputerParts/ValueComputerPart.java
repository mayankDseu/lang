package Hack.ComputerParts;

public abstract class ValueComputerPart extends ComputerPart {
   private static final int FLASH_TIME = 500;
   protected short nullValue;

   public ValueComputerPart(boolean var1) {
      super(var1);
   }

   public void setValueAt(int var1, short var2, boolean var3) {
      this.doSetValueAt(var1, var2);
      if (this.displayChanges) {
         if (var3) {
            this.quietUpdateGUI(var1, var2);
         } else {
            this.updateGUI(var1, var2);
         }
      }

   }

   public abstract void doSetValueAt(int var1, short var2);

   public abstract short getValueAt(int var1);

   public synchronized void updateGUI(int var1, short var2) {
      if (this.displayChanges) {
         ValueComputerPartGUI var3 = (ValueComputerPartGUI)this.getGUI();
         var3.setValueAt(var1, var2);
         if (this.animate) {
            var3.flash(var1);

            try {
               this.wait(500L);
            } catch (InterruptedException var5) {
            }

            var3.hideFlash();
         }

         var3.highlight(var1);
      }

   }

   public void quietUpdateGUI(int var1, short var2) {
      if (this.displayChanges) {
         ((ValueComputerPartGUI)this.getGUI()).setValueAt(var1, var2);
      }

   }

   public void hideHighlight() {
      if (this.displayChanges) {
         ((ValueComputerPartGUI)this.getGUI()).hideHighlight();
      }

   }

   public void setNumericFormat(int var1) {
      if (this.displayChanges) {
         ((ValueComputerPartGUI)this.getGUI()).setNumericFormat(var1);
      }

   }

   public void setNullValue(short var1, boolean var2) {
      this.nullValue = var1;
      if (this.hasGUI) {
         ValueComputerPartGUI var3 = (ValueComputerPartGUI)this.getGUI();
         var3.setNullValue(var1, var2);
      }

   }
}
