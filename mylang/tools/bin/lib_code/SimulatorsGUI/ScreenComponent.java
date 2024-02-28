package SimulatorsGUI;

import Hack.CPUEmulator.ScreenGUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ScreenComponent extends JPanel implements ScreenGUI, ActionListener {
   private static final int ANIMATION_CLOCK_INTERVALS = 50;
   private static final int STATIC_CLOCK_INTERVALS = 500;
   private short[] data;
   private boolean redraw = true;
   private int[] x;
   private int[] y;
   protected Timer timer;

   public ScreenComponent() {
      this.setOpaque(true);
      this.setBackground(Color.white);
      this.setBorder(BorderFactory.createEtchedBorder());
      Insets var1 = this.getBorder().getBorderInsets(this);
      int var2 = var1.left + var1.right;
      int var3 = var1.top + var1.bottom;
      this.setPreferredSize(new Dimension(512 + var2, 256 + var3));
      this.setSize(512 + var2, 256 + var3);
      this.data = new short[131072];
      this.x = new int[131072];
      this.y = new int[131072];
      this.x[0] = var1.left;
      this.y[0] = var1.top;

      for(int var4 = 1; var4 < 131072; ++var4) {
         this.x[var4] = this.x[var4 - 1] + 16;
         this.y[var4] = this.y[var4 - 1];
         if (this.x[var4] == 512 + var1.left) {
            this.x[var4] = var1.left;
            int var10002 = this.y[var4]++;
         }
      }

      this.timer = new Timer(500, this);
      this.timer.start();
   }

   public void setValueAt(int var1, short var2) {
      this.data[var1] = var2;
      this.redraw = true;
   }

   public void setContents(short[] var1) {
      this.data = var1;
      this.redraw = true;
   }

   public void reset() {
      for(int var1 = 0; var1 < this.data.length; ++var1) {
         this.data[var1] = 0;
      }

      this.redraw = true;
   }

   public void refresh() {
      if (this.redraw) {
         this.repaint();
         this.redraw = false;
      }

   }

   public void startAnimation() {
      this.timer.setDelay(50);
   }

   public void stopAnimation() {
      this.timer.setDelay(500);
   }

   public void actionPerformed(ActionEvent var1) {
      if (this.redraw) {
         this.repaint();
         this.redraw = false;
      }

   }

   public void paintComponent(Graphics var1) {
      super.paintComponent(var1);

      for(int var2 = 0; var2 < 131072; ++var2) {
         if (this.data[var2] != 0) {
            if (this.data[var2] == '\uffff') {
               var1.drawLine(this.x[var2], this.y[var2], this.x[var2] + 15, this.y[var2]);
            } else {
               short var3 = this.data[var2];

               for(int var4 = 0; var4 < 16; ++var4) {
                  if ((var3 & 1) == 1) {
                     var1.drawLine(this.x[var2] + var4, this.y[var2], this.x[var2] + var4, this.y[var2]);
                  }

                  var3 = (short)(var3 >> 1);
               }
            }
         }
      }

   }
}
