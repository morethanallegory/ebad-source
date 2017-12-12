package network;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

/**
 * Simple Class to send an email using JavaMail API (javax.mail) and Gmail SMTP
 * server
 * 
 * @author Dunith Dhanushka, dunithd@gmail.com
 * @version 1.0
 */
public class GmailSender {

	private static String HOST = "smtp.gmail.com";
	private static String PORT = "465";

	private static String STARTTLS = "true";
	private static String AUTH = "true";
	private static String DEBUG = "true";
	private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";

	public static synchronized void send(String subject, String text, String attachFileName, String username, String password, String from, String[] recipients) {
		// Use Properties object to set environment properties
		Properties props = new Properties();

		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		props.put("mail.smtp.user", username);

		props.put("mail.smtp.auth", AUTH);
		props.put("mail.smtp.starttls.enable", STARTTLS);
		props.put("mail.smtp.debug", DEBUG);

		props.put("mail.smtp.socketFactory.port", PORT);
		props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");

		try {

			// Obtain the default mail session
			Session session = Session.getDefaultInstance(props, null);
			session.setDebug(true);

			// Construct the mail message
			MimeMessage message = new MimeMessage(session);
			message.setSubject(subject);
			message.setFrom(new InternetAddress(from));
			for (String to : recipients) {
				message.addRecipient(RecipientType.TO, new InternetAddress(to));

			}
			MimeBodyPart body = new MimeBodyPart();
			body.setText(text);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(body);

			if (attachFileName != null) {
				MimeBodyPart attachMent = new MimeBodyPart();
				FileDataSource dataSource = new FileDataSource(new File(attachFileName));
				attachMent.setDataHandler(new DataHandler(dataSource));
				attachMent.setFileName(attachFileName);
				attachMent.setDisposition(MimeBodyPart.ATTACHMENT);
				multipart.addBodyPart(attachMent);
			}

			
			message.setContent(multipart);

			// Use Transport to deliver the message
			Transport transport = session.getTransport("smtp");
			transport.connect(HOST, username, password);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
