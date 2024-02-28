package HackGUI;

import java.io.IOException;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class HTMLViewFrame extends JFrame {
   private JScrollPane scrollPane;
   private JEditorPane ep = new JEditorPane();

   public HTMLViewFrame(String var1) {
      this.setTitle("Help");
      this.ep.setEditable(false);
      this.ep.setContentType("text/html");

      try {
         this.ep.setPage("file:" + var1);
      } catch (IOException var3) {
         System.err.println("Error while reading file: " + var1);
         System.exit(-1);
      }

      this.ep.addHyperlinkListener(new Hyperactive());
      this.scrollPane = new JScrollPane(this.ep);
      this.setBounds(30, 44, 750, 460);
      this.setDefaultCloseOperation(1);
      this.getContentPane().add(this.scrollPane, "Center");
   }
}
