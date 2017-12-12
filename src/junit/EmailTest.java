package junit;

import static org.junit.Assert.fail;
import network.GmailSender;

import org.junit.Test;

import application.Application;
import application.FileAndPathNames;


public class EmailTest {

	@Test
	public void testSend() {
		try {
			String[] recipients = Application.getEmailRecipients();
			String subject = Application.getEmailSubject();
			String body = Application.getEmailBody();
			String username = Application.getEmailUsername();
			String password = Application.getEmailPassword();
			String from = Application.getEmailFrom();
			String attachment = FileAndPathNames.COMPRESSED_RESULTS_PAGE_PATH.toString();
			System.out.println(subject);
			System.out.println(body);
			System.out.println(username);
			System.out.println(password);
			System.out.println(from);
			System.out.println(attachment);

			for (String recip : recipients) {
				System.out.println("Recipient: " + recip);
			}
			GmailSender.send(subject, body, attachment, username, password,
					from, recipients);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
