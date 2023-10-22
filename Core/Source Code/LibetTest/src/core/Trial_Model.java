package core;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import trial.*;

public class Trial_Model extends JFrame implements TrialListener, ActionListener {

	private static final long serialVersionUID = 1L;

	private boolean libetTrialRunning = false;
	private boolean blockAllLibetInputs = false;
	private static boolean DEFAULTMANUALMODE = true;

	private TrialPanel trialPanel;
	private TrialController controller = null;
	private LibetClockConfigure libetClockConfig;
	private TrialConfigure trialConfig;
	private MyoConnectionPanel myoConnection;
	private JTextArea libetReadingsArea;
	private MyoDataCapture myoDataCapture;
	private MyoSocket myoSocket;
	private Thread thread;

	private static String actionKey = "button";
	private static String startTrialMsg = "Press the footswitch to start a trial.";
	private static String infoStringButtonInput = "Press the " + actionKey + " whenever you want.";
	private static String infoStringEMGInput = "Flex your wrist whenever you want.";
	private static String infoStringNoInput = "No action is required.";

	private boolean libetInputsEnabled = true;
	private JFrame experimentWindow;
	private JButton moveOnBtn, startFullTrialBtn, checkTrialSettingBtn;
	private JTextPane instructions1, instructions2, instructions3;
	private JPanel p1, p2, pLibet, instructions2Panel;
	private JPanel pWest, pEast;

	SimpleAttributeSet attribs;
	Timer timer;

	private int perceivedTime;

