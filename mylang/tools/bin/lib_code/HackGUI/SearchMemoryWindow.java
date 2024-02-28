package HackGUI;

import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class SearchMemoryWindow extends JFrame {
   private JLabel instructionLbl = new JLabel();
   private JTextField rowNumber = new JTextField();
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private JTable table;
   private JPanel tableContainer;

   public SearchMemoryWindow(JPanel var1, JTable var2) {
      super("Go to");
      this.table = var2;
      this.tableContainer = var1;
      this.jbInit();
   }

   public void showWindow() {
      this.setVisible(true);
      this.rowNumber.requestFocus();
   }

   private void jbInit() {
      this.instructionLbl.setFont(Utilities.thinLabelsFont);
      this.instructionLbl.setText("Enter Address :");
      this.instructionLbl.setBounds(new Rectangle(9, 22, 132, 23));
      this.getContentPane().setLayout((LayoutManager)null);
      this.rowNumber.setBounds(new Rectangle(102, 25, 158, 18));
      this.rowNumber.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchMemoryWindow.this.rowNumber_actionPerformed(var1);
         }
      });
      this.okButton.setToolTipText("Ok");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(49, 60, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchMemoryWindow.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(176, 60, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchMemoryWindow.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("Cancel");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.instructionLbl, (Object)null);
      this.getContentPane().add(this.rowNumber, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.setSize(300, 150);
      this.setLocation(250, 250);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      try {
         short var2 = Format.translateValueToShort(this.rowNumber.getText(), 0);
         this.table.setRowSelectionInterval(var2, var2);
         Utilities.tableCenterScroll(this.tableContainer, this.table, var2);
         this.setVisible(false);
      } catch (NumberFormatException var3) {
      } catch (IllegalArgumentException var4) {
      }

   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.setVisible(false);
   }

   public void rowNumber_actionPerformed(ActionEvent var1) {
      this.okButton_actionPerformed(var1);
   }
}
