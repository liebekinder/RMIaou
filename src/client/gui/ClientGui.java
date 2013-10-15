package client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

import client.ChatRoomWrapper;
import client.Client;
import client.ClientConfig;
import client.LaunchClient;

public class ClientGui extends JFrame implements ActionListener {

	private static final long serialVersionUID = 6945386908303590271L;

	private static Logger logger = Logger.getLogger(LaunchClient.class);

	private int maxWidth = 400;

	private int maxHeight = 650;

	private Client client;

	private JTextField ipdomainValue;

	private JTextField portValue;

	private JTextField serverNameValue;

	private JButton addChatRoom;

	private DefaultListModel<String> listModel;

	private JButton delChatRoom;

	private JButton coChatRoom;

	private JList<String> list;

	private JTextField pseudolabValue;

	private JButton connection;

	public ClientGui(String frameName) {
		super(frameName);

		JMenuBar menuBar = getSpecificMenuBar();
		JPanel contentPane = getClientJPanel();

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

		this.setJMenuBar(menuBar);
		this.setVisible(true);
	}

	private JMenuBar getSpecificMenuBar() {

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem menuItemHelp = new JMenuItem("help");
		menuItemHelp.setEnabled(false);
		menuItemHelp.addActionListener(this);
		menu.addSeparator();
		JMenuItem menuItemQuit = new JMenuItem("quit");
		menuItemQuit.addActionListener(this);
		menu.add(menuItemHelp);
		menu.add(menuItemQuit);
		menuBar.add(menu);
		return menuBar;
	}

