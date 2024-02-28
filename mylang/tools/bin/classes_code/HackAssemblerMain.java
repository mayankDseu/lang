import AssemblerGUI.AssemblerComponent;
import Hack.Assembler.HackAssembler;
import Hack.Translators.HackTranslatorException;
import javax.swing.UIManager;

public class HackAssemblerMain {
   public static void main(String[] var0) {
      if (var0.length > 1) {
         System.err.println("Usage: java HackAssembler [.asm name]");
         System.exit(-1);
      }

      if (var0.length == 1) {
         try {
            new HackAssembler(var0[0], 32768, (short)0, true);
         } catch (HackTranslatorException var5) {
            System.err.println(var5.getMessage());
         }
      } else {
         try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
         } catch (Exception var4) {
         }

         try {
            AssemblerComponent var1 = new AssemblerComponent();
            var1.setAboutFileName("bin/help/asmAbout.html");
            var1.setUsageFileName("bin/help/asmUsage.html");
            new HackAssembler(var1, 32768, (short)0, (String)null);
         } catch (HackTranslatorException var3) {
            System.err.println(var3.getMessage());
            System.exit(-1);
         }
      }

   }
}
