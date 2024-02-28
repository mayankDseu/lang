package Hack.Compiler;

public class IdentifierProperties {
   private String type;
   private short index;

   public IdentifierProperties(String var1, short var2) {
      this.type = var1;
      this.index = var2;
   }

   public String getType() {
      return this.type;
   }

   public short getIndex() {
      return this.index;
   }
}
