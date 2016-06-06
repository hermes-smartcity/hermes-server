package es.udc.lbd.hermes.model.osmimport.dbconcept.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnectionType;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;

public class DBConceptJDBCDao {

	private Connection connection;
	private DBConnection dbConnection;
	
	public DBConceptJDBCDao(DBConnection dbConnection){
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
	
	public String consultarTabla(String nombreEsquema, String nombreTabla, String geomName, Geometry polygon, List<String> atributos) throws Exception{
		
		String geojson = null;
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try{

			
			abrirConexionBD();
		
			String queryStr =   "SELECT row_to_json(fc) ";
			queryStr = queryStr +   "FROM ( SELECT 'FeatureCollection' As type, array_to_json(array_agg(f)) As features ";
			queryStr = queryStr +   "FROM (SELECT 'Feature' As type ";
			queryStr = queryStr +     ", ST_AsGeoJSON(lg."+ "\"" + geomName + "\")::json As geometry ";
			queryStr = queryStr +     ", row_to_json((SELECT l FROM (SELECT ";
			
			for (String atributo : atributos) {
				queryStr = queryStr + atributo + ",";
			}
			queryStr = queryStr.substring(0, queryStr.length()-1); //quitar ultima coma
			
			queryStr = queryStr + ") As l ";
			queryStr = queryStr +  "  )) As properties ";
			queryStr = queryStr +  "FROM " + "\"" + nombreEsquema + "\".\"" + nombreTabla + "\"  As lg ";
			queryStr = queryStr + " WHERE ST_intersects("+ "\"" + geomName + "\", 'SRID=4326;" + polygon + "'::geometry ) = true ";
			queryStr = queryStr + ") As f )  As fc";
			
			preparedStatement = connection.prepareStatement(queryStr);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
	        	return null;
			}
		
			geojson = resultSet.getString(1);
			
			resultSet.close();
			preparedStatement.close();
			
			cerrarConexionBD();
			
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} 
		
		return geojson;
	}
}
