package trial;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import utilities.NumericTextField;

public class BlockConfigurationPanel extends JPanel implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	private JButton updateBtn;
	private NumericTextField[] textFields = new NumericTextField[8];

	private int[] results = {-1, -1, -1, -1, -1, -1, -1, -1};
	private int[] initialOrder;

	private boolean warningVisible = false;
	private boolean orderChanged = false;

	private TrialController controller;

	public int[] getReturnValues() {
		return results;
	}

	public boolean readResetOrderChanged() {
		boolean b = getOrderChanged();
		orderChanged = false;
		return b;
	}

	private boolean getOrderChanged() {
		return orderChanged;
	}

	public BlockConfigurationPanel(TrialController ctrl) {

		controller = ctrl;
		initialOrder = controller.getBlockOrder();

		DecimalFormat format = new DecimalFormat("###");
		JLabel msgBlockLabel = new JLabel("Block order: ");

		updateBtn = new JButton("Update");
		updateBtn.addActionListener(this);

		JPanel actionPanel = new JPanel();
		actionPanel.setBackground(Color.LIGHT_GRAY);
		actionPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;

		c.weightx = 1000;
		c.gridx++;
		actionPanel.add(Box.createHorizontalStrut(1), c);

		c.weightx = 0;
		for (int i = 0; i < textFields.length; i++) {
			textFields[i] = new NumericTextField(2, format);
			textFields[i].addKeyListener(this);
			textFields[i].setHorizontalAlignment(JTextField.CENTER);
			textFields[i].addActionListener(this);

			c.gridx++;
			actionPanel.add(textFields[i], c);

			c.gridx++;
			actionPanel.add(Box.createHorizontalStrut(1), c);
		}

		updateTextFields(initialOrder);
		enableTextFields(false);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.LIGHT_GRAY);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(updateBtn);

		JPanel overallPanel = new JPanel();
		overallPanel.setBackground(Color.LIGHT_GRAY);
		overallPanel.setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;

		overallPanel.add(msgBlockLabel, c);
		c.gridy++;
		overallPanel.add(actionPanel, c);
		c.anchor = GridBagConstraints.EAST;
		c.gridy++;
		overallPanel.add(buttonPanel, c);
		c.gridy++;
		c.gridx = 0;
		c.gridwidth = 1;
		overallPanel.add(Box.createHorizontalStrut(300), c);

		add(overallPanel);
		setBackground(Color.LIGHT_GRAY);
	}

	private void updateTextFields(int[] order) {
		for (int i = 0; i < textFields.length; i++) {
			String s = (order[i] + 1) + "";
			textFields[i].setText(s);
		}
	}

	public void enableInputs(boolean b) {
		updateBtn.setEnabled(b);
		enableTextFields(b);
	}

	private void enableTextFields(boolean b) {
		for (int i = 0; i < textFields.length; i++) {
			textFields[i].setEditable(b);
			textFields[i].setFocusable(b);
			textFields[i].setEnabled(b);
		}
	}

	private void verifyInput() {

		for (int i = 0; i < textFields.length; i++) {
			int no = -1;
			try {
				no = Integer.parseInt(textFields[i].getText());
			} catch (NumberFormatException ex) {
				warningVisible = true;
				JOptionPane.showMessageDialog(this, "Please enter a number from 1 to 8 in box " + String.valueOf(i + 1),
						"Try again", JOptionPane.ERROR_MESSAGE);
				warningVisible = false;
				textFields[i].setText("");
				textFields[i].requestFocusInWindow();
				return;
			}

			if (no > 0 && no <= 8) {
				results[i] = no - 1;
			} else {
				warningVisible = true;
				JOptionPane.showMessageDialog(this, "Please enter a number from 1 to 8 in box " + String.valueOf(i + 1),
						"Try again", JOptionPane.ERROR_MESSAGE);
				warningVisible = false;
				textFields[i].setText("");
				textFields[i].requestFocusInWindow();
			}
		}
		if (checkForDuplicates(results)) {
			warningVisible = true;
			JOptionPane.showMessageDialog(this, "Please check for duplicate block numbers", "Try again",
					JOptionPane.ERROR_MESSAGE);
			warningVisible = false;
			textFields[0].requestFocusInWindow();
		} else {
			controller.setBlockOrder(results);
			updateBtn.setForeground(Color.BLACK);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateBtn && !warningVisible) {
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

	private boolean checkForDuplicates(int[] sValueTemp) {
		Set<Integer> sValueSet = new HashSet<Integer>();
		for (int tempValueSet : sValueTemp) {
			if (sValueSet.contains(tempValueSet))
				return true;
			else
				sValueSet.add(tempValueSet);
		}
		return false;
	}
}