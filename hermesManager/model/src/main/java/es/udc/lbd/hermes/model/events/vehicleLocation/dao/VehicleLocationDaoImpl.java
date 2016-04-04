package es.udc.lbd.hermes.model.events.vehicleLocation.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.vehicleLocation.VehicleLocation;
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
	public List<VehicleLocation> obterVehicleLocationsWithLimit(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count, Integer limit){

		List<VehicleLocation> elementos = null;

		String queryStr =  "from VehicleLocation where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and timestamp > :fechaIni ";
		queryStr += "and timestamp < :fechaFin ";

		if(limit!=null)
			queryStr += "ORDER BY timestamp DESC LIMIT :limit ";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(limit!=null)
			query.setInteger("limit", limit);

		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		elementos = query.list();
		return elementos;
	}
}
