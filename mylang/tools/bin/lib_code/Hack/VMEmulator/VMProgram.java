package Hack.VMEmulator;

import Hack.ComputerParts.ComputerPartGUI;
import Hack.ComputerParts.InteractiveComputerPart;
import Hack.Controller.ProgramException;
import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import Hack.Utilities.HackFileFilter;
import Hack.VirtualMachine.HVMInstructionSet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

public class VMProgram extends InteractiveComputerPart implements ProgramEventListener {
   public static final short BUILTIN_FUNCTION_ADDRESS = -1;
   private static final int BUILTIN_ACCESS_UNDECIDED = 0;
   private static final int BUILTIN_ACCESS_AUTHORIZED = 1;
   private static final int BUILTIN_ACCESS_DENIED = 2;
   private Vector listeners;
   private VMEmulatorInstruction[] instructions;
   private int instructionsLength;
   private int visibleInstructionsLength;
   private short nextPC;
   private short currentPC;
   private short prevPC;
   private VMProgramGUI gui;
   private short startAddress;
   private Hashtable staticRange;
   private Hashtable functions;
   private short infiniteLoopForBuiltInsAddress;
   private int currentStaticIndex;
   private int largestStaticIndex;
   private int builtInAccessStatus;
   private boolean isSlashStar;

   public VMProgram(VMProgramGUI var1) {
      super(var1 != null);
      this.gui = var1;
      this.listeners = new Vector();
      this.staticRange = new Hashtable();
      this.functions = new Hashtable();
      if (this.hasGUI) {
         var1.addProgramListener(this);
         var1.addErrorListener(this);
      }

      this.reset();
   }

   public void loadProgram(String var1) throws ProgramException {
      File var2 = new File(var1);
      if (!var2.exists()) {
         throw new ProgramException("cannot find " + var1);
      } else {
         File[] var3;
         if (var2.isDirectory()) {
            var3 = var2.listFiles(new HackFileFilter(".vm"));
            if (var3 == null || var3.length == 0) {
               throw new ProgramException("No vm files found in " + var1);
            }
         } else {
            var3 = new File[]{var2};
         }

         if (this.displayChanges) {
            this.gui.showMessage("Loading...");
         }

         this.staticRange.clear();
         this.functions.clear();
         this.builtInAccessStatus = 0;
         Hashtable var4 = new Hashtable();
         this.nextPC = 0;

         String var7;
         for(int var5 = 0; var5 < var3.length; ++var5) {
            String var6 = var3[var5].getName();
            var7 = var6.substring(0, var6.indexOf("."));
            this.staticRange.put(var7, new Boolean(true));

            try {
               this.updateSymbolTable(var3[var5], var4, this.functions);
            } catch (ProgramException var12) {
               if (this.displayChanges) {
                  this.gui.hideMessage();
               }

               throw new ProgramException(var6 + ": " + var12.getMessage());
            }
         }

         boolean var13 = false;
         if ((var2.isDirectory() || var4.get("Main.main") != null) && var4.get("Sys.init") == null) {
            var13 = true;
            this.getAddress("Sys.init");
            ++this.nextPC;
         }

         this.instructions = new VMEmulatorInstruction[this.nextPC + 4];
         this.nextPC = 0;
         this.currentStaticIndex = 16;

         for(int var14 = 0; var14 < var3.length; ++var14) {
            var7 = var3[var14].getName();
            String var8 = var7.substring(0, var7.indexOf("."));
            this.largestStaticIndex = -1;
            int[] var9 = new int[]{this.currentStaticIndex, 0};

            try {
               this.buildProgram(var3[var14], var4);
            } catch (ProgramException var11) {
               if (this.displayChanges) {
                  this.gui.hideMessage();
               }

               throw new ProgramException(var7 + ": " + var11.getMessage());
            }

            this.currentStaticIndex += this.largestStaticIndex + 1;
            var9[1] = this.currentStaticIndex - 1;
            this.staticRange.put(var8, var9);
         }

         this.instructionsLength = this.visibleInstructionsLength = this.nextPC;
         if (this.builtInAccessStatus == 1) {
            this.instructionsLength += 4;
            if (var13) {
               ++this.instructionsLength;
            }

            byte var15 = 0;
            this.instructions[this.nextPC] = new VMEmulatorInstruction((byte)13, (short)this.instructionsLength, var15);
            this.instructions[this.nextPC].setStringArg("afterInvisibleCode");
            ++this.nextPC;
            this.instructions[this.nextPC] = new VMEmulatorInstruction((byte)12, (short)-1);
            this.instructions[this.nextPC].setStringArg("infiniteLoopForBuiltIns");
            ++this.nextPC;
            this.infiniteLoopForBuiltInsAddress = this.nextPC;
            short var16 = (short)(var15 + 1);
            this.instructions[this.nextPC] = new VMEmulatorInstruction((byte)13, this.nextPC, var16);
            this.instructions[this.nextPC].setStringArg("infiniteLoopForBuiltIns");
            ++this.nextPC;
            if (var13) {
               VMEmulatorInstruction[] var10000 = this.instructions;
               short var10001 = this.nextPC;
               short var10005 = this.getAddress("Sys.init");
               ++var16;
               var10000[var10001] = new VMEmulatorInstruction((byte)17, var10005, (short)0, var16);
               this.instructions[this.nextPC].setStringArg("Sys.init");
               this.startAddress = this.nextPC++;
            }

            this.instructions[this.nextPC] = new VMEmulatorInstruction((byte)12, (short)-1);
            this.instructions[this.nextPC].setStringArg("afterInvisibleCode");
            ++this.nextPC;
         }

         if (!var13) {
            Short var17 = (Short)var4.get("Sys.init");
            if (var17 == null) {
               this.startAddress = 0;
            } else {
               this.startAddress = var17;
            }
         }

         if (this.displayChanges) {
            this.gui.hideMessage();
         }

         this.nextPC = this.startAddress;
         this.setGUIContents();
         this.notifyProgramListeners((byte)1, var1);
      }
   }

