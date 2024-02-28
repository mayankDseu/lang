package Hack.Controller;

import Hack.Utilities.Conversions;

public class ScriptCondition {
   public static final byte EQUAL = 1;
   public static final byte GREATER = 2;
   public static final byte LESS = 3;
   public static final byte GREATER_EQUAL = 4;
   public static final byte LESS_EQUAL = 5;
   public static final byte NOT_EQUAL = 6;
   private String arg0;
   private String arg1;
   private byte comparisonOperator;

   public ScriptCondition(ScriptTokenizer var1) throws ScriptException, ControllerException {
      if (var1.getTokenType() != 3 && var1.getTokenType() != 4) {
         throw new ScriptException("A condition expected");
      } else {
         this.arg0 = var1.getToken();
         var1.advance();
         if (var1.getTokenType() == 2) {
            String var2 = var1.getToken();
            var1.advance();
            if (var1.getTokenType() == 2) {
               var2 = var2 + var1.getToken();
               var1.advance();
            }

            if (var2.equals("=")) {
               this.comparisonOperator = 1;
            } else if (var2.equals(">")) {
               this.comparisonOperator = 2;
            } else if (var2.equals(">=")) {
               this.comparisonOperator = 4;
            } else if (var2.equals("<")) {
               this.comparisonOperator = 3;
            } else if (var2.equals("<=")) {
               this.comparisonOperator = 5;
            } else {
               if (!var2.equals("<>")) {
                  throw new ScriptException("Illegal comparison operator: " + var2);
               }

               this.comparisonOperator = 6;
            }

            if (var1.getTokenType() != 3 && var1.getTokenType() != 4) {
               throw new ScriptException("A variable name or constant expected");
            } else {
               this.arg1 = var1.getToken();
               var1.advance();
            }
         } else {
            throw new ScriptException("Comparison operator expected");
         }
      }
   }

   public boolean compare(HackSimulator var1) throws ControllerException {
      boolean var2 = false;
      int var5 = 0;
      int var6 = 0;

      String var3;
      try {
         var3 = var1.getValue(this.arg0);
      } catch (VariableException var13) {
         var3 = this.arg0;
      }

      String var4;
      try {
         var4 = var1.getValue(this.arg1);
      } catch (VariableException var12) {
         var4 = this.arg1;
      }

      boolean var7;
      try {
         var5 = Integer.parseInt(Conversions.toDecimalForm(var3));
         var7 = true;
      } catch (NumberFormatException var11) {
         var7 = false;
      }

      boolean var8;
      try {
         var6 = Integer.parseInt(Conversions.toDecimalForm(var4));
         var8 = true;
      } catch (NumberFormatException var10) {
         var8 = false;
      }

      if (var7 && var8) {
         switch(this.comparisonOperator) {
         case 1:
            var2 = var5 == var6;
            break;
         case 2:
            var2 = var5 > var6;
            break;
         case 3:
            var2 = var5 < var6;
            break;
         case 4:
            var2 = var5 >= var6;
            break;
         case 5:
            var2 = var5 <= var6;
            break;
         case 6:
            var2 = var5 != var6;
         }
      } else {
         if (var7 || var8) {
            throw new ControllerException("Cannot compare an integer with a string");
         }

         switch(this.comparisonOperator) {
         case 1:
            var2 = var3.equals(var4);
            break;
         case 6:
            var2 = !var3.equals(var4);
            break;
         default:
            throw new ControllerException("Only = and <> can be used to compare strings");
         }
      }

      return var2;
   }
}
