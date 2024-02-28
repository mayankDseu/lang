package Hack.HardwareSimulator;

import Hack.Gates.HDLException;
import Hack.Gates.HDLTokenizer;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class HDLLineTokenizer extends HDLTokenizer {
   public HDLLineTokenizer(String var1) throws HDLException {
      BufferedReader var2 = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(var1.getBytes())));

      try {
         this.initizalizeInput(var2);
      } catch (IOException var4) {
         throw new HDLException("Error while initializing HDL for reading");
      }
   }

   public void HDLError(String var1) throws HDLException {
      throw new HDLException(var1);
   }
}
