package client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatRoomGui extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private ConstrainedTextField chatRoomInput;
    private ClientChatRoomGUI chatRoomTextField;
    private ChatRoomWrapper chatRoom;



    public ChatRoomGui(ChatRoomWrapper chatRoomT) {
    	
        JPanel listPane = new JPanel();
//        listPane.setLayout();
        
        this.chatRoom = chatRoomT;
        
        listPane.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        chatRoomInput = new ConstrainedTextField();
        chatRoomInput.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                chatRoom.send(chatRoomInput.getText());
                chatRoomInput.setText("");

            }
        });
        chatRoomTextField = new ClientChatRoomGUI();
        

    	chatRoomT.setOutputGui(this);
        
        

        JScrollPane scrollP = new JScrollPane(chatRoomTextField);
        scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
        listPane.add(scrollP);
        listPane.add(chatRoomInput);
        
        this.addWindowListener(new WindowListener() {
            
            public void windowOpened(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void windowIconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void windowDeiconified(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void windowDeactivated(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void windowClosing(WindowEvent arg0) {
                chatRoom.deconnect();
            }
            
            public void windowClosed(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
            
            public void windowActivated(WindowEvent arg0) {
                // TODO Auto-generated method stub
                
            }
        });
        
        this.add(listPane);
        
        this.setSize(400,600);
        this.setVisible(true);

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

    public ClientChatRoomGUI getClientChatRoomGui() {
        return chatRoomTextField;
    }

	public void addText(String string, Color color) {
		chatRoomTextField.addText(string, color);
	}

	public void disableInput() {
		chatRoomInput.setEnabled(false);
	}

}
