package Hack.Translators;

import java.util.EventObject;

public class HackTranslatorEvent extends EventObject {
   public static final byte SINGLE_STEP = 1;
   public static final byte FAST_FORWARD = 2;
   public static final byte STOP = 3;
   public static final byte REWIND = 4;
   public static final byte FULL_COMPILATION = 5;
   public static final byte SAVE_DEST = 6;
   public static final byte SOURCE_LOAD = 7;
   private byte action;
   private Object data;

   public HackTranslatorEvent(Object var1, byte var2, Object var3) {
      super(var1);
      this.action = var2;
      this.data = var3;
   }

   public byte getAction() {
      return this.action;
   }

   public Object getData() {
      return this.data;
   }
}
