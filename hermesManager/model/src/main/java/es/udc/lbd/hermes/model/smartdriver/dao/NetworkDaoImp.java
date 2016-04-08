package es.udc.lbd.hermes.model.smartdriver.dao;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.smartdriver.NetworkLink;
import es.udc.lbd.hermes.model.smartdriver.NetworkLinkVO;
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
}
