package es.udc.lbd.signsrouter.writer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.SpeedLimit;
import es.udc.lbd.signsrouter.model.TurnRestriction;

public class PSQLGraphWriter implements GraphWriter {
	
	private static final Logger log = Logger.getLogger(PSQLGraphWriter.class);
	
	private Connection connection;
	
	public PSQLGraphWriter(Connection connection) {
		this.connection = connection;
	}

	public boolean writeGraph(Graph graph) {
		log.info("Preparing to write traffic rules...");
		ResultSet rs;
		
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
			
			
			log.debug("Writting speed limits");
			st = connection.prepareStatement("SELECT max(id) + 1 FROM hermes_network_element");
			rs = st.executeQuery();
			rs.next();
			long nextId = rs.getLong(1);
			long id = nextId;
			rs.close();
			
			st = connection.prepareStatement("SELECT max(id) + 1 FROM hermes_network_reference");
			rs = st.executeQuery();
			rs.next();
			long hnrId = rs.getLong(1);
			rs.close();
			
			st = connection.prepareStatement("SELECT max(id) + 1 FROM hermes_transport_property");
			rs = st.executeQuery();
			rs.next();
			long htpId = rs.getLong(1);
			rs.close();
			
			List<SpeedLimit> limits = new ArrayList<SpeedLimit>(graph.speedLimits.size()+1);
			limits.addAll(graph.speedLimits);	// I need some guaranteed order
			
			st = connection.prepareStatement("INSERT INTO hermes_network_element (id, type) VALUES (?, 'TransportLinkSet')");
			for (SpeedLimit limit : limits) {
				st.setLong(1, id++);
				st.addBatch();
			}
			
			st.executeBatch();
			id = nextId;
			
			st = connection.prepareStatement("INSERT INTO hermes_transport_link_set (id) VALUES (?)");
			for (SpeedLimit limit : limits) {
				st.setLong(1, id++);
				st.addBatch();
			}
			
			st.executeBatch();
			id = nextId;
			
			st = connection.prepareStatement("INSERT INTO hermes_transport_link_set_element (link_set_id, link_id, type) VALUES (?, ?, 'TransportLink')");
			for (SpeedLimit limit : limits) {
				for (Edge e : limit.edges) {
					st.setLong(1, id);
					st.setLong(2, Math.abs(e.id));
					st.addBatch();
				}
				
				id++;
			}
			
			st.executeBatch();
			id = nextId;
			
			st = connection.prepareStatement("INSERT INTO hermes_network_reference (network_element_id, type) VALUES (?, 'SimplePointReference')");
			for (SpeedLimit limit : limits) {
				st.setLong(1, id++);
				st.addBatch();
			}
			
			st.executeBatch();
			id = nextId;
			
			st = connection.prepareStatement("INSERT INTO hermes_transport_property (type) VALUES ('SpeedLimit')");
			for (SpeedLimit limit : limits) {
				st.addBatch();
			}
			
			st.executeBatch();
			id = htpId;
			
			st = connection.prepareStatement("INSERT INTO hermes_network_reference_transport_property (network_reference_id, transport_property_id) VALUES (?, ?)");
			for (SpeedLimit limit : limits) {
				st.setLong(1, hnrId++);
				st.setLong(2, id++);
				st.addBatch();
			}
			
			st.executeBatch();
			id = htpId;
			
			st = connection.prepareStatement("INSERT INTO hermes_speed_limit (id, speedLimitValue) VALUES (?, ?)");
			for (SpeedLimit limit : limits) {
				st.setLong(1, id++);
				st.setInt(2, limit.speed);
				st.addBatch();
			}
			
			st.executeBatch();
			connection.commit();
			
			connection.setAutoCommit(true);
			st.close();
			
		} catch (SQLException e) {
			System.err.println(e.getNextException());
			e.printStackTrace();
			return false;
		}
		
		log.info("Successfully written.");
		return true;
	}

}
