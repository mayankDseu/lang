package HackGUI;

import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

public class FileChooserComponent extends JPanel {
   private JLabel fileTypeName = new JLabel();
   protected JTextField fileName = new JTextField();
   private JButton browseButton = new JButton();
   private JFileChooser fc = new JFileChooser();
   private JFrame fileChooserFrame = new JFrame();
   protected String currentFileName = "";
   private ImageIcon load = new ImageIcon("bin/images/open.gif");
   private Vector listeners = new Vector();

   public FileChooserComponent() {
      this.jbInit();
      this.fileChooserFrame.setSize(440, 250);
      this.fileChooserFrame.setLocation(250, 250);
      this.fileChooserFrame.setTitle("Choose a file :");
      this.fileChooserFrame.getContentPane().add(this.fc);
   }

   public void addListener(EnterPressedListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(EnterPressedListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners() {
      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ((EnterPressedListener)this.listeners.elementAt(var1)).enterPressed();
      }

   }

   public void setName(String var1) {
      this.fileTypeName.setText(var1);
   }

   public String getCurrentFileName() {
      return this.currentFileName;
   }

   public void setSelectionToDirectories() {
      this.fc.setFileSelectionMode(1);
   }

   public void setCurrentFileName(String var1) {
      this.currentFileName = var1;
   }

   public void showCurrentFileName() {
      this.fileName.setText(this.currentFileName);
   }

   public void setFilter(FileFilter var1) {
      this.fc.setFileFilter(var1);
   }

   public boolean isFileNameChanged() {
      return !this.currentFileName.equals(this.fileName.getText());
   }

   public String getFileName() {
      return this.fileName.getText();
   }

   public JTextField getTextField() {
      return this.fileName;
   }

   public void setScriptDir(String var1) {
      this.fc.setCurrentDirectory(new File(var1));
   }

   private void jbInit() {
      this.fileTypeName.setFont(Utilities.thinLabelsFont);
      this.fileTypeName.setText("File Type: ");
      this.fileTypeName.setBounds(new Rectangle(10, 10, 104, 26));
      this.setLayout((LayoutManager)null);
      this.fileName.setDisabledTextColor(Color.black);
      this.fileName.setBounds(new Rectangle(118, 13, 221, 22));
      this.fileName.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FileChooserComponent.this.fileName_actionPerformed(var1);
         }
      });
      this.browseButton.setToolTipText("Load File");
      this.browseButton.setIcon(this.load);
      this.browseButton.setBounds(new Rectangle(351, 12, 46, 24));
      this.browseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            FileChooserComponent.this.browseButton_actionPerformed(var1);
         }
      });
      this.currentFileName = "";
      this.add(this.fileTypeName, (Object)null);
      this.add(this.fileName, (Object)null);
      this.add(this.browseButton, (Object)null);
   }

   public void browseButton_actionPerformed(ActionEvent var1) {
      int var2 = this.fc.showDialog(this, "Select file");
      if (var2 == 0) {
         File var3 = this.fc.getSelectedFile();
         this.fileName.setText(var3.getAbsolutePath());
      }

   }

   public void fileName_actionPerformed(ActionEvent var1) {
      this.notifyListeners();
   }
}
