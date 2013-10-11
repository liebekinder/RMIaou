package client;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

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
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;

public class ClientGui extends JFrame implements ActionListener {

    private static final long serialVersionUID = 6945386908303590271L;

    private static Logger logger = Logger.getLogger(LaunchClient.class);

    private int maxWidth = 400;

    private int maxHeight = 600;

    private Client2 client;

    private JTextField ipdomainValue;

    private JTextField portValue;

    private JTextField serverNameValue;

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
        String[] tabString = {};
        JList<String> list = new JList<String>(tabString);
        list.setLayoutOrientation(JList.VERTICAL);

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

        JButton connection = new JButton("connect");
        connection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                connect();

            }
        });

        JScrollPane listView = listView();
        listView.setMinimumSize(new Dimension(390, 150));

        JButton addChatRoom = new JButton("add a chatRoom");
        addChatRoom.setEnabled(false);

        c.gridy = 0;
        c.weighty = 1;
        contentPane.add(chat, c);
        c.gridy++;
        c.weighty = 0;
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

        this.getRootPane().setDefaultButton(connection);
        connection.requestFocus();

        return contentPane;
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
        int port = ClientConfig.minPort;
        boolean portFound = false;
        client = null;
        while (!portFound && port <= ClientConfig.maxPort) {
            try {
                client = new Client2(port);
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

        // TODO => MAJ de l'interface

        return 0;
    }

    protected void connectToChatRoom(String chatRoomName) {
        //TODO ouvrir gui de chat
        // plus addMessageListener pour set la gui
        
//        MessageListener ml = null;
//        try {
//            ml = client.addMessageListener(chatRoomName);
//        } catch (Exception e) {
//            System.out.println("ERR: Failed to add listener to this chatRoom");
//            e.printStackTrace();
//            System.exit(1);
//        }
        
        ChatRoomWrapper chatRoom = null;
        try {
            chatRoom = client.connectToChatRoom(chatRoomName);
        } catch (Exception e) {
            System.out.println("ERR: Cannot connect to chatroom. Aborting.");
            e.printStackTrace();
            System.exit(1);
        }
        
//        new ChatRoomGui(chatRoom);
//        ml.setOutputGui(chatGui);
        
        
        
        chatRoom.sendRaw(ClientConfig.pseudo + " join the chatroom.");
    }

}
