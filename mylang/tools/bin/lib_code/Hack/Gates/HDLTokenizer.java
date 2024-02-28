package Hack.Gates;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Hashtable;

public class HDLTokenizer {
   public static final int TYPE_KEYWORD = 1;
   public static final int TYPE_SYMBOL = 2;
   public static final int TYPE_IDENTIFIER = 3;
   public static final int TYPE_INT_CONST = 4;
   public static final int KW_CHIP = 1;
   public static final int KW_IN = 2;
   public static final int KW_OUT = 3;
   public static final int KW_BUILTIN = 4;
   public static final int KW_CLOCKED = 5;
   public static final int KW_PARTS = 6;
   private StreamTokenizer parser;
   private Hashtable keywords;
   private Hashtable symbols;
   private int tokenType;
   private int keyWordType;
   private char symbol;
   private int intValue;
   private String stringValue;
   private String identifier;
   private String currentToken;
   private String fileName;

   public HDLTokenizer(String var1) throws HDLException {
      this.fileName = var1;

      FileReader var2;
      try {
         var2 = new FileReader(var1);
      } catch (IOException var5) {
         throw new HDLException("Can't find HDL file " + var1);
      }

      try {
         this.initizalizeInput(var2);
      } catch (IOException var4) {
         throw new HDLException("Error while initializing for reading", var1);
      }
   }

   public HDLTokenizer() {
   }

   protected void initizalizeInput(Reader var1) throws IOException {
      this.parser = new StreamTokenizer(var1);
      this.parser.parseNumbers();
      this.parser.slashSlashComments(true);
      this.parser.slashStarComments(true);
      this.parser.wordChars(58, 58);
      this.parser.wordChars(91, 91);
      this.parser.wordChars(93, 93);
      this.parser.nextToken();
      this.initKeywords();
      this.initSymbols();
   }

   public String getFileName() {
      return this.fileName;
   }

   public void advance() throws HDLException {
      if (!this.hasMoreTokens()) {
         this.HDLError("Unexpected end of file");
      }

      try {
         switch(this.parser.ttype) {
         case -3:
            this.currentToken = this.parser.sval;
            Integer var1 = (Integer)this.keywords.get(this.currentToken);
            if (var1 != null) {
               this.tokenType = 1;
               this.keyWordType = var1;
            } else {
               this.tokenType = 3;
               this.identifier = this.currentToken;
            }
            break;
         case -2:
            this.tokenType = 4;
            this.intValue = (int)this.parser.nval;
            this.currentToken = String.valueOf(this.intValue);
            break;
         default:
            this.tokenType = 2;
            this.symbol = (char)this.parser.ttype;
            this.currentToken = String.valueOf(this.symbol);
         }

         this.parser.nextToken();
      } catch (IOException var2) {
         throw new HDLException("Error while reading HDL file");
      }
   }

   public String getToken() {
      return this.currentToken;
   }

   public int getTokenType() {
      return this.tokenType;
   }

   public int getKeywordType() {
      return this.keyWordType;
   }

   public char getSymbol() {
      return this.symbol;
   }

   public int getIntValue() {
      return this.intValue;
   }

   public String getStringValue() {
      return this.stringValue;
   }

   public String getIdentifier() {
      return this.identifier;
   }

   public boolean hasMoreTokens() {
      StreamTokenizer var10001 = this.parser;
      return this.parser.ttype != -1;
   }

   private void initKeywords() {
      this.keywords = new Hashtable();
      this.keywords.put("CHIP", new Integer(1));
      this.keywords.put("IN", new Integer(2));
      this.keywords.put("OUT", new Integer(3));
      this.keywords.put("BUILTIN", new Integer(4));
      this.keywords.put("CLOCKED", new Integer(5));
      this.keywords.put("PARTS:", new Integer(6));
   }

   private void initSymbols() {
      this.symbols = new Hashtable();
      this.symbols.put("{", "{");
      this.symbols.put("}", "}");
      this.symbols.put(",", ",");
      this.symbols.put(";", ";");
      this.symbols.put("(", "(");
      this.symbols.put(")", ")");
   }

   public void HDLError(String var1) throws HDLException {
      throw new HDLException(var1, this.fileName, this.parser.lineno());
   }
}
