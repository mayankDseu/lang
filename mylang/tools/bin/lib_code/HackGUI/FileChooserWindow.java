package HackGUI;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class FileChooserWindow extends JFrame implements EnterPressedListener {
   private ViewableFileChooserComponent fileChooser = new ViewableFileChooserComponent();
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private Vector listeners = new Vector();

   public FileChooserWindow(FileFilter var1) {
      this.fileChooser.setFilter(var1);
      this.jbInit();
   }

   public void showWindow() {
      this.setVisible(true);
      this.fileChooser.getTextField().requestFocus();
   }

   public void enterPressed() {
      String var1 = null;
      var1 = this.fileChooser.getFileName();
      this.fileChooser.setCurrentFileName(var1);
      if (var1 != null) {
         this.notifyListeners(var1);
      }

      this.setVisible(false);
   }

   public void setFileName(String var1) {
      this.fileChooser.setCurrentFileName(var1);
      this.fileChooser.showCurrentFileName();
   }

   public void setName(String var1) {
      this.fileChooser.setName(var1);
   }

   public JTextField getTextField() {
      return this.fileChooser.getTextField();
   }

   private void jbInit() {
      this.fileChooser.addListener(this);
      this.fileChooser.setWindowLocation(647, 3);
      this.getContentPane().setLayout((LayoutManager)null);
      this.setTitle("Files selection");
      this.fileChooser.setBounds(new Rectangle(5, 2, 482, 48));
      this.okButton.setToolTipText("OK");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(124, 64, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FileChooserWindow.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(282, 63, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FileChooserWindow.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("CANCEL");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.fileChooser, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.setSize(496, 150);
      this.setLocation(145, 250);
   }

   public void addListener(FilesTypeListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(FilesTypeListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(String var1) {
      FilesTypeEvent var2 = new FilesTypeEvent(this, var1, (String)null, (String)null);

      for(int var3 = 0; var3 < this.listeners.size(); ++var3) {
         ((FilesTypeListener)this.listeners.elementAt(var3)).filesNamesChanged(var2);
      }

   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.fileChooser.showCurrentFileName();
      this.setVisible(false);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      String var2 = null;
      var2 = this.fileChooser.getFileName();
      this.fileChooser.setCurrentFileName(var2);
      this.setVisible(false);
      if (var2 != null) {
         this.notifyListeners(var2);
      }

   }
}
