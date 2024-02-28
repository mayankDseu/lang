package Hack.Assembler;

import Hack.Translators.HackTranslatorException;
import Hack.Utilities.Conversions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class HackAssemblerTranslator {
   public static final short NOP = -32768;
   private static final Short ZERO = new Short((short)-5504);
   private static final Short ONE = new Short((short)-4160);
   private static final Short MINUS_ONE = new Short((short)-4480);
   private static final Short EXP_D = new Short((short)-7424);
   private static final Short NOT_D = new Short((short)-7360);
   private static final Short EXP_M = new Short((short)-1024);
   private static final Short EXP_A = new Short((short)-5120);
   private static final Short NOT_M = new Short((short)-960);
   private static final Short NOT_A = new Short((short)-5056);
   private static final Short MINUS_D = new Short((short)-7232);
   private static final Short MINUS_M = new Short((short)-832);
   private static final Short MINUS_A = new Short((short)-4928);
   private static final Short D_PLUS_ONE = new Short((short)-6208);
   private static final Short M_PLUS_ONE = new Short((short)-576);
   private static final Short A_PLUS_ONE = new Short((short)-4672);
   private static final Short D_MINUS_ONE = new Short((short)-7296);
   private static final Short M_MINUS_ONE = new Short((short)-896);
   private static final Short A_MINUS_ONE = new Short((short)-4992);
   private static final Short D_PLUS_M = new Short((short)-3968);
   private static final Short D_PLUS_A = new Short((short)-8064);
   private static final Short D_MINUS_M = new Short((short)-2880);
   private static final Short D_MINUS_A = new Short((short)-6976);
   private static final Short M_MINUS_D = new Short((short)-3648);
   private static final Short A_MINUS_D = new Short((short)-7744);
   private static final Short D_AND_M = new Short((short)-4096);
   private static final Short D_AND_A = new Short((short)-8192);
   private static final Short D_OR_M = new Short((short)-2752);
   private static final Short D_OR_A = new Short((short)-6848);
   private static final Short A = new Short((short)32);
   private static final Short M = new Short((short)8);
   private static final Short D = new Short((short)16);
   private static final Short AM = new Short((short)40);
   private static final Short AD = new Short((short)48);
   private static final Short MD = new Short((short)24);
   private static final Short AMD = new Short((short)56);
   private static final Short JMP = new Short((short)7);
   private static final Short JMP_LESS_THEN = new Short((short)4);
   private static final Short JMP_EQUAL = new Short((short)2);
   private static final Short JMP_GREATER_THEN = new Short((short)1);
   private static final Short JMP_NOT_EQUAL = new Short((short)5);
   private static final Short JMP_LESS_EQUAL = new Short((short)6);
   private static final Short JMP_GREATER_EQUAL = new Short((short)3);
   private static HackAssemblerTranslator instance;
   private Hashtable expToCode;
   private Hashtable destToCode;
   private Hashtable jmpToCode;
   private Hashtable expToText;
   private Hashtable destToText;
   private Hashtable jmpToText;

   private HackAssemblerTranslator() {
      instance = this;
      this.initExp();
      this.initDest();
      this.initJmp();
   }

   public static HackAssemblerTranslator getInstance() {
      if (instance == null) {
         new HackAssemblerTranslator();
      }

      return instance;
   }

   public short getExpByText(String var1) throws AssemblerException {
      Short var2 = (Short)this.expToCode.get(var1);
      if (var2 == null) {
         throw new AssemblerException("Illegal exp: " + var1);
      } else {
         return var2;
      }
   }

   public String getExpByCode(short var1) throws AssemblerException {
      String var2 = (String)this.expToText.get(new Short(var1));
      if (var2 == null) {
         throw new AssemblerException("Illegal exp: " + var1);
      } else {
         return var2;
      }
   }

   public short getDestByText(String var1) throws AssemblerException {
      Short var2 = (Short)this.destToCode.get(var1);
      if (var2 == null) {
         throw new AssemblerException("Illegal dest: " + var1);
      } else {
         return var2;
      }
   }

   public String getDestByCode(short var1) throws AssemblerException {
      String var2 = (String)this.destToText.get(new Short(var1));
      if (var2 == null) {
         throw new AssemblerException("Illegal dest: " + var1);
      } else {
         return var2;
      }
   }

   public short getJmpByText(String var1) throws AssemblerException {
      Short var2 = (Short)this.jmpToCode.get(var1);
      if (var2 == null) {
         throw new AssemblerException("Illegal jmp: " + var1);
      } else {
         return var2;
      }
   }

   public String getJmpByCode(short var1) throws AssemblerException {
      String var2 = (String)this.jmpToText.get(new Short(var1));
      if (var2 == null) {
         throw new AssemblerException("Illegal jmp: " + var1);
      } else {
         return var2;
      }
   }

   public short textToCode(String var1) throws AssemblerException {
      boolean var2 = false;
      boolean var3 = false;
      short var4 = 0;
      short var5 = 0;

      try {
         AssemblyLineTokenizer var6 = new AssemblyLineTokenizer(var1);
         short var13;
         if (var6.isToken("@")) {
            var6.advance(true);

            try {
               var13 = Short.parseShort(var6.token());
            } catch (NumberFormatException var10) {
               throw new AssemblerException("A numeric value is expected");
            }
         } else {
            String var7 = var6.token();
            var6.advance(false);
            Short var8;
            if (var6.isToken("=")) {
               var8 = (Short)this.destToCode.get(var7);
               if (var8 == null) {
                  throw new AssemblerException("Destination expected");
               }

               var5 = var8;
               var6.advance(true);
            }

            if (!var7.equals("=") && var5 == 0) {
               var8 = (Short)this.expToCode.get(var7);
            } else {
               var8 = (Short)this.expToCode.get(var6.token());
            }

            if (var8 == null) {
               throw new AssemblerException("Expression expected");
            }

            short var14 = var8;
            var6.advance(false);
            if (var6.isToken(";")) {
               var6.advance(false);
            }

            if (!var6.isEnd()) {
               Short var9 = (Short)this.jmpToCode.get(var6.token());
               if (var9 == null) {
                  throw new AssemblerException("Jump directive expected");
               }

               var4 = var9;
               var6.ensureEnd();
            }

            var13 = (short)(var5 + var14 + var4);
         }

         return var13;
      } catch (IOException var11) {
         throw new AssemblerException("Error while parsing assembly line");
      } catch (HackTranslatorException var12) {
         throw new AssemblerException(var12.getMessage());
      }
   }

   public String codeToText(short var1) throws AssemblerException {
      StringBuffer var2 = new StringBuffer();
      if (var1 != -32768) {
         if ((var1 & '耀') == 0) {
            var2.append('@');
            var2.append(var1);
         } else {
            short var3 = (short)(var1 & '\uffc0');
            short var4 = (short)(var1 & 56);
            short var5 = (short)(var1 & 7);
            String var6 = this.getExpByCode(var3);
            if (!var6.equals("")) {
               if (var4 != 0) {
                  var2.append(this.getDestByCode(var4));
                  var2.append('=');
               }

               var2.append(var6);
               if (var5 != 0) {
                  var2.append(';');
                  var2.append(this.getJmpByCode(var5));
               }
            }
         }
      }

      return var2.toString();
   }

   public static short[] loadProgram(String var0, int var1, short var2) throws AssemblerException {
      Object var3 = null;
      File var4 = new File(var0);
      if (!var4.exists()) {
         throw new AssemblerException(var0 + " doesn't exist");
      } else {
         short[] var13;
         if (var0.endsWith(".hack")) {
            var13 = new short[var1];

            for(int var5 = 0; var5 < var1; ++var5) {
               var13[var5] = var2;
            }

            try {
               BufferedReader var14 = new BufferedReader(new FileReader(var0));

               String var6;
               short var16;
               for(int var7 = 0; (var6 = var14.readLine()) != null; var13[var7++] = var16) {
                  boolean var8 = false;
                  if (var7 >= var1) {
                     throw new AssemblerException("Program too large");
                  }

                  try {
                     var16 = (short)Conversions.binaryToInt(var6);
                  } catch (NumberFormatException var11) {
                     throw new AssemblerException("Illegal character");
                  }
               }

               var14.close();
            } catch (IOException var12) {
               throw new AssemblerException("IO error while reading " + var0);
            }
         } else {
            if (!var0.endsWith(".asm")) {
               throw new AssemblerException(var0 + " is not a .hack or .asm file");
            }

            try {
               HackAssembler var15 = new HackAssembler(var0, var1, var2, false);
               var13 = var15.getProgram();
            } catch (HackTranslatorException var10) {
               throw new AssemblerException(var10.getMessage());
            }
         }

         return var13;
      }
   }

   private void initExp() {
      this.expToCode = new Hashtable();
      this.expToText = new Hashtable();
      this.expToCode.put("0", ZERO);
      this.expToCode.put("1", ONE);
      this.expToCode.put("-1", MINUS_ONE);
      this.expToCode.put("D", EXP_D);
      this.expToCode.put("!D", NOT_D);
      this.expToCode.put("NOTD", NOT_D);
      this.expToCode.put("M", EXP_M);
      this.expToCode.put("A", EXP_A);
      this.expToCode.put("!M", NOT_M);
      this.expToCode.put("NOTM", NOT_M);
      this.expToCode.put("!A", NOT_A);
      this.expToCode.put("NOTA", NOT_A);
      this.expToCode.put("-D", MINUS_D);
      this.expToCode.put("-M", MINUS_M);
      this.expToCode.put("-A", MINUS_A);
      this.expToCode.put("D+1", D_PLUS_ONE);
      this.expToCode.put("M+1", M_PLUS_ONE);
      this.expToCode.put("A+1", A_PLUS_ONE);
      this.expToCode.put("D-1", D_MINUS_ONE);
      this.expToCode.put("M-1", M_MINUS_ONE);
      this.expToCode.put("A-1", A_MINUS_ONE);
      this.expToCode.put("D+M", D_PLUS_M);
      this.expToCode.put("M+D", D_PLUS_M);
      this.expToCode.put("D+A", D_PLUS_A);
      this.expToCode.put("A+D", D_PLUS_A);
      this.expToCode.put("D-M", D_MINUS_M);
      this.expToCode.put("D-A", D_MINUS_A);
      this.expToCode.put("M-D", M_MINUS_D);
      this.expToCode.put("A-D", A_MINUS_D);
      this.expToCode.put("D&M", D_AND_M);
      this.expToCode.put("M&D", D_AND_M);
      this.expToCode.put("D&A", D_AND_A);
      this.expToCode.put("A&D", D_AND_A);
      this.expToCode.put("D|M", D_OR_M);
      this.expToCode.put("M|D", D_OR_M);
      this.expToCode.put("D|A", D_OR_A);
      this.expToCode.put("A|D", D_OR_A);
      this.expToText.put(ZERO, "0");
      this.expToText.put(ONE, "1");
      this.expToText.put(MINUS_ONE, "-1");
      this.expToText.put(EXP_D, "D");
      this.expToText.put(NOT_D, "!D");
      this.expToText.put(EXP_M, "M");
      this.expToText.put(EXP_A, "A");
      this.expToText.put(NOT_M, "!M");
      this.expToText.put(NOT_A, "!A");
      this.expToText.put(MINUS_D, "-D");
      this.expToText.put(MINUS_M, "-M");
      this.expToText.put(MINUS_A, "-A");
      this.expToText.put(D_PLUS_ONE, "D+1");
      this.expToText.put(M_PLUS_ONE, "M+1");
      this.expToText.put(A_PLUS_ONE, "A+1");
      this.expToText.put(D_MINUS_ONE, "D-1");
      this.expToText.put(M_MINUS_ONE, "M-1");
      this.expToText.put(A_MINUS_ONE, "A-1");
      this.expToText.put(D_PLUS_M, "D+M");
      this.expToText.put(D_PLUS_A, "D+A");
      this.expToText.put(D_MINUS_M, "D-M");
      this.expToText.put(D_MINUS_A, "D-A");
      this.expToText.put(M_MINUS_D, "M-D");
      this.expToText.put(A_MINUS_D, "A-D");
      this.expToText.put(D_AND_M, "D&M");
      this.expToText.put(D_AND_A, "D&A");
      this.expToText.put(D_OR_M, "D|M");
      this.expToText.put(D_OR_A, "D|A");
   }

   private void initDest() {
      this.destToCode = new Hashtable();
      this.destToText = new Hashtable();
      this.destToCode.put("A", A);
      this.destToCode.put("M", M);
      this.destToCode.put("D", D);
      this.destToCode.put("AM", AM);
      this.destToCode.put("AD", AD);
      this.destToCode.put("MD", MD);
      this.destToCode.put("AMD", AMD);
      this.destToText.put(A, "A");
      this.destToText.put(M, "M");
      this.destToText.put(D, "D");
      this.destToText.put(AM, "AM");
      this.destToText.put(AD, "AD");
      this.destToText.put(MD, "MD");
      this.destToText.put(AMD, "AMD");
   }

   private void initJmp() {
      this.jmpToCode = new Hashtable();
      this.jmpToText = new Hashtable();
      this.jmpToCode.put("JMP", JMP);
      this.jmpToCode.put("JLT", JMP_LESS_THEN);
      this.jmpToCode.put("JEQ", JMP_EQUAL);
      this.jmpToCode.put("JGT", JMP_GREATER_THEN);
      this.jmpToCode.put("JNE", JMP_NOT_EQUAL);
      this.jmpToCode.put("JLE", JMP_LESS_EQUAL);
      this.jmpToCode.put("JGE", JMP_GREATER_EQUAL);
      this.jmpToText.put(JMP, "JMP");
      this.jmpToText.put(JMP_LESS_THEN, "JLT");
      this.jmpToText.put(JMP_EQUAL, "JEQ");
      this.jmpToText.put(JMP_GREATER_THEN, "JGT");
      this.jmpToText.put(JMP_NOT_EQUAL, "JNE");
      this.jmpToText.put(JMP_LESS_EQUAL, "JLE");
      this.jmpToText.put(JMP_GREATER_EQUAL, "JGE");
   }
}
