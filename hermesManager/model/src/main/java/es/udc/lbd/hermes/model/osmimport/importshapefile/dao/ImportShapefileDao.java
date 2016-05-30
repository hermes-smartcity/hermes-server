package es.udc.lbd.hermes.model.osmimport.importshapefile.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.opengis.feature.type.PropertyType;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnectionType;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;

public class ImportShapefileDao{

static Logger logger = Logger.getLogger(ImportShapefileDao.class);
	
	private Connection connection;
	private DBConnection dbConnection;
	
	public ImportShapefileDao(DBConnection dbConnection){
		this.dbConnection = dbConnection;
	}
	
	private void abrirConexionBD() throws SQLException, ClassNotFoundException, Exception{
		try {
			//Recuperamos el classForName segun el tipo de la conexion
			DBConnectionType dbConnectionType = dbConnection.getType();
			switch (dbConnectionType) {
			case POSTGRESQL:
				Class.forName("org.postgresql.Driver");
				break;
	
			case MYSQL:
				Class.forName("com.mysql.jdbc.Driver");
				break;
			
			}
			
			//Recuperamos los parametros de url/usuario/password
			String url = construirUrl();
			String user = dbConnection.getUserDb();
			
			String pass = CifrarDescifrarUtil.descifra(dbConnection.getPassDb());
			
			connection = DriverManager.getConnection(url, user, pass);
			
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private String construirUrl(){
		
		String url = "";
		DBConnectionType dbConnectionType = dbConnection.getType();
		switch (dbConnectionType) {
		case POSTGRESQL:
			url = "jdbc:postgresql://" + dbConnection.getHost() + ":" + dbConnection.getPort() + "/" + dbConnection.getDbName();
			break;

		case MYSQL:
			url = "jdbc:mysql://" + dbConnection.getHost() + ":" + dbConnection.getPort() + "/" + dbConnection.getDbName();
			break;
		
		}
		return url;
	}
	
	private void cerrarConexionBD() throws SQLException{
		try {
			connection.close();
		} catch (SQLException e) {
			throw e;
		}
	}
	
	public void createTable(String nombreTabla, String nombreEsquema, Map<String, PropertyType> mapaAtributos, String nombreAtributoId) throws Exception{
		
		PreparedStatement preparedStatement = null;
		try{

			abrirConexionBD();
		
			String queryStr = "CREATE TABLE \"" + nombreEsquema + "\".\"" + nombreTabla + "\" (";
			queryStr = queryStr + nombreAtributoId + " bigserial primary key,";
			
			Iterator<Entry<String, PropertyType>> it = mapaAtributos.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, PropertyType> e = (Map.Entry<String, PropertyType>)it.next();
				String nombreCampo = e.getKey();
				PropertyType propertyType = e.getValue();
			
				String tipoBD = correspondenciaTipos(propertyType);
				if (tipoBD != null){
					queryStr = queryStr + nombreCampo + " " + tipoBD + ",";
				}
			}
			queryStr = queryStr.substring(0, queryStr.length()-1); //le quitamos la ultima coma
			
			queryStr = queryStr + ")";
			
			preparedStatement = connection.prepareStatement(queryStr);
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			cerrarConexionBD();
			
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} 
		
	}
	
	private String correspondenciaTipos(PropertyType propertyType){
		
		String nombreClase = propertyType.getBinding().getName();
		
		String tipoPostgres = null;;
		
		switch (nombreClase) {
		case "java.lang.Integer":
			tipoPostgres = "integer";
			break;

		case "java.lang.String":
			tipoPostgres = "text";
			break;

		case "java.lang.Long":
			tipoPostgres = "bigint";
			break;

		case "java.lang.Double":
			tipoPostgres = "double precision";
			break;

		case "java.lang.Boolean":
			tipoPostgres = "boolean";
			break;

		case "Date":
			tipoPostgres = "timestamp without time zone";
			break;

		case "Timestamp":
			tipoPostgres = "timestamp without time zone";
			break;
			
		case "com.vividsolutions.jts.geom.Point":
			tipoPostgres = "geometry(Point, 4326)";
			break;

		case "com.vividsolutions.jts.geom.Polygon":
			tipoPostgres = "geometry(Polygon, 4326)";
			break;

		case "com.vividsolutions.jts.geom.LineString":
			tipoPostgres = "geometry(Line, 4326)";
			break;

		case "com.vividsolutions.jts.geom.MultiPoint":
			tipoPostgres = "geometry(MultiPoint, 4326)";
			break;

		case "com.vividsolutions.jts.geom.MultiPolygon":
			tipoPostgres = "geometry(MultiPolygon, 4326)";
			break;

		case "com.vividsolutions.jts.geom.MultiLineString":
			tipoPostgres = "geometry(MultiLineString, 4326)";
			break;
			
			
		default:
			break;
		}
		
		return tipoPostgres;
	}
}
