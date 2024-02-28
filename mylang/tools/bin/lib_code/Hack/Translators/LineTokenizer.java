package Hack.Translators;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

public class LineTokenizer extends StreamTokenizer {
   public LineTokenizer(String var1) throws IOException {
      super(new StringReader(var1));
      this.slashSlashComments(true);
   }

   public void advance(boolean var1) throws IOException, HackTranslatorException {
      this.nextToken();
      if (var1 && this.ttype == -1) {
         throw new HackTranslatorException("Unexpected end of line", this.lineno());
      }
   }

   public String token() {
      String var1 = null;
      switch(this.ttype) {
      case -3:
         var1 = this.sval;
         break;
      case -2:
         var1 = String.valueOf((int)this.nval);
         break;
      default:
         var1 = String.valueOf((char)this.ttype);
      }

      return var1;
   }

   public int number() {
      return this.ttype == -2 ? (int)this.nval : 0;
   }

   public char symbol() {
      return this.ttype > 0 ? (char)this.ttype : '\u0000';
   }

   public boolean isToken(String var1) {
      return this.token().equalsIgnoreCase(var1);
   }

   public boolean isWord() {
      return this.ttype == -3;
   }

   public boolean isNumber() {
      return this.ttype == -2;
   }

   public boolean isSymbol() {
      return this.ttype > 0;
   }

   public boolean isEnd() {
      return this.ttype == -1;
   }

   public void ensureEnd() throws HackTranslatorException, IOException {
      this.advance(false);
      if (!this.isEnd()) {
         throw new HackTranslatorException("end of line expected, '" + this.token() + "' is found");
      }
   }

   public boolean contains(String var1) throws IOException {
      boolean var2 = false;

      while(!var2 && !this.isEnd()) {
         if (!(var2 = this.token().equals(var1))) {
            try {
               this.advance(false);
            } catch (HackTranslatorException var4) {
            }
         }
      }

      return var2;
   }
}
