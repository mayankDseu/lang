package Hack.Compiler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

public class CompilationEngine {
   private static final int GENERAL_TYPE = 1;
   private static final int NUMERIC_TYPE = 2;
   private static final int INT_TYPE = 3;
   private static final int CHAR_TYPE = 4;
   private static final int BOOLEAN_TYPE = 5;
   private static final int STRING_TYPE = 6;
   private static final int THIS_TYPE = 7;
   private static final int NULL_TYPE = 8;
   private JackTokenizer input;
   private VMWriter output;
   private SymbolTable identifiers;
   private HashMap subroutines = new HashMap();
   private HashSet classes = new HashSet();
   private Vector subroutineCalls = new Vector();
   private int ifCounter;
   private int whileCounter;
   private int subroutineType;
   private int[] expTypes;
   private int expIndex;
   private String fileName;
   private boolean validJack;

   CompilationEngine() {
   }

   public boolean compileClass(JackTokenizer var1, VMWriter var2, String var3, String var4) {
      this.input = var1;
      this.output = var2;
      this.fileName = var4;
      this.expTypes = new int[100];
      this.expIndex = -1;
      this.validJack = true;
      var1.advance();
      String var5 = null;

      label93:
      try {
         try {
            if (this.isKeywordClass()) {
               var1.advance();
            } else {
               this.recoverableError("Expected 'class'", -1, "", var4);
            }

            if (this.isIdentifier()) {
               var5 = var1.getIdentifier();
               if (!var5.equals(var3)) {
                  this.recoverableError("The class name doesn't match the file name", -1, "", var4);
               }

               var1.advance();
            } else {
               this.recoverableError("Expected a class name", -1, "", var4);
               var5 = var3;
            }

            this.identifiers = new SymbolTable(var5);
            if (this.isSymbol('{')) {
               var1.advance();
            } else {
               this.recoverableError("Expected {", -1, "", var4);
            }

            this.compileFieldAndStaticDeclarations();
            this.compileAllSubroutines();
            if (!this.isSymbol('}')) {
               this.recoverableError("Expected }", -1, "", var4);
            }

            if (var1.hasMoreTokens()) {
               this.recoverableError("Expected end-of-file", -1, "", var4);
            }
         } catch (JackException var11) {
         }
      } finally {
         break label93;
      }

      var2.close();
      if (this.validJack) {
         this.classes.add(var5);
      }

      return this.validJack;
   }

   boolean verifySubroutineCalls() {
      this.validJack = true;
      Iterator var1 = this.subroutineCalls.iterator();

      while(true) {
         while(true) {
            Object[] var2;
            String var3;
            boolean var4;
            short var5;
            String var6;
            String var7;
            int var8;
            do {
               if (!var1.hasNext()) {
                  return this.validJack;
               }

               var2 = (Object[])var1.next();
               var3 = (String)var2[0];
               var4 = (Boolean)var2[1];
               var5 = (Short)var2[2];
               var6 = (String)var2[3];
               var7 = (String)var2[4];
               var8 = (Integer)var2[5];
            } while(!this.classes.contains(var3.substring(0, var3.indexOf("."))));

            if (!this.subroutines.containsKey(var3)) {
               this.recoverableError((var4 ? "Method " : "Function or constructor ") + var3 + " doesn't exist", var8, var7, var6);
            } else {
               var2 = (Object[])this.subroutines.get(var3);
               int var9 = (Integer)var2[0];
               short var10 = (Short)var2[1];
               if (var4 && var9 != 1) {
                  this.recoverableError((var9 == 2 ? "Function " : "Constructor ") + var3 + " called as a method", var8, var7, var6);
               } else if (!var4 && var9 == 1) {
                  this.recoverableError("Method " + var3 + " called as a function/constructor", var8, var7, var6);
               }

               if (var5 != var10) {
                  this.recoverableError("Subroutine " + var3 + " (declared to accept " + var10 + " parameter(s)) called with " + var5 + " parameter(s)", var8, var7, var6);
               }
            }
         }
      }
   }

   private void compileAllSubroutines() throws JackException {
      while(this.isKeywordMethod() || this.isKeywordFunction() || this.isKeywordConstructor()) {
         try {
            if (this.isKeywordMethod()) {
               this.compileMethod();
            } else if (this.isKeywordFunction()) {
               this.compileFunction();
            } else {
               this.compileConstructor();
            }
         } catch (JackException var2) {
            while(!this.isKeywordMethod() && !this.isKeywordFunction() && !this.isKeywordConstructor() && this.input.hasMoreTokens()) {
               this.input.advance();
            }
         }
      }

   }

