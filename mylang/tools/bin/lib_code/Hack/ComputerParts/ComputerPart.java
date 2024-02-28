package Hack.ComputerParts;

public abstract class ComputerPart {
   protected boolean displayChanges = true;
   protected boolean animate;
   protected boolean hasGUI;

   public ComputerPart(boolean var1) {
      this.hasGUI = var1;
      this.displayChanges = var1;
      this.animate = false;
   }

   public void setDisplayChanges(boolean var1) {
      this.displayChanges = var1 && this.hasGUI;
   }

   public void setAnimate(boolean var1) {
      this.animate = var1 && this.hasGUI;
   }

   public void reset() {
      if (this.hasGUI) {
         this.getGUI().reset();
      }

   }

   public abstract ComputerPartGUI getGUI();

   public abstract void refreshGUI();
}
