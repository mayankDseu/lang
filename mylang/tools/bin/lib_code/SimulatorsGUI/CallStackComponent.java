package SimulatorsGUI;

import Hack.VMEmulator.CallStackGUI;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class CallStackComponent extends JPanel implements CallStackGUI {
   protected static final int DEFAULT_VISIBLE_ROWS = 10;
   private Vector methodNames = new Vector();
   private JTable callStackTable;
   private CallStackComponent.CallStackTableModel model = new CallStackComponent.CallStackTableModel();
   private JScrollPane scrollPane;
   private JLabel nameLbl = new JLabel();

   public CallStackComponent() {
      this.callStackTable = new JTable(this.model);
      this.jbInit();
   }

   public void setContents(Vector var1) {
      this.methodNames = (Vector)var1.clone();
      this.callStackTable.revalidate();
      Rectangle var2 = this.callStackTable.getCellRect(var1.size() - 1, 0, true);
      this.callStackTable.scrollRectToVisible(var2);
      this.repaint();
   }

   public void reset() {
      this.methodNames.removeAllElements();
      this.callStackTable.revalidate();
      this.callStackTable.clearSelection();
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.callStackTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   public int getTableWidth() {
      return 190;
   }

   private void jbInit() {
      this.callStackTable.addFocusListener(new FocusListener() {
         public void focusGained(FocusEvent var1) {
            CallStackComponent.this.callStackTable_focusGained(var1);
         }

         public void focusLost(FocusEvent var1) {
            CallStackComponent.this.callStackTable_focusLost(var1);
         }
      });
      this.callStackTable.setTableHeader((JTableHeader)null);
      this.callStackTable.setDefaultRenderer(this.callStackTable.getColumnClass(0), this.getCellRenderer());
      this.scrollPane = new JScrollPane(this.callStackTable);
      this.setVisibleRows(10);
      this.scrollPane.setLocation(0, 27);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.setLayout((LayoutManager)null);
      this.nameLbl.setText("Call Stack");
      this.nameLbl.setBounds(new Rectangle(3, 4, 70, 23));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.add(this.scrollPane, (Object)null);
      this.add(this.nameLbl, (Object)null);
   }

   public void callStackTable_focusGained(FocusEvent var1) {
   }

   public void callStackTable_focusLost(FocusEvent var1) {
      this.callStackTable.clearSelection();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new CallStackComponent.callStackTableCellRenderer();
   }

   public class callStackTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setForeground((Color)null);
         this.setBackground((Color)null);
         this.setRenderer(var5, var6);
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }

      public void setRenderer(int var1, int var2) {
         if (var1 == CallStackComponent.this.methodNames.size() - 1) {
            this.setForeground(Color.blue);
         }

      }
   }

   class CallStackTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 1;
      }

      public int getRowCount() {
         return CallStackComponent.this.methodNames.size();
      }

      public String getColumnName(int var1) {
         return "";
      }

      public Object getValueAt(int var1, int var2) {
         return CallStackComponent.this.methodNames.elementAt(var1);
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }
}
