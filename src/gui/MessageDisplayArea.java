package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import bus.CommsBusListener;

 class MessageDisplayArea extends JTextArea  implements CommsBusListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3967784754867501285L;

	public MessageDisplayArea() {
		initialize();
	}

	public MessageDisplayArea(String text) {
		super(text);
		initialize();
	}
	
	private void initialize(){
		setEditable(false);
	    setLineWrap(true);
	    setWrapStyleWord(true);
	    JLabel lb = new JLabel();
	    Font f = lb.getFont();
	    setFont(f.deriveFont(f.getSize2D() * 0.9f));
	    setBorder(lb.getBorder());
	    setBackground(new Color(lb.getBackground().getRGB(), true));
	    setForeground(new Color(lb.getForeground().getRGB(), true));
	}

	

	@Override
	public void messageSent(String message) {
		setText(message);
	}

}
