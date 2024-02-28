package Hack.VirtualMachine;

public class HVMInstruction {
   private byte opCode;
   private short arg0;
   private short arg1;
   private String stringArg;
   private short numberOfArgs;

   public HVMInstruction(byte var1, short var2, short var3) {
      this.opCode = var1;
      this.arg0 = var2;
      this.arg1 = var3;
      this.numberOfArgs = 2;
   }

   public HVMInstruction(byte var1, short var2) {
      this.opCode = var1;
      this.arg0 = var2;
      this.numberOfArgs = 1;
   }

   public HVMInstruction(byte var1) {
      this.opCode = var1;
      this.numberOfArgs = 0;
   }

   public short getOpCode() {
      return (short)this.opCode;
   }

   public short getArg0() {
      return this.arg0;
   }

   public short getArg1() {
      return this.arg1;
   }

   public void setStringArg(String var1) {
      this.stringArg = var1;
   }

   public String getStringArg() {
      return this.stringArg;
   }

   public short getNumberOfArgs() {
      return this.numberOfArgs;
   }

   public String[] getFormattedStrings() {
      String[] var1 = new String[3];
      HVMInstructionSet var2 = HVMInstructionSet.getInstance();
      var1[1] = "";
      var1[2] = "";
      var1[0] = var2.instructionCodeToString(this.opCode);
      if (var1[0] == null) {
         var1[0] = "";
      }

      switch(this.opCode) {
      case 10:
         var1[1] = var2.segmentCodeToVMString((byte)this.arg0);
         var1[2] = String.valueOf(this.arg1);
         break;
      case 11:
         if (this.numberOfArgs == 2) {
            var1[1] = var2.segmentCodeToVMString((byte)this.arg0);
            var1[2] = String.valueOf(this.arg1);
         }
         break;
      case 12:
         var1[1] = this.stringArg;
         break;
      case 13:
         var1[1] = this.stringArg;
         break;
      case 14:
         var1[1] = this.stringArg;
         break;
      case 15:
         var1[1] = this.stringArg;
         var1[2] = String.valueOf(this.arg0);
      case 16:
      default:
         break;
      case 17:
         var1[1] = this.stringArg;
         var1[2] = String.valueOf(this.arg1);
      }

      return var1;
   }

   public String toString() {
      String[] var1 = this.getFormattedStrings();
      StringBuffer var2 = new StringBuffer();
      if (!var1[0].equals("")) {
         var2.append(var1[0]);
         if (!var1[1].equals("")) {
            var2.append(" ");
            var2.append(var1[1]);
            if (!var1[2].equals("")) {
               var2.append(" ");
               var2.append(var1[2]);
            }
         }
      }

      return var2.toString();
   }
}
