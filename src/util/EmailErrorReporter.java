package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import network.GmailSender;
import application.Application;

public class EmailErrorReporter {

	private static final List<Error> errors = new ArrayList<Error>();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		while (EmailErrorReporter.errors.size() < 5) {
			Error error = new EmailErrorReporter().new Error("Message is that you messed up", new Exception("A strange error has occured"), EmailErrorReporter.class.getName(), "Main");
			EmailErrorReporter.errors.add(error);
		}

		EmailErrorReporter.sendErrorEmail();
		//System.out.println(reporter.formatErrorMessages());
	}

	private EmailErrorReporter() {

	}

	public static boolean hasErrors() {
		return !errors.isEmpty();
	}

	public static void addError(Exception e, String message, String classLocation, String methodLocation) {
		errors.add(new EmailErrorReporter().new Error(message, e, classLocation, methodLocation));
	}

	public static void sendErrorEmail() {
		if (!hasErrors())
			return;
		try {
			String[] recipients = Application.getEmailRecipients();
			String subject = Application.getEmailSubject();
			String body = formatErrorMessages();
			String username = Application.getEmailUsername();
			String password = Application.getEmailPassword();
			String from = Application.getEmailFrom();

			GmailSender.send(subject, body, null, username, password, from, recipients);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String formatErrorMessages() {
		StringBuilder sb = new StringBuilder();
		for (Error e : errors) {
			sb.append(e.getMessage());
			sb.append("\r\n");
			sb.append(e.getClassLocation());
			sb.append("\r\n");
			sb.append(e.getMethodLocation());
			sb.append("\r\n");
			sb.append(e.getException().getClass().getName());
			sb.append("\r\n");
			sb.append(e.getException().getMessage());
			sb.append("\r\n");
			if (e.getException().getCause() != null) {
				sb.append(e.getException().getCause().toString());
				sb.append("\r\n");
			}
			sb.append("\r\n");
			sb.append("\r\n");
		}
		sb.append("System properties");
		sb.append("\r\n");
		Set<Map.Entry<Object, Object>> entries = System.getProperties().entrySet();
		for (Map.Entry<Object, Object> entry : entries) {
			sb.append(entry.getKey().toString());
			sb.append(" ");
			sb.append(entry.getValue().toString());
			sb.append("\r\n");
		}
		return sb.toString();
	}

	private final class Error {
		public String getMessage() {
			return message;
		}


		public Exception getException() {
			return exception;
		}


		public String getClassLocation() {
			return classLocation;
		}


		public String getMethodLocation() {
			return methodLocation;
		}


		public Error(String message, Exception exception, String classLocation, String methodLocation) {
			super();
			this.message = message;
			this.exception = exception;
			this.classLocation = classLocation;
			this.methodLocation = methodLocation;
		}

		private String message;
		private Exception exception;
		private String classLocation;
		private String methodLocation;

	}

}
