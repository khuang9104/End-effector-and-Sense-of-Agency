package utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class DataSaveAddressChecker {

	private Scanner scanner;
	private String data_save_path = null;

	public DataSaveAddressChecker() {
		String address_config = null;
		try {
			address_config = new URL("file:resources//data_save_address.txt").getPath();
		} catch (MalformedURLException e1) {
			throw new RuntimeException("Address config file not found");
		}
		try {
			File file = new File(address_config);
			scanner = new Scanner(file);
			data_save_path = scanner.nextLine();
			System.out.println(data_save_path);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Address config file not found");
		}
	}
	
	public String getDataSaveAddress() {
		return data_save_path;
	}
	
	public void checkAddressExist() {
		File file = new File(data_save_path);
		if (!file.exists()){
			String message = "Data Save Path: " + data_save_path + " is not exist, please double check the address config txt.";
			JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
	}
}
