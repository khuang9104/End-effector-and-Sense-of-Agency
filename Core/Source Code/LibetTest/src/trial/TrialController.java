package trial;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import jxl.write.WriteException;
import utilities.StoredDoubles;
import utilities.WriteToExcel;
import utilities.WriteTrialData;

public class TrialController {

	private TrialPanel libetPanel = null;
	private int currentCount = 0;
	private int trialGroupIndex = 0;

	private int[] blockOrder;
	private int blockSize;
	private boolean manualMode;

	private boolean blockGroupUpdated = false;
	private boolean finishedLastBlock = false;

	private StoredDoubles[] storedData_estimationError = null;
	private StoredDoubles[] storedData_actualTime = null;
	private StoredDoubles[] storedData_perceivedTime = null;

	private StoredDoubles[] storedData_startTimeMicro = null;
	private StoredDoubles[] storedData_actionTimeMicro = null;
	private StoredDoubles[] storedData_beepTriggerTimeMicro = null;
	private StoredDoubles[] storedData_actualBeepTimeMicro = null;
	private StoredDoubles[] storedData_actionTimeLocal = null;
	
	private int trialType = -1;

	private WriteTrialData writeTrialData;

	private JLabel exp_TrialType;
	private JTextArea exp_Instructions0, exp_Instructions1, exp_Instructions2;
	private JLabel exp_Counter, exp_BlockSize, exp_BlockOrder;

	public TrialController(TrialPanel lPanel, int[] order, int size, boolean manMode) {

		libetPanel = lPanel;

		if (order == null)
			blockOrder = TrialSetting.defaultBlockOrder;
		else
			blockOrder = order;

		if (size == -1)
			blockSize = TrialSetting.defaultBlockSize;
		else
			blockSize = size;

		setManualMode(manMode);

		trialType = blockOrder[trialGroupIndex];

		writeTrialData = new WriteTrialData(blockOrder);

		createstoredData();

		System.out.println("Experiment initialization completed");
	}

	private void createstoredData() {
		storedData_estimationError = new StoredDoubles[blockOrder.length];
		storedData_actualTime = new StoredDoubles[blockOrder.length];
		storedData_perceivedTime = new StoredDoubles[blockOrder.length];

		storedData_startTimeMicro = new StoredDoubles[blockOrder.length];
		storedData_actionTimeMicro = new StoredDoubles[blockOrder.length];
		storedData_beepTriggerTimeMicro = new StoredDoubles[blockOrder.length];
		storedData_actualBeepTimeMicro = new StoredDoubles[blockOrder.length];
		
		storedData_actionTimeLocal = new StoredDoubles[blockOrder.length];

		for (int i = 0; i < storedData_estimationError.length; i++) {
			storedData_estimationError[i] = new StoredDoubles(blockSize);
			storedData_actualTime[i] = new StoredDoubles(blockSize);
			storedData_perceivedTime[i] = new StoredDoubles(blockSize);

			storedData_startTimeMicro[i] = new StoredDoubles(blockSize);
			storedData_actionTimeMicro[i] = new StoredDoubles(blockSize);
			storedData_beepTriggerTimeMicro[i] = new StoredDoubles(blockSize);
			storedData_actualBeepTimeMicro[i] = new StoredDoubles(blockSize);
			
			storedData_actionTimeLocal[i] = new StoredDoubles(blockSize);
		}
	}

