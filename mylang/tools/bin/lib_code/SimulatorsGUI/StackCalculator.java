package SimulatorsGUI;

import Hack.VMEmulator.CalculatorGUI;
import HackGUI.Format;
import HackGUI.Utilities;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D.Double;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class StackCalculator extends JPanel implements CalculatorGUI {
   private JTextField firstInput = new JTextField();
   private JTextField command = new JTextField();
   private JTextField secondInput = new JTextField();
   private JTextField output = new JTextField();
   private static final BasicStroke wideStroke = new BasicStroke(3.0F);
   private static final BasicStroke regularStroke = new BasicStroke(1.0F);
   protected short nullValue;
   protected boolean hideNullValue;

   public StackCalculator() {
      this.jbInit();
   }

   public void disableUserInput() {
   }

   public void enableUserInput() {
   }

   public void setNullValue(short var1, boolean var2) {
      this.nullValue = var1;
      this.hideNullValue = var2;
   }

   protected String translateValueToString(short var1) {
      if (this.hideNullValue) {
         return var1 == this.nullValue ? "" : Format.translateValueToString(var1, 0);
      } else {
         return Format.translateValueToString(var1, 0);
      }
   }

   public void hideCalculator() {
      this.setVisible(false);
   }

   public void showCalculator() {
      this.setVisible(true);
   }

   public void hideLeftInput() {
      this.firstInput.setVisible(false);
   }

   public void showLeftInput() {
      this.firstInput.setVisible(true);
   }

   public void setOperator(char var1) {
      this.command.setText(String.valueOf(var1));
   }

   public void hideFlash() {
      this.firstInput.setBackground(UIManager.getColor("Button.background"));
      this.secondInput.setBackground(UIManager.getColor("Button.background"));
      this.output.setBackground(UIManager.getColor("Button.background"));
   }

   public void flash(int var1) {
      switch(var1) {
      case 0:
         this.firstInput.setBackground(Color.orange);
         break;
      case 1:
         this.secondInput.setBackground(Color.orange);
         break;
      case 2:
         this.output.setBackground(Color.orange);
      }

   }

   public void hideHighlight() {
      this.firstInput.setForeground(Color.black);
      this.secondInput.setForeground(Color.black);
      this.output.setForeground(Color.black);
   }

   public void highlight(int var1) {
      switch(var1) {
      case 0:
         this.firstInput.setForeground(Color.blue);
         break;
      case 1:
         this.secondInput.setForeground(Color.blue);
         break;
      case 2:
         this.output.setForeground(Color.blue);
      }

   }

   public String getValueAsString(int var1) {
      switch(var1) {
      case 0:
         return this.firstInput.getText();
      case 1:
         return this.secondInput.getText();
      case 2:
         return this.output.getText();
      default:
         return "";
      }
   }

   public void reset() {
      this.firstInput.setText(this.translateValueToString(this.nullValue));
      this.secondInput.setText(this.translateValueToString(this.nullValue));
      this.output.setText(this.translateValueToString(this.nullValue));
      this.hideFlash();
      this.hideHighlight();
   }

   public Point getCoordinates(int var1) {
      Point var2 = this.getLocation();
      switch(var1) {
      case 0:
         return new Point((int)(var2.getX() + this.firstInput.getLocation().getX()), (int)(var2.getY() + this.firstInput.getLocation().getY()));
      case 1:
         return new Point((int)(var2.getX() + this.secondInput.getLocation().getX()), (int)(var2.getY() + this.secondInput.getLocation().getY()));
      case 2:
         return new Point((int)(var2.getX() + this.output.getLocation().getX()), (int)(var2.getY() + this.output.getLocation().getY()));
      default:
         return null;
      }
   }

   public void setValueAt(int var1, short var2) {
      String var3 = this.translateValueToString(var2);
      switch(var1) {
      case 0:
         this.firstInput.setText(var3);
         break;
      case 1:
         this.secondInput.setText(var3);
         break;
      case 2:
         this.output.setText(var3);
      }

   }

   public void paintComponent(Graphics var1) {
      Graphics2D var2 = (Graphics2D)var1;
      var2.setPaint(Color.black);
      var2.setStroke(wideStroke);
      var2.draw(new Double(18.0D, 60.0D, 142.0D, 60.0D));
      var2.setStroke(regularStroke);
   }

   public void setNumericFormat(int var1) {
   }

   private void jbInit() {
      this.setLayout((LayoutManager)null);
      this.firstInput.setHorizontalAlignment(4);
      this.firstInput.setBounds(new Rectangle(18, 8, 124, 19));
      this.firstInput.setBackground(UIManager.getColor("Button.background"));
      this.firstInput.setFont(Utilities.valueFont);
      this.command.setFont(Utilities.bigLabelsFont);
      this.command.setHorizontalAlignment(0);
      this.command.setBounds(new Rectangle(2, 34, 13, 19));
      this.command.setBackground(UIManager.getColor("Button.background"));
      this.command.setBorder((Border)null);
      this.secondInput.setHorizontalAlignment(4);
      this.secondInput.setBounds(new Rectangle(18, 34, 124, 19));
      this.secondInput.setBackground(UIManager.getColor("Button.background"));
      this.secondInput.setFont(new Font("Courier New", 0, 12));
      this.output.setHorizontalAlignment(4);
      this.output.setBounds(new Rectangle(18, 70, 124, 19));
      this.output.setBackground(UIManager.getColor("Button.background"));
      this.output.setFont(Utilities.valueFont);
      this.add(this.secondInput, (Object)null);
      this.add(this.firstInput, (Object)null);
      this.add(this.output, (Object)null);
      this.add(this.command, (Object)null);
   }
}
