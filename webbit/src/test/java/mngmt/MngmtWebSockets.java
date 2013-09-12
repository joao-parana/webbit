package mngmt;

import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;
import org.webbitserver.handler.StaticFileHandler;

import br.cepel.common.Configuration;

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
		/*
		 * transpose(new double[][] { { 1, 2, 3, 4, 5, 6, 7, 8 }, { 9, 10, 11,
		 * 12, 13, 14 }, { 15, 16, 17, 18, 19, 20 }, { 21, 22, 23, 24, 25, 26,
		 * 27, 28, 29 } });
		 */
		// Mes começa de Zero. Ano e Dia começa de UM
		// Exemplo Zero Hora GMT do dia 10/09/2013
		GregorianCalendar g = new GregorianCalendar(2013, 9 - 1, 10);
		g.setTimeZone(TimeZone.getTimeZone("GMT"));
		logger.info("GregorianCalendar milis = " + g.getTimeInMillis());
		logger.info("System milis            = " + System.currentTimeMillis());

		Configuration.initializeForJUnitTest(".");
		logger.info("Time to load " + MngmtWebSockets.doIt(args) + " milis.");
	}

	private static String getCurrentDir() {
		String currDir = "";
		currDir = (new File(".")).getAbsolutePath();
		return currDir;
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
