package HackGUI;

import Hack.Controller.Breakpoint;
import java.util.EventObject;

public class BreakpointChangedEvent extends EventObject {
   private Breakpoint breakpoint;

   public BreakpointChangedEvent(Object var1, Breakpoint var2) {
      super(var1);
      this.breakpoint = var2;
   }

   public Breakpoint getBreakpoint() {
      return this.breakpoint;
   }
}
