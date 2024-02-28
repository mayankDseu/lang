package SimulatorsGUI;

import Hack.Utilities.Conversions;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class BinaryComponent extends JPanel implements MouseListener, KeyListener {
   private JTextField bit0 = new JTextField(1);
   private JTextField bit1 = new JTextField(1);
   private JTextField bit2 = new JTextField(1);
   private JTextField bit3 = new JTextField(1);
   private JTextField bit4 = new JTextField(1);
   private JTextField bit5 = new JTextField(1);
   private JTextField bit6 = new JTextField(1);
   private JTextField bit7 = new JTextField(1);
   private JTextField bit8 = new JTextField(1);
   private JTextField bit9 = new JTextField(1);
   private JTextField bit10 = new JTextField(1);
   private JTextField bit11 = new JTextField(1);
   private JTextField bit12 = new JTextField(1);
   private JTextField bit13 = new JTextField(1);
   private JTextField bit14 = new JTextField(1);
   private JTextField bit15 = new JTextField(1);
   private JTextField[] bits = new JTextField[16];
   private StringBuffer valueStr;
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/smallok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/smallcancel.gif");
   private Vector listeners = new Vector();
   private boolean isOk = false;
   private Border binaryBorder;
   private int numberOfBits;

   public BinaryComponent() {
      this.jbInit();
   }

   public void addListener(PinValueListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(PinValueListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners() {
      PinValueEvent var1 = new PinValueEvent(this, this.valueStr.toString(), this.isOk);

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((PinValueListener)this.listeners.elementAt(var2)).pinValueChanged(var1);
      }

   }

   public void setNumOfBits(int var1) {
      this.numberOfBits = var1;

      for(int var2 = 0; var2 < this.bits.length; ++var2) {
         if (var2 < this.bits.length - var1) {
            this.bits[var2].setText("");
            this.bits[var2].setBackground(Color.darkGray);
            this.bits[var2].setEnabled(false);
         } else {
            this.bits[var2].setBackground(Color.white);
            this.bits[var2].setEnabled(true);
         }
      }

   }

   public void setValue(short var1) {
      this.valueStr = new StringBuffer(Conversions.decimalToBinary(var1, 16));

      for(int var2 = 0; var2 < this.bits.length; ++var2) {
         this.bits[var2].setText(String.valueOf(this.valueStr.charAt(var2)));
      }

   }

   public short getValue() {
      return (short)Conversions.binaryToInt(this.valueStr.toString());
   }

   private void updateValue() {
      this.valueStr = new StringBuffer(16);

      for(int var2 = 0; var2 < this.bits.length; ++var2) {
         char var1;
         if (this.bits[var2].getText().equals("")) {
            var1 = '0';
         } else {
            var1 = this.bits[var2].getText().charAt(0);
         }

         this.valueStr.append(var1);
      }

   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getClickCount() == 2) {
         JTextField var2 = (JTextField)var1.getSource();
         if (var2.getText().equals("0")) {
            var2.setText("1");
         } else if (var2.getText().equals("1")) {
            var2.setText("0");
         }
      }

   }

   public void keyTyped(KeyEvent var1) {
      JTextField var2 = (JTextField)var1.getSource();
      if (var1.getKeyChar() != '0' && var1.getKeyChar() != '1') {
         if (var1.getKeyChar() == '\n') {
            this.approve();
         } else if (var1.getKeyChar() == 27) {
            this.hideBinary();
         } else {
            var2.selectAll();
            var2.getToolkit().beep();
         }
      } else {
         var2.transferFocus();
         var2.selectAll();
      }

   }

   public void mouseExited(MouseEvent var1) {
   }

   public void mouseEntered(MouseEvent var1) {
   }

   public void mouseReleased(MouseEvent var1) {
   }

   public void mousePressed(MouseEvent var1) {
   }

   public void keyReleased(KeyEvent var1) {
   }

   public void keyPressed(KeyEvent var1) {
   }

   private void jbInit() {
      this.binaryBorder = BorderFactory.createLineBorder(Color.black, 3);
      this.setLayout((LayoutManager)null);
      this.bit0.setFont(Utilities.valueFont);
      this.bit0.setText("0");
      this.bit0.setHorizontalAlignment(4);
      this.bit0.setBounds(new Rectangle(211, 8, 13, 19));
      this.bit0.addMouseListener(this);
      this.bit0.addKeyListener(this);
      this.bit1.setFont(Utilities.valueFont);
      this.bit1.setText("0");
      this.bit1.setHorizontalAlignment(4);
      this.bit1.setBounds(new Rectangle(198, 8, 13, 19));
      this.bit1.addMouseListener(this);
      this.bit1.addKeyListener(this);
      this.bit2.setFont(Utilities.valueFont);
      this.bit2.setText("0");
      this.bit2.setHorizontalAlignment(4);
      this.bit2.setBounds(new Rectangle(185, 8, 13, 19));
      this.bit2.addMouseListener(this);
      this.bit2.addKeyListener(this);
      this.bit3.setFont(Utilities.valueFont);
      this.bit3.setText("0");
      this.bit3.setHorizontalAlignment(4);
      this.bit3.setBounds(new Rectangle(172, 8, 13, 19));
      this.bit3.addMouseListener(this);
      this.bit3.addKeyListener(this);
      this.bit4.setFont(Utilities.valueFont);
      this.bit4.setText("0");
      this.bit4.setHorizontalAlignment(4);
      this.bit4.setBounds(new Rectangle(159, 8, 13, 19));
      this.bit4.addMouseListener(this);
      this.bit4.addKeyListener(this);
      this.bit5.setFont(Utilities.valueFont);
      this.bit5.setText("0");
      this.bit5.setHorizontalAlignment(4);
      this.bit5.setBounds(new Rectangle(146, 8, 13, 19));
      this.bit5.addMouseListener(this);
      this.bit5.addKeyListener(this);
      this.bit6.setFont(Utilities.valueFont);
      this.bit6.setText("0");
      this.bit6.setHorizontalAlignment(4);
      this.bit6.setBounds(new Rectangle(133, 8, 13, 19));
      this.bit6.addMouseListener(this);
      this.bit6.addKeyListener(this);
      this.bit7.setFont(Utilities.valueFont);
      this.bit7.setText("0");
      this.bit7.setHorizontalAlignment(4);
      this.bit7.setBounds(new Rectangle(120, 8, 13, 19));
      this.bit7.addMouseListener(this);
      this.bit7.addKeyListener(this);
      this.bit8.setFont(Utilities.valueFont);
      this.bit8.setText("0");
      this.bit8.setHorizontalAlignment(4);
      this.bit8.setBounds(new Rectangle(107, 8, 13, 19));
      this.bit8.addMouseListener(this);
      this.bit8.addKeyListener(this);
      this.bit9.setFont(Utilities.valueFont);
      this.bit9.setText("0");
      this.bit9.setHorizontalAlignment(4);
      this.bit9.setBounds(new Rectangle(94, 8, 13, 19));
      this.bit9.addMouseListener(this);
      this.bit9.addKeyListener(this);
      this.bit10.setFont(Utilities.valueFont);
      this.bit10.setText("0");
      this.bit10.setHorizontalAlignment(4);
      this.bit10.setBounds(new Rectangle(81, 8, 13, 19));
      this.bit10.addMouseListener(this);
      this.bit10.addKeyListener(this);
      this.bit11.setFont(Utilities.valueFont);
      this.bit11.setText("0");
      this.bit11.setHorizontalAlignment(4);
      this.bit11.setBounds(new Rectangle(68, 8, 13, 19));
      this.bit11.addMouseListener(this);
      this.bit11.addKeyListener(this);
      this.bit12.setFont(Utilities.valueFont);
      this.bit12.setText("0");
      this.bit12.setHorizontalAlignment(4);
      this.bit12.setBounds(new Rectangle(55, 8, 13, 19));
      this.bit12.addMouseListener(this);
      this.bit12.addKeyListener(this);
      this.bit13.setFont(Utilities.valueFont);
      this.bit13.setText("0");
      this.bit13.setHorizontalAlignment(4);
      this.bit13.setBounds(new Rectangle(42, 8, 13, 19));
      this.bit13.addMouseListener(this);
      this.bit13.addKeyListener(this);
      this.bit14.setFont(Utilities.valueFont);
      this.bit14.setText("0");
      this.bit14.setHorizontalAlignment(4);
      this.bit14.setBounds(new Rectangle(29, 8, 13, 19));
      this.bit14.addMouseListener(this);
      this.bit14.addKeyListener(this);
      this.bit15.setFont(Utilities.valueFont);
      this.bit15.setText("0");
      this.bit15.setHorizontalAlignment(4);
      this.bit15.setBounds(new Rectangle(16, 8, 13, 19));
      this.bit15.addMouseListener(this);
      this.bit15.addKeyListener(this);
      this.okButton.setHorizontalTextPosition(0);
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(97, 29, 23, 20));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BinaryComponent.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setHorizontalTextPosition(0);
      this.cancelButton.setIcon(this.cancelIcon);
      this.cancelButton.setBounds(new Rectangle(125, 29, 23, 20));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BinaryComponent.this.cancelButton_actionPerformed(var1);
         }
      });
      this.setBorder(this.binaryBorder);
      this.add(this.bit15, (Object)null);
      this.add(this.bit14, (Object)null);
      this.add(this.bit13, (Object)null);
      this.add(this.bit12, (Object)null);
      this.add(this.bit11, (Object)null);
      this.add(this.bit10, (Object)null);
      this.add(this.bit9, (Object)null);
      this.add(this.bit8, (Object)null);
      this.add(this.bit7, (Object)null);
      this.add(this.bit6, (Object)null);
      this.add(this.bit5, (Object)null);
      this.add(this.bit4, (Object)null);
      this.add(this.bit3, (Object)null);
      this.add(this.bit2, (Object)null);
      this.add(this.bit1, (Object)null);
      this.add(this.bit0, (Object)null);
      this.add(this.cancelButton, (Object)null);
      this.add(this.okButton, (Object)null);
      this.bits[0] = this.bit15;
      this.bits[1] = this.bit14;
      this.bits[2] = this.bit13;
      this.bits[3] = this.bit12;
      this.bits[4] = this.bit11;
      this.bits[5] = this.bit10;
      this.bits[6] = this.bit9;
      this.bits[7] = this.bit8;
      this.bits[8] = this.bit7;
      this.bits[9] = this.bit6;
      this.bits[10] = this.bit5;
      this.bits[11] = this.bit4;
      this.bits[12] = this.bit3;
      this.bits[13] = this.bit2;
      this.bits[14] = this.bit1;
      this.bits[15] = this.bit0;
   }

   private void approve() {
      this.isOk = true;
      this.updateValue();
      this.notifyListeners();
      this.setVisible(false);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      this.approve();
   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.hideBinary();
   }

   public void hideBinary() {
      this.isOk = false;
      this.notifyListeners();
      this.setVisible(false);
   }

   public void showBinary() {
      this.setVisible(true);
      this.bits[16 - this.numberOfBits].grabFocus();
   }
}
