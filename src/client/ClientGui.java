package client;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;


public class ClientGui extends JFrame {

	private static final long serialVersionUID = 6945386908303590271L;
	private static Logger logger = Logger.getLogger(LaunchClient.class);
	private int maxWidth = 400;
	private int maxHeight = 600;

	public ClientGui(String frameName) {
		super(frameName);
		

		JPanel contentPane = new JPanel();

		this.setContentPane(contentPane);

		this.setSize(maxWidth, maxHeight);
		this.setMaximumSize(new Dimension(maxWidth, maxHeight));

		WindowListener l = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				logger.info("exit");
				System.exit(0);
			}
		};

		addWindowListener(l);

		this.setVisible(true);
	}

}