   private void compileMethod() throws JackException {
      SymbolTable var10001 = this.identifiers;
      this.compileSubroutine(1);
   }

   private void compileFunction() throws JackException {
      SymbolTable var10001 = this.identifiers;
      this.compileSubroutine(2);
   }

   private void compileConstructor() throws JackException {
      SymbolTable var10001 = this.identifiers;
      this.compileSubroutine(3);
   }

   private void compileSubroutine(int var1) throws JackException {
      this.subroutineType = var1;
      this.ifCounter = 0;
      this.whileCounter = 0;
      this.input.advance();
      String var2 = null;
      if (this.isKeywordVoid()) {
         var2 = "void";
      } else {
         var2 = this.getType();
      }

      int var3 = this.input.getLineNumber();
      this.input.advance();
      String var4 = null;
      String var5;
      if (this.isIdentifier()) {
         var4 = this.input.getIdentifier();
         var5 = this.identifiers.getClassName() + "." + var4;
         if (this.subroutines.containsKey(var5)) {
            this.recoverableError("Subroutine " + var4 + " redeclared", -1, "", this.fileName);
         }

         this.input.advance();
      } else {
         this.recoverableError("Expected a type followed by a subroutine name", -1, "", this.fileName);
         var5 = this.identifiers.getClassName() + "." + "unknownname";
      }

      switch(var1) {
      case 1:
         this.identifiers.startMethod(var4, var2);
         break;
      case 2:
         this.identifiers.startFunction(var4, var2);
         break;
      case 3:
         this.identifiers.startConstructor(var4);
         if (!var2.equals(this.identifiers.getClassName())) {
            this.recoverableError("The return type of a constructor must be of the class type", var3);
         }
      }

      if (this.isSymbol('(')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected (");
      }

      short var6 = this.compileParametersList();
      this.input.advance();
      this.compileSubroutineBody(var5);
      this.identifiers.endSubroutine();
      if (var4 != null) {
         this.subroutines.put(var5, new Object[]{new Integer(var1), new Short(var6)});
      }

   }

   private void compileSubroutineBody(String var1) throws JackException {
      if (this.isSymbol('{')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected {");
      }

      this.compileLocalsDeclarations();
      SymbolTable var10001 = this.identifiers;
      short var2 = this.identifiers.getNumberOfIdentifiers(3);
      this.output.function(var1, var2);
      var10001 = this.identifiers;
      if (this.subroutineType == 1) {
         this.output.push("argument", (short)0);
         this.output.pop("pointer", (short)0);
      } else {
         var10001 = this.identifiers;
         if (this.subroutineType == 3) {
            var10001 = this.identifiers;
            short var3 = this.identifiers.getNumberOfIdentifiers(1);
            this.output.push("constant", var3);
            this.output.callFunction("Memory.alloc", (short)1);
            this.output.pop("pointer", (short)0);
         }
      }

      if (this.compileStatements(true)) {
         this.recoverableError("Program flow may reach end of subroutine without 'return'");
      }

      this.input.advance();
   }

   private short compileParametersList() throws JackException {
      short var1 = 0;
      if (this.isSymbol(')')) {
         return var1;
      } else {
         boolean var2 = true;

         while(var2) {
            ++var1;
            String var3 = this.getType();
            this.input.advance();
            String var4 = null;
            if (this.isIdentifier()) {
               var4 = this.input.getIdentifier();
               this.input.advance();
            } else {
               this.recoverableError("Expected a type followed by a variable name");
            }

            SymbolTable var10003 = this.identifiers;
            this.identifiers.define(var4, var3, 2);
            if (this.isSymbol(')')) {
               var2 = false;
            } else if (this.isSymbol(',')) {
               this.input.advance();
            } else {
               this.terminalError("Expected ) or , in parameters list");
            }
         }

         return var1;
      }
   }

   private void compileFieldAndStaticDeclarations() throws JackException {
      boolean var1 = true;

      while(var1) {
         SymbolTable var10001;
         if (this.isKeywordField()) {
            var10001 = this.identifiers;
            this.compileDeclarationLine(1);
         } else if (this.isKeywordStatic()) {
            var10001 = this.identifiers;
            this.compileDeclarationLine(0);
         } else {
            var1 = false;
         }
      }

   }

   private void compileLocalsDeclarations() throws JackException {
      while(this.isKeywordLocal()) {
         SymbolTable var10001 = this.identifiers;
         this.compileDeclarationLine(3);
      }

   }

   private void compileDeclarationLine(int var1) throws JackException {
      this.input.advance();
      String var2 = this.getType();
      boolean var3 = true;

      while(var3) {
         this.input.advance();
         if (this.isIdentifier()) {
            this.identifiers.define(this.input.getIdentifier(), var2, var1);
            this.input.advance();
         } else {
            this.recoverableError("Expected a type followed by comma-seperated variable names");
         }

         if (this.isSymbol(';')) {
            this.input.advance();
            var3 = false;
         } else if (!this.isSymbol(',')) {
            this.terminalError("Expected , or ;");
         }
      }

   }

   private void skipToEndOfStatement() {
      while(!this.isSymbol(';') && !this.isSymbol('}') && this.input.hasMoreTokens()) {
         this.input.advance();
      }

      if (this.isSymbol(';') && this.input.hasMoreTokens()) {
         this.input.advance();
      }

   }

   private boolean compileStatements(boolean var1) throws JackException {
      boolean var2 = !var1;

      while(!this.isSymbol('}')) {
         if (!var1 && !var2) {
            this.warning("Unreachable code");
            var2 = true;
         }

         if (this.isKeywordDo()) {
            try {
               this.compileDo();
            } catch (JackException var4) {
               this.skipToEndOfStatement();
            }
         } else if (this.isKeywordLet()) {
            try {
               this.compileLet();
            } catch (JackException var6) {
               this.skipToEndOfStatement();
            }
         } else if (this.isKeywordWhile()) {
            var1 = this.compileWhile(var1);
         } else if (this.isKeywordReturn()) {
            try {
               this.compileReturn();
            } catch (JackException var5) {
               this.skipToEndOfStatement();
            }

            var1 = false;
         } else if (this.isKeywordIf()) {
            var1 = this.compileIf(var1);
         } else if (this.isSymbol(';')) {
            this.recoverableError("An empty statement is not allowed");
            this.input.advance();
         } else {
            String var3 = "Expected statement(do, let, while, return or if)";
            if (this.isIdentifier()) {
               this.recoverableError(var3);
               this.skipToEndOfStatement();
            } else {
               this.terminalError(var3);
            }
         }
      }

      return var1;
   }

   private void compileDo() throws JackException {
      this.input.advance();
      this.compileSubroutineCall();
      this.output.pop("temp", (short)0);
      if (this.isSymbol(';')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected ;");
      }

   }

   private void compileSubroutineCall() throws JackException {
      if (!this.isIdentifier()) {
         this.terminalError("Expected class name, subroutine name, field, parameter or local or static variable name");
      }

      String var1 = this.input.getIdentifier();
      int var2 = this.input.getLineNumber();
      this.input.advance();
      if (this.isSymbol('.')) {
         this.compileExternalSubroutineCall(var1);
      } else {
         this.compileInternalSubroutineCall(var1, var2);
      }

   }

   private void compileExternalSubroutineCall(String var1) throws JackException {
      String var2 = null;
      String var3 = null;
      boolean var4 = true;
      this.input.advance();
      int var5 = this.input.getLineNumber();

      try {
         var2 = this.identifiers.getTypeOf(var1);
         int var6 = this.identifiers.getKindOf(var1);
         short var7 = this.identifiers.getIndexOf(var1);
         this.pushVariable(var6, var7);
      } catch (JackException var8) {
         var2 = var1;
         var4 = false;
      }

      if (this.isIdentifier()) {
         var3 = var2 + "." + this.input.getIdentifier();
         this.input.advance();
      } else {
         this.terminalError("Expected subroutine name");
      }

      short var9 = this.compileExpressionList();
      this.output.callFunction(var3, (short)(var9 + (var4 ? 1 : 0)));
      Object[] var10 = new Object[]{var3, new Boolean(var4), new Short(var9), this.fileName, this.identifiers.getSubroutineName(), new Integer(var5)};
      this.subroutineCalls.addElement(var10);
   }

   private void compileInternalSubroutineCall(String var1, int var2) throws JackException {
      String var3 = null;
      var3 = this.identifiers.getClassName() + "." + var1;
      this.output.push("pointer", (short)0);
      short var4 = this.compileExpressionList();
      this.output.callFunction(var3, (short)(var4 + 1));
      if (this.subroutineType == 2) {
         this.recoverableError("Subroutine " + var3 + " called as a method from within a function", var2);
      } else {
         Object[] var5 = new Object[]{var3, Boolean.TRUE, new Short(var4), this.fileName, this.identifiers.getSubroutineName(), new Integer(var2)};
         this.subroutineCalls.addElement(var5);
      }

   }

   private void skipFromParensToBlockStart() {
      while(!this.isSymbol(';') && !this.isSymbol('}') && !this.isSymbol('{') && this.input.hasMoreTokens()) {
         this.input.advance();
      }

   }

   private boolean compileWhile(boolean var1) throws JackException {
      int var2 = this.whileCounter++;
      this.input.advance();
      if (this.isSymbol('(')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected (");
      }

      try {
         this.output.label("WHILE_EXP" + var2);
         this.compileNewExpression(1);
         this.output.not();
         if (this.isSymbol(')')) {
            this.input.advance();
         } else {
            this.recoverableError("Expected )");
         }
      } catch (JackException var4) {
         this.skipFromParensToBlockStart();
         if (!this.isSymbol('{')) {
            throw var4;
         }
      }

      if (this.isSymbol('{')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected {");
      }

      this.output.ifGoTo("WHILE_END" + var2);
      var1 = this.compileStatements(var1);
      if (this.isSymbol('}')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected }");
      }

      this.output.goTo("WHILE_EXP" + var2);
      this.output.label("WHILE_END" + var2);
      return var1;
   }

   private boolean compileIf(boolean var1) throws JackException {
      int var2 = this.ifCounter++;
      this.input.advance();
      if (this.isSymbol('(')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected (");
      }

      try {
         this.compileNewExpression(1);
         if (this.isSymbol(')')) {
            this.input.advance();
         } else {
            this.recoverableError("Expected )");
         }
      } catch (JackException var5) {
         this.skipFromParensToBlockStart();
         if (!this.isSymbol('{')) {
            throw var5;
         }
      }

      if (this.isSymbol('{')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected {");
      }

      this.output.ifGoTo("IF_TRUE" + var2);
      this.output.goTo("IF_FALSE" + var2);
      this.output.label("IF_TRUE" + var2);
      boolean var3 = this.compileStatements(var1);
      if (this.isSymbol('}')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected }");
      }

      if (this.isKeywordElse()) {
         this.input.advance();
         if (this.isSymbol('{')) {
            this.input.advance();
         } else {
            this.recoverableError("Expected {");
         }

         this.output.goTo("IF_END" + var2);
         this.output.label("IF_FALSE" + var2);
         boolean var4 = this.compileStatements(var1);
         if (this.isSymbol('}')) {
            this.input.advance();
         } else {
            this.recoverableError("Expected }");
         }

         this.output.label("IF_END" + var2);
         return var3 || var4;
      } else {
         this.output.label("IF_FALSE" + var2);
         return true;
      }
   }

   private void compileReturn() throws JackException {
      this.input.advance();
      if (this.subroutineType == 3 && !this.isKeywordThis()) {
         this.recoverableError("A constructor must return 'this'");
      }

      if (this.isSymbol(';')) {
         if (!this.identifiers.getReturnType().equals("void")) {
            this.recoverableError("A non-void function must return a value");
         }

         this.output.push("constant", (short)0);
         this.output.returnFromFunction();
         this.input.advance();
      } else {
         if (this.identifiers.getReturnType().equals("void")) {
            this.recoverableError("A void function must not return a value");
         }

         this.compileNewExpression(1);
         this.output.returnFromFunction();
         if (this.isSymbol(';')) {
            this.input.advance();
         } else {
            this.recoverableError("Expected ;");
         }
      }

   }

   private void compileLet() throws JackException {
      this.input.advance();
      if (!this.isIdentifier()) {
         this.terminalError("Expected field, parameter or local or static variable name");
      }

      String var1 = this.input.getIdentifier();

      int var2;
      short var3;
      String var4;
      try {
         var2 = this.identifiers.getKindOf(var1);
         var3 = this.identifiers.getIndexOf(var1);
         var4 = this.identifiers.getTypeOf(var1);
      } catch (JackException var6) {
         this.recoverableError(var1 + " is not defined as a field, parameter or local or static variable");
         SymbolTable var10000 = this.identifiers;
         var2 = 0;
         var3 = 0;
         var4 = "int";
      }

      this.input.advance();
      if (this.isSymbol('=')) {
         this.input.advance();
         int var5 = this.compileNewExpression(1);
         this.popVariable(var2, var3);
         if (var5 != 1) {
            if (var4.equals("int") && var5 != 3 && var5 != 2) {
               this.recoverableError("an int value is expected", this.input.getLineNumber() - 1);
            } else if (var4.equals("char") && var5 != 4 && var5 != 2) {
               this.recoverableError("a char value is expected", this.input.getLineNumber() - 1);
            } else if (var4.equals("boolean") && var5 != 2 && var5 != 3 && var5 != 5) {
               this.recoverableError("a boolean value is expected", this.input.getLineNumber() - 1);
            }
         }
      } else if (this.isSymbol('[')) {
         this.input.advance();
         this.compileNewExpression(2);
         if (this.isSymbol(']')) {
            this.input.advance();
         } else {
            this.terminalError("Expected ]");
         }

         this.pushVariable(var2, var3);
         this.output.add();
         if (this.isSymbol('=')) {
            this.input.advance();
         } else {
            this.terminalError("Expected =");
         }

         this.compileNewExpression(1);
         this.output.pop("temp", (short)0);
         this.output.pop("pointer", (short)1);
         this.output.push("temp", (short)0);
         this.output.pop("that", (short)0);
      } else {
         this.terminalError("Expected [ or =");
      }

      if (this.isSymbol(';')) {
         this.input.advance();
      } else {
         this.recoverableError("Expected ;");
      }

   }

   private short compileExpressionList() throws JackException {
      if (this.isSymbol('(')) {
         this.input.advance();
      } else {
         this.terminalError("Expected (");
      }

      short var1 = 0;
      if (this.isSymbol(')')) {
         this.input.advance();
      } else {
         boolean var2 = true;

         while(var2) {
            this.compileNewExpression(1);
            ++var1;
            if (this.isSymbol(',')) {
               this.input.advance();
            } else if (this.isSymbol(')')) {
               var2 = false;
               this.input.advance();
            } else {
               this.terminalError("Expected , or ) in expression list");
            }
         }
      }

      return var1;
   }

   private int compileNewExpression(int var1) throws JackException {
      ++this.expIndex;
      this.setExpType(var1);
      this.compileExpression();
      --this.expIndex;
      return this.expTypes[this.expIndex + 1];
   }

   private void compileExpression() throws JackException {
      boolean var1 = false;
      this.compileTerm();

      do {
         int var10000 = this.input.getTokenType();
         JackTokenizer var10001 = this.input;
         if (var10000 == 2) {
            char var2 = this.input.getSymbol();
            var1 = var2 == '+' || var2 == '-' || var2 == '*' || var2 == '/' || var2 == '&' || var2 == '|' || var2 == '>' || var2 == '<' || var2 == '=';
            if (var1) {
               this.input.advance();
               this.compileTerm();
               switch(var2) {
               case '&':
                  this.output.and();
                  break;
               case '*':
                  this.output.callFunction("Math.multiply", (short)2);
                  break;
               case '+':
                  this.output.add();
                  break;
               case '-':
                  this.output.substract();
                  break;
               case '/':
                  this.output.callFunction("Math.divide", (short)2);
                  break;
               case '<':
                  this.output.lessThan();
                  break;
               case '=':
                  this.output.equal();
                  break;
               case '>':
                  this.output.greaterThan();
                  break;
               case '|':
                  this.output.or();
               }
            }
         }
      } while(var1);

   }

   private void compileTerm() throws JackException {
      switch(this.input.getTokenType()) {
      case 1:
         this.compileKeywordConst();
         break;
      case 2:
      default:
         if (this.isSymbol('-')) {
            this.input.advance();
            this.compileTerm();
            this.output.negate();
         } else if (this.isSymbol('~')) {
            this.input.advance();
            this.compileTerm();
            this.output.not();
         } else if (this.isSymbol('(')) {
            this.input.advance();
            this.compileNewExpression(1);
            if (this.isSymbol(')')) {
               this.input.advance();
            } else {
               this.terminalError("Expected )");
            }
         } else {
            this.terminalError("Expected - or ~ or ( in term");
         }
         break;
      case 3:
         this.compileIdentifierTerm();
         break;
      case 4:
         this.compileIntConst();
         break;
      case 5:
         this.compileStringConst();
      }

   }

   private void compileIntConst() throws JackException {
      if (this.input.getIntValue() > 32767) {
         this.recoverableError("Integer constant too big");
      }

      short var1 = (short)this.input.getIntValue();
      this.output.push("constant", var1);
      if (this.getExpType() < 2) {
         this.setExpType(2);
      } else if (this.getExpType() > 4) {
         this.recoverableError("a numeric value is illegal here");
      }

      this.input.advance();
   }

   private void compileStringConst() throws JackException {
      if (this.getExpType() == 1) {
         this.setExpType(6);
      } else {
         this.recoverableError("A string constant is illegal here");
      }

      String var1 = this.input.getStringValue();
      short var2 = (short)var1.length();
      this.output.push("constant", var2);
      this.output.callFunction("String.new", (short)1);

      for(short var3 = 0; var3 < var2; ++var3) {
         this.output.push("constant", (short)var1.charAt(var3));
         this.output.callFunction("String.appendChar", (short)2);
      }

      this.input.advance();
   }

   private void compileKeywordConst() throws JackException {
      int var1 = this.input.getKeywordType();
      switch(var1) {
      case 18:
         this.output.push("constant", (short)0);
         this.output.not();
         break;
      case 19:
      case 20:
         this.output.push("constant", (short)0);
         break;
      case 21:
         SymbolTable var10001 = this.identifiers;
         if (this.subroutineType == 2) {
            this.recoverableError("'this' can't be referenced in a function");
         }

         this.output.push("pointer", (short)0);
         break;
      default:
         this.terminalError("Illegal keyword in term");
      }

      switch(var1) {
      case 18:
      case 19:
         if (this.getExpType() <= 2) {
            this.setExpType(5);
         } else {
            this.recoverableError("A boolean value is illegal here");
         }
         break;
      case 20:
         if (this.getExpType() == 1) {
            this.setExpType(8);
         } else {
            this.recoverableError("'null' is illegal here");
         }
         break;
      case 21:
         if (this.getExpType() == 1) {
            this.setExpType(7);
         } else {
            this.recoverableError("'this' is illegal here");
         }
      }

      this.input.advance();
   }

   private void compileIdentifierTerm() throws JackException {
      if (this.getExpType() == 6) {
         this.recoverableError("Illegal casting into String constant");
      }

      String var1 = this.input.getIdentifier();
      int var2 = this.input.getLineNumber();
      this.input.advance();
      if (this.isSymbol('[')) {
         this.input.advance();
         this.compileNewExpression(2);

         try {
            this.pushVariable(this.identifiers.getKindOf(var1), this.identifiers.getIndexOf(var1));
         } catch (JackException var6) {
            this.recoverableError(var1 + " is not defined as a field, parameter or local or static variable", var2);
         }

         this.output.add();
         this.output.pop("pointer", (short)1);
         this.output.push("that", (short)0);
         if (this.isSymbol(']')) {
            this.input.advance();
         } else {
            this.terminalError("Expected ]");
         }
      } else if (this.isSymbol('(')) {
         this.compileInternalSubroutineCall(var1, var2);
      } else if (this.isSymbol('.')) {
         this.compileExternalSubroutineCall(var1);
      } else {
         try {
            int var3 = this.identifiers.getKindOf(var1);
            short var4 = this.identifiers.getIndexOf(var1);
            if (this.subroutineType == 2 && var3 == 1) {
               this.recoverableError("A field may not be referenced in a function", var2);
            }

            this.pushVariable(var3, var4);
            String var5 = this.identifiers.getTypeOf(var1);
            if (var5.equals("int")) {
               if (this.getExpType() <= 2) {
                  this.setExpType(3);
               } else if (this.getExpType() > 3) {
                  this.recoverableError("An int value is illegal here", var2);
               }
            } else if (var5.equals("char")) {
               if (this.getExpType() <= 2) {
                  this.setExpType(4);
               } else if (this.getExpType() > 4 || this.getExpType() == 3) {
                  this.recoverableError("A char value is illegal here", var2);
               }
            } else if (var5.equals("boolean")) {
               if (this.getExpType() <= 2) {
                  this.setExpType(5);
               } else if (this.getExpType() != 5) {
                  this.recoverableError("A boolean value is illegal here", var2);
               }
            }
         } catch (JackException var7) {
            this.recoverableError(var1 + " is not defined as a field, parameter or local or static variable", var2);
         }
      }

   }

   private int getExpType() {
      return this.expTypes[this.expIndex];
   }

   private int setExpType(int var1) {
      return this.expTypes[this.expIndex] = var1;
   }

   private String getType() throws JackException {
      String var1 = null;
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      if (var10000 == 1) {
         switch(this.input.getKeywordType()) {
         case 5:
            var1 = "int";
            break;
         case 6:
            var1 = "boolean";
            break;
         case 7:
            var1 = "char";
            break;
         default:
            this.terminalError("Expected primitive type or class name");
         }
      } else if (this.isIdentifier()) {
         var1 = this.input.getIdentifier();
      } else {
         this.terminalError("Expected primitive type or class name");
      }

      return var1;
   }

   private void pushVariable(int var1, short var2) throws JackException {
      switch(var1) {
      case 0:
         this.output.push("static", var2);
         break;
      case 1:
         this.output.push("this", var2);
         break;
      case 2:
         this.output.push("argument", var2);
         break;
      case 3:
         this.output.push("local", var2);
         break;
      default:
         this.terminalError("Internal Error: Illegal kind");
      }

   }

   private void popVariable(int var1, short var2) throws JackException {
      switch(var1) {
      case 0:
         this.output.pop("static", var2);
         break;
      case 1:
         this.output.pop("this", var2);
         break;
      case 2:
         this.output.pop("argument", var2);
         break;
      case 3:
         this.output.pop("local", var2);
      }

   }

   private boolean isKeywordClass() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 1) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordStatic() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 10) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordField() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 11) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordLocal() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 9) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isIdentifier() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      return var10000 == 3;
   }

   private boolean isSymbol(char var1) {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      return var10000 == 2 && this.input.getSymbol() == var1;
   }

   private boolean isKeywordMethod() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 2) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordFunction() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 3) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordConstructor() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 4) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordVoid() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 8) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordDo() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 13) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordLet() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 12) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordWhile() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 16) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordReturn() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 17) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordIf() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 14) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordElse() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 15) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private boolean isKeywordThis() {
      int var10000 = this.input.getTokenType();
      JackTokenizer var10001 = this.input;
      boolean var1;
      if (var10000 == 1) {
         var10000 = this.input.getKeywordType();
         var10001 = this.input;
         if (var10000 == 21) {
            var1 = true;
            return var1;
         }
      }

      var1 = false;
      return var1;
   }

   private void terminalError(String var1) throws JackException {
      this.terminalError(var1, -1, (String)null, (String)null);
   }

   private void terminalError(String var1, int var2) throws JackException {
      this.terminalError(var1, var2, (String)null, (String)null);
   }

   private void terminalError(String var1, int var2, String var3, String var4) throws JackException {
      this.recoverableError(var1, var2, var3, var4);
      throw new JackException(this.generateMessage(var1, var2, var3, var4));
   }

   private void warning(String var1) {
      this.warning(var1, -1, (String)null, (String)null);
   }

   private void warning(String var1, int var2) {
      this.warning(var1, var2, (String)null, (String)null);
   }

   private void warning(String var1, int var2, String var3, String var4) {
      System.err.println(this.generateMessage("Warning: " + var1, var2, var3, var4));
   }

   private void recoverableError(String var1) {
      this.recoverableError(var1, -1, (String)null, (String)null);
   }

   private void recoverableError(String var1, int var2) {
      this.recoverableError(var1, var2, (String)null, (String)null);
   }

   private void recoverableError(String var1, int var2, String var3, String var4) {
      System.err.println(this.generateMessage(var1, var2, var3, var4));
      this.validJack = false;
   }

   private String generateMessage(String var1, int var2, String var3, String var4) {
      if (var4 == null) {
         var4 = this.fileName;
      }

      if (var3 == null) {
         var3 = this.identifiers.getSubroutineName();
      }

      if (var2 == -1) {
         var2 = this.input.getLineNumber();
      }

      return "In " + var4 + " (line " + var2 + "): " + ("".equals(var3) ? "" : "In subroutine" + (var3 == null ? "" : " " + var3) + ": ") + var1;
   }
}
