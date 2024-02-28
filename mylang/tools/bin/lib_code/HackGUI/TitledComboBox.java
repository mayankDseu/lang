package HackGUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TitledComboBox extends JPanel {
   private static final int TOTAL_HEIGHT = 37;
   private static final int COMBO_HEIGHT = 22;
   private JComboBox combo;
   private JLabel title;
   private LinkedList listeners;

   public TitledComboBox(String var1, String var2, String[] var3, int var4) {
      this.title = new JLabel(var1);
      this.combo = new JComboBox(var3);
      this.combo.setToolTipText(var2);
      Dimension var5 = new Dimension(var4, 37);
      this.setMaximumSize(var5);
      this.setPreferredSize(var5);
      this.setSize(var5);
      this.setLayout(new BorderLayout());
      this.add(this.title, "North");
      this.add(this.combo, "South");
      this.combo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TitledComboBox.this.notifyListeners(var1);
         }
      });
      this.title.setFont(Utilities.thinLabelsFont);
      this.combo.setFont(Utilities.thinLabelsFont);
      this.combo.setPreferredSize(new Dimension(var4, 22));
      this.listeners = new LinkedList();
   }

   public void addActionListener(ActionListener var1) {
      this.listeners.add(var1);
   }

   private void notifyListeners(ActionEvent var1) {
      Iterator var2 = this.listeners.iterator();

      while(var2.hasNext()) {
         ((ActionListener)var2.next()).actionPerformed(var1);
      }

   }

   public boolean isSelectedIndex(int var1) {
      return this.combo.getSelectedIndex() == var1;
   }

   public boolean isSelectedItem(String var1) {
      return this.combo.getSelectedItem().equals(var1);
   }

   public int getSelectedIndex() {
      return this.combo.getSelectedIndex();
   }

   public void setSelectedIndex(int var1) {
      this.combo.setSelectedIndex(var1);
   }

   public void setEnabled(boolean var1) {
      this.combo.setEnabled(var1);
      this.title.setEnabled(var1);
   }
}
