package es.udc.lbd.hermes.model.testExitsTableQuery.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeType;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnectionType;
import es.udc.lbd.hermes.model.osmimport.job.json.Element;
import es.udc.lbd.hermes.model.osmimport.job.json.Tag;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;

public class ExistTableQuery {

	static Logger logger = Logger.getLogger(ExistTableQuery.class);
	
	private Connection connection;
	private DBConnection dbConnection;
	
	public ExistTableQuery(DBConnection dbConnection){
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
			url = "jdbc:postgresql://" + dbConnection.getHost() + ":" + dbConnection.getPort() + "/" + dbConnection.getName();
			break;

		case MYSQL:
			url = "jdbc:mysql://" + dbConnection.getHost() + ":" + dbConnection.getPort() + "/" + dbConnection.getName();
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
	
	public Boolean exitsSchemaTable(String schema, String table) throws SQLException, ClassNotFoundException, Exception{
		Boolean existe = false;
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try{
			abrirConexionBD();
			
			String queryStr = "SELECT EXISTS ( " +
								  "SELECT 1 " +
								  "FROM information_schema.tables " +
								  "WHERE table_schema = ? " +
								  "AND table_name = ? " +
							  ")";
			
			preparedStatement = connection.prepareStatement(queryStr);
			
			int i = 1;
			preparedStatement.setString(i++, schema);
			preparedStatement.setString(i++, table);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
	        	return false;
			}
		
			existe = resultSet.getBoolean(1);
		
			resultSet.close();
			preparedStatement.close();
			
			cerrarConexionBD();
			
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} 
		
		return existe;
		
		
	}
	
	public Boolean existsColumnTable(String schema, String table, String column) throws SQLException, ClassNotFoundException, Exception{
		Boolean existe = false;
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
				
		try{
			
			abrirConexionBD();
			
			String queryStr = "SELECT EXISTS ( " +
					  "SELECT 1 " +
					  "FROM information_schema.columns " +
					  "WHERE table_schema = ? " +
					  "AND table_name = ? " +
					  "AND column_name = ? " +
				  ")";
	
			preparedStatement = connection.prepareStatement(queryStr);
			
			int i = 1;
			preparedStatement.setString(i++, schema);
			preparedStatement.setString(i++, table);
			preparedStatement.setString(i++, column);
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
	        	return false;
			}
		
			existe = resultSet.getBoolean(1);
			
			resultSet.close();
			preparedStatement.close();
			
			cerrarConexionBD();
		
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		}  
		
		return existe;
	}
	
	public Boolean existsTypeColumn(String schema, String table, String column, List<String> types) throws SQLException, ClassNotFoundException, Exception{
		Boolean existe = false;
		
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		
		try{

			abrirConexionBD();

			String queryStr = "SELECT EXISTS ( " +
					  "SELECT 1 " +
					  "FROM information_schema.columns " +
					  "WHERE table_schema = ? " +
					  "AND table_name = ? " +
					  "AND column_name = ? ";
			
			for (int j=0; j<types.size();j++) {
				queryStr = queryStr + "AND data_type IN (?) ";
			}
					  
			queryStr = queryStr + ")";
	
			preparedStatement = connection.prepareStatement(queryStr);
			
			int i = 1;
			preparedStatement.setString(i++, schema);
			preparedStatement.setString(i++, table);
			preparedStatement.setString(i++, column);
			for (String type : types) {
				preparedStatement.setString(i++, type);
			}
			
			resultSet = preparedStatement.executeQuery();
			
			if (!resultSet.next()) {
	        	return false;
			}
		
			existe = resultSet.getBoolean(1);
			
			resultSet.close();
			preparedStatement.close();
			
			cerrarConexionBD();
		
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} 
		
		return existe;
	}
	
	public void insertarDBConcept(DBConcept dbConcept, List<AttributeMapping> attributeMappings, Element element) throws SQLException, ClassNotFoundException, Exception{
		PreparedStatement preparedStatement = null;
		try{

			abrirConexionBD();
			
			String queryStr = "INSERT INTO ?.? (?, ?";
			
			preparedStatement = connection.prepareStatement(queryStr);
			
			int i = 1;
			preparedStatement.setString(i++, dbConcept.getSchemaName());
			preparedStatement.setString(i++, dbConcept.getTableName());
			preparedStatement.setString(i++, dbConcept.getOsmIdName());
			preparedStatement.setString(i++, dbConcept.getGeomName());
			
			for (AttributeMapping attributeMapping : attributeMappings) {
				DBAttribute dbAttribute = attributeMapping.getDbAttribute();
				
				queryStr = queryStr + ", ?";
				
				preparedStatement.setString(i++, dbAttribute.getAttributeName());
			}
			
			queryStr = queryStr + ") VALUES (?, st_geometryfromtext('POINT('|| ? || ' ' || ? ||')', 4326)";
			
			preparedStatement.setLong(i++, element.getId());
			preparedStatement.setDouble(i++, element.getLon());
			preparedStatement.setDouble(i++, element.getLat());
			
			for (AttributeMapping attributeMapping : attributeMappings) {
				OsmAttribute osmAttribute = attributeMapping.getOsmAttribute();
				DBAttribute dbAttribute = attributeMapping.getDbAttribute();
			
				//Recuperamos el osmAttribute indicado en la lista de tags de elemento
				String osmAttributeName = osmAttribute.getName();
				Tag tag = element.getTags();
				String valueTag = tag.recuperarAtributoIndicado(osmAttributeName);
				
				if (valueTag != null){
					
					queryStr = queryStr + ", ?";
					
					//En funcion del tipo asignaremos un long, char, boolean...
					DBAttributeType dbAttributeType = dbAttribute.getAttributeType();
									
					switch (dbAttributeType) {
					case DATE:
						//TODO: en que formato vienen los date?
						preparedStatement.setDate(i++, new Date(4535363));
						break;

					case NUMBER_LONG:
						Long valueLong = new Long(valueTag);
						preparedStatement.setLong(i++, valueLong);
						break;
						
					case NUMBER_DOUBLE:
						Double valueDouble = new Double(valueTag);
						preparedStatement.setDouble(i++, valueDouble);
						break;
						
					case NUMBER_INT:
						Integer valueInteger = new Integer(valueTag);
						preparedStatement.setInt(i++, valueInteger);
						break;
						
					case NUMBER_FLOAT:
						Float valueFloat = new Float(valueTag);
						preparedStatement.setFloat(i++, valueFloat);
						break;
						
					case CHAR:
						preparedStatement.setString(i++, valueTag);
						break;
						
					case BOOLEAN:
						//TODO: ¿COMO VIENE LOS BOOLEAN "SI", "NO", "S", "N", ..
						preparedStatement.setBoolean(i++, true);
						break;
					
					}
				}
				
				
			}
						
			queryStr = queryStr + ")";
			
			
			preparedStatement.executeUpdate();
			
			preparedStatement.close();
			
			cerrarConexionBD();
			
		} catch (SQLException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			throw e;
		} 
	}
	
}