   private void updateSymbolTable(File var1, Hashtable var2, Hashtable var3) throws ProgramException {
      BufferedReader var4 = null;

      try {
         var4 = new BufferedReader(new FileReader(var1.getAbsolutePath()));
      } catch (FileNotFoundException var10) {
         throw new ProgramException("file " + var1.getName() + " does not exist");
      }

      String var6 = null;
      int var8 = 0;
      this.isSlashStar = false;

      try {
         String var5;
         while((var5 = this.unCommentLine(var4.readLine())) != null) {
            ++var8;
            if (!var5.trim().equals("")) {
               StringTokenizer var9;
               if (var5.startsWith("function ")) {
                  var9 = new StringTokenizer(var5);
                  var9.nextToken();
                  var6 = var9.nextToken();
                  if (var2.containsKey(var6)) {
                     throw new ProgramException("subroutine " + var6 + " already exists");
                  }

                  var3.put(var6, new Short(this.nextPC));
                  var2.put(var6, new Short(this.nextPC));
               } else if (var5.startsWith("label ")) {
                  var9 = new StringTokenizer(var5);
                  var9.nextToken();
                  String var7 = var6 + "$" + var9.nextToken();
                  var2.put(var7, new Short((short)(this.nextPC + 1)));
               }

               ++this.nextPC;
            }
         }

         var4.close();
      } catch (IOException var11) {
         throw new ProgramException("Error while reading from file");
      } catch (NoSuchElementException var12) {
         throw new ProgramException("In line " + var8 + ": unexpected end of command");
      }

      if (this.isSlashStar) {
         throw new ProgramException("Unterminated /* comment at end of file");
      }
   }

