package Hack.Assembler;

import Hack.Translators.LineTokenizer;
import java.io.IOException;
import java.text.StringCharacterIterator;

public class AssemblyLineTokenizer extends LineTokenizer {
   public AssemblyLineTokenizer(String var1) throws IOException {
      super(removeSpaces(var1));
      this.resetSyntax();
      this.slashSlashComments(true);
      this.whitespaceChars(32, 32);
      this.whitespaceChars(10, 10);
      this.whitespaceChars(13, 13);
      this.whitespaceChars(9, 9);
      this.wordChars(48, 57);
      this.wordChars(65, 90);
      this.wordChars(97, 122);
      this.wordChars(95, 95);
      this.wordChars(43, 43);
      this.wordChars(45, 45);
      this.wordChars(46, 46);
      this.wordChars(58, 58);
      this.wordChars(33, 33);
      this.wordChars(38, 38);
      this.wordChars(124, 124);
      this.wordChars(36, 36);
      this.nextToken();
   }

   private static String removeSpaces(String var0) {
      StringBuffer var1 = new StringBuffer();
      StringCharacterIterator var2 = new StringCharacterIterator(var0);
      var2.first();

      for(; var2.current() != '\uffff'; var2.next()) {
         if (var2.current() != ' ') {
            var1.append(var2.current());
         }
      }

      return var1.toString();
   }
}