	public void createControllerDisplay(JFrame experimentWindow) {
		JPanel exp_InfoPanel = new JPanel();
		exp_TrialType = new JLabel("Current block = " + String.valueOf(trialType + 1));
		exp_TrialType.setFont(new Font("Dialog", Font.PLAIN, 14));
		exp_TrialType.setForeground(Color.BLUE);

		exp_Counter = new JLabel("Trial count = " + currentCount);
		exp_Counter.setFont(new Font("Dialog", Font.PLAIN, 14));
		exp_Counter.setForeground(Color.BLUE);

		exp_BlockSize = new JLabel("(Total: " + blockSize + ")");
		exp_BlockSize.setFont(new Font("Dialog", Font.PLAIN, 14));

		JPanel infoP1 = new JPanel();
		infoP1.setBorder(new EmptyBorder(15, 20, 15, 20));
		infoP1.setLayout(new BoxLayout(infoP1, BoxLayout.X_AXIS));
		infoP1.add(exp_TrialType);
		infoP1.add(Box.createHorizontalStrut(35));
		infoP1.add(exp_Counter);
		infoP1.add(Box.createHorizontalStrut(20));
		infoP1.add(exp_BlockSize);
		infoP1.add(Box.createHorizontalGlue());

		exp_BlockOrder = new JLabel("Block order = " + getBlockOrderToString());
		exp_BlockOrder.setFont(new Font("Dialog", Font.PLAIN, 14));

		JPanel infoP2 = new JPanel();
		infoP2.setBorder(new EmptyBorder(15, 20, 15, 20));
		infoP2.setLayout(new BoxLayout(infoP2, BoxLayout.X_AXIS));
		infoP2.add(exp_BlockOrder);
		infoP2.add(Box.createHorizontalGlue());

		exp_Instructions0 = new JTextArea("Instructions:");
		exp_Instructions0.setFocusable(false);
		exp_Instructions0.setEditable(false);
		exp_Instructions0.setFont(new Font("Dialog", Font.PLAIN, 16));
		exp_Instructions0.setBorder(new EmptyBorder(10, 20, 0, 10));
		exp_Instructions0.setForeground(Color.BLUE);

		exp_Instructions1 = new JTextArea("instructions here");
		exp_Instructions1.setFocusable(false);
		exp_Instructions1.setEditable(false);
		exp_Instructions1.setFont(new Font("Dialog", Font.PLAIN, 16));
		exp_Instructions1.setBorder(new EmptyBorder(0, 20, 0, 10));

		exp_Instructions2 = new JTextArea("instructions here");
		exp_Instructions2.setFocusable(false);
		exp_Instructions2.setEditable(false);
		exp_Instructions2.setFont(new Font("Dialog", Font.PLAIN, 16));
		exp_Instructions2.setForeground(Color.RED);
		exp_Instructions2.setBorder(new EmptyBorder(0, 20, 0, 10));

		exp_InfoPanel.setLayout(new BoxLayout(exp_InfoPanel, BoxLayout.Y_AXIS));
		exp_InfoPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
		exp_InfoPanel.add(infoP1);
		exp_InfoPanel.add(infoP2);
		exp_InfoPanel.add(Box.createVerticalStrut(10));
		exp_InfoPanel.add(exp_Instructions0);
		exp_InfoPanel.add(exp_Instructions1);
		exp_InfoPanel.add(exp_Instructions2);

		if (experimentWindow != null) {
			experimentWindow.add(exp_InfoPanel);
			experimentWindow.setTitle("Experiment info");
			experimentWindow.setSize(500, 400);
			experimentWindow.setLocation(50, 50);
			experimentWindow.setResizable(false);
			experimentWindow.setVisible(false);
			experimentWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		}
	}

	public void setMessages(String m1, String m2) {
		exp_Instructions1.setText(m1);
		exp_Instructions2.setText(m2);
	}

	public void initiateController() {
		updateLibetTrialSettings();
	}

	public void setTrialType(int i) {
		trialType = i;
		exp_TrialType.setText("Current block = " + String.valueOf(i + 1));
		System.out.println("Trial type switched to " + String.valueOf(i + 1));
	}

	public int getTrialType() {
		return trialType + 1;
	}

	public int getTrialGroupIndex() {
		return trialGroupIndex;
	}

	public void setManualMode(boolean b) {
		manualMode = b;
		if (!manualMode) {
			trialGroupIndex = 0;
			updateLibetTrialSettings();
		}
	}

	public boolean getManualMode() {
		return manualMode;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int bSize) {
		this.blockSize = bSize;
		exp_BlockSize.setText("(" + bSize + ")");
		createstoredData();
	}

	public int[] getBlockOrder() {
		return blockOrder;
	}

	public void setBlockOrder(int[] order) {
		blockOrder = order;
		writeTrialData.setBlockOrder(blockOrder);
		if (!manualMode) {
			trialGroupIndex = 0;
			updateLibetTrialSettings();
			createstoredData();
		}
		exp_BlockOrder.setText("Block order = " + getBlockOrderToString());
	}

