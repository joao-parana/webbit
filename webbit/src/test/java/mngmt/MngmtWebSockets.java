package mngmt;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.handler.StaticFileHandler;

public class MngmtWebSockets extends BaseWebSocketHandler {
	private static Logger logger = Logger.getLogger(MngmtWebSockets.class);
	private MessageHandler webSocketHandler = null;

	MngmtWebSockets(MessageHandler webSocketHandler) {
		super();
		this.webSocketHandler = webSocketHandler;
	}

	public void onOpen(WebSocketConnection connection) {
		webSocketHandler.onOpen(connection);
	}

	public void onClose(WebSocketConnection connection) {
		webSocketHandler.onClose(connection);
	}

	public void onMessage(WebSocketConnection connection, String message) {
		webSocketHandler.onMessage(connection, message);
	}

	public static void main(String[] args) {
		// conf/log4j.properties
		String log4JFile = "conf/log4j.properties";

		System.out.println("***  Iniciando o Log4J para o SOMA ..."
				+ " log4JFile = '" + log4JFile + "' ***");

		if (log4JFile != null) {
			// Carrega configuracoes do arquivo passado para o Log4j a cada 60
			// segundos.
			File f = new File(".");
			System.out.println(f.getAbsolutePath());
			String fullPath = "";
			try {
				// System.out.println(f.getCanonicalPath());
				fullPath = f.getCanonicalPath()
						+ System.getProperty("file.separator") + log4JFile;
				System.out.println("## CONF do LOG em : " + fullPath);
			} catch (IOException e) {
				e.printStackTrace();
			}

			PropertyConfigurator.configureAndWatch(fullPath);
		}
		logger.info("Log4J inicializado.");

		logger.info("Time to load " + MngmtWebSockets.doIt(args) + " milis.");
	}

	public static long doIt(String[] args) {

		logger.info("*** doIt executando '"
				+ MngmtWebSockets.class.getSimpleName() + "'");
		long x = System.currentTimeMillis();
		WebServer webServer = WebServers.createWebServer(17883)
				.add("/wskt", new MngmtWebSockets(new MyWebSocketHandler()))
				.add(new StaticFileHandler("/mngmt/"));
		webServer.start();
		logger.info("Server version " + StaticFileHandler.version
				+ " running at " + webServer.getUri());
		return System.currentTimeMillis() - x;
	}
}
