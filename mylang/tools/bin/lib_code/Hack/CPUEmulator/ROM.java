package Hack.CPUEmulator;

import Hack.Assembler.AssemblerException;
import Hack.Assembler.HackAssemblerTranslator;
import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.PointedMemory;
import Hack.Controller.ProgramException;
import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import java.util.Vector;

public class ROM extends PointedMemory implements ProgramEventListener {
   public static final int DECIMAL_FORMAT = 0;
   public static final int HEXA_FORMAT = 1;
   public static final int BINARY_FORMAT = 2;
   public static final int ASM_FORMAT = 4;
   private Vector listeners;

   public ROM(ROMGUI var1) {
      super(32768, var1);
      this.setNullValue((short)-32768, true);
      this.listeners = new Vector();
      if (this.hasGUI) {
         var1.addProgramListener(this);
         var1.setNumericFormat(4);
      }

   }

   public synchronized void loadProgram(String var1) throws ProgramException {
      Object var2 = null;
      if (this.displayChanges) {
         ((ROMGUI)this.gui).showMessage("Loading...");
      }

      try {
         short[] var5 = HackAssemblerTranslator.loadProgram(var1, 32768, (short)-32768);
         this.mem = var5;
         if (this.displayChanges) {
            this.gui.setContents(this.mem);
            ((ROMGUI)this.gui).setProgram(var1);
            ((ROMGUI)this.gui).hideMessage();
            this.gui.hideHighlight();
         }

         this.notifyProgramListeners((byte)1, var1);
      } catch (AssemblerException var4) {
         if (this.displayChanges) {
            ((ROMGUI)this.gui).hideMessage();
         }

         throw new ProgramException(var4.getMessage());
      }
   }

   public void programChanged(ProgramEvent var1) {
      switch(var1.getType()) {
      case 1:
         ROM.ROMLoadProgramTask var2 = new ROM.ROMLoadProgramTask(var1.getProgramFileName());
         Thread var3 = new Thread(var2);
         var3.start();
         break;
      case 3:
         this.notifyProgramListeners((byte)3, (String)null);
      }

   }

   public void valueChanged(ComputerPartEvent var1) {
      short var2 = var1.getValue();
      int var3 = var1.getIndex();
      this.clearErrorListeners();

      try {
         HackAssemblerTranslator.getInstance().codeToText(var2);
         this.setValueAt(var3, var2, true);
      } catch (AssemblerException var5) {
         this.notifyErrorListeners("Illegal instruction");
         this.quietUpdateGUI(var3, this.mem[var3]);
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

   class ROMLoadProgramTask implements Runnable {
      private String programName;

      public ROMLoadProgramTask(String var2) {
         this.programName = var2;
      }

      public void run() {
         ROM.this.clearErrorListeners();

         try {
            ROM.this.loadProgram(this.programName);
         } catch (ProgramException var2) {
            ROM.this.notifyErrorListeners(var2.getMessage());
         }

      }
   }
}
