package SimulatorsGUI;

import Hack.Assembler.AssemblerException;
import Hack.Assembler.HackAssemblerTranslator;
import Hack.CPUEmulator.ROMGUI;
import Hack.Events.ProgramEvent;
import Hack.Events.ProgramEventListener;
import HackGUI.Format;
import HackGUI.MouseOverJButton;
import HackGUI.PointedMemoryComponent;
import HackGUI.TranslationException;
import HackGUI.Utilities;
import HackGUI.PointedMemoryComponent.PointedMemoryTableCellRenderer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableCellRenderer;

public class ROMComponent extends PointedMemoryComponent implements ROMGUI {
   private Vector programEventListeners;
   private static final int ASM_FORMAT = 4;
   protected MouseOverJButton loadButton = new MouseOverJButton();
   private ImageIcon loadIcon = new ImageIcon("bin/images/open2.gif");
   private FileFilter filter;
   private JFileChooser fileChooser;
   private HackAssemblerTranslator translator = HackAssemblerTranslator.getInstance();
   private JTextField messageTxt = new JTextField();
   private String[] format = new String[]{"Asm", "Dec", "Hex", "Bin"};
   protected JComboBox romFormat;
   private String programFileName;

   public ROMComponent() {
      this.romFormat = new JComboBox(this.format);
      this.dataFormat = 4;
      this.programEventListeners = new Vector();
      this.filter = new ROMFileFilter();
      this.fileChooser = new JFileChooser();
      this.fileChooser.setFileFilter(this.filter);
      this.jbInit();
   }

   public void setNumericFormat(int var1) {
      super.setNumericFormat(var1);
      switch(var1) {
      case 0:
         this.romFormat.setSelectedIndex(1);
         break;
      case 1:
         this.romFormat.setSelectedIndex(2);
         break;
      case 2:
         this.romFormat.setSelectedIndex(3);
      case 3:
      default:
         break;
      case 4:
         this.romFormat.setSelectedIndex(0);
      }

   }

   public void addProgramListener(ProgramEventListener var1) {
      this.programEventListeners.addElement(var1);
   }

   public void removeProgramListener(ProgramEventListener var1) {
      this.programEventListeners.removeElement(var1);
   }

   public void notifyProgramListeners(byte var1, String var2) {
      ProgramEvent var3 = new ProgramEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.programEventListeners.size(); ++var4) {
         ((ProgramEventListener)this.programEventListeners.elementAt(var4)).programChanged(var3);
      }

   }

   public void notifyClearListeners() {
      super.notifyClearListeners();
      this.notifyProgramListeners((byte)3, (String)null);
   }

   protected DefaultTableCellRenderer getCellRenderer() {
      return new ROMComponent.ROMTableCellRenderer();
   }

   public void setProgram(String var1) {
      this.programFileName = var1;
   }

   public String getValueAsString(int var1) {
      return this.dataFormat != 4 ? super.getValueAsString(var1) : Format.translateValueToString(this.values[var1], 0);
   }

   public void hideMessage() {
      this.messageTxt.setText("");
      this.messageTxt.setVisible(false);
      this.loadButton.setVisible(true);
      this.searchButton.setVisible(true);
      this.romFormat.setVisible(true);
   }

   public void showMessage(String var1) {
      this.messageTxt.setText(var1);
      this.loadButton.setVisible(false);
      this.searchButton.setVisible(false);
      this.romFormat.setVisible(false);
      this.messageTxt.setVisible(true);
   }

   protected short translateValueToShort(String var1) throws TranslationException {
      boolean var2 = false;
      short var5;
      if (this.dataFormat != 4) {
         var5 = super.translateValueToShort(var1);
      } else {
         try {
            var5 = this.translator.textToCode(var1);
         } catch (AssemblerException var4) {
            throw new TranslationException(var4.getMessage());
         }
      }

      return var5;
   }

   protected String translateValueToString(short var1) {
      String var2 = null;
      if (this.dataFormat != 4) {
         var2 = super.translateValueToString(var1);
      } else {
         try {
            var2 = this.translator.codeToText(var1);
         } catch (AssemblerException var4) {
         }
      }

      return var2;
   }

   private void jbInit() {
      this.loadButton.setIcon(this.loadIcon);
      this.loadButton.setBounds(new Rectangle(97, 2, 31, 25));
      this.loadButton.setToolTipText("Load Program");
      this.loadButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ROMComponent.this.loadButton_actionPerformed(var1);
         }
      });
      this.messageTxt.setBackground(SystemColor.info);
      this.messageTxt.setEnabled(false);
      this.messageTxt.setFont(Utilities.labelsFont);
      this.messageTxt.setPreferredSize(new Dimension(70, 20));
      this.messageTxt.setDisabledTextColor(Color.red);
      this.messageTxt.setEditable(false);
      this.messageTxt.setHorizontalAlignment(0);
      this.messageTxt.setBounds(new Rectangle(37, 3, 154, 22));
      this.messageTxt.setVisible(false);
      this.romFormat.setPreferredSize(new Dimension(125, 23));
      this.romFormat.setBounds(new Rectangle(39, 3, 56, 23));
      this.romFormat.setFont(Utilities.thinLabelsFont);
      this.romFormat.setToolTipText("Display Format");
      this.romFormat.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ROMComponent.this.romFormat_actionPerformed(var1);
         }
      });
      this.add(this.messageTxt, (Object)null);
      this.add(this.loadButton);
      this.add(this.romFormat, (Object)null);
   }

   public void setWorkingDir(File var1) {
      this.fileChooser.setCurrentDirectory(var1);
   }

   public void loadProgram() {
      int var1 = this.fileChooser.showDialog(this, "Load ROM");
      if (var1 == 0) {
         this.notifyProgramListeners((byte)1, this.fileChooser.getSelectedFile().getAbsolutePath());
      }

   }

   public void loadButton_actionPerformed(ActionEvent var1) {
      this.loadProgram();
   }

   public void romFormat_actionPerformed(ActionEvent var1) {
      String var2 = (String)this.romFormat.getSelectedItem();
      if (var2.equals(this.format[0])) {
         this.setNumericFormat(4);
      } else if (var2.equals(this.format[1])) {
         this.setNumericFormat(0);
      } else if (var2.equals(this.format[2])) {
         this.setNumericFormat(1);
      } else if (var2.equals(this.format[3])) {
         this.setNumericFormat(2);
      }

   }

   public class ROMTableCellRenderer extends PointedMemoryTableCellRenderer {
      public ROMTableCellRenderer() {
         super(ROMComponent.this);
      }

      public void setRenderer(int var1, int var2) {
         super.setRenderer(var1, var2);
         if (ROMComponent.this.dataFormat == 4 && var2 == 1) {
            this.setHorizontalAlignment(2);
         }

      }
   }
}
