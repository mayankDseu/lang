package Hack.Controller;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Hashtable;

public class ScriptTokenizer {
   public static final int TYPE_KEYWORD = 1;
   public static final int TYPE_SYMBOL = 2;
   public static final int TYPE_IDENTIFIER = 3;
   public static final int TYPE_INT_CONST = 4;
   public static final int KW_OUTPUT_FILE = 1;
   public static final int KW_COMPARE_TO = 2;
   public static final int KW_OUTPUT_LIST = 3;
   public static final int KW_OUTPUT = 4;
   public static final int KW_BREAKPOINT = 5;
   public static final int KW_CLEAR_BREAKPOINTS = 6;
   public static final int KW_REPEAT = 7;
   public static final int KW_WHILE = 8;
   public static final int KW_ECHO = 9;
   public static final int KW_CLEAR_ECHO = 10;
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

   public ScriptTokenizer(Reader var1) throws ControllerException {
      try {
         this.parser = new StreamTokenizer(var1);
         this.parser.parseNumbers();
         this.parser.slashSlashComments(true);
         this.parser.slashStarComments(true);
         this.parser.wordChars(58, 58);
         this.parser.wordChars(37, 37);
         this.parser.wordChars(91, 91);
         this.parser.wordChars(93, 93);
         this.parser.nextToken();
         this.initKeywords();
         this.initSymbols();
      } catch (IOException var3) {
         throw new ControllerException("Error while initializing script for reading");
      }
   }

   public void advance() throws ControllerException {
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
            this.symbol = (char)this.parser.ttype;
            if (this.symbol == '"') {
               this.currentToken = this.parser.sval;
               this.tokenType = 3;
               this.identifier = this.currentToken;
            } else {
               this.tokenType = 2;
               this.currentToken = String.valueOf(this.symbol);
            }
         }

         this.parser.nextToken();
      } catch (IOException var2) {
         throw new ControllerException("Error while reading script");
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

   public int getLineNumber() {
      return this.parser.lineno();
   }

   private void initKeywords() {
      this.keywords = new Hashtable();
      this.keywords.put("output-file", new Integer(1));
      this.keywords.put("compare-to", new Integer(2));
      this.keywords.put("output-list", new Integer(3));
      this.keywords.put("output", new Integer(4));
      this.keywords.put("echo", new Integer(9));
      this.keywords.put("clear-echo", new Integer(10));
      this.keywords.put("breakpoint", new Integer(5));
      this.keywords.put("clear-breakpoints", new Integer(6));
      this.keywords.put("repeat", new Integer(7));
      this.keywords.put("while", new Integer(8));
   }

   private void initSymbols() {
      this.symbols = new Hashtable();
      this.symbols.put("{", "{");
      this.symbols.put("}", "}");
      this.symbols.put(",", ",");
      this.symbols.put(";", ";");
      this.symbols.put("!", "!");
      this.symbols.put("=", "=");
      this.symbols.put(">", ">");
      this.symbols.put("<", "<");
   }
}
