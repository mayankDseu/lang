package SimulatorsGUI;

import Hack.CPUEmulator.ALUGUI;
import HackGUI.Format;
import HackGUI.Utilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D.Double;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class ALUComponent extends JPanel implements ALUGUI {
   private static final int START_LOCATION_ZERO_X = 7;
   private static final int START_LOCATION_ZERO_Y = 39;
   private static final int START_LOCATION_ONE_Y = 85;
   private static final int START_LOCATION_TWO_X = 237;
   private static final int START_LOCATION_TWO_Y = 61;
   private static final int START_ALU_X = 159;
   private static final int FINISH_ALU_X = 216;
   private static final int LOCATION_WIDTH = 124;
   private static final int LOCATION_HEIGHT = 19;
   private static final BasicStroke wideStroke = new BasicStroke(3.0F);
   private static final BasicStroke regularStroke = new BasicStroke(1.0F);
   protected int dataFormat = 0;
   protected short location0Value;
   protected short location1Value;
   protected short location2Value;
   protected JTextField location0 = new JTextField();
   protected JTextField location1 = new JTextField();
   protected JTextField location2 = new JTextField();
   private JTextField commandLbl = new JTextField();
   private Color aluColor = new Color(107, 194, 46);
   private JLabel nameLbl = new JLabel();
   private Border commandBorder;
   private JLabel location0Lbl = new JLabel();
   private JLabel location1Lbl = new JLabel();
   private JLabel location2Lbl = new JLabel();
   protected short nullValue;
   protected boolean hideNullValue;

   public ALUComponent() {
      this.jbInit();
   }

   public void setNullValue(short var1, boolean var2) {
      this.nullValue = var1;
      this.hideNullValue = var2;
   }

   protected String translateValueToString(short var1) {
      if (this.hideNullValue) {
         return var1 == this.nullValue ? "" : Format.translateValueToString(var1, this.dataFormat);
      } else {
         return Format.translateValueToString(var1, this.dataFormat);
      }
   }

   public void disableUserInput() {
   }

   public void enableUserInput() {
   }

   public void commandFlash() {
      this.commandLbl.setBackground(Color.red);
      this.repaint();
   }

   public void hideCommandFlash() {
      this.commandLbl.setBackground(new Color(107, 194, 46));
      this.repaint();
   }

   public void bodyFlash() {
      this.aluColor = Color.red;
      this.commandLbl.setBackground(Color.red);
      this.repaint();
   }

   public void hideBodyFlash() {
      this.aluColor = new Color(107, 194, 46);
      this.commandLbl.setBackground(new Color(107, 194, 46));
      this.repaint();
   }

   public void flash(int var1) {
      switch(var1) {
      case 0:
         this.location0.setBackground(Color.orange);
         break;
      case 1:
         this.location1.setBackground(Color.orange);
         break;
      case 2:
         this.location2.setBackground(Color.orange);
      }

   }

   public void hideFlash() {
      this.location0.setBackground((Color)null);
      this.location1.setBackground((Color)null);
      this.location2.setBackground((Color)null);
   }

   public void hideHighlight() {
      this.location0.setDisabledTextColor(Color.black);
      this.location1.setDisabledTextColor(Color.black);
      this.location2.setDisabledTextColor(Color.black);
      this.repaint();
   }

   public void highlight(int var1) {
      switch(var1) {
      case 0:
         this.location0.setDisabledTextColor(Color.blue);
         break;
      case 1:
         this.location1.setDisabledTextColor(Color.blue);
         break;
      case 2:
         this.location2.setDisabledTextColor(Color.blue);
      }

      this.repaint();
   }

   public Point getCoordinates(int var1) {
      Point var2 = this.getLocation();
      switch(var1) {
      case 0:
         return new Point((int)(var2.getX() + this.location0.getLocation().getX()), (int)(var2.getY() + this.location0.getLocation().getY()));
      case 1:
         return new Point((int)(var2.getX() + this.location1.getLocation().getX()), (int)(var2.getY() + this.location1.getLocation().getY()));
      case 2:
         return new Point((int)(var2.getX() + this.location2.getLocation().getX()), (int)(var2.getY() + this.location2.getLocation().getY()));
      default:
         return null;
      }
   }

   public String getValueAsString(int var1) {
      switch(var1) {
      case 0:
         return this.location0.getText();
      case 1:
         return this.location1.getText();
      case 2:
         return this.location2.getText();
      default:
         return null;
      }
   }

   public void reset() {
      this.location0.setText(Format.translateValueToString(this.nullValue, this.dataFormat));
      this.location1.setText(Format.translateValueToString(this.nullValue, this.dataFormat));
      this.location2.setText(Format.translateValueToString(this.nullValue, this.dataFormat));
      this.setCommand("");
      this.hideFlash();
      this.hideHighlight();
   }

   public void setValueAt(int var1, short var2) {
      String var3 = Format.translateValueToString(var2, this.dataFormat);
      switch(var1) {
      case 0:
         this.location0Value = var2;
         this.location0.setText(var3);
         break;
      case 1:
         this.location1Value = var2;
         this.location1.setText(var3);
         break;
      case 2:
         this.location2Value = var2;
         this.location2.setText(var3);
      }

   }

   public void setCommand(String var1) {
      this.commandLbl.setText(var1);
   }

   public void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      var2.setPaint(Color.black);
      int[] var3 = new int[]{159, 216, 216, 159};
      int[] var4 = new int[]{23, 56, 83, 116};
      GeneralPath var5 = new GeneralPath(0, var3.length);
      var5.moveTo((float)var3[0], (float)var4[0]);

      for(int var6 = 1; var6 < var3.length; ++var6) {
         var5.lineTo((float)var3[var6], (float)var4[var6]);
      }

      var5.closePath();
      var2.setPaint(this.aluColor);
      var2.fill(var5);
      var2.setStroke(wideStroke);
      var2.setPaint(Color.black);
      var2.draw(var5);
      var2.setStroke(regularStroke);
      var2.draw(new Double(131.0D, 48.0D, 159.0D, 48.0D));
      var2.draw(new Double(131.0D, 94.0D, 159.0D, 94.0D));
      var2.draw(new Double(216.0D, 70.0D, 236.0D, 70.0D));
   }

   public void setNumericFormat(int var1) {
      this.dataFormat = var1;
      this.location0.setText(Format.translateValueToString(this.location0Value, var1));
      this.location1.setText(Format.translateValueToString(this.location1Value, var1));
      this.location2.setText(Format.translateValueToString(this.location2Value, var1));
   }

   private void jbInit() {
      this.setOpaque(false);
      this.commandBorder = BorderFactory.createLineBorder(Color.black, 1);
      this.setLayout((LayoutManager)null);
      this.location0.setForeground(Color.black);
      this.location0.setDisabledTextColor(Color.black);
      this.location0.setEditable(false);
      this.location0.setHorizontalAlignment(4);
      this.location0.setBounds(new Rectangle(7, 39, 124, 19));
      this.location0.setBackground(UIManager.getColor("Button.background"));
      this.location0.setEnabled(false);
      this.location0.setFont(Utilities.valueFont);
      this.location1.setHorizontalAlignment(4);
      this.location1.setBounds(new Rectangle(7, 85, 124, 19));
      this.location1.setForeground(Color.black);
      this.location1.setDisabledTextColor(Color.black);
      this.location1.setEditable(false);
      this.location1.setBackground(UIManager.getColor("Button.background"));
      this.location1.setEnabled(false);
      this.location1.setFont(Utilities.valueFont);
      this.location2.setHorizontalAlignment(4);
      this.location2.setBounds(new Rectangle(237, 61, 124, 19));
      this.location2.setForeground(Color.black);
      this.location2.setDisabledTextColor(Color.black);
      this.location2.setEditable(false);
      this.location2.setBackground(UIManager.getColor("Button.background"));
      this.location2.setEnabled(false);
      this.location2.setFont(Utilities.valueFont);
      this.commandLbl.setBackground(new Color(107, 194, 46));
      this.commandLbl.setEnabled(false);
      this.commandLbl.setFont(Utilities.labelsFont);
      this.commandLbl.setForeground(Color.black);
      this.commandLbl.setBorder(this.commandBorder);
      this.commandLbl.setDisabledTextColor(Color.black);
      this.commandLbl.setEditable(false);
      this.commandLbl.setHorizontalAlignment(0);
      this.commandLbl.setBounds(new Rectangle(163, 62, 50, 16));
      this.location0Lbl.setText("D Input :");
      this.location0Lbl.setBounds(new Rectangle(7, 23, 56, 16));
      this.location0Lbl.setFont(Utilities.smallLabelsFont);
      this.location0Lbl.setForeground(Color.black);
      this.location1Lbl.setText("M/A Input :");
      this.location1Lbl.setBounds(new Rectangle(7, 69, 70, 16));
      this.location1Lbl.setFont(Utilities.smallLabelsFont);
      this.location1Lbl.setForeground(Color.black);
      this.location2Lbl.setText("ALU output :");
      this.location2Lbl.setBounds(new Rectangle(237, 45, 72, 16));
      this.location2Lbl.setFont(Utilities.smallLabelsFont);
      this.location2Lbl.setForeground(Color.black);
      this.nameLbl.setText("ALU");
      this.nameLbl.setFont(Utilities.labelsFont);
      this.nameLbl.setBounds(new Rectangle(6, 0, 50, 22));
      this.add(this.commandLbl, (Object)null);
      this.add(this.location1, (Object)null);
      this.add(this.location0, (Object)null);
      this.add(this.location2, (Object)null);
      this.add(this.location0Lbl, (Object)null);
      this.add(this.location1Lbl, (Object)null);
      this.add(this.location2Lbl, (Object)null);
      this.add(this.nameLbl, (Object)null);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setPreferredSize(new Dimension(368, 122));
      this.setSize(368, 122);
   }
}
