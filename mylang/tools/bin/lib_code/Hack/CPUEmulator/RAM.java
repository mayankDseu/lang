package Hack.CPUEmulator;

import Hack.ComputerParts.LabeledPointedMemoryGUI;
import Hack.ComputerParts.MemorySegment;
import Hack.ComputerParts.PointedMemory;
import Hack.ComputerParts.PointedMemoryGUI;
import Hack.ComputerParts.PointedMemorySegment;

public class RAM extends PointedMemory {
   private static final int LABEL_FLASH_TIME = 500;
   private static final short[] emptyScreen = new short[8192];
   private ScreenGUI screen;
   private MemorySegment[][] segments;

   public RAM(PointedMemoryGUI var1, MemorySegment[][] var2, ScreenGUI var3) {
      super(24577, var1);
      this.segments = var2;
      this.screen = var3;
   }

   public void setValueAt(int var1, short var2, boolean var3) {
      super.setValueAt(var1, var2, var3);
      if (this.screen != null && var1 >= 16384 && var1 < 24576) {
         this.screen.setValueAt((short)(var1 - 16384), var2);
      }

      if (this.segments != null && this.segments[var1] != null) {
         for(int var4 = 0; var4 < this.segments[var1].length; ++var4) {
            if (this.segments[var1][var4] instanceof PointedMemorySegment) {
               ((PointedMemorySegment)this.segments[var1][var4]).setPointerAddress(var2);
            } else {
               this.segments[var1][var4].setStartAddress(var2);
            }
         }
      }

   }

   public synchronized void setLabel(int var1, String var2, boolean var3) {
      if (this.hasGUI && this.gui instanceof LabeledPointedMemoryGUI) {
         ((LabeledPointedMemoryGUI)this.gui).setLabel(var1, var2);
         if (!var3) {
            ((LabeledPointedMemoryGUI)this.gui).labelFlash(var1);

            try {
               this.wait(500L);
            } catch (InterruptedException var5) {
            }

            ((LabeledPointedMemoryGUI)this.gui).hideLabelFlash();
         }
      }

   }

   public void clearLabels() {
      if (this.hasGUI && this.gui instanceof LabeledPointedMemoryGUI) {
         ((LabeledPointedMemoryGUI)this.gui).clearLabels();
      }

   }

   public void reset() {
      super.reset();
      if (this.screen != null) {
         this.screen.reset();
      }

   }

   public void clearScreen() {
      this.setContents(emptyScreen, 16384);
      if (this.screen != null) {
         this.screen.reset();
      }

   }

   public void refreshGUI() {
      super.refreshGUI();
      if (this.segments != null) {
         for(int var1 = 0; var1 < this.size; ++var1) {
            if (this.segments[var1] != null) {
               for(int var2 = 0; var2 < this.segments[var1].length; ++var2) {
                  if (this.segments[var1][var2] instanceof PointedMemorySegment) {
                     ((PointedMemorySegment)this.segments[var1][var2]).setPointerAddress(this.mem[var1]);
                  } else {
                     this.segments[var1][var2].setStartAddress(this.mem[var1]);
                  }
               }
            }
         }
      }

   }
}
