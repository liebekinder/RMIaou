package client;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * Generate a new GUI for the console.
 * Used to print text with @see {@link ClientChatRoomGUI#addText(String, Font, Color)}
 * 
 * @author Ornicare
 *
 */
public class ClientChatRoomGUI extends JFrame {
	
	/**
	 * Number maximum of total characters
	 */
	private int maxChar = 300*300;
	
	/**
     * Console's text font. 
     * Default to "Courrier", 12 pts
     * 
     * @see ClientChatRoomGUI#setDefaultFont()
     * @see ClientChatRoomGUI#getDefaultFont(Font)
     */
	private Font defaultFont = new Font("Courrier",Font.PLAIN, 12);
	
	/**
     * Console's text color. 
     * Defaukt to white
     * 
     * @see ClientChatRoomGUI#setDefaultColor()
     * @see ClientChatRoomGUI#getDefaultColor(Color)
     */
	private Color defaultColor = Color.WHITE;
	
	/**
     * Console's text background. 
     * Default to black
     * 
     * @see ClientChatRoomGUI#getDefaultColorBackground()
     * @see ClientChatRoomGUI#setDefaultColorBackground(Color)
     */
	private Color defaultColorBackground = Color.BLACK;
	
	/**
     * Content of the @see {@link ClientChatRoomGUI#textPane}
     * 
     * @see ClientChatRoomGUI#getDefaultColorBackground()
     * @see ClientChatRoomGUI#setDefaultColorBackground(Color)
     */
	private StyledDocument doc;
	
	private JTextPane textPane = new JTextPane();
	
	private static final long serialVersionUID = 1L;
	
	public Font getDefaultFont() {
		return defaultFont;
	}


	public void setDefaultFont(Font defaultFont) {
		this.defaultFont = defaultFont;
	}

	public Color getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(Color defaultColor) {
		this.defaultColor = defaultColor;
	}

	public Color getDefaultColorBackground() {
		return defaultColorBackground;
	}

	public void setDefaultColorBackground(Color defaultColorBackground) {
		this.defaultColorBackground = defaultColorBackground;
	}

	
	/**
     * Constructor
     * <p>
     * Create a new GUI for the Console with defaults font, display text's color, and background.
     * </p>
     */
	public ClientChatRoomGUI() {
		
		this.setTitle("Console");
		this.setSize(500, 500);
		this.setLocationRelativeTo(null);
		
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
			textPane.setEditable(false);
			textPane.setBackground(defaultColorBackground);
			textPane.setForeground(defaultColor);
		//Ajout d'un scrollPane pour avoir des scrollBars
		JScrollPane scrollP = new JScrollPane(textPane);
		scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.setContentPane(scrollP);
			
		DefaultCaret caret = (DefaultCaret)textPane.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//Contenu du textPane
		doc = textPane.getStyledDocument();

		this.setVisible(true);	
	}

	/**
	 * <code>font</code> defaults to {@link defaultFont}.
	 *
	 * @see ClientChatRoomGUI#addText(String ,Font, Color)
	 */
	public void addText(String text) {
		addText(text, defaultFont , defaultColor);
	}
	
	/**
	 * <code>color</code> defaults to {@link defaultColor}.
	 *
	 * @see ClientChatRoomGUI#addText(String ,Font, Color)
	 */
	public void addText(String text, Color color) {
		addText(text, defaultFont, color);
	}
	
	/**
	 * <code>font</code> defaults to {@link defaultFont}.
	 * <code>color</code> defaults to {@link defaultColor}.
	 *
	 * @see ClientChatRoomGUI#addText(String ,Font, Color)
	 */
	public void addText(String text, Font font) {
		addText(text, font, defaultColor);
	}

	/**
	 * Add text to the console
	 * 
	 * @param text text to put on the console
	 * @param font <code>text</code>'s font
	 * @param color <code>text</code>'s color
	 */
	public void addText(String text, Font font, Color color) {
		Style style = textPane.addStyle("I'm a Style", null);
        StyleConstants.setForeground(style, color);
        StyleConstants.setFontFamily(style, font.getFamily());
        StyleConstants.setFontSize(style, font.getSize());
        
        StyleConstants.setItalic(style, (font.getStyle() & Font.ITALIC) != 0);
        StyleConstants.setBold(style, (font.getStyle() & Font.BOLD) != 0);

        try {
        		doc.insertString(doc.getLength(), text,style); 
        		if(doc.getLength()>maxChar) {
        			doc.remove(0, doc.getLength()-maxChar);
        		}
        	}
        catch (BadLocationException e){}
        

	}

	public void setMaxChar(int maxChar) {
		this.maxChar = maxChar;
	}


	/**
	 * Equivalent of WindowConstants.DISPOSE_ON_CLOSE
	 * Hide and dispose the window.
	 * 
	 */
	public void close() {
		setVisible(false);
		dispose();
	}

}