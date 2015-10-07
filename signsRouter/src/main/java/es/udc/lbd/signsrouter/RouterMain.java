package es.udc.lbd.signsrouter;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.BasicConfigurator;

import es.udc.lbd.signsrouter.builder.GraphBuilder;
import es.udc.lbd.signsrouter.builder.PSQLGraphBuilder;
import es.udc.lbd.signsrouter.detector.ConusSignDetector;
import es.udc.lbd.signsrouter.detector.DistanceDetector;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.navigator.BreadthFirstNavigator;
import es.udc.lbd.signsrouter.navigator.Navigator;
import es.udc.lbd.signsrouter.writer.GraphWriter;
import es.udc.lbd.signsrouter.writer.PSQLGraphWriter;

public class RouterMain {
	
	private static final String POSTGRESQL_PROPS = "postgresql.properties";
	private static final String HERMES_PROPS = "hermes.properties";
	private static final long START_EDGE = 53312;
	
    public static void main( String[] args )  {
    	BasicConfigurator.configure();	// Log4J configuration
    	
    	Connection connection = null;
    	Properties props = new Properties();
    	InputStream is = RouterMain.class.getClassLoader().getResourceAsStream(POSTGRESQL_PROPS);
    	
    	try {
			props.load(is);
			connection = PSQLConnectionProvider.getConnection(props);
			is.close();
			
			is = RouterMain.class.getClassLoader().getResourceAsStream(HERMES_PROPS);
			props.load(is);
			is.close();
			
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
    	
    	GraphBuilder builder = new PSQLGraphBuilder(connection);
    	Graph g = builder.readGraph();
    	g.epsg = Integer.parseInt(props.getProperty("hermes.epsg"));
    	Set<TrafficSign> signs = builder.readTrafficSigns();
    	
        Navigator navigator = new BreadthFirstNavigator(new DistanceDetector(connection), new ConusSignDetector(signs));
		navigator.navigate(g, g.findEdge(START_EDGE));
		
		GraphWriter writer = new PSQLGraphWriter(connection);
		writer.writeGraph(g);
		
		try {
			connection.close();
		} catch (SQLException e) {
			// Fail silently
		}
    }
}
