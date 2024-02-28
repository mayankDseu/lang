package Hack.ComputerParts;

public class Bus extends ComputerPart {
   private BusGUI gui;

   public Bus(BusGUI var1) {
      super(var1 != null);
      this.gui = var1;
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void setAnimationSpeed(int var1) {
      if (this.hasGUI) {
         this.gui.setSpeed(var1);
      }

   }

   public synchronized void send(ValueComputerPart var1, int var2, ValueComputerPart var3, int var4) {
      if (this.animate && var1.animate && this.hasGUI) {
         try {
            this.wait(100L);
         } catch (InterruptedException var6) {
         }

         this.gui.move(((ValueComputerPartGUI)var1.getGUI()).getCoordinates(var2), ((ValueComputerPartGUI)var3.getGUI()).getCoordinates(var4), ((ValueComputerPartGUI)var1.getGUI()).getValueAsString(var2));
      }

      var3.setValueAt(var4, var1.getValueAt(var2), false);
   }

   public void refreshGUI() {
   }
}
