package Hack.Translators;

public class HackTranslatorException extends Exception {
   public HackTranslatorException(String var1) {
      super(var1);
   }

   public HackTranslatorException(String var1, int var2) {
      super("In line " + var2 + ", " + var1);
   }
}