   private void buildProgram(File var1, Hashtable var2) throws ProgramException {
      BufferedReader var3 = null;

      try {
         var3 = new BufferedReader(new FileReader(var1.getAbsolutePath()));
      } catch (FileNotFoundException var23) {
         throw new ProgramException("file does not exist");
      }

      int var4 = 0;
      String var8 = null;
      short var9 = 0;
      short var13 = this.nextPC;
      HVMInstructionSet var14 = HVMInstructionSet.getInstance();
      this.isSlashStar = false;

      try {
         String var5;
         for(; (var5 = this.unCommentLine(var3.readLine())) != null; this.nextPC = var13) {
            ++var4;
            if (!var5.trim().equals("")) {
               StringTokenizer var15 = new StringTokenizer(var5);
               String var7 = var15.nextToken();
               byte var10 = var14.instructionStringToCode(var7);
               if (var10 == -99) {
                  throw new ProgramException("in line " + var4 + ": unknown instruction - " + var7);
               }

               String var6;
               short var11;
               short var12;
               String var16;
               Short var19;
               switch(var10) {
               case 10:
                  var16 = var15.nextToken();

                  try {
                     var11 = (short)this.translateSegment(var16, var14, var1.getName());
                  } catch (ProgramException var22) {
                     throw new ProgramException("in line " + var4 + var22.getMessage());
                  }

                  var12 = Short.parseShort(var15.nextToken());
                  if (var12 < 0) {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  if (var11 == 100 && var12 > this.largestStaticIndex) {
                     this.largestStaticIndex = var12;
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var12, var9);
                  break;
               case 11:
                  int var17 = var15.countTokens();
                  var16 = var15.nextToken();

                  try {
                     var11 = (short)this.translateSegment(var16, var14, var1.getName());
                  } catch (ProgramException var21) {
                     throw new ProgramException("in line " + var4 + var21.getMessage());
                  }

                  var12 = Short.parseShort(var15.nextToken());
                  if (var12 < 0) {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  if (var11 == 100 && var12 > this.largestStaticIndex) {
                     this.largestStaticIndex = var12;
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var12, var9);
                  break;
               case 12:
                  var6 = var8 + "$" + var15.nextToken();
                  this.instructions[var13] = new VMEmulatorInstruction(var10, (short)-1);
                  this.instructions[var13].setStringArg(var6);
                  --var9;
                  break;
               case 13:
                  var6 = var8 + "$" + var15.nextToken();
                  var19 = (Short)var2.get(var6);
                  if (var19 == null) {
                     throw new ProgramException("in line " + var4 + ": Unknown label - " + var6);
                  }

                  var11 = var19;
                  if (var11 < 0 || var11 > '耀') {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var9);
                  this.instructions[var13].setStringArg(var6);
                  break;
               case 14:
                  var6 = var8 + "$" + var15.nextToken();
                  var19 = (Short)var2.get(var6);
                  if (var19 == null) {
                     throw new ProgramException("in line " + var4 + ": Unknown label - " + var6);
                  }

                  var11 = var19;
                  if (var11 < 0 || var11 > '耀') {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var9);
                  this.instructions[var13].setStringArg(var6);
                  break;
               case 15:
                  var8 = var15.nextToken();
                  var9 = 0;
                  var11 = Short.parseShort(var15.nextToken());
                  if (var11 < 0) {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var9);
                  this.instructions[var13].setStringArg(var8);
                  break;
               case 16:
               default:
                  if (var15.countTokens() == 0) {
                     this.instructions[var13] = new VMEmulatorInstruction(var10, var9);
                  } else {
                     var11 = Short.parseShort(var15.nextToken());
                     if (var11 < 0) {
                        throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                     }

                     this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var9);
                  }
                  break;
               case 17:
                  String var18 = var15.nextToken();

                  try {
                     var11 = this.getAddress(var18);
                  } catch (ProgramException var20) {
                     throw new ProgramException("in line " + var4 + ": " + var20.getMessage());
                  }

                  var12 = Short.parseShort(var15.nextToken());
                  if (var12 < 0 || (var11 < 0 || var11 > '耀') && var11 != -1) {
                     throw new ProgramException("in line " + var4 + ": Illegal argument - " + var5);
                  }

                  this.instructions[var13] = new VMEmulatorInstruction(var10, var11, var12, var9);
                  this.instructions[var13].setStringArg(var18);
               }

               if (var15.hasMoreTokens()) {
                  throw new ProgramException("in line " + var4 + ": Too many arguments - " + var5);
               }

               ++var13;
               ++var9;
            }
         }

         var3.close();
      } catch (IOException var24) {
         throw new ProgramException("Error while reading from file");
      } catch (NumberFormatException var25) {
         throw new ProgramException("Illegal 16-bit value");
      } catch (NoSuchElementException var26) {
         throw new ProgramException("In line " + var4 + ": unexpected end of command");
      }

      if (this.isSlashStar) {
         throw new ProgramException("Unterminated /* comment at end of file");
      }
   }

   private String unCommentLine(String var1) {
      String var2 = var1;
      if (var1 != null) {
         int var3;
         if (this.isSlashStar) {
            var3 = var1.indexOf("*/");
            if (var3 >= 0) {
               this.isSlashStar = false;
               var2 = this.unCommentLine(var1.substring(var3 + 2));
            } else {
               var2 = "";
            }
         } else {
            var3 = var1.indexOf("//");
            int var4 = var1.indexOf("/*");
            if (var3 >= 0 && (var4 < 0 || var4 > var3)) {
               var2 = var1.substring(0, var3);
            } else if (var4 >= 0) {
               this.isSlashStar = true;
               var2 = var1.substring(0, var4) + this.unCommentLine(var1.substring(var4 + 2));
            }
         }
      }

      return var2;
   }

   public int[] getStaticRange(String var1) {
      return (int[])this.staticRange.get(var1);
   }

   public int getSize() {
      return this.instructionsLength;
   }

   public short getAddress(String var1) throws ProgramException {
      Short var2 = (Short)this.functions.get(var1);
      if (var2 != null) {
         return var2;
      } else {
         String var3 = var1.substring(0, var1.indexOf("."));
         if (this.staticRange.get(var3) == null) {
            if (this.builtInAccessStatus == 0) {
               if (this.hasGUI && this.gui.confirmBuiltInAccess()) {
                  this.builtInAccessStatus = 1;
               } else {
                  this.builtInAccessStatus = 2;
               }
            }

            if (this.builtInAccessStatus == 1) {
               return -1;
            }
         }

         throw new ProgramException(var3 + ".vm not found " + "or function " + var1 + " not found in " + var3 + ".vm");
      }
   }

   public short getPC() {
      return this.nextPC;
   }

   public short getCurrentPC() {
      return this.currentPC;
   }

   public short getPreviousPC() {
      return this.prevPC;
   }

   public void setPC(short var1) {
      this.prevPC = this.currentPC;
      this.currentPC = this.nextPC;
      this.nextPC = var1;
      this.setGUIPC();
   }

   public void setPCToInfiniteLoopForBuiltIns(String var1) {
      if (this.hasGUI) {
         this.gui.notify(var1);
      }

      this.setPC(this.infiniteLoopForBuiltInsAddress);
   }

   public VMEmulatorInstruction getNextInstruction() {
      VMEmulatorInstruction var1 = null;
      if (this.nextPC < this.instructionsLength) {
         var1 = this.instructions[this.nextPC];
         this.prevPC = this.currentPC;
         this.currentPC = this.nextPC;

         do {
            ++this.nextPC;
         } while(this.nextPC < this.instructionsLength && this.instructions[this.nextPC].getOpCode() == 12);

         this.setGUIPC();
      }

      return var1;
   }

   public void restartProgram() {
      this.currentPC = -999;
      this.prevPC = -999;
      this.nextPC = this.startAddress;
      this.setGUIPC();
   }

   public void reset() {
      this.instructions = new VMEmulatorInstruction[0];
      this.visibleInstructionsLength = this.instructionsLength = 0;
      this.currentPC = -999;
      this.prevPC = -999;
      this.nextPC = -1;
      this.setGUIContents();
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public void programChanged(ProgramEvent var1) {
      switch(var1.getType()) {
      case 1:
         VMProgram.LoadProgramTask var2 = new VMProgram.LoadProgramTask(var1.getProgramFileName());
         Thread var3 = new Thread(var2);
         var3.start();
         break;
      case 3:
         this.reset();
         this.notifyProgramListeners((byte)3, (String)null);
      }

   }

   private byte translateSegment(String var1, HVMInstructionSet var2, String var3) throws ProgramException {
      byte var4 = var2.segmentVMStringToCode(var1);
      if (var4 == -1) {
         throw new ProgramException(": Illegal memory segment - " + var1);
      } else {
         return var4;
      }
   }

   private void setGUIContents() {
      if (this.displayChanges) {
         this.gui.setContents(this.instructions, this.visibleInstructionsLength);
         this.gui.setCurrentInstruction(this.nextPC);
      }

   }

   private void setGUIPC() {
      if (this.displayChanges) {
         this.gui.setCurrentInstruction(this.nextPC);
      }

   }

   public void refreshGUI() {
      if (this.displayChanges) {
         this.gui.setContents(this.instructions, this.visibleInstructionsLength);
         this.gui.setCurrentInstruction(this.nextPC);
      }

   }

   public void addProgramListener(ProgramEventListener var1) {
      this.listeners.add(var1);
   }

   public void removeProgramListener(ProgramEventListener var1) {
      this.listeners.remove(var1);
   }

   protected void notifyProgramListeners(byte var1, String var2) {
      ProgramEvent var3 = new ProgramEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ProgramEventListener)this.listeners.elementAt(var4)).programChanged(var3);
      }

   }

   class LoadProgramTask implements Runnable {
      private String fileName;

      public LoadProgramTask(String var2) {
         this.fileName = var2;
      }

      public void run() {
         VMProgram.this.clearErrorListeners();

         try {
            VMProgram.this.loadProgram(this.fileName);
         } catch (ProgramException var2) {
            VMProgram.this.notifyErrorListeners(var2.getMessage());
         }

      }
   }
}