	public JScrollPane listView() {

		listModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				int index = -1;
				@SuppressWarnings("unchecked")
				JList<String> list = (JList<String>) evt.getSource();
				if (evt.getClickCount() >= 2) {
					index = list.locationToIndex(evt.getPoint());
					if (index > -1) {
						connectToChatRoom((String) listModel.get(index));
					}
				}
			}
		});

		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setSize(new Dimension(100, 80));
		return listScroller;
	}

	public JPanel getClientJPanel() {
		JPanel contentPane = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.anchor = GridBagConstraints.NORTH;

		ImageIcon img = new ImageIcon("ressources/chat.png");

		JLabel chat = new JLabel(img);

		JLabel pseudolab = new JLabel("your pseudo:");
		pseudolabValue = new JTextField(ClientConfig.pseudo);
		pseudolabValue.setMinimumSize(new Dimension(250, 20));
		pseudolabValue.setHorizontalAlignment(SwingConstants.CENTER);
		pseudolabValue.setCaretPosition(pseudolabValue.getText().length());
		
		JLabel ipdomain = new JLabel("server IP or domain name:");
		ipdomainValue = new JTextField("localhost");
		ipdomainValue.setMinimumSize(new Dimension(250, 20));
		ipdomainValue.setHorizontalAlignment(SwingConstants.CENTER);
		ipdomainValue.setCaretPosition(ipdomainValue.getText().length());

		JLabel serverName = new JLabel("server name:");
		serverNameValue = new JTextField("MyFirstServer");
		serverNameValue.setMinimumSize(new Dimension(250, 20));
		serverNameValue.setHorizontalAlignment(SwingConstants.CENTER);
		serverNameValue.setCaretPosition(serverNameValue.getText().length());

		JLabel port = new JLabel("server port:");
		portValue = new JTextField("10010");
		portValue.setMinimumSize(new Dimension(250, 20));
		portValue.setHorizontalAlignment(SwingConstants.CENTER);
		portValue.setCaretPosition(portValue.getText().length());

		connection = new JButton("connect");
		connection.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				connect();

			}
		});

		JScrollPane listView = listView();
		listView.setMinimumSize(new Dimension(390, 150));

		addChatRoom = new JButton("add a chatRoom");
		addChatRoom.setEnabled(false);
		addChatRoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				addChatRoom();

			}
		});

		delChatRoom = new JButton("delete a chatRoom");
		delChatRoom.setEnabled(false);
		delChatRoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				try {
					int index = list.getSelectedIndex();
					if (index > -1) {
						logger.info((client.getServer().deleteChatRoom(
							listModel.get(index), ClientConfig.pseudo)));
						actualiseListe();
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}

			}
		});

		coChatRoom = new JButton("connect to chatRoom");
		coChatRoom.setEnabled(false);
		coChatRoom.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				int index = list.getSelectedIndex();
				if (index > -1) {
					connectToChatRoom((String) listModel.get(index));
				}
			}
		});

		c.gridy = 0;
		c.weighty = 1;
		contentPane.add(chat, c);
		c.gridy++;
		c.weighty = 0;
		contentPane.add(pseudolab, c);
		c.gridy++;
		contentPane.add(pseudolabValue, c);
		c.gridy++;
		contentPane.add(ipdomain, c);
		c.gridy++;
		contentPane.add(ipdomainValue, c);
		c.gridy++;
		contentPane.add(serverName, c);
		c.gridy++;
		contentPane.add(serverNameValue, c);
		c.gridy++;
		contentPane.add(port, c);
		c.gridy++;
		contentPane.add(portValue, c);
		c.gridy++;
		c.weighty = 1;
		contentPane.add(connection, c);
		c.gridy++;
		contentPane.add(listView, c);
		c.weighty = 0;
		c.gridy++;
		contentPane.add(addChatRoom, c);
		c.gridy++;
		contentPane.add(delChatRoom, c);
		c.gridy++;
		contentPane.add(coChatRoom, c);

		this.getRootPane().setDefaultButton(connection);
		connection.requestFocus();

		return contentPane;
	}

	protected void addChatRoom() {
		String chatRoomString = JForm.openForm(this);
		logger.debug(chatRoomString);
		if (chatRoomString != null && !chatRoomString.isEmpty()) {

			boolean alreadyExists = false;

			for (int i = 0; i < listModel.getSize(); ++i) {
				if (chatRoomString.equals((String) listModel.get(i)))
					alreadyExists = true;
			}

			if (!alreadyExists) {
				try {
					logger.info(client.getServer().createChatRoom(
							chatRoomString, ClientConfig.pseudo));
					actualiseListe();

				} catch (RemoteException e) {
					logger.error(e.getLocalizedMessage());
					System.exit(1);
				}
			}

			connectToChatRoom(chatRoomString);
		}
		else {
			logger.info("Chatroom name was empty !");
		}
	}

	public void actionPerformed(ActionEvent e) {
		logger.debug(e.getActionCommand());
		if (e.getActionCommand() == "quit") {
			this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		if (e.getActionCommand() == "help") {
			logger.info("this functionnality haven't been implemented yet!");
		}

	}

	/**
	 * Connect this client to a server.
	 */
	protected int connect() {
		ClientConfig.pseudo = pseudolabValue.getText();
		int port = ClientConfig.minPort;
		boolean portFound = false;
		client = null;
		while (!portFound && port <= ClientConfig.maxPort) {
			try {
				client = new Client(port, this);
				portFound = true;
			} catch (RemoteException e) {
				if (e.getCause() instanceof BindException) {
					logger.error("ERR: port " + port
							+ " already bound. Trying another port.");
					port++;
				} else {
					logger.error("ERR: Fatal error. Cannot bind client. Aborting.");
					e.printStackTrace();
					return -1;
				}

			} catch (Exception e) {
				logger.error("ERR: Fatal error. Cannot bind client. Aborting.");
				return -1;
			}
		}

		if (client == null) {
			logger.error("ERR: Failed to create client instance. Aborting.");
			return -1;
		}

		try {
			int portGui = 10010;
			try {
				portGui = Integer.valueOf(portValue.getText());
			} catch (NumberFormatException e) {
				logger.error(e.getLocalizedMessage());
				return -1;
			}
			client.connectToServer(ipdomainValue.getText(),
					serverNameValue.getText(), portGui);

		} catch (Exception e) {
			logger.error("ERR: Fatal error when connecting to server. Aborting.");
			return -1;
		}
		logger.info("Succefully connected to server.");
		
		pseudolabValue.setEnabled(false);
		ipdomainValue.setEnabled(false);
		portValue.setEnabled(false);
		serverNameValue.setEnabled(false);
		connection.setEnabled(false);

		addChatRoom.setEnabled(true);
		delChatRoom.setEnabled(true);
		coChatRoom.setEnabled(true);
		
		synchro();

		// MAJ GUI
		actualiseListe();

		return 0;
	}

	private void synchro() {
		try {
			client.connectToChatRoomNoGui("ServerMessages");
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			logger.error("Synchronisation channel opening failed !");
		}
	}

	private void actualiseListe() {
		Set<String> chatRoomList;
		try {
			listModel.clear();
			chatRoomList = client.getServer().getChatRoomsList();
			for (String chatRoomName : chatRoomList) {
				listModel.addElement(chatRoomName);
			}
		} catch (RemoteException e) {
			logger.error("ERR: Cannot fetch chatroom list. Aborting.");
			System.exit(1);
		}

	}

	protected void connectToChatRoom(String chatRoomName) {

		if (client.getConnectedChatRoomList().contains(chatRoomName)) {
			logger.info("User " + ClientConfig.pseudo
					+ " already connected to this chatRoom !");
			return;
		}

		ChatRoomWrapper chatRoom = null;
		try {
			chatRoom = client.connectToChatRoom(chatRoomName);
		} catch (Exception e) {
			System.out.println("ERR: Cannot connect to chatroom. Aborting.");
			return;
		}

		chatRoom.sendRaw(ClientConfig.pseudo + " join the chatroom.");
	}

	public void actualise() {
		actualiseListe();
	}

}
