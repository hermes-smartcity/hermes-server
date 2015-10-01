package es.udc.lbd.signsrouter.builder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.Graph;
import es.udc.lbd.signsrouter.model.Node;
import es.udc.lbd.signsrouter.model.TrafficSign;

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
			g = new Graph();
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
									" LATERAL (SELECT 10/st_length(centerline_geometry)) AS props(ten_m), " +	// Calculate what % of this line is 10 meters
									" LATERAL (SELECT ST_LineInterpolatePoint(centerline_geometry, LEAST(ten_m, 0.5))) AS start_point(p), " +	// Add 10 meters from the start
									" LATERAL (SELECT ST_LineInterpolatePoint(centerline_geometry, GREATEST(1-ten_m, 0.5))) AS end_point(p) ");	// Substract 10 meters from the end
			r = st.executeQuery();
			
			while (r.next()) {
				e = new Edge(r.getLong("id"), 
						new Node(r.getLong("start_node"), new Coordinate(r.getDouble("node_x1"), r.getDouble("node_y1")), null), 
						new Node(r.getLong("end_node"), new Coordinate(r.getDouble("node_x2"), r.getDouble("node_y2")), null),
						new Coordinate(r.getDouble("x1"), r.getDouble("y1")),
						new Coordinate(r.getDouble("x2"), r.getDouble("y2")));
						
				g.add(e);
				g.add(e.getSym());
			}
			
//			g.calculateTransients();
			
			r.close();
			st.close();
//			c.close();
			
			log.info("Graph size: " + g.getEdgeEnds().size() + " edges, " + g.getNodes().size() + " nodes.");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		STRtree tree = new STRtree();
		
		for (Object o : g.getNodes()) {
			com.vividsolutions.jts.geomgraph.Node n = (com.vividsolutions.jts.geomgraph.Node) o;
			tree.insert(new Envelope(n.getCoordinate()), n);
		}
		
		tree.build();
		
		List<com.vividsolutions.jts.geomgraph.Node> foundNodes = tree.query(new Envelope(548635.433, 548642.624, 4802853.873, 4802860.091));
		
		return g;
	}

	public Set<TrafficSign> readTrafficSigns(Object... properties) {
		Connection c = this.connection;
		PreparedStatement st;
		ResultSet r;
		Set<TrafficSign> signs = new HashSet<TrafficSign>();
        Edge e = null;
        
		try {
			st = c.prepareStatement("SELECT COUNT(*) FROM hermes_transport_node");
			r = st.executeQuery();
			r.next();
			r.close();
			st.close();
			
			st = c.prepareStatement("SELECT gid, ST_x(geom) x, ST_y(geom) y, azimut, tipo FROM es_cor_signs");
			r = st.executeQuery();
			
			while (r.next()) {
				signs.add(new TrafficSign(new Coordinate(r.getDouble("x"), r.getDouble("y")), r.getFloat("azimut"), r.getString("tipo")));
			}
			
//			g.calculateTransients();
			
			r.close();
			st.close();
//			c.close();
			
			log.info("Read " + signs.size() + " traffic signs.");
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		return signs;
	}

}
