package utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import trial.TrialSetting;

public class WriteTrialData {

	private String fileAddr = null;
	private boolean writtenOnce = false;
	private BufferedWriter out;
	private String fileName = "\\TrialData_Default.txt";
	private int[] trialOrder;
	private DataSaveAddressChecker dataAddressChecker = new DataSaveAddressChecker();

	public WriteTrialData(int[] order) {
		dataAddressChecker.checkAddressExist();
		this.fileAddr = dataAddressChecker.getDataSaveAddress();
		this.trialOrder = order;
	}

	public void createUniqueOutputFile() {
		fileName = "\\LibetTrial_" + getDateTime() + ".txt";
		writeTrialInfo();
		writeTrialOrder();
	}

	public String getFileName() {
		return fileName;
	}

	public void setBlockOrder(int[] order) {
		trialOrder = order;
	}

	private void writeTrialInfo() {
		writtenOnce = false;
		writeData(fileName + "\n\nTime and date = " + getDateTime() + "\n\n" + getTypeInfo() + "\n\n");
	}

	private void writeTrialOrder() {
		String s = "Trial order = ";
		for (int i = 0; i < trialOrder.length; i++) {
			s += String.valueOf(trialOrder[i]+1) + " ";
		}
		writeData(s + "\n\n");
	}

	public boolean writeData(String newData) {
		try {
			if (writtenOnce) {
				out = new BufferedWriter(new FileWriter(fileAddr+fileName, true));
			} else {
				// System.out.println("Overwriting file " + fileName);
				out = new BufferedWriter(new FileWriter(fileAddr+fileName));
				writtenOnce = true;
			}
			out.write(newData);
			out.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	private String getDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH.mm");
		Date date = new Date();
		return dateFormat.format(date);
	}

	private String getTypeInfo() {
		String s = "";
		for (int i = 0; i < TrialSetting.TRIALTYPES.length; i++) {
			s += "Type " + (i+1) + " = " + TrialSetting.TRIALTYPES[i] + "\n";
		}
		return s;
	}
}
