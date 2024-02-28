package Hack.Controller;

public class Breakpoint {
   private String varName;
   private String value;
   private boolean reached;

   public Breakpoint(String var1, String var2) {
      this.varName = var1;
      this.value = var2;
      this.reached = false;
   }

   public String getVarName() {
      return this.varName;
   }

   public String getValue() {
      return this.value;
   }

   public void off() {
      this.reached = false;
   }

   public void on() {
      this.reached = true;
   }

   public boolean isReached() {
      return this.reached;
   }
}
