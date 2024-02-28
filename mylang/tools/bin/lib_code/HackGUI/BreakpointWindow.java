package HackGUI;

import Hack.Controller.Breakpoint;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class BreakpointWindow extends JFrame implements MouseListener, BreakpointChangedListener {
   private JTable breakpointTable;
   private Vector breakpoints = new Vector();
   private BreakpointWindow.BreakpointTableModel model = new BreakpointWindow.BreakpointTableModel();
   private Vector listeners;
   private FlowLayout flowLayout = new FlowLayout();
   private JButton addButton = new JButton();
   private JButton removeButton = new JButton();
   private JButton okButton = new JButton();
   private BreakpointWindow.ColoredTableCellRenderer coloredRenderer = new BreakpointWindow.ColoredTableCellRenderer();
   private ImageIcon addIcon = new ImageIcon("bin/images/smallplus.gif");
   private ImageIcon removeIcon = new ImageIcon("bin/images/smallminus.gif");
   private ImageIcon okIcon = new ImageIcon("bin/images/ok2.gif");
   private BreakpointVariablesWindow variables = new BreakpointVariablesWindow();
   private int selectedRowIndex = -1;

   public BreakpointWindow() {
      super("Breakpoint Panel");
      this.breakpointTable = new JTable(this.model);
      this.breakpointTable.setDefaultRenderer(this.breakpointTable.getColumnClass(0), this.coloredRenderer);
      this.listeners = new Vector();
      this.setResizable(false);
      this.jbInit();
   }

   public void setBreakpoints(Vector var1) {
      this.breakpoints = (Vector)var1.clone();
      this.breakpointTable.revalidate();
   }

   public void setVariables(String[] var1) {
      this.variables.setVariables(var1);
   }

   public void addBreakpointListener(BreakpointsChangedListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeBreakpointListener(BreakpointsChangedListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyListeners() {
      BreakpointsChangedEvent var1 = new BreakpointsChangedEvent(this, this.breakpoints);

      for(int var2 = 0; var2 < this.listeners.size(); ++var2) {
         ((BreakpointsChangedListener)this.listeners.elementAt(var2)).breakpointsChanged(var1);
      }

   }

   public JTable getTable() {
      return this.breakpointTable;
   }

   public void breakpointChanged(BreakpointChangedEvent var1) {
      Breakpoint var2 = var1.getBreakpoint();
      if (this.selectedRowIndex == -1) {
         this.breakpoints.addElement(var2);
      } else {
         this.breakpoints.setElementAt(var2, this.selectedRowIndex);
      }

      this.breakpointTable.revalidate();
      this.repaint();
      this.notifyListeners();
   }

   public void mouseClicked(MouseEvent var1) {
      if (var1.getClickCount() == 2) {
         int var2 = this.breakpointTable.getSelectedRow();
         this.selectedRowIndex = var2;
         this.variables.setBreakpointName(((Breakpoint)this.breakpoints.elementAt(var2)).getVarName());
         this.variables.setBreakpointValue(((Breakpoint)this.breakpoints.elementAt(var2)).getValue());
         this.variables.showWindow();
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

   private void jbInit() {
      this.variables.addListener(this);
      this.getContentPane().setLayout(this.flowLayout);
      this.breakpointTable.setSelectionMode(0);
      this.breakpointTable.addMouseListener(this);
      ListSelectionModel var1 = this.breakpointTable.getSelectionModel();
      JScrollPane var2 = new JScrollPane(this.breakpointTable);
      var2.setPreferredSize(new Dimension(190, 330));
      this.addButton.setPreferredSize(new Dimension(35, 25));
      this.addButton.setToolTipText("Add breakpoint");
      this.addButton.setIcon(this.addIcon);
      this.addButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointWindow.this.addButton_actionPerformed(var1);
         }
      });
      this.removeButton.setPreferredSize(new Dimension(35, 25));
      this.removeButton.setToolTipText("Remove breakpoint");
      this.removeButton.setIcon(this.removeIcon);
      this.removeButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointWindow.this.removeButton_actionPerformed(var1);
         }
      });
      this.okButton.setPreferredSize(new Dimension(35, 25));
      this.okButton.setToolTipText("OK");
      this.okButton.setIcon(this.okIcon);
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            BreakpointWindow.this.okButton_actionPerformed(var1);
         }
      });
      this.getContentPane().add(var2, (Object)null);
      this.getContentPane().add(this.addButton, (Object)null);
      this.getContentPane().add(this.removeButton, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.setSize(210, 410);
      this.setLocation(250, 250);
   }

   public void addButton_actionPerformed(ActionEvent var1) {
      this.breakpointTable.clearSelection();
      this.selectedRowIndex = -1;
      this.variables.setNameCombo(0);
      this.variables.setBreakpointName("");
      this.variables.setBreakpointValue("");
      this.variables.showWindow();
   }

   public void removeButton_actionPerformed(ActionEvent var1) {
      int var2 = this.breakpointTable.getSelectedRow();
      if (var2 >= 0 && var2 < this.breakpointTable.getRowCount()) {
         this.model.removeRow(this.breakpointTable.getSelectedRow());
         this.breakpointTable.revalidate();
         this.notifyListeners();
      }

   }

   public void okButton_actionPerformed(ActionEvent var1) {
      this.setVisible(false);
   }

   class ColoredTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setEnabled(var1 == null || var1.isEnabled());
         if (((Breakpoint)BreakpointWindow.this.breakpoints.elementAt(var5)).isReached()) {
            this.setBackground(Color.red);
         } else {
            this.setBackground((Color)null);
         }

         this.setHorizontalAlignment(0);
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }
   }

   class BreakpointTableModel extends AbstractTableModel {
      String[] columnNames = new String[]{"Variable Name", "Value"};

      public int getColumnCount() {
         return this.columnNames.length;
      }

      public int getRowCount() {
         return BreakpointWindow.this.breakpoints.size();
      }

      public String getColumnName(int var1) {
         return this.columnNames[var1];
      }

      public Object getValueAt(int var1, int var2) {
         Breakpoint var3 = (Breakpoint)BreakpointWindow.this.breakpoints.elementAt(var1);
         return var2 == 0 ? var3.getVarName() : var3.getValue();
      }

      public void removeRow(int var1) {
         if (BreakpointWindow.this.breakpoints.size() > 0) {
            BreakpointWindow.this.breakpoints.removeElementAt(var1);
         }

      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }
}
