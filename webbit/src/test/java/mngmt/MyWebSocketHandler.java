package mngmt;

import org.apache.log4j.Logger;
import org.webbitserver.WebSocketConnection;

public class MyWebSocketHandler implements MessageHandler {
	private static Logger logger = Logger.getLogger(MyWebSocketHandler.class);

	private int connectionCount;

	public void onOpen(WebSocketConnection connection) {
		try {
			connection.send("Hello! There are " + connectionCount
					+ " other connections active");
			connectionCount++;
		} catch (Throwable e) {

		}
	}

	public void onClose(WebSocketConnection connection) {
		connectionCount--;
	}

	public void onMessage(WebSocketConnection connection, String message) {
		// echo back message in uppercase
		logger.info("MSG = '" + message + "'");
		connection.send(message.toUpperCase());
	}

	public void onMessage(WebSocketConnection connection, byte[] msg) {
		// TODO Auto-generated method stub

	}

	public void onPing(WebSocketConnection connection, byte[] msg) {
		// TODO Auto-generated method stub

	}

	public void onPong(WebSocketConnection connection, byte[] msg) {
		// TODO Auto-generated method stub

	}

}
