package SimulatorsGUI;

import Hack.Gates.GatesPanelGUI;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import javax.swing.JPanel;

public class GatesPanel implements GatesPanelGUI {
   private JPanel nullLayoutGatesPanel = new JPanel();
   private JPanel flowLayoutGatesPanel = new JPanel();
   private boolean flowLayout = false;

   public GatesPanel() {
      this.nullLayoutGatesPanel.setLayout((LayoutManager)null);
      this.flowLayoutGatesPanel.setLayout(new FlowLayout(1, 1, 1));
   }

   public void addGateComponent(Component var1) {
      this.flowLayoutGatesPanel.add(var1);
      if (this.flowLayout) {
         this.flowLayoutGatesPanel.revalidate();
         this.flowLayoutGatesPanel.repaint();
      } else {
         Component[] var2 = this.nullLayoutGatesPanel.getComponents();

         for(int var3 = 0; var3 < var2.length; ++var3) {
            Rectangle var4 = var2[var3].getBounds();
            int var5 = (int)var4.getX();
            int var6 = (int)var4.getY();
            int var7 = (int)(var4.getX() + var4.getWidth() - 1.0D);
            int var8 = (int)(var4.getY() + var4.getHeight() - 1.0D);
            if (var1.getY() <= var8 && var1.getX() <= var7 && var1.getY() + var1.getHeight() - 1 >= var6 && var1.getX() + var1.getWidth() - 1 >= var5) {
               this.flowLayout = true;
               break;
            }
         }

         if (!this.flowLayout) {
            this.nullLayoutGatesPanel.add(var1);
            this.nullLayoutGatesPanel.revalidate();
            this.nullLayoutGatesPanel.repaint();
         }
      }

   }

   public void removeGateComponent(Component var1) {
      this.nullLayoutGatesPanel.remove(var1);
      this.flowLayoutGatesPanel.remove(var1);
      this.nullLayoutGatesPanel.revalidate();
      this.flowLayoutGatesPanel.revalidate();
      this.nullLayoutGatesPanel.repaint();
      this.flowLayoutGatesPanel.repaint();
   }

   public void removeAllGateComponents() {
      this.flowLayout = false;
      this.nullLayoutGatesPanel.removeAll();
      this.flowLayoutGatesPanel.removeAll();
      this.nullLayoutGatesPanel.revalidate();
      this.flowLayoutGatesPanel.revalidate();
      this.nullLayoutGatesPanel.repaint();
      this.flowLayoutGatesPanel.repaint();
   }

   public JPanel getGatesPanel() {
      return this.flowLayout ? this.flowLayoutGatesPanel : this.nullLayoutGatesPanel;
   }
}
