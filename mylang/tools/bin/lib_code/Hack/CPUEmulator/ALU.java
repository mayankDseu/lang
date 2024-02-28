package Hack.CPUEmulator;

import Hack.ComputerParts.ComputerPartGUI;
import Hack.ComputerParts.ValueComputerPart;
import Hack.Utilities.Definitions;

public class ALU extends ValueComputerPart {
   private static final int BODY_FLASH_TIME = 500;
   private static final int COMMAND_FLASH_TIME = 500;
   private short input0;
   private short input1;
   private short output;
   private String commandDescription;
   private ALUGUI gui;
   private boolean zero0;
   private boolean zero1;
   private boolean negate0;
   private boolean negate1;
   private boolean ADDorAND;
   private boolean negateOutput;

   public ALU(ALUGUI var1) {
      super(var1 != null);
      this.gui = var1;
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
   }

   public synchronized void setCommand(String var1, boolean var2, boolean var3, boolean var4, boolean var5, boolean var6, boolean var7) {
      this.commandDescription = var1;
      this.zero0 = var2;
      this.negate0 = var3;
      this.zero1 = var4;
      this.negate1 = var5;
      this.ADDorAND = var6;
      this.negateOutput = var7;
      if (this.displayChanges) {
         this.gui.setCommand(var1);
      }

      if (this.animate) {
         this.gui.commandFlash();

         try {
            this.wait(500L);
         } catch (InterruptedException var9) {
         }

         this.gui.hideCommandFlash();
      }

   }

   public synchronized void compute() {
      if (this.animate) {
         this.gui.bodyFlash();

         try {
            this.wait(500L);
         } catch (InterruptedException var2) {
         }

         this.gui.hideBodyFlash();
      }

      short var1 = Definitions.computeALU(this.input0, this.input1, this.zero0, this.negate0, this.zero1, this.negate1, this.ADDorAND, this.negateOutput);
      this.setValueAt(2, var1, false);
   }

   public void setInput0(short var1) {
      this.setValueAt(0, var1, false);
   }

   public void setInput1(short var1) {
      this.setValueAt(1, var1, false);
   }

   public short getOutput() {
      return this.getValueAt(2);
   }

   public short getValueAt(int var1) {
      short var2 = 0;
      switch(var1) {
      case 0:
         var2 = this.input0;
         break;
      case 1:
         var2 = this.input1;
         break;
      case 2:
         var2 = this.output;
      }

      return var2;
   }

   public void doSetValueAt(int var1, short var2) {
      switch(var1) {
      case 0:
         this.input0 = var2;
         break;
      case 1:
         this.input1 = var2;
         break;
      case 2:
         this.output = var2;
      }

   }

   public void reset() {
      super.reset();
      this.input0 = this.nullValue;
      this.input1 = this.nullValue;
      this.output = this.nullValue;
   }

   public void refreshGUI() {
      this.quietUpdateGUI(0, this.input0);
      this.quietUpdateGUI(1, this.input1);
      this.quietUpdateGUI(2, this.output);
   }
}
