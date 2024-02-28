package HackGUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class MouseOverJButton extends JButton implements MouseListener {
   public MouseOverJButton() {
      this.setBorder((Border)null);
      this.addMouseListener(this);
   }

   public void mouseEntered(MouseEvent var1) {
      this.setBorder(BorderFactory.createRaisedBevelBorder());
   }

   public void mouseExited(MouseEvent var1) {
      this.setBorder((Border)null);
   }

   public void mouseClicked(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }
}
