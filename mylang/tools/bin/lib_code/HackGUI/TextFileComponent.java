package HackGUI;

import Hack.ComputerParts.TextFileEvent;
import Hack.ComputerParts.TextFileEventListener;
import Hack.ComputerParts.TextFileGUI;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class TextFileComponent extends JPanel implements TextFileGUI {
   private Vector listeners = new Vector();
   private Vector rowsVector = new Vector();
   private TextFileComponent.TextFileTableModel model = new TextFileComponent.TextFileTableModel();
   private JTable textFileTable;
   private JScrollPane scrollPane;
   private JLabel nameLbl = new JLabel();
   private Set highlightedLines;
   private Set emphasizedLines;
   private boolean isEnabled;

   public TextFileComponent() {
      this.textFileTable = new WideTable(this.model, 1000);
      this.textFileTable.setDefaultRenderer(this.textFileTable.getColumnClass(0), this.getCellRenderer());
      this.textFileTable.setTableHeader((JTableHeader)null);
      this.highlightedLines = new HashSet();
      this.emphasizedLines = new HashSet();
      this.enableUserInput();
      this.jbInit();
   }

   public void enableUserInput() {
      this.textFileTable.setRowSelectionAllowed(true);
      this.isEnabled = true;
   }

   public void disableUserInput() {
      this.textFileTable.setRowSelectionAllowed(false);
      this.isEnabled = false;
   }

   public void hideSelect() {
      this.textFileTable.clearSelection();
   }

   public void select(int var1, int var2) {
      this.textFileTable.setRowSelectionInterval(var1, var2);
      Utilities.tableCenterScroll(this, this.textFileTable, var1);
   }

   public void setName(String var1) {
      this.nameLbl.setText(var1);
   }

   public void addHighlight(int var1, boolean var2) {
      if (var2) {
         this.highlightedLines.clear();
      }

      this.highlightedLines.add(new Integer(var1));
      Utilities.tableCenterScroll(this, this.textFileTable, var1);
      this.repaint();
   }

   public void clearHighlights() {
      this.highlightedLines.clear();
      this.repaint();
   }

   public void addEmphasis(int var1) {
      this.emphasizedLines.add(new Integer(var1));
      this.repaint();
   }

   public void removeEmphasis(int var1) {
      this.emphasizedLines.remove(new Integer(var1));
      this.repaint();
   }

   public String getLineAt(int var1) {
      return (String)this.rowsVector.elementAt(var1);
   }

   public int getNumberOfLines() {
      return this.rowsVector.size();
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new TextFileComponent.TextFileCellRenderer();
   }

   public void addTextFileListener(TextFileEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeTextFileListener(TextFileEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyTextFileListeners(String var1, int var2) {
      TextFileEvent var3 = new TextFileEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((TextFileEventListener)this.listeners.elementAt(var4)).rowSelected(var3);
      }

   }

   public void addLine(String var1) {
      this.rowsVector.addElement(var1);
      this.textFileTable.revalidate();
      this.repaint();
      this.addHighlight(this.rowsVector.size() - 1, false);
   }

   public void setLineAt(int var1, String var2) {
      this.rowsVector.setElementAt(var2, var1);
      this.addHighlight(var1, false);
   }

   public void setContents(String[] var1) {
      this.rowsVector.removeAllElements();

      for(int var2 = 0; var2 < var1.length; ++var2) {
         this.rowsVector.addElement(var1[var2]);
      }

      this.textFileTable.revalidate();
      this.repaint();
   }

   public void setContents(String var1) {
      this.rowsVector.removeAllElements();

      try {
         BufferedReader var2 = new BufferedReader(new FileReader(var1));

         String var3;
         while((var3 = var2.readLine()) != null) {
            this.rowsVector.addElement(var3);
         }

         var2.close();
      } catch (IOException var4) {
      }

      this.textFileTable.clearSelection();
      this.textFileTable.revalidate();
      this.repaint();
   }

   public void reset() {
      this.highlightedLines.clear();
      this.rowsVector.removeAllElements();
      this.textFileTable.revalidate();
      this.textFileTable.clearSelection();
      this.repaint();
   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.textFileTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
      this.textFileTable.getParent().setSize(new Dimension(1000, var2));
   }

   public int getTableWidth() {
      return 241;
   }

   private void jbInit() {
      this.textFileTable.setShowHorizontalLines(false);
      ListSelectionModel var1 = this.textFileTable.getSelectionModel();
      var1.addListSelectionListener(new ListSelectionListener() {
         public void valueChanged(ListSelectionEvent var1) {
            if (TextFileComponent.this.isEnabled && !var1.getValueIsAdjusting()) {
               ListSelectionModel var2 = (ListSelectionModel)var1.getSource();
               if (!var2.isSelectionEmpty()) {
                  int var3 = var2.getMinSelectionIndex();
                  TextFileComponent.this.notifyTextFileListeners((String)TextFileComponent.this.rowsVector.elementAt(var3), var3);
               }

            }
         }
      });
      this.setLayout((LayoutManager)null);
      this.scrollPane = new JScrollPane(this.textFileTable);
      this.scrollPane.setLocation(0, 27);
      this.scrollPane.setHorizontalScrollBarPolicy(32);
      this.scrollPane.getHorizontalScrollBar().setUnitIncrement(this.scrollPane.getHorizontalScrollBar().getBlockIncrement());
      this.nameLbl.setBounds(new Rectangle(3, 3, 102, 21));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.textFileTable.setFont(Utilities.valueFont);
      this.setBorder(BorderFactory.createEtchedBorder());
      this.add(this.scrollPane, (Object)null);
      this.add(this.nameLbl, (Object)null);
   }

   public class TextFileCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setForeground((Color)null);
         this.setBackground((Color)null);
         this.setRenderer(var5, var6);
         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }

      public void setRenderer(int var1, int var2) {
         if (TextFileComponent.this.highlightedLines.contains(new Integer(var1))) {
            this.setBackground(Color.yellow);
         } else {
            this.setBackground((Color)null);
         }

         if (TextFileComponent.this.emphasizedLines.contains(new Integer(var1))) {
            this.setForeground(Color.red);
         } else {
            this.setForeground((Color)null);
         }

      }
   }

   class TextFileTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 1;
      }

      public int getRowCount() {
         return TextFileComponent.this.rowsVector.size();
      }

      public String getColumnName(int var1) {
         return "";
      }

      public Object getValueAt(int var1, int var2) {
         return TextFileComponent.this.rowsVector.elementAt(var1);
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }
}