	public String getBlockOrderToString() {
		String s = "";
		for (int i = 0; i < blockOrder.length; i++) {
			s += String.valueOf(blockOrder[i] + 1) + "    ";
		}
		return s;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public boolean getBlockGroupUpdated() {
		return blockGroupUpdated;
	}

	public void resetBlockGroupUpdated() {
		blockGroupUpdated = false;
	}

	public boolean getFinishedLastBlock() {
		return finishedLastBlock;
	}

	public void resetFinishedLastBlock() {
		finishedLastBlock = false;
	}

	private void moveToNextTrialGroup() {
		if (manualMode)
			return;
		trialGroupIndex++;
		writeData("\n\n\n");
		if (trialGroupIndex >= blockOrder.length) {
			System.out.println("Experiment completed");
			trialGroupIndex = 0;
			finishedLastBlock = true;
		}
		blockGroupUpdated = true;
		updateLibetTrialSettings();
	}

	private void updateLibetTrialSettings() {
		int mode = blockOrder[trialGroupIndex];
		exp_TrialType.setText("Current block = " + String.valueOf(mode + 1));
		switch (mode) {
		case TrialSetting.BUTTON_No_BEEP_Rec_BUTTON:
			libetPanel.setUseSensorInput(false);
			libetPanel.setPlayBeep(false);
			libetPanel.setRecordBeepTime(false);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.BUTTON_Plus_BEEP_Rec_BUTTON:
			libetPanel.setUseSensorInput(false);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(false);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.BUTTON_Plus_BEEP_Rec_BEEP:
			libetPanel.setUseSensorInput(false);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(true);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.BUTTON_No_ACTION_Rec_BEEP:
			libetPanel.setUseSensorInput(false);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(true);
			libetPanel.setUseNoInput(true);
			break;

		case TrialSetting.SENSOR_No_BEEP_Rec_FLEX:
			libetPanel.setUseSensorInput(true);
			libetPanel.setPlayBeep(false);
			libetPanel.setRecordBeepTime(false);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.SENSOR_Plus_BEEP_Rec_FLEX:
			libetPanel.setUseSensorInput(true);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(false);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.SENSOR_Plus_BEEP_Rec_BEEP:
			libetPanel.setUseSensorInput(true);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(true);
			libetPanel.setUseNoInput(false);
			break;
		case TrialSetting.SENSOR_No_ACTION_Rec_BEEP:
			libetPanel.setUseSensorInput(true);
			libetPanel.setPlayBeep(true);
			libetPanel.setRecordBeepTime(true);
			libetPanel.setUseNoInput(true);
			break;

		default:
			System.out.println("Invalid mode.");
			break;
		}
	}

	public void libetTestStarted() {
		if (!manualMode) {
			trialType = blockOrder[trialGroupIndex];
			exp_TrialType.setText("Current block = " + String.valueOf(trialType + 1));
		}
	}

	public void libetAcceptingActions() {
	}

	public void libetActionReceived() {
	}

	public void libetNotAcceptingActions() {
	}

	public void libetTestFinished(boolean include) {
		if (manualMode)
			return;
		if (include)
			currentCount++;
		exp_Counter.setText("Trial count = " + currentCount);
		if (currentCount >= blockSize) {
			moveToNextTrialGroup();
		}
	}

	public void libetTrialModeChanged() {
		currentCount = 0;
		exp_Counter.setText("Trial count = " + currentCount);
	}

	public void storeDataPoint(double estimation_error, double actual_time, double perceived_time,
			double start_time_micro, double action_time_micro, double beep_triger_time_micro, double actual_beep_time_micro, double action_time_local) {
		storedData_estimationError[trialGroupIndex].setValue(estimation_error, currentCount);
		storedData_actualTime[trialGroupIndex].setValue(actual_time, currentCount);
		storedData_perceivedTime[trialGroupIndex].setValue(perceived_time, currentCount);

		storedData_startTimeMicro[trialGroupIndex].setValue(start_time_micro, currentCount);
		storedData_actionTimeMicro[trialGroupIndex].setValue(action_time_micro, currentCount);
		storedData_beepTriggerTimeMicro[trialGroupIndex].setValue(beep_triger_time_micro, currentCount);
		storedData_actualBeepTimeMicro[trialGroupIndex].setValue(actual_beep_time_micro, currentCount);
		
		storedData_actionTimeLocal[trialGroupIndex].setValue(action_time_local, currentCount);
	}

	public void writestoredData() {
		String s = "\n\n\n\nStored data: \n\n";
		for (int i = 0; i < storedData_estimationError.length; i++)
			s += storedData_estimationError[i].toString() + "\n";
		writeData(s);
		try {
			WriteToExcel writeToExcel = new WriteToExcel();
			String name = getOutputFilename();
			name = name.replaceAll(".txt", ".xls");
			writeToExcel.write(name, blockOrder, storedData_estimationError, storedData_actualTime,
					storedData_perceivedTime, storedData_startTimeMicro, storedData_actionTimeMicro,
					storedData_beepTriggerTimeMicro, storedData_actualBeepTimeMicro, storedData_actionTimeLocal);
		} catch (WriteException e) {
			System.out.println("Excel problem");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Excel problem");
			e.printStackTrace();
		}
	}

	public void writeData(String data) {
		writeTrialData.writeData(data);
	}

	public String getOutputFilename() {
		return writeTrialData.getFileName();
	}

	public void createOutputFile() {
		writeTrialData.createUniqueOutputFile();
	}
}
