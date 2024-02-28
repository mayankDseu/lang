package HackGUI;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class FileContentWindow extends JFrame {
   private JTextArea fileContent = new JTextArea();
   private BufferedReader reader;
   private String displayedFileName;
   private boolean loadAnyway;
   private JScrollPane scrollPane;

   public FileContentWindow() {
      this.jbInit();
   }

   public void loadAnyway() {
      this.loadAnyway = true;
   }

   public void setContent(File var1) {
      if (this.loadAnyway || !var1.equals(this.displayedFileName)) {
         this.displayedFileName = var1.getAbsolutePath();
         this.fileContent.setText("");

         try {
            this.reader = new BufferedReader(new FileReader(var1));

            String var2;
            while((var2 = this.reader.readLine()) != null) {
               this.fileContent.append(var2);
               this.fileContent.append("\n");
            }

            this.reader.close();
         } catch (IOException var3) {
         }
      }

      this.fileContent.select(0, 0);
   }

   public void deleteContent() {
      this.fileContent.setText("");
   }

   private void jbInit() {
      this.fileContent.setEditable(false);
      this.fileContent.setFont(Utilities.valueFont);
      this.fileContent.setEnabled(false);
      this.fileContent.setDisabledTextColor(Color.black);
      this.scrollPane = new JScrollPane(this.fileContent);
      this.scrollPane.setPreferredSize(new Dimension(190, 330));
      this.setSize(375, 372);
      this.getContentPane().add(this.scrollPane, "Center");
   }
}
