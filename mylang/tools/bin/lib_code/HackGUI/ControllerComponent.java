package HackGUI;

import Hack.Controller.ControllerEvent;
import Hack.Controller.ControllerEventListener;
import Hack.Controller.ControllerGUI;
import Hack.Controller.HackSimulatorGUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControllerComponent extends JFrame implements ControllerGUI, FilesTypeListener, BreakpointsChangedListener {
   protected static final int TOOLBAR_WIDTH = 1016;
   protected static final int TOOLBAR_HEIGHT = 55;
   private static final int CONTROLLER_WIDTH = 1024;
   private static final int CONTROLLER_HEIGHT = 741;
   protected static final Dimension separatorDimension = new Dimension(3, 50);
   private Vector listeners = new Vector();
   protected MouseOverJButton ffwdButton;
   protected MouseOverJButton stopButton;
   protected MouseOverJButton rewindButton;
   protected MouseOverJButton scriptButton;
   protected MouseOverJButton breakButton;
   protected MouseOverJButton singleStepButton;
   protected MouseOverJButton loadProgramButton;
   private JFileChooser fileChooser = new JFileChooser();
   private BreakpointWindow breakpointWindow = new BreakpointWindow();
   private ImageIcon rewindIcon = new ImageIcon("bin/images/vcrrewind.gif");
   private ImageIcon ffwdIcon = new ImageIcon("bin/images/vcrfastforward.gif");
   private ImageIcon singleStepIcon = new ImageIcon("bin/images/vcrforward.gif");
   private ImageIcon stopIcon = new ImageIcon("bin/images/vcrstop.gif");
   private ImageIcon breakIcon = new ImageIcon("bin/images/redflag.gif");
   private ImageIcon loadProgramIcon = new ImageIcon("bin/images/opendoc.gif");
   private ImageIcon scriptIcon = new ImageIcon("bin/images/scroll.gif");
   protected JSlider speedSlider;
   protected TitledComboBox formatCombo = new TitledComboBox("Format:", "Numeric display format", new String[]{"Decimal", "Hexa", "Binary"}, 75);
   protected TitledComboBox additionalDisplayCombo = new TitledComboBox("View:", "View options", new String[]{"Script", "Output", "Compare", "Screen"}, 80);
   protected TitledComboBox animationCombo = new TitledComboBox("Animate:", "Animtion type", new String[]{"Program flow", "Program & data flow", "No animation"}, 135);
   protected JToolBar toolBar;
   protected JMenuBar menuBar;
   protected JMenu fileMenu;
   protected JMenu viewMenu;
   protected JMenu runMenu;
   protected JMenu helpMenu;
   protected JMenuItem singleStepMenuItem;
   protected JMenuItem ffwdMenuItem;
   protected JMenuItem stopMenuItem;
   protected JMenuItem rewindMenuItem;
   protected JMenuItem exitMenuItem;
   protected JMenuItem usageMenuItem;
   protected JMenuItem aboutMenuItem;
   protected JMenu animationSubMenu;
   protected JMenu numericFormatSubMenu;
   protected JMenu additionalDisplaySubMenu;
   protected JMenuItem breakpointsMenuItem;
   protected JMenuItem scriptMenuItem;
   protected JMenuItem programMenuItem;
   protected JRadioButtonMenuItem decMenuItem;
   protected JRadioButtonMenuItem hexaMenuItem;
   protected JRadioButtonMenuItem binMenuItem;
   protected JRadioButtonMenuItem scriptDisplayMenuItem;
   protected JRadioButtonMenuItem outputMenuItem;
   protected JRadioButtonMenuItem compareMenuItem;
   protected JRadioButtonMenuItem noAdditionalDisplayMenuItem;
   protected JRadioButtonMenuItem partAnimMenuItem;
   protected JRadioButtonMenuItem fullAnimMenuItem;
   protected JRadioButtonMenuItem noAnimMenuItem;
   protected JLabel messageLbl = new JLabel();
   protected FileDisplayComponent scriptComponent = new FileDisplayComponent();
   protected FileDisplayComponent outputComponent = new FileDisplayComponent();
   protected FileDisplayComponent comparisonComponent = new FileDisplayComponent();
   private HTMLViewFrame usageWindow;
   private HTMLViewFrame aboutWindow;

   public ControllerComponent() {
      this.init();
      this.jbInit();
   }

   public void setWorkingDir(File var1) {
      this.fileChooser.setCurrentDirectory(var1);
   }

   public void setSimulator(HackSimulatorGUI var1) {
      ((JComponent)var1).setLocation(0, 55);
      this.getContentPane().add((JComponent)var1, (Object)null);
      ((JComponent)var1).revalidate();
      this.repaint();
      if (var1.getUsageFileName() != null) {
         this.usageWindow = new HTMLViewFrame(var1.getUsageFileName());
         this.usageWindow.setSize(450, 430);
      }

      if (var1.getAboutFileName() != null) {
         this.aboutWindow = new HTMLViewFrame(var1.getAboutFileName());
         this.aboutWindow.setSize(450, 420);
      }

   }

   public JComponent getComparisonComponent() {
      return this.comparisonComponent;
   }

   public JComponent getOutputComponent() {
      return this.outputComponent;
   }

   public JComponent getScriptComponent() {
      return this.scriptComponent;
   }

   protected void init() {
      this.speedSlider = new JSlider(0, 1, 5, 1);
      this.loadProgramButton = new MouseOverJButton();
      this.ffwdButton = new MouseOverJButton();
      this.stopButton = new MouseOverJButton();
      this.rewindButton = new MouseOverJButton();
      this.scriptButton = new MouseOverJButton();
      this.breakButton = new MouseOverJButton();
      this.singleStepButton = new MouseOverJButton();
   }

   public void addControllerListener(ControllerEventListener var1) {
      this.listeners.addElement(var1);
   }

   public void removeControllerListener(ControllerEventListener var1) {
      this.listeners.removeElement(var1);
   }

   public void notifyControllerListeners(byte var1, Object var2) {
      ControllerEvent var3 = new ControllerEvent(this, var1, var2);

      for(int var4 = 0; var4 < this.listeners.size(); ++var4) {
         ((ControllerEventListener)this.listeners.elementAt(var4)).actionPerformed(var3);
      }

   }

   public void setScriptFile(String var1) {
      this.scriptComponent.setContents(var1);
   }

   public void setOutputFile(String var1) {
      this.outputComponent.setContents(var1);
   }

   public void setComparisonFile(String var1) {
      this.comparisonComponent.setContents(var1);
   }

   public void setCurrentScriptLine(int var1) {
      this.scriptComponent.setSelectedRow(var1);
   }

   public void setCurrentOutputLine(int var1) {
      this.outputComponent.setSelectedRow(var1);
   }

   public void setCurrentComparisonLine(int var1) {
      this.comparisonComponent.setSelectedRow(var1);
   }

   public void showBreakpoints() {
      this.breakpointWindow.getTable().clearSelection();
      this.breakpointWindow.setVisible(true);
      if (this.breakpointWindow.getState() == 1) {
         this.breakpointWindow.setState(0);
      }

   }

   public void enableSingleStep() {
      this.singleStepButton.setEnabled(true);
      this.singleStepMenuItem.setEnabled(true);
   }

   public void disableSingleStep() {
      this.singleStepButton.setEnabled(false);
      this.singleStepMenuItem.setEnabled(false);
   }

   public void enableFastForward() {
      this.ffwdButton.setEnabled(true);
      this.ffwdMenuItem.setEnabled(true);
   }

   public void disableFastForward() {
      this.ffwdButton.setEnabled(false);
      this.ffwdMenuItem.setEnabled(false);
   }

   public void enableStop() {
      this.stopButton.setEnabled(true);
      this.stopMenuItem.setEnabled(true);
   }

   public void disableStop() {
      this.stopButton.setEnabled(false);
      this.stopMenuItem.setEnabled(false);
   }

   public void enableScript() {
      this.scriptButton.setEnabled(true);
      this.scriptMenuItem.setEnabled(true);
   }

   public void disableScript() {
      this.scriptButton.setEnabled(false);
      this.scriptMenuItem.setEnabled(false);
   }

   public void enableRewind() {
      this.rewindButton.setEnabled(true);
      this.rewindMenuItem.setEnabled(true);
   }

   public void disableRewind() {
      this.rewindButton.setEnabled(false);
      this.rewindMenuItem.setEnabled(false);
   }

   public void enableLoadProgram() {
      this.loadProgramButton.setEnabled(true);
   }

   public void disableLoadProgram() {
      this.loadProgramButton.setEnabled(false);
   }

   public void disableSpeedSlider() {
      this.speedSlider.setEnabled(false);
   }

   public void enableSpeedSlider() {
      this.speedSlider.setEnabled(true);
   }

   public void disableAnimationModes() {
      this.animationCombo.setEnabled(false);
      this.partAnimMenuItem.setEnabled(false);
      this.fullAnimMenuItem.setEnabled(false);
      this.noAnimMenuItem.setEnabled(false);
   }

   public void enableAnimationModes() {
      this.animationCombo.setEnabled(true);
      this.partAnimMenuItem.setEnabled(true);
      this.fullAnimMenuItem.setEnabled(true);
      this.noAnimMenuItem.setEnabled(true);
   }

   public void setBreakpoints(Vector var1) {
      this.breakpointWindow.setBreakpoints(var1);
   }

   public void setSpeed(int var1) {
      this.speedSlider.setValue(var1);
      this.repaint();
   }

   public void setVariables(String[] var1) {
      this.breakpointWindow.setVariables(var1);
   }

   public void filesNamesChanged(FilesTypeEvent var1) {
      if (var1.getFirstFile() != null) {
         this.scriptComponent.setContents(var1.getFirstFile());
         this.notifyControllerListeners((byte)6, var1.getFirstFile());
      }

      if (var1.getSecondFile() != null) {
         this.outputComponent.setContents(var1.getSecondFile());
      }

      if (var1.getThirdFile() != null) {
         this.comparisonComponent.setContents(var1.getThirdFile());
      }

   }

   public void breakpointsChanged(BreakpointsChangedEvent var1) {
      this.notifyControllerListeners((byte)5, var1.getBreakpoints());
   }

   public void outputFileUpdated() {
      this.outputComponent.refresh();
   }

   public void hideController() {
      this.setVisible(false);
   }

   public void showController() {
      this.setVisible(true);
   }

   public void setAnimationMode(int var1) {
      if (!this.animationCombo.isSelectedIndex(var1)) {
         this.animationCombo.setSelectedIndex(var1);
      }

   }

   public void setAdditionalDisplay(int var1) {
      if (!this.additionalDisplayCombo.isSelectedIndex(var1)) {
         this.additionalDisplayCombo.setSelectedIndex(var1);
      }

   }

   public void setNumericFormat(int var1) {
      if (!this.formatCombo.isSelectedIndex(var1)) {
         this.formatCombo.setSelectedIndex(var1);
      }

   }

   public void displayMessage(String var1, boolean var2) {
      if (var2) {
         this.messageLbl.setForeground(Color.red);
      } else {
         this.messageLbl.setForeground(UIManager.getColor("Label.foreground"));
      }

      this.messageLbl.setText(var1);
      this.messageLbl.setToolTipText(var1);
   }

   protected void setControllerSize() {
      this.setSize(new Dimension(1024, 741));
   }

   protected void arrangeToolBar() {
      this.toolBar.add(this.loadProgramButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.singleStepButton);
      this.toolBar.add(this.ffwdButton);
      this.toolBar.add(this.stopButton);
      this.toolBar.add(this.rewindButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.scriptButton);
      this.toolBar.add(this.breakButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.speedSlider);
      this.toolBar.add(this.animationCombo);
      this.toolBar.add(this.additionalDisplayCombo);
      this.toolBar.add(this.formatCombo);
   }

   protected void arrangeMenu() {
      this.fileMenu = new JMenu("File");
      this.fileMenu.setMnemonic(70);
      this.menuBar.add(this.fileMenu);
      this.viewMenu = new JMenu("View");
      this.viewMenu.setMnemonic(86);
      this.menuBar.add(this.viewMenu);
      this.runMenu = new JMenu("Run");
      this.runMenu.setMnemonic(82);
      this.menuBar.add(this.runMenu);
      this.helpMenu = new JMenu("Help");
      this.helpMenu.setMnemonic(72);
      this.menuBar.add(this.helpMenu);
      this.programMenuItem = new JMenuItem("Load Program", 79);
      this.programMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.programMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.programMenuItem);
      this.scriptMenuItem = new JMenuItem("Load Script", 80);
      this.scriptMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.scriptMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.scriptMenuItem);
      this.fileMenu.addSeparator();
      this.exitMenuItem = new JMenuItem("Exit", 88);
      this.exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(88, 8));
      this.exitMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.exitMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.exitMenuItem);
      this.viewMenu.addSeparator();
      ButtonGroup var1 = new ButtonGroup();
      this.animationSubMenu = new JMenu("Animate");
      this.animationSubMenu.setMnemonic(65);
      this.viewMenu.add(this.animationSubMenu);
      this.partAnimMenuItem = new JRadioButtonMenuItem("Program flow");
      this.partAnimMenuItem.setMnemonic(80);
      this.partAnimMenuItem.setSelected(true);
      this.partAnimMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.partAnimMenuItem_actionPerformed(var1);
         }
      });
      var1.add(this.partAnimMenuItem);
      this.animationSubMenu.add(this.partAnimMenuItem);
      this.fullAnimMenuItem = new JRadioButtonMenuItem("Program & data flow");
      this.fullAnimMenuItem.setMnemonic(68);
      this.fullAnimMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.fullAnimMenuItem_actionPerformed(var1);
         }
      });
      var1.add(this.fullAnimMenuItem);
      this.animationSubMenu.add(this.fullAnimMenuItem);
      this.noAnimMenuItem = new JRadioButtonMenuItem("No Animation");
      this.noAnimMenuItem.setMnemonic(78);
      this.noAnimMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.noAnimMenuItem_actionPerformed(var1);
         }
      });
      var1.add(this.noAnimMenuItem);
      this.animationSubMenu.add(this.noAnimMenuItem);
      ButtonGroup var2 = new ButtonGroup();
      this.additionalDisplaySubMenu = new JMenu("View");
      this.additionalDisplaySubMenu.setMnemonic(86);
      this.viewMenu.add(this.additionalDisplaySubMenu);
      this.scriptDisplayMenuItem = new JRadioButtonMenuItem("Script");
      this.scriptDisplayMenuItem.setMnemonic(83);
      this.scriptDisplayMenuItem.setSelected(true);
      this.scriptDisplayMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.scriptDisplayMenuItem_actionPerformed(var1);
         }
      });
      var2.add(this.scriptDisplayMenuItem);
      this.additionalDisplaySubMenu.add(this.scriptDisplayMenuItem);
      this.outputMenuItem = new JRadioButtonMenuItem("Output");
      this.outputMenuItem.setMnemonic(79);
      this.outputMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.outputMenuItem_actionPerformed(var1);
         }
      });
      var2.add(this.outputMenuItem);
      this.additionalDisplaySubMenu.add(this.outputMenuItem);
      this.compareMenuItem = new JRadioButtonMenuItem("Compare");
      this.compareMenuItem.setMnemonic(67);
      this.compareMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.compareMenuItem_actionPerformed(var1);
         }
      });
      var2.add(this.compareMenuItem);
      this.additionalDisplaySubMenu.add(this.compareMenuItem);
      this.noAdditionalDisplayMenuItem = new JRadioButtonMenuItem("Screen");
      this.noAdditionalDisplayMenuItem.setMnemonic(78);
      this.noAdditionalDisplayMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.noAdditionalDisplayMenuItem_actionPerformed(var1);
         }
      });
      var2.add(this.noAdditionalDisplayMenuItem);
      this.additionalDisplaySubMenu.add(this.noAdditionalDisplayMenuItem);
      ButtonGroup var3 = new ButtonGroup();
      this.numericFormatSubMenu = new JMenu("Format");
      this.numericFormatSubMenu.setMnemonic(70);
      this.viewMenu.add(this.numericFormatSubMenu);
      this.decMenuItem = new JRadioButtonMenuItem("Decimal");
      this.decMenuItem.setMnemonic(68);
      this.decMenuItem.setSelected(true);
      this.decMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.decMenuItem_actionPerformed(var1);
         }
      });
      var3.add(this.decMenuItem);
      this.numericFormatSubMenu.add(this.decMenuItem);
      this.hexaMenuItem = new JRadioButtonMenuItem("Hexadecimal");
      this.hexaMenuItem.setMnemonic(72);
      this.hexaMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.hexaMenuItem_actionPerformed(var1);
         }
      });
      var3.add(this.hexaMenuItem);
      this.numericFormatSubMenu.add(this.hexaMenuItem);
      this.binMenuItem = new JRadioButtonMenuItem("Binary");
      this.binMenuItem.setMnemonic(66);
      this.binMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.binMenuItem_actionPerformed(var1);
         }
      });
      var3.add(this.binMenuItem);
      this.numericFormatSubMenu.add(this.binMenuItem);
      this.viewMenu.addSeparator();
      this.singleStepMenuItem = new JMenuItem("Single Step", 83);
      this.singleStepMenuItem.setAccelerator(KeyStroke.getKeyStroke("F11"));
      this.singleStepMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.singleStepMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.singleStepMenuItem);
      this.ffwdMenuItem = new JMenuItem("Run", 70);
      this.ffwdMenuItem.setAccelerator(KeyStroke.getKeyStroke("F5"));
      this.ffwdMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.ffwdMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.ffwdMenuItem);
      this.stopMenuItem = new JMenuItem("Stop", 84);
      this.stopMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift F5"));
      this.stopMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.stopMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.stopMenuItem);
      this.rewindMenuItem = new JMenuItem("Reset", 82);
      this.rewindMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.rewindMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.rewindMenuItem);
      this.runMenu.addSeparator();
      this.breakpointsMenuItem = new JMenuItem("Breakpoints", 66);
      this.breakpointsMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.breakpointsMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.breakpointsMenuItem);
      this.usageMenuItem = new JMenuItem("Usage", 85);
      this.usageMenuItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
      this.usageMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.usageMenuItem_actionPerformed(var1);
         }
      });
      this.helpMenu.add(this.usageMenuItem);
      this.aboutMenuItem = new JMenuItem("About ...", 65);
      this.aboutMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.aboutMenuItem_actionPerformed(var1);
         }
      });
      this.helpMenu.add(this.aboutMenuItem);
   }

   private void scriptPressed() {
      int var1 = this.fileChooser.showDialog(this, "Load Script");
      if (var1 == 0) {
         this.notifyControllerListeners((byte)6, this.fileChooser.getSelectedFile().getAbsoluteFile());
         this.scriptComponent.setContents(this.fileChooser.getSelectedFile().getAbsolutePath());
      }

   }

   private void jbInit() {
      this.fileChooser.setFileFilter(new ScriptFileFilter());
      this.getContentPane().setLayout((LayoutManager)null);
      Hashtable var1 = new Hashtable();
      JLabel var2 = new JLabel("Slow");
      var2.setFont(Utilities.thinLabelsFont);
      JLabel var3 = new JLabel("Fast");
      var3.setFont(Utilities.thinLabelsFont);
      var1.put(new Integer(1), var2);
      var1.put(new Integer(5), var3);
      this.speedSlider.addChangeListener(new ChangeListener() {
         public void stateChanged(ChangeEvent var1) {
            ControllerComponent.this.SpeedSlider_stateChanged(var1);
         }
      });
      this.speedSlider.setLabelTable(var1);
      this.speedSlider.setMajorTickSpacing(1);
      this.speedSlider.setPaintTicks(true);
      this.speedSlider.setPaintLabels(true);
      this.speedSlider.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
      this.speedSlider.setPreferredSize(new Dimension(95, 50));
      this.speedSlider.setMinimumSize(new Dimension(95, 50));
      this.speedSlider.setToolTipText("Speed");
      this.speedSlider.setMaximumSize(new Dimension(95, 50));
      this.loadProgramButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.loadProgramButton_actionPerformed(var1);
         }
      });
      this.loadProgramButton.setMaximumSize(new Dimension(39, 39));
      this.loadProgramButton.setMinimumSize(new Dimension(39, 39));
      this.loadProgramButton.setPreferredSize(new Dimension(39, 39));
      this.loadProgramButton.setSize(new Dimension(39, 39));
      this.loadProgramButton.setToolTipText("Load Program");
      this.loadProgramButton.setIcon(this.loadProgramIcon);
      this.ffwdButton.setMaximumSize(new Dimension(39, 39));
      this.ffwdButton.setMinimumSize(new Dimension(39, 39));
      this.ffwdButton.setPreferredSize(new Dimension(39, 39));
      this.ffwdButton.setToolTipText("Run");
      this.ffwdButton.setIcon(this.ffwdIcon);
      this.ffwdButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.ffwdButton_actionPerformed(var1);
         }
      });
      this.stopButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.stopButton_actionPerformed(var1);
         }
      });
      this.stopButton.setMaximumSize(new Dimension(39, 39));
      this.stopButton.setMinimumSize(new Dimension(39, 39));
      this.stopButton.setPreferredSize(new Dimension(39, 39));
      this.stopButton.setToolTipText("Stop");
      this.stopButton.setIcon(this.stopIcon);
      this.rewindButton.setMaximumSize(new Dimension(39, 39));
      this.rewindButton.setMinimumSize(new Dimension(39, 39));
      this.rewindButton.setPreferredSize(new Dimension(39, 39));
      this.rewindButton.setToolTipText("Reset");
      this.rewindButton.setIcon(this.rewindIcon);
      this.rewindButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.rewindButton_actionPerformed(var1);
         }
      });
      this.scriptButton.setMaximumSize(new Dimension(39, 39));
      this.scriptButton.setMinimumSize(new Dimension(39, 39));
      this.scriptButton.setPreferredSize(new Dimension(39, 39));
      this.scriptButton.setToolTipText("Load Script");
      this.scriptButton.setIcon(this.scriptIcon);
      this.scriptButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.scriptButton_actionPerformed(var1);
         }
      });
      this.breakButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.breakButton_actionPerformed(var1);
         }
      });
      this.breakButton.setMaximumSize(new Dimension(39, 39));
      this.breakButton.setMinimumSize(new Dimension(39, 39));
      this.breakButton.setPreferredSize(new Dimension(39, 39));
      this.breakButton.setToolTipText("Open breakpoint panel");
      this.breakButton.setIcon(this.breakIcon);
      this.breakpointWindow.addBreakpointListener(this);
      this.singleStepButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.singleStepButton_actionPerformed(var1);
         }
      });
      this.singleStepButton.setMaximumSize(new Dimension(39, 39));
      this.singleStepButton.setMinimumSize(new Dimension(39, 39));
      this.singleStepButton.setPreferredSize(new Dimension(39, 39));
      this.singleStepButton.setSize(new Dimension(39, 39));
      this.singleStepButton.setToolTipText("Single Step");
      this.singleStepButton.setIcon(this.singleStepIcon);
      this.animationCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.animationCombo_actionPerformed(var1);
         }
      });
      this.formatCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.formatCombo_actionPerformed(var1);
         }
      });
      this.additionalDisplayCombo.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            ControllerComponent.this.additionalDisplayCombo_actionPerformed(var1);
         }
      });
      this.messageLbl.setFont(Utilities.statusLineFont);
      this.messageLbl.setBorder(BorderFactory.createLoweredBevelBorder());
      this.messageLbl.setBounds(new Rectangle(0, 667, 1016, 25));
      this.toolBar = new JToolBar();
      this.toolBar.setSize(new Dimension(1016, 55));
      this.toolBar.setLayout(new FlowLayout(0, 3, 0));
      this.toolBar.setFloatable(false);
      this.toolBar.setLocation(0, 0);
      this.toolBar.setBorder(BorderFactory.createEtchedBorder());
      this.arrangeToolBar();
      this.getContentPane().add(this.toolBar, (Object)null);
      this.toolBar.revalidate();
      this.toolBar.repaint();
      this.repaint();
      this.menuBar = new JMenuBar();
      this.arrangeMenu();
      this.setJMenuBar(this.menuBar);
      this.setDefaultCloseOperation(3);
      this.getContentPane().add(this.messageLbl, (Object)null);
      this.setControllerSize();
      this.setVisible(true);
   }

   public void additionalDisplayCombo_actionPerformed(ActionEvent var1) {
      int var2 = this.additionalDisplayCombo.getSelectedIndex();
      switch(var2) {
      case 0:
         if (!this.scriptMenuItem.isSelected()) {
            this.scriptMenuItem.setSelected(true);
         }
         break;
      case 1:
         if (!this.outputMenuItem.isSelected()) {
            this.outputMenuItem.setSelected(true);
         }
         break;
      case 2:
         if (!this.compareMenuItem.isSelected()) {
            this.compareMenuItem.setSelected(true);
         }
         break;
      case 3:
         if (!this.noAdditionalDisplayMenuItem.isSelected()) {
            this.noAdditionalDisplayMenuItem.setSelected(true);
         }
      }

      this.notifyControllerListeners((byte)12, new Integer(var2));
   }

   public void loadProgramButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)27, (Object)null);
   }

   public void ffwdButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)2, (Object)null);
   }

   public void rewindButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)9, (Object)null);
   }

   public void scriptButton_actionPerformed(ActionEvent var1) {
      this.scriptPressed();
   }

   public void breakButton_actionPerformed(ActionEvent var1) {
      this.showBreakpoints();
   }

   public void singleStepButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)1, (Object)null);
   }

   public void stopButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)4, (Object)null);
   }

   public void SpeedSlider_stateChanged(ChangeEvent var1) {
      JSlider var2 = (JSlider)var1.getSource();
      if (!var2.getValueIsAdjusting()) {
         int var3 = var2.getValue();
         this.notifyControllerListeners((byte)3, new Integer(var3));
      }

   }

   public void animationCombo_actionPerformed(ActionEvent var1) {
      int var2 = this.animationCombo.getSelectedIndex();
      switch(var2) {
      case 0:
         if (!this.partAnimMenuItem.isSelected()) {
            this.partAnimMenuItem.setSelected(true);
         }
         break;
      case 1:
         if (!this.fullAnimMenuItem.isSelected()) {
            this.fullAnimMenuItem.setSelected(true);
         }
         break;
      case 2:
         if (!this.noAnimMenuItem.isSelected()) {
            this.noAnimMenuItem.setSelected(true);
         }
      }

      this.notifyControllerListeners((byte)10, new Integer(var2));
   }

   public void formatCombo_actionPerformed(ActionEvent var1) {
      int var2 = this.formatCombo.getSelectedIndex();
      switch(var2) {
      case 0:
         if (!this.decMenuItem.isSelected()) {
            this.decMenuItem.setSelected(true);
         }
         break;
      case 1:
         if (!this.hexaMenuItem.isSelected()) {
            this.hexaMenuItem.setSelected(true);
         }
         break;
      case 2:
         if (!this.binMenuItem.isSelected()) {
            this.binMenuItem.setSelected(true);
         }
      }

      this.notifyControllerListeners((byte)11, new Integer(var2));
   }

   public void programMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)27, (Object)null);
   }

   public void scriptMenuItem_actionPerformed(ActionEvent var1) {
      this.scriptPressed();
   }

   public void exitMenuItem_actionPerformed(ActionEvent var1) {
      System.exit(0);
   }

   public void partAnimMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.animationCombo.isSelectedIndex(0)) {
         this.animationCombo.setSelectedIndex(0);
      }

   }

   public void fullAnimMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.animationCombo.isSelectedIndex(1)) {
         this.animationCombo.setSelectedIndex(1);
      }

   }

   public void noAnimMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.animationCombo.isSelectedIndex(2)) {
         this.animationCombo.setSelectedIndex(2);
      }

   }

   public void decMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.formatCombo.isSelectedIndex(0)) {
         this.formatCombo.setSelectedIndex(0);
      }

   }

   public void hexaMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.formatCombo.isSelectedIndex(1)) {
         this.formatCombo.setSelectedIndex(1);
      }

   }

   public void binMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.formatCombo.isSelectedIndex(2)) {
         this.formatCombo.setSelectedIndex(2);
      }

   }

   public void scriptDisplayMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.additionalDisplayCombo.isSelectedIndex(0)) {
         this.additionalDisplayCombo.setSelectedIndex(0);
      }

   }

   public void outputMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.additionalDisplayCombo.isSelectedIndex(1)) {
         this.additionalDisplayCombo.setSelectedIndex(1);
      }

   }

   public void compareMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.additionalDisplayCombo.isSelectedIndex(2)) {
         this.additionalDisplayCombo.setSelectedIndex(2);
      }

   }

   public void noAdditionalDisplayMenuItem_actionPerformed(ActionEvent var1) {
      if (!this.additionalDisplayCombo.isSelectedIndex(3)) {
         this.additionalDisplayCombo.setSelectedIndex(3);
      }

   }

   public void singleStepMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)1, (Object)null);
   }

   public void ffwdMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)2, (Object)null);
   }

   public void stopMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)4, (Object)null);
   }

   public void rewindMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)9, (Object)null);
   }

   public void breakpointsMenuItem_actionPerformed(ActionEvent var1) {
      this.showBreakpoints();
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
