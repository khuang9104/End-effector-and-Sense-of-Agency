package trial;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

import utilities.AudioPlayerClip;

public class TrialPanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;

	private Set<TrialListener> trialListener = new HashSet<TrialListener>(1);

	private boolean drawTraces = TrialSetting.drawTraces;
	private boolean drawActionBlocker = TrialSetting.drawActionBlocker;

	// Trial settings
	private boolean useSensorInput = false;
	private boolean playBeep = false;
	private boolean requestBeepTime = false;
	private boolean useNoInput = false;

	private String messageToUser = "read this";
	private boolean userMessageOn = false;
	
	private Image image;
	private int radius;
	private int armGap = 6;
	private boolean clockArm = true; // true = use clock arm, false = use rotating circle.
	private boolean useTicks = false;
	private double rotationTime = 2560; // rotation time of Libet clock in ms.
	private int clockImage = 0;
	private int armWidth = 1;
	private boolean drawClock = false;
	private int circleDiameter = 8;

	private AudioPlayerClip beepPlayer; // Test Audio Clip

	private int threadUpdateTime = 1; // Thread sleep time in ms.
	private int paintUpdateRate = 20; // Libet clock update rate = printUpdateRate*threadUpdateTime
	private int paintUpdateCounter = 0;

	private double degreesPerMilliSec;
	private String libetReadings = "";
	private String libetData = "";

	private double degreesCurrent;
	private double radiansCurrent;
	private long startTime, actionTime, beepTime;
	private double actionTimeLocal;
	private long timeSinceActionMS = 0;
	private long timeSinceBeepMS = 0;
	private long nanoToMilli = 1000000;
	private int beepInterval = 250; // Delay time before beep in ms.
	private int blockActionTime = 250;
	private int beepIntervalSubtractor = 0;
	private Random generator = new Random();
	private int randomPostBeepDelay, randomNoActionDelay; // Delay before clock disappears in ms.

	private boolean testActive = false;
	private boolean acceptingActions = false;
	private boolean disableActions = false;
	private boolean beepTriggered = false;
	private boolean actionTriggered = false;

	private double startDegrees = 0;
	private double actionDegrees, actionDegreesWithSubtractor = 0;
	private double beepDegrees = 0;

	private double computerTime = 0;

	private DecimalFormat df = new DecimalFormat("#.##");
	private SimpleDateFormat df_localTime = new SimpleDateFormat("ssSSS");
	private SimpleDateFormat df_localTime_test = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS"); // for test only

	private Thread thread;

	private BufferedImage dbImage;
	private Graphics2D dbImageGraphics;
	private RenderingHints renderHints;

	private Ellipse2D.Double ellipse;
	private Ellipse2D.Double clearCirc;
	private Ellipse2D.Double centreCirc;
	private Line2D.Double line;

	private Line2D.Double actionTrace, actionTraceWithSubtraction, beepTrace, startTrace;

	private long beepTriggerInterval, actualBeepTime;

	public TrialPanel() {
		
		String audio_path = null;
		try {
			audio_path = new URL("file:resources//audio//beep0.wav").getPath();
		} catch (MalformedURLException e1) {
			System.err.print("Audio path error");
		}
		try {
			beepPlayer = new AudioPlayerClip(audio_path);
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			System.err.println("Beep player error.");
		}
		
		/*
		 * try { beepPlayer = new AudioPlayerClip("/src/resources/audio/beep0.wav"); }
		 * catch (LineUnavailableException | IOException | UnsupportedAudioFileException
		 * e) { System.err.println("Beep player error."); }
		 */

		setBackground(Color.WHITE);
		setClockImage(clockImage);
		updateDrawingInfo();
		resetDateDisplay();

		renderHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		renderHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

		dbImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		dbImageGraphics = dbImage.createGraphics();
		dbImageGraphics.setRenderingHints(renderHints);
	}

	public boolean getTestActive() {
		return testActive;
	}

	public boolean getAcceptingActions() {
		return acceptingActions;
	}

	public void setPlayBeep(boolean b) {
		playBeep = b;
		fireTrialModeChanged();
	}

	public boolean getPlayBeep() {
		return playBeep;
	}

	public void setUseSensorInput(boolean b) {
		useSensorInput = b;
		fireTrialModeChanged();
	}

	public boolean getUseSensorInput() {
		return useSensorInput;
	}

	public void setUseNoInput(boolean b) {
		useNoInput = b;
		fireTrialModeChanged();
	}

	public boolean getUseNoInput() {
		return useNoInput;
	}

	public void setRecordBeepTime(boolean b) {
		requestBeepTime = b;
		fireTrialModeChanged();
	}

	public boolean getRecordBeepTime() {
		return requestBeepTime;
	}

	public String getMessageToUser() {
		return messageToUser;
	}

	public void setMessageToUser(String msg) {
		messageToUser = msg;
	}

	public boolean isUserMessageOn() {
		return userMessageOn;
	}

	public void setUserMessageOn(boolean msgOn) {
		userMessageOn = msgOn;
	}

	public int getArmWidth() {
		return armWidth;
	}

	public void setArmWidth(int width) {
		this.armWidth = width;
	}

	public int getArmGap() {
		return armGap;
	}

	public void setArmGap(int armGap) {
		this.armGap = armGap;
	}

	public void setClockImage(int i) {

		String imagePath = null;
		String imageFilePath0 = "file:resources//gui//Libet300_90";
		String imageFilePath1 = "file:resources//gui//Libet300_100";
		String imageFilePath2 = "file:resources//gui//Libet300_120";
		String imageFilePath3 = "file:resources//gui//Libet300_140";
		String imageFilePath4 = "file:resources//gui//Libet300_160";

		if (i == 0) {
			imagePath = imageFilePath0;
			radius = 45;
			circleDiameter = 6;
		} else if (i == 1) {
			imagePath = imageFilePath1;
			radius = 50;
			circleDiameter = 6;
		} else if (i == 2) {
			imagePath = imageFilePath2;
			radius = 60;
			circleDiameter = 8;
		} else if (i == 3) {
			imagePath = imageFilePath3;
			radius = 70;
			circleDiameter = 8;
		} else if (i == 4) {
			imagePath = imageFilePath4;
			radius = 80;
			circleDiameter = 8;
		} else {
			imagePath = imageFilePath0;
			radius = 45;
			circleDiameter = 6;
		}

		if (useTicks) {
			imagePath = imagePath + "_t.png";
		} else {
			imagePath = imagePath + ".png";
		}

		drawClock = true;

		//URL url = this.getClass().getResource(imagePath);
		try {
			imagePath = new URL(imagePath).getPath();
		} catch (MalformedURLException e) {
			System.err.print("Image path error");
		}
		image = Toolkit.getDefaultToolkit().getImage(imagePath);

		MediaTracker mt = new MediaTracker(this);
		mt.addImage(image, 0);
		try {
			mt.waitForID(0);
		} catch (InterruptedException ie) {
		}
		clockImage = i;
		repaint();
	}

	public int getClockImage() {
		return clockImage;
	}

	// By default Libet clock uses a rotating circle. Send true to use clock arm
	// instead.
	public void setClockArm(boolean b) {
		clockArm = b;
	}

	public boolean getClockArm() {
		return clockArm;
	}

	// By default Libet clock uses a rotating circle. Send true to use clock arm
	// instead.
	public void setUseTicks(boolean b) {
		useTicks = b;
		setClockImage(clockImage);
	}

	public boolean getUseTicks() {
		return useTicks;
	}

	public void setDrawTraces(boolean b) {
		drawTraces = b;
	}

	public boolean getDrawTraces() {
		return drawTraces;
	}

	// By default Libet clock has a period of 2520ms. Set a new value in ms.
	public void setRotationTime(double rotTime) {
		rotationTime = rotTime;
		updateDrawingInfo();
	}

	// By default Libet clock has a period of 2520ms. Set a new value in ms.
	public double getRotationTime() {
		return rotationTime;
	}

	public void setBeepInterval(int beepInterval) {
		this.beepInterval = beepInterval;
	}

	public int getBeepInterval() {
		return beepInterval;
	}

	private void updateDrawingInfo() {
		degreesPerMilliSec = 360 / rotationTime;
	}

	private void updateLibetData() {

		double startDegreesScaled = (startDegrees + 90) % 360;
		double startTimeScaled = startDegreesScaled / 6;
		double startTimeNanoScaled = startDegreesScaled * (rotationTime / 360);

		double actionDegreesScaled = (actionDegrees + 90) % 360;
		double actionTimeScaled = actionDegreesScaled / 6;
		double actionTimeNanoScaled = actionDegreesScaled * (rotationTime / 360);

		double actionDegreesWithSubtractorScaled = (actionDegreesWithSubtractor + 90) % 360;
		double actionTimeWithSubtractorScaled = actionDegreesWithSubtractorScaled / 6;
		double actionTimeWithSubtractorNanoScaled = actionDegreesWithSubtractorScaled * (rotationTime / 360);

		double beepDegreesScaled = (beepDegrees + 90) % 360;
		double beepTimeScaled = beepDegreesScaled / 6;
		double beepTimeNanoScaled = beepDegreesScaled * (rotationTime / 360);

		String startDetails = "Start time = " + df.format(startTimeScaled) + ", milli time = "
				+ df.format(startTimeNanoScaled);
		String actionDetails = "Action time = " + df.format(actionTimeScaled) + ", milli time = "
				+ df.format(actionTimeNanoScaled);
		// String actionWithSubtractorDetails = "Action with subtractor: time = " +
		// df.format(actionTimeWithSubtractorScaled) + ", milli time = " +
		// df.format(actionTimeWithSubtractorNanoScaled);
		String beepDetails = "Beep time = " + df.format(beepTimeScaled) + ", milli time = "
				+ df.format(beepTimeNanoScaled);

		String calMeasure = "System clock interval measure (Beep trigger time - Action/Start time) = ";
		if (useNoInput) {
			calMeasure += (beepTime - startTime) / nanoToMilli + "ms";
		} else {
			calMeasure += (beepTime - actionTime) / nanoToMilli + "ms";
		}
		
		String calMeasureActual = "System clock interval measure (Actual beep time - Action/Start time) = ";
		if (useNoInput) {
			calMeasureActual += (actualBeepTime - startTime) / nanoToMilli + "ms";
		} else {
			calMeasureActual += (actualBeepTime - actionTime) / nanoToMilli + "ms";
		}

		libetReadings = calMeasure + "\n" + calMeasureActual + "\n" + startDetails + "\n" + actionDetails + ";  " + beepDetails;
		libetData = "";

		if (useNoInput) {
			libetData += "action = none";
			libetData += "";
		} else {
			if (!useSensorInput) {
				libetData += "Button Input Actual = " + df.format(actionTimeNanoScaled) + "ms ("
						+ df.format(actionTimeScaled) + ")";
			} else {
				libetData += "Sensor Input Actual = " + df.format(actionTimeWithSubtractorNanoScaled) + "ms ("
						+ df.format(actionTimeWithSubtractorScaled) + ")";
			}
		}

		if (playBeep) {
			if (useNoInput) {
				libetData += " Beep Actual = " + df.format(beepTimeNanoScaled) + "ms (" + df.format(beepTimeScaled)
						+ ")";
			} else {
				libetData += "\t>> Beep Actual = " + df.format(beepTimeNanoScaled) + "ms (" + df.format(beepTimeScaled)
						+ ")";
			}
		}

		if (getRecordBeepTime()) {
			computerTime = beepTimeNanoScaled;
		} else {
			if (!useSensorInput) {
				computerTime = actionTimeNanoScaled;
			} else {
				computerTime = actionTimeWithSubtractorNanoScaled;
			}
		}
	}

	public String getLibetReadings(String additionalString) {
		libetReadings = libetReadings + additionalString;
		return libetReadings;
	}

	public String getLibetData() {
		return libetData;
	}

	public double getComputerTime() {
		return computerTime;
	}

	public Dimension getPreferredSize() {
		return new Dimension(300, 300);
	}

	private void resetDateDisplay() {
		libetReadings = "";
	}

	public void start() {

		if (testActive)
			return;
		actionTrace = null;
		actionTraceWithSubtraction = null;
		beepTrace = null;
		startDegrees = generator.nextInt(360);
		degreesCurrent = startDegrees;

		randomPostBeepDelay = generator.nextInt(1500) + 1000; // Set a random delay time after beep;
		randomNoActionDelay = generator.nextInt(2500) + 2500; // Set a random delay time after beep;

		testActive = true;
		actionTriggered = false;
		beepTriggered = false;
		beepIntervalSubtractor = 0;

		paintUpdateCounter = 0;
		timeSinceActionMS = 0;
		timeSinceBeepMS = 0;

		resetDateDisplay();
		startTime = System.nanoTime();
		computerTime = 0;

		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();

		fireLibetTestStarted();
	}

	public void action() {
		beepIntervalSubtractor = 0;
		triggerAction();
	}

	public void actionWithSubtractor(int subtractor) {
		beepIntervalSubtractor = subtractor;
		triggerAction();
	}

	private void triggerAction() {
		// System.out.println("Received action in LibetPanel with subtractor = " +
		// beepIntervalSubtractor);
		if (!disableActions & testActive) {
			actionTime = System.nanoTime();
			actionTimeLocal = Double.parseDouble(df_localTime.format(new Date())); // ssSSS (second + millisecond)
			actionDegrees = degreesCurrent;
			actionDegreesWithSubtractor = actionDegrees - (beepIntervalSubtractor * degreesPerMilliSec);
			actionTriggered = true;
			disableActions = true;
			fireLibetActionReceived();
		}
	}

	public boolean getDisableActionsStatus() {
		return disableActions;
	}

	private void triggerBeep() {
		System.out.println("Beep trigger time: " + df_localTime_test.format(new Date()));
		if (playBeep) {
			beepPlayer.start();
		}
		
		beepTime = System.nanoTime();
		beepDegrees = degreesCurrent;
		
		if (!useNoInput) {
			beepTriggerInterval = beepTime - actionTime;
			long beepTriggerIntervalMS = beepTriggerInterval / nanoToMilli;
			System.out.println("Time to trigger beep = " + beepTriggerIntervalMS + "ms (" + beepTriggerInterval + "ns)");
		}
		
		beepTriggered = true;
	}

	public synchronized void stop() {
		thread = null;
		testActive = false;
		acceptingActions = false;
		disableActions = false;
		beepTriggered = false;
		beepPlayer.stop();    // Stop clip(beepPlayer)
		getActualBeepTime();  // Get actual beep time
		updateLibetData();
		drawClock = true;
		repaint();
		fireLibetTestFinished();
	}
	
	private void getActualBeepTime() {
		long actualBeepInterval;
		actualBeepTime = beepPlayer.getBeepTime();
		if (!useNoInput) {
			actualBeepInterval = actualBeepTime - actionTime;
			long actualBeepIntervalMS = actualBeepInterval / nanoToMilli;
			System.out.println("Actual beep interval = " + actualBeepIntervalMS + "ms (" + beepTriggerInterval + "ns)");
		}
	}

	@SuppressWarnings("static-access")
	public void run() {
		Thread me = Thread.currentThread();
		while (thread == me) {
			long currentTime = System.nanoTime();
			long elapsedTimeMS = (currentTime - startTime) / nanoToMilli;
			timeSinceActionMS = (currentTime - actionTime) / nanoToMilli;
			timeSinceBeepMS = (currentTime - beepTime) / nanoToMilli;
			try {
				if (elapsedTimeMS > blockActionTime && !acceptingActions) {
					acceptingActions = true;
					fireLibetAcceptingActions();
				}
				if (actionTriggered && !beepTriggered) {
					// set -2 to remove beep interval bias (Original value: -1)
					if ((beepInterval - beepIntervalSubtractor - 2) < timeSinceActionMS) {
						triggerBeep();
					}
				} else if (useNoInput && !beepTriggered && elapsedTimeMS > randomNoActionDelay) {
					triggerBeep();
				} else if (beepTriggered) {
					if (randomPostBeepDelay < timeSinceBeepMS)
						stop();
				}
				// Calculate position of clock arm.
				degreesCurrent = startDegrees + (elapsedTimeMS) * degreesPerMilliSec;
				paintUpdateCounter++;
				if (paintUpdateCounter == paintUpdateRate) {
					radiansCurrent = Math.toRadians(degreesCurrent);
					int clearL = 150 - radius + 2;
					int positionL = (radius - 2) * 2;
					clearCirc = new Ellipse2D.Double(clearL, clearL, positionL, positionL);
					centreCirc = new Ellipse2D.Double(148, 148, 4, 4);

					if (clockArm) {
						double xValue = 150.0 + ((radius - armGap - 2) * (Math.cos(radiansCurrent)));
						double yValue = 150.0 + ((radius - armGap - 2) * (Math.sin(radiansCurrent)));
						line = new Line2D.Double(150, 150, xValue, yValue);
					} else {
						int rad = circleDiameter / 2;
						double xValue = 150.0 - rad + ((radius - armGap - 4) * (Math.cos(radiansCurrent)));
						double yValue = 150.0 - rad + ((radius - armGap - 4) * (Math.sin(radiansCurrent)));
						ellipse = new Ellipse2D.Double(xValue, yValue, circleDiameter, circleDiameter);
					}

					if (drawTraces) {
						double startRadians = Math.toRadians(startDegrees);
						double xValue1 = 150.0 + ((radius - armGap - 2) * (Math.cos(startRadians)));
						double yValue1 = 150.0 + ((radius - armGap - 2) * (Math.sin(startRadians)));
						startTrace = new Line2D.Double(150, 150, xValue1, yValue1);
						if (actionTriggered) {
							double actionRadians = Math.toRadians(actionDegrees);
							double xValue = 150.0 + ((radius - armGap - 2) * (Math.cos(actionRadians)));
							double yValue = 150.0 + ((radius - armGap - 2) * (Math.sin(actionRadians)));
							actionTrace = new Line2D.Double(150, 150, xValue, yValue);

							double actionRadians1 = Math.toRadians(actionDegreesWithSubtractor);
							double xV = 150.0 + ((radius - armGap - 2) * (Math.cos(actionRadians1)));
							double yV = 150.0 + ((radius - armGap - 2) * (Math.sin(actionRadians1)));
							actionTraceWithSubtraction = new Line2D.Double(150, 150, xV, yV);
						}
						if (beepTriggered) {
							double beepRadians = Math.toRadians(beepDegrees);
							double xValue = 150.0 + ((radius - armGap - 2) * (Math.cos(beepRadians)));
							double yValue = 150.0 + ((radius - armGap - 2) * (Math.sin(beepRadians)));
							beepTrace = new Line2D.Double(150, 150, xValue, yValue);
						}
					}
					repaint();
					paintUpdateCounter = 0;
				}
				// Put the thread to sleep to a time (sleep)
				thread.sleep(threadUpdateTime);
			} catch (InterruptedException e) {
				break;
			}
		}
		thread = null;
	}

	public void paintComponent(Graphics g) {
		if (userMessageOn) {
			dbImageGraphics.setColor(Color.RED);
			dbImageGraphics.drawString(messageToUser, 50, 50);
		} else {
			if (drawClock) {
				dbImageGraphics.drawImage(image, 0, 0, this);
				drawClock = false;
			}
			if (testActive) {
				if (armGap >= 2) {
					dbImageGraphics.setColor(Color.WHITE);
					dbImageGraphics.fill(clearCirc);
					dbImageGraphics.setColor(Color.BLACK);
					dbImageGraphics.fill(centreCirc);
				} else {
					dbImageGraphics.drawImage(image, 0, 0, this);
				}
				if (clockArm) {
					dbImageGraphics.setColor(Color.BLACK);
					dbImageGraphics.setStroke(new BasicStroke(armWidth)); // Set thickness of line.
					dbImageGraphics.draw(line);
				} else {
					dbImageGraphics.setStroke(new BasicStroke(0)); // Set thickness of line.
					dbImageGraphics.setColor(Color.RED);
					dbImageGraphics.draw(ellipse);
					dbImageGraphics.fill(ellipse);
				}
				if (!acceptingActions && drawActionBlocker) {
					dbImageGraphics.setColor(Color.MAGENTA);
					dbImageGraphics.fill(new Ellipse2D.Double(146, 146, 8, 8));
					dbImageGraphics.draw(new Line2D.Double(130, 130, 170, 170));
					dbImageGraphics.draw(new Line2D.Double(170, 130, 130, 170));
					dbImageGraphics.draw(new Line2D.Double(130, 130, 130, 170));
					dbImageGraphics.draw(new Line2D.Double(170, 130, 170, 170));
					dbImageGraphics.draw(new Line2D.Double(130, 130, 170, 170));
					dbImageGraphics.draw(new Line2D.Double(130, 170, 170, 170));
				}
				if (drawTraces) {
					if (startTrace != null) {
						dbImageGraphics.setColor(Color.BLUE);
						dbImageGraphics.setStroke(new BasicStroke(armWidth)); // Set thickness of line.
						dbImageGraphics.draw(startTrace);
					}
					if (actionTrace != null) {
						dbImageGraphics.setColor(Color.RED);
						dbImageGraphics.setStroke(new BasicStroke(armWidth)); // Set thickness of line.
						dbImageGraphics.draw(actionTrace);
					}
					if (actionTraceWithSubtraction != null && beepIntervalSubtractor != 0) {
						dbImageGraphics.setColor(Color.MAGENTA);
						dbImageGraphics.setStroke(new BasicStroke(armWidth)); // Set thickness of line.
						dbImageGraphics.draw(actionTraceWithSubtraction);
					}
					if (beepTrace != null) {
						dbImageGraphics.setColor(Color.GREEN);
						dbImageGraphics.setStroke(new BasicStroke(armWidth)); // Set thickness of line.
						dbImageGraphics.draw(beepTrace);
					}
				}
			}
		}
		g.drawImage(dbImage, 0, 0, this);

	}// end paintComponent

	// Add and remove TrialListener.
	public void addTrialListener(TrialListener l) {
		trialListener.add(l);
	}

	public void removeTrialListener(TrialListener l) {
		trialListener.remove(l);
	}

	public double getStartTimeMicro() {
		return startTime / 1000; // Nano to Micro
	}

	public double getActionTimeMicro() {
		return actionTime / 1000; // Nano to Micro
	}

	public double getBeepTriggerTimeMicro() {
		return beepTime / 1000; // Nano to Micro
	}
	
	public double getActualBeepTimeMicro() {
		return actualBeepTime / 1000; // Nano to Micro
	}

	public double getActionTimeLocal() {
		return actionTimeLocal; // ssSSS Second+Millisecond
	}

	// Inform listeners that test has started.
	private void fireLibetTestStarted() {
		Iterator<TrialListener> iterator;
		iterator = new HashSet<TrialListener>(trialListener).iterator();

		while (iterator.hasNext()) {
			TrialListener libetListener = iterator.next();
			libetListener.libetTestStarted();
		}
	}

	private void fireLibetAcceptingActions() {
		Iterator<TrialListener> iterator;
		iterator = new HashSet<TrialListener>(trialListener).iterator();

		while (iterator.hasNext()) {
			TrialListener libetListener = iterator.next();
			libetListener.libetAcceptingActions();
		}
	}

	// Inform listeners that action was received.
	private void fireLibetActionReceived() {
		Iterator<TrialListener> iterator;
		iterator = new HashSet<TrialListener>(trialListener).iterator();

		while (iterator.hasNext()) {
			TrialListener libetListener = iterator.next();
			libetListener.libetActionReceived();
		}
	}

	// Inform listeners that test is finished.
	private void fireLibetTestFinished() {
		Iterator<TrialListener> iterator;
		iterator = new HashSet<TrialListener>(trialListener).iterator();

		while (iterator.hasNext()) {
			TrialListener libetListener = iterator.next();
			libetListener.libetTestFinished();
		}
	}

	private void fireTrialModeChanged() {
		Iterator<TrialListener> iterator;
		iterator = new HashSet<TrialListener>(trialListener).iterator();

		while (iterator.hasNext()) {
			TrialListener libetListener = iterator.next();
			libetListener.libetTrialModeChanged();
		}

	}

}