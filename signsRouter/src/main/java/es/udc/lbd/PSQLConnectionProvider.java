package es.udc.lbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PSQLConnectionProvider {
	private static final Logger log = Logger.getLogger(PSQLGraphBuilder.class);
	
	public static Connection getConnection(Properties connectionProps) throws SQLException {

	    Connection conn = DriverManager.getConnection("jdbc:postgresql://"
	            + connectionProps.getProperty("address") + ":" 
	    		+ connectionProps.getProperty("port") + "/" 
	    		+ connectionProps.getProperty("database"),
	    	connectionProps);
	    
	    log.info("Connected to database");
	    return conn;
	}
}
