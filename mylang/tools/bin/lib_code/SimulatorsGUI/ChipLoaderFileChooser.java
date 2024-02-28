package SimulatorsGUI;

import HackGUI.FileChooserComponent;
import HackGUI.FilesTypeEvent;
import HackGUI.FilesTypeListener;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ChipLoaderFileChooser extends JFrame {
   private FileChooserComponent workingDir = new FileChooserComponent();
   private FileChooserComponent builtInDir = new FileChooserComponent();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private Vector listeners = new Vector();

   public ChipLoaderFileChooser() {
      super("Directories Selection");
      this.setSelectionToDirectory();
      this.setNames();
      this.jbInit();
   }

   public void showWindow() {
      this.setVisible(true);
      this.workingDir.getTextField().requestFocus();
   }

   public void addListener(FilesTypeListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(FilesTypeListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(String var1, String var2) {
      FilesTypeEvent var3 = new FilesTypeEvent(this, var1, var2, (String)null);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((FilesTypeListener)this.listeners.elementAt(var4)).filesNamesChanged(var3);
      }

   }

   public void setWorkingDir(File var1) {
      this.workingDir.setCurrentFileName(var1.getName());
      this.workingDir.showCurrentFileName();
   }

   public void setBuiltInDir(File var1) {
      this.builtInDir.setCurrentFileName(var1.getName());
      this.builtInDir.showCurrentFileName();
   }

   private void setSelectionToDirectory() {
      this.workingDir.setSelectionToDirectories();
      this.builtInDir.setSelectionToDirectories();
   }

   private void setNames() {
      this.workingDir.setName("Working Dir :");
      this.builtInDir.setName("BuiltIn Dir :");
   }

   private void jbInit() {
      this.getContentPane().setLayout((LayoutManager)null);
      this.workingDir.setBounds(new Rectangle(5, 2, 485, 48));
      this.builtInDir.setBounds(new Rectangle(5, 38, 485, 48));
      this.okButton.setToolTipText("OK");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(90, 95, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ChipLoaderFileChooser.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(265, 95, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ChipLoaderFileChooser.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("CANCEL");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.workingDir, (Object)null);
      this.getContentPane().add(this.builtInDir, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.setSize(470, 210);
      this.setLocation(250, 250);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      String var2 = null;
      String var3 = null;
      if (this.workingDir.isFileNameChanged()) {
         var2 = this.workingDir.getFileName();
         this.workingDir.setCurrentFileName(var2);
         this.workingDir.showCurrentFileName();
      }

      if (this.builtInDir.isFileNameChanged()) {
         var3 = this.builtInDir.getFileName();
         this.builtInDir.setCurrentFileName(var3);
         this.builtInDir.showCurrentFileName();
      }

      if (var2 != null || var3 != null) {
         this.notifyListeners(var2, var3);
      }

      this.setVisible(false);
   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.workingDir.showCurrentFileName();
      this.builtInDir.showCurrentFileName();
      this.setVisible(false);
   }
}
