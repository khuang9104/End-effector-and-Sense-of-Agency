package trial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import utilities.NumericTextField;

public class TrialConfigure extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private TrialPanel TrialPanel;
	private TrialController controller;

	private JTextField intervalTimeField, blockSizeField;
	private JButton updateBeepIntervalBtn, updateBlockSizeBtn;
	private JRadioButton manualModeBtn, automaticModeBtn;
	private JRadioButton sensorNoBeep_flexRecorded, sensorPlusBeep_flexRecorded, sensorPlusBeep_BeepRecorded;
	public JRadioButton buttonNoBeep_RecordButton;
	private JRadioButton buttonPlusBeep_ButtonRecorded;
	private JRadioButton buttonPlusBeep_BeepRecorded;
	private JRadioButton justPlayTheBeepButton, justPlayTheBeepFlex;

	private int widthSet = 300;
	private int vertBorder = 20;
	private int horzBorder = 10;

	private Color lighterGray = Color.LIGHT_GRAY;
	private BlockConfigurationPanel bConfig;

	public TrialConfigure(TrialPanel lPanel, TrialController lc) {

		setBackground(lighterGray);
		this.TrialPanel = lPanel;
		this.controller = lc;

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(new EmptyBorder(10, horzBorder, vertBorder, 0));
		titlePanel.setBackground(lighterGray);

		JLabel titleLabel = new JLabel("Trial Configure");
		titlePanel.add(titleLabel, BorderLayout.NORTH);

		JLabel intervalTimeLabel = new JLabel("Beep interval (ms):  ");

		DecimalFormat format = new DecimalFormat("###");
		intervalTimeField = new NumericTextField(4, format);
		intervalTimeField.setMaximumSize(intervalTimeField.getPreferredSize());
		intervalTimeField.setHorizontalAlignment(JTextField.RIGHT);
		intervalTimeField.setText(Integer.toString(TrialPanel.getBeepInterval()));

		updateBeepIntervalBtn = new JButton("Update");
		updateBeepIntervalBtn.setBackground(lighterGray);
		updateBeepIntervalBtn.addActionListener(this);

		JPanel intervalConfig = new JPanel();
		intervalConfig.setBorder(new EmptyBorder(0, horzBorder, vertBorder, 0));
		intervalConfig.setBackground(lighterGray);
		intervalConfig.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		intervalConfig.add(intervalTimeLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 700;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		intervalConfig.add(Box.createHorizontalStrut(1), c);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		intervalConfig.add(intervalTimeField, c);

		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 300;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		intervalConfig.add(Box.createHorizontalStrut(1), c);

		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.EAST;
		intervalConfig.add(updateBeepIntervalBtn, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		c.gridwidth = 5;
		intervalConfig.add(Box.createHorizontalStrut(widthSet), c);

		JLabel blockSizeLabel = new JLabel("Trials per block:  ");
		blockSizeField = new NumericTextField(4, format);
		blockSizeField.setMaximumSize(blockSizeField.getPreferredSize());
		blockSizeField.setHorizontalAlignment(JTextField.RIGHT);
		blockSizeField.setText(Integer.toString(controller.getBlockSize()));

		updateBlockSizeBtn = new JButton("Update");
		updateBlockSizeBtn.setBackground(lighterGray);
		updateBlockSizeBtn.addActionListener(this);

		JPanel blockSizeConfig = new JPanel();
		blockSizeConfig.setBorder(new EmptyBorder(0, horzBorder, vertBorder, 0));
		blockSizeConfig.setBackground(lighterGray);
		blockSizeConfig.setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		blockSizeConfig.add(blockSizeLabel, c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 700;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		blockSizeConfig.add(Box.createHorizontalStrut(1), c);

		c.gridx = 2;
		c.gridy = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		blockSizeConfig.add(blockSizeField, c);

		c.gridx = 3;
		c.gridy = 0;
		c.weightx = 300;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		blockSizeConfig.add(Box.createHorizontalStrut(1), c);

		c.gridx = 4;
		c.gridy = 0;
		c.weightx = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.WEST;
		blockSizeConfig.add(updateBlockSizeBtn, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		c.gridwidth = 5;
		blockSizeConfig.add(Box.createHorizontalStrut(widthSet), c);

		buttonNoBeep_RecordButton = new JRadioButton();
		JPanel buttonNoBeepPanel = configureNewRadioPanel(buttonNoBeep_RecordButton, "1 - " + TrialSetting.TYPE1,
				TrialSetting.BUTTON_No_BEEP_Rec_BUTTON);

		buttonPlusBeep_ButtonRecorded = new JRadioButton();
		JPanel buttonPlusBeep_ButtonRecordedPanel = configureNewRadioPanel(buttonPlusBeep_ButtonRecorded,
				"2 - " + TrialSetting.TYPE2, TrialSetting.BUTTON_Plus_BEEP_Rec_BUTTON);

		buttonPlusBeep_BeepRecorded = new JRadioButton();
		JPanel buttonPlusBeep_BeepRecordedPanel = configureNewRadioPanel(buttonPlusBeep_BeepRecorded,
				"3 - " + TrialSetting.TYPE3, TrialSetting.BUTTON_Plus_BEEP_Rec_BEEP);

		justPlayTheBeepButton = new JRadioButton();
		JPanel justPlayTheBeepButtonPanel = configureNewRadioPanel(justPlayTheBeepButton, "4 - " + TrialSetting.TYPE4,
				TrialSetting.BUTTON_No_ACTION_Rec_BEEP);

		sensorNoBeep_flexRecorded = new JRadioButton();
		JPanel flexNoBeepPanel = configureNewRadioPanel(sensorNoBeep_flexRecorded, "5 - " + TrialSetting.TYPE5,
				TrialSetting.SENSOR_No_BEEP_Rec_FLEX);

		sensorPlusBeep_flexRecorded = new JRadioButton();
		JPanel flexPlusBeep_TapRecordedPanel = configureNewRadioPanel(sensorPlusBeep_flexRecorded,
				"6 - " + TrialSetting.TYPE6, TrialSetting.SENSOR_Plus_BEEP_Rec_FLEX);

		sensorPlusBeep_BeepRecorded = new JRadioButton();
		JPanel flexPlusBeep_BeepRecordedPanel = configureNewRadioPanel(sensorPlusBeep_BeepRecorded,
				"7 - " + TrialSetting.TYPE7, TrialSetting.SENSOR_Plus_BEEP_Rec_BEEP);

		justPlayTheBeepFlex = new JRadioButton();
		JPanel justPlayTheBeepFlexPanel = configureNewRadioPanel(justPlayTheBeepFlex, "8 - " + TrialSetting.TYPE8,
				TrialSetting.SENSOR_No_ACTION_Rec_BEEP);

		ButtonGroup optionsGroup = new ButtonGroup();
		optionsGroup.add(buttonNoBeep_RecordButton);
		optionsGroup.add(buttonPlusBeep_ButtonRecorded);
		optionsGroup.add(buttonPlusBeep_BeepRecorded);
		optionsGroup.add(justPlayTheBeepButton);
		optionsGroup.add(sensorNoBeep_flexRecorded);
		optionsGroup.add(sensorPlusBeep_flexRecorded);
		optionsGroup.add(sensorPlusBeep_BeepRecorded);
		optionsGroup.add(justPlayTheBeepFlex);

		JPanel optionsPanel = new JPanel();
		optionsPanel.setBorder(new EmptyBorder(0, horzBorder, vertBorder, 0));
		optionsPanel.setBackground(lighterGray);
		optionsPanel.setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.WEST;
		c.gridwidth = 1;
		c.weightx = 0;
		c.weighty = 0;
		c.gridx = 0;
		c.gridy = 0;
		optionsPanel.add(buttonNoBeepPanel, c);

		c.gridy++;
		optionsPanel.add(buttonPlusBeep_ButtonRecordedPanel, c);

		c.gridy++;
		optionsPanel.add(buttonPlusBeep_BeepRecordedPanel, c);

		c.gridy++;
		optionsPanel.add(justPlayTheBeepButtonPanel, c);

		c.gridy++;
		optionsPanel.add(Box.createVerticalStrut(5), c);

		c.gridy++;
		optionsPanel.add(flexNoBeepPanel, c);

		c.gridy++;
		optionsPanel.add(flexPlusBeep_TapRecordedPanel, c);

		c.gridy++;
		optionsPanel.add(flexPlusBeep_BeepRecordedPanel, c);

		c.gridy++;
		optionsPanel.add(justPlayTheBeepFlexPanel, c);

		c.gridy++;
		c.weighty = 1000;
		optionsPanel.add(Box.createHorizontalStrut(widthSet), c);

		JLabel modeLabel = new JLabel("Trial mode:");
		manualModeBtn = new JRadioButton("Manual");
		manualModeBtn.setBackground(lighterGray);
		manualModeBtn.setHorizontalTextPosition(SwingConstants.LEADING);
		manualModeBtn.addActionListener(this);
		automaticModeBtn = new JRadioButton("Automatic");
		automaticModeBtn.setBackground(lighterGray);
		automaticModeBtn.setHorizontalTextPosition(SwingConstants.LEADING);
		automaticModeBtn.addActionListener(this);

		ButtonGroup modeGroup = new ButtonGroup();
		modeGroup.add(manualModeBtn);
		modeGroup.add(automaticModeBtn);

		JPanel modeButtonPanel = new JPanel();
		modeButtonPanel.setBackground(lighterGray);
		modeButtonPanel.setLayout(new BoxLayout(modeButtonPanel, BoxLayout.X_AXIS));
		modeButtonPanel.add(modeLabel);
		modeButtonPanel.add(Box.createHorizontalGlue());
		modeButtonPanel.add(manualModeBtn);
		modeButtonPanel.add(Box.createHorizontalStrut(10));
		modeButtonPanel.add(automaticModeBtn);

		JPanel modePanel = new JPanel(new BorderLayout());
		modePanel.setBorder(new EmptyBorder(0, horzBorder, vertBorder, 0));
		modePanel.setBackground(lighterGray);
		modePanel.add(modeButtonPanel, BorderLayout.CENTER);
		modePanel.add(Box.createHorizontalStrut(widthSet), BorderLayout.SOUTH);

		bConfig = new BlockConfigurationPanel(controller);
		bConfig.setBorder(new EmptyBorder(0, horzBorder - 5, vertBorder, 0));

		JPanel overallTrialPanel = new JPanel();
		overallTrialPanel.setBackground(lighterGray);
		overallTrialPanel.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;

		overallTrialPanel.add(titlePanel, c);
		c.gridy++;
		overallTrialPanel.add(Box.createVerticalStrut(20), c);
		c.gridy++;
		overallTrialPanel.add(modePanel, c);
		c.gridy++;
		overallTrialPanel.add(Box.createVerticalStrut(20), c);
		c.gridy++;
		overallTrialPanel.add(blockSizeConfig, c);
		c.gridy++;
		overallTrialPanel.add(bConfig, c);
		c.gridy++;
		overallTrialPanel.add(Box.createVerticalStrut(10), c);
		c.gridy++;
		overallTrialPanel.add(intervalConfig, c);
		c.gridy++;
		overallTrialPanel.add(Box.createVerticalStrut(10), c);
		c.gridy++;
		overallTrialPanel.add(optionsPanel, c);
		c.gridy++;
		c.weighty = 1000;
		overallTrialPanel.add(Box.createVerticalStrut(1), c);

		setLayout(new BorderLayout());
		add(overallTrialPanel, BorderLayout.CENTER);
		setBorder(new EmptyBorder(0, 10, 0, 5));
		configureManualModeSelections();
		configureBlockOptionsSelections();
	}

	private JPanel configureNewRadioPanel(JRadioButton b, String title, int actionCommand) {

		JPanel jp = new JPanel(new BorderLayout());
		jp.setBackground(lighterGray);
		jp.setBorder(new EmptyBorder(10, 0, 0, 0));

		JLabel jL = new JLabel(title);

		b.setHorizontalTextPosition(SwingConstants.LEADING);
		b.addActionListener(this);
		b.setActionCommand(actionCommand + "");
		b.setBackground(lighterGray);

		jp.add(jL, BorderLayout.WEST);
		jp.add(b, BorderLayout.EAST);
		jp.add(Box.createHorizontalStrut(widthSet), BorderLayout.SOUTH);

		return jp;
	}

	private void configureManualModeSelections() {
		manualModeBtn.setSelected(controller.getManualMode());
		automaticModeBtn.setSelected(!controller.getManualMode());
	}

	public void initiateConfiguration() {
		configAutomaticModeInputs(!controller.getManualMode());
		configManualModeInputs(controller.getManualMode());
	}

	public void configureBlockOptionsSelections() {
		buttonNoBeep_RecordButton.setSelected(!TrialPanel.getUseSensorInput() && !TrialPanel.getPlayBeep()
				&& !TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput() && controller.getTrialType() == 0);

		buttonPlusBeep_ButtonRecorded.setSelected(!TrialPanel.getUseSensorInput() && TrialPanel.getPlayBeep()
				&& !TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput() && controller.getTrialType() == 1);

		buttonPlusBeep_BeepRecorded.setSelected(!TrialPanel.getUseSensorInput() && TrialPanel.getPlayBeep()
				&& TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput() && controller.getTrialType() == 2);

		justPlayTheBeepButton.setSelected(
				!TrialPanel.getUseSensorInput() && TrialPanel.getUseNoInput() && controller.getTrialType() == 3);

		sensorNoBeep_flexRecorded.setSelected(TrialPanel.getUseSensorInput() && !TrialPanel.getPlayBeep()
				&& !TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput());

		sensorPlusBeep_flexRecorded.setSelected(TrialPanel.getUseSensorInput() && TrialPanel.getPlayBeep()
				&& !TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput());

		sensorPlusBeep_BeepRecorded.setSelected(TrialPanel.getUseSensorInput() && TrialPanel.getPlayBeep()
				&& TrialPanel.getRecordBeepTime() && !TrialPanel.getUseNoInput());

		justPlayTheBeepFlex.setSelected(TrialPanel.getUseSensorInput() && TrialPanel.getUseNoInput());

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(updateBeepIntervalBtn)) {
			int origValue = TrialPanel.getBeepInterval();
			int anInt = -1;
			try {
				anInt = Integer.parseInt(intervalTimeField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a number greater than " + TrialSetting.minInterval,
						"Try again", JOptionPane.ERROR_MESSAGE);
				intervalTimeField.setText("" + origValue);
				intervalTimeField.requestFocusInWindow();
				return;
			}
			if (anInt >= TrialSetting.minInterval) {
				TrialPanel.setBeepInterval(anInt);
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a number greater than " + TrialSetting.minInterval,
						"Try again", JOptionPane.ERROR_MESSAGE);
				intervalTimeField.setText("" + origValue);
				intervalTimeField.requestFocusInWindow();
			}
		}

		if (source.equals(updateBlockSizeBtn)) {
			int origValue = controller.getBlockSize();
			int anInt = -1;
			try {
				anInt = Integer.parseInt(blockSizeField.getText());
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "Please enter a number greater than 0", "Try again",
						JOptionPane.ERROR_MESSAGE);
				blockSizeField.setText("" + origValue);
				blockSizeField.requestFocusInWindow();
				return;
			}
			if (anInt > 0) {
				controller.setBlockSize(anInt);
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a number greater than 0", "Try again",
						JOptionPane.ERROR_MESSAGE);
				blockSizeField.setText("" + origValue);
				blockSizeField.requestFocusInWindow();
			}
		} else if (source.equals(manualModeBtn)) {
			controller.setManualMode(true);
			configAutomaticModeInputs(false);
			configManualModeInputs(true);
		} else if (source.equals(automaticModeBtn)) {
			controller.setManualMode(false);
			configAutomaticModeInputs(true);
			configManualModeInputs(false);
			
		} else if (source.equals(buttonNoBeep_RecordButton)) {
			controller.setTrialType(TrialSetting.BUTTON_No_BEEP_Rec_BUTTON);
			TrialPanel.setUseSensorInput(false);
			TrialPanel.setPlayBeep(false);
			TrialPanel.setRecordBeepTime(false);
			TrialPanel.setUseNoInput(false);
		} else if (source.equals(buttonPlusBeep_ButtonRecorded)) {
			controller.setTrialType(TrialSetting.BUTTON_Plus_BEEP_Rec_BUTTON);
			TrialPanel.setUseSensorInput(false);
			TrialPanel.setPlayBeep(true);
			TrialPanel.setRecordBeepTime(false);
			TrialPanel.setUseNoInput(false);
		} else if (source.equals(buttonPlusBeep_BeepRecorded)) {
			controller.setTrialType(TrialSetting.BUTTON_Plus_BEEP_Rec_BEEP);
			TrialPanel.setUseSensorInput(false);
			TrialPanel.setPlayBeep(true);
			TrialPanel.setRecordBeepTime(true);
			TrialPanel.setUseNoInput(false);
		} else if (source.equals(justPlayTheBeepButton)) {
			controller.setTrialType(TrialSetting.BUTTON_No_ACTION_Rec_BEEP);
			TrialPanel.setUseSensorInput(false);
			TrialPanel.setPlayBeep(true);
			TrialPanel.setRecordBeepTime(true);
			TrialPanel.setUseNoInput(true);
		}

		 else if(source.equals(sensorNoBeep_flexRecorded)){
		 controller.setTrialType(TrialSetting.SENSOR_No_BEEP_Rec_FLEX);
		 TrialPanel.setUseSensorInput(true); TrialPanel.setPlayBeep(false);
		 TrialPanel.setRecordBeepTime(false); TrialPanel.setUseNoInput(false); }
		 
		 else if(source.equals(sensorPlusBeep_flexRecorded)){
		 controller.setTrialType(TrialSetting.SENSOR_Plus_BEEP_Rec_FLEX);
		 TrialPanel.setUseSensorInput(true); TrialPanel.setPlayBeep(true);
		 TrialPanel.setRecordBeepTime(false); TrialPanel.setUseNoInput(false); }
		 
		 else if(source.equals(sensorPlusBeep_BeepRecorded)){
		 controller.setTrialType(TrialSetting.SENSOR_Plus_BEEP_Rec_BEEP);
		 TrialPanel.setUseSensorInput(true); TrialPanel.setPlayBeep(true);
		 TrialPanel.setRecordBeepTime(true); TrialPanel.setUseNoInput(false); }
		 
		 else if(source.equals(justPlayTheBeepFlex)){
		 controller.setTrialType(TrialSetting.SENSOR_No_ACTION_Rec_BEEP);
		 TrialPanel.setUseSensorInput(true); TrialPanel.setPlayBeep(true);
		 TrialPanel.setRecordBeepTime(true); TrialPanel.setUseNoInput(true); }
	}

	public void configEnabled(boolean b) {
		manualModeBtn.setEnabled(b);
		automaticModeBtn.setEnabled(b);
		updateBeepIntervalBtn.setEnabled(b);
		intervalTimeField.setEnabled(b);
		if (controller.getManualMode())
			configManualModeInputs(b);
		else
			configAutomaticModeInputs(b);
	}

	public void configManualModeInputs(boolean b) {

		buttonNoBeep_RecordButton.setEnabled(b);
		buttonPlusBeep_ButtonRecorded.setEnabled(b);
		buttonPlusBeep_BeepRecorded.setEnabled(b);
		justPlayTheBeepButton.setEnabled(b);
		
		sensorNoBeep_flexRecorded.setEnabled(b); 
		sensorPlusBeep_flexRecorded.setEnabled(b);
		sensorPlusBeep_BeepRecorded.setEnabled(b);
		justPlayTheBeepFlex.setEnabled(b);
		
	}

	public void configAutomaticModeInputs(boolean b) {
		updateBlockSizeBtn.setEnabled(b);
		blockSizeField.setEnabled(b);
		bConfig.enableInputs(b);
	}

	public void switchToAuto() {
		automaticModeBtn.doClick();
	}
	
	public void switchToDefaultOptions() {
		buttonNoBeep_RecordButton.doClick();
	}
}
