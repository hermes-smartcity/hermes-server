package es.udc.lbd.hermes.model.events.measurement.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.measurement.Measurement;
import es.udc.lbd.hermes.model.events.measurement.MeasurementType;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;
@Repository
public class MeasurementDaoImpl extends GenericDaoHibernate<Measurement, Long> implements
MeasurementDao {
	@Override
	public List<Measurement> obterMeasurementsSegunTipo(MeasurementType tipo, Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds,
			int startIndex, int count) {
		
			List<Measurement> elementos = null;
			
			String queryStr =  "from Measurement where  within(position, :bounds) = true ";
			if(idUsuario!=null)
				queryStr += "and usuarioMovil.id = :idUsuario";
			
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
	
	@Override
	public List<EventosPorDia> eventosPorDia(MeasurementType tipo,Long idUsuario, Calendar fechaIni, Calendar fechaFin) {

		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
					+ " (select cast(m.timestamp as date), count(*) as neventos "
					+ " from Measurement m "
					+ "	where (m.timestamp > :fechaIni "; 
					
					queryStr += " and tipo LIKE :tipo ";
		
					if(idUsuario!=null)
						queryStr += " and m.usuarioMovil.id = :idUsuario ";
				
					queryStr += " and m.timestamp < :fechaFin ) "
					+ "  group by cast(m.timestamp as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timestamp as date) = generateddate order by anio, mes, dia";

		
		SQLQuery query = getSession().createSQLQuery(queryStr);
		
//		String queryStr="select extract(day from m.timestamp) as dia, extract(month from m.timestamp) as mes, "
//				+ "extract(year from m.timestamp) as anio, count(*) as numeroEventos" +
//				" from Measurement m where m.timestamp > :fechaIni and m.timestamp < :fechaFin ";
//	
//		queryStr += " and tipo LIKE :tipo ";
//		
//		if(idUsuario!=null)
//			queryStr += "and m.usuarioMovil.id = :idUsuario ";
//		
//		queryStr+="group by extract(day from m.timestamp), extract(month from m.timestamp), "
//				+ "extract (year from m.timestamp) order by anio, mes, dia";
//		
//		Query query = getSession().createQuery(queryStr);
		
		if(idUsuario!=null)
			 query.setParameter("idUsuario", idUsuario);
		
		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);
		query.setString("tipo", tipo.getName());
		
		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}

}
