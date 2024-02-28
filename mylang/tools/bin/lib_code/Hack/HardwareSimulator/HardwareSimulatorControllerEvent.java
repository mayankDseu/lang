package Hack.HardwareSimulator;

import Hack.Controller.ControllerEvent;

public class HardwareSimulatorControllerEvent extends ControllerEvent {
   public static final byte TICKTOCK_CLICKED = 100;
   public static final byte EVAL_CLICKED = 101;
   public static final byte CHIP_CHANGED = 102;
   public static final byte DISABLE_TICKTOCK = 105;
   public static final byte ENABLE_TICKTOCK = 106;
   public static final byte DISABLE_EVAL = 107;
   public static final byte ENABLE_EVAL = 108;

   public HardwareSimulatorControllerEvent(Object var1, byte var2, Object var3) {
      super(var1, var2, var3);
   }
}
