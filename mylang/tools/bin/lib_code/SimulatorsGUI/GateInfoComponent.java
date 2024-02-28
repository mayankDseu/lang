package SimulatorsGUI;

import Hack.HardwareSimulator.GateInfoGUI;
import HackGUI.Utilities;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.SystemColor;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GateInfoComponent extends JPanel implements GateInfoGUI {
   private JLabel chipNameLbl = new JLabel();
   private JLabel timeLbl = new JLabel();
   private JTextField chipNameTxt = new JTextField();
   private JTextField timeTxt = new JTextField();
   private boolean clockUp;
   private String chipName;

   public GateInfoComponent() {
      this.jbInit();
   }

   public void setChip(String var1) {
      this.chipName = var1;
      this.chipNameTxt.setText(var1);
   }

   public void setClock(boolean var1) {
      this.clockUp = var1;
      if (var1) {
         this.timeTxt.setText(this.timeTxt.getText() + "+");
      }

   }

   public void setClocked(boolean var1) {
      if (var1) {
         this.chipNameTxt.setText(this.chipName + " (Clocked) ");
      } else {
         this.chipNameTxt.setText(this.chipName);
      }

   }

   public void setTime(int var1) {
      if (this.clockUp) {
         this.timeTxt.setText(var1 + "+");
      } else {
         this.timeTxt.setText(String.valueOf(var1));
      }

   }

   public void reset() {
      this.chipNameTxt.setText("");
      this.timeTxt.setText("0");
   }

   public void enableTime() {
      this.timeLbl.setEnabled(true);
      this.timeTxt.setEnabled(true);
   }

   public void disableTime() {
      this.timeLbl.setEnabled(false);
      this.timeTxt.setEnabled(false);
   }

   private void jbInit() {
      this.setLayout((LayoutManager)null);
      this.chipNameLbl.setText("Chip Name :");
      this.chipNameLbl.setBounds(new Rectangle(11, 7, 74, 21));
      this.timeLbl.setBounds(new Rectangle(341, 8, 42, 21));
      this.timeLbl.setText("Time :");
      this.chipNameTxt.setBackground(SystemColor.info);
      this.chipNameTxt.setFont(Utilities.thinBigLabelsFont);
      this.chipNameTxt.setEditable(false);
      this.chipNameTxt.setHorizontalAlignment(2);
      this.chipNameTxt.setBounds(new Rectangle(89, 8, 231, 20));
      this.timeTxt.setBackground(SystemColor.info);
      this.timeTxt.setFont(Utilities.thinBigLabelsFont);
      this.timeTxt.setEditable(false);
      this.timeTxt.setBounds(new Rectangle(388, 8, 69, 20));
      this.add(this.chipNameTxt, (Object)null);
      this.add(this.chipNameLbl, (Object)null);
      this.add(this.timeLbl, (Object)null);
      this.add(this.timeTxt, (Object)null);
      this.setSize(483, 37);
      this.setBorder(BorderFactory.createEtchedBorder());
   }
}
