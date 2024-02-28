package Hack.VMEmulator;

import Hack.Controller.ProgramException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BuiltInFunctionsRunner implements Runnable {
   private static final int CALL_REQUEST = 0;
   private static final int RETURN_REQUEST = 1;
   private static final int END_PROGRAM_REQUEST = 2;
   private static final int THROW_PROGRAM_EXCEPTION_REQUEST = 3;
   private static final int INFINITE_LOOP_REQUEST = 4;
   private BuiltInFunctionsRunner.BuiltInToProgramRequest builtInToProgram;
   private BuiltInFunctionsRunner.ProgramToBuiltInRequest programToBuiltIn;
   private Thread thread;
   private CPU cpu;
   private File builtInDir;

   private synchronized void continueOtherThread() {
      this.notify();

      while(true) {
         try {
            this.wait();
            return;
         } catch (InterruptedException var2) {
         }
      }
   }

   public BuiltInFunctionsRunner(CPU var1, File var2) {
      this.cpu = var1;
      this.builtInDir = var2;
      this.builtInToProgram = new BuiltInFunctionsRunner.BuiltInToProgramRequest();
      this.programToBuiltIn = new BuiltInFunctionsRunner.ProgramToBuiltInRequest();
      this.thread = new Thread(this);
      synchronized(this) {
         this.thread.start();
         this.continueOtherThread();
      }
   }

   public void killAllRunningBuiltInFunctions() {
      this.programToBuiltIn.request = 2;
      this.continueOtherThread();
   }

   public void returnToBuiltInFunction(short var1) throws ProgramException {
      this.programToBuiltIn.request = 1;
      this.programToBuiltIn.returnValue = var1;
      this.sendBuiltInRequestAndWaitForAnswer();
   }

   public void callBuiltInFunction(String var1, short[] var2) throws ProgramException {
      int var3 = var1.indexOf(".");
      if (var3 == -1) {
         throw new ProgramException("Illegal function name: " + var1);
      } else {
         String var4 = var1.substring(0, var3);
         String var5 = var1.substring(var3 + 1, var1.length());
         if (var5.equals("new")) {
            var5 = "NEW";
         }

         Class var6;
         try {
            var6 = Class.forName(this.builtInDir + "." + var4);
         } catch (ClassNotFoundException var13) {
            throw new ProgramException("Can't find " + var4 + ".vm or a built-in implementation for class " + var4);
         }

         Class var7 = var6;

         boolean var8;
         do {
            var7 = var7.getSuperclass();
            var8 = var7.getName().equals("Hack.VMEmulator.BuiltInVMClass");
         } while(!var8 && !var7.getName().equals("java.lang.Object"));

         if (!var8) {
            throw new ProgramException("Built-in implementation for " + var4 + " is not a subclass of BuiltInVMClass");
         } else {
            Class[] var9 = new Class[var2.length];
            Object[] var10 = new Object[var2.length];

            for(int var11 = 0; var11 < var2.length; ++var11) {
               var10[var11] = new Short(var2[var11]);
               var9[var11] = Short.TYPE;
            }

            Method var15;
            try {
               var15 = var6.getDeclaredMethod(var5, var9);
            } catch (NoSuchMethodException var14) {
               throw new ProgramException("Can't find " + var4 + ".vm or a built-in implementation for function " + var5 + " in class " + var4 + " taking " + var2.length + " argument" + (var2.length == 1 ? "" : "s") + ".");
            }

            Class var12 = var15.getReturnType();
            if (var12 != Short.TYPE && var12 != Void.TYPE && var12 != Character.TYPE && var12 != Boolean.TYPE) {
               throw new ProgramException("Can't find " + var4 + ".vm and the built-in implementation for " + var1 + " taking " + var2.length + " arguments doesn't return short/char/void/boolean.");
            } else {
               this.programToBuiltIn.request = 0;
               this.programToBuiltIn.params = var10;
               this.programToBuiltIn.functionObject = var15;
               this.sendBuiltInRequestAndWaitForAnswer();
            }
         }
      }
   }

   private void sendBuiltInRequestAndWaitForAnswer() throws ProgramException {
      this.continueOtherThread();
      switch(this.builtInToProgram.request) {
      case 0:
         this.cpu.callFunctionFromBuiltIn(this.builtInToProgram.details, this.builtInToProgram.params);
         break;
      case 1:
         this.cpu.returnFromBuiltInFunction(this.builtInToProgram.returnValue);
      case 2:
      default:
         break;
      case 3:
         throw new ProgramException(this.builtInToProgram.details);
      case 4:
         this.cpu.infiniteLoopFromBuiltIn(this.builtInToProgram.details);
      }

   }

   public void run() {
      synchronized(this) {
         BuiltInVMClass.associateForThread(this);

         while(true) {
            try {
               this.builtInFunctionRequestsCall((String)null, (short[])null);
               return;
            } catch (TerminateVMProgramThrowable var4) {
            }
         }
      }
   }

   public short builtInFunctionRequestsCall(String var1, short[] var2) throws TerminateVMProgramThrowable {
      this.builtInToProgram.request = 0;
      this.builtInToProgram.details = var1;
      this.builtInToProgram.params = var2;
      this.continueOtherThread();

      for(; this.programToBuiltIn.request == 0; this.continueOtherThread()) {
         try {
            Class var8 = this.programToBuiltIn.functionObject.getReturnType();
            var1 = this.programToBuiltIn.functionObject.getName();
            Object var4 = this.programToBuiltIn.functionObject.invoke((Object)null, this.programToBuiltIn.params);
            this.builtInToProgram.request = 1;
            if (var8 == Short.TYPE) {
               this.builtInToProgram.returnValue = (Short)var4;
            } else if (var8 == Character.TYPE) {
               this.builtInToProgram.returnValue = (short)(Character)var4;
            } else if (var8 == Boolean.TYPE) {
               if ((Boolean)var4) {
                  this.builtInToProgram.returnValue = -1;
               } else {
                  this.builtInToProgram.returnValue = 0;
               }
            } else {
               this.builtInToProgram.returnValue = 0;
            }
         } catch (IllegalAccessException var6) {
            this.builtInToProgram.request = 3;
            this.builtInToProgram.details = "Error trying to run the built-in implementation of " + var1;
         } catch (InvocationTargetException var7) {
            InvocationTargetException var3 = var7;

            try {
               throw (TerminateVMProgramThrowable)var3.getTargetException();
            } catch (ClassCastException var5) {
               this.builtInToProgram.request = 3;
               this.builtInToProgram.details = "The built-in implementation of " + var1 + " caused an exception: " + var7.getTargetException().toString();
            }
         }
      }

      if (this.programToBuiltIn.request == 1) {
         return this.programToBuiltIn.returnValue;
      } else {
         throw new TerminateVMProgramThrowable();
      }
   }

   private void checkMemoryAddress(short var1) throws TerminateVMProgramThrowable {
      if ((var1 < 2048 || var1 > 16383) && (var1 < 16384 || var1 > 24576) && var1 != 0) {
         this.builtInToProgram.request = 3;
         this.builtInToProgram.details = "A built-in function tried to access memory outside the Heap or Screen range";
         this.continueOtherThread();
         throw new TerminateVMProgramThrowable();
      }
   }

   public void builtInFunctionRequestsInfiniteLoop(String var1) throws TerminateVMProgramThrowable {
      this.builtInToProgram.request = 4;
      this.builtInToProgram.details = var1;
      this.continueOtherThread();
      throw new TerminateVMProgramThrowable();
   }

   public void builtInFunctionRequestsMemoryWrite(short var1, short var2) throws TerminateVMProgramThrowable {
      this.checkMemoryAddress(var1);
      this.cpu.getRAM().setValueAt(var1, var2, false);
   }

   public short builtInFunctionRequestsMemoryRead(short var1) throws TerminateVMProgramThrowable {
      this.checkMemoryAddress(var1);
      return this.cpu.getRAM().getValueAt(var1);
   }

   private class ProgramToBuiltInRequest {
      int request;
      Method functionObject;
      Object[] params;
      short returnValue;

      private ProgramToBuiltInRequest() {
      }

      // $FF: synthetic method
      ProgramToBuiltInRequest(Object var2) {
         this();
      }
   }

   private class BuiltInToProgramRequest {
      int request;
      String details;
      short[] params;
      short returnValue;

      private BuiltInToProgramRequest() {
      }

      // $FF: synthetic method
      BuiltInToProgramRequest(Object var2) {
         this();
      }
   }
}
