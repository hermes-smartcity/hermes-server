package es.udc.lbd.signsrouter.detector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vividsolutions.jts.geom.Coordinate;

import es.udc.lbd.signsrouter.model.Edge;
import es.udc.lbd.signsrouter.model.TrafficSign;
import es.udc.lbd.signsrouter.utils.AngleUtils;

public class DistanceDetector implements SignDetector {

	private static final Logger log = Logger.getLogger(DistanceDetector.class);
	
	private Connection connection;
	private PreparedStatement st;
	
	public DistanceDetector(Connection connection) {
		this.connection = connection;
		
		try {
			this.st = connection.prepareStatement("SELECT gid, ST_x(geom), ST_y(geom), azimut, tipo, "
					+ " ST_x(my_pos.p), ST_y(my_pos.p) "
					+ " FROM hermes_transport_link htl JOIN es_cor_signs ecs ON ST_Distance(geom, centerline_geometry) < 8, "
					+ " LATERAL (SELECT ST_LineInterpolatePoint(centerline_geometry, GREATEST(LEAST(ST_LineLocatePoint(centerline_geometry, geom) + ?, 1), 0))) AS my_pos(p) "
					+ " WHERE htl.id = ? AND ST_LineLocatePoint(centerline_geometry, geom) BETWEEN 0.0001 AND 0.9999");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Set<TrafficSign> detect(final Object... parameters) {
		Edge e = (Edge) parameters[0];
		Set<TrafficSign> signs = new HashSet<TrafficSign>();
		
		try {
			double ten_meters = e.id > 0 ? -e.ten_meters : e.ten_meters;
			st.setDouble(1, ten_meters);
			st.setLong(2, Math.abs(e.id));
			ResultSet rs = st.executeQuery();
			
			while (rs.next()) {
				TrafficSign sign = new TrafficSign(new Coordinate(rs.getDouble(2), rs.getDouble(3)), rs.getFloat(4), rs.getString(5));
				
				// Only add the sign if it's looking at me
				if (Math.abs(AngleUtils.diffDegrees(AngleUtils.calculateHeading(sign.position, new Coordinate(rs.getDouble(6), rs.getDouble(7))), sign.azimut)) < 80)
					signs.add(sign);
			}
			
			rs.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		return signs;
	}

}
