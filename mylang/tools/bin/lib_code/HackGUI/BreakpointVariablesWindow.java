package HackGUI;

import Hack.Controller.Breakpoint;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class BreakpointVariablesWindow extends JFrame {
   private JLabel nameLbl = new JLabel();
   private JLabel valueLbl = new JLabel();
   private JTextField nameTxt = new JTextField();
   private JTextField valueTxt = new JTextField();
   private JComboBox nameCombo = new JComboBox();
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private Vector listeners = new Vector();
   private Breakpoint breakpoint;

   public BreakpointVariablesWindow() {
      super("Breakpoint Variables");
      this.jbInit();
   }

   public void addListener(BreakpointChangedListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(BreakpointChangedListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners() {
      BreakpointChangedEvent var1 = new BreakpointChangedEvent(this, this.breakpoint);

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((BreakpointChangedListener)this.listeners.elementAt(var2)).breakpointChanged(var1);
      }

   }

   public void setVariables(String[] var1) {
      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.nameCombo.addItem(var1[var2]);
      }

   }

   public void setBreakpointName(String var1) {
      this.nameTxt.setText(var1);
   }

   public void setBreakpointValue(String var1) {
      this.valueTxt.setText(var1);
   }

   public void setNameCombo(int var1) {
      this.nameCombo.setSelectedIndex(var1);
   }

   public void showWindow() {
      this.nameTxt.requestFocus();
      this.setVisible(true);
   }

   private void jbInit() {
      this.nameLbl.setFont(Utilities.thinLabelsFont);
      this.nameLbl.setText("Name :");
      this.nameLbl.setBounds(new Rectangle(9, 10, 61, 19));
      this.getContentPane().setLayout((LayoutManager)null);
      this.valueLbl.setBounds(new Rectangle(9, 42, 61, 19));
      this.valueLbl.setFont(Utilities.thinLabelsFont);
      this.valueLbl.setText("Value :");
      this.nameTxt.setBounds(new Rectangle(53, 10, 115, 19));
      this.valueTxt.setBounds(new Rectangle(53, 42, 115, 19));
      this.nameCombo.setBounds(new Rectangle(180, 10, 124, 19));
      this.nameCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointVariablesWindow.this.nameCombo_actionPerformed(var1);
         }
      });
      this.okButton.setToolTipText("Ok");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(61, 74, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointVariablesWindow.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(180, 74, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointVariablesWindow.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("Cancel");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.nameLbl, (Object)null);
      this.getContentPane().add(this.valueLbl, (Object)null);
      this.getContentPane().add(this.nameTxt, (Object)null);
      this.getContentPane().add(this.valueTxt, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.getContentPane().add(this.nameCombo, (Object)null);
      this.setSize(320, 160);
      this.setLocation(500, 250);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      this.breakpoint = new Breakpoint(this.nameTxt.getText(), this.valueTxt.getText());
      this.setVisible(false);
      this.notifyListeners();
   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.setVisible(false);
   }

   public void nameCombo_actionPerformed(ActionEvent var1) {
      String var2 = (String)this.nameCombo.getSelectedItem();
      this.nameTxt.setText(var2);
   }
}
