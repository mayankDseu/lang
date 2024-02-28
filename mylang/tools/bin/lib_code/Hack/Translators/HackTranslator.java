package Hack.Translators;

import Hack.ComputerParts.TextFileEvent;
import Hack.ComputerParts.TextFileEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.Timer;

public abstract class HackTranslator implements HackTranslatorEventListener, ActionListener, TextFileEventListener {
   private static final int FAST_FORWARD_DELAY = 750;
   private PrintWriter writer;
   protected String sourceFileName;
   protected String destFileName;
   protected int programSize;
   protected short[] program;
   protected String[] source;
   protected HackTranslatorGUI gui;
   private Timer timer;
   private boolean singleStepLocked;
   private HackTranslator.SingleStepTask singleStepTask;
   private HackTranslator.FullCompilationTask fullCompilationTask;
   private HackTranslator.FastForwardTask fastForwardTask;
   private HackTranslator.LoadSourceTask loadSourceTask;
   protected boolean compilationStarted;
   protected int destPC;
   protected int sourcePC;
   private boolean updateGUI;
   protected Hashtable compilationMap;
   protected boolean inFullCompilation;
   protected boolean inFastForward;

   public HackTranslator(String var1, int var2, short var3, boolean var4) throws HackTranslatorException {
      if (var1.indexOf(".") < 0) {
         var1 = var1 + "." + this.getSourceExtension();
      }

      this.checkSourceFile(var1);
      this.source = new String[0];
      this.init(var2, var3);
      this.loadSource(var1);
      this.fullCompilation();
      if (var4) {
         this.save();
      }

   }

   public HackTranslator(HackTranslatorGUI var1, int var2, short var3, String var4) throws HackTranslatorException {
      this.gui = var1;
      var1.addHackTranslatorListener(this);
      var1.getSource().addTextFileListener(this);
      var1.setTitle(this.getName() + getVersionString());
      this.singleStepTask = new HackTranslator.SingleStepTask();
      this.fullCompilationTask = new HackTranslator.FullCompilationTask();
      this.fastForwardTask = new HackTranslator.FastForwardTask();
      this.loadSourceTask = new HackTranslator.LoadSourceTask();
      this.timer = new Timer(750, this);
      this.init(var2, var3);
      File var5 = this.loadWorkingDir();
      var1.setWorkingDir(var5);
      if (var4 == null) {
         var1.disableSingleStep();
         var1.disableFastForward();
         var1.disableStop();
         var1.disableRewind();
         var1.disableFullCompilation();
         var1.disableSave();
         var1.enableLoadSource();
         var1.disableSourceRowSelection();
      } else {
         this.loadSource(var4);
         var1.setSourceName(var4);
      }

   }

   protected abstract String getSourceExtension();

   protected abstract String getDestinationExtension();

   protected abstract String getName();

   private static String getVersionString() {
      return " (2.5)";
   }

   protected int[] compileLineAndCount(String var1) throws HackTranslatorException {
      int[] var2 = null;
      int var3 = this.destPC;
      this.compileLine(var1);
      int var4 = this.destPC - var3;
      if (var4 > 0) {
         var2 = new int[]{var3, this.destPC - 1};
      }

      return var2;
   }

   protected abstract void compileLine(String var1) throws HackTranslatorException;

   protected void init(int var1, short var2) {
      this.program = new short[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         this.program[var3] = var2;
      }

      this.programSize = 0;
   }

   private void checkSourceFile(String var1) throws HackTranslatorException {
      if (!var1.endsWith("." + this.getSourceExtension())) {
         throw new HackTranslatorException(var1 + " is not a ." + this.getSourceExtension() + " file");
      } else {
         File var2 = new File(var1);
         if (!var2.exists()) {
            throw new HackTranslatorException("file " + var1 + " does not exist");
         }
      }
   }

   private void checkDestinationFile(String var1) throws HackTranslatorException {
      if (!var1.endsWith("." + this.getDestinationExtension())) {
         throw new HackTranslatorException(var1 + " is not a ." + this.getDestinationExtension() + " file");
      }
   }

