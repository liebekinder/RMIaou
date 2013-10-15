package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.BindException;
import java.rmi.RemoteException;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ClientPrincipaleGUI extends JFrame {

    private static final long serialVersionUID = 3572024391545427383L;

    private int maxWidth = 400;

    private int maxHeight = 600;

    private JTextField domaineNameZone;

    private JPanel principal;

    private DefaultListModel<String> listModel;

    private JPanel principalChatRoom;

    private Client2 client;

    private JTextField chatRoomName;

//    private JPanel principalChatRoomGui;

//    private ConstrainedTextField chatRoomInput;

    private JList<String> list;

//    private ClientChatRoomGUI chatRoomGui;

    public ClientPrincipaleGUI() {
        super();

        initializeConnectionView();
        initializeChatRoomSelection();
//        initializeChatRoomGui();

        this.add(principal);

        this.setSize(maxWidth, maxHeight);
        this.setMaximumSize(new Dimension(maxWidth, maxHeight));

        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void initializeConnectionView() {
        principal = new JPanel();

        JLabel imageLabel = new JLabel();
        JLabel domainNameLabel = new JLabel(
                "<html>Please type the server's name (In this demo version,<br/> ip is localhost and port 10010)</html>");

        domaineNameZone = new ConstrainedTextField();
        // Dimension maxSize = domaineNameZone.getMaximumSize();
        // maxSize.height = domaineNameZone.getMinimumSize().height;
        // domaineNameZone.setMaximumSize(maxSize);
        domaineNameZone.setText("MyFirstServer");

        JButton connectButton = new JButton("connect to server");

        connectButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                connect();
            }
        });

        principal.setLayout(new BoxLayout(principal, BoxLayout.PAGE_AXIS));
        principal.add(imageLabel);
        principal.add(domainNameLabel);
        principal.add(domaineNameZone);
        principal.add(connectButton);

    }

    private void initializeChatRoomSelection() {
        principalChatRoom = new JPanel();
        principalChatRoom.setLayout(new BoxLayout(principalChatRoom,
                BoxLayout.PAGE_AXIS));

        listModel = new DefaultListModel<String>();
        list = new JList<String>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int index = -1;
                @SuppressWarnings("unchecked")
                JList<String> list = (JList<String>) evt.getSource();
                if (evt.getClickCount() == 1) {
                    index = list.locationToIndex(evt.getPoint());
                    if (index > -1) {
                        chatRoomName.setText((String) listModel.get(index));
                    }
                } else if (evt.getClickCount() >= 2) {
                    index = list.locationToIndex(evt.getPoint());
                    if (index > -1) {
                        connectToChatRoom((String) listModel.get(index));
                    }
                }

            }
        });
        list.setVisibleRowCount(20);
        JScrollPane listScrollPane = new JScrollPane(list);

        chatRoomName = new ConstrainedTextField();
        
        JButton goButton = new JButton("Go !");
        
        goButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent arg0) {
                boolean alreadyExists = false;

                String chatRoomString = chatRoomName.getText();
                for(int i = 0;i< listModel.getSize();++i) {
                    if(chatRoomString.equals((String) listModel.get(i))) alreadyExists = true;
                }

                if(!alreadyExists) {
                    try {
                        System.out.println(client.getServer().createChatRoom(chatRoomString, ClientConfig.pseudo));;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
                connectToChatRoom(chatRoomName.getText());
            }
        });
        
        JButton deleteButton = new JButton("Delete ChatRoom !");
        
        deleteButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                try {
                    System.out.println(client.getServer().deleteChatRoom(chatRoomName.getText(), ClientConfig.pseudo));;
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
            }
        });
        
        principalChatRoom.add(listScrollPane);
        principalChatRoom
                .add(new JLabel(
                        "<html>Select or type the name of an existing chatroom<br/> or enter a new one to create it.</html>"));
        principalChatRoom.add(chatRoomName);
        principalChatRoom.add(goButton);
        principalChatRoom.add(deleteButton);
    }

    /**
     * Connect the client to a selected chatroom
     * 
     * @param source
     */
    protected void connectToChatRoom(String chatRoomName) {
        
        if(client.getConnectedChatRoomList().contains(chatRoomName)) {
            System.out.println("User "+ClientConfig.pseudo+" already connected to this chatRoom !");
            return;
        }
        
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

    /**
     * Connect this client to a server.
     */
    protected void connect() {
        int port = ClientConfig.minPort;
        boolean portFound = false;
        client = null;
        while (!portFound && port <= ClientConfig.maxPort) {
            try {
                client = new Client2(port);
                portFound = true;
//                client.setOutputGui(chatRoomGui);
            } catch (RemoteException e) {
                if (e.getCause() instanceof BindException) {
                    System.out.println("ERR: port " + port
                            + " already bound. Trying another port.");
                    port++;
                } else {
                    System.out
                            .println("ERR: Fatal error. Cannot bind client. Aborting.");
                    e.printStackTrace();
                    System.exit(1);
                }

            } catch (Exception e) {
                System.out
                        .println("ERR: Fatal error. Cannot bind client. Aborting.");
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (client == null) {
            System.err
                    .println("ERR: Failed to create client instance. Aborting.");
            System.exit(1);
        }

       
        try {
            client.connectToServer("localhost", domaineNameZone.getText(),
                    10010);

        } catch (Exception e) {
            System.out
                    .println("ERR: Fatal error when connecting to server. Aborting.");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Succefully connected to server.");
        
        
        changePanel(principalChatRoom);

        Set<String> chatRoomList;
        try {
            chatRoomList = client.getServer().getChatRoomsList();
            for (String chatRoomName : chatRoomList) {
                listModel.addElement("["+client.getServer().getClients(chatRoomName)+"]"+chatRoomName);
            }
        } catch (RemoteException e) {
            System.out.println("ERR: Cannot fetch chatroom list. Aborting.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void changePanel(final JPanel panel) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                getContentPane().removeAll();
                getContentPane().add(panel, BorderLayout.CENTER);
                getContentPane().doLayout();
                getContentPane().validate();
                update(getGraphics());
            }
        });

    }

    private class ConstrainedTextField extends JTextField {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        public ConstrainedTextField() {
            Dimension maxSize = this.getMaximumSize();
            maxSize.height = this.getMinimumSize().height;
            this.setMaximumSize(maxSize);
        }
    }

}
