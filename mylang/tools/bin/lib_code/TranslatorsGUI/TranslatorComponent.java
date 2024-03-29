package TranslatorsGUI;

import Hack.ComputerParts.TextFileGUI;
import Hack.Translators.HackTranslatorEvent;
import Hack.Translators.HackTranslatorEventListener;
import Hack.Translators.HackTranslatorGUI;
import HackGUI.HTMLViewFrame;
import HackGUI.MouseOverJButton;
import HackGUI.TextFileComponent;
import HackGUI.Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

public class TranslatorComponent extends JFrame implements HackTranslatorGUI {
   protected static final int TOOLBAR_WIDTH = 1016;
   protected static final int TOOLBAR_HEIGHT = 55;
   private static final int TRANSLATOR_WIDTH = 1024;
   private static final int TRANSLATOR_HEIGHT = 741;
   protected static final Dimension separatorDimension = new Dimension(3, 50);
   private Vector listeners;
   private MouseOverJButton loadButton;
   private MouseOverJButton saveButton;
   private MouseOverJButton ffwdButton;
   private MouseOverJButton stopButton;
   private MouseOverJButton singleStepButton;
   private MouseOverJButton rewindButton;
   private MouseOverJButton fullTranslationButton;
   private ImageIcon ffwdIcon = new ImageIcon("bin/images/vcrfastforward.gif");
   private ImageIcon stopIcon = new ImageIcon("bin/images/vcrstop.gif");
   private ImageIcon singleStepIcon = new ImageIcon("bin/images/vcrforward.gif");
   private ImageIcon rewindIcon = new ImageIcon("bin/images/vcrrewind.gif");
   private ImageIcon fullTranslationIcon = new ImageIcon("bin/images/hex.gif");
   private ImageIcon loadIcon = new ImageIcon("bin/images/opendoc.gif");
   private ImageIcon saveIcon = new ImageIcon("bin/images/save.gif");
   private ImageIcon arrowIcon = new ImageIcon("bin/images/arrow2.gif");
   protected JToolBar toolBar;
   protected JMenuBar menuBar;
   protected JMenu fileMenu;
   protected JMenu runMenu;
   protected JMenu helpMenu;
   protected JMenuItem loadSourceMenuItem;
   protected JMenuItem saveDestMenuItem;
   protected JMenuItem exitMenuItem;
   protected JMenuItem singleStepMenuItem;
   protected JMenuItem ffwdMenuItem;
   protected JMenuItem stopMenuItem;
   protected JMenuItem rewindMenuItem;
   protected JMenuItem fullTranslationMenuItem;
   protected JMenuItem aboutMenuItem;
   protected JMenuItem usageMenuItem;
   protected JFileChooser sourceFileChooser;
   protected JFileChooser destFileChooser;
   private JLabel arrowLabel;
   private JLabel messageLbl;
   private TextFileComponent source;
   protected TextFileComponent destination;
   protected FileFilter sourceFilter;
   protected FileFilter destFilter;
   private HTMLViewFrame usageWindow;
   private HTMLViewFrame aboutWindow;

   public TranslatorComponent(FileFilter var1, FileFilter var2) {
      this.sourceFilter = var1;
      this.destFilter = var2;
      this.init();
      this.jbInit();
      this.source.setName("Source");
      this.destination.setName("Destination");
      this.sourceFileChooser = new JFileChooser();
      this.sourceFileChooser.setFileFilter(var1);
      this.destFileChooser = new JFileChooser();
      this.destFileChooser.setFileFilter(var2);
      this.source.enableUserInput();
      this.destination.disableUserInput();
   }

