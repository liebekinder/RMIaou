package client;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * Generate a new GUI for the console.
 * Used to print text with @see {@link ClientChatRoomGUI#addText(String, Font, Color)}
 * 
 * @author Ornicare
 *
 */
public class ClientChatRoomGUI extends JTextPane {
	
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

	private ClientChatRoomGUI textPane;
	
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
	 * @param client 
     */
	public ClientChatRoomGUI() {

		this.setEditable(false);
		this.setBackground(defaultColorBackground);
		this.setForeground(defaultColor);
		
		//wrap
		this.setEditorKit(new WrapEditorKit());
//		Ajout d'un scrollPane pour avoir des scrollBars
//		JScrollPane scrollP = new JScrollPane(this);
//		scrollP.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//		scrollP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//		this.setContentPane(scrollP);
			
		DefaultCaret caret = (DefaultCaret)this.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		
		//Contenu du textPane
		doc = this.getStyledDocument();
		
		this.textPane = this;

//		this.setVisible(true);	
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
		Style style = this.addStyle("I'm a Style", null);
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
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                int end = doc.getLength();
                try {
                	textPane.scrollRectToVisible(textPane.modelToView(end));
                } catch (BadLocationException e) {
                    throw new RuntimeException(e); // Should never get here.
                }
            }
        });
        

	}

	public void setMaxChar(int maxChar) {
		this.maxChar = maxChar;
	}
	
	private class WrapEditorKit extends StyledEditorKit {
	        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
			ViewFactory defaultFactory=new WrapColumnFactory();
	        public ViewFactory getViewFactory() {
	            return defaultFactory;
	        }

	    }

	 private class WrapColumnFactory implements ViewFactory {
	        public View create(Element elem) {
	            String kind = elem.getName();
	            if (kind != null) {
	                if (kind.equals(AbstractDocument.ContentElementName)) {
	                    return new WrapLabelView(elem);
	                } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
	                    return new ParagraphView(elem);
	                } else if (kind.equals(AbstractDocument.SectionElementName)) {
	                    return new BoxView(elem, View.Y_AXIS);
	                } else if (kind.equals(StyleConstants.ComponentElementName)) {
	                    return new ComponentView(elem);
	                } else if (kind.equals(StyleConstants.IconElementName)) {
	                    return new IconView(elem);
	                }
	            }

	            // default to text display
	            return new LabelView(elem);
	        }
	    }

	    private class WrapLabelView extends LabelView {
	        public WrapLabelView(Element elem) {
	            super(elem);
	        }

	        public float getMinimumSpan(int axis) {
	            switch (axis) {
	                case View.X_AXIS:
	                    return 0;
	                case View.Y_AXIS:
	                    return super.getMinimumSpan(axis);
	                default:
	                    throw new IllegalArgumentException("Invalid axis: " + axis);
	            }
	        }
	    }
	    
	    

}