package HackGUI;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ControllerFileChooser extends JFrame {
   private FileChooserComponent outputFileChooser = new FileChooserComponent();
   private FileChooserComponent comparisonFileChooser = new FileChooserComponent();
   private FileChooserComponent scriptFileChooser = new FileChooserComponent();
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private Vector listeners = new Vector();

   public ControllerFileChooser() {
      this.jbInit();
      this.scriptFileChooser.setName("Script File :");
      this.outputFileChooser.setName("Output File :");
      this.comparisonFileChooser.setName("Comparison File :");
   }

   public void showWindow() {
      this.setVisible(true);
      this.scriptFileChooser.getTextField().requestFocus();
   }

   public void addListener(FilesTypeListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(FilesTypeListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(String var1, String var2, String var3) {
      FilesTypeEvent var4 = new FilesTypeEvent(this, var1, var2, var3);

      for(int var5 = 0; var5 < this.listeners.size(); ++var5) {
         ((FilesTypeListener)this.listeners.elementAt(var5)).filesNamesChanged(var4);
      }

   }

   public void setScriptDir(String var1) {
      this.scriptFileChooser.setScriptDir(var1);
   }

   public void setScriptFile(String var1) {
      this.scriptFileChooser.setCurrentFileName(var1);
      this.scriptFileChooser.showCurrentFileName();
   }

   public void setOutputFile(String var1) {
      this.outputFileChooser.setCurrentFileName(var1);
      this.outputFileChooser.showCurrentFileName();
   }

   public void setComparisonFile(String var1) {
      this.comparisonFileChooser.setCurrentFileName(var1);
      this.comparisonFileChooser.showCurrentFileName();
   }

   private void jbInit() {
      this.getContentPane().setLayout((LayoutManager)null);
      this.setTitle("Files selection");
      this.scriptFileChooser.setBounds(new Rectangle(5, 2, 485, 48));
      this.outputFileChooser.setBounds(new Rectangle(5, 38, 485, 48));
      this.comparisonFileChooser.setBounds(new Rectangle(5, 74, 485, 48));
      this.okButton.setToolTipText("OK");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(123, 134, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerFileChooser.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(283, 134, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerFileChooser.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("CANCEL");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.scriptFileChooser, (Object)null);
      this.getContentPane().add(this.outputFileChooser, (Object)null);
      this.getContentPane().add(this.comparisonFileChooser, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.setSize(500, 210);
      this.setLocation(20, 415);
   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.scriptFileChooser.showCurrentFileName();
      this.outputFileChooser.showCurrentFileName();
      this.comparisonFileChooser.showCurrentFileName();
      this.setVisible(false);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      String var2 = null;
      String var3 = null;
      String var4 = null;
      if (this.scriptFileChooser.isFileNameChanged() || !this.scriptFileChooser.getFileName().equals("")) {
         var2 = this.scriptFileChooser.getFileName();
         this.scriptFileChooser.setCurrentFileName(var2);
         this.scriptFileChooser.showCurrentFileName();
      }

      if (this.outputFileChooser.isFileNameChanged() || !this.outputFileChooser.getFileName().equals("")) {
         var3 = this.outputFileChooser.getFileName();
         this.outputFileChooser.setCurrentFileName(var3);
         this.outputFileChooser.showCurrentFileName();
      }

      if (this.comparisonFileChooser.isFileNameChanged() || !this.comparisonFileChooser.getFileName().equals("")) {
         var4 = this.comparisonFileChooser.getFileName();
         this.comparisonFileChooser.setCurrentFileName(var4);
         this.comparisonFileChooser.showCurrentFileName();
      }

      if (var2 != null || var3 != null || var4 != null) {
         this.notifyListeners(var2, var3, var4);
      }

      this.setVisible(false);
   }
}
