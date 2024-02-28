package HackGUI;

import Hack.ComputerParts.BusGUI;
import Hack.Controller.HackController;
import java.awt.Color;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.Border;

public class BusComponent extends JPanel implements ActionListener, BusGUI {
   private static final int MIN_MS = 10;
   private static final int MAX_MS = 40;
   private static final double MIN_STEP_LENGTH = 3.0D;
   private static final double MAX_STEP_LENGTH = 11.0D;
   private static final int HEIGHT = 22;
   private static final int WIDTH = 128;
   protected JTextField txtField = new JTextField();
   private Timer timer = new Timer(1000, this);
   protected Border txtBorder;
   private int[] delays;
   private double[] stepLengths;
   private int counter = 0;
   private double dx = 0.0D;
   private double dy = 0.0D;
   private double x = 0.0D;
   private double y = 0.0D;
   private double currentStepLength;

   public BusComponent() {
      byte var1 = 5;
      float[] var2 = HackController.SPEED_FUNCTION;
      this.stepLengths = new double[var1];
      this.delays = new int[var1];

      for(int var3 = 0; var3 < var1; ++var3) {
         this.stepLengths[var3] = (double)var2[var3] * 8.0D + 3.0D;
         this.delays[var3] = (int)(40.0D - (double)var2[var3] * 30.0D);
      }

      this.setSpeed(3);
      this.jbInit();
   }

   public synchronized void actionPerformed(ActionEvent var1) {
      this.x += this.dx;
      this.y += this.dy;
      this.txtField.setLocation((int)this.x, (int)this.y);
      --this.counter;
      if (this.counter == 0) {
         this.timer.stop();
         this.txtField.setVisible(false);
         this.notify();
      }

   }

   public synchronized void move(Point var1, Point var2, String var3) {
      this.txtField.setText(var3);
      this.x = var1.getX() - 2.0D;
      this.y = var1.getY() - 2.0D;
      this.txtField.setLocation((int)this.x, (int)this.y);
      this.txtField.setVisible(true);
      int var4 = (int)(var2.getX() - var1.getX()) + 2;
      int var5 = (int)(var2.getY() - var1.getY()) + 2;
      int var6 = Math.abs(var4);
      int var7 = Math.abs(var5);
      this.dy = this.currentStepLength * (double)var7 / (double)(var6 + var7);
      this.dx = this.currentStepLength - this.dy;
      this.counter = (int)((double)var6 / this.dx);
      if (var4 < 0) {
         this.dx = -this.dx;
      }

      if (var5 < 0) {
         this.dy = -this.dy;
      }

      this.timer.start();

      try {
         this.wait();
      } catch (InterruptedException var9) {
      }

   }

   public void setSpeed(int var1) {
      this.timer.setDelay(this.delays[var1 - 1]);
      this.currentStepLength = this.stepLengths[var1 - 1];
   }

   public void reset() {
   }

   public void setBusFont(Font var1) {
      this.txtField.setFont(var1);
   }

   public void setBusSize(Rectangle var1) {
      this.txtField.setBounds(var1);
   }

   private void jbInit() {
      this.txtBorder = BorderFactory.createMatteBorder(4, 4, 4, 4, Color.orange);
      this.txtField.setBounds(new Rectangle(10, 8, 128, 22));
      this.txtField.setBackground(Color.white);
      this.txtField.setEnabled(false);
      this.txtField.setBorder(this.txtBorder);
      this.txtField.setDisabledTextColor(Color.black);
      this.txtField.setEditable(false);
      this.txtField.setHorizontalAlignment(4);
      this.txtField.setFont(Utilities.valueFont);
      this.setLayout((LayoutManager)null);
      this.add(this.txtField, (Object)null);
      this.txtField.setVisible(false);
      this.setOpaque(false);
   }
}
