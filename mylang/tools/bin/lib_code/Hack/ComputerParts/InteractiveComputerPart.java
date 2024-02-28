package Hack.ComputerParts;

import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.util.Vector;

public abstract class InteractiveComputerPart extends ComputerPart implements ErrorEventListener {
   private Vector errorListeners = new Vector();

   public InteractiveComputerPart(boolean var1) {
      super(var1);
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
}
