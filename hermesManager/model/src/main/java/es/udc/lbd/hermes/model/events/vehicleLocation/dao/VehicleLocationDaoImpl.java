package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocationDTO;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.util.GeomUtil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class VehicleLocationDaoImpl extends GenericDaoHibernate<VehicleLocation, Long> implements
VehicleLocationDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocation> obterVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){

		List<VehicleLocation> elementos = null;

		String queryStr =  "from VehicleLocation where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		elementos = query.list();
		return elementos;
	}

	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from VehicleLocation")
				.uniqueResult();
	}

	//	
	//	select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, greatest(numeroEventos,0)  as numeroEventos
	//	from
	//	(
	//	  select v.timestamp::date, count(*) as numeroEventos
	//	  from VehicleLocation v 
	//	  where (v.timestamp > '2015-07-01' and v.timestamp < '2016-03-01')
	//	  group by v.timestamp::date
	//	) as eventos
	//	right join
	//	(
	//	  select '2015-07-01'::date + s.a AS generateddate 
	//	  from generate_series(0,('2016-03-01'::date - '2015-07-01'::date),1) as s(a)
	//	) as todoslosdias 
	//	on eventos.timestamp::date = generateddate
	//	order by anio, mes, dia

	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.timestamp as date), count(*) as neventos "
				+ " from VehicleLocation v "
				+ "	where (v.timestamp > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.timestamp < :fechaFin ) "
				+ "  group by cast(v.timestamp as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timestamp as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}

	@Override
	public Long contarVehicleLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds){

		String queryStr =  "select count(*) from VehicleLocation where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<VehicleLocationDTO> obterVehicleLocationsWithLimit(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, Integer limit){

		List<VehicleLocationDTO> elementos = null;
		int significantDecimals = GeomUtil.computeSignificantDecimals(bounds);
		
		String queryStr = "select last(id) as id, last(timestamp) as timestamp, last(position) as position, "
				+ "(select sourceid from usuario_movil where id = last(idUsuarioMovil)) as \"userId\", "
				+ "last(accuracy) as accuracy, last(speed) as speed, last(rr) as rr "
				+ "from vehicleLocation where st_within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin ";
		queryStr += "group by round(cast(st_x(position) as numeric), " + significantDecimals + "), round(cast(st_y(position) as numeric), " + significantDecimals + ") ";

		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("timestamp", CalendarType.INSTANCE);
		query.addScalar("position", GeometryType.INSTANCE);		
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("accuracy", DoubleType.INSTANCE);
		query.addScalar("speed", DoubleType.INSTANCE);
		query.addScalar("rr", DoubleType.INSTANCE);
		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(VehicleLocationDTO.class));
		elementos = query.list();
		
		return elementos;
	}
	
	@Override
	public AggregateMeasurementVO getAggregateValue(Double lat, Double lon, Integer day, Integer time){
		String queryStr =  "select count(*) as \"numberOfValues\", " +
								"max(speed) as max, " + 
								"min(speed) as min, " +
								"avg(speed) as average, " + 
								"stddev(speed) as \"standardDeviation\" " +
							"from vehicleLocation " +
							"where st_distance(position, st_geometryfromtext('POINT('|| :lon || ' ' ||:lat ||')', 4326), true) < 10 " +
							"and EXTRACT(DOW FROM timestamp) = :day " +
							"and EXTRACT(HOUR FROM timestamp) = :hora";
		
		Query query = getSession().createSQLQuery(queryStr);
		query.setResultTransformer(Transformers.aliasToBean(AggregateMeasurementVO.class));
		
		query.setParameter("lon", lon);
		query.setParameter("lat", lat);
		query.setParameter("day", day);
		query.setParameter("hora", time);
		
		AggregateMeasurementVO resultado = (AggregateMeasurementVO) query.uniqueResult();

		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<GroupedDTO> obterVehicleLocationsGrouped(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds,int startIndex, Integer numberOfCells){
		
		List<GroupedDTO> elementos = null;
		
		String gridQuery = "select (ST_Dump(makegrid_2d(:bounds, :numberOfCells))).geom";
        String vehicleLocationQuery = "select * from vehiclelocation where st_within(position, :bounds) and timestamp > :fechaIni and timestamp < :fechaFin";

        if(idUsuario!=null)
            vehicleLocationQuery += " and idUsuarioMovil = :idUsuario";
	
        String queryStr = "select st_centroid(st_union(position)) as geom, count(*) as count " 
                + "from " 
                + "("+gridQuery+") as grid " 
                + "left join " 
                + "(" + vehicleLocationQuery + ") as vehiclelocation " 
                + "on (position && geom) " 
                + "group by geom " 
                + "having count(*) > 1 ";
        
        
		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("geom", GeometryType.INSTANCE);  
		query.addScalar("count", IntegerType.INSTANCE);
		
		query.setParameter("bounds", bounds);
		query.setParameter("numberOfCells", numberOfCells);
		
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		
		if(startIndex!=-1)
            query.setFirstResult(startIndex);
		
		query.setResultTransformer(Transformers.aliasToBean(GroupedDTO.class));
        
		elementos = query.list();
		
        return elementos;        
	}
}
