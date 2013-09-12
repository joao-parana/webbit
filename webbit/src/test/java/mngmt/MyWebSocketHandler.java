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

	public static double[][] transposeMatrix(double[][] m) {
		double[][] temp = new double[m[0].length][m.length];
		for (int i = 0; i < m.length; i++)
			for (int j = 0; j < m[0].length; j++)
				temp[j][i] = m[i][j];
		return temp;
	}

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
			logger.debug("Using gson.fromJson() to get: " + inteiros.toString());
			String ret = getHistoricalAvgByDateAndDataSourceList(simpleDate,
					inteiros);
			logger.info("-------------------------------------"
					+ "-------------------------------------\n" + ret);
			connection.send(ret);
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
		HistoricalMeasurements historicalMeasurements = new HistoricalMeasurements();
		try {
			historicalMeasurements.historical = historicalServiceFacade
					.getHistoricalAvgByDateAndDataSourceList(simpleDate.year,
							simpleDate.month, simpleDate.day,
							(List<Long>) dataSourceIdList);
			logger.info(historicalMeasurements.historical);
			if (historicalMeasurements.historical.size() == 0) {
				String msg = "Não foi encontrado nenhum registro de histórico para as fontes de dado "
						+ dataSourceIdList;
				historicalMeasurements.code = 2001;
				historicalMeasurements.message = msg;
				logger.warn(msg);
			} else {
				if (historicalMeasurements.historical.size() != (dataSourceIdList
						.size() + 1)) {
					String msg = "Número de vetores de dados deveria ser "
							+ (dataSourceIdList.size()) + " mas veio "
							+ (historicalMeasurements.historical.size() - 1)
							+ ". dataSourceIdList = " + dataSourceIdList;
					historicalMeasurements.code = 2002;
					historicalMeasurements.message = msg;
					logger.warn(msg);
				}
			}
		} catch (Exception e) {
			historicalMeasurements.code = 1000;
			historicalMeasurements.message = e.getClass().getCanonicalName()
					+ " - " + e.getLocalizedMessage();
		}

		// posso criar simplesmente assim new Gson(); ou ...
		Gson gson = new GsonBuilder().setPrettyPrinting()
				.serializeSpecialFloatingPointValues().create();

		// Collection<Collection<Double>> tudo = new
		// ArrayList<Collection<Double>>();
		// Collection<Double> linha = new ArrayList<Double>();
		HistoricalMeasurements hm = historicalMeasurements.clone();
		DataTable dataTable = new DataTable();
		dataTable.aaData = hm.historical;
		dataTable.aoColumns.add(new DataTableColumnHeader("velocidade2d"));
		dataTable.aoColumns.add(new DataTableColumnHeader("direcao2d"));
		dataTable.aoColumns.add(new DataTableColumnHeader("azVelocidade3d"));
		historicalMeasurements.dataTable = dataTable;
		String json = gson.toJson(historicalMeasurements);
		return json;
	}

	private void putData(WebSocketConnection connection, String params) {
		throw new NotImplementedException();
	}

	public void onMessage(WebSocketConnection connection, String message) {
		// echo back message in uppercase

		Incoming incoming = json.fromJson(message, Incoming.class);
		String msg = "";
		switch (incoming.action) {
		// LOGIN username, timestamp, hash
		case LOGIN:
			msg = "o LOGIN";
			connection.send("{ \"code\": " + 9005 + ", \"message\": \""
					+ "Aguardando até um máximo de 5 segundos para processar "
					+ msg + "\" }");
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
			msg = "o GET_DATA";
			connection.send("{ \"code\": " + 9025 + ", \"message\": \""
					+ "Aguardando até 25 segundos para processar " + msg
					+ "\" }");
			getData(connection, incoming.params);
			break;
		// PUT_DATA params
		case PUT_DATA:
			msg = "o GET_DATA";
			connection.send("{ \"code\": " + 9025 + ", \"message\": \""
					+ "Aguardando até 25 segundos para processar " + msg
					+ "\" }");
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

// Códigos de Erro / Retorno:
// 0 - Sem Erro e a mensagem vai vazia
// de 1000 à 1999 - Erro grave
// de 2000 à 2999 - Warnings
// de 9000 à 9999 - Não é erro e indica que o processo foi iniciado no Servidor
// e a resposta sairá em breve em outra mensagem. A diferença entre 9999 e 9000
// dá aquantidade máxima de tempo (em segundos) a ser esperada pelo outra
// resposta e 999 significa infinito.
// Nas situações de erro a propriedade historical pode não fazer sentido e não
// deve ser usada
class HistoricalMeasurements {
	public Integer code;
	public String message;
	public Collection<Collection<Double>> historical;
	// usado apenas para exibir no Browser para teste
	public DataTable dataTable;

	public HistoricalMeasurements() {
		this.code = 0;
		this.message = "";
		this.historical = new ArrayList<Collection<Double>>();
		this.dataTable = new DataTable();
	}

	@Override
	public HistoricalMeasurements clone() {
		HistoricalMeasurements hm = new HistoricalMeasurements();
		for (Collection<Double> doubleCol : this.historical) {
			hm.historical.add(doubleCol);
		}
		return hm;
	}
}

// http://datatables.net/examples/data_sources/js_array.html
class DataTable {
	/* { aaData : [[... ], ...], aoColumns : [ { sTitle : "Título" }, ... ] */
	public Collection<Collection<Double>> aaData;
	public Collection<DataTableColumnHeader> aoColumns = new ArrayList<DataTableColumnHeader>();
}

// https://datatables.net/usage/options
class SortOption extends ArrayList<Object> {
	public SortOption(Integer column, String value) {
		add(column);
		add(value);
	}
}

class SortOptionArray extends ArrayList<SortOption> {

}

class DataTableOptions {
	public boolean bPaginate = false;
	public boolean bLengthChange = false;
	public boolean bFilter = true;
	public boolean bSort = false;
	public boolean bInfo = false;
	public boolean bAutoWidth = false;
	public SortOptionArray aaSorting = new SortOptionArray();

	public void addSortOption(Integer column, String value) {
		aaSorting.add(new SortOption(column, value));
	}
}

class DataTableColumnHeader {
	public String sTitle; // título da coluna
	public String sClass = "center";// nome da classe CSS
	public String fnRender; // nome da função JavaScript

	public DataTableColumnHeader(String sTitle) {
		this.sTitle = sTitle;
		this.sClass = null;
		this.fnRender = null;
	}

	public DataTableColumnHeader(String sTitle, String sClass) {
		this.sTitle = sTitle;
		this.sClass = sClass;
		this.fnRender = null;
	}

	public DataTableColumnHeader(String sTitle, String sClass, String fnRender) {
		this.sTitle = sTitle;
		this.sClass = sClass;
		this.fnRender = fnRender;
	}
}
