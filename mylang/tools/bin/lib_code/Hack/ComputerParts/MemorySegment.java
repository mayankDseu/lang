package Hack.ComputerParts;

public class MemorySegment extends InteractiveValueComputerPart {
   protected MemorySegmentGUI gui;
   protected Memory mainMemory;
   protected int startAddress;

   public MemorySegment(Memory var1, MemorySegmentGUI var2) {
      super(var2 != null);
      this.init(var1, var2);
   }

   public MemorySegment(Memory var1, MemorySegmentGUI var2, short var3, short var4) {
      super(var2 != null, var3, var4);
      this.init(var1, var2);
   }

   private void init(Memory var1, MemorySegmentGUI var2) {
      this.mainMemory = var1;
      this.gui = var2;
      if (this.hasGUI) {
         var2.addListener(this);
         var2.addErrorListener(this);
      }

   }

   public void setStartAddress(int var1) {
      this.startAddress = var1;
      if (this.displayChanges) {
         this.gui.setStartAddress(var1);
      }

   }

   public int getStartAddress() {
      return this.startAddress;
   }

   public void doSetValueAt(int var1, short var2) {
      if (this.mainMemory.getValueAt(this.startAddress + var1) != var2) {
         this.mainMemory.setValueAt(this.startAddress + var1, var2, true);
      }

   }

   public short getValueAt(int var1) {
      return this.mainMemory.getValueAt(this.startAddress + var1);
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void refreshGUI() {
      super.refreshGUI();
      if (this.displayChanges) {
         this.gui.setStartAddress(this.startAddress);
      }

   }

   public void scrollTo(int var1) {
      if (this.displayChanges) {
         this.gui.scrollTo(this.startAddress + var1);
      }

   }

   public void hideSelect() {
      if (this.displayChanges) {
         this.gui.hideSelect();
      }

   }
}
