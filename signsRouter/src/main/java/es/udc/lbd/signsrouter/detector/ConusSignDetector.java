package es.udc.lbd.signsrouter.detector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import es.udc.lbd.signsrouter.model.Position;
import es.udc.lbd.signsrouter.model.TrafficSign;

public class ConusSignDetector implements SignDetector {

	private static final Logger log = Logger.getLogger(ConusSignDetector.class);
	
	private Connection connection;
	private PreparedStatement statement;
	
	public ConusSignDetector(Connection connection) {
		this.connection = connection;
		try {
			// Make a conus heading to the street we want to go into, a left angle of 10 and a right angle of 60
			// Also check if the sign is oriented to us so we can see it
			// FIXME: This should not be so SRID dependant
			this.statement = connection.prepareStatement("SELECT ST_x(geom) AS x, ST_y(geom) AS y, azimut, tipo FROM es_cor_signs "
					+ " WHERE ST_Distance(ST_Transform(geom, 25829), ST_Transform(ST_SetSRID(ST_Point(?, ?), 4326), 25829)) < 10 "
					+ " AND degrees(ST_Azimuth(ST_SetSRID(ST_Point(?, ?), 4326), geom)) - ? BETWEEN -10 AND 60 "
					+ " AND abs(degrees(ST_Azimuth(geom, ST_SetSRID(ST_Point(?, ?), 4326))) - azimut) NOT BETWEEN 80 AND 280 ");	// I hate working with angles...
		} catch (SQLException e) {
			// Should never fail
			e.printStackTrace();
		}
	}

	public Set<TrafficSign> detect(Position p, double heading) {
		Set<TrafficSign> signs = new HashSet<TrafficSign>();
//		log.debug("Searching for signs from " + p + " heading to " + heading);
		
		try {
			statement.setDouble(1, p.x);
			statement.setDouble(2, p.y);
			statement.setDouble(3, p.x);
			statement.setDouble(4, p.y);
			statement.setDouble(5, heading);
			statement.setDouble(6, p.x);
			statement.setDouble(7, p.y);
			ResultSet r = statement.executeQuery();
			
			while (r.next()) {
				signs.add(new TrafficSign(new Position(r.getDouble("x"), r.getDouble("y")), r.getFloat("azimut"), r.getString("tipo")));
			}
			
			r.close();
			
		} catch (SQLException e) {
			// Should never fail
			e.printStackTrace();
		}
		
		return signs;
	}

}
