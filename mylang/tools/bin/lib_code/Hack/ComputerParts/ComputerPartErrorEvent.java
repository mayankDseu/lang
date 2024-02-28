package Hack.ComputerParts;

import Hack.Events.ErrorEvent;

public class ComputerPartErrorEvent extends ErrorEvent {
   public ComputerPartErrorEvent(ComputerPart var1, String var2) {
      super(var1, var2);
   }
}
