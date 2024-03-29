package AssemblerGUI;

import Hack.Assembler.HackAssemblerGUI;
import Hack.ComputerParts.TextFileGUI;
import HackGUI.MouseOverJButton;
import HackGUI.TextFileComponent;
import TranslatorsGUI.TranslatorComponent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

public class AssemblerComponent extends TranslatorComponent implements HackAssemblerGUI {
   private ImageIcon equalIcon;
   private MouseOverJButton compareButton;
   private JMenuItem loadCompareMenuItem;
   private JLabel equalLabel;
   private TextFileComponent comparison;
   protected JFileChooser compareFileChooser;

   public AssemblerComponent() {
      super(new ASMFileFilter(), new HACKFileFilter());
      this.comparison.disableUserInput();
      this.comparison.setName("Comparison");
      this.compareFileChooser = new JFileChooser();
      this.compareFileChooser.setFileFilter(this.destFilter);
   }

   public void setWorkingDir(File var1) {
      super.setWorkingDir(var1);
      this.compareFileChooser.setCurrentDirectory(var1);
   }

   public void disableLoadComparison() {
      this.compareButton.setEnabled(false);
      this.loadCompareMenuItem.setEnabled(false);
   }

   public void enableLoadComparison() {
      this.compareButton.setEnabled(true);
      this.loadCompareMenuItem.setEnabled(true);
   }

   public void setComparisonName(String var1) {
      this.compareFileChooser.setName(var1);
      this.compareFileChooser.setSelectedFile(new File(var1));
   }

   public void showComparison() {
      this.comparison.setVisible(true);
      this.equalLabel.setVisible(true);
   }

   public void hideComparison() {
      this.comparison.setVisible(false);
      this.equalLabel.setVisible(false);
   }

   public TextFileGUI getComparison() {
      return this.comparison;
   }

   protected void arrangeMenu() {
      super.arrangeMenu();
      this.fileMenu.removeAll();
      this.fileMenu.add(this.loadSourceMenuItem);
      this.loadCompareMenuItem = new JMenuItem("Load Comparison File", 67);
      this.loadCompareMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AssemblerComponent.this.loadCompareMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.saveDestMenuItem);
      this.fileMenu.add(this.loadCompareMenuItem);
      this.fileMenu.addSeparator();
      this.fileMenu.add(this.exitMenuItem);
   }

   protected void init() {
      super.init();
      this.equalIcon = new ImageIcon("bin/images/equal.gif");
      this.equalLabel = new JLabel();
      this.comparison = new TextFileComponent();
      this.compareButton = new MouseOverJButton();
   }

   private void loadComparison() {
      int var1 = this.compareFileChooser.showDialog(this, "Load Comparison File");
      if (var1 == 0) {
         this.notifyHackTranslatorListeners((byte)9, this.compareFileChooser.getSelectedFile().getAbsolutePath());
      }

   }

   protected void arrangeToolBar() {
      super.arrangeToolBar();
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.compareButton);
   }

   protected void jbInit() {
      super.jbInit();
      this.equalLabel.setBounds(new Rectangle(632, 324, 88, 71));
      this.equalLabel.setIcon(this.equalIcon);
      this.equalLabel.setVisible(false);
      this.comparison.setVisibleRows(31);
      this.comparison.setVisible(false);
      this.comparison.setBounds(new Rectangle(725, 100, this.comparison.getWidth(), this.comparison.getHeight()));
      this.compareButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            AssemblerComponent.this.loadCompareButton_actionPerformed(var1);
         }
      });
      this.compareButton.setMaximumSize(new Dimension(39, 39));
      this.compareButton.setMinimumSize(new Dimension(39, 39));
      this.compareButton.setPreferredSize(new Dimension(39, 39));
      this.compareButton.setSize(new Dimension(39, 39));
      this.compareButton.setToolTipText("Load Comparison File");
      this.compareButton.setIcon(new ImageIcon("bin/images/smallequal.gif"));
      this.getContentPane().add(this.equalLabel, (Object)null);
      this.getContentPane().add(this.comparison, (Object)null);
   }

   public void loadCompareMenuItem_actionPerformed(ActionEvent var1) {
      this.loadComparison();
   }

   public void loadCompareButton_actionPerformed(ActionEvent var1) {
      this.loadComparison();
   }
}