	private DecimalFormat df = new DecimalFormat("#.##");
	private SimpleDateFormat df_localTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	// constructor instantiates model, view, and controller
	public Trial_Model(JFrame experimentWindow) {
		
		trialPanel = new TrialPanel();
		trialPanel.addTrialListener(this);

		controller = new TrialController(trialPanel, null, -1, DEFAULTMANUALMODE);

		this.experimentWindow = experimentWindow;
		if (experimentWindow != null) {
			controller.createControllerDisplay(experimentWindow);
		} else {
			controller.createControllerDisplay(null);
		}

		libetClockConfig = new LibetClockConfigure(trialPanel);
		libetClockConfig.setBorder(new CompoundBorder(new LineBorder(Color.DARK_GRAY), new EmptyBorder(10, 10, 5, 10)));
		libetClockConfig.setBorder(new EmptyBorder(10, 10, 5, 10));

		trialConfig = new TrialConfigure(trialPanel, controller);
		trialConfig.setBorder(new EmptyBorder(10, 10, 5, 10));

		myoConnection = new MyoConnectionPanel();

		libetReadingsArea = new JTextArea();
		libetReadingsArea.setFont(new Font("Dialog", Font.PLAIN, 11));
		libetReadingsArea.setEditable(false);
		libetReadingsArea.setFocusable(false);
		libetReadingsArea.setText("Trial data will appear here");
		libetReadingsArea.setBackground(new Color(230, 230, 230));

		JPanel libetReadingsPanel = new JPanel();
		libetReadingsPanel.setBackground(new Color(230, 230, 230));
		libetReadingsPanel.setLayout(new BoxLayout(libetReadingsPanel, BoxLayout.X_AXIS));
		libetReadingsPanel.add(libetReadingsArea);
		libetReadingsPanel.add(Box.createVerticalStrut(50));
		libetReadingsPanel.setBorder(new EmptyBorder(5, 10, 0, 5));

		attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontSize(attribs, 16);
		StyleConstants.setSpaceBelow(attribs, 5);

		instructions1 = new JTextPane();
		instructions1.setFocusable(false);
		instructions1.setEditable(false);
		instructions1.setParagraphAttributes(attribs, true);

		JPanel instructionsPanel = new JPanel();
		instructionsPanel.setLayout(new BoxLayout(instructionsPanel, BoxLayout.X_AXIS));
		instructionsPanel.add(Box.createHorizontalGlue());
		instructionsPanel.add(Box.createVerticalStrut(90));
		instructionsPanel.add(instructions1);
		instructionsPanel.add(Box.createVerticalStrut(90));
		instructionsPanel.add(Box.createHorizontalGlue());
		updateInstructions1();

		JPanel libetClockContainer = new JPanel();
		libetClockContainer.setBackground(Color.WHITE);
		libetClockContainer.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Adjust this for specific resolution
		int horizontal_struts_size = 400; // Original value is 310 but not work for 1920*1080.

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 400;
		c.gridwidth = 3;
		libetClockContainer.add(Box.createHorizontalStrut(horizontal_struts_size), c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 500;
		c.weighty = 0;
		c.gridwidth = 1;
		libetClockContainer.add(Box.createVerticalStrut(horizontal_struts_size), c);

		c.gridx++;
		c.gridy = 1;
		c.weightx = 0;
		c.weighty = 0;
		libetClockContainer.add(trialPanel, c);

		c.gridx++;
		c.gridy = 1;
		c.weightx = 500;
		c.weighty = 0;
		libetClockContainer.add(Box.createVerticalStrut(horizontal_struts_size), c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		c.weighty = 200;
		c.gridwidth = 3;
		libetClockContainer.add(Box.createHorizontalStrut(horizontal_struts_size), c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.weighty = 400;
		c.gridwidth = 3;
		c.anchor = GridBagConstraints.NORTH;
		libetClockContainer.add(instructionsPanel, c);

		instructions2 = new JTextPane();
		instructions2.setBorder(new EmptyBorder(60, 0, 30, 0));
		instructions2.setFocusable(false);
		instructions2.setEditable(false);
		StyleConstants.setFontSize(attribs, 20);
		StyleConstants.setSpaceBelow(attribs, 10);
		StyleConstants.setForeground(attribs, Color.BLACK);
		instructions2.setParagraphAttributes(attribs, true);

		JPanel i2Panel = new JPanel();
		i2Panel.setLayout(new BoxLayout(i2Panel, BoxLayout.X_AXIS));
		i2Panel.add(Box.createHorizontalGlue());
		i2Panel.add(instructions2);
		i2Panel.add(Box.createHorizontalGlue());
		i2Panel.add(Box.createVerticalStrut(100));

		instructions3 = new JTextPane();
		instructions3.setBorder(new EmptyBorder(15, 0, 50, 0));
		instructions3.setFocusable(false);
		instructions3.setEditable(false);
		StyleConstants.setFontSize(attribs, 20);
		StyleConstants.setSpaceBelow(attribs, 10);
		StyleConstants.setForeground(attribs, Color.RED);
		instructions3.setParagraphAttributes(attribs, true);

		JPanel i3Panel = new JPanel();
		i3Panel.setLayout(new BoxLayout(i3Panel, BoxLayout.X_AXIS));
		i3Panel.add(Box.createHorizontalGlue());
		i3Panel.add(instructions3);
		i3Panel.add(Box.createHorizontalGlue());
		i3Panel.add(Box.createVerticalStrut(100));

		moveOnBtn = new JButton("Ok");
		moveOnBtn.addActionListener(this);

		JPanel i2ButtonPanel = new JPanel();
		i2ButtonPanel.setBackground(Color.WHITE);
		i2ButtonPanel.setLayout(new BoxLayout(i2ButtonPanel, BoxLayout.X_AXIS));
		i2ButtonPanel.add(Box.createHorizontalGlue());
		i2ButtonPanel.add(moveOnBtn);
		i2ButtonPanel.add(Box.createHorizontalGlue());

		instructions2Panel = new JPanel(new BorderLayout());
		instructions2Panel.add(i2Panel, BorderLayout.NORTH);
		instructions2Panel.add(i3Panel, BorderLayout.CENTER);
		instructions2Panel.add(i2ButtonPanel, BorderLayout.SOUTH);
		instructions2Panel.setVisible(false);
		updateInstructions2();

		checkTrialSettingBtn = new JButton();
		checkTrialSettingBtn.setText("Check trial setting");
		checkTrialSettingBtn.addActionListener(this);
		checkTrialSettingBtn.setBackground(Color.LIGHT_GRAY);
		checkTrialSettingBtn.setFocusable(false);

		JPanel checkTrialSettingPanel = new JPanel();
		checkTrialSettingPanel.setLayout(new BoxLayout(checkTrialSettingPanel, BoxLayout.LINE_AXIS));
		checkTrialSettingPanel.setBorder(new EmptyBorder(15, 10, 20, 10));
		checkTrialSettingPanel.setBackground(Color.LIGHT_GRAY);
		checkTrialSettingPanel.add(Box.createHorizontalGlue());
		checkTrialSettingPanel.add(checkTrialSettingBtn);
		checkTrialSettingPanel.add(Box.createHorizontalGlue());

		startFullTrialBtn = new JButton();
		startFullTrialBtn.setText("Start full trial");
		startFullTrialBtn.addActionListener(this);
		startFullTrialBtn.setBackground(Color.LIGHT_GRAY);
		startFullTrialBtn.setFocusable(false);

		JPanel fullTrialPanel = new JPanel();
		fullTrialPanel.setLayout(new BoxLayout(fullTrialPanel, BoxLayout.LINE_AXIS));
		fullTrialPanel.setBorder(new EmptyBorder(15, 10, 20, 10));
		fullTrialPanel.setBackground(Color.LIGHT_GRAY);
		fullTrialPanel.add(Box.createHorizontalGlue());
		fullTrialPanel.add(startFullTrialBtn);
		fullTrialPanel.add(Box.createHorizontalGlue());

		p1 = new JPanel(new BorderLayout());
		p1.add(libetClockContainer, BorderLayout.CENTER);

		pLibet = new JPanel(new BorderLayout());
		pLibet.setBackground(Color.WHITE);
		pLibet.setBorder(new LineBorder(Color.BLACK, 3));
		pLibet.add(p1, BorderLayout.CENTER);
		pLibet.add(instructions2Panel, BorderLayout.NORTH);

		p2 = new JPanel(new BorderLayout());
		p2.add(libetClockConfig, BorderLayout.NORTH);
		p2.add(libetReadingsPanel, BorderLayout.SOUTH);
		p2.setBorder(new LineBorder(Color.GRAY));

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add(p2, BorderLayout.SOUTH);
		p3.add(pLibet, BorderLayout.CENTER);

		pWest = new JPanel(new BorderLayout());
		pWest.setBorder(new LineBorder(Color.DARK_GRAY));
		pWest.add(trialConfig, BorderLayout.NORTH);
		pWest.add(checkTrialSettingPanel, BorderLayout.CENTER);
		pWest.add(fullTrialPanel, BorderLayout.SOUTH);

		pEast = new JPanel(new BorderLayout());
		pEast.setBorder(new LineBorder(Color.DARK_GRAY));
		pEast.add(myoConnection, BorderLayout.CENTER);

		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(new LineBorder(Color.BLACK));
		main.add(p3, BorderLayout.CENTER);
		main.add(pWest, BorderLayout.WEST);
		main.add(pEast, BorderLayout.EAST);

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

		setBackground(Color.WHITE);
		getContentPane().add(main);

		controller.initiateController();
		trialConfig.initiateConfiguration();
		// trialConfig.switchToAuto(); // Set to auto mode;
		trialConfig.buttonNoBeep_RecordButton.doClick(); // Set to default option;
		repaint();
	}

	private void updateInstructions1() {
		instructions1.setText(startTrialMsg);
		libetReadingsArea.setText("");
	}

	private void updateInstructions2() {

		int currentTrialIndex = controller.getTrialGroupIndex();
		int currentBlockType = controller.getBlockOrder()[currentTrialIndex];
		String inputMsg = "";

		if (trialPanel.getUseNoInput()) {
			inputMsg = infoStringNoInput;
		} else {
			if (currentBlockType < 4)
				inputMsg = infoStringButtonInput;
			else if (currentBlockType >= 4)
				inputMsg = infoStringEMGInput;
		}

		if (trialPanel.getPlayBeep() && !trialPanel.getUseNoInput()) {
			inputMsg += "\n\nThis will cause a beep";
		}

		String msg = "Record the time when you ";
		if (currentBlockType == 0 || currentBlockType == 1) {
			msg += "pressed the " + actionKey + ".";
		} else if (currentBlockType == 4 || currentBlockType == 5) {
			msg += "flex your wrist.";
		}

		if (trialPanel.getRecordBeepTime()) {
			msg = "Record the time when you hear the beep.";
		}

		if (controller.getTrialGroupIndex() == 0) {
			instructions2.setText("In the first block of trials: \n\n\n\n" + inputMsg);
		} else {
			instructions2.setText("In the next block of trials: \n\n\n\n" + inputMsg);
		}

		instructions3.setText(msg);
		controller.setMessages(inputMsg, msg);
	}

	public void actionPerformed(ActionEvent e) {
		// Override
		if (e.getSource() == moveOnBtn) {
			resetDisplay();
		} else if (e.getSource() == startFullTrialBtn) {
			trialConfig.switchToAuto();
			controller.createOutputFile();
			activateStartScreen();
			p2.setVisible(false);
			pWest.setVisible(false);
			pEast.setVisible(false);
		} else if (e.getSource() == checkTrialSettingBtn) {
			controllerDisplay();
		}
	}

	private class MyDispatcher implements KeyEventDispatcher {

		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (!blockAllLibetInputs) {
					if (e.getKeyCode() == 10 && libetInputsEnabled && libetTrialRunning
							&& !trialPanel.getUseSensorInput() && !trialPanel.getUseNoInput()) // Enter key
					{
						if (!trialPanel.getDisableActionsStatus() && trialPanel.getTestActive()) {
							System.out.println("Action time (HCI): " + df_localTime.format(new Date()));
						}
						trialPanel.action();
					} else if (e.getKeyCode() == 17 && libetInputsEnabled && !libetTrialRunning
							&& !trialPanel.getUseSensorInput())
					// 'B' or CTRL key
					{
						trialPanel.start();
					} else if (e.getKeyCode() == 17 && libetInputsEnabled && !libetTrialRunning
							&& trialPanel.getUseSensorInput())
					// 'B' or CTRL key
					{
						if (!trialPanel.getUseNoInput()) {
							myoSocket = new MyoSocket();
							if (myoSocket.myoRestCheck().equals("Ready")) {
								trialPanel.start();
								myoDataCapture = new MyoDataCapture();
								thread = new Thread(myoDataCapture);
								thread.start();
							} else {
								JOptionPane.showMessageDialog(null,
										"Please relax your wrist before starting the trial.", "Message",
										JOptionPane.INFORMATION_MESSAGE);
							}
						} else {
							trialPanel.start();
						}
					}
				}
			}
			return false;
		}
	}

	private class MyoDataCapture implements Runnable {

		private MyoSocket myoSocket;

		@Override
		public void run() {
			myoSocket = new MyoSocket();
			if (myoSocket.synchronization().equals("Synced")) {
				if (myoSocket.MyoCapture().equals("active")) {
					if (!trialPanel.getDisableActionsStatus() && trialPanel.getTestActive()) {
						System.out.println("Action time (Myo): " + df_localTime.format(new Date()));
					}
					trialPanel.action();
				}
			}
		}
	}

	public void libetTestStarted() {
		libetTrialRunning = true;
		instructions1.setVisible(false);
		libetReadingsArea.setText("");
		perceivedTime = -1;
		trialConfig.configEnabled(false);
		controller.libetTestStarted();
	}

	public void libetTestFinished() {
		// Create the recording request message
		String msg = "Enter the time when you ";

		if (trialPanel.getRecordBeepTime()) {
			msg += "heard the beep.";
		} else {
			if (trialPanel.getUseSensorInput()) {
				msg += "flexed your wrist.";
			} else {
				msg += "pressed the button.";
			}
		}

		TimeRecordingDialog dial = new TimeRecordingDialog(this, msg);
		perceivedTime = dial.getReturnValue();
		if (perceivedTime == 0) {
			perceivedTime = 60;
		}
		dial.dispose();
		double milliScalar = trialPanel.getRotationTime() / 60;
		double perceivedMilliTime = perceivedTime * milliScalar;

		String data = "<t=";
		if (controller.getManualMode()) {
			data += controller.getTrialType() + " manual>" + "\t " + trialPanel.getLibetData();
		} else {
			data += controller.getTrialType() + ">\t " + trialPanel.getLibetData();
		}

		if (trialPanel.getRecordBeepTime()) {
			data += "\t>> Beep perceived = " + df.format(perceivedMilliTime) + "ms (" + perceivedTime + ")";
		} else {
			data += "\t>> Act perceived = " + df.format(perceivedMilliTime) + "ms (" + perceivedTime + ")";
		}

		double perceptionDifference = perceivedMilliTime - trialPanel.getComputerTime();
		double actualTime = trialPanel.getComputerTime();

		data += "\t>> Diff = " + df.format(perceptionDifference);

		if (perceptionDifference > 1750 && perceptionDifference != 2560) {
			perceptionDifference = perceptionDifference - 2560;
			data += "\t>> Rollover: " + df.format(perceptionDifference) + "\n\n";
		} else if (perceptionDifference < -1750) {
			perceptionDifference = perceptionDifference + 2560;
			data += "\t>> Rollover: " + df.format(perceptionDifference) + "\n\n";
		} else {
			data += "\n\n";
		}

		controller.writeData(data);
		controller.storeDataPoint(perceptionDifference, actualTime, perceivedMilliTime, trialPanel.getStartTimeMicro(),
				trialPanel.getActionTimeMicro(), trialPanel.getBeepTriggerTimeMicro(), trialPanel.getActualBeepTimeMicro(), trialPanel.getActionTimeLocal());

		DecimalFormat df = new DecimalFormat("#.##");
		String perceivedTimeString = "";
		String perceptionDifferenceString = "";
		if (controller.getTrialType() == 1 || controller.getTrialType() == 2 || controller.getTrialType() == 5
				|| controller.getTrialType() == 6) {
			perceivedTimeString = "\n" + "Action perceived time = " + perceivedTime + ", milli time = "
					+ perceivedMilliTime;
		} else {
			perceivedTimeString = "\n" + "Beep perceived time = " + perceivedTime + ", milli time = "
					+ perceivedMilliTime;
		}
		perceptionDifferenceString = "\n" + "Perceived difference = " + df.format(perceptionDifference);
		libetReadingsArea.setText(trialPanel.getLibetReadings(perceivedTimeString + perceptionDifferenceString));

		postTrialPause();
		startFullTrialBtn.setEnabled(true);
		controller.libetTestFinished(perceivedTime != 0);
	}

	public void libetTrialModeChanged() {
		updateInstructions1();
		trialConfig.configureBlockOptionsSelections();
		controller.libetTrialModeChanged();
	}

	// Implement CaptureModel Listener methods.
	// public void captureModelActionDetected(int timeSubtractor) {
	// if (libetInputsEnabled && libetTrialRunning && trialPanel.getUseSensorInput()
	// && !trialPanel.getUseNoInput()) {
	// trialPanel.actionWithSubtractor(timeSubtractor);
	// }
	// }
	// Action with subtractor

	private void activateStartScreen() {
		p1.setVisible(false);
		blockAllLibetInputs = true;
		controller.resetBlockGroupUpdated();
		updateInstructions2();
		instructions2Panel.setVisible(true);
	}

	private void activateExperimentCompleteScreen() {
		p1.setVisible(false);
		blockAllLibetInputs = true;
		controller.resetBlockGroupUpdated();
		instructions2.setText("\n\nThe trials are now complete.\n");
		instructions3.setText("Thanks you.\n\n");
		moveOnBtn.setVisible(false);
		instructions2Panel.setVisible(true);
	}

	private void activateNextBlockScreen() {
		updateInstructions2();
		controller.resetBlockGroupUpdated();
		instructions2Panel.setVisible(true);
	}

	private void resetDisplay() {
		instructions1.setVisible(true);
		p1.setVisible(true);
		instructions2Panel.setVisible(false);
		trialConfig.configEnabled(true);
		blockAllLibetInputs = false;
		libetTrialRunning = false;
	}

	private void postTrialPause() {
		p1.setVisible(false);
		timer = new Timer();
		timer.schedule(new RemindTask(), 500);
	}

	class RemindTask extends TimerTask {
		public void run() {
			timer.cancel();
			if (controller.getBlockGroupUpdated()) {
				if (controller.getFinishedLastBlock()) {
					activateExperimentCompleteScreen();
					controller.writestoredData();
				} else {
					activateNextBlockScreen();
				}
			} else {
				resetDisplay();
			}
		}
	}

	public void controllerDisplay() {
		updateInstructions2();
		experimentWindow.setVisible(true);
	}
	
	@Override
	public void libetAcceptingActions() {
	}

	@Override
	public void libetActionReceived() {
	}

	@Override
	public void libetNotAcceptingActions() {
	}

}
