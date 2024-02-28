package Hack.Utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

public class Graph {
   private HashMap graph = new HashMap();
   private boolean hasCircle;

   public void addEdge(Object var1, Object var2) {
      this.checkExistence(var1);
      this.checkExistence(var2);
      Set var3 = (Set)this.graph.get(var1);
      var3.add(var2);
   }

   private void checkExistence(Object var1) {
      if (!this.graph.keySet().contains(var1)) {
         HashSet var2 = new HashSet();
         this.graph.put(var1, var2);
      }

   }

   public boolean isEmpty() {
      return this.graph.keySet().isEmpty();
   }

   public boolean pathExists(Object var1, Object var2) {
      HashSet var3 = new HashSet();
      return this.doPathExists(var1, var2, var3);
   }

   private boolean doPathExists(Object var1, Object var2, Set var3) {
      boolean var4 = false;
      var3.add(var1);
      Set var5 = (Set)this.graph.get(var1);
      if (var5 != null) {
         Iterator var6 = var5.iterator();

         while(var6.hasNext() && !var4) {
            Object var7 = var6.next();
            var4 = var7.equals(var2);
            if (!var4 && !var3.contains(var7)) {
               var4 = this.doPathExists(var7, var2, var3);
            }
         }
      }

      return var4;
   }

   public Object[] topologicalSort(Object var1) {
      this.hasCircle = false;
      HashSet var2 = new HashSet();
      HashSet var3 = new HashSet();
      Vector var4 = new Vector();
      this.doTopologicalSort(var1, var4, var2, var3);
      Object[] var5 = new Object[var4.size()];

      for(int var6 = 0; var6 < var5.length; ++var6) {
         var5[var6] = var4.elementAt(var5.length - var6 - 1);
      }

      return var5;
   }

   private void doTopologicalSort(Object var1, Vector var2, Set var3, Set var4) {
      var3.add(var1);
      var4.add(var1);
      Set var5 = (Set)this.graph.get(var1);
      if (var5 != null) {
         Iterator var6 = var5.iterator();

         while(var6.hasNext()) {
            Object var7 = var6.next();
            if (var4.contains(var7)) {
               this.hasCircle = true;
            }

            if (!var3.contains(var7)) {
               this.doTopologicalSort(var7, var2, var3, var4);
            }
         }
      }

      var4.remove(var1);
      var2.addElement(var1);
   }

   public boolean hasCircle() {
      return this.hasCircle;
   }
}
