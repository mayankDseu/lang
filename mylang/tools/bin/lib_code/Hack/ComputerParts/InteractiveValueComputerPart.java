package Hack.ComputerParts;

import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.util.Vector;

public abstract class InteractiveValueComputerPart extends ValueComputerPart implements ComputerPartEventListener, ErrorEventListener {
   private Vector errorListeners = new Vector();
   private short minValue;
   private short maxValue;
   private int startEnabledRange;
   private int endEnabledRange;
   private boolean grayDisabledRange;

   public InteractiveValueComputerPart(boolean var1) {
      super(var1);
      this.minValue = -32768;
      this.maxValue = 32767;
      this.startEnabledRange = -1;
      this.endEnabledRange = -1;
   }

   public InteractiveValueComputerPart(boolean var1, short var2, short var3) {
      super(var1);
      this.minValue = var2;
      this.maxValue = var3;
   }

   public void addErrorListener(ComputerPartErrorEventListener var1) {
      this.errorListeners.addElement(var1);
   }

   public void removeErrorListener(ComputerPartErrorEventListener var1) {
      this.errorListeners.removeElement(var1);
   }

   public void notifyErrorListeners(String var1) {
      ComputerPartErrorEvent var2 = new ComputerPartErrorEvent(this, var1);

      for(int var3 = 0; var3 < this.errorListeners.size(); ++var3) {
         ((ComputerPartErrorEventListener)this.errorListeners.elementAt(var3)).computerPartErrorOccured(var2);
      }

   }

   public void clearErrorListeners() {
      ComputerPartErrorEvent var1 = new ComputerPartErrorEvent(this, (String)null);

      for(int var2 = 0; var2 < this.errorListeners.size(); ++var2) {
         ((ComputerPartErrorEventListener)this.errorListeners.elementAt(var2)).computerPartErrorOccured(var1);
      }

   }

   public void errorOccured(ErrorEvent var1) {
      this.notifyErrorListeners(var1.getErrorMessage());
   }

   public void valueChanged(ComputerPartEvent var1) {
      short var2 = var1.getValue();
      int var3 = var1.getIndex();
      this.clearErrorListeners();
      if ((var2 < this.minValue || var2 > this.maxValue) && var2 != this.nullValue) {
         this.notifyErrorListeners("Value must be in the range " + this.minValue + ".." + this.maxValue);
         this.quietUpdateGUI(var3, this.getValueAt(var3));
      } else {
         this.setValueAt(var3, var2, true);
      }

   }

   public void guiGainedFocus() {
   }

   public void enableUserInput() {
      if (this.hasGUI) {
         ((InteractiveValueComputerPartGUI)this.getGUI()).enableUserInput();
      }

   }

   public void disableUserInput() {
      if (this.hasGUI) {
         ((InteractiveValueComputerPartGUI)this.getGUI()).disableUserInput();
      }

   }

   public int[] getEnabledRange() {
      return new int[]{this.startEnabledRange, this.endEnabledRange};
   }

   public void setEnabledRange(int var1, int var2, boolean var3) {
      this.startEnabledRange = var1;
      this.endEnabledRange = var2;
      this.grayDisabledRange = var3;
      if (this.displayChanges) {
         ((InteractiveValueComputerPartGUI)this.getGUI()).setEnabledRange(var1, var2, var3);
      }

   }

   public void refreshGUI() {
      if (this.displayChanges && this.startEnabledRange != -1 && this.endEnabledRange != -1) {
         ((InteractiveValueComputerPartGUI)this.getGUI()).setEnabledRange(this.startEnabledRange, this.endEnabledRange, this.grayDisabledRange);
      }

   }
}
