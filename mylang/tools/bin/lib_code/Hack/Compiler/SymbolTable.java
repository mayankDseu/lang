package Hack.Compiler;

import java.util.HashMap;

public class SymbolTable {
   public static final int KIND_STATIC = 0;
   public static final int KIND_FIELD = 1;
   public static final int KIND_PARAMETER = 2;
   public static final int KIND_LOCAL = 3;
   public static final int SUBROUTINE_TYPE_UNDEFINED = 0;
   public static final int SUBROUTINE_TYPE_METHOD = 1;
   public static final int SUBROUTINE_TYPE_FUNCTION = 2;
   public static final int SUBROUTINE_TYPE_CONSTRUCTOR = 3;
   private static short staticsNumbering;
   private static short fieldsNumbering;
   private static short parametersNumbering;
   private static short localsNumbering;
   private String className;
   private String subroutineName;
   private int subroutineType;
   private String returnType;
   private HashMap fields;
   private HashMap parameters;
   private HashMap locals;
   private HashMap statics;

   public SymbolTable(String var1) {
      this.className = var1;
      this.fields = new HashMap();
      this.statics = new HashMap();
      this.parameters = new HashMap();
      this.locals = new HashMap();
      fieldsNumbering = 0;
      staticsNumbering = 0;
      localsNumbering = 0;
      parametersNumbering = 1;
      this.subroutineType = 0;
   }

   public void startMethod(String var1, String var2) {
      this.startSubroutine(var1, 1, var2, (short)1);
   }

   public void startFunction(String var1, String var2) {
      this.startSubroutine(var1, 2, var2, (short)0);
   }

   public void startConstructor(String var1) {
      this.startSubroutine(var1, 3, this.className, (short)0);
   }

   public void endSubroutine() {
      this.parameters.clear();
      this.locals.clear();
      localsNumbering = 0;
      parametersNumbering = 1;
      this.subroutineType = 0;
      this.subroutineName = null;
      this.returnType = null;
   }

   private void startSubroutine(String var1, int var2, String var3, short var4) {
      this.endSubroutine();
      this.subroutineName = var1;
      this.subroutineType = var2;
      this.returnType = var3;
      parametersNumbering = var4;
   }

   public void define(String var1, String var2, int var3) {
      switch(var3) {
      case 0:
         this.statics.put(var1, new IdentifierProperties(var2, staticsNumbering));
         ++staticsNumbering;
         break;
      case 1:
         this.fields.put(var1, new IdentifierProperties(var2, fieldsNumbering));
         ++fieldsNumbering;
         break;
      case 2:
         this.parameters.put(var1, new IdentifierProperties(var2, parametersNumbering));
         ++parametersNumbering;
         break;
      case 3:
         this.locals.put(var1, new IdentifierProperties(var2, localsNumbering));
         ++localsNumbering;
      }

   }

   public int getKindOf(String var1) throws JackException {
      byte var2;
      if (this.parameters.containsKey(var1)) {
         var2 = 2;
      } else if (this.locals.containsKey(var1)) {
         var2 = 3;
      } else if (this.subroutineType != 2 && this.fields.containsKey(var1)) {
         var2 = 1;
      } else {
         if (!this.statics.containsKey(var1)) {
            throw new JackException(var1);
         }

         var2 = 0;
      }

      return var2;
   }

   public String getTypeOf(String var1) throws JackException {
      return this.getIdentifierProperties(var1).getType();
   }

   public short getIndexOf(String var1) throws JackException {
      return this.getIdentifierProperties(var1).getIndex();
   }

   public short getNumberOfIdentifiers(int var1) {
      short var2 = -1;
      switch(var1) {
      case 0:
         var2 = staticsNumbering;
         break;
      case 1:
         var2 = fieldsNumbering;
         break;
      case 2:
         var2 = (short)(parametersNumbering - 1);
         break;
      case 3:
         var2 = localsNumbering;
      }

      return var2;
   }

   public String getClassName() {
      return this.className;
   }

   public String getSubroutineName() {
      return this.subroutineName;
   }

   public String getReturnType() {
      return this.returnType;
   }

   private IdentifierProperties getIdentifierProperties(String var1) throws JackException {
      IdentifierProperties var2 = null;
      switch(this.getKindOf(var1)) {
      case 0:
         var2 = (IdentifierProperties)this.statics.get(var1);
         break;
      case 1:
         var2 = (IdentifierProperties)this.fields.get(var1);
         break;
      case 2:
         var2 = (IdentifierProperties)this.parameters.get(var1);
         break;
      case 3:
         var2 = (IdentifierProperties)this.locals.get(var1);
      }

      return var2;
   }
}
