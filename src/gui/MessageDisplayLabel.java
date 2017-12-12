package gui;

import javax.swing.JLabel;

import bus.CommsBusListener;

@SuppressWarnings("serial")
 class MessageDisplayLabel extends JLabel implements CommsBusListener {


	public MessageDisplayLabel() {
	}
	

	@Override
	public void messageSent(String message) {
		setText("<html>" + message + "</html>");
	}
}
