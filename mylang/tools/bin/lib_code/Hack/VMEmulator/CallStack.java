package Hack.VMEmulator;

import Hack.ComputerParts.ComputerPart;
import Hack.ComputerParts.ComputerPartGUI;
import java.util.Vector;

public class CallStack extends ComputerPart {
   private Vector names = new Vector();
   private CallStackGUI gui;

   public CallStack(CallStackGUI var1) {
      super(var1 != null);
      this.gui = var1;
   }

   public String getTopFunction() {
      return this.names.size() > 0 ? (String)this.names.elementAt(this.names.size() - 1) : "";
   }

   public void pushFunction(String var1) {
      this.names.addElement(var1);
      if (this.displayChanges) {
         this.gui.setContents(this.names);
      }

   }

   public void popFunction() {
      if (this.names.size() > 0) {
         this.names.removeElementAt(this.names.size() - 1);
         if (this.displayChanges) {
            this.gui.setContents(this.names);
         }
      }

   }

   public void reset() {
      super.reset();
      this.names.removeAllElements();
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void refreshGUI() {
      if (this.displayChanges) {
         this.gui.setContents(this.names);
      }

   }
}
