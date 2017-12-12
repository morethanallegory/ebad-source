package gui;

import java.awt.Image;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import network.IO;
import application.FileAndPathNames;

 class MessageDialogue extends JOptionPane {

	public static void main(String... args) {
		String testMessage = "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH + "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH + "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH + "Please view search results in file "
				+ FileAndPathNames.RESULTS_PAGE_PATH + "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH + "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH;

		new MessageDialogue(testMessage, MessageDialogue.ERROR_MESSAGE, false);
		new MessageDialogue(testMessage, MessageDialogue.INFORMATION_MESSAGE, true);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MessageDialogue(Object message, int optionType, boolean systemExitAfter) {

		Icon logo = null;
		try {
			Image ebad1_gif = IO.loadImage("ebad1.gif");
			logo = new ImageIcon(ebad1_gif);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String label = "<html><body><p style='width: 250px;'>" + message + "</body></html>";

		//showMessageDialog(new JFrame(), label, "eBad message", optionType, logo);

		JOptionPane.showConfirmDialog(null, label, "eBad message", JOptionPane.PLAIN_MESSAGE, optionType, logo);
		//optionType, logo, null, null);
		if (systemExitAfter) {
			System.exit(0);
		}

	}

}
