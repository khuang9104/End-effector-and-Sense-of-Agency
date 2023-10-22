package trial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MyoSocket{

	private String ip = "localhost";
	private int port = 9999;
	private int port_dataCapture = 8888;
	private Socket socket = null;
	private String checkConnection = "Connect";
	private String sync = "Sync";
	private String restCheck = "RestCheck";
	private byte[] input_checkConnection = checkConnection.getBytes();
	private byte[] input_sync = sync.getBytes();
	private byte[] input_restCheck = restCheck.getBytes();

	public MyoSocket() {
	}

	public String checkConnect() {
		return socketCom(input_checkConnection);
	}

	public String synchronization() {
		return socketCom(input_sync);
	}
	
	public String myoRestCheck() {
		return socketCom(input_restCheck);
	}

	private String socketCom(byte[] command) {
		String result = "Disconnected";
		try {
			socket = new Socket(ip, port);
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			BufferedReader inRead = new BufferedReader(new InputStreamReader(in));
			out.write(command);
			result = inRead.readLine();
			socket.close();
			return result;
		} catch (UnknownHostException e) {
			System.err.println("Socket Unknown HostException");
		} catch (IOException e) {
			System.err.println("Socket IO Exception");
		}
		return result;
	}

	public String MyoCapture() {
		String result = "MyoCapture Socket Exception";
		try {
			socket = new Socket(ip, port_dataCapture);
			InputStream in = socket.getInputStream();
			BufferedReader inRead = new BufferedReader(new InputStreamReader(in));
			result = inRead.readLine();
			socket.close();
			return result;
		} catch (UnknownHostException e) {
			System.err.println("MyoCapture Socket Unknown HostException");
		} catch (IOException e) {
			System.err.println("MyoCapture Socket IO Exception");
		}
		return result;
	}
}
