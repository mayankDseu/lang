package Hack.CPUEmulator;

import Hack.Assembler.AssemblerException;
import Hack.Assembler.HackAssemblerTranslator;
import Hack.ComputerParts.Bus;
import Hack.ComputerParts.Register;
import Hack.Controller.ProgramException;

public class CPU {
   protected PointerAddressRegisterAdapter A;
   protected PointerAddressRegisterAdapter PC;
   protected Register D;
   protected RAM M;
   protected ROM rom;
   protected ALU alu;
   protected Bus bus;
   protected long time;
   protected HackAssemblerTranslator assemblerTranslator;

   public CPU(RAM var1, ROM var2, PointerAddressRegisterAdapter var3, Register var4, PointerAddressRegisterAdapter var5, ALU var6, Bus var7) {
      this.M = var1;
      this.rom = var2;
      this.A = var3;
      this.D = var4;
      this.PC = var5;
      this.alu = var6;
      this.bus = var7;
      var3.setUpdatePointer(false);
      this.assemblerTranslator = HackAssemblerTranslator.getInstance();
   }

   public Bus getBus() {
      return this.bus;
   }

   public Register getA() {
      return this.A;
   }

   public Register getD() {
      return this.D;
   }

   public Register getPC() {
      return this.PC;
   }

   public RAM getRAM() {
      return this.M;
   }

   public ROM getROM() {
      return this.rom;
   }

   public ALU getALU() {
      return this.alu;
   }

   public long getTime() {
      return this.time;
   }

   public void initProgram() {
      this.A.reset();
      this.A.setUpdatePointer(true);
      this.A.setUpdatePointer(false);
      this.D.reset();
      this.PC.reset();
      this.alu.reset();
      this.M.clearScreen();
      this.M.hideSelect();
      this.M.hideHighlight();
      this.rom.hideSelect();
      this.rom.hideHighlight();
      this.time = 0L;
   }

   public void executeInstruction() throws ProgramException {
      short var1 = this.rom.getValueAt(this.PC.get());
      boolean var2 = false;
      if ((var1 & '耀') == 0) {
         this.bus.send(this.rom, this.PC.get(), this.A, 0);
      } else if ((var1 & '\ue000') == 57344) {
         this.computeExp(var1);
         this.setDestination(var1);
         var2 = this.checkJump(var1);
      } else if (var1 != -32768) {
         throw new ProgramException("At line " + this.PC.get() + ": Illegal instruction");
      }

      if (!var2) {
         short var3 = (short)(this.PC.get() + 1);
         if (var3 < 0 || var3 >= '耀') {
            throw new ProgramException("At line " + this.PC.get() + ": Can't continue past last line");
         }

         this.PC.setValueAt(0, var3, true);
      }

      ++this.time;
   }

   protected void computeExp(short var1) throws ProgramException {
      boolean var2 = (var1 & 4096) > 0;
      boolean var3 = (var1 & 2048) > 0;
      boolean var4 = (var1 & 1024) > 0;
      boolean var5 = (var1 & 512) > 0;
      boolean var6 = (var1 & 256) > 0;
      boolean var7 = (var1 & 128) > 0;
      boolean var8 = (var1 & 64) > 0;

      try {
         this.alu.setCommand(this.assemblerTranslator.getExpByCode((short)(var1 & '\uffc0')), var3, var4, var5, var6, var7, var8);
      } catch (AssemblerException var10) {
      }

      this.bus.send(this.D, 0, this.alu, 0);
      if (var2) {
         short var9 = this.A.get();
         if (var9 < 0 || var9 >= this.M.getSize()) {
            throw new ProgramException("At line " + this.PC.get() + ": Expression involves M but A=" + var9 + " is an illegal memory address.");
         }

         this.A.setUpdatePointer(true);
         this.bus.send(this.M, var9, this.alu, 1);
         this.A.setUpdatePointer(false);
      } else {
         this.bus.send(this.A, 0, this.alu, 1);
      }

      this.alu.compute();
   }

   protected void setDestination(short var1) throws ProgramException {
      boolean var2 = (var1 & 32) > 0;
      boolean var3 = (var1 & 16) > 0;
      boolean var4 = (var1 & 8) > 0;
      if (var4) {
         short var5 = this.A.get();
         if (var5 < 0 || var5 >= this.M.getSize()) {
            throw new ProgramException("At line " + this.PC.get() + ": Destination is M but A=" + var5 + " is an illegal memory address.");
         }

         this.A.setUpdatePointer(true);
         this.bus.send(this.alu, 2, this.M, var5);
         this.A.setUpdatePointer(false);
      }

      if (var2) {
         this.bus.send(this.alu, 2, this.A, 0);
      }

      if (var3) {
         this.bus.send(this.alu, 2, this.D, 0);
      }

   }

   protected boolean checkJump(short var1) throws ProgramException {
      boolean var2 = (var1 & 4) > 0;
      boolean var3 = (var1 & 2) > 0;
      boolean var4 = (var1 & 1) > 0;
      boolean var5 = false;
      short var6 = this.alu.getValueAt(2);
      if (var6 < 0 && var2 || var6 == 0 && var3 || var6 > 0 && var4) {
         short var7 = this.A.get();
         if (var7 < 0 || var7 >= '耀') {
            throw new ProgramException("At line " + this.PC.get() + ": Jump requested but A=" + var7 + " is an illegal program address.");
         }

         this.bus.send(this.A, 0, this.PC, 0);
         var5 = true;
      }

      return var5;
   }
}
