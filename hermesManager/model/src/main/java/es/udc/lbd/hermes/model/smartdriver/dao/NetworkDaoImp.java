package es.udc.lbd.hermes.model.smartdriver.dao;

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

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
import es.udc.lbd.hermes.model.smartdriver.RouteSegment;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

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
	
	public Integer obtainOriginPoint(Double fromLat, Double fromLng){
		
		String queryString = "select source " +
							"from network.link " +
							"where geom_way && st_expand(st_geometryfromtext('POINT('|| :fromLng || ' ' ||:fromLat ||')', 4326), 0.001) " + 
							"order by st_distance(geom_way, st_geometryfromtext('POINT('|| :fromLng || ' ' ||:fromLat ||')', 4326)) " + 
							"limit 1";
		
		Query query = getSession().createSQLQuery(queryString);
		
		query.setParameter("fromLat", fromLat);
		query.setParameter("fromLng", fromLng);
		
		return (Integer) query.uniqueResult();
	}
	
	public Integer obtainDestinyPoint(Double toLat, Double toLng){
		String queryString = "select source " +
				"from network.link " +
				"where geom_way && st_expand(st_geometryfromtext('POINT('|| :toLng || ' ' ||:toLat ||')', 4326), 0.001) " + 
				"order by st_distance(geom_way, st_geometryfromtext('POINT('|| :toLng || ' ' ||:toLat ||')', 4326)) " + 
				"limit 1";

		Query query = getSession().createSQLQuery(queryString);
		
		query.setParameter("toLat", toLat);
		query.setParameter("toLng", toLng);
		
		return (Integer) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<RouteSegment> obtainListSections(Integer originPoint, Integer destinyPoint){
		
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
							"FROM pgr_bdAstar('SELECT id, source, target, cost, x1, y1, x2, y2, " +
													"reverse_cost " +
											  "FROM network.link', :originPoint, :destinyPoint, true, true)" +
							") as path " +
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
				
		query.setParameter("originPoint", originPoint);
		query.setParameter("destinyPoint", destinyPoint);
		
		query.setResultTransformer(Transformers.aliasToBean(RouteSegment.class));
		return (List<RouteSegment>) query.list();
	}
}
