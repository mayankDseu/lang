package HackGUI;

import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.JCheckBox;

public class ViewableFileChooserComponent extends FileChooserComponent {
   private JCheckBox viewCheckBox = new JCheckBox();
   private FileContentWindow window;
   private File contentFile;

   public ViewableFileChooserComponent() {
      this.jbInit();
      this.window = new FileContentWindow();
   }

   public void setWindowLocation(int var1, int var2) {
      this.window.setLocation(var1, var2);
   }

   public void setFileContent() {
      this.contentFile = new File(this.getFileName());
      this.window.setContent(this.contentFile);
      this.window.setTitle("File: " + this.contentFile);
   }

   public void deleteContentFile() {
      this.window.deleteContent();
      this.window.setTitle("");
   }

   public void refresh() {
      this.window.loadAnyway();
      this.setFileContent();
   }

   public void disableCheckBox() {
      this.viewCheckBox.setEnabled(false);
   }

   public void showCurrentFileName() {
      this.fileName.setText(this.currentFileName);
      if (this.viewCheckBox.isSelected()) {
         if (!this.currentFileName.equals("")) {
            this.setFileContent();
         } else {
            this.viewCheckBox.setSelected(false);
            this.window.setVisible(false);
            this.deleteContentFile();
         }
      }

   }

   private void jbInit() {
      this.viewCheckBox.setText("View File");
      this.viewCheckBox.setFont(Utilities.thinLabelsFont);
      this.viewCheckBox.setBounds(new Rectangle(407, 12, 76, 23));
      this.viewCheckBox.addItemListener(new ItemListener() {
         public void itemStateChanged(ItemEvent var1) {
            ViewableFileChooserComponent.this.viewCheckBox_itemStateChanged(var1);
         }
      });
      this.add(this.viewCheckBox, (Object)null);
   }

   public void viewCheckBox_itemStateChanged(ItemEvent var1) {
      if (var1.getStateChange() == 1) {
         this.window.setTitle("Loading...");
         this.window.setVisible(true);
         this.setFileContent();
      } else if (var1.getStateChange() == 2) {
         this.window.setVisible(false);
      }

   }
}
