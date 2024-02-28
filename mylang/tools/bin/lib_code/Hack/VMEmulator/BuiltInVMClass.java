package Hack.VMEmulator;

import java.util.Hashtable;

public abstract class BuiltInVMClass {
   private static Hashtable builtInFunctionsRunnerByThread = new Hashtable();
   public static final short SCREEN_START_ADDRESS = 16384;
   public static final short SCREEN_END_ADDRESS = 24575;
   public static final int SCREEN_WIDTH = 512;
   public static final int SCREEN_HEIGHT = 256;
   public static final short HEAP_START_ADDRESS = 2048;
   public static final short HEAP_END_ADDRESS = 16383;
   public static final short KEYBOARD_ADDRESS = 24576;
   public static final short NEWLINE_KEY = 128;
   public static final short BACKSPACE_KEY = 129;

   protected static void writeMemory(int var0, int var1) throws TerminateVMProgramThrowable {
      ((BuiltInFunctionsRunner)builtInFunctionsRunnerByThread.get(Thread.currentThread())).builtInFunctionRequestsMemoryWrite((short)var0, (short)var1);
   }

   protected static short readMemory(int var0) throws TerminateVMProgramThrowable {
      return ((BuiltInFunctionsRunner)builtInFunctionsRunnerByThread.get(Thread.currentThread())).builtInFunctionRequestsMemoryRead((short)var0);
   }

   protected static short callFunction(String var0, short[] var1) throws TerminateVMProgramThrowable {
      return ((BuiltInFunctionsRunner)builtInFunctionsRunnerByThread.get(Thread.currentThread())).builtInFunctionRequestsCall(var0, var1);
   }

   protected static short callFunction(String var0) throws TerminateVMProgramThrowable {
      return callFunction(var0, new short[0]);
   }

   protected static short callFunction(String var0, int var1) throws TerminateVMProgramThrowable {
      return callFunction(var0, new short[]{(short)var1});
   }

   protected static short callFunction(String var0, int var1, int var2) throws TerminateVMProgramThrowable {
      return callFunction(var0, new short[]{(short)var1, (short)var2});
   }

   protected static short callFunction(String var0, int var1, int var2, int var3) throws TerminateVMProgramThrowable {
      return callFunction(var0, new short[]{(short)var1, (short)var2, (short)var3});
   }

   protected static short callFunction(String var0, int var1, int var2, int var3, int var4) throws TerminateVMProgramThrowable {
      return callFunction(var0, new short[]{(short)var1, (short)var2, (short)var3, (short)var4});
   }

   protected static void infiniteLoop(String var0) throws TerminateVMProgramThrowable {
      ((BuiltInFunctionsRunner)builtInFunctionsRunnerByThread.get(Thread.currentThread())).builtInFunctionRequestsInfiniteLoop(var0);
   }

   static final void associateForThread(BuiltInFunctionsRunner var0) {
      builtInFunctionsRunnerByThread.put(Thread.currentThread(), var0);
   }
}
