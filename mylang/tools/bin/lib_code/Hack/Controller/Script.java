package Hack.Controller;

import Hack.Utilities.Conversions;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class Script {
   public static final int MAX_SIMULATOR_COMMAND_ARGUMENTS = 4;
   public static final int MAX_OUTPUT_LIST_ARGUMENTS = 20;
   private Vector commands;
   private Vector lineNumbers;
   private String scriptName;
   private ScriptTokenizer input;

   public Script(String var1) throws ScriptException, ControllerException {
      this.scriptName = var1;

      try {
         this.input = new ScriptTokenizer(new FileReader(var1));
      } catch (IOException var3) {
         throw new ScriptException("Script " + var1 + " not found");
      }

      this.commands = new Vector();
      this.lineNumbers = new Vector();
      this.buildScript();
   }

   private void buildScript() throws ScriptException, ControllerException {
      boolean var1 = false;
      boolean var2 = false;
      boolean var3 = false;
      boolean var4 = false;
      boolean var5 = false;
      Command var6 = null;
      int var7 = 0;

      while(this.input.hasMoreTokens()) {
         var7 = this.input.getLineNumber() - 1;
         this.input.advance();
         if (var3 && this.input.getTokenType() == 2 && this.input.getSymbol() == '}') {
            this.scriptError("An empty " + (var1 ? "Repeat" : "While") + " block is not allowed");
         }

         var3 = false;
         switch(this.input.getTokenType()) {
         case 1:
            var6 = this.createControllerCommand();
            if (var6.getCode() == 8) {
               if (!var1 && !var2) {
                  var1 = true;
                  var3 = true;
               } else {
                  this.scriptError("Nested Repeat and While are not allowed");
               }
            } else if (var6.getCode() == 10) {
               if (!var1 && !var2) {
                  var2 = true;
                  var3 = true;
               } else {
                  this.scriptError("Nested Repeat and While are not allowed");
               }
            } else if (var6.getCode() == 4) {
               var4 = true;
            } else if (var6.getCode() == 5 && !var4) {
               this.scriptError("No output list created");
            }
            break;
         case 3:
            var6 = this.createSimulatorCommand();
            break;
         case 4:
            this.scriptError("A command cannot begin with " + this.input.getIntValue());
         case 2:
            if (this.input.getSymbol() == '}') {
               if (!var1 && !var2) {
                  this.scriptError("a '}' without a Repeat or While");
               } else if (var1) {
                  var6 = new Command((byte)9);
                  var1 = false;
               } else if (var2) {
                  var6 = new Command((byte)11);
                  var2 = false;
               }
            } else {
               this.scriptError("A command cannot begin with '" + this.input.getSymbol() + "'");
            }
         }

         switch(this.input.getSymbol()) {
         case '!':
            var6.setTerminator((byte)3);
            break;
         case ',':
            var6.setTerminator((byte)1);
            break;
         case ';':
            var6.setTerminator((byte)2);
         }

         this.commands.addElement(var6);
         this.lineNumbers.addElement(new Integer(var7));
      }

      if (var1 || var2) {
         this.scriptError("Repeat or While not closed");
      }

      var6 = new Command((byte)12);
      this.commands.addElement(var6);
      this.lineNumbers.addElement(new Integer(var7));
   }

   private Command createSimulatorCommand() throws ControllerException, ScriptException {
      String[] var1 = this.readArgs(4);

      int var2;
      for(var2 = 0; var2 < var1.length && var1[var2] != null; ++var2) {
      }

      String[] var3 = new String[var2];
      System.arraycopy(var1, 0, var3, 0, var2);
      return new Command((byte)1, var3);
   }

   private Command createControllerCommand() throws ControllerException, ScriptException {
      Command var1 = null;
      switch(this.input.getKeywordType()) {
      case 1:
         var1 = this.createOutputFileCommand();
         break;
      case 2:
         var1 = this.createCompareToCommand();
         break;
      case 3:
         var1 = this.createOutputListCommand();
         break;
      case 4:
         var1 = this.createOutputCommand();
         break;
      case 5:
         var1 = this.createBreakpointCommand();
         break;
      case 6:
         var1 = this.createClearBreakpointsCommand();
         break;
      case 7:
         var1 = this.createRepeatCommand();
         break;
      case 8:
         var1 = this.createWhileCommand();
         break;
      case 9:
         var1 = this.createEchoCommand();
         break;
      case 10:
         var1 = this.createClearEchoCommand();
      }

      return var1;
   }

   private Command createOutputFileCommand() throws ControllerException, ScriptException {
      this.input.advance();
      String[] var1 = this.readArgs(1);
      return new Command((byte)2, var1[0]);
   }

   private Command createCompareToCommand() throws ControllerException, ScriptException {
      this.input.advance();
      String[] var1 = this.readArgs(1);
      return new Command((byte)3, var1[0]);
   }

   private Command createOutputListCommand() throws ControllerException, ScriptException {
      this.input.advance();
      String[] var1 = this.readArgs(20);

      int var2;
      for(var2 = 0; var2 < var1.length && var1[var2] != null; ++var2) {
      }

      VariableFormat[] var3 = new VariableFormat[var2];

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5 = var1[var4].indexOf(37);
         if (var5 == -1) {
            var5 = var1[var4].length();
            var1[var4] = var1[var4] + "%B1.1.1";
         }

         String var6 = var1[var4].substring(0, var5);
         char var7 = var1[var4].charAt(var5 + 1);
         if (var7 != 'B' && var7 != 'D' && var7 != 'X' && var7 != 'S') {
            this.scriptError("%" + var7 + " is not a legal format");
         }

         int var8 = 0;
         int var9 = var1[var4].indexOf(46, var5);
         if (var9 == -1) {
            this.scriptError("Missing '.'");
         }

         try {
            var8 = Integer.parseInt(var1[var4].substring(var5 + 2, var9));
         } catch (NumberFormatException var16) {
            this.scriptError("padL must be a number");
         }

         if (var8 < 0) {
            this.scriptError("padL must be positive");
         }

         int var10 = 0;
         int var11 = var1[var4].indexOf(46, var9 + 1);
         if (var11 == -1) {
            this.scriptError("Missing '.'");
         }

         try {
            var10 = Integer.parseInt(var1[var4].substring(var9 + 1, var11));
         } catch (NumberFormatException var15) {
            this.scriptError("len must be a number");
         }

         if (var10 < 1) {
            this.scriptError("len must be greater than 0");
         }

         int var12 = 0;

         try {
            var12 = Integer.parseInt(var1[var4].substring(var11 + 1));
         } catch (NumberFormatException var14) {
            this.scriptError("padR must be a number");
         }

         if (var12 < 0) {
            this.scriptError("padR must be positive");
         }

         var3[var4] = new VariableFormat(var6, var7, var8, var12, var10);
      }

      return new Command((byte)4, var3);
   }

   private Command createOutputCommand() throws ControllerException, ScriptException {
      this.input.advance();
      this.checkTerminator();
      return new Command((byte)5);
   }

   private Command createEchoCommand() throws ControllerException, ScriptException {
      this.input.advance();
      String[] var1 = this.readArgs(1);
      return new Command((byte)13, var1[0]);
   }

   private Command createClearEchoCommand() throws ControllerException, ScriptException {
      this.input.advance();
      this.checkTerminator();
      return new Command((byte)14);
   }

   private Command createBreakpointCommand() throws ControllerException, ScriptException {
      this.input.advance();
      String[] var1 = this.readArgs(2);

      int var2;
      for(var2 = 0; var2 < var1.length && var1[var2] != null; ++var2) {
      }

      if (var2 < 2) {
         this.scriptError("Not enough arguments");
      }

      String var3 = var1[1];
      if (var3.startsWith("%S")) {
         var3 = var3.substring(2);
      } else if (var1[1].startsWith("%")) {
         var3 = Conversions.toDecimalForm(var3);
      }

      Breakpoint var4 = new Breakpoint(var1[0], var3);
      return new Command((byte)6, var4);
   }

   private Command createClearBreakpointsCommand() throws ControllerException, ScriptException {
      this.input.advance();
      this.checkTerminator();
      return new Command((byte)7);
   }

   private Command createRepeatCommand() throws ScriptException, ControllerException {
      this.input.advance();
      int var1 = 0;
      if (this.input.getTokenType() == 4) {
         var1 = this.input.getIntValue();
         if (var1 < 1) {
            this.scriptError("Illegal repeat quantity");
         }

         this.input.advance();
      }

      if (this.input.getTokenType() != 2 || this.input.getSymbol() != '{') {
         this.scriptError("Missing '{' in repeat command");
      }

      return new Command((byte)8, new Integer(var1));
   }

   private Command createWhileCommand() throws ScriptException, ControllerException {
      this.input.advance();
      ScriptCondition var1 = null;

      try {
         var1 = new ScriptCondition(this.input);
      } catch (ScriptException var3) {
         this.scriptError(var3.getMessage());
      }

      if (this.input.getTokenType() != 2 || this.input.getSymbol() != '{') {
         this.scriptError("Missing '{' in while command");
      }

      return new Command((byte)10, var1);
   }

   private String[] readArgs(int var1) throws ControllerException, ScriptException {
      String[] var2 = new String[var1];
      int var3 = 0;

      while(this.input.hasMoreTokens() && this.input.getTokenType() != 2 && var3 < var1) {
         var2[var3++] = this.input.getToken();
         this.input.advance();
      }

      this.checkTerminator();
      if (var3 == 0) {
         this.scriptError("Missing arguments");
      }

      return var2;
   }

   private void checkTerminator() throws ScriptException {
      if (this.input.getTokenType() != 2) {
         if (this.input.hasMoreTokens()) {
            this.scriptError("too many arguments");
         } else {
            this.scriptError("Script ends without a terminator");
         }
      } else if (this.input.getSymbol() != ',' && this.input.getSymbol() != ';' && this.input.getSymbol() != '!') {
         this.scriptError("Illegal terminator: '" + this.input.getSymbol() + "'");
      }

   }

   private void scriptError(String var1) throws ScriptException {
      throw new ScriptException(var1, this.scriptName, this.input.getLineNumber());
   }

   public Command getCommandAt(int var1) {
      return (Command)this.commands.elementAt(var1);
   }

   public int getLineNumberAt(int var1) {
      return (Integer)this.lineNumbers.elementAt(var1);
   }

   public int getLength() {
      return this.commands.size();
   }
}
