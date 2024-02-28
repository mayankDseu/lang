package Hack.Events;

import java.util.EventObject;

public class ProgramEvent extends EventObject {
   public static final byte LOAD = 1;
   public static final byte SAVE = 2;
   public static final byte CLEAR = 3;
   private String programFileName;
   private byte eventType;

   public ProgramEvent(Object var1, byte var2, String var3) {
      super(var1);
      this.programFileName = var3;
      this.eventType = var2;
   }

   public String getProgramFileName() {
      return this.programFileName;
   }

   public byte getType() {
      return this.eventType;
   }
}
