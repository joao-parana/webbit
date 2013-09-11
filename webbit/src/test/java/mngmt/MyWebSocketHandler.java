package mngmt;

// import javax.xml.bind.JAXBContext;
// import javax.xml.bind.Marshaller;
// import javax.xml.bind.Unmarshaller;
// import javax.xml.transform.stream.StreamSource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.webbitserver.WebSocketConnection;

import service.application.ApplicationService;
import service.diag.ResidualLifeService;
import service.historical.HistoricalService;
import service.historical.HistoricalServiceFacade;
import service.measurement.DataSourceTypeService;
import service.measurement.MeasurementUnitService;
import service.plant.BehaviorService;
import service.plant.DataSourceService;
import service.plant.PlantService;
import service.user.RoleService;
import util.PersistenceUtil;
import br.cepel.asset.PersistenceUnitName;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

public class MyWebSocketHandler implements MessageHandler {
	private static Logger logger = Logger.getLogger(MyWebSocketHandler.class);

	private final Gson json = new Gson();

	private int connectionCount;

	private String persistenceUnit = null;

	private MeasurementUnitService mUnitService;
	private DataSourceTypeService dsTypeService;
	private PlantService plantService;
	private ResidualLifeService lifeMetaService;
	private DataSourceService dataSourceService;
	private HistoricalService historicalMetaService;
	private BehaviorService behaviorService;
	private RoleService roleService;
	private ApplicationService applicationService;

	private HistoricalServiceFacade historicalServiceFacade;

