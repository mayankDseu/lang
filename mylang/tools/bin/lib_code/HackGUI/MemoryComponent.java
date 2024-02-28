package HackGUI;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.ComputerPartEventListener;
import Hack.ComputerParts.MemoryGUI;
import Hack.Events.ClearEvent;
import Hack.Events.ClearEventListener;
import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class MemoryComponent extends JPanel implements MemoryGUI {
   protected static final int DEFAULT_VISIBLE_ROWS = 10;
   public int dataFormat = 0;
   private Vector listeners;
   private Vector clearListeners;
   private Vector errorEventListeners;
   private Vector changeListeners;
   protected JTable memoryTable;
   private MemoryComponent.MemoryTableModel tableModel = new MemoryComponent.MemoryTableModel();
   protected String[] valuesStr;
   protected short[] values;
   protected String[] addresses;
   protected MouseOverJButton searchButton = new MouseOverJButton();
   protected MouseOverJButton clearButton = new MouseOverJButton();
   private ImageIcon searchIcon = new ImageIcon("bin/images/find.gif");
   private ImageIcon clearIcon = new ImageIcon("bin/images/smallnew.gif");
   private SearchMemoryWindow searchWindow;
   protected JScrollPane scrollPane;
   protected Vector highlightIndex;
   protected int flashIndex = -1;
   protected Point topLevelLocation;
   protected JLabel nameLbl = new JLabel();
   protected boolean isEnabled = true;
   protected short nullValue;
   protected boolean hideNullValue;
   protected int startEnabling = -1;
   protected int endEnabling = -1;
   protected boolean grayDisabledRange;

   public MemoryComponent() {
      JTextField var1 = new JTextField();
      var1.setFont(Utilities.bigBoldValueFont);
      var1.setBorder((Border)null);
      DefaultCellEditor var2 = new DefaultCellEditor(var1);
      this.listeners = new Vector();
      this.clearListeners = new Vector();
      this.errorEventListeners = new Vector();
      this.changeListeners = new Vector();
      this.highlightIndex = new Vector();
      this.memoryTable = new JTable(this.getTableModel());
      this.memoryTable.setDefaultRenderer(this.memoryTable.getColumnClass(0), this.getCellRenderer());
      this.memoryTable.getColumnModel().getColumn(this.getValueColumnIndex()).setCellEditor(var2);
      this.memoryTable.setTableHeader((JTableHeader)null);
      this.values = new short[0];
      this.addresses = new String[0];
      this.valuesStr = new String[0];
      this.searchWindow = new SearchMemoryWindow(this, this.memoryTable);
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

   protected int getValueColumnIndex() {
      return 1;
   }

   protected TableModel getTableModel() {
      return new MemoryComponent.MemoryTableModel();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new MemoryComponent.MemoryTableCellRenderer();
   }

   public void setName(String var1) {
      this.nameLbl.setText(var1);
   }

   public void setTopLevelLocation(Component var1) {
      this.topLevelLocation = Utilities.getTopLevelLocation(var1, this.memoryTable);
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
      new ComputerPartEvent(this);

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((ComputerPartEventListener)this.listeners.elementAt(var2)).guiGainedFocus();
      }

   }

   public void addClearListener(ClearEventListener var1) {
      this.clearListeners.addElement(var1);
   }

   public void removeClearListener(ClearEventListener var1) {
      this.clearListeners.removeElement(var1);
   }

   public void notifyClearListeners() {
      ClearEvent var1 = new ClearEvent(this);

      for(int var2 = 0; var2 < this.clearListeners.size(); ++var2) {
         ((ClearEventListener)this.clearListeners.elementAt(var2)).clearRequested(var1);
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

   public void addChangeListener(MemoryChangeListener var1) {
      this.changeListeners.addElement(var1);
   }

   public void removeChangeListener(MemoryChangeListener var1) {
      this.changeListeners.removeElement(var1);
   }

   public void notifyRepaintListeners() {
      for(int var1 = 0; var1 < this.changeListeners.size(); ++var1) {
         ((MemoryChangeListener)this.changeListeners.elementAt(var1)).repaintChange();
      }

   }

   public void notifyRevalidateListeners() {
      for(int var1 = 0; var1 < this.changeListeners.size(); ++var1) {
         ((MemoryChangeListener)this.changeListeners.elementAt(var1)).revalidateChange();
      }

   }

   public void setContents(short[] var1) {
      this.values = new short[var1.length];
      this.addresses = new String[var1.length];
      this.valuesStr = new String[var1.length];
      System.arraycopy(var1, 0, this.values, 0, var1.length);

      for(int var2 = 0; var2 < this.values.length; ++var2) {
         this.addresses[var2] = Format.translateValueToString((short)var2, 0);
         this.valuesStr[var2] = this.translateValueToString(this.values[var2]);
      }

      this.memoryTable.revalidate();
      this.repaint();
      this.notifyRevalidateListeners();
   }

   protected void updateTable(short var1, int var2) {
      this.values[var2] = var1;
      this.valuesStr[var2] = this.translateValueToString(var1);
   }

   public void setValueAt(int var1, short var2) {
      this.updateTable(var2, var1);
      this.repaint();
      this.notifyRepaintListeners();
   }

   public void reset() {
      for(int var1 = 0; var1 < this.values.length; ++var1) {
         this.updateTable(this.nullValue, var1);
      }

      this.repaint();
      this.notifyRepaintListeners();
      this.memoryTable.clearSelection();
      this.hideFlash();
      this.hideHighlight();
   }

   public void hideHighlight() {
      this.highlightIndex.removeAllElements();
      this.repaint();
   }

   public void highlight(int var1) {
      this.highlightIndex.addElement(new Integer(var1));
      this.repaint();
   }

   public void hideFlash() {
      this.flashIndex = -1;
      this.repaint();
   }

   public void flash(int var1) {
      this.flashIndex = var1;
      Utilities.tableCenterScroll(this, this.memoryTable, var1);
   }

   public void setEnabledRange(int var1, int var2, boolean var3) {
      this.startEnabling = var1;
      this.endEnabling = var2;
      this.grayDisabledRange = var3;
      this.repaint();
   }

   public int getMemorySize() {
      return this.values != null ? this.values.length : 0;
   }

   public String getValueAsString(int var1) {
      return Format.translateValueToString(this.values[var1], this.dataFormat);
   }

   public void select(int var1, int var2) {
      this.memoryTable.setRowSelectionInterval(var1, var2);
      Utilities.tableCenterScroll(this, this.memoryTable, var1);
   }

   public void hideSelect() {
      this.memoryTable.clearSelection();
      this.repaint();
   }

   public Point getCoordinates(int var1) {
      JScrollBar var2 = this.scrollPane.getVerticalScrollBar();
      Rectangle var3 = this.memoryTable.getCellRect(var1, this.getValueColumnIndex(), true);
      this.memoryTable.scrollRectToVisible(var3);
      return new Point((int)(var3.getX() + this.topLevelLocation.getX()), (int)(var3.getY() + this.topLevelLocation.getY() - (double)var2.getValue()));
   }

   public String getAddressStr(short var1) {
      return this.addresses[var1];
   }

   public String getValueStr(short var1) {
      return this.valuesStr[var1];
   }

   public short getValueAsShort(short var1) {
      return this.values[var1];
   }

   protected short translateValueToShort(String var1) throws TranslationException {
      boolean var2 = false;

      try {
         short var5 = Format.translateValueToShort(var1, this.dataFormat);
         return var5;
      } catch (NumberFormatException var4) {
         throw new TranslationException("Illegal value: " + var1);
      }
   }

   protected String translateValueToString(short var1) {
      if (this.hideNullValue) {
         return var1 == this.nullValue ? "" : Format.translateValueToString(var1, this.dataFormat);
      } else {
         return Format.translateValueToString(var1, this.dataFormat);
      }
   }

   public void setTableFont(Font var1) {
      this.memoryTable.setFont(var1);
   }

   private void jbInit() {
      this.memoryTable.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            MemoryComponent.this.memoryTable_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            MemoryComponent.this.memoryTable_focusLost(var1);
         }
      });
      this.scrollPane = new JScrollPane(this.memoryTable);
      this.setLayout((LayoutManager)null);
      this.searchButton.setToolTipText("Search");
      this.searchButton.setIcon(this.searchIcon);
      this.searchButton.setBounds(new Rectangle(159, 2, 31, 25));
      this.searchButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MemoryComponent.this.searchButton_actionPerformed(var1);
         }
      });
      this.memoryTable.setFont(Utilities.valueFont);
      this.nameLbl.setBounds(new Rectangle(3, 5, 70, 23));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.determineColumnWidth();
      this.setBorder(BorderFactory.createEtchedBorder());
      this.scrollPane.setLocation(0, 27);
      this.clearButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            MemoryComponent.this.clearButton_actionPerformed(var1);
         }
      });
      this.clearButton.setIcon(this.clearIcon);
      this.clearButton.setBounds(new Rectangle(128, 2, 31, 25));
      this.clearButton.setToolTipText("Clear");
      this.add(this.scrollPane, (Object)null);
      this.add(this.searchButton, (Object)null);
      this.add(this.nameLbl, (Object)null);
      this.add(this.clearButton, (Object)null);
   }

   public int getTableWidth() {
      return 193;
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.memoryTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   protected void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 2; ++var2) {
         var1 = this.memoryTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(30);
         } else {
            var1.setPreferredWidth(100);
         }
      }

   }

   public void scrollTo(int var1) {
      Utilities.tableCenterScroll(this, this.memoryTable, var1);
   }

   public void memoryTable_focusGained(FocusEvent var1) {
      this.memoryTable.clearSelection();
      this.notifyListeners();
   }

   public void memoryTable_focusLost(FocusEvent var1) {
      this.memoryTable.clearSelection();
   }

   public void searchButton_actionPerformed(ActionEvent var1) {
      this.searchWindow.showWindow();
   }

   public void clearButton_actionPerformed(ActionEvent var1) {
      Object[] var2 = new Object[]{"Yes", "No", "Cancel"};
      int var3 = JOptionPane.showOptionDialog(this.getParent(), "Are you sure you want to clear ?", "Warning Message", 1, 2, (Icon)null, var2, var2[2]);
      if (var3 == 0) {
         this.notifyClearListeners();
      }

   }

   public void setNumericFormat(int var1) {
      this.dataFormat = var1;

      for(int var2 = 0; var2 < this.values.length; ++var2) {
         this.valuesStr[var2] = this.translateValueToString(this.values[var2]);
      }

      this.repaint();
      this.notifyRepaintListeners();
   }

   public void setNameLabelSize() {
      this.nameLbl.setBounds(new Rectangle(3, 7, 150, 23));
   }

   class MemoryTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setForeground((Color)null);
         this.setBackground((Color)null);
         this.setRenderer(var5, var6);
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }

      public void setRenderer(int var1, int var2) {
         if (var2 == 0) {
            this.setHorizontalAlignment(0);
         } else if (var2 == 1) {
            this.setHorizontalAlignment(4);

            for(int var3 = 0; var3 < MemoryComponent.this.highlightIndex.size(); ++var3) {
               if (var1 == (Integer)MemoryComponent.this.highlightIndex.elementAt(var3)) {
                  this.setForeground(Color.blue);
                  break;
               }
            }

            if (var1 == MemoryComponent.this.flashIndex) {
               this.setBackground(Color.orange);
            }
         }

         if (var1 < MemoryComponent.this.startEnabling || var1 > MemoryComponent.this.endEnabling && MemoryComponent.this.grayDisabledRange) {
            this.setForeground(Color.lightGray);
         }

      }
   }

   class MemoryTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 2;
      }

      public int getRowCount() {
         return MemoryComponent.this.getMemorySize();
      }

      public String getColumnName(int var1) {
         return null;
      }

      public Object getValueAt(int var1, int var2) {
         return var2 == 0 ? MemoryComponent.this.addresses[var1] : MemoryComponent.this.valuesStr[var1];
      }

      public boolean isCellEditable(int var1, int var2) {
         boolean var3 = false;
         if (MemoryComponent.this.isEnabled && var2 == 1 && (MemoryComponent.this.endEnabling == -1 || var1 >= MemoryComponent.this.startEnabling && var1 <= MemoryComponent.this.endEnabling)) {
            var3 = true;
         }

         return var3;
      }

      public void setValueAt(Object var1, int var2, int var3) {
         String var4 = ((String)var1).trim();
         if (!MemoryComponent.this.valuesStr[var2].equals(var4)) {
            try {
               MemoryComponent.this.valuesStr[var2] = var4;
               if (var4.equals("") && MemoryComponent.this.hideNullValue) {
                  MemoryComponent.this.values[var2] = MemoryComponent.this.nullValue;
               } else {
                  MemoryComponent.this.values[var2] = MemoryComponent.this.translateValueToShort(var4);
               }

               MemoryComponent.this.notifyListeners((short)var2, MemoryComponent.this.values[var2]);
            } catch (TranslationException var6) {
               MemoryComponent.this.notifyErrorListeners(var6.getMessage());
               MemoryComponent.this.valuesStr[var2] = MemoryComponent.this.translateValueToString(MemoryComponent.this.values[var2]);
            }

            MemoryComponent.this.repaint();
            MemoryComponent.this.notifyRepaintListeners();
         }

      }
   }
}
