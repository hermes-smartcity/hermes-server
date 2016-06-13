package es.udc.lbd.hermes.model.smartdriver.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.linearref.LengthIndexedLine;

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.OriginDestinyPoint;
import es.udc.lbd.hermes.model.smartdriver.RoutePoint;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
import es.udc.lbd.hermes.model.util.exceptions.RouteException;

@Repository
public class NetworkDaoImp extends GenericDaoHibernate<NetworkLink, Long> implements NetworkDao {

	public NetworkLinkVO getLinkInformation(Double currentLong, Double currentLat, Double previousLong, Double previousLat){
		
		String queryString = "select osm_id as \"linkId\", kmh as \"maxSpeed\", osm_name as \"linkName\", " +
								"case " +
								    "when clazz = 1 then 'highway' " +
								    "when clazz = 2 then 'highway_link' " + 
								    "when clazz = 3 then 'trunk' " +
								    "when clazz = 4 then 'trunk_link' " + 
								    "when clazz = 5 then 'primary' " +
								    "when clazz = 6 then 'primary_link' " + 
								    "when clazz = 7 then 'secondary' " +
								    "when clazz = 8 then 'secondary_link' " + 
								    "when clazz = 9 then 'tertiary' " +
								    "when clazz = 10 then 'tertiary_link' " + 
								    "when clazz = 11 then 'residential' " +
								    "when clazz = 12 then 'road' " +
								    "when clazz = 13 then 'unclassified' " + 
								    "when clazz = 14 then 'service' " +
								    "when clazz = 15 then 'living_street' " + 
								    "when clazz = 16 then 'pedestrian' " +
								    "when clazz = 17 then 'track' " +
								    "when clazz = 18 then 'path' " +
								    "when clazz = 19 then 'cicleway' " + 
								    "when clazz = 20 then 'footway' " + 
								    "when clazz = 21 then 'steps' " +
							    "end as \"linkType\", " +
								"st_length(geom_way, true) as length, " +
								"st_lineLocatePoint(geom_way, st_geometryfromtext('POINT('|| :currentlong || ' ' ||:currentlat ||')', 4326)) as position, " +
								"st_lineLocatePoint(geom_way, st_geometryfromtext('POINT('|| :previouslong || ' ' ||:previouslat ||')', 4326)) as \"previousPosition\", " +
								"case " +
									"when st_lineLocatePoint(geom_way, st_geometryfromtext('POINT('|| :currentlong || ' ' ||:currentlat ||')', 4326)) - st_lineLocatePoint(geom_way, st_geometryfromtext('POINT('|| :previouslong || ' ' ||:previouslat ||')', 4326)) > 0 then 0 " +
									"else 1 " +
								"end as direction " +
								"from network.link " +
								"where st_geometryfromtext('POINT('|| :currentlong || ' ' ||:currentlat ||')', 4326) && geom_way " +
								"and st_distance(st_geometryfromtext('POINT('|| :currentlong || ' ' ||:currentlat ||')', 4326), geom_way, true) < 10 " +
								"order by st_distance(st_geometryfromtext('POINT('|| :currentlong || ' ' ||:currentlat ||')', 4326), geom_way, true) limit 1";
		
		Query query = getSession().createSQLQuery(queryString);
		query.setResultTransformer(Transformers.aliasToBean(NetworkLinkVO.class));
		
		query.setParameter("currentlong", currentLong);
		query.setParameter("currentlat", currentLat);
		query.setParameter("previouslong", previousLong);
		query.setParameter("previouslat", previousLat);
		
		NetworkLinkVO resultado = (NetworkLinkVO) query.uniqueResult();
		
		return resultado;
	}
	
	public OriginDestinyPoint obtainOriginPoint(Double fromLat, Double fromLng){
		
		String queryString = "select source as source, x1 as x1, y1 as y1 " +
							"from network.link " +
							"where geom_way && st_expand(st_geometryfromtext('POINT('|| :fromLng || ' ' ||:fromLat ||')', 4326), 0.1) " + 
							"order by st_distance(geom_way, st_geometryfromtext('POINT('|| :fromLng || ' ' ||:fromLat ||')', 4326)) " + 
							"limit 1";
		
		Query query = getSession().createSQLQuery(queryString);
		query.setResultTransformer(Transformers.aliasToBean(OriginDestinyPoint.class));
		
		query.setParameter("fromLat", fromLat);
		query.setParameter("fromLng", fromLng);
		
		return (OriginDestinyPoint) query.uniqueResult();
	}
	
