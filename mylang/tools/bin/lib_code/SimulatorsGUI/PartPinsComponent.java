package SimulatorsGUI;

import Hack.HardwareSimulator.HardwareSimulator;
import Hack.HardwareSimulator.PartPinInfo;
import Hack.HardwareSimulator.PartPinsGUI;
import HackGUI.Format;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class PartPinsComponent extends PinsComponent implements PartPinsGUI {
   private PartPinInfo[] partPins = new PartPinInfo[0];
   private String[] valuesStr = new String[0];
   private PartPinsComponent.PartPinsTableCellRenderer renderer = new PartPinsComponent.PartPinsTableCellRenderer();
   private boolean isEnabled = true;
   private JLabel partNameLbl = new JLabel();

   public PartPinsComponent() {
      this.pinsTable.setDefaultRenderer(this.pinsTable.getColumnClass(0), this.renderer);
      this.jbInit();
   }

   public void setPartName(String var1) {
      this.partNameLbl.setText(var1);
   }

   public void enableUserInput() {
      this.isEnabled = true;
   }

   public void disableUserInput() {
      this.isEnabled = false;
   }

   protected int getValueColumn() {
      return 2;
   }

   protected TableModel getTableModel() {
      return new PartPinsComponent.PartPinsTableModel();
   }

   public void pinValueChanged(PinValueEvent var1) {
      this.pinsTable.setEnabled(true);
      if (var1.getIsOk()) {
         this.valuesStr[this.pinsTable.getSelectedRow()] = var1.getValueStr();
         this.partPins[this.pinsTable.getSelectedRow()].value = Format.translateValueToShort(var1.getValueStr(), this.dataFormat);
      }

      this.notifyListeners(this.pinsTable.getSelectedRow(), Format.translateValueToShort(var1.getValueStr(), this.dataFormat));
   }

   public Point getCoordinates(int var1) {
      JScrollBar var2 = this.scrollPane.getVerticalScrollBar();
      Rectangle var3 = this.pinsTable.getCellRect(var1, 2, true);
      this.pinsTable.scrollRectToVisible(var3);
      return new Point((int)(var3.getX() + this.topLevelLocation.getX()), (int)(var3.getY() + this.topLevelLocation.getY() - (double)var2.getValue()));
   }

   public void setContents(Vector var1) {
      this.partPins = new PartPinInfo[var1.size()];
      this.valuesStr = new String[var1.size()];
      var1.toArray(this.partPins);

      for(int var2 = 0; var2 < this.partPins.length; ++var2) {
         this.valuesStr[var2] = Format.translateValueToString(this.partPins[var2].value, this.dataFormat);
      }

      this.pinsTable.clearSelection();
      this.pinsTable.revalidate();
      this.repaint();
   }

   public String getValueAsString(int var1) {
      return this.valuesStr[var1];
   }

   public void setValueAt(int var1, short var2) {
      this.partPins[var1].value = var2;
      this.valuesStr[var1] = Format.translateValueToString(var2, this.dataFormat);
      this.repaint();
   }

   public void reset() {
      this.pinsTable.clearSelection();
      this.repaint();
      this.hideFlash();
      this.hideHighlight();
   }

   protected void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 2; ++var2) {
         var1 = this.pinsTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(20);
         } else if (var2 == 1) {
            var1.setPreferredWidth(20);
         } else if (var2 == 2) {
            var1.setPreferredWidth(180);
         }
      }

   }

   private void jbInit() {
      this.partNameLbl.setFont(Utilities.bigLabelsFont);
      this.partNameLbl.setHorizontalAlignment(0);
      this.partNameLbl.setText("keyboard");
      this.partNameLbl.setForeground(Color.black);
      this.partNameLbl.setBounds(new Rectangle(62, 10, 102, 21));
      this.add(this.partNameLbl, (Object)null);
   }

   class PartPinsTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setEnabled(var1 == null || var1.isEnabled());
         if (var6 != 0 && var6 != 1) {
            this.setHorizontalAlignment(4);

            for(int var7 = 0; var7 < PartPinsComponent.this.highlightIndex.size(); ++var7) {
               if (var5 == (Integer)PartPinsComponent.this.highlightIndex.elementAt(var7)) {
                  this.setForeground(Color.blue);
                  break;
               }

               this.setForeground((Color)null);
            }

            if (var5 == PartPinsComponent.this.flashIndex) {
               this.setBackground(Color.orange);
            } else {
               this.setBackground((Color)null);
            }
         } else {
            this.setHorizontalAlignment(0);
            this.setForeground((Color)null);
            this.setBackground((Color)null);
         }

         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }
   }

   class PartPinsTableModel extends AbstractTableModel {
      String[] columnNames = new String[]{"Part pin", "Gate pin", "Value"};

      public int getColumnCount() {
         return this.columnNames.length;
      }

      public int getRowCount() {
         return PartPinsComponent.this.partPins == null ? 0 : PartPinsComponent.this.partPins.length;
      }

      public String getColumnName(int var1) {
         return this.columnNames[var1];
      }

      public Object getValueAt(int var1, int var2) {
         String var3 = "";
         if (var2 == 0) {
            var3 = HardwareSimulator.getFullPinName(PartPinsComponent.this.partPins[var1].partPinName, PartPinsComponent.this.partPins[var1].partPinSubBus);
         } else if (var2 == 1) {
            var3 = HardwareSimulator.getFullPinName(PartPinsComponent.this.partPins[var1].gatePinName, PartPinsComponent.this.partPins[var1].gatePinSubBus);
         } else if (var2 == 2) {
            var3 = PartPinsComponent.this.valuesStr[var1];
         }

         return var3;
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public void setValueAt(Object var1, int var2, int var3) {
         String var4 = ((String)var1).trim();

         try {
            PartPinsComponent.this.valuesStr[var2] = var4;
            PartPinsComponent.this.partPins[var2].value = Format.translateValueToShort(var4, PartPinsComponent.this.dataFormat);
            PartPinsComponent.this.notifyListeners((short)var2, PartPinsComponent.this.partPins[var2].value);
         } catch (NumberFormatException var6) {
            PartPinsComponent.this.notifyErrorListeners("Illegal value");
            PartPinsComponent.this.valuesStr[var2] = Format.translateValueToString(PartPinsComponent.this.partPins[var2].value, PartPinsComponent.this.dataFormat);
         }

         PartPinsComponent.this.repaint();
      }
   }
}
