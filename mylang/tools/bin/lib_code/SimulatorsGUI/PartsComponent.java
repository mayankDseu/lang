package SimulatorsGUI;

import Hack.Gates.BuiltInGate;
import Hack.Gates.CompositeGate;
import Hack.Gates.Gate;
import Hack.HardwareSimulator.PartsGUI;
import HackGUI.Utilities;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class PartsComponent extends JPanel implements PartsGUI {
   private static final String BUILTIN_GATE = "BuiltIn";
   private static final String COMPOSITE_GATE = "Composite";
   private JTable partsTable;
   private Gate[] parts = new Gate[0];
   private JScrollPane scrollPane;
   private PartsComponent.PartsTableModel model = new PartsComponent.PartsTableModel();
   private JLabel nameLbl = new JLabel();

   public PartsComponent() {
      this.partsTable = new JTable(this.model);
      this.jbInit();
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.partsTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   public int getTableWidth() {
      return 241;
   }

   public void setName(String var1) {
      this.nameLbl.setText(var1);
   }

   public void setContents(Gate[] var1) {
      this.parts = new Gate[var1.length];
      System.arraycopy(var1, 0, this.parts, 0, var1.length);
      this.partsTable.clearSelection();
      this.partsTable.revalidate();
   }

   public void reset() {
      this.partsTable.clearSelection();
      this.repaint();
   }

   private void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 3; ++var2) {
         var1 = this.partsTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(110);
         } else if (var2 == 1) {
            var1.setPreferredWidth(72);
         } else if (var2 == 2) {
            var1.setPreferredWidth(55);
         }
      }

   }

   private void jbInit() {
      this.setLayout((LayoutManager)null);
      this.partsTable.setFont(Utilities.valueFont);
      this.partsTable.getTableHeader().setReorderingAllowed(false);
      this.partsTable.getTableHeader().setResizingAllowed(false);
      this.partsTable.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            PartsComponent.this.partsTable_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            PartsComponent.this.partsTable_focusLost(var1);
         }
      });
      this.setBorder(BorderFactory.createEtchedBorder());
      this.scrollPane = new JScrollPane(this.partsTable);
      this.scrollPane.setLocation(0, 27);
      this.nameLbl.setText("Name :");
      this.nameLbl.setBounds(new Rectangle(3, 3, 102, 21));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.add(this.scrollPane, (Object)null);
      this.add(this.nameLbl, (Object)null);
      this.determineColumnWidth();
   }

   public void partsTable_focusGained(FocusEvent var1) {
   }

   public void partsTable_focusLost(FocusEvent var1) {
      this.partsTable.clearSelection();
   }

   class PartsTableModel extends AbstractTableModel {
      String[] columnNames = new String[]{"Chip Name", "Type", "Clocked"};

      public int getColumnCount() {
         return this.columnNames.length;
      }

      public int getRowCount() {
         return PartsComponent.this.parts.length;
      }

      public String getColumnName(int var1) {
         return this.columnNames[var1];
      }

      public Object getValueAt(int var1, int var2) {
         Object var3 = null;
         if (var2 == 0) {
            var3 = PartsComponent.this.parts[var1].getGateClass().getName();
         } else if (var2 == 1) {
            if (PartsComponent.this.parts[var1] instanceof CompositeGate) {
               var3 = "Composite";
            } else if (PartsComponent.this.parts[var1] instanceof BuiltInGate) {
               var3 = "BuiltIn";
            }
         } else if (var2 == 2) {
            var3 = new Boolean(PartsComponent.this.parts[var1].getGateClass().isClocked());
         }

         return var3;
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }

      public Class getColumnClass(int var1) {
         return this.getValueAt(0, var1).getClass();
      }
   }
}
