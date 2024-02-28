package SimulatorsGUI;

import Hack.Events.ErrorEvent;
import Hack.Events.ErrorEventListener;
import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import Hack.VMEmulator.VMEmulatorInstruction;
import Hack.VMEmulator.VMProgramGUI;
import HackGUI.MouseOverJButton;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class ProgramComponent extends JPanel implements VMProgramGUI {
   private Vector listeners = new Vector();
   private Vector errorEventListeners = new Vector();
   protected JTable programTable;
   private ProgramComponent.ProgramTableModel model = new ProgramComponent.ProgramTableModel();
   protected VMEmulatorInstruction[] instructions = new VMEmulatorInstruction[0];
   protected MouseOverJButton browseButton = new MouseOverJButton();
   private ImageIcon browseIcon = new ImageIcon("bin/images/open2.gif");
   private JFileChooser fileChooser = new JFileChooser();
   private int instructionIndex;
   private JTextField messageTxt = new JTextField();
   private ProgramComponent.ColoredTableCellRenderer coloredRenderer = new ProgramComponent.ColoredTableCellRenderer();
   private MouseOverJButton searchButton = new MouseOverJButton();
   private ImageIcon searchIcon = new ImageIcon("bin/images/find.gif");
   private SearchProgramWindow searchWindow;
   private JScrollPane scrollPane;
   private JLabel nameLbl = new JLabel();
   protected MouseOverJButton clearButton = new MouseOverJButton();
   private ImageIcon clearIcon = new ImageIcon("bin/images/smallnew.gif");

   public ProgramComponent() {
      this.programTable = new JTable(this.model);
      this.programTable.setDefaultRenderer(this.programTable.getColumnClass(0), this.coloredRenderer);
      this.searchWindow = new SearchProgramWindow(this.programTable, this.instructions);
      this.jbInit();
   }

   public void addProgramListener(ProgramEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeProgramListener(ProgramEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyProgramListeners(byte var1, String var2) {
      ProgramEvent var3 = new ProgramEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ProgramEventListener)this.listeners.elementAt(var4)).programChanged(var3);
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

   public void setWorkingDir(File var1) {
      this.fileChooser.setCurrentDirectory(var1);
   }

   public synchronized void setContents(VMEmulatorInstruction[] var1, int var2) {
      this.instructions = new VMEmulatorInstruction[var2];
      System.arraycopy(var1, 0, this.instructions, 0, var2);
      this.programTable.revalidate();

      try {
         this.wait(100L);
      } catch (InterruptedException var4) {
      }

      this.searchWindow.setInstructions(this.instructions);
   }

   public void setCurrentInstruction(int var1) {
      this.instructionIndex = var1;
      Utilities.tableCenterScroll(this, this.programTable, var1);
   }

   public void reset() {
      this.instructions = new VMEmulatorInstruction[0];
      this.programTable.clearSelection();
      this.repaint();
   }

   public void loadProgram() {
      int var1 = this.fileChooser.showDialog(this, "Load Program");
      if (var1 == 0) {
         this.notifyProgramListeners((byte)1, this.fileChooser.getSelectedFile().getAbsolutePath());
      }

   }

   public void browseButton_actionPerformed(ActionEvent var1) {
      this.loadProgram();
   }

   public void hideMessage() {
      this.messageTxt.setText("");
      this.messageTxt.setVisible(false);
      this.searchButton.setVisible(true);
      this.clearButton.setVisible(true);
      this.browseButton.setVisible(true);
   }

   public void showMessage(String var1) {
      this.messageTxt.setText(var1);
      this.messageTxt.setVisible(true);
      this.searchButton.setVisible(false);
      this.clearButton.setVisible(false);
      this.browseButton.setVisible(false);
   }

   private void determineColumnWidth() {
      TableColumn var1 = null;

      for(int var2 = 0; var2 < 3; ++var2) {
         var1 = this.programTable.getColumnModel().getColumn(var2);
         if (var2 == 0) {
            var1.setPreferredWidth(30);
         } else if (var2 == 1) {
            var1.setPreferredWidth(40);
         } else if (var2 == 2) {
            var1.setPreferredWidth(100);
         }
      }

   }

   public void searchButton_actionPerformed(ActionEvent var1) {
      this.searchWindow.showWindow();
   }

   public void clearButton_actionPerformed(ActionEvent var1) {
      Object[] var2 = new Object[]{"Yes", "No", "Cancel"};
      int var3 = JOptionPane.showOptionDialog(this.getParent(), "Are you sure you want to clear the program?", "Warning Message", 1, 2, (Icon)null, var2, var2[2]);
      if (var3 == 0) {
         this.notifyProgramListeners((byte)3, (String)null);
      }

   }

   public void setVisibleRows(int var1) {
      int var2 = var1 * this.programTable.getRowHeight();
      this.scrollPane.setSize(this.getTableWidth(), var2 + 3);
      this.setPreferredSize(new Dimension(this.getTableWidth(), var2 + 30));
      this.setSize(this.getTableWidth(), var2 + 30);
   }

   public void setNameLabel(String var1) {
      this.nameLbl.setText(var1);
   }

   public int getTableWidth() {
      return 225;
   }

   private void jbInit() {
      this.fileChooser.setFileSelectionMode(2);
      this.fileChooser.setFileFilter(new VMFileFilter());
      this.programTable.getTableHeader().setReorderingAllowed(false);
      this.programTable.getTableHeader().setResizingAllowed(false);
      this.scrollPane = new JScrollPane(this.programTable);
      this.scrollPane.setLocation(0, 27);
      this.browseButton.setToolTipText("Load Program");
      this.browseButton.setIcon(this.browseIcon);
      this.browseButton.setBounds(new Rectangle(119, 2, 31, 24));
      this.browseButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ProgramComponent.this.browseButton_actionPerformed(var1);
         }
      });
      this.messageTxt.setBackground(SystemColor.info);
      this.messageTxt.setEnabled(false);
      this.messageTxt.setFont(Utilities.labelsFont);
      this.messageTxt.setPreferredSize(new Dimension(70, 20));
      this.messageTxt.setDisabledTextColor(Color.red);
      this.messageTxt.setEditable(false);
      this.messageTxt.setBounds(new Rectangle(91, 2, 132, 23));
      this.messageTxt.setVisible(false);
      this.searchButton.setToolTipText("Search");
      this.searchButton.setIcon(this.searchIcon);
      this.searchButton.setBounds(new Rectangle(188, 2, 31, 24));
      this.searchButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ProgramComponent.this.searchButton_actionPerformed(var1);
         }
      });
      this.setForeground(Color.lightGray);
      this.setLayout((LayoutManager)null);
      this.nameLbl.setText("Program");
      this.nameLbl.setBounds(new Rectangle(5, 5, 73, 20));
      this.nameLbl.setFont(Utilities.labelsFont);
      this.clearButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ProgramComponent.this.clearButton_actionPerformed(var1);
         }
      });
      this.clearButton.setBounds(new Rectangle(154, 2, 31, 24));
      this.clearButton.setIcon(this.clearIcon);
      this.clearButton.setToolTipText("Clear");
      this.add(this.scrollPane, (Object)null);
      this.add(this.nameLbl, (Object)null);
      this.add(this.searchButton, (Object)null);
      this.add(this.clearButton, (Object)null);
      this.add(this.messageTxt, (Object)null);
      this.add(this.browseButton, (Object)null);
      this.determineColumnWidth();
      this.programTable.setTableHeader((JTableHeader)null);
      this.setBorder(BorderFactory.createEtchedBorder());
   }

   public boolean confirmBuiltInAccess() {
      String var1 = "No implementation was found for some functions which are called in the VM code.\nThe VM Emulator provides built-in implementations for the OS functions.\nIf available, should this built-in implementation be used for functions which were not implemented in the VM code?";
      return JOptionPane.showConfirmDialog(this.getParent(), var1, "Confirmation Message", 0, 3) == 0;
   }

   public void notify(String var1) {
      JOptionPane.showMessageDialog(this.getParent(), var1, "Information Message", 1);
   }

   class ColoredTableCellRenderer extends DefaultTableCellRenderer {
      public Component getTableCellRendererComponent(JTable var1, Object var2, boolean var3, boolean var4, int var5, int var6) {
         this.setEnabled(var1 == null || var1.isEnabled());
         this.setBackground((Color)null);
         this.setForeground((Color)null);
         if (var6 == 0) {
            this.setHorizontalAlignment(0);
         } else {
            this.setHorizontalAlignment(2);
         }

         if (var5 == ProgramComponent.this.instructionIndex) {
            this.setBackground(Color.yellow);
         } else {
            VMEmulatorInstruction var7 = ProgramComponent.this.instructions[var5];
            String var8 = var7.getFormattedStrings()[0];
            if (var8.equals("function") && (var6 == 1 || var6 == 2)) {
               this.setBackground(new Color(190, 171, 210));
            }
         }

         super.getTableCellRendererComponent(var1, var2, var3, var4, var5, var6);
         return this;
      }
   }

   class ProgramTableModel extends AbstractTableModel {
      public int getColumnCount() {
         return 3;
      }

      public int getRowCount() {
         return ProgramComponent.this.instructions.length;
      }

      public String getColumnName(int var1) {
         return null;
      }

      public Object getValueAt(int var1, int var2) {
         String[] var3 = ProgramComponent.this.instructions[var1].getFormattedStrings();
         switch(var2) {
         case 0:
            short var4 = ProgramComponent.this.instructions[var1].getIndexInFunction();
            if (var4 >= 0) {
               return new Short(var4);
            }

            return "";
         case 1:
            return var3[0];
         case 2:
            return var3[1] + " " + var3[2];
         default:
            return null;
         }
      }

      public boolean isCellEditable(int var1, int var2) {
         return false;
      }
   }
}
