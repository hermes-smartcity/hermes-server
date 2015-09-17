package es.udc.lbd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class PSQLGraphBuilder implements GraphBuilder {
	
	private static final Logger log = Logger.getLogger(PSQLGraphBuilder.class);
	
	public Graph readGraph(Object... properties) {
		Connection c = (Connection) properties[0];
		PreparedStatement st;
		ResultSet r;
        Graph g = null;
        Edge e = null;
        
		try {
			st = c.prepareStatement("SELECT COUNT(*) FROM hermes_transport_node");
			r = st.executeQuery();
			r.next();
			g = new Graph(r.getInt(1)+1);
			r.close();
			st.close();
			
			st = c.prepareStatement("SELECT id, "
					+ " ST_x(ST_PointN(link_geometry, 2)) AS x1, ST_y(ST_PointN(link_geometry, 2)) AS y1, "
					+ " ST_x(ST_PointN(link_geometry, ST_NPoints(link_geometry) - 1)) AS x2, ST_y(ST_PointN(link_geometry, ST_NPoints(link_geometry) - 1)) AS y2, "
					+ " s_id, ST_x(s_geometry) AS node_x1, ST_y(s_geometry) AS node_y1, "
					+ " t_id, ST_x(t_geometry) AS node_x2, ST_y(t_geometry) AS node_y2 "
					+ " FROM h_link");
			r = st.executeQuery();
			
			while (r.next()) {
				e = new Edge(r.getLong("id"), 
						new Node(r.getLong("s_id"), new Position(r.getDouble("node_x1"), r.getDouble("node_y1"))), 
						new Node(r.getLong("t_id"), new Position(r.getDouble("node_x2"), r.getDouble("node_y2"))),
						new Position(r.getDouble("x1"), r.getDouble("y1")),
						new Position(r.getDouble("x2"), r.getDouble("y2")));
						
				g.addEdge(e);
				g.addEdge(e.reverse());
			}
			
//			g.calculateTransients();
			
			r.close();
			st.close();
//			c.close();
			
			log.info("Graph size: " + g.edges.size() + " edges, " + g.nodes.length + " nodes.");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return g;
	}

}
