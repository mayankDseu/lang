package SimulatorsGUI;

import Hack.VirtualMachine.HVMInstruction;
import HackGUI.Utilities;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

public class SearchProgramWindow extends JFrame {
   private JLabel instructionLbl = new JLabel();
   private JTextField instruction = new JTextField();
   private JButton okButton = new JButton();
   private JButton cancelButton = new JButton();
   private ImageIcon okIcon = new ImageIcon("bin/images/ok.gif");
   private ImageIcon cancelIcon = new ImageIcon("bin/images/cancel.gif");
   private JTable table;
   private HVMInstruction[] instructions;

   public SearchProgramWindow(JTable var1, HVMInstruction[] var2) {
      super("Search");
      this.table = var1;
      this.jbInit();
   }

   public void setInstructions(HVMInstruction[] var1) {
      this.instructions = var1;
   }

   private int getSearchedRowIndex() {
      int var1 = -1;
      String var2 = this.instruction.getText();
      StringTokenizer var3 = new StringTokenizer(var2);
      String var4 = "";
      String var5 = "";
      String var6 = "";
      int var7 = var3.countTokens();
      int var8;
      String[] var9;
      switch(var7) {
      case 0:
      default:
         break;
      case 1:
         var4 = var3.nextToken();

         for(var8 = 0; var8 < this.instructions.length; ++var8) {
            var9 = this.instructions[var8].getFormattedStrings();
            if (var9[0].equalsIgnoreCase(var4)) {
               var1 = var8;
               return var1;
            }
         }

         return var1;
      case 2:
         var4 = var3.nextToken();
         var5 = var3.nextToken();

         for(var8 = 0; var8 < this.instructions.length; ++var8) {
            var9 = this.instructions[var8].getFormattedStrings();
            if (var9[0].equalsIgnoreCase(var4) && var9[1].equalsIgnoreCase(var5)) {
               var1 = var8;
               return var1;
            }
         }

         return var1;
      case 3:
         var4 = var3.nextToken();
         var5 = var3.nextToken();
         var6 = var3.nextToken();

         for(var8 = 0; var8 < this.instructions.length; ++var8) {
            var9 = this.instructions[var8].getFormattedStrings();
            if (var9[0].equalsIgnoreCase(var4) && var9[1].equalsIgnoreCase(var5) && var9[2].equalsIgnoreCase(var6)) {
               var1 = var8;
               break;
            }
         }
      }

      return var1;
   }

   public void showWindow() {
      this.setVisible(true);
      this.instruction.requestFocus();
   }

   private void jbInit() {
      this.instructionLbl.setFont(Utilities.thinLabelsFont);
      this.instructionLbl.setText("Text to find :");
      this.instructionLbl.setBounds(new Rectangle(9, 22, 79, 23));
      this.getContentPane().setLayout((LayoutManager)null);
      this.instruction.setBounds(new Rectangle(82, 25, 220, 18));
      this.instruction.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchProgramWindow.this.instruction_actionPerformed(var1);
         }
      });
      this.okButton.setToolTipText("OK");
      this.okButton.setIcon(this.okIcon);
      this.okButton.setBounds(new Rectangle(66, 68, 63, 44));
      this.okButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchProgramWindow.this.okButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setBounds(new Rectangle(190, 68, 63, 44));
      this.cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            SearchProgramWindow.this.cancelButton_actionPerformed(var1);
         }
      });
      this.cancelButton.setToolTipText("CANCEL");
      this.cancelButton.setIcon(this.cancelIcon);
      this.getContentPane().add(this.instruction, (Object)null);
      this.getContentPane().add(this.okButton, (Object)null);
      this.getContentPane().add(this.cancelButton, (Object)null);
      this.getContentPane().add(this.instructionLbl, (Object)null);
      this.setSize(320, 150);
      this.setLocation(250, 250);
   }

   public void okButton_actionPerformed(ActionEvent var1) {
      try {
         int var2 = this.getSearchedRowIndex();
         if (var2 != -1) {
            Rectangle var3 = this.table.getCellRect(var2, 0, true);
            this.table.scrollRectToVisible(var3);
            this.table.setRowSelectionInterval(var2, var2);
            this.setVisible(false);
         }
      } catch (NumberFormatException var4) {
      } catch (IllegalArgumentException var5) {
      }

   }

   public void cancelButton_actionPerformed(ActionEvent var1) {
      this.setVisible(false);
   }

   public void instruction_actionPerformed(ActionEvent var1) {
      this.okButton_actionPerformed(var1);
   }
}
