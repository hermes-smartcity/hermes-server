package es.udc.lbd;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;

public class RouterMain {
	
	private static final String POSTGRESQL_PROPS = "postgresql.properties";
	private static final long START_EDGE = 84670;
	
    public static void main( String[] args )  {
    	BasicConfigurator.configure();	// Log4J configuration
    	
    	Connection connection = null;
    	GraphBuilder builder = new PSQLGraphBuilder();
    	Properties props = new Properties();
    	InputStream is = RouterMain.class.getClassLoader().getResourceAsStream(POSTGRESQL_PROPS);
    	
    	try {
			props.load(is);
			connection = PSQLConnectionProvider.getConnection(props);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	Graph g = builder.readGraph(connection);
    	
    	System.out.println(g.nodes[21150].outgouingEdges);
    	
        Navigator navigator = new BreadthFirstNavigator(connection, new ConusSignDetector(connection));
		navigator.navigate(g, g.edges.get(START_EDGE));
		
		// TODO insert into database
		
		try {
			connection.close();
		} catch (SQLException e) {
			// Fail silently
		}
    }
}
