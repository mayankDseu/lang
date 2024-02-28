package SimulatorsGUI;

import Hack.HardwareSimulator.HardwareSimulatorControllerGUI;
import HackGUI.ControllerComponent;
import HackGUI.MouseOverJButton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

public class HardwareSimulatorControllerComponent extends ControllerComponent implements HardwareSimulatorControllerGUI {
   private MouseOverJButton loadChipButton;
   private MouseOverJButton tickTockButton;
   private MouseOverJButton evalButton;
   private ImageIcon loadChipIcon;
   private ImageIcon tickTockIcon;
   private ImageIcon evalIcon;
   private ChipLoaderFileChooser settingsWindow;
   private JMenuItem loadChipMenuItem;
   private JMenuItem evalMenuItem;
   private JMenuItem tickTockMenuItem;
   private JFileChooser chipFileChooser;

   public HardwareSimulatorControllerComponent() {
      this.scriptComponent.updateSize(516, 592);
      this.outputComponent.updateSize(516, 592);
      this.comparisonComponent.updateSize(516, 592);
   }

   public void disableEval() {
      this.evalButton.setEnabled(false);
      this.evalMenuItem.setEnabled(false);
   }

   public void enableEval() {
      this.evalButton.setEnabled(true);
      this.evalMenuItem.setEnabled(true);
   }

   public void disableTickTock() {
      this.tickTockButton.setEnabled(false);
      this.tickTockMenuItem.setEnabled(false);
   }

   public void enableTickTock() {
      this.tickTockButton.setEnabled(true);
      this.tickTockMenuItem.setEnabled(true);
   }

   protected void init() {
      super.init();
      this.settingsWindow = new ChipLoaderFileChooser();
      this.settingsWindow.addListener(this);
      this.chipFileChooser = new JFileChooser();
      this.chipFileChooser.setFileFilter(new HDLFileFilter());
      this.initLoadChipButton();
      this.initTickTockButton();
      this.initEvalButton();
   }

   public void setWorkingDir(File var1) {
      super.setWorkingDir(var1);
      this.chipFileChooser.setCurrentDirectory(var1);
   }

   protected void arrangeToolBar() {
      this.toolBar.setSize(1016, 55);
      this.toolBar.add(this.loadChipButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.singleStepButton);
      this.toolBar.add(this.ffwdButton);
      this.toolBar.add(this.stopButton);
      this.toolBar.add(this.rewindButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.evalButton);
      this.toolBar.add(this.tickTockButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.scriptButton);
      this.toolBar.add(this.breakButton);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.speedSlider);
      this.toolBar.addSeparator(separatorDimension);
      this.toolBar.add(this.animationCombo);
      this.toolBar.add(this.formatCombo);
      this.toolBar.add(this.additionalDisplayCombo);
   }

   protected void arrangeMenu() {
      super.arrangeMenu();
      this.fileMenu.removeAll();
      this.loadChipMenuItem = new JMenuItem("Load Chip", 76);
      this.loadChipMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.loadChipMenuItem_actionPerformed(var1);
         }
      });
      this.fileMenu.add(this.loadChipMenuItem);
      this.fileMenu.add(this.scriptMenuItem);
      this.fileMenu.addSeparator();
      this.fileMenu.add(this.exitMenuItem);
      this.runMenu.removeAll();
      this.runMenu.add(this.singleStepMenuItem);
      this.runMenu.add(this.ffwdMenuItem);
      this.runMenu.add(this.stopMenuItem);
      this.runMenu.add(this.rewindMenuItem);
      this.runMenu.addSeparator();
      this.evalMenuItem = new JMenuItem("Eval", 69);
      this.evalMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.evalMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.evalMenuItem);
      this.tickTockMenuItem = new JMenuItem("Tick Tock", 67);
      this.tickTockMenuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.tickTockMenuItem_actionPerformed(var1);
         }
      });
      this.runMenu.add(this.tickTockMenuItem);
      this.runMenu.addSeparator();
      this.runMenu.add(this.breakpointsMenuItem);
   }

   private void initLoadChipButton() {
      this.loadChipIcon = new ImageIcon("bin/images/chip.gif");
      this.loadChipButton = new MouseOverJButton();
      this.loadChipButton.setMaximumSize(new Dimension(39, 39));
      this.loadChipButton.setMinimumSize(new Dimension(39, 39));
      this.loadChipButton.setPreferredSize(new Dimension(39, 39));
      this.loadChipButton.setToolTipText("Load Chip");
      this.loadChipButton.setIcon(this.loadChipIcon);
      this.loadChipButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.loadChipButton_actionPerformed(var1);
         }
      });
   }

   private void initTickTockButton() {
      this.tickTockIcon = new ImageIcon("bin/images/clock2.gif");
      this.tickTockButton = new MouseOverJButton();
      this.tickTockButton.setMaximumSize(new Dimension(39, 39));
      this.tickTockButton.setMinimumSize(new Dimension(39, 39));
      this.tickTockButton.setPreferredSize(new Dimension(39, 39));
      this.tickTockButton.setToolTipText("Tick Tock");
      this.tickTockButton.setIcon(this.tickTockIcon);
      this.tickTockButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.tickTockButton_actionPerformed(var1);
         }
      });
   }

   private void initEvalButton() {
      this.evalIcon = new ImageIcon("bin/images/calculator2.gif");
      this.evalButton = new MouseOverJButton();
      this.evalButton.setMaximumSize(new Dimension(39, 39));
      this.evalButton.setMinimumSize(new Dimension(39, 39));
      this.evalButton.setPreferredSize(new Dimension(39, 39));
      this.evalButton.setToolTipText("Eval");
      this.evalButton.setIcon(this.evalIcon);
      this.evalButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent var1) {
            HardwareSimulatorControllerComponent.this.evalButton_actionPerformed(var1);
         }
      });
   }

   private void loadChipPressed() {
      int var1 = this.chipFileChooser.showDialog(this, "Load Chip");
      if (var1 == 0) {
         this.notifyControllerListeners((byte)102, this.chipFileChooser.getSelectedFile().getAbsoluteFile());
      }

   }

   public void loadChipButton_actionPerformed(ActionEvent var1) {
      this.loadChipPressed();
   }

   public void tickTockButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)100, (Object)null);
   }

   public void evalButton_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)101, (Object)null);
   }

   public void loadChipMenuItem_actionPerformed(ActionEvent var1) {
      this.loadChipPressed();
   }

   public void evalMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)101, (Object)null);
   }

   public void tickTockMenuItem_actionPerformed(ActionEvent var1) {
      this.notifyControllerListeners((byte)100, (Object)null);
   }
}
