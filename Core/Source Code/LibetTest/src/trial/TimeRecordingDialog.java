package trial;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import utilities.NumericTextField;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

public class TimeRecordingDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private int border = 20;
	private int result = -1;
	private boolean warningVisible = false;

	private JButton enterBtn;
	private NumericTextField textField;

	public int getReturnValue() {
		return result;
	}

	public TimeRecordingDialog(Frame aFrame, String msg) {
		super(aFrame, true);
		setTitle("");

		JLabel msgLabel = new JLabel(msg);
		msgLabel.setBorder(new EmptyBorder(border, border, border, border));

		DecimalFormat format = new DecimalFormat("###");

		textField = new NumericTextField(6, format);
		textField.addKeyListener(this);
		textField.setHorizontalAlignment(JTextField.CENTER);
		enterBtn = new JButton("Enter");
		enterBtn.addActionListener(this);

		JPanel actionPanel = new JPanel();
		actionPanel.setBorder(new EmptyBorder(border, border, 2 * border, 2 * border));
		actionPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1000;
		c.weighty = 0;
		actionPanel.add(Box.createHorizontalGlue(), c);

		c.gridx++;
		c.weightx = 0;
		actionPanel.add(textField, c);
		c.gridx++;
		actionPanel.add(Box.createHorizontalStrut(10), c);
		c.gridx++;
		actionPanel.add(enterBtn, c);

		JPanel overallPanel = new JPanel(new BorderLayout());
		overallPanel.add(msgLabel, BorderLayout.NORTH);
		overallPanel.add(actionPanel, BorderLayout.SOUTH);
		overallPanel.add(Box.createHorizontalStrut(400), BorderLayout.CENTER);

		// Make this dialog display it.
		setContentPane(overallPanel);

		// Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		// Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textField.requestFocusInWindow();
			}
		});

		// Register an event handler that puts the text into the option pane.
		textField.addActionListener(this);
		pack();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = aFrame.getSize();

		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}

		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}

		int x = (screenSize.width - frameSize.width) / 2 + (frameSize.width / 2) - (getWidth() / 2) - 10;
		int y = (screenSize.height - frameSize.height) / 2 + 200;

		setLocationRelativeTo(aFrame);
		setLocation(x, y);
		setVisible(true);
	}

	private void verifyInput() {
		int no = -1;
		try {
			no = Integer.parseInt(textField.getText());
		} catch (NumberFormatException ex) {
			warningVisible = true;
			JOptionPane.showMessageDialog(this, "Please enter a number from 1 to 60", "Try again",
					JOptionPane.ERROR_MESSAGE);
			warningVisible = false;
			result = no;
			textField.setText("");
			textField.requestFocusInWindow();
			return;
		}
		if (no > 0 && no <= 60) {
			result = no;
			clearAndHide();
		} else if (no == 0) {
			result = no;
			clearAndHide();
		} else {
			warningVisible = true;
			JOptionPane.showMessageDialog(this, "Please enter a number from 1 to 60", "Try again",
					JOptionPane.ERROR_MESSAGE);
			warningVisible = false;
			result = no;
			textField.setText("");
			textField.requestFocusInWindow();
		}
	}

	/** This method handles events for the text field. */
	public void actionPerformed(ActionEvent e) {
		if (isVisible() && e.getSource() == enterBtn && !warningVisible) {
			verifyInput();
		}
	}

	public void keyPressed(KeyEvent e) {
		if (isVisible() && e.getKeyCode() == 10) {
			verifyInput();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// This method clears the dialog and hides it.
	public void clearAndHide() {
		setVisible(false);
	}
}