import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextComparer {
   public static void main(String[] var0) {
      if (var0.length != 2) {
         System.err.println("Usage: java TextComparer <file name> <file name>");
         System.exit(-1);
      }

      BufferedReader var1 = null;
      BufferedReader var2 = null;

      try {
         var1 = new BufferedReader(new FileReader(var0[0]));
      } catch (IOException var9) {
         System.err.println("Cannot open " + var0[0]);
         System.exit(-1);
      }

      try {
         var2 = new BufferedReader(new FileReader(var0[1]));
      } catch (IOException var8) {
         System.err.println("Cannot open " + var0[1]);
         System.exit(-1);
      }

      int var5 = 0;

      try {
         String var3;
         for(; (var3 = var1.readLine()) != null; ++var5) {
            var3 = removeSpaces(var3);
            String var4 = var2.readLine();
            if (var4 == null) {
               System.out.println("Second file is shorter (only " + var5 + " lines)");
               System.exit(-1);
            } else {
               var4 = removeSpaces(var4);
               if (!var3.equals(var4)) {
                  System.out.println("Comparison failure in line " + var5 + ":");
                  System.out.println(var3);
                  System.out.println(var4);
                  System.exit(-1);
               }
            }
         }

         if (var2.readLine() != null) {
            System.out.println("First file is shorter (only " + var5 + " lines)");
            System.exit(-1);
         }
      } catch (IOException var10) {
         System.err.println("IO error while reading files");
         System.exit(-1);
      }

      try {
         var1.close();
         var2.close();
      } catch (IOException var7) {
         System.err.println("Could not close files");
         System.exit(-1);
      }

      System.out.println("Comparison ended successfully");
   }

   private static String removeSpaces(String var0) {
      int var3 = 0;
      int var4 = 0;
      StringBuffer var1 = new StringBuffer(var0);

      while(var4 < var1.length()) {
         if (var1.charAt(var4) == ' ') {
            ++var4;
         } else {
            if (var3 != var4) {
               var1.setCharAt(var3, var1.charAt(var4));
            }

            ++var3;
            ++var4;
         }
      }

      var1.setLength(var3);
      return var1.toString().trim();
   }
}
