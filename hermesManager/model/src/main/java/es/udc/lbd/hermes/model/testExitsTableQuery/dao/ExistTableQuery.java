package es.udc.lbd.hermes.model.testExitsTableQuery.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeType;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnectionType;
import es.udc.lbd.hermes.model.osmimport.job.json.Element;
import es.udc.lbd.hermes.model.osmimport.job.json.Tags;
import es.udc.lbd.hermes.model.osmimport.osmattribute.OsmAttribute;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;
import es.udc.lbd.hermes.model.util.exceptions.OsmAttributeBooleanException;
import es.udc.lbd.hermes.model.util.exceptions.OsmAttributeDateException;
import es.udc.lbd.hermes.model.util.exceptions.OsmAttributeException;
import es.udc.lbd.hermes.model.util.exceptions.OsmAttributeTypeException;

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
			
			queryStr = queryStr + "AND data_type IN (";
			for (int j=0; j<types.size();j++) {
				queryStr = queryStr + "?, ";
			}
			queryStr = queryStr.substring(0, queryStr.length()-2);
			queryStr = queryStr + ")";
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
	
	public void insertarDBConcept(DBConcept dbConcept, List<AttributeMapping> attributeMappings, Element element) throws SQLException, ClassNotFoundException, OsmAttributeException, OsmAttributeDateException, OsmAttributeBooleanException, OsmAttributeTypeException, Exception{
		PreparedStatement preparedStatement = null;
		try{

			abrirConexionBD();
			
			String queryStr = "INSERT INTO \"" + dbConcept.getSchemaName() + "\".\"" + dbConcept.getTableName() + "\""
					+ " (\"" + dbConcept.getOsmIdName() + "\", \"" + dbConcept.getGeomName() + "\"";
			
			for (AttributeMapping attributeMapping : attributeMappings) {
				DBAttribute dbAttribute = attributeMapping.getDbAttribute();
				queryStr = queryStr + ", \"" + dbAttribute.getAttributeName() + "\"";
			}
			
			queryStr = queryStr + ") VALUES (?, st_geometryfromtext('POINT('|| ? || ' ' || ? ||')', 4326)";
			
			for(int i=0;i<attributeMappings.size();i++){	
				queryStr = queryStr + ", ?";
			}
						
			queryStr = queryStr + ")";
			
			preparedStatement = connection.prepareStatement(queryStr);
			
			int i = 1;
						
			preparedStatement.setLong(i++, element.getId());
			preparedStatement.setDouble(i++, element.getLon());
			preparedStatement.setDouble(i++, element.getLat());
			
			for (AttributeMapping attributeMapping : attributeMappings) {
				OsmAttribute osmAttribute = attributeMapping.getOsmAttribute();
				DBAttribute dbAttribute = attributeMapping.getDbAttribute();
			
				//Recuperamos el osmAttribute indicado en la lista de tags de elemento
				String osmAttributeName = osmAttribute.getName();
				Tags tag = element.getTags();
				String valueTag = tag.recuperarAtributoIndicado(osmAttributeName);
				
				//En funcion del tipo asignaremos un long, char, boolean...
				DBAttributeType dbAttributeType = dbAttribute.getAttributeType();
								
				switch (dbAttributeType) {
				case DATE:
					
					if (valueTag != null){
						//Otros formatos: YYYY-MM-DDThh:mmTZD, YYYY-MM-DDThh:mm:ssTZD, 
						//YYYY-MM-DDThh:mm:ss.sTZD
						try {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
							java.util.Date fecha = sdf.parse(valueTag);
						    preparedStatement.setDate(i++, new Date(fecha.getTime()));
						} catch (ParseException ex) {
							throw new OsmAttributeDateException(osmAttributeName);
						}
					}else{
						preparedStatement.setNull(i++, Types.DATE);
					}
										
					break;

				case NUMBER_LONG:
					if (valueTag != null){
						Long valueLong = new Long(valueTag);
						preparedStatement.setLong(i++, valueLong);	
					}else{
						preparedStatement.setNull(i++, Types.LONGVARBINARY);
					}
					
					break;
					
				case NUMBER_DOUBLE:
					if (valueTag != null){
						Double valueDouble = new Double(valueTag);
						preparedStatement.setDouble(i++, valueDouble);	
					}else{
						preparedStatement.setNull(i++, Types.DOUBLE);
					}
					
					break;
					
				case NUMBER_INT:
					if (valueTag != null){
						Integer valueInteger = new Integer(valueTag);
						preparedStatement.setInt(i++, valueInteger);	
					}else{
						preparedStatement.setNull(i++, Types.INTEGER);
					}
					
					break;
					
				case NUMBER_FLOAT:
					if (valueTag != null){
						Float valueFloat = new Float(valueTag);
						preparedStatement.setFloat(i++, valueFloat);	
					}else{
						preparedStatement.setNull(i++, Types.FLOAT);
					}
					
					break;
					
				case CHAR:
					if (valueTag != null){
						preparedStatement.setString(i++, valueTag);	
					}else{
						preparedStatement.setNull(i++, Types.CHAR);
					}
					
					break;
					
				case BOOLEAN:
					if (valueTag != null){
						if (valueTag.toLowerCase().equals("yes")){
							preparedStatement.setBoolean(i++, true);	
						}else if (valueTag.toLowerCase().equals("no")){
							preparedStatement.setBoolean(i++, false);
						}else{
							throw new OsmAttributeBooleanException(osmAttributeName);
						}
					}else{
						preparedStatement.setNull(i++, Types.BOOLEAN);
					}
					
					
					break;
				
				default :
					throw new OsmAttributeTypeException(osmAttributeName, dbAttributeType.getName());
					
				}
				
			}
			
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
