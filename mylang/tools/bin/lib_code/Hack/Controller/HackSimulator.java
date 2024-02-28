package Hack.Controller;

import Hack.ComputerParts.ComputerPartErrorEventListener;
import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import java.io.File;
import java.util.Vector;

public abstract class HackSimulator implements ProgramEventListener, ComputerPartErrorEventListener {
   private Vector listeners = new Vector();
   private Vector programListeners = new Vector();
   protected File workingDir;

   public abstract String getName();

   public abstract String getValue(String var1) throws VariableException;

   public abstract void setValue(String var1, String var2) throws VariableException;

   public abstract void doCommand(String[] var1) throws CommandException, ProgramException, VariableException;

   public abstract void restart();

   public abstract void setAnimationMode(int var1);

   public abstract void setNumericFormat(int var1);

   public abstract void setAnimationSpeed(int var1);

   public abstract void refresh();

   public abstract void prepareFastForward();

   public abstract void prepareGUI();

   public abstract String[] getVariables();

   public int getInitialAnimationMode() {
      return 0;
   }

   public int getInitialNumericFormat() {
      return 0;
   }

   public int getInitialAdditionalDisplay() {
      return 3;
   }

   protected abstract HackSimulatorGUI getGUI();

   protected void loadProgram() {
      this.getGUI().loadProgram();
   }

   public void setWorkingDir(File var1) {
      File var2 = var1.getParentFile();
      this.workingDir = var1.isDirectory() ? var1 : var2;
      HackSimulatorGUI var3 = this.getGUI();
      if (var3 != null) {
         this.getGUI().setWorkingDir(var2);
      }

   }

   protected void displayMessage(String var1, boolean var2) {
      if (var2) {
         this.notifyListeners((byte)26, var1);
      } else {
         this.notifyListeners((byte)25, var1);
      }

   }

   protected void clearMessage() {
      this.notifyListeners((byte)25, "");
   }

   public void addListener(ControllerEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(ControllerEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(byte var1, Object var2) {
      ControllerEvent var3 = new ControllerEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ControllerEventListener)this.listeners.elementAt(var4)).actionPerformed(var3);
      }

   }

   public void addProgramListener(ProgramEventListener var1) {
      this.programListeners.add(var1);
   }

   public void removeProgramListener(ProgramEventListener var1) {
      this.programListeners.remove(var1);
   }

   protected void notifyProgramListeners(byte var1, String var2) {
      ProgramEvent var3 = new ProgramEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.programListeners.size(); ++var4) {
         ((ProgramEventListener)this.programListeners.elementAt(var4)).programChanged(var3);
      }

   }

   public void programChanged(ProgramEvent var1) {
      this.notifyProgramListeners(var1.getType(), var1.getProgramFileName());
   }
}
