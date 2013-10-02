package client;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ClientPrincipaleGUI extends JFrame{

	private static final long serialVersionUID = 3572024391545427383L;
	private int minWidth = 300; 
	private int minHeight = 600; 
	
	
	public ClientPrincipaleGUI(){
		super();
		JFrame principal = new JFrame();
		
		JLabel imageLabel = new JLabel();
		JLabel domainNameLabel = new JLabel("Please type the server's name or IP");
		JTextField domaineNameZone = new JTextField();
		JButton connectButton = new JButton("connect to server");
		
		BoxLayout mainLayout = new BoxLayout(principal, MAXIMIZED_VERT);
		principal.add(imageLabel);
		principal.add(domainNameLabel);
		principal.add(domaineNameZone);
		principal.add(connectButton);
		
		principal.setSize(minHeight,minHeight);
		principal.setVisible(true);		
	}

}
