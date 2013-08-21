package mngmt;

import org.webbitserver.WebSocket;
import org.webbitserver.WebSocketConnection;

public interface MessageHandler {
	void onOpen(WebSocketConnection connection);

	/**
	 * Called when a connection is closed.
	 * 
	 * @param connection
	 *            the connection that was closed. Beware that the connection
	 *            will be null if this handler is used in a {@link WebSocket}
	 *            that fails to connect.
	 * @throws Exception
	 */
	void onClose(WebSocketConnection connection);

	void onMessage(WebSocketConnection connection, String msg);

	void onMessage(WebSocketConnection connection, byte[] msg);

	void onPing(WebSocketConnection connection, byte[] msg);

	void onPong(WebSocketConnection connection, byte[] msg);
}
