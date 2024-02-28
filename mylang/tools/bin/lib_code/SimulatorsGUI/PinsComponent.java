package SimulatorsGUI;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.ComputerPartEventListener;
import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import Hack.Gates.PinInfo;
import Hack.HardwareSimulator.PinsGUI;
import HackGUI.Format;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class PinsComponent extends JPanel implements PinsGUI, MouseListener, PinValueListener {
   protected BinaryComponent binary;
   protected JTable pinsTable;
   private PinInfo[] pins;
   private String[] valueStr;
   protected int dataFormat = 0;
   private Vector listeners;
   private Vector errorEventListeners;
   protected JScrollPane scrollPane;
   protected int flashIndex = -1;
   protected Vector highlightIndex;
   protected Point topLevelLocation;
   private PinsComponent.PinsTableCellRenderer renderer = new PinsComponent.PinsTableCellRenderer();
   private JLabel nameLbl = new JLabel();
   private boolean isEnabled = true;
   protected short nullValue;
   protected boolean hideNullValue;
   protected int startEnabling;
   protected int endEnabling;
   private int lastSelectedRow;

   public PinsComponent() {
      JTextField var1 = new JTextField();
      var1.setFont(Utilities.bigBoldValueFont);
      var1.setBorder((Border)null);
      DefaultCellEditor var2 = new DefaultCellEditor(var1);
      this.startEnabling = -1;
      this.endEnabling = -1;
      this.pins = new PinInfo[0];
      this.valueStr = new String[0];
      this.listeners = new Vector();
      this.errorEventListeners = new Vector();
      this.highlightIndex = new Vector();
      this.binary = new BinaryComponent();
      this.pinsTable = new JTable(this.getTableModel());
      this.pinsTable.setDefaultRenderer(this.pinsTable.getColumnClass(0), this.renderer);
      this.pinsTable.getColumnModel().getColumn(this.getValueColumn()).setCellEditor(var2);
      this.jbInit();
   }

   public void setNullValue(short var1, boolean var2) {
      this.nullValue = var1;
      this.hideNullValue = var2;
   }

   public void enableUserInput() {
      this.isEnabled = true;
   }

   public void disableUserInput() {
      this.isEnabled = false;
   }

   public void setDimmed(boolean var1) {
      this.pinsTable.setEnabled(!var1);
   }

   protected int getValueColumn() {
      return 1;
   }

   protected TableModel getTableModel() {
      return new PinsComponent.PinsTableModel();
   }

   public void setPinsName(String var1) {
      this.nameLbl.setText(var1);
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

   public void addListener(ComputerPartEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeListener(ComputerPartEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners(int var1, short var2) {
      ComputerPartEvent var3 = new ComputerPartEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ComputerPartEventListener)this.listeners.elementAt(var4)).valueChanged(var3);
      }

   }

   public void notifyListeners() {
      for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
         ((ComputerPartEventListener)this.listeners.elementAt(var1)).guiGainedFocus();
      }

   }

   public void pinValueChanged(PinValueEvent var1) {
      this.pinsTable.setEnabled(true);
      if (var1.getIsOk()) {
         this.pins[this.lastSelectedRow].value = this.translateValueToShort(var1.getValueStr());
         this.valueStr[this.lastSelectedRow] = this.translateValueToString(this.pins[this.lastSelectedRow].value, this.pins[this.lastSelectedRow].width);
      }

      this.notifyListeners(this.lastSelectedRow, this.pins[this.lastSelectedRow].value);
   }

   public void flash(int var1) {
      this.flashIndex = var1;
      Utilities.tableCenterScroll(this, this.pinsTable, var1);
   }

   public void hideFlash() {
      this.flashIndex = -1;
      this.repaint();
   }

   public void highlight(int var1) {
      this.highlightIndex.addElement(new Integer(var1));
      this.repaint();
   }

   public void hideHighlight() {
      this.highlightIndex.removeAllElements();
      this.repaint();
   }

   public void setEnabledRange(int var1, int var2, boolean var3) {
      this.startEnabling = var1;
      this.endEnabling = var2;
   }

   public String getValueAsString(int var1) {
      return this.valueStr[var1];
   }

   public void setValueAt(int var1, short var2) {
      this.pins[var1].value = var2;
      this.valueStr[var1] = this.translateValueToString(var2, this.pins[var1].width);
   }

   public Point getCoordinates(int var1) {
      JScrollBar var2 = this.scrollPane.getVerticalScrollBar();
      Rectangle var3 = this.pinsTable.getCellRect(var1, 1, true);
      this.pinsTable.scrollRectToVisible(var3);
      return new Point((int)(var3.getX() + this.topLevelLocation.getX()), (int)(var3.getY() + this.topLevelLocation.getY() - (double)var2.getValue()));
   }

   public Point getLocation(int var1) {
      Rectangle var2 = this.pinsTable.getCellRect(var1, 0, true);
      Point var3 = Utilities.getTopLevelLocation(this, this.pinsTable);
      return new Point((int)(var2.getX() + var3.getX()), (int)(var2.getY() + var3.getY()));
   }

   public void reset() {
      this.pinsTable.clearSelection();
      this.repaint();
      this.hideFlash();
      this.hideHighlight();
   }

   public void setContents(PinInfo[] var1) {
      this.pins = new PinInfo[var1.length];
      this.valueStr = new String[var1.length];
      System.arraycopy(var1, 0, this.pins, 0, var1.length);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.valueStr[var2] = this.translateValueToString(var1[var2].value, var1[var2].width);
      }

      this.pinsTable.clearSelection();
      this.pinsTable.revalidate();
      this.repaint();
   }

   public void mouseClicked(MouseEvent var1) {
      if (this.isEnabled && (var1.getModifiers() & 16) != 0) {
         if (this.binary.isVisible()) {
            this.binary.hideBinary();
            this.pinsTable.setEnabled(true);
            this.pinsTable.changeSelection(this.pinsTable.rowAtPoint(var1.getPoint()), this.pinsTable.columnAtPoint(var1.getPoint()), false, false);
            this.pinsTable.grabFocus();
         }

         if (var1.getClickCount() == 2 && this.dataFormat == 2) {
            this.pinsTable.setEnabled(false);
            this.binary.setLocation((int)this.getLocation(this.pinsTable.getSelectedRow() + 1).getX(), (int)this.getLocation(this.pinsTable.getSelectedRow() + 1).getY());
            this.binary.setValue(this.pins[this.pinsTable.getSelectedRow()].value);
            this.binary.setNumOfBits(this.pins[this.pinsTable.getSelectedRow()].width);
            this.binary.showBinary();
         }
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

   public void setTopLevelLocation(Component var1) {
      this.topLevelLocation = Utilities.getTopLevelLocation(var1, this.pinsTable);
   }

   protected void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 2; ++var2) {
         var1 = this.pinsTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(116);
         } else {
            var1.setPreferredWidth(124);
         }
      }

   }

   public void setNumericFormat(int var1) {
      this.dataFormat = var1;

      for(int var2 = 0; var2 < this.pins.length; ++var2) {
         this.valueStr[var2] = this.translateValueToString(this.pins[var2].value, this.pins[var2].width);
      }

      this.repaint();
   }

   protected short translateValueToShort(String var1) {
      return Format.translateValueToShort(var1, this.dataFormat);
   }

   protected String translateValueToString(short var1, int var2) {
      String var3 = null;
      if (var1 == this.nullValue && this.hideNullValue) {
         var3 = "";
      } else {
         var3 = Format.translateValueToString(var1, this.dataFormat);
         if (this.dataFormat == 2) {
            var3 = var3.substring(var3.length() - var2, var3.length());
         }
      }

      return var3;
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.pinsTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   public int getTableWidth() {
      return 241;
   }

   private void jbInit() {
      this.pinsTable.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            PinsComponent.this.pinsTable_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            PinsComponent.this.pinsTable_focusLost(var1);
         }
      });
      this.pinsTable.addMouseListener(this);
      this.pinsTable.getTableHeader().setReorderingAllowed(false);
      this.pinsTable.getTableHeader().setResizingAllowed(false);
      this.setLayout((LayoutManager)null);
      this.scrollPane = new JScrollPane(this.pinsTable);
      this.scrollPane.setLocation(0, 27);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.binary.setSize(new Dimension(240, 52));
      this.binary.setLayout((LayoutManager)null);
      this.binary.setVisible(false);
      this.binary.addListener(this);
      this.determineColumnWidth();
      this.nameLbl.setText("Name :");
      this.nameLbl.setBounds(new Rectangle(3, 3, 102, 21));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.pinsTable.setFont(Utilities.valueFont);
      this.add(this.binary, (Object)null);
      this.add(this.scrollPane, (Object)null);
      this.add(this.nameLbl, (Object)null);
   }

   public void pinsTable_focusGained(FocusEvent var1) {
      this.notifyListeners();
   }

   public void pinsTable_focusLost(FocusEvent var1) {
      this.lastSelectedRow = this.pinsTable.getSelectedRow();
      this.pinsTable.clearSelection();
   }

   class PinsTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setEnabled(var1 == null || var1.isEnabled());
         if (var6 == 0) {
            this.setHorizontalAlignment(0);
            this.setForeground((Color)null);
            this.setBackground((Color)null);
         } else {
            this.setHorizontalAlignment(4);

            for(int var7 = 0; var7 < PinsComponent.this.highlightIndex.size(); ++var7) {
               if (var5 == (Integer)PinsComponent.this.highlightIndex.elementAt(var7)) {
                  this.setForeground(Color.blue);
                  break;
               }

               this.setForeground((Color)null);
            }

            if (var5 == PinsComponent.this.flashIndex) {
               this.setBackground(Color.orange);
            } else {
               this.setBackground((Color)null);
            }
         }

         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }
   }

   class PinsTableModel extends AbstractTableModel {
      String[] columnNames = new String[]{"Name", "Value"};

      public int getColumnCount() {
         return this.columnNames.length;
      }

      public int getRowCount() {
         return PinsComponent.this.pins.length;
      }

      public String getColumnName(int var1) {
         return this.columnNames[var1];
      }

      public Object getValueAt(int var1, int var2) {
         return var2 == 0 ? PinsComponent.this.pins[var1].name + (PinsComponent.this.pins[var1].width > 1 ? "[" + PinsComponent.this.pins[var1].width + "]" : "") : PinsComponent.this.valueStr[var1];
      }

      public boolean isCellEditable(int var1, int var2) {
         return PinsComponent.this.isEnabled && var2 == 1 && PinsComponent.this.dataFormat != 2 && (PinsComponent.this.endEnabling == -1 || var1 >= PinsComponent.this.startEnabling && var1 <= PinsComponent.this.endEnabling);
      }

      public void setValueAt(Object var1, int var2, int var3) {
         String var4 = (String)var1;
         if (!PinsComponent.this.valueStr[var2].equals(var4)) {
            try {
               PinsComponent.this.valueStr[var2] = var4;
               if (var4.equals("") && PinsComponent.this.hideNullValue) {
                  PinsComponent.this.pins[var2].value = PinsComponent.this.nullValue;
               } else {
                  PinsComponent.this.pins[var2].value = Format.translateValueToShort(var4, PinsComponent.this.dataFormat);
               }

               PinsComponent.this.notifyListeners((short)var2, PinsComponent.this.pins[var2].value);
            } catch (NumberFormatException var6) {
               PinsComponent.this.notifyErrorListeners("Illegal value");
               PinsComponent.this.valueStr[var2] = Format.translateValueToString(PinsComponent.this.pins[var2].value, PinsComponent.this.dataFormat);
            }

            PinsComponent.this.repaint();
         }

      }
   }
}