	public MyWebSocketHandler() {
		super();
		// historicalServiceFacade = new
		// HistoricalServiceFacade(persistenceUnit);
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
		DateOnly, DateFollowedByIntegerArray, DateFollowedByStringArray, DateFollowedByString
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
		// posso criar simplesmente assim new Gson(); ou ...
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
		// Neste Caso de Uso é informado uma data e uma lista de ids de fontes
		// de dado e recuperamos um Historico de medidas de 24 horas retornando
		// numa tabela bidimensional serializada em JSON (cuidado adicional deve
		// ser tomado com o ponto decimal que universalmente é o ponto "." e não
		// a vírgula como no Brasil)
		case DateFollowedByIntegerArray:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			@SuppressWarnings("unchecked")
			Type collectionType = new TypeToken<List<Long>>() {
			}.getType();
			List<Long> inteiros = gson.fromJson(array.get(2), collectionType);
			for (Long long1 : inteiros) {
				logger.info(long1 + " " + long1.getClass().getCanonicalName());
			}
			logger.info("Using gson.fromJson() to get: " + inteiros.toString());
			String ret = getHistoricalAvgByDateAndDataSourceList(simpleDate,
					inteiros);
			logger.info(ret);
			break;
		// { action: "GET_DATA", params: [ "DateFollowedByStringArray",
		// { year: 2013, month:8, day:21 },
		// [ 'velocidade2d', 'direcao2d', 'azVelocidade3d' ] ] }
		// Neste Caso de Uso é informado uma data e uma lista de nomes de fontes
		// de dado e recuperamos uma lista de IDs de fontes de dado para usar na
		// obtenção dos registros de Historico de medidas de 24 horas numa
		// chamada posterior
		// { action: "GET_DATA", params: [ "DateFollowedByStringArray", { year:
		// 2013, month:9, day:9 }, [ 'velocidade2d', 'direcao2d',
		// 'azVelocidade3d' ] ] }
		case DateFollowedByStringArray:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			@SuppressWarnings("unchecked")
			Collection<String> names = gson.fromJson(array.get(2),
					Collection.class);
			logger.info("Using gson.fromJson() to get: " + names.toString());
			Collection<Long> ids = getDataSourceIds(names);
			logger.info("lista de IDs das Fontes de Dado " + gson.toJson(ids));
			break;
		// { action: "GET_DATA", params: [ "DateFollowedByString", { year: 2013,
		// month:8, day:21 }, "Uma String qualquer pode vir aqui !" ] }
		case DateFollowedByString:
			simpleDate = gson.fromJson(array.get(1), SimpleDate.class);
			String stringQualquer = gson.fromJson(array.get(2), String.class);
			logger.info("Using gson.fromJson() to get: " + stringQualquer);
			break;
		default:
			throw new NotImplementedException("paramType = " + paramType);
		}
	}

	private Collection<Long> getDataSourceIds(Collection<String> dataSourceNames) {
		return historicalServiceFacade.getDataSourceIds(dataSourceNames);
	}

	private String getHistoricalAvgByDateAndDataSourceList(
			SimpleDate simpleDate, List<? extends Number> dataSourceIdList) {
		@SuppressWarnings("unchecked")
		Collection<Collection<Double>> tudinho = historicalServiceFacade
				.getHistoricalAvgByDateAndDataSourceList(simpleDate.year,
						simpleDate.month, simpleDate.day,
						(List<Long>) dataSourceIdList);
		logger.info(tudinho);
		// posso criar simplesmente assim new Gson(); ou ...
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		Collection<Collection<Double>> tudo = new ArrayList<Collection<Double>>();
		Collection<Double> linha = new ArrayList<Double>();
		String json = gson.toJson(tudinho);
		return json;

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

	private DBMS init() throws Exception {
		Map<String, String> additionalParams = new HashMap<String, String>();
		// ECLIPSELINK_PERSISTENCE_XML
		// additionalParams.put(
		// PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML,
		// "teste-persistence.xml");
		// WARNING é um nivel bom para produção
		additionalParams
				.put(PersistenceUnitProperties.LOGGING_LEVEL, "WARNING");
		// "javax.persistence.jdbc.driver";
		additionalParams.put(PersistenceUnitProperties.JDBC_DRIVER,
				"com.mysql.jdbc.Driver");
		// JDBC_URL = "javax.persistence.jdbc.url";
		additionalParams.put(PersistenceUnitProperties.JDBC_URL,
				"jdbc:mysql://localhost:3306/soma");
		// JDBC_USER = "javax.persistence.jdbc.user";
		additionalParams.put(PersistenceUnitProperties.JDBC_USER, "teste");
		// JDBC_PASSWORD
		additionalParams.put(PersistenceUnitProperties.JDBC_PASSWORD, "teste");
		// DDL_GENERATION : CREATE_ONLY ou DROP_AND_CREATE
		additionalParams.put(PersistenceUnitProperties.JDBC_PASSWORD, "teste");
		// DDL_GENERATION : CREATE_ONLY ou DROP_AND_CREATE
		additionalParams.put(PersistenceUnitProperties.DDL_GENERATION,
				PersistenceUnitProperties.CREATE_ONLY);
		// PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML_DEFAULT);

		// equivalente a PersistenceUtil.ECLIPSELINK_ORM_ENGINE
		persistenceUnit = PersistenceUtil.setupPersistence(
				PersistenceUnitName.SOMA_PERSISTENCE,
				PersistenceUtil.DEFAULT_ORM_ENGINE, additionalParams);

		mUnitService = new MeasurementUnitService(persistenceUnit);
		dsTypeService = new DataSourceTypeService(persistenceUnit);
		plantService = new PlantService(persistenceUnit);
		lifeMetaService = new ResidualLifeService(persistenceUnit);
		dataSourceService = new DataSourceService(persistenceUnit);
		historicalMetaService = new HistoricalService(persistenceUnit);
		behaviorService = new BehaviorService(persistenceUnit);
		roleService = new RoleService(persistenceUnit);
		applicationService = new ApplicationService(persistenceUnit);

		historicalServiceFacade = new HistoricalServiceFacade(persistenceUnit);
		if (additionalParams.get(PersistenceUnitProperties.JDBC_URL)
				.startsWith("XXXXdbc:mysql")) {
			String cmd = "ALTER TABLE META_DADOS DROP FOREIGN KEY FK_META_DADOS_TIPO";
			execSql(cmd);
			cmd = "ALTER TABLE VIDA_RESIDUAL_META DROP FOREIGN KEY FK_VIDA_RESIDUAL_META_CODIGO";
			execSql(cmd);
			cmd = "ALTER TABLE COMPORTAMENTO_META DROP FOREIGN KEY FK_COMPORTAMENTO_META_COD_FONTE_META";
			execSql(cmd);
			cmd = "ALTER TABLE COMPONENTE DROP FOREIGN KEY FK_COMPONENTE_ITEM_CODIGO";
			execSql(cmd);
			cmd = "ALTER TABLE FONTE_DADO DROP FOREIGN KEY FK_FONTE_DADO_CODIGO";
			execSql(cmd);
			cmd = "ALTER TABLE HISTORICO_META DROP FOREIGN KEY FK_HISTORICO_META_CODIGO";
			execSql(cmd);
			// TODO: desfazer isso ao termino da inserção de registros de
			// histórico (duas constraints)
			cmd = "ALTER TABLE HISTORICO_SERIES DROP FOREIGN KEY FK_HISTORICO_SERIES_COD_HIST_META";
			execSql(cmd);
			cmd = "ALTER TABLE HISTORICO_MEDIDA DROP FOREIGN KEY FK_HISTORICO_MEDIDA_COD_HIST_SERIES";
			execSql(cmd);
		}
		if (additionalParams.get(PersistenceUnitProperties.JDBC_URL)
				.startsWith("jdbc:mysql")) {
			return DBMS.MySQL;
		} else {
			return DBMS.Oracle;
		}

	}

	private void execSql(String cmd) {
		try {
			logger.info("Executando SQL : " + cmd);
			this.plantService.executeSql(cmd);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(cmd + " : " + e.getLocalizedMessage());
		}

	}
}

enum DBMS {
	MySQL, Oracle, PostgreSql
}

class SimpleDate {
	public Integer year, month, day;

	public SimpleDate(Integer year, Integer month, Integer day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}

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