   protected void restartCompilation() {
      this.compilationMap = new Hashtable();
      this.sourcePC = 0;
      this.destPC = 0;
      if (this.gui != null) {
         this.compilationStarted = false;
         this.gui.getDestination().reset();
         this.hidePointers();
         this.gui.enableSingleStep();
         this.gui.enableFastForward();
         this.gui.disableStop();
         this.gui.enableRewind();
         this.gui.enableFullCompilation();
         this.gui.disableSave();
         this.gui.enableLoadSource();
         this.gui.disableSourceRowSelection();
      }

   }

   private void loadSource(String var1) throws HackTranslatorException {
      Vector var3 = new Vector();
      Vector var4 = null;
      String var5 = null;

      try {
         if (this.gui != null) {
            this.gui.disableSingleStep();
            this.gui.disableFastForward();
            this.gui.disableStop();
            this.gui.disableRewind();
            this.gui.disableFullCompilation();
            this.gui.disableSave();
            this.gui.disableLoadSource();
            this.gui.disableSourceRowSelection();
            this.gui.displayMessage("Please wait...", false);
         }

         this.checkSourceFile(var1);
         this.sourceFileName = var1;
         var4 = new Vector();
         BufferedReader var6 = new BufferedReader(new FileReader(this.sourceFileName));

         String var2;
         while((var2 = var6.readLine()) != null) {
            var3.addElement(var2);
            if (this.gui != null) {
               var4.addElement(var2);
            }
         }

         var6.close();
         this.source = new String[var3.size()];
         var3.toArray(this.source);
         if (this.gui != null) {
            String[] var7 = new String[var4.size()];
            var4.toArray(var7);
            this.gui.getSource().setContents(var7);
         }

         this.destFileName = this.sourceFileName.substring(0, this.sourceFileName.indexOf(46)) + "." + this.getDestinationExtension();
         this.initSource();
         this.restartCompilation();
         this.resetProgram();
         if (this.gui != null) {
            this.gui.setDestinationName(this.destFileName);
            this.gui.displayMessage("", false);
         }
      } catch (HackTranslatorException var8) {
         var5 = var8.getMessage();
      } catch (IOException var9) {
         var5 = "error reading from file " + this.sourceFileName;
      }

      if (var5 != null) {
         if (this.gui != null) {
            this.gui.enableLoadSource();
         }

         throw new HackTranslatorException(var5);
      }
   }

   protected abstract void initSource() throws HackTranslatorException;

   protected void resetProgram() {
      this.programSize = 0;
      if (this.gui != null) {
         this.gui.getDestination().reset();
      }

   }

   protected abstract void initCompilation() throws HackTranslatorException;

   protected abstract void finalizeCompilation();

   protected void successfulCompilation() throws HackTranslatorException {
      if (this.gui != null) {
         this.gui.displayMessage("File compilation succeeded", false);
      }

   }

   private void fullCompilation() throws HackTranslatorException {
      try {
         this.inFullCompilation = true;
         this.initCompilation();
         if (this.gui != null) {
            this.gui.disableSingleStep();
            this.gui.disableFastForward();
            this.gui.disableRewind();
            this.gui.disableFullCompilation();
            this.gui.disableLoadSource();
            this.gui.getSource().setContents(this.sourceFileName);
         }

         for(this.updateGUI = false; this.sourcePC < this.source.length; ++this.sourcePC) {
            int[] var1 = this.compileLineAndCount(this.source[this.sourcePC]);
            if (var1 != null) {
               this.compilationMap.put(new Integer(this.sourcePC), var1);
            }
         }

         this.successfulCompilation();
         this.finalizeCompilation();
         this.programSize = this.destPC;
         if (this.gui != null) {
            this.showProgram(this.programSize);
            this.gui.getDestination().clearHighlights();
            this.gui.enableRewind();
            this.gui.enableLoadSource();
            this.gui.enableSave();
            this.gui.enableSourceRowSelection();
         }

         this.inFullCompilation = false;
      } catch (HackTranslatorException var2) {
         this.inFullCompilation = false;
         throw new HackTranslatorException(var2.getMessage());
      }
   }

   protected abstract String getCodeString(short var1, int var2, boolean var3);

