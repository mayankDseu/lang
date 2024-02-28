package Hack.Gates;

import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.awt.Component;
import java.util.Vector;

public abstract class BuiltInGateWithGUI extends BuiltInGate implements ErrorEventListener {
   private Vector errorListeners = new Vector();
   protected Gate parent;

   protected void setParent(Gate var1) {
      this.parent = var1;
   }

   protected void evalParent() {
      this.parent.setDirty();
      this.parent.eval();
   }

   public void addErrorListener(GateErrorEventListener var1) {
      this.errorListeners.addElement(var1);
   }

   public void removeErrorListener(GateErrorEventListener var1) {
      this.errorListeners.removeElement(var1);
   }

   public void notifyErrorListeners(String var1) {
      GateErrorEvent var2 = new GateErrorEvent(this, var1);

      for(int var3 = 0; var3 < this.errorListeners.size(); ++var3) {
         ((GateErrorEventListener)this.errorListeners.elementAt(var3)).gateErrorOccured(var2);
      }

   }

   public void clearErrorListeners() {
      GateErrorEvent var1 = new GateErrorEvent(this, (String)null);

      for(int var2 = 0; var2 < this.errorListeners.size(); ++var2) {
         ((GateErrorEventListener)this.errorListeners.elementAt(var2)).gateErrorOccured(var1);
      }

   }

   public void errorOccured(ErrorEvent var1) {
      this.notifyErrorListeners(var1.getErrorMessage());
   }

   public abstract Component getGUIComponent();

   public abstract short getValueAt(int var1) throws GateException;

   public abstract void setValueAt(int var1, short var2) throws GateException;

   public void doCommand(String[] var1) throws GateException {
      throw new GateException("This chip supports no commands");
   }
}
