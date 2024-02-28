package Hack.Assembler;

import Hack.ComputerParts.TextFileEvent;
import Hack.ComputerParts.TextFileGUI;
import Hack.Translators.HackTranslator;
import Hack.Translators.HackTranslatorEvent;
import Hack.Translators.HackTranslatorException;
import Hack.Utilities.Conversions;
import Hack.Utilities.Definitions;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class HackAssembler extends HackTranslator {
   private BufferedReader comparisonReader;
   private String comparisonFileName;
   private Hashtable symbolTable;
   private short[] comparisonProgram;
   private HackAssemblerTranslator translator;
   private short varIndex;

   public HackAssembler(String var1, int var2, short var3, boolean var4) throws HackTranslatorException {
      super(var1, var2, var3, var4);
   }

   public HackAssembler(HackAssemblerGUI var1, int var2, short var3, String var4) throws HackTranslatorException {
      super(var1, var2, var3, var4);
      var1.enableLoadComparison();
      var1.hideComparison();
   }

   protected String getSourceExtension() {
      return "asm";
   }

   protected String getDestinationExtension() {
      return "hack";
   }

   protected String getName() {
      return "Assembler";
   }

   protected void init(int var1, short var2) {
      super.init(var1, var2);
      this.translator = HackAssemblerTranslator.getInstance();
   }

   private void checkComparisonFile(String var1) throws HackTranslatorException {
      if (!var1.endsWith("." + this.getDestinationExtension())) {
         throw new HackTranslatorException(var1 + " is not a ." + this.getDestinationExtension() + " file");
      } else {
         File var2 = new File(var1);
         if (!var2.exists()) {
            throw new HackTranslatorException("File " + var1 + " does not exist");
         }
      }
   }

   protected void restartCompilation() {
      super.restartCompilation();
      this.varIndex = 16;
      if (this.gui != null) {
         ((HackAssemblerGUI)this.gui).enableLoadComparison();
      }

   }

   private void resetComparisonFile() throws HackTranslatorException {
      try {
         this.comparisonReader = new BufferedReader(new FileReader(this.comparisonFileName));
         if (this.gui != null) {
            TextFileGUI var1 = ((HackAssemblerGUI)this.gui).getComparison();
            var1.reset();
            var1.setContents(this.comparisonFileName);
            this.comparisonProgram = new short[var1.getNumberOfLines()];

            for(int var2 = 0; var2 < var1.getNumberOfLines(); ++var2) {
               if (var1.getLineAt(var2).length() != 16) {
                  throw new HackTranslatorException("Error in file " + this.comparisonFileName + ": Line " + var2 + " does not contain exactly " + 16 + " characters");
               }

               try {
                  this.comparisonProgram[var2] = (short)Conversions.binaryToInt(var1.getLineAt(var2));
               } catch (NumberFormatException var4) {
                  throw new HackTranslatorException("Error in file " + this.comparisonFileName + ": Line " + var2 + " does not contain only 1/0 characters");
               }
            }
         }

      } catch (IOException var5) {
         throw new HackTranslatorException("Error reading from file " + this.comparisonFileName);
      }
   }

   protected void initSource() throws HackTranslatorException {
      this.generateSymbolTable();
   }

   private void generateSymbolTable() throws HackTranslatorException {
      this.symbolTable = Definitions.getInstance().getAddressesTable();
      short var1 = 0;

      try {
         BufferedReader var4 = new BufferedReader(new FileReader(this.sourceFileName));

         String var2;
         while((var2 = var4.readLine()) != null) {
            AssemblyLineTokenizer var5 = new AssemblyLineTokenizer(var2);
            if (!var5.isEnd()) {
               if (var5.isToken("(")) {
                  var5.advance(true);
                  String var3 = var5.token();
                  var5.advance(true);
                  if (!var5.isToken(")")) {
                     this.error("')' expected");
                  }

                  var5.ensureEnd();
                  this.symbolTable.put(var3, new Short(var1));
               } else if (var5.contains("[")) {
                  var1 = (short)(var1 + 2);
               } else {
                  ++var1;
               }
            }
         }

         var4.close();
      } catch (IOException var6) {
         throw new HackTranslatorException("Error reading from file " + this.sourceFileName);
      }
   }

   protected void initCompilation() throws HackTranslatorException {
      if (this.gui != null && (this.inFullCompilation || !this.compilationStarted)) {
         ((HackAssemblerGUI)this.gui).disableLoadComparison();
      }

   }

   protected void successfulCompilation() throws HackTranslatorException {
      if (this.comparisonReader == null) {
         super.successfulCompilation();
      } else if (this.gui != null) {
         ((HackAssemblerGUI)this.gui).displayMessage("File compilation & comparison succeeded", false);
      }

   }

   protected int[] compileLineAndCount(String var1) throws HackTranslatorException {
      int[] var2 = super.compileLineAndCount(var1);
      if (var2 != null && this.comparisonReader != null) {
         int var3 = var2[1] - var2[0] + 1;
         boolean var4 = this.compare(var2);
         if (this.inFullCompilation) {
            if (!var4 && this.gui != null) {
               this.programSize = this.destPC + var3 - 1;
               this.showProgram(this.programSize);
               this.gui.getSource().addHighlight(this.sourcePC, true);
               this.gui.getDestination().addHighlight(this.destPC - 1, true);
               ((HackAssemblerGUI)this.gui).getComparison().addHighlight(this.destPC - 1, true);
               this.gui.enableRewind();
               this.gui.enableLoadSource();
            }
         } else if (var4) {
            ((HackAssemblerGUI)this.gui).getComparison().addHighlight(this.destPC + var3 - 2, true);
         } else {
            this.gui.getDestination().addHighlight(this.destPC - 1, true);
            ((HackAssemblerGUI)this.gui).getComparison().addHighlight(this.destPC - 1, true);
         }

         if (!var4) {
            throw new HackTranslatorException("Comparison failure");
         }
      }

      return var2;
   }

   private boolean compare(int[] var1) {
      boolean var2 = true;
      int var3 = var1[1] - var1[0] + 1;

      for(int var4 = 0; var4 < var3 && var2; ++var4) {
         var2 = this.program[var1[0] + var4] == this.comparisonProgram[var1[0] + var4];
      }

      return var2;
   }

   protected String getCodeString(short var1, int var2, boolean var3) {
      return Conversions.decimalToBinary(var1, 16);
   }

   protected void fastForward() {
      ((HackAssemblerGUI)this.gui).disableLoadComparison();
      super.fastForward();
   }

   protected void hidePointers() {
      super.hidePointers();
      if (this.comparisonReader != null) {
         ((HackAssemblerGUI)this.gui).getComparison().clearHighlights();
      }

   }

   protected void end(boolean var1) {
      super.end(var1);
      ((HackAssemblerGUI)this.gui).disableLoadComparison();
   }

   protected void stop() {
      super.stop();
      ((HackAssemblerGUI)this.gui).disableLoadComparison();
   }

   protected void rewind() {
      super.rewind();
      if (this.comparisonReader != null) {
         ((HackAssemblerGUI)this.gui).getComparison().clearHighlights();
         ((HackAssemblerGUI)this.gui).getComparison().hideSelect();
      }

   }

   protected void compileLine(String var1) throws HackTranslatorException {
      try {
         AssemblyLineTokenizer var2 = new AssemblyLineTokenizer(var1);
         if (!var2.isEnd() && !var2.isToken("(")) {
            if (var2.isToken("@")) {
               var2.advance(true);
               boolean var3 = true;
               String var12 = var2.token();
               var2.ensureEnd();

               try {
                  Short.parseShort(var12);
               } catch (NumberFormatException var8) {
                  var3 = false;
               }

               if (!var3) {
                  Short var13 = (Short)this.symbolTable.get(var12);
                  if (var13 == null) {
                     short var10004 = this.varIndex;
                     this.varIndex = (short)(var10004 + 1);
                     var13 = new Short(var10004);
                     this.symbolTable.put(var12, var13);
                  }

                  this.addCommand(this.translator.textToCode("@" + var13));
               } else {
                  this.addCommand(this.translator.textToCode(var1));
               }
            } else {
               try {
                  this.addCommand(this.translator.textToCode(var1));
               } catch (AssemblerException var9) {
                  int var4 = var1.indexOf("[");
                  if (var4 >= 0) {
                     int var5 = var1.lastIndexOf("[");
                     int var6 = var1.indexOf("]");
                     if (var4 == var5 && var4 <= var6 && var4 + 1 != var6) {
                        String var7 = var1.substring(var4 + 1, var6);
                        this.compileLine("@" + var7);
                        this.compileLine(var1.substring(0, var4).concat(var1.substring(var6 + 1)));
                        return;
                     }

                     throw new AssemblerException("Illegal use of the [] notation");
                  }

                  throw new AssemblerException(var9.getMessage());
               }
            }
         }

      } catch (IOException var10) {
         throw new HackTranslatorException("Error reading from file " + this.sourceFileName);
      } catch (AssemblerException var11) {
         throw new HackTranslatorException(var11.getMessage(), this.sourcePC);
      }
   }

   protected void finalizeCompilation() {
   }

   public void rowSelected(TextFileEvent var1) {
      super.rowSelected(var1);
      int[] var2 = this.rowIndexToRange(var1.getRowIndex());
      if (var2 != null) {
         if (this.comparisonReader != null) {
            ((HackAssemblerGUI)this.gui).getComparison().select(var2[0], var2[1]);
         }
      } else if (this.comparisonReader != null) {
         ((HackAssemblerGUI)this.gui).getComparison().hideSelect();
      }

   }

   public void actionPerformed(HackTranslatorEvent var1) {
      super.actionPerformed(var1);
      switch(var1.getAction()) {
      case 7:
         this.comparisonFileName = "";
         this.comparisonReader = null;
         ((HackAssemblerGUI)this.gui).setComparisonName("");
         ((HackAssemblerGUI)this.gui).hideComparison();
         break;
      case 9:
         this.clearMessage();
         String var2 = (String)var1.getData();

         try {
            this.checkComparisonFile(var2);
            this.comparisonFileName = var2;
            this.saveWorkingDir(new File(var2));
            this.resetComparisonFile();
            ((HackAssemblerGUI)this.gui).showComparison();
         } catch (HackTranslatorException var4) {
            this.gui.displayMessage(var4.getMessage(), true);
         }
      }

   }
}