   protected void addCommand(short var1) throws HackTranslatorException {
      if (this.destPC >= this.program.length) {
         throw new HackTranslatorException("Program too large");
      } else {
         this.program[this.destPC++] = var1;
         if (this.updateGUI) {
            this.gui.getDestination().addLine(this.getCodeString(var1, this.destPC - 1, true));
         }

      }
   }

   protected void replaceCommand(int var1, short var2) {
      this.program[var1] = var2;
      if (this.updateGUI) {
         this.gui.getDestination().setLineAt(var1, this.getCodeString(var2, var1, true));
      }

   }

   protected void showProgram(int var1) {
      this.gui.getDestination().reset();
      String[] var2 = new String[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         var2[var3] = this.getCodeString(this.program[var3], var3, true);
      }

      this.gui.getDestination().setContents(var2);
   }

   protected void fastForward() {
      this.gui.disableSingleStep();
      this.gui.disableFastForward();
      this.gui.enableStop();
      this.gui.disableRewind();
      this.gui.disableFullCompilation();
      this.gui.disableLoadSource();
      this.inFastForward = true;
      this.timer.start();
   }

   private void singleStep() {
      this.singleStepLocked = true;

      try {
         this.initCompilation();
         if (!this.compilationStarted) {
            this.compilationStarted = true;
         }

         this.gui.getSource().addHighlight(this.sourcePC, true);
         this.gui.getDestination().clearHighlights();
         this.updateGUI = true;
         int[] var1 = this.compileLineAndCount(this.source[this.sourcePC]);
         if (var1 != null) {
            this.compilationMap.put(new Integer(this.sourcePC), var1);
         }

         ++this.sourcePC;
         if (this.sourcePC == this.source.length) {
            this.successfulCompilation();
            this.programSize = this.destPC;
            this.gui.enableSave();
            this.gui.enableSourceRowSelection();
            this.end(false);
         }

         this.finalizeCompilation();
      } catch (HackTranslatorException var2) {
         this.gui.displayMessage(var2.getMessage(), true);
         this.end(false);
      }

      this.singleStepLocked = false;
   }

   protected void hidePointers() {
      this.gui.getSource().clearHighlights();
      this.gui.getDestination().clearHighlights();
      this.gui.getSource().hideSelect();
      this.gui.getDestination().hideSelect();
   }

   protected void end(boolean var1) {
      this.timer.stop();
      this.gui.disableSingleStep();
      this.gui.disableFastForward();
      this.gui.disableStop();
      this.gui.enableRewind();
      this.gui.disableFullCompilation();
      this.gui.enableLoadSource();
      this.inFastForward = false;
      if (var1) {
         this.hidePointers();
      }

   }

   protected void stop() {
      this.timer.stop();
      this.gui.enableSingleStep();
      this.gui.enableFastForward();
      this.gui.disableStop();
      this.gui.enableRewind();
      this.gui.enableLoadSource();
      this.gui.enableFullCompilation();
      this.inFastForward = false;
   }

   protected void rewind() {
      this.restartCompilation();
      this.resetProgram();
   }

   private void save() throws HackTranslatorException {
      try {
         this.writer = new PrintWriter(new FileWriter(this.destFileName));
         this.dumpToFile();
         this.writer.close();
      } catch (IOException var2) {
         throw new HackTranslatorException("could not create file " + this.destFileName);
      }
   }

   public short[] getProgram() {
      return this.program;
   }

   private void dumpToFile() {
      for(short var1 = 0; var1 < this.programSize; ++var1) {
         this.writer.println(this.getCodeString(this.program[var1], var1, false));
      }

      this.writer.close();
   }

   protected void clearMessage() {
      this.gui.displayMessage("", false);
   }

   protected int[] rowIndexToRange(int var1) {
      Integer var2 = new Integer(var1);
      return (int[])this.compilationMap.get(var2);
   }

   protected File loadWorkingDir() {
      String var1 = ".";

      try {
         BufferedReader var2 = new BufferedReader(new FileReader("bin/" + this.getName() + ".dat"));
         var1 = var2.readLine();
         var2.close();
      } catch (IOException var3) {
      }

      return new File(var1);
   }

