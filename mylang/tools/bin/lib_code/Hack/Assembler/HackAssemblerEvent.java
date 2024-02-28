package Hack.Assembler;

import Hack.Translators.HackTranslatorEvent;

public class HackAssemblerEvent extends HackTranslatorEvent {
   public static final byte COMPARISON_LOAD = 9;

   public HackAssemblerEvent(Object var1, byte var2, Object var3) {
      super(var1, var2, var3);
   }
}
