package mngmt;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.webbitserver.WebSocketConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

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

	enum ParamType {
		DateOnly, DateFollowedByIntegerArray, DateFollowedByString
	}

	/**
	 * @param connection
	 * @param params
	 */
	private void getData(WebSocketConnection connection, Collection<?> params) {
		// com.google.gson.internal.LinkedTreeMap
		// class LinkedTreeMap<K, V> extends java.util.AbstractMap<K, V>
		// implements Serializable
		// SimpleDate params1 = json.fromJson(params.toString(),
		// SimpleDate.class);

		System.out.println("\n\n" + params);
		// System.out.println(params1);
		Gson gson = new Gson();
		// @SuppressWarnings("rawtypes")
		// Collection collection = new ArrayList();
		String json = gson.toJson(params);

		System.out.println("Using gson.toJson() on a raw collection: " + json);
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(json).getAsJsonArray();
		ParamType paramType = gson.fromJson(array.get(0), ParamType.class);
		SimpleDate simpleDate = null;

		// Teste de serialização
		// Collection<Integer> ints = new ArrayList<Integer>();
		// ints.add(1);
		// ints.add(3);
		// ints.add(2);
		// System.out.println("ints json = " + gson.toJson(ints));

		switch (paramType) {
		// { action: "GET_DATA", params: [ "DateOnly", { year: 2013, month:8,
		// day:21 } ]
		// }
		case DateOnly:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			System.out.printf("Using gson.fromJson() to get: %s", simpleDate);
			break;
		// { action: "GET_DATA", params: [ "DateFollowedByIntegerArray", { year:
		// 2013, month:8, day:21 }, [ 1, 3, 2 ] ] }
		case DateFollowedByIntegerArray:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			@SuppressWarnings("unchecked")
			Collection<Integer> inteiros = gson.fromJson(array.get(2),
					Collection.class);
			System.out.printf("Using gson.fromJson() to get: %s",
					inteiros.toString());
			break;
		// { action: "GET_DATA", params: [ "DateFollowedByString", { year: 2013,
		// month:8, day:21 }, "Uma String qualquer pode vir aqui !" ] }
		case DateFollowedByString:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			String stringQualquer = gson.fromJson(array.get(2), String.class);
			System.out.printf("Using gson.fromJson() to get: %s",
					stringQualquer);
			break;
		default:
			throw new NotImplementedException("paramType = " + paramType);
		}
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
			// 1. Incoming =>
			// '{ action: "GET_DATA", params: { year: 2013, month:8, day:21 } }'
			// retornará por exemplo:
			// {"action":"RETURN","code":0, message:"OK", data: ['x','y', ...]}
			getData(connection, incoming.params);
			break;
		// PUT_DATA params
		case PUT_DATA:
			// putData(connection, incoming.params);
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

class SimpleDate {
	public Integer year, month, day;

	@Override
	public String toString() {
		return "SimpleDate [year=" + year + ", month=" + month + ", day=" + day
				+ "]";
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
	// Coleção de alguma tralha qualquer (objetos arbitrários do JavaScript)
	Collection<?> params;
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