	public OriginDestinyPoint obtainDestinyPoint(Double toLat, Double toLng){
		String queryString = "select source as source, x1 as x1, y1 as y1 " +
				"from network.link " +
				"where geom_way && st_expand(st_geometryfromtext('POINT('|| :toLng || ' ' ||:toLat ||')', 4326), 0.1) " + 
				"order by st_distance(geom_way, st_geometryfromtext('POINT('|| :toLng || ' ' ||:toLat ||')', 4326)) " + 
				"limit 1";

		Query query = getSession().createSQLQuery(queryString);
		query.setResultTransformer(Transformers.aliasToBean(OriginDestinyPoint.class));
		
		query.setParameter("toLat", toLat);
		query.setParameter("toLng", toLng);
		
		return (OriginDestinyPoint) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RouteSegment> obtainListSections(OriginDestinyPoint originPoint, OriginDestinyPoint destinyPoint) throws RouteException{
		
		List<RouteSegment> listado = null;
		try{
			String queryString = "SELECT osm_id as \"linkId\", kmh as \"maxSpeed\", osm_name as \"linkName\", " +
								"case " +
									"when clazz = 1 then 'highway' " +
									"when clazz = 2 then 'highway_link' " +
									"when clazz = 3 then 'trunk' " +
									"when clazz = 4 then 'trunk_link' " +
									"when clazz = 5 then 'primary' " +
									"when clazz = 6 then 'primary_link' " +
									"when clazz = 7 then 'secondary' " +
									"when clazz = 8 then 'secondary_link' " +
									"when clazz = 9 then 'tertiary' " +
									"when clazz = 10 then 'tertiary_link' " +
									"when clazz = 11 then 'residential' " +
									"when clazz = 12 then 'road' " +
									"when clazz = 13 then 'unclassified' " +
									"when clazz = 14 then 'service' " +
									"when clazz = 15 then 'living_street' " +
									"when clazz = 16 then 'pedestrian' " +
									"when clazz = 17 then 'track' " +
									"when clazz = 18 then 'path' " +
									"when clazz = 19 then 'cicleway' " +
									"when clazz = 20 then 'footway' " +
									"when clazz = 21 then 'steps' " +
								"end as \"linkType\", km as length, link.cost as cost, geom_way " +
								"FROM network.link, " +
								"(SELECT seq, id1 as node, id2 as edge, cost " +
								"FROM pgr_astar('SELECT id, source, target, cost, x1, y1, x2, y2, " +
														"reverse_cost " +
												  "FROM network.link " +
												  "WHERE geom_way && " +
												  	"(SELECT st_expand(st_envelope(st_union(geom_way)),1) " +
												  	"FROM network.link " +
												  	"WHERE source IN ('||:originPoint || ', ' ||:destinyPoint||'))', " +
												  	":originPoint, :destinyPoint, true, true)) as path " +
								"where link.id = path.edge " +
								"order by path.seq";
			
			SQLQuery query = getSession().createSQLQuery(queryString);
			query.addScalar("linkId", LongType.INSTANCE);
			query.addScalar("maxSpeed", IntegerType.INSTANCE);
			query.addScalar("linkName", StringType.INSTANCE);
			query.addScalar("linkType", StringType.INSTANCE);
			query.addScalar("length", DoubleType.INSTANCE);
			query.addScalar("cost", DoubleType.INSTANCE);
			query.addScalar("geom_way", GeometryType.INSTANCE);		
					
			query.setParameter("originPoint", originPoint.getSource());
			query.setParameter("destinyPoint", destinyPoint.getSource());
					
			query.setResultTransformer(Transformers.aliasToBean(RouteSegment.class));
			listado = (List<RouteSegment>) query.list();
			Coordinate previousCoordinate = new Coordinate(originPoint.getX1(), originPoint.getY1());			
			for (RouteSegment routeSegment:listado){
				Coordinate firstCoordinate = routeSegment.getGeom_way().getStartPoint().getCoordinate();
				if (!firstCoordinate.equals2D(previousCoordinate)) {
					routeSegment.setGeom_way((LineString)routeSegment.getGeom_way().reverse());
				}
				previousCoordinate = routeSegment.getGeom_way().getEndPoint().getCoordinate();
			}
		} catch (org.hibernate.exception.GenericJDBCException e){
			throw new RouteException();
		}		
		return listado;
	}
	
	@SuppressWarnings("unchecked")
	public List<RoutePoint> simulateListSections(OriginDestinyPoint originPoint, OriginDestinyPoint destinyPoint, Double sf, Double secondsperstep)  throws RouteException{
		
		List<RouteSegment> listado = null;
		List<RoutePoint> result = new ArrayList<RoutePoint>();
		GeometryFactory geometryFactory = new GeometryFactory();
		
		try{
			String queryString = "SELECT osm_id as \"linkId\", kmh as \"maxSpeed\", osm_name as \"linkName\", " +
								"case " +
									"when clazz = 1 then 'highway' " +
									"when clazz = 2 then 'highway_link' " +
									"when clazz = 3 then 'trunk' " +
									"when clazz = 4 then 'trunk_link' " +
									"when clazz = 5 then 'primary' " +
									"when clazz = 6 then 'primary_link' " +
									"when clazz = 7 then 'secondary' " +
									"when clazz = 8 then 'secondary_link' " +
									"when clazz = 9 then 'tertiary' " +
									"when clazz = 10 then 'tertiary_link' " +
									"when clazz = 11 then 'residential' " +
									"when clazz = 12 then 'road' " +
									"when clazz = 13 then 'unclassified' " +
									"when clazz = 14 then 'service' " +
									"when clazz = 15 then 'living_street' " +
									"when clazz = 16 then 'pedestrian' " +
									"when clazz = 17 then 'track' " +
									"when clazz = 18 then 'path' " +
									"when clazz = 19 then 'cicleway' " +
									"when clazz = 20 then 'footway' " +
									"when clazz = 21 then 'steps' " +
								"end as \"linkType\", km as length, link.cost as cost, geom_way " +
								"FROM network.link, " +
								"(SELECT seq, id1 as node, id2 as edge, cost " +
								"FROM pgr_astar('SELECT id, source, target, cost, x1, y1, x2, y2, " +
														"reverse_cost " +
												  "FROM network.link " +
												  "WHERE geom_way && " +
												  	"(SELECT st_expand(st_envelope(st_union(geom_way)),1) " +
												  	"FROM network.link " +
												  	"WHERE source IN ('||:originPoint || ', ' ||:destinyPoint||'))', " +
												  	":originPoint, :destinyPoint, true, true)) as path " +
								"where link.id = path.edge " +
								"order by path.seq";
			
			SQLQuery query = getSession().createSQLQuery(queryString);
			query.addScalar("linkId", LongType.INSTANCE);
			query.addScalar("maxSpeed", IntegerType.INSTANCE);
			query.addScalar("linkName", StringType.INSTANCE);
			query.addScalar("linkType", StringType.INSTANCE);
			query.addScalar("length", DoubleType.INSTANCE);
			query.addScalar("cost", DoubleType.INSTANCE);
			query.addScalar("geom_way", GeometryType.INSTANCE);		
					
			query.setParameter("originPoint", originPoint.getSource());
			query.setParameter("destinyPoint", destinyPoint.getSource());
					
			query.setResultTransformer(Transformers.aliasToBean(RouteSegment.class));
			listado = (List<RouteSegment>) query.list();
			double previousSeconds = 0;
			Coordinate previousEnd = new Coordinate(originPoint.getX1(), originPoint.getY1());
			for (RouteSegment routeSegment:listado){
				Coordinate firstCoordinate = routeSegment.getGeom_way().getStartPoint().getCoordinate();
				if (!firstCoordinate.equals2D(previousEnd)) {
					routeSegment.setGeom_way((LineString)routeSegment.getGeom_way().reverse());
				}
				double rsSpeed = kmPerHourToDegreesPerSecond(routeSegment.getMaxSpeed() * sf, routeSegment.getGeom_way().getStartPoint().getY());
				LengthIndexedLine lil = new LengthIndexedLine(routeSegment.getGeom_way());
				int position = 0;
				double step = distanceStep(rsSpeed, secondsperstep);
				double offset = distanceStep(rsSpeed, (secondsperstep - previousSeconds));
				while (position*step+offset < routeSegment.getGeom_way().getLength()) {
					Coordinate point = lil.extractPoint(position*step+offset);
					result.add(new RoutePoint(routeSegment.getMaxSpeed() * sf, geometryFactory.createPoint(point)));
					position++;
				}
				if (position > 0) {
					double lastDistance = lil.indexOf(result.get(result.size()-1).getPosition().getCoordinate());
					double lastStep = routeSegment.getGeom_way().getLength() - lastDistance;
					previousSeconds = lastStep/rsSpeed;					
				} else { // No point was used for this segment
					previousSeconds = previousSeconds + (routeSegment.getGeom_way().getLength() / rsSpeed); 
				}
				previousEnd = routeSegment.getGeom_way().getEndPoint().getCoordinate();
			}
		} catch (org.hibernate.exception.GenericJDBCException e){
			throw new RouteException();
		}		
		return result;		
	}
	
	private double kmPerHourToDegreesPerSecond(double speed, double latitude) {
		return speed * (metersToDecimalDegrees(1000.0, latitude)/3600.0);
	}

	
	private double distanceStep(double speed, double seconds) {
		return speed * seconds;
	}
	
	private double metersToDecimalDegrees(double meters, double latitude) {
	    return meters / (111.32 * 1000 * Math.cos(latitude * (Math.PI / 180)));
	}
}
