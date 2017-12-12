package bus;

import java.util.ArrayList;

public final class CommsBus {
	private static ArrayList<CommsBusListener> listeners = new ArrayList<CommsBusListener>();
	private static ArrayList<CommsBusListener> errorListeners = new ArrayList<CommsBusListener>();

	public static void addErrorMessageListener(CommsBusListener listener) {
		errorListeners.add(listener);
	}


	public static void addMessageListener(CommsBusListener listener) {
		listeners.add(listener);
	}

	public static void messageSent(Object message) {
		for (CommsBusListener l : listeners) {
			l.messageSent(message.toString());
		}
	}

	public static void errorMessageSent(Object message) {
		for (CommsBusListener l : errorListeners) {
			l.messageSent(message.toString());
		}
	}

}
