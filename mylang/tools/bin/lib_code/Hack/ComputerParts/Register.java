package Hack.ComputerParts;

public class Register extends InteractiveValueComputerPart implements ComputerPartEventListener {
   protected short value;
   protected RegisterGUI gui;

   public Register(RegisterGUI var1, short var2, short var3) {
      super(var1 != null, var2, var3);
      this.init(var1);
   }

   public Register(RegisterGUI var1) {
      super(var1 != null);
      this.init(var1);
   }

   private void init(RegisterGUI var1) {
      this.gui = var1;
      if (this.hasGUI) {
         var1.addListener(this);
         var1.addErrorListener(this);
      }

   }

   public short get() {
      return this.getValueAt(0);
   }

   public void store(short var1) {
      this.setValueAt(0, var1, false);
   }

   public short getValueAt(int var1) {
      return this.value;
   }

   public void doSetValueAt(int var1, short var2) {
      this.value = var2;
   }

   public void reset() {
      super.reset();
      this.value = this.nullValue;
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void refreshGUI() {
      super.refreshGUI();
      if (this.displayChanges) {
         this.quietUpdateGUI(0, this.value);
      }

   }
}
