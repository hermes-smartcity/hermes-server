package es.udc.lbd.hermes.model.events.dataSection.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.smartdriver.AggregateMeasurementVO;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class DataSectionDaoImpl extends GenericDaoHibernate<DataSection, Long> implements
DataSectionDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<DataSection> obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){
		List<DataSection> elementos = null;

		String queryStr =  "from DataSection d where intersects(roadSection, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and d.usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and d.timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and d.timestamp < :fechaFin";

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

		elementos = query.list();
		return elementos;
	}

	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from DataSection")
				.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(d.timestamp as date), count(*) as neventos "
				+ " from DataSection d "
				+ "	where (d.timestamp > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and d.idusuariomovil = :idUsuario ";

		queryStr += " and d.timestamp < :fechaFin ) "
				+ "  group by cast(d.timestamp as date)) as eventos right join "
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
	public Long contarDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds){

		String queryStr =  "select count(*) from DataSection d where intersects(roadSection, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and d.usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and d.timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and d.timestamp < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);
		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);
		if(fechaIni!=null)
			query.setCalendar("fechaIni", fechaIni);
		if(fechaFin!=null)
			query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DataSection> obterDataSectionsWithLimit(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, Integer limit){
		List<DataSection> elementos = null;

		String queryStr =  "from DataSection d where intersects(roadSection, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and d.usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and d.timestamp > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and d.timestamp < :fechaFin";

		Query query = getSession().createQuery(queryStr);

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

		elementos = query.list();
		return elementos;
	}
	
	@Override
	public AggregateMeasurementVO getAggregateValue(String campo, Double lat, Double lon, Integer day, Integer time){
		String queryStr =  "select count(*) as \"numberOfValues\", " +
				"max(" + campo + ") as max, " + 
				"min(" + campo + ") as min, " +
				"avg(" + campo + ") as average, " + 
				"stddev(" + campo + ") as \"standardDeviation\" " +
			"from dataSection " +
			"where st_distance(roadSection, st_geometryfromtext('POINT('|| :lon || ' ' ||:lat ||')', 4326)) < 10 " +
			"and EXTRACT(DOW FROM timestamp) = :day " +
			"and EXTRACT(HOUR FROM timestamp) = :hora";

		Query query = getSession().createSQLQuery(queryStr);
		query.setResultTransformer(Transformers.aliasToBean(AggregateMeasurementVO.class));
		
		//query.setParameter("campo", campo);
		query.setParameter("lon", lon);
		query.setParameter("lat", lat);
		query.setParameter("day", day);
		query.setParameter("hora", time);
		
		AggregateMeasurementVO resultado = (AggregateMeasurementVO) query.uniqueResult();
		
		
		return resultado;
	}

}