   protected void saveWorkingDir(File var1) {
      try {
         PrintWriter var2 = new PrintWriter(new FileWriter("bin/" + this.getName() + ".dat"));
         var2.println(var1.getAbsolutePath());
         var2.close();
      } catch (IOException var3) {
      }

      this.gui.setWorkingDir(var1);
   }

   public void rowSelected(TextFileEvent var1) {
      int var2 = var1.getRowIndex();
      int[] var3 = this.rowIndexToRange(var2);
      this.gui.getSource().addHighlight(var2, true);
      this.gui.getSource().hideSelect();
      if (var3 != null) {
         this.gui.getDestination().clearHighlights();

         for(int var4 = var3[0]; var4 <= var3[1]; ++var4) {
            this.gui.getDestination().addHighlight(var4, false);
         }
      } else {
         this.gui.getDestination().clearHighlights();
      }

   }

   public void actionPerformed(ActionEvent var1) {
      if (!this.singleStepLocked) {
         this.singleStep();
      }

   }

   protected void error(String var1) throws HackTranslatorException {
      throw new HackTranslatorException(var1, this.sourcePC);
   }

   public void actionPerformed(HackTranslatorEvent var1) {
      Thread var2;
      String var3;
      File var4;
      switch(var1.getAction()) {
      case 1:
         this.clearMessage();
         if (this.sourceFileName == null) {
            this.gui.displayMessage("No source file specified", true);
         } else if (this.destFileName == null) {
            this.gui.displayMessage("No destination file specified", true);
         } else {
            var2 = new Thread(this.singleStepTask);
            var2.start();
         }
         break;
      case 2:
         this.clearMessage();
         var2 = new Thread(this.fastForwardTask);
         var2.start();
         break;
      case 3:
         this.stop();
         break;
      case 4:
         this.clearMessage();
         this.rewind();
         break;
      case 5:
         this.clearMessage();
         var2 = new Thread(this.fullCompilationTask);
         var2.start();
         break;
      case 6:
         this.clearMessage();
         var3 = (String)var1.getData();

         try {
            this.checkDestinationFile(var3);
            this.destFileName = var3;
            var4 = new File(var3);
            this.saveWorkingDir(var4);
            this.gui.setTitle(this.getName() + getVersionString() + " - " + var3);
            this.save();
         } catch (HackTranslatorException var6) {
            this.gui.setDestinationName("");
            this.gui.displayMessage(var6.getMessage(), true);
         }
         break;
      case 7:
         var3 = (String)var1.getData();
         var4 = new File(var3);
         this.saveWorkingDir(var4);
         this.gui.setTitle(this.getName() + getVersionString() + " - " + var3);
         this.loadSourceTask.setFileName(var3);
         var2 = new Thread(this.loadSourceTask);
         var2.start();
      }

   }

   class LoadSourceTask implements Runnable {
      private String fileName;

      public void run() {
         try {
            HackTranslator.this.loadSource(this.fileName);
         } catch (HackTranslatorException var2) {
            HackTranslator.this.gui.setSourceName("");
            HackTranslator.this.gui.displayMessage(var2.getMessage(), true);
         }

      }

      public void setFileName(String var1) {
         this.fileName = var1;
      }
   }

   class FastForwardTask implements Runnable {
      public void run() {
         HackTranslator.this.fastForward();
      }
   }

   class SingleStepTask implements Runnable {
      public void run() {
         if (!HackTranslator.this.singleStepLocked) {
            HackTranslator.this.singleStep();
         }

      }
   }

   class FullCompilationTask implements Runnable {
      public void run() {
         HackTranslator.this.gui.displayMessage("Please wait...", false);

         try {
            HackTranslator.this.restartCompilation();
            HackTranslator.this.fullCompilation();
         } catch (HackTranslatorException var2) {
            HackTranslator.this.end(false);
            HackTranslator.this.gui.getSource().addHighlight(HackTranslator.this.sourcePC, true);
            HackTranslator.this.gui.displayMessage(var2.getMessage(), true);
         }

      }
   }
}
