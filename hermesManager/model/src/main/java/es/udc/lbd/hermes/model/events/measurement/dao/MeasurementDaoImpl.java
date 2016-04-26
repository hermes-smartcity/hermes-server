package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementDTO;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.smartdriver.Type;
import es.udc.lbd.hermes.model.util.GeomUtil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
@Repository
public class MeasurementDaoImpl extends GenericDaoHibernate<Measurement, Long> implements
MeasurementDao {
	@SuppressWarnings("unchecked")
	@Override
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds,
			int startIndex, int count) {

		List<Measurement> elementos = null;

		String queryStr =  "from Measurement where  within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += " and tipo LIKE :tipo ";

		if(fechaIni!=null)
			queryStr += "and timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and timestamp < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		if(fechaIni!=null)
			query.setCalendar("fechaIni", fechaIni);
		if(fechaFin!=null)
			query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		query.setString("tipo", tipo.getName());
		elementos = query.list();
		return elementos;

	}

	@Override
	public long contar(MeasurementType tipo) {
		String queryStr = "select count(*) from Measurement";
		if(tipo!=null)
			queryStr += "where tipo LIKE :tipo";

		Query query = getSession().createQuery(queryStr);
		if(tipo!=null)
			query.setString("tipo", tipo.getName());
		return (long) query.uniqueResult();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin) {

		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(m.timestamp as date), count(*) as neventos "
				+ " from Measurement m "
				+ "	where (m.timestamp > :fechaIni "; 

		queryStr += " and tipo LIKE :tipo ";

		if(idUsuario!=null)
			queryStr += " and m.idusuariomovil = :idUsuario ";

		queryStr += " and m.timestamp < :fechaFin ) "
				+ "  group by cast(m.timestamp as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timestamp as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setString("tipo", tipo.getName());

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}

	@Override
	public Long contarMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds) {

		String queryStr =  "select count(*) from Measurement where  within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += " and tipo LIKE :tipo ";

		if(fechaIni!=null)
			queryStr += "and timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and timestamp < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		if(fechaIni!=null)
			query.setCalendar("fechaIni", fechaIni);
		if(fechaFin!=null)
			query.setCalendar("fechaFin", fechaFin);

		query.setString("tipo", tipo.getName());

		Long numero  = (Long) query.uniqueResult();

		return numero;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MeasurementDTO> obterMeasurementsSegunTipoWithLimit(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, Integer limit) {

		List<MeasurementDTO> elementos = null;
		int significantDecimals = GeomUtil.computeSignificantDecimals(bounds);
				
		String queryStr = "select last(id) as id, last(timestamp) as timestamp, last(position) as position, "
				+ "(select sourceid from usuario_movil where id = last(idUsuarioMovil)) as \"userId\", "
				+ "last(accuracy) as accuracy, last(speed) as speed, " 
				+ "last(tipo) as tipo, last(value) as value "  
				+ "from measurement where st_within(position, :bounds) = true ";
		
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += " and tipo LIKE :tipo ";

		if(fechaIni!=null)
			queryStr += "and timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and timestamp < :fechaFin ";
		
		queryStr += "group by round(cast(st_x(position) as numeric), " + significantDecimals + "), round(cast(st_y(position) as numeric), " + significantDecimals + ") ";

		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("timestamp", CalendarType.INSTANCE);
		query.addScalar("position", GeometryType.INSTANCE);		
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("accuracy", DoubleType.INSTANCE);
		query.addScalar("speed", DoubleType.INSTANCE);
		query.addScalar("tipo", StringType.INSTANCE);
		query.addScalar("value", DoubleType.INSTANCE);
		
		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		if(fechaIni!=null)
			query.setCalendar("fechaIni", fechaIni);
		if(fechaFin!=null)
			query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
			query.setFirstResult(startIndex);

		if(limit!=-1)                                
			query.setMaxResults(limit);

		query.setString("tipo", tipo.getName());
		
		query.setResultTransformer(Transformers.aliasToBean(MeasurementDTO.class));
		
		elementos = query.list();
		return elementos;

	}
	
	@Override
	public AggregateMeasurementVO getAggregateValue(Type type, Double lat, Double lon, Integer day, Integer time){
		String queryStr =  "select count(*) as \"numberOfValues\", " +
								"max(value) as max, " + 
								"min(value) as min, " +
								"average(value) as average, " + 
								"stddev(value) as \"standardDeviation\" " +
							"from measurement " +
							"where st_distance(position, st_geometryfromtext('POINT('|| :lon || ' ' ||:lat ||')', 4326), true) < 10 " +
							"and tipo LIKE :type " +
							"and EXTRACT(DOW FROM timestamp) = :day " +
							"and EXTRACT(HOUR FROM timestamp) = :hora";
		
		Query query = getSession().createSQLQuery(queryStr);
		query.setResultTransformer(Transformers.aliasToBean(AggregateMeasurementVO.class));
		
		query.setParameter("lon", lon);
		query.setParameter("lat", lat);
		query.setParameter("type", type.getName());
		query.setParameter("day", day);
		query.setParameter("hora", time);
		
		AggregateMeasurementVO resultado = (AggregateMeasurementVO) query.uniqueResult();

		return resultado;
	}
}
