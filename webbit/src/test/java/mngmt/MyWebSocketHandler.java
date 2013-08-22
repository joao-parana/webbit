package mngmt;

import org.apache.log4j.Logger;
import org.webbitserver.WebSocketConnection;

import com.google.gson.Gson;

public class MyWebSocketHandler implements MessageHandler {
	private static Logger logger = Logger.getLogger(MyWebSocketHandler.class);

	private final Gson json = new Gson();

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

	private void login(WebSocketConnection connection, String username,
			String timestamp, String hash) {
		throw new NotImplementedException();
	}

	private void getData(WebSocketConnection connection, String params) {
		throw new NotImplementedException();
	}

	private void putData(WebSocketConnection connection, String params) {
		throw new NotImplementedException();
	}

	public void onMessage(WebSocketConnection connection, String message) {
		// echo back message in uppercase
		logger.info("MSG = '" + message + "'");
		connection.send(message.toUpperCase());
		Incoming incoming = json.fromJson(message, Incoming.class);
		switch (incoming.action) {
		// LOGIN username, timestamp, hash
		case LOGIN:
			login(connection, incoming.username, incoming.timestamp,
					incoming.hash);
			break;
		// GET_DATA params
		case GET_DATA:
			// exemplos
			// 1. Incoming => '{ action: "GET_DATA", value:2 }'
			// retornará por exemplo:
			// {"action":"RETURN","code":0, message:"OK", data: ['x','y', ...]}
			getData(connection, incoming.params);
			break;
		// PUT_DATA params
		case PUT_DATA:
			putData(connection, incoming.params);
			break;
		}
	}

	public void onMessage(WebSocketConnection connection, byte[] msg) {
		onMessage(connection, msg.toString());
	}

	public void onPing(WebSocketConnection connection, byte[] msg) {
		throw new NotImplementedException();
	}

	public void onPong(WebSocketConnection connection, byte[] msg) {
		throw new NotImplementedException();
	}
}

class Incoming {
	enum Action {
		LOGIN, GET_DATA, PUT_DATA
	}

	// LOGIN username, timestamp, hash
	// GET_DATA params
	// PUT_DATA params
	Action action;
	String username;
	String timestamp;
	String hash;
	String params;
}

class Outgoing {
	enum Action {
		RETURN, ERROR, LOG
	}

	// RETURN code, message, data
	// ERROR code, message
	// LOG message
	Action action;
	Integer code;
	String message;
	byte[] data;
}