   public void notifyHackTranslatorListeners(byte var1, Object var2) {
      HackTranslatorEvent var3 = new HackTranslatorEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((HackTranslatorEventListener)this.listeners.elementAt(var4)).actionPerformed(var3);
      }

   }

   public void removeHackTranslatorListener(HackTranslatorEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void addHackTranslatorListener(HackTranslatorEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void setWorkingDir(File var1) {
      this.sourceFileChooser.setCurrentDirectory(var1);
      this.destFileChooser.setCurrentDirectory(var1);
   }

   public void disableStop() {
      this.stopButton.setEnabled(false);
      this.stopMenuItem.setEnabled(false);
   }

   public void enableStop() {
      this.stopButton.setEnabled(true);
      this.stopMenuItem.setEnabled(true);
   }

   public void disableFastForward() {
      this.ffwdButton.setEnabled(false);
      this.ffwdMenuItem.setEnabled(false);
   }

   public void enableFastForward() {
      this.ffwdButton.setEnabled(true);
      this.ffwdMenuItem.setEnabled(true);
   }

   public void disableSingleStep() {
      this.singleStepButton.setEnabled(false);
      this.singleStepMenuItem.setEnabled(false);
   }

   public void enableSingleStep() {
      this.singleStepButton.setEnabled(true);
      this.singleStepMenuItem.setEnabled(true);
   }

   public void disableRewind() {
      this.rewindButton.setEnabled(false);
      this.rewindMenuItem.setEnabled(false);
   }

   public void enableRewind() {
      this.rewindButton.setEnabled(true);
      this.rewindMenuItem.setEnabled(true);
   }

   public void disableSave() {
      this.saveButton.setEnabled(false);
      this.saveDestMenuItem.setEnabled(false);
   }

   public void enableSave() {
      this.saveButton.setEnabled(true);
      this.saveDestMenuItem.setEnabled(true);
   }

   public void disableFullCompilation() {
      this.fullTranslationButton.setEnabled(false);
      this.fullTranslationMenuItem.setEnabled(false);
   }

   public void enableFullCompilation() {
      this.fullTranslationButton.setEnabled(true);
      this.fullTranslationMenuItem.setEnabled(true);
   }

   public void disableLoadSource() {
      this.loadButton.setEnabled(false);
      this.loadSourceMenuItem.setEnabled(false);
   }

   public void enableLoadSource() {
      this.loadButton.setEnabled(true);
      this.loadSourceMenuItem.setEnabled(true);
   }

   public void enableSourceRowSelection() {
      this.source.enableUserInput();
   }

   public void disableSourceRowSelection() {
      this.source.disableUserInput();
   }

   public void setSourceName(String var1) {
      this.sourceFileChooser.setName(var1);
      this.sourceFileChooser.setSelectedFile(new File(var1));
   }

   public void setDestinationName(String var1) {
      this.destFileChooser.setName(var1);
      this.destFileChooser.setSelectedFile(new File(var1));
   }

   public TextFileGUI getSource() {
      return this.source;
   }

   public TextFileGUI getDestination() {
      return this.destination;
   }

   public void setUsageFileName(String var1) {
      this.usageWindow = new HTMLViewFrame(var1);
      this.usageWindow.setSize(450, 430);
   }

   public void setAboutFileName(String var1) {
      this.aboutWindow = new HTMLViewFrame(var1);
      this.aboutWindow.setSize(450, 420);
   }

   public void displayMessage(String var1, boolean var2) {
      if (var2) {
         this.messageLbl.setForeground(Color.red);
      } else {
         this.messageLbl.setForeground(UIManager.getColor("Label.foreground"));
      }

      this.messageLbl.setText(var1);
   }

   private void loadSource() {
      int var1 = this.sourceFileChooser.showDialog(this, "Load Source File");
      if (var1 == 0) {
         this.notifyHackTranslatorListeners((byte)7, this.sourceFileChooser.getSelectedFile().getAbsolutePath());
      }

   }

   private void saveDest() {
      int var1 = this.destFileChooser.showDialog(this, "Save Destination File");
      if (var1 == 0) {
         if (this.destFileChooser.getSelectedFile().exists()) {
            Object[] var2 = new Object[]{"Yes", "No", "Cancel"};
            int var3 = JOptionPane.showOptionDialog(this, "File exists. Replace it ?", "Question", 1, 3, (Icon)null, var2, var2[2]);
            if (var3 != 0) {
               return;
            }
         }

         String var4 = this.destFileChooser.getSelectedFile().getAbsolutePath();
         this.notifyHackTranslatorListeners((byte)6, var4);
      }

   }

   protected void arrangeToolBar() {
      this.toolBar.setSize(new Dimension(1016, 55));
      this.toolBar.add(this.loadButton);
      this.toolBar.add(this.saveButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.singleStepButton);
      this.toolBar.add(this.ffwdButton);
      this.toolBar.add(this.stopButton);
      this.toolBar.add(this.rewindButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.fullTranslationButton);
   }

   protected void arrangeMenu() {
      this.fileMenu = new JMenu("File");
      this.fileMenu.setMnemonic(70);
      this.menuBar.add(this.fileMenu);
      this.runMenu = new JMenu("Run");
      this.runMenu.setMnemonic(82);
      this.menuBar.add(this.runMenu);
      this.helpMenu = new JMenu("Help");
      this.helpMenu.setMnemonic(72);
      this.menuBar.add(this.helpMenu);
      this.loadSourceMenuItem = new JMenuItem("Load Source file", 79);
      this.loadSourceMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.loadSourceMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.loadSourceMenuItem);
      this.saveDestMenuItem = new JMenuItem("Save Destination file", 83);
      this.saveDestMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.saveDestMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.saveDestMenuItem);
      this.fileMenu.addSeparator();
      this.exitMenuItem = new JMenuItem("Exit", 88);
      this.exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 8));
      this.exitMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.exitMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.exitMenuItem);
      this.singleStepMenuItem = new JMenuItem("Single Step", 83);
      this.singleStepMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.singleStepMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.singleStepMenuItem);
      this.ffwdMenuItem = new JMenuItem("Fast Forward", 70);
      this.ffwdMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.ffwdMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.ffwdMenuItem);
      this.stopMenuItem = new JMenuItem("Stop", 84);
      this.stopMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.stopMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.stopMenuItem);
      this.rewindMenuItem = new JMenuItem("Rewind", 82);
      this.rewindMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.rewindMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.rewindMenuItem);
      this.runMenu.addSeparator();
      this.fullTranslationMenuItem = new JMenuItem("Fast Translation", 85);
      this.fullTranslationMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.fullTranslationMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.fullTranslationMenuItem);
      this.usageMenuItem = new JMenuItem("Usage", 85);
      this.usageMenuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
      this.usageMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.usageMenuItem_actionPerformed(var1);
         }
      });
      this.helpMenu.add(this.usageMenuItem);
      this.aboutMenuItem = new JMenuItem("About...", 65);
      this.aboutMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.aboutMenuItem_actionPerformed(var1);
         }
      });
      this.helpMenu.add(this.aboutMenuItem);
   }

   protected void init() {
      this.toolBar = new JToolBar();
      this.menuBar = new JMenuBar();
      this.arrowLabel = new JLabel();
      this.messageLbl = new JLabel();
      this.listeners = new Vector();
      this.ffwdButton = new MouseOverJButton();
      this.rewindButton = new MouseOverJButton();
      this.stopButton = new MouseOverJButton();
      this.singleStepButton = new MouseOverJButton();
      this.fullTranslationButton = new MouseOverJButton();
      this.saveButton = new MouseOverJButton();
      this.loadButton = new MouseOverJButton();
      this.source = new TextFileComponent();
      this.destination = new TextFileComponent();
   }

   protected void jbInit() {
      this.getContentPane().setLayout((LayoutManager)null);
      this.loadButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.loadButton_actionPerformed(var1);
         }
      });
      this.loadButton.setMaximumSize(new Dimension(39, 39));
      this.loadButton.setMinimumSize(new Dimension(39, 39));
      this.loadButton.setPreferredSize(new Dimension(39, 39));
      this.loadButton.setSize(new Dimension(39, 39));
      this.loadButton.setToolTipText("Load Source File");
      this.loadButton.setIcon(this.loadIcon);
      this.saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.saveButton_actionPerformed(var1);
         }
      });
      this.saveButton.setMaximumSize(new Dimension(39, 39));
      this.saveButton.setMinimumSize(new Dimension(39, 39));
      this.saveButton.setPreferredSize(new Dimension(39, 39));
      this.saveButton.setSize(new Dimension(39, 39));
      this.saveButton.setToolTipText("Save Destination File");
      this.saveButton.setIcon(this.saveIcon);
      this.singleStepButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.singleStepButton_actionPerformed(var1);
         }
      });
      this.singleStepButton.setMaximumSize(new Dimension(39, 39));
      this.singleStepButton.setMinimumSize(new Dimension(39, 39));
      this.singleStepButton.setPreferredSize(new Dimension(39, 39));
      this.singleStepButton.setSize(new Dimension(39, 39));
      this.singleStepButton.setToolTipText("Single Step");
      this.singleStepButton.setIcon(this.singleStepIcon);
      this.ffwdButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.ffwdButton_actionPerformed(var1);
         }
      });
      this.ffwdButton.setMaximumSize(new Dimension(39, 39));
      this.ffwdButton.setMinimumSize(new Dimension(39, 39));
      this.ffwdButton.setPreferredSize(new Dimension(39, 39));
      this.ffwdButton.setSize(new Dimension(39, 39));
      this.ffwdButton.setToolTipText("Fast Forward");
      this.ffwdButton.setIcon(this.ffwdIcon);
      this.rewindButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.rewindButton_actionPerformed(var1);
         }
      });
      this.rewindButton.setMaximumSize(new Dimension(39, 39));
      this.rewindButton.setMinimumSize(new Dimension(39, 39));
      this.rewindButton.setPreferredSize(new Dimension(39, 39));
      this.rewindButton.setSize(new Dimension(39, 39));
      this.rewindButton.setToolTipText("Rewind");
      this.rewindButton.setIcon(this.rewindIcon);
      this.stopButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.stopButton_actionPerformed(var1);
         }
      });
      this.stopButton.setMaximumSize(new Dimension(39, 39));
      this.stopButton.setMinimumSize(new Dimension(39, 39));
      this.stopButton.setPreferredSize(new Dimension(39, 39));
      this.stopButton.setSize(new Dimension(39, 39));
      this.stopButton.setToolTipText("Stop");
      this.stopButton.setIcon(this.stopIcon);
      this.fullTranslationButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            TranslatorComponent.this.fullTranslationButton_actionPerformed(var1);
         }
      });
      this.fullTranslationButton.setMaximumSize(new Dimension(39, 39));
      this.fullTranslationButton.setMinimumSize(new Dimension(39, 39));
      this.fullTranslationButton.setPreferredSize(new Dimension(39, 39));
      this.fullTranslationButton.setSize(new Dimension(39, 39));
      this.fullTranslationButton.setToolTipText("Fast Translation");
      this.fullTranslationButton.setIcon(this.fullTranslationIcon);
      this.messageLbl.setFont(Utilities.statusLineFont);
      this.messageLbl.setBorder(BorderFactory.createLoweredBevelBorder());
      this.messageLbl.setBounds(new Rectangle(0, 672, 1016, 20));
      this.getContentPane().add(this.messageLbl, (Object)null);
      this.arrowLabel.setBounds(new Rectangle(290, 324, 88, 71));
      this.arrowLabel.setIcon(this.arrowIcon);
      this.source.setVisibleRows(31);
      this.destination.setVisibleRows(31);
      this.source.setBounds(new Rectangle(35, 100, this.source.getWidth(), this.source.getHeight()));
      this.destination.setBounds(new Rectangle(375, 100, this.destination.getWidth(), this.destination.getHeight()));
      this.getContentPane().add(this.source, (Object)null);
      this.getContentPane().add(this.destination, (Object)null);
      this.toolBar.setFloatable(false);
      this.toolBar.setLocation(0, 0);
      this.toolBar.setLayout(new FlowLayout(0, 3, 0));
      this.toolBar.setBorder(BorderFactory.createEtchedBorder());
      this.arrangeToolBar();
      this.getContentPane().add(this.toolBar, (Object)null);
      this.toolBar.revalidate();
      this.toolBar.repaint();
      this.repaint();
      this.arrangeMenu();
      this.setJMenuBar(this.menuBar);
      this.setDefaultCloseOperation(3);
      this.setSize(new Dimension(1024, 741));
      this.setVisible(true);
      this.getContentPane().add(this.arrowLabel, (Object)null);
   }

   public void singleStepButton_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)1, (Object)null);
   }

   public void ffwdButton_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)2, (Object)null);
   }

   public void stopButton_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)3, (Object)null);
   }

   public void rewindButton_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)4, (Object)null);
   }

   public void fullTranslationButton_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)5, (Object)null);
   }

   public void loadButton_actionPerformed(ActionEvent var1) {
      this.loadSource();
   }

   public void saveButton_actionPerformed(ActionEvent var1) {
      this.saveDest();
   }

   public void loadSourceMenuItem_actionPerformed(ActionEvent var1) {
      this.loadSource();
   }

   public void saveDestMenuItem_actionPerformed(ActionEvent var1) {
      this.saveDest();
   }

   public void exitMenuItem_actionPerformed(ActionEvent var1) {
      System.exit(0);
   }

   public void singleStepMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)1, (Object)null);
   }

   public void ffwdMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)2, (Object)null);
   }

   public void stopMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)3, (Object)null);
   }

   public void rewindMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)4, (Object)null);
   }

   public void fullTranslationMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyHackTranslatorListeners((byte)5, (Object)null);
   }

   public void usageMenuItem_actionPerformed(ActionEvent var1) {
      if (this.usageWindow != null) {
         this.usageWindow.setVisible(true);
      }

   }

   public void aboutMenuItem_actionPerformed(ActionEvent var1) {
      if (this.aboutWindow != null) {
         this.aboutWindow.setVisible(true);
      }

   }
}
