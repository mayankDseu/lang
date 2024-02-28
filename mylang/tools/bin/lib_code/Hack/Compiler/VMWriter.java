package Hack.Compiler;

import Hack.VirtualMachine.VirtualMachine;
import java.io.PrintWriter;

public class VMWriter implements VirtualMachine {
   private PrintWriter writer;

   public VMWriter(PrintWriter var1) {
      this.writer = var1;
   }

   public void close() {
      this.writer.flush();
      this.writer.close();
   }

   public void add() {
      this.writer.println("add");
   }

   public void substract() {
      this.writer.println("sub");
   }

   public void negate() {
      this.writer.println("neg");
   }

   public void equal() {
      this.writer.println("eq");
   }

   public void greaterThan() {
      this.writer.println("gt");
   }

   public void lessThan() {
      this.writer.println("lt");
   }

   public void and() {
      this.writer.println("and");
   }

   public void or() {
      this.writer.println("or");
   }

   public void not() {
      this.writer.println("not");
   }

   public void push(String var1, short var2) {
      this.writer.println("push " + var1 + " " + var2);
   }

   public void pop(String var1, short var2) {
      this.writer.println("pop " + var1 + " " + var2);
   }

   public void label(String var1) {
      this.writer.println("label " + var1);
   }

   public void goTo(String var1) {
      this.writer.println("goto " + var1);
   }

   public void ifGoTo(String var1) {
      this.writer.println("if-goto " + var1);
   }

   public void function(String var1, short var2) {
      this.writer.println("function " + var1 + " " + var2);
   }

   public void returnFromFunction() {
      this.writer.println("return");
   }

   public void callFunction(String var1, short var2) {
      this.writer.println("call " + var1 + " " + var2);
   }
}
