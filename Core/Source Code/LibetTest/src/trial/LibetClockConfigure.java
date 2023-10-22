package trial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import utilities.NumericTextField;

public class LibetClockConfigure extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private TrialPanel trialPanel;
	private JRadioButton useArm;
	private JRadioButton useCircle;
	private JTextField rotationTimeField;
	private JButton updateRotTime;
	Integer[] imageInts = { 0, 1, 2, 3, 4 };
	private JComboBox<Integer> imageList = new JComboBox<Integer>(imageInts);
	Integer[] gapInts = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
	private JComboBox<Integer> gapList = new JComboBox<Integer>(gapInts);
	Integer[] widthInts = { 1, 2 };
	private JComboBox<Integer> widthList = new JComboBox<Integer>(widthInts);
	private JRadioButton ticksYes, ticksNo;
	private JRadioButton tracesYes, tracesNo;

	public LibetClockConfigure(TrialPanel trialPanel) {

		Color lighterGray = new Color(230, 230, 230);
		setBackground(lighterGray);

		this.trialPanel = trialPanel;

		JLabel clockType = new JLabel("Clock style");
		clockType.setBorder(new EmptyBorder(0, 0, 5, 0));
		useArm = new JRadioButton("Arm");
		useArm.addActionListener(this);
		useArm.setBackground(lighterGray);
		useCircle = new JRadioButton("Circle");
		useCircle.addActionListener(this);
		useCircle.setBackground(lighterGray);

		ButtonGroup clockTypeGroup = new ButtonGroup();
		clockTypeGroup.add(useArm);
		clockTypeGroup.add(useCircle);

		JPanel clockConfig = new JPanel();
		clockConfig.setBorder(new EmptyBorder(0, 15, 0, 15));
		clockConfig.setBackground(lighterGray);
		clockConfig.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		clockConfig.add(clockType, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		clockConfig.add(useArm, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		clockConfig.add(useCircle, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.weighty = 1000;
		clockConfig.add(Box.createHorizontalStrut(1), c);

		JLabel clockSize = new JLabel("Clock size");
		clockSize.setBorder(new EmptyBorder(0, 0, 5, 0));
		JLabel clockInfo = new JLabel("0 = smallest");
		clockInfo.setFont(new Font("Dialog", Font.PLAIN, 10));
		clockInfo.setBorder(new EmptyBorder(5, 0, 0, 0));

		imageList.setSelectedIndex(trialPanel.getClockImage());
		imageList.addActionListener(this);

		JPanel clockSizeConfig = new JPanel();
		clockSizeConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		clockSizeConfig.setBackground(lighterGray);
		clockSizeConfig.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		clockSizeConfig.add(clockSize, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockSizeConfig.add(imageList, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockSizeConfig.add(clockInfo, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		clockSizeConfig.add(Box.createHorizontalStrut(1), c);

		JLabel drawTraces = new JLabel("Draw traces");
		drawTraces.setBorder(new EmptyBorder(0, 0, 5, 0));
		tracesYes = new JRadioButton("Yes");
		tracesYes.addActionListener(this);
		tracesYes.setBackground(lighterGray);
		tracesNo = new JRadioButton("No");
		tracesNo.addActionListener(this);
		tracesNo.setBackground(lighterGray);

		ButtonGroup traceGroup = new ButtonGroup();
		traceGroup.add(tracesYes);
		traceGroup.add(tracesNo);

		JPanel traceConfig = new JPanel();
		traceConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		traceConfig.setBackground(lighterGray);
		traceConfig.setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		traceConfig.add(drawTraces, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		traceConfig.add(tracesNo, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		traceConfig.add(tracesYes, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.weighty = 1000;
		traceConfig.add(Box.createHorizontalStrut(1), c);

		JLabel useClockTicks = new JLabel("Small ticks");
		useClockTicks.setBorder(new EmptyBorder(0, 0, 5, 0));
		ticksYes = new JRadioButton("Yes");
		ticksYes.addActionListener(this);
		ticksYes.setBackground(lighterGray);
		ticksNo = new JRadioButton("No");
		ticksNo.addActionListener(this);
		ticksNo.setBackground(lighterGray);

		ButtonGroup tickTypeGroup = new ButtonGroup();
		tickTypeGroup.add(ticksYes);
		tickTypeGroup.add(ticksNo);

		JPanel tickConfig = new JPanel();
		tickConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		tickConfig.setBackground(lighterGray);
		tickConfig.setLayout(new GridBagLayout());

		c.anchor = GridBagConstraints.WEST;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		tickConfig.add(useClockTicks, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0;
		tickConfig.add(ticksNo, c);

		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0;
		tickConfig.add(ticksYes, c);

		c.gridx = 0;
		c.gridy = 3;
		c.weightx = 0;
		c.weighty = 1000;
		tickConfig.add(Box.createHorizontalStrut(1), c);

		JLabel clockArmGap = new JLabel("Arm spacing");
		clockArmGap.setBorder(new EmptyBorder(0, 0, 5, 0));
		JLabel clockGapInfo = new JLabel("0 = no spacing");
		clockGapInfo.setFont(new Font("Dialog", Font.PLAIN, 10));
		clockGapInfo.setBorder(new EmptyBorder(5, 0, 0, 0));

		gapList.setSelectedIndex(trialPanel.getArmGap());
		gapList.addActionListener(this);

		JPanel clockGapConfig = new JPanel();
		clockGapConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		clockGapConfig.setBackground(lighterGray);
		clockGapConfig.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		clockGapConfig.add(clockArmGap, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockGapConfig.add(gapList, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockGapConfig.add(clockGapInfo, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		clockGapConfig.add(Box.createHorizontalStrut(1), c);

		JLabel clockArmWidth = new JLabel("Arm width");
		clockArmWidth.setBorder(new EmptyBorder(0, 0, 5, 0));
		JLabel clockArmWidthInfo = new JLabel(" ");
		clockArmWidthInfo.setFont(new Font("Dialog", Font.PLAIN, 10));
		clockArmWidthInfo.setBorder(new EmptyBorder(5, 0, 0, 0));

		widthList.setSelectedIndex(trialPanel.getArmWidth() - 1);
		widthList.addActionListener(this);

		JPanel clockArmWidthConfig = new JPanel();
		clockArmWidthConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		clockArmWidthConfig.setBackground(lighterGray);
		clockArmWidthConfig.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		clockArmWidthConfig.add(clockArmWidth, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockArmWidthConfig.add(widthList, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		clockArmWidthConfig.add(clockArmWidthInfo, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		clockArmWidthConfig.add(Box.createHorizontalStrut(1), c);

		DecimalFormat format = new DecimalFormat("###");

		JLabel rotationTimeLabel = new JLabel("Rotation time (ms)");
		rotationTimeField = new NumericTextField(7, format);
		rotationTimeField.setMaximumSize(rotationTimeField.getPreferredSize());
		rotationTimeField.setHorizontalAlignment(JTextField.RIGHT);
		updateRotTime = new JButton("Update");
		updateRotTime.addActionListener(this);

		JPanel rotConfig = new JPanel();
		rotConfig.setBorder(new EmptyBorder(0, 0, 0, 15));
		rotConfig.setBackground(lighterGray);
		rotConfig.setLayout(new GridBagLayout());

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0;
		c.weighty = 0;
		rotConfig.add(rotationTimeLabel, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.anchor = GridBagConstraints.EAST;
		rotConfig.add(rotationTimeField, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		rotConfig.add(updateRotTime, c);

		c.gridx = 0;
		c.gridy++;
		c.weightx = 0;
		c.weighty = 1000;
		rotConfig.add(Box.createHorizontalStrut(1), c);

		JPanel overallLibetPanel = new JPanel();
		overallLibetPanel.setLayout(new BoxLayout(overallLibetPanel, BoxLayout.LINE_AXIS));
		overallLibetPanel.setBackground(lighterGray);

		overallLibetPanel.add(Box.createHorizontalGlue());
		overallLibetPanel.add(clockConfig);
		overallLibetPanel.add(clockSizeConfig);
		overallLibetPanel.add(tickConfig);
		overallLibetPanel.add(clockGapConfig);
		overallLibetPanel.add(rotConfig);
		overallLibetPanel.add(traceConfig);
		overallLibetPanel.add(Box.createHorizontalGlue());

		JLabel configureLibelClock = new JLabel("Configure Libet clock");
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		labelPanel.setBorder(new LineBorder(Color.DARK_GRAY));
		labelPanel.setBackground(lighterGray);
		labelPanel.add(Box.createHorizontalStrut(20));
		labelPanel.add(configureLibelClock);
		labelPanel.add(Box.createHorizontalGlue());

		setLayout(new BorderLayout());
		add(overallLibetPanel, BorderLayout.CENTER);

		updateController();
	}

	private void updateController() {

		DecimalFormat df = new DecimalFormat("#####");

		useArm.setSelected(trialPanel.getClockArm());
		useCircle.setSelected(!trialPanel.getClockArm());

		ticksYes.setSelected(trialPanel.getUseTicks());
		ticksNo.setSelected(!trialPanel.getUseTicks());

		tracesYes.setSelected(trialPanel.getDrawTraces());
		tracesNo.setSelected(!trialPanel.getDrawTraces());

		rotationTimeField.setText(df.format(trialPanel.getRotationTime()));
	}

	public void controlerEnabled(boolean b) {
		setVisible(b);
	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource().equals(useArm)) {
			trialPanel.setClockArm(true);
		} else if (e.getSource().equals(useCircle)) {
			trialPanel.setClockArm(false);
		} else if (e.getSource().equals(ticksNo)) {
			trialPanel.setUseTicks(false);
		} else if (e.getSource().equals(ticksYes)) {
			trialPanel.setUseTicks(true);
		} else if (e.getSource().equals(tracesNo)) {
			trialPanel.setDrawTraces(false);
		} else if (e.getSource().equals(tracesYes)) {
			trialPanel.setDrawTraces(true);
		} else if (e.getSource().equals(updateRotTime)) {

			double aDouble = -1;
			try {
				aDouble = Double.parseDouble(rotationTimeField.getText());
			} catch (NumberFormatException ex) {

				JOptionPane.showMessageDialog(this, "Please enter a rotation greater than 500", "Try again",
						JOptionPane.ERROR_MESSAGE);

				rotationTimeField.requestFocusInWindow();
				return;
			}
			if (aDouble >= 500) {
				trialPanel.setRotationTime(aDouble);
			} else {
				JOptionPane.showMessageDialog(this, "Please enter a rotation time greater than 500", "Try again",
						JOptionPane.ERROR_MESSAGE);

				rotationTimeField.requestFocusInWindow();
				return;
			}
		} else if (e.getSource().equals(imageList)) {
			JComboBox<?> cb = (JComboBox<?>) e.getSource();
			int anInt = cb.getSelectedIndex();
			trialPanel.setClockImage(anInt);
		} else if (e.getSource().equals(gapList)) {
			JComboBox<?> cb = (JComboBox<?>) e.getSource();
			int anInt = cb.getSelectedIndex();
			trialPanel.setArmGap(anInt);
		} else if (e.getSource().equals(widthList)) {
			JComboBox<?> cb = (JComboBox<?>) e.getSource();
			int anInt = cb.getSelectedIndex() + 1;
			trialPanel.setArmWidth(anInt);
		}
	}
}
