package HackGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class FileDisplayComponent extends JPanel {
   private String[] rows = new String[0];
   private WideTable fileDisplayTable;
   private JScrollPane scrollPane;
   private int selectedRow = -1;
   private String fileName;

   public FileDisplayComponent() {
      this.jbInit();
   }

   public void setSelectedRow(int var1) {
      this.selectedRow = var1;
      if (this.selectedRow >= 0) {
         Utilities.tableCenterScroll(this, this.fileDisplayTable, this.selectedRow);
      }

      this.repaint();
   }

   public void deleteContent() {
      this.rows = new String[0];
      this.fileDisplayTable.revalidate();
      this.repaint();
   }

   public void refresh() {
      this.setContents(this.fileName);
   }

   public synchronized void setContents(String var1) {
      this.fileName = var1;
      Vector var3 = new Vector();

      try {
         BufferedReader var2 = new BufferedReader(new FileReader(var1));

         String var4;
         while((var4 = var2.readLine()) != null) {
            var3.addElement(var4);
         }

         var2.close();
      } catch (IOException var6) {
      }

      this.rows = new String[var3.size()];
      var3.toArray(this.rows);
      this.fileDisplayTable.clearSelection();
      this.fileDisplayTable.revalidate();

      try {
         this.wait(50L);
      } catch (InterruptedException var5) {
      }

      this.repaint();
   }

   public void updateSize(int var1, int var2) {
      this.setSize(var1, var2);
      this.scrollPane.setPreferredSize(new Dimension(var1, var2));
      this.scrollPane.setSize(var1, var2);
   }

   private void jbInit() {
      this.setLayout((LayoutManager)null);
      this.fileDisplayTable = new WideTable(new FileDisplayComponent.FileDisplayTableModel(), 1000);
      this.fileDisplayTable.setTableHeader((JTableHeader)null);
      this.fileDisplayTable.setDefaultRenderer(this.fileDisplayTable.getColumnClass(0), new FileDisplayComponent.FileDisplayTableCellRenderer());
      this.scrollPane = new JScrollPane(this.fileDisplayTable);
      this.fileDisplayTable.setRowSelectionAllowed(false);
      this.fileDisplayTable.setShowHorizontalLines(false);
      this.fileDisplayTable.setShowVerticalLines(false);
      this.fileDisplayTable.setFont(Utilities.valueFont);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.scrollPane.setHorizontalScrollBarPolicy(32);
      this.scrollPane.getHorizontalScrollBar().setUnitIncrement(this.scrollPane.getHorizontalScrollBar().getBlockIncrement());
      this.scrollPane.setLocation(0, 0);
      this.scrollPane.setPreferredSize(new Dimension(516, 260));
      this.scrollPane.setSize(516, 260);
      this.setSize(516, 260);
      this.add(this.scrollPane, (Object)null);
   }

   class FileDisplayTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setForeground((Color)null);
         this.setBackground((Color)null);
         this.setRenderer(var5, var6);
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }

      public void setRenderer(int var1, int var2) {
         if (var1 == FileDisplayComponent.this.selectedRow) {
            this.setBackground(Color.yellow);
         } else {
            this.setBackground((Color)null);
         }

      }
   }

   class FileDisplayTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 1;
      }

      public int getRowCount() {
         return FileDisplayComponent.this.rows.length;
      }

      public String getColumnName(int var1) {
         return "";
      }

      public Object getValueAt(int var1, int var2) {
         return FileDisplayComponent.this.rows[var1];
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }
}
