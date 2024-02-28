package SimulatorsGUI;

import Hack.Controller.HackSimulatorGUI;
import java.awt.Point;
import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class HackSimulatorComponent extends JPanel implements HackSimulatorGUI {
   protected JComponent currentAdditionalDisplay = null;
   protected String usageFileName;
   protected String aboutFileName;

   public void setAdditionalDisplay(JComponent var1) {
      if (this.currentAdditionalDisplay != null) {
         this.remove(this.currentAdditionalDisplay);
      }

      this.currentAdditionalDisplay = var1;
      if (var1 != null) {
         var1.setLocation(this.getAdditionalDisplayLocation());
         this.add(var1, 1);
         var1.revalidate();
      }

      this.revalidate();
      this.repaint();
   }

   protected abstract Point getAdditionalDisplayLocation();

   public void setUsageFileName(String var1) {
      this.usageFileName = var1;
   }

   public void setAboutFileName(String var1) {
      this.aboutFileName = var1;
   }

   public String getUsageFileName() {
      return this.usageFileName;
   }

   public String getAboutFileName() {
      return this.aboutFileName;
   }
}
