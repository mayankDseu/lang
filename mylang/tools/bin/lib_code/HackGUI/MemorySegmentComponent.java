package HackGUI;

import Hack.ComputerParts.ComputerPartEvent;
import Hack.ComputerParts.ComputerPartEventListener;
import Hack.ComputerParts.MemorySegmentGUI;
import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class MemorySegmentComponent extends JPanel implements MemorySegmentGUI, MemoryChangeListener {
   public int dataFormat = 0;
   protected MemoryComponent memory;
   protected int startAddress = 0;
   protected JTable segmentTable = new JTable(this.getTableModel());
   protected MemorySegmentComponent.MemorySegmentTableModel model;
   protected JScrollPane scrollPane;
   protected JLabel nameLbl = new JLabel();
   protected Vector highlightIndex = new Vector();
   private Vector listeners = new Vector();
   private Vector errorEventListeners = new Vector();
   protected int flashIndex = -1;
   protected Point topLevelLocation;
   private BorderLayout borderLayout = new BorderLayout();
   private Component topLevelComponent;
   protected boolean isEnabled = true;
   protected short nullValue;
   protected boolean hideNullValue;
   protected int startEnabling;
   protected int endEnabling;
   protected boolean hideDisabledRange;

   public MemorySegmentComponent() {
      this.segmentTable.setDefaultRenderer(this.segmentTable.getColumnClass(0), this.getCellRenderer());
      this.startEnabling = -1;
      this.endEnabling = -1;
      JTextField var1 = new JTextField();
      var1.setFont(Utilities.bigBoldValueFont);
      var1.setBorder((Border)null);
      DefaultCellEditor var2 = new DefaultCellEditor(var1);
      this.segmentTable.getColumnModel().getColumn(this.getColumnValue()).setCellEditor(var2);
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

   protected int getColumnValue() {
      return 1;
   }

   public void setTopLevelLocation() {
      this.topLevelLocation = Utilities.getTopLevelLocation(this.topLevelComponent, this.segmentTable);
   }

   public void setTopLevelLocation(Component var1) {
      this.topLevelComponent = var1;
      this.setTopLevelLocation();
   }

   protected TableModel getTableModel() {
      return new MemorySegmentComponent.MemorySegmentTableModel();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new MemorySegmentComponent.MemorySegmentTableCellRenderer();
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

   public void setValueAt(int var1, short var2) {
      Rectangle var3 = this.segmentTable.getCellRect(var1, 0, true);
      this.segmentTable.scrollRectToVisible(var3);
      this.repaint();
   }

   public void setStartAddress(int var1) {
      this.startAddress = var1;
      this.segmentTable.revalidate();
   }

   public void reset() {
      this.segmentTable.clearSelection();
      this.hideFlash();
      this.hideHighlight();
   }

   public String getValueAsString(int var1) {
      return Format.translateValueToString(this.memory.getValueAsShort((short)(var1 + this.startAddress)), this.dataFormat);
   }

   public Point getCoordinates(int var1) {
      JScrollBar var2 = this.scrollPane.getVerticalScrollBar();
      Rectangle var3 = this.segmentTable.getCellRect(var1, 1, true);
      this.segmentTable.scrollRectToVisible(var3);
      this.setTopLevelLocation();
      return new Point((int)(var3.getX() + this.topLevelLocation.getX()), (int)(var3.getY() + this.topLevelLocation.getY()));
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
      Utilities.tableCenterScroll(this, this.segmentTable, var1);
   }

   public void setEnabledRange(int var1, int var2, boolean var3) {
      this.startEnabling = var1;
      this.endEnabling = var2;
      this.hideDisabledRange = var3;
      this.repaint();
   }

   public void setMemoryComponent(MemoryComponent var1) {
      this.memory = var1;
   }

   public void repaintChange() {
      this.repaint();
   }

   public void revalidateChange() {
      this.segmentTable.revalidate();
      this.repaint();
   }

   protected void determineColumnWidth() {
      if (this.segmentTable.getColumnCount() == 2) {
         TableColumn var1 = null;

         for(int var2 = 0; var2 < 2; ++var2) {
            var1 = this.segmentTable.getColumnModel().getColumn(var2);
            if (var2 == 0) {
               var1.setPreferredWidth(30);
            } else {
               var1.setPreferredWidth(100);
            }
         }
      }

   }

   public void setSegmentName(String var1) {
      this.nameLbl.setText(var1);
   }

   public void setNumericFormat(int var1) {
      this.dataFormat = var1;
   }

   public void hideSelect() {
      this.segmentTable.clearSelection();
   }

   public void scrollTo(int var1) {
      Utilities.tableCenterScroll(this, this.segmentTable, var1);
   }

   public int getTableWidth() {
      return 193;
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.segmentTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   private void jbInit() {
      this.segmentTable.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            MemorySegmentComponent.this.segmentTable_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            MemorySegmentComponent.this.segmentTable_focusLost(var1);
         }
      });
      this.segmentTable.setTableHeader((JTableHeader)null);
      this.scrollPane = new JScrollPane(this.segmentTable);
      this.scrollPane.setMinimumSize(new Dimension(this.getTableWidth(), 0));
      this.setLayout(new BorderLayout(0, 0));
      this.determineColumnWidth();
      this.nameLbl.setPreferredSize(new Dimension(this.getTableWidth(), 25));
      this.nameLbl.setMinimumSize(new Dimension(this.getTableWidth(), 0));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.segmentTable.setFont(Utilities.valueFont);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setMinimumSize(new Dimension(this.getTableWidth(), 0));
      this.add(this.nameLbl, "North");
      this.add(this.scrollPane, "Center");
   }

   public void segmentTable_focusGained(FocusEvent var1) {
      this.segmentTable.clearSelection();
      this.notifyListeners();
   }

   public void segmentTable_focusLost(FocusEvent var1) {
      this.segmentTable.clearSelection();
   }

   private String getStrAt(int var1) {
      short var2 = this.memory.getValueAsShort((short)(var1 + this.startAddress));
      return var2 == this.nullValue && this.hideNullValue ? "" : Format.translateValueToString(var2, this.dataFormat);
   }

   public JTable getTable() {
      return this.segmentTable;
   }

   class MemorySegmentTableCellRenderer extends DefaultTableCellRenderer {
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

            for(int var3 = 0; var3 < MemorySegmentComponent.this.highlightIndex.size(); ++var3) {
               if (var1 == (Integer)MemorySegmentComponent.this.highlightIndex.elementAt(var3)) {
                  this.setForeground(Color.blue);
                  break;
               }
            }

            if (var1 == MemorySegmentComponent.this.flashIndex) {
               this.setBackground(Color.orange);
            }
         }

         this.setEnabledRange(var1);
      }

      public void setEnabledRange(int var1) {
         if ((var1 + MemorySegmentComponent.this.startAddress < MemorySegmentComponent.this.startEnabling || var1 + MemorySegmentComponent.this.startAddress > MemorySegmentComponent.this.endEnabling) && MemorySegmentComponent.this.hideDisabledRange) {
            this.setForeground(Color.white);
         }

      }
   }

   class MemorySegmentTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 2;
      }

      public int getRowCount() {
         return MemorySegmentComponent.this.memory != null ? Math.max(MemorySegmentComponent.this.memory.getMemorySize() - MemorySegmentComponent.this.startAddress, 0) : 0;
      }

      public String getColumnName(int var1) {
         return "";
      }

      public Object getValueAt(int var1, int var2) {
         return var2 == 0 ? String.valueOf(var1) : MemorySegmentComponent.this.getStrAt(var1);
      }

      public boolean isCellEditable(int var1, int var2) {
         boolean var3 = false;
         if (MemorySegmentComponent.this.isEnabled && var2 == 1 && (MemorySegmentComponent.this.endEnabling == -1 || var1 + MemorySegmentComponent.this.startAddress >= MemorySegmentComponent.this.startEnabling && var1 + MemorySegmentComponent.this.startAddress <= MemorySegmentComponent.this.endEnabling)) {
            var3 = true;
         }

         return var3;
      }

      public void setValueAt(Object var1, int var2, int var3) {
         String var4 = ((String)var1).trim();
         if (!MemorySegmentComponent.this.getStrAt(var2).equals(var4)) {
            try {
               short var5;
               if (var4.equals("") && MemorySegmentComponent.this.hideNullValue) {
                  var5 = MemorySegmentComponent.this.nullValue;
               } else {
                  var5 = Format.translateValueToShort(var4, MemorySegmentComponent.this.memory.dataFormat);
               }

               MemorySegmentComponent.this.notifyListeners((short)var2, var5);
            } catch (NumberFormatException var6) {
               MemorySegmentComponent.this.notifyErrorListeners("Illegal value");
            }

            MemorySegmentComponent.this.repaint();
         }

      }
   }
}
