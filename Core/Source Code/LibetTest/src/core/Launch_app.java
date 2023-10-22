package core;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Launch_app {

	public static void main(String[] args) {
		{
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				System.err.println("Couldn't use system look and feel.");
			}

			final JFrame experimentInfo = new JFrame();

			Trial_Model application = new Trial_Model(experimentInfo);
			application.setResizable(true);
			application.setMinimumSize(new Dimension(1920, 1080));
			application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

			application.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent arg0) {
					experimentInfo.dispose();
					System.exit(0);
				}
				public void windowClosed(WindowEvent arg0) {
				}
			});
			// Center the window
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			Dimension frameSize = application.getSize();
			if (frameSize.height > screenSize.height) {
				frameSize.height = screenSize.height;
			}
			if (frameSize.width > screenSize.width) {
				frameSize.width = screenSize.width;
			}
			application.setSize(1920, 1280);
			application.setLocation((screenSize.width - frameSize.width) / 2, 0);
			application.setExtendedState(JFrame.MAXIMIZED_BOTH);
			application.setVisible(true);
			experimentInfo.setVisible(true);
			Toolkit.getDefaultToolkit().beep();
		}
	}
}