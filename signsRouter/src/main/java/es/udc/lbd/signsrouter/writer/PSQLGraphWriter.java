package es.udc.lbd.signsrouter.writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.TurnRestriction;

public class PSQLGraphWriter implements GraphWriter {
	
	private static final Logger log = Logger.getLogger(PSQLGraphWriter.class);
	
	private Connection connection;
	
	public PSQLGraphWriter(Connection connection) {
		this.connection = connection;
	}

	public boolean writeGraph(Graph graph) {
		log.info("Preparing to write traffic rules...");
		
		try {
			connection.setAutoCommit(false);
			
			log.debug("Writting forbidden directions");
			PreparedStatement st = connection.prepareStatement("UPDATE hermes_transport_link SET traffic_direction = ? WHERE id = ?");
			
			for (Edge e : graph.bannedEdges) {
				st.setString(1, e.id > 0 ? "inOppositeDirection" : "inDirection");
				st.setLong(2, Math.abs(e.id));
				st.addBatch();
			}
			
			st.executeBatch();
			connection.commit();
			
			
			log.debug("Writting forbidden turns");
			st = connection.prepareStatement("INSERT INTO hermes_turn_restriction (from_link, to_link) VALUES (?, ?)");
			
			for (TurnRestriction tr : graph.turnRestrictions) {
				st.setLong(1, Math.abs(tr.origin.id));
				st.setLong(2, Math.abs(tr.dest.id));
				st.addBatch();
			}
			
			st.executeBatch();
			connection.commit();
			
			connection.setAutoCommit(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		log.info("Successfully written.");
		return true;
	}

}
