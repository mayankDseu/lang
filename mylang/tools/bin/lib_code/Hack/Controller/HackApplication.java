package Hack.Controller;

public abstract class HackApplication {
   public HackApplication(HackSimulator var1, ControllerGUI var2, HackSimulatorGUI var3, String var4, String var5, String var6) {
      try {
         var3.setUsageFileName(var5);
         var3.setAboutFileName(var6);
         this.createController(var1, var2, var4);
      } catch (ScriptException var8) {
         System.err.println(var8.getMessage());
         System.exit(-1);
      } catch (ControllerException var9) {
         System.err.println(var9.getMessage());
         System.exit(-1);
      } catch (Exception var10) {
         System.err.println("Unexpected Error: " + var10.getMessage());
         var10.printStackTrace();
         System.exit(-1);
      }

   }

   protected void createController(HackSimulator var1, ControllerGUI var2, String var3) throws ScriptException, ControllerException {
      new HackController(var2, var1, var3);
   }
}
