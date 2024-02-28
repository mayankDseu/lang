package Hack.Gates;

import java.io.File;
import java.util.Vector;

public class GatesManager {
   private static GatesManager singleton;
   private File workingDir;
   private File builtInDir;
   private GatesPanelGUI gatesPanel;
   private GateErrorEventListener errorHandler;
   private Vector chips = new Vector();
   private boolean updateChipsGUI = true;

   private GatesManager() {
   }

   public static GatesManager getInstance() {
      if (singleton == null) {
         singleton = new GatesManager();
      }

      return singleton;
   }

   public File getWorkingDir() {
      return this.workingDir;
   }

   public void setWorkingDir(File var1) {
      this.workingDir = var1;
   }

   public File getBuiltInDir() {
      return this.builtInDir;
   }

   public void setBuiltInDir(File var1) {
      this.builtInDir = var1;
   }

   public BuiltInGateWithGUI[] getChips() {
      BuiltInGateWithGUI[] var1 = new BuiltInGateWithGUI[this.chips.size()];
      this.chips.toArray(var1);
      return var1;
   }

   public void addChip(BuiltInGateWithGUI var1) {
      this.chips.add(var1);
      var1.addErrorListener(this.errorHandler);
      var1.setParent(var1);
      if (this.gatesPanel != null) {
         this.gatesPanel.addGateComponent(var1.getGUIComponent());
      }

   }

   public void removeChip(BuiltInGateWithGUI var1) {
      this.chips.remove(var1);
      var1.removeErrorListener(this.errorHandler);
      if (this.gatesPanel != null) {
         this.gatesPanel.removeGateComponent(var1.getGUIComponent());
      }

   }

   public void removeAllChips() {
      for(int var1 = 0; var1 < this.chips.size(); ++var1) {
         ((BuiltInGateWithGUI)this.chips.elementAt(var1)).removeErrorListener(this.errorHandler);
      }

      this.chips.removeAllElements();
      if (this.gatesPanel != null) {
         this.gatesPanel.removeAllGateComponents();
      }

   }

   public void setGatesPanel(GatesPanelGUI var1) {
      this.gatesPanel = var1;
   }

   public GateErrorEventListener getErrorHandler() {
      return this.errorHandler;
   }

   public void setErrorHandler(GateErrorEventListener var1) {
      this.errorHandler = var1;
   }

   public String getHDLFileName(String var1) {
      String var2 = null;
      String var3 = var1 + ".hdl";
      File var4 = new File(this.workingDir, var3);
      if (var4.exists()) {
         var2 = var4.getAbsolutePath();
      } else {
         var4 = new File(this.builtInDir, var3);
         if (var4.exists()) {
            var2 = var4.getAbsolutePath();
         }
      }

      return var2;
   }

   public boolean isChipsGUIEnabled() {
      return this.updateChipsGUI;
   }

   public void enableChipsGUI(boolean var1) {
      this.updateChipsGUI = var1;
   }
}
