package HackGUI;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.ComputerPartEventListener;
import Hack.ComputerParts.RegisterGUI;
import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class RegisterComponent extends JPanel implements RegisterGUI {
   protected JLabel registerName = new JLabel();
   protected JTextField registerValue = new JTextField();
   private Vector listeners = new Vector();
   private Vector errorEventListeners = new Vector();
   protected short value = 0;
   protected String oldValue;
   protected int dataFormat = 0;
   protected short nullValue;
   protected boolean hideNullValue;

   public RegisterComponent() {
      this.registerValue.setText(this.translateValueToString(this.value));
      this.jbInit();
      this.setVisible(true);
   }

   public void setNullValue(short var1, boolean var2) {
      this.nullValue = var1;
      this.hideNullValue = var2;
      if (this.value == this.nullValue && var2) {
         this.oldValue = "";
      }

   }

   public void addListener(ComputerPartEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(ComputerPartEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(int var1, short var2) {
      ComputerPartEvent var3 = new ComputerPartEvent(this, 0, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ComputerPartEventListener)this.listeners.elementAt(var4)).valueChanged(var3);
      }

   }

   public void notifyListeners() {
      new ComputerPartEvent(this);

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((ComputerPartEventListener)this.listeners.elementAt(var2)).guiGainedFocus();
      }

   }

   public void addErrorListener(ErrorEventListener var1) {
      this.errorEventListeners.addElement(var1);
   }

   public void removeErrorListener(ErrorEventListener var1) {
      this.errorEventListeners.removeElement(var1);
   }

   public void notifyErrorListeners(String var1) {
      ErrorEvent var2 = new ErrorEvent(this, var1);

      for(int var3 = 0; var3 < this.errorEventListeners.size(); ++var3) {
         ((ErrorEventListener)this.errorEventListeners.elementAt(var3)).errorOccured(var2);
      }

   }

   public void enableUserInput() {
      this.registerValue.setEnabled(true);
   }

   public void disableUserInput() {
      this.registerValue.setEnabled(false);
   }

   protected String translateValueToString(short var1) {
      if (this.hideNullValue) {
         return var1 == this.nullValue ? "" : Format.translateValueToString(var1, this.dataFormat);
      } else {
         return Format.translateValueToString(var1, this.dataFormat);
      }
   }

   public void setValueAt(int var1, short var2) {
      String var3 = this.translateValueToString(var2);
      this.value = var2;
      this.registerValue.setText(var3);
   }

   public void reset() {
      this.value = this.nullValue;
      if (this.hideNullValue) {
         this.oldValue = "";
      }

      this.registerValue.setText(this.translateValueToString(this.nullValue));
      this.hideFlash();
      this.hideHighlight();
   }

   public Point getCoordinates(int var1) {
      Point var2 = this.getLocation();
      return new Point((int)var2.getX() + this.registerValue.getX(), (int)var2.getY() + this.registerValue.getY());
   }

   public void hideHighlight() {
      this.registerValue.setForeground(Color.black);
   }

   public void highlight(int var1) {
      this.registerValue.setForeground(Color.blue);
   }

   public void flash(int var1) {
      this.registerValue.setBackground(Color.orange);
   }

   public void hideFlash() {
      this.registerValue.setBackground(Color.white);
   }

   public void setEnabledRange(int var1, int var2, boolean var3) {
   }

   public String getValueAsString(int var1) {
      return this.registerValue.getText();
   }

   public void setNumericFormat(int var1) {
      this.dataFormat = var1;
      this.registerValue.setText(Format.translateValueToString(this.value, var1));
   }

   private void valueChanged() {
      String var1 = this.registerValue.getText();
      if (!var1.equals(this.oldValue)) {
         try {
            this.value = Format.translateValueToShort(var1, this.dataFormat);
            this.notifyListeners(0, this.value);
            this.oldValue = var1;
         } catch (NumberFormatException var3) {
            this.notifyErrorListeners("Illegal value");
            this.registerValue.setText(this.translateValueToString(this.value));
         }
      }

   }

   public void setName(String var1) {
      this.registerName.setText(var1);
   }

   private void jbInit() {
      this.registerValue.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            RegisterComponent.this.registerValue_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            RegisterComponent.this.registerValue_focusLost(var1);
         }
      });
      this.registerName.setFont(Utilities.labelsFont);
      this.registerName.setBounds(new Rectangle(8, 3, 41, 18));
      this.setLayout((LayoutManager)null);
      this.registerValue.setFont(Utilities.valueFont);
      this.registerValue.setDisabledTextColor(Color.black);
      this.registerValue.setHorizontalAlignment(4);
      this.registerValue.setBounds(new Rectangle(36, 3, 124, 18));
      this.registerValue.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            RegisterComponent.this.registerValue_actionPerformed(var1);
         }
      });
      this.add(this.registerValue, (Object)null);
      this.add(this.registerName, (Object)null);
      this.setPreferredSize(new Dimension(164, 24));
      this.setSize(164, 24);
      this.setBorder(BorderFactory.createEtchedBorder());
   }

   public void registerValue_focusGained(FocusEvent var1) {
      this.oldValue = this.registerValue.getText();
      this.notifyListeners();
   }

   public void registerValue_focusLost(FocusEvent var1) {
      this.valueChanged();
   }

   public void registerValue_actionPerformed(ActionEvent var1) {
      this.valueChanged();
   }
}
