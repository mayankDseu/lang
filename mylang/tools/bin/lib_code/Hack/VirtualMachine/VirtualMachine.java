package Hack.VirtualMachine;

public interface VirtualMachine {
   void add();

   void substract();

   void negate();

   void equal();

   void greaterThan();

   void lessThan();

   void and();

   void or();

   void not();

   void push(String var1, short var2);

   void pop(String var1, short var2);

   void label(String var1);

   void goTo(String var1);

   void ifGoTo(String var1);

   void function(String var1, short var2);

   void returnFromFunction();

   void callFunction(String var1, short var2);
}
