package HackGUI;

import java.util.EventObject;
import java.util.Vector;

public class BreakpointsChangedEvent extends EventObject {
   private Vector breakpoints = new Vector();

   public BreakpointsChangedEvent(Object var1, Vector var2) {
      super(var1);
      this.breakpoints = (Vector)var2.clone();
   }

   public Vector getBreakpoints() {
      return this.breakpoints;
   }
}
