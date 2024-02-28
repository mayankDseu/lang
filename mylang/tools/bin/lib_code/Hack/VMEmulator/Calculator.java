package Hack.VMEmulator;

import Hack.ComputerParts.ComputerPartGUI;
import Hack.ComputerParts.ValueComputerPart;

public class Calculator extends ValueComputerPart {
   public static final int ADD = 0;
   public static final int SUBTRACT = 1;
   public static final int NEGATE = 2;
   public static final int AND = 3;
   public static final int OR = 4;
   public static final int NOT = 5;
   public static final int EQUAL = 6;
   public static final int GREATER_THAN = 7;
   public static final int LESS_THAN = 8;
   public static final char ADD_SYMBOL = '+';
   public static final char SUBTRACT_SYMBOL = '-';
   public static final char NEGATE_SYMBOL = '-';
   public static final char AND_SYMBOL = '&';
   public static final char OR_SYMBOL = '|';
   public static final char NOT_SYMBOL = '!';
   public static final char EQUAL_SYMBOL = '=';
   public static final char GREATER_THAN_SYMBOL = '>';
   public static final char LESS_THAN_SYMBOL = '<';
   private CalculatorGUI gui;
   private char[] operators;
   private short input0;
   private short input1;
   private short output;

   public Calculator(CalculatorGUI var1) {
      super(var1 != null);
      this.gui = var1;
      this.operators = new char[9];
      this.operators[0] = '+';
      this.operators[1] = '-';
      this.operators[2] = '-';
      this.operators[3] = '&';
      this.operators[4] = '|';
      this.operators[5] = '!';
      this.operators[6] = '=';
      this.operators[7] = '>';
      this.operators[8] = '<';
   }

   public void compute(int var1) {
      short var2 = 0;
      switch(var1) {
      case 0:
         var2 = (short)(this.input0 + this.input1);
         break;
      case 1:
         var2 = (short)(this.input0 - this.input1);
         break;
      case 2:
         var2 = (short)(-this.input1);
         break;
      case 3:
         var2 = (short)(this.input0 & this.input1);
         break;
      case 4:
         var2 = (short)(this.input0 | this.input1);
         break;
      case 5:
         var2 = (short)(~this.input1);
         break;
      case 6:
         var2 = (short)(this.input0 == this.input1 ? -1 : 0);
         break;
      case 7:
         var2 = (short)(this.input0 > this.input1 ? -1 : 0);
         break;
      case 8:
         var2 = (short)(this.input0 < this.input1 ? -1 : 0);
      }

      this.setValueAt(2, var2, true);
   }

   public ComputerPartGUI getGUI() {
      return this.gui;
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

   public void showCalculator(int var1, int var2) {
      if (this.animate) {
         if (var2 == 2) {
            this.gui.showLeftInput();
         } else {
            this.gui.hideLeftInput();
         }

         this.gui.reset();
         this.gui.setOperator(this.operators[var1]);
         this.gui.showCalculator();
      }

   }

   public void hideCalculator() {
      if (this.animate) {
         this.gui.hideCalculator();
      }

   }
}
