package trial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MyoConnectionPanel extends JPanel implements ActionListener, Runnable {

	private static final long serialVersionUID = 1L;
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private JLabel titleLabel, connectionDescription, connectionStatus, myoActivitionDescription, myoActivitionStatus;
	private JLabel myoDurationDescription, myoDurationStatus;
	private JButton testConnectBtn, starttestConnectBtn, resetBtn;
	private MyoSocket myoSocket = new MyoSocket();
	private int count = 0;
	private long startTime = 0, stopTime = 0, responseDuration = 0;
	private long nanoToMilli = 1000000;
	private Thread thread;

	public MyoConnectionPanel() {

		setBackground(Color.LIGHT_GRAY);
		GridBagConstraints c = new GridBagConstraints();

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setBorder(new EmptyBorder(25, 15, 10, 15));
		titlePanel.setBackground(Color.LIGHT_GRAY);
		titleLabel = new JLabel("Myo Connection Test");
		titlePanel.add(titleLabel);

		JPanel myoConnection = new JPanel();
		myoConnection.setBorder(new EmptyBorder(10, 10, 10, 10));
		myoConnection.setBackground(Color.LIGHT_GRAY);
		connectionDescription = new JLabel("Myo Connection status: ");
		connectionStatus = new JLabel("Disconnected");
		myoConnection.add(connectionDescription);
		myoConnection.add(connectionStatus);

		JPanel myoActivition = new JPanel();
		myoActivition.setBorder(new EmptyBorder(10, 10, 10, 10));
		myoActivition.setBackground(Color.LIGHT_GRAY);
		myoActivitionDescription = new JLabel("Myo Activition Count: ");
		myoActivitionStatus = new JLabel(String.valueOf(count));
		myoActivition.add(myoActivitionDescription);
		myoActivition.add(myoActivitionStatus);

		JPanel myoResponseDuration = new JPanel();
		myoResponseDuration.setBorder(new EmptyBorder(10, 10, 10, 10));
		myoResponseDuration.setBackground(Color.LIGHT_GRAY);
		myoDurationDescription = new JLabel("Response Duration: ");
		myoDurationStatus = new JLabel("  ms");
		myoResponseDuration.add(myoDurationDescription);
		myoResponseDuration.add(myoDurationStatus);

		testConnectBtn = new JButton("Test Connection");
		testConnectBtn.setBackground(Color.LIGHT_GRAY);
		testConnectBtn.addActionListener(this);
		JPanel myoBottonPanel_1 = new JPanel();
		myoBottonPanel_1.setBorder(new EmptyBorder(10, 10, 10, 10));
		myoBottonPanel_1.setBackground(Color.LIGHT_GRAY);
		myoBottonPanel_1.add(testConnectBtn);

		starttestConnectBtn = new JButton("Start Test");
		starttestConnectBtn.setBackground(Color.LIGHT_GRAY);
		starttestConnectBtn.addActionListener(this);
		resetBtn = new JButton("Reset");
		resetBtn.setBackground(Color.LIGHT_GRAY);
		resetBtn.addActionListener(this);
		JPanel myoBottonPanel_2 = new JPanel();
		myoBottonPanel_2.setBorder(new EmptyBorder(10, 10, 10, 10));
		myoBottonPanel_2.setBackground(Color.LIGHT_GRAY);
		myoBottonPanel_2.add(starttestConnectBtn);
		myoBottonPanel_2.add(resetBtn);
		starttestConnectBtn.setEnabled(false);
		resetBtn.setEnabled(false);

		JPanel overallPanel = new JPanel();
		overallPanel.setBackground(Color.LIGHT_GRAY);
		overallPanel.setLayout(new GridBagLayout());
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.NORTHWEST;
		overallPanel.add(titlePanel, c);
		c.gridy++;
		overallPanel.add(Box.createVerticalStrut(20), c);
		c.gridy++;
		overallPanel.add(myoConnection, c);
		c.gridy++;
		overallPanel.add(myoActivition, c);
		c.gridy++;
		overallPanel.add(myoResponseDuration, c);
		c.gridy++;

		c.gridx = 0;
		c.gridy = 10;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		overallPanel.add(myoBottonPanel_1, c);

		c.gridx = 0;
		c.gridy = 15;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 1;
		c.anchor = GridBagConstraints.CENTER;
		overallPanel.add(myoBottonPanel_2, c);

		setLayout(new BorderLayout());
		add(overallPanel, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(testConnectBtn)) {
			if (myoSocket.checkConnect().equals("Connected")) {
				connectionStatus.setText("Connected   ");
				starttestConnectBtn.setEnabled(true);
				resetBtn.setEnabled(true);

			} else {
				connectionStatus.setText("Disconnected");
				starttestConnectBtn.setEnabled(false);
				resetBtn.setEnabled(false);
			}
		}
		if (source.equals(starttestConnectBtn)) {
			if (myoSocket.myoRestCheck().equals("Ready")) {
				thread = new Thread(this);
				thread.start();
			} else {
				JOptionPane.showMessageDialog(null, "Please relax your wrist before the trial.", "Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
		if (source.equals(resetBtn)) {
			count = 0;
			myoActivitionStatus.setText(String.valueOf(count));
			myoDurationStatus.setText("  ms");
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		startTime = System.nanoTime();
		if (myoSocket.synchronization().equals("Synced")) {
			System.out.println("Synced time: " + df.format(new Date()));
			if (myoSocket.MyoCapture().equals("active")) {
				System.out.println("Active time: " + df.format(new Date()));
				stopTime = System.nanoTime();
				responseDuration = (stopTime - startTime) / nanoToMilli;
				count = count + 1;
				myoActivitionStatus.setText(String.valueOf(count));
				myoDurationStatus.setText(String.valueOf(responseDuration) + " ms");
			}
		} else {
			System.err.println("Sync error");
		}
	}
}
