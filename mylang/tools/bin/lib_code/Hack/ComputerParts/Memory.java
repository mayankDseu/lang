package Hack.ComputerParts;

import Hack.Events.ClearEvent;
import Hack.Events.ClearEventListener;

public class Memory extends InteractiveValueComputerPart implements ClearEventListener {
   protected int size;
   protected short[] mem;
   protected MemoryGUI gui;

   public Memory(int var1, MemoryGUI var2) {
      super(var2 != null);
      this.init(var1, var2);
   }

   public Memory(int var1, MemoryGUI var2, short var3, short var4) {
      super(var2 != null, var3, var4);
      this.init(var1, var2);
   }

   private void init(int var1, MemoryGUI var2) {
      this.size = var1;
      this.gui = var2;
      this.mem = new short[var1];
      if (this.hasGUI) {
         var2.setContents(this.mem);
         var2.addListener(this);
         var2.addClearListener(this);
         var2.addErrorListener(this);
      }

   }

   public short getValueAt(int var1) {
      return this.mem[var1];
   }

   public void doSetValueAt(int var1, short var2) {
      this.mem[var1] = var2;
   }

   public short[] getContents() {
      return this.mem;
   }

   public void setContents(short[] var1, int var2) {
      System.arraycopy(var1, 0, this.mem, var2, var1.length);
      this.refreshGUI();
   }

   public int getSize() {
      return this.size;
   }

   public void reset() {
      super.reset();

      for(int var1 = 0; var1 < this.size; ++var1) {
         this.mem[var1] = this.nullValue;
      }

   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void refreshGUI() {
      super.refreshGUI();
      if (this.displayChanges) {
         this.gui.setContents(this.mem);
      }

   }

   public void scrollTo(int var1) {
      if (this.displayChanges) {
         this.gui.scrollTo(var1);
      }

   }

   public void clearRequested(ClearEvent var1) {
      this.reset();
   }

   public void select(int var1, int var2) {
      if (this.displayChanges) {
         this.gui.select(var1, var2);
      }

   }

   public void hideSelect() {
      if (this.displayChanges) {
         this.gui.hideSelect();
      }

   }
}
