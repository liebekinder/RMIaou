package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;


public class ClientGui extends JFrame {

	private static final long serialVersionUID = 6945386908303590271L;
	private static Logger logger = Logger.getLogger(LaunchClient.class);
	private int maxWidth = 400;
	private int maxHeight = 600;

	public ClientGui(String frameName) {
		super(frameName);
		
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
			c.fill= GridBagConstraints.HORIZONTAL;
			c.gridx = 0;

		ImageIcon img = new ImageIcon("ressources/chat.png");
		JLabel chat = new JLabel(img);
		JLabel ipdomain = new JLabel("server IP or domain name:");
		JTextField ipdomainValue = new JTextField();
		JLabel serverName = new JLabel("server name:");
		JTextField serverNameValue = new JTextField();
		JLabel port = new JLabel("server port:");
		JTextField portValue = new JTextField("10010");
		JButton connection = new JButton("connect");
		
		c.gridy=0;
		contentPane.add(chat,c);
		c.gridy=1;
		contentPane.add(ipdomain,c);
		c.gridy=2;
		contentPane.add(ipdomainValue,c);
		c.gridy=3;
		contentPane.add(serverName,c);
		c.gridy=4;
		contentPane.add(serverNameValue,c);
		c.gridy=5;
		contentPane.add(port,c);
		c.gridy=6;
		contentPane.add(portValue,c);
		c.gridy=7;
		contentPane.add(connection,c);
		c.gridy=8;
		
		
		
		this.setContentPane(contentPane);
		this.setSize(maxWidth, maxHeight);
		this.setResizable(false);
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
	
	public JList<String> listView(){
		JList<String> list = new JList<String>();
		return list;
	}

}
