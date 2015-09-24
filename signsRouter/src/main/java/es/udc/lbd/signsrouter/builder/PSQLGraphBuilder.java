package es.udc.lbd.signsrouter.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.Node;
import es.udc.lbd.signsrouter.model.Position;

public class PSQLGraphBuilder implements GraphBuilder {
	
	private static final Logger log = Logger.getLogger(PSQLGraphBuilder.class);
	
	private Connection connection;
	
	public PSQLGraphBuilder(Connection connection) {
		this.connection = connection;
	}
	
	public Graph readGraph(Object... properties) {
		//Connection c = (Connection) properties[0];
		Connection c = this.connection;
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
			
			st = c.prepareStatement(" SELECT  " +
										" id, " +
										" start_node, " +
										" st_x(ST_StartPoint(centerline_geometry)) node_x1, " +
										" st_y(ST_StartPoint(centerline_geometry)) node_y1, " +
										" st_x(start_point.p) x1, " +
										" st_y(start_point.p) y1, " +
										" end_node, " +
										" st_x(ST_EndPoint(centerline_geometry)) node_x2, " +
										" st_y(ST_EndPoint(centerline_geometry)) node_y2, " +
										" st_x(end_point.p) x2, " +
										" st_y(end_point.p) y2 " +
									" FROM hermes_transport_link, " +
									" LATERAL (SELECT 10/st_length(st_transform(centerline_geometry, 25829))) AS props(ten_m), " +	// Calculate what % of this line is 10 meters
									" LATERAL (SELECT ST_LineInterpolatePoint(centerline_geometry, LEAST(ten_m, 0.5))) AS start_point(p), " +	// Add 10 meters from the start
									" LATERAL (SELECT ST_LineInterpolatePoint(centerline_geometry, GREATEST(1-ten_m, 0.5))) AS end_point(p) ");	// Substract 10 meters from the end
			r = st.executeQuery();
			
			while (r.next()) {
				e = new Edge(r.getLong("id"), 
						new Node(r.getLong("start_node"), new Position(r.getDouble("node_x1"), r.getDouble("node_y1"))), 
						new Node(r.getLong("end_node"), new Position(r.getDouble("node_x2"), r.getDouble("node_y2"))),
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
