package Hack.Compiler;

import Hack.Utilities.HackFileFilter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class JackCompiler {
   private CompilationEngine compilationEngine = new CompilationEngine();

   public boolean compileFile(File var1) {
      String var2 = var1.getName().substring(0, var1.getName().indexOf(46));
      String var3 = var1.getParent();

      try {
         JackTokenizer var4 = new JackTokenizer(new FileReader(var1.getPath()));
         File var5 = new File(var3 + "/" + var2 + ".vm");
         VMWriter var6 = new VMWriter(new PrintWriter(new FileWriter(var5)));
         if (this.compilationEngine.compileClass(var4, var6, var2, var1.getName())) {
            return true;
         } else {
            var5.delete();
            return false;
         }
      } catch (IOException var7) {
         System.err.println("Error reading/writing while compiling " + var1);
         System.exit(-1);
         return false;
      }
   }

   public boolean compileDirectory(String var1) {
      boolean var2 = true;
      File var3 = new File(var1);
      File[] var4 = var3.listFiles(new HackFileFilter(".jack"));

      for(int var5 = 0; var5 < var4.length; ++var5) {
         var2 &= this.compileFile(var4[var5]);
      }

      return var2;
   }

   public boolean verify() {
      return this.compilationEngine.verifySubroutineCalls();
   }

   public static void main(String[] var0) {
      if (var0.length != 1) {
         try {
            BufferedReader var1 = new BufferedReader(new FileReader(new File("bin/help/compiler.txt")));

            String var2;
            while((var2 = var1.readLine()) != null) {
               System.out.println(var2);
            }

            System.out.println("");
         } catch (FileNotFoundException var4) {
         } catch (IOException var5) {
         }

         System.out.println("Usage: java JackCompiler <Jack-dir or Jack-file-name>");
         System.exit(-1);
      }

      JackCompiler var6 = new JackCompiler();
      File var7 = new File(var0[0]);
      if (!var7.exists()) {
         System.err.println("Could not find file or directory: " + var0[0]);
         System.exit(-1);
      }

      boolean var3;
      if (var7.isDirectory()) {
         var3 = var6.compileDirectory(var0[0]);
      } else {
         var3 = var6.compileFile(var7);
      }

      var3 &= var6.verify();
      System.exit(var3 ? 0 : 1);
   }
}
