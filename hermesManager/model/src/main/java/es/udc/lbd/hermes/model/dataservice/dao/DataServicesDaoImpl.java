package es.udc.lbd.hermes.model.dataservice.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.dataservice.DataServices;
import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class DataServicesDaoImpl extends GenericDaoHibernate<DataServices, Long> implements DataServicesDao{

	@SuppressWarnings("unchecked")
	public List<DataServices> obterDataService(){
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<EventosPorDia> peticionesPorDia(String service, String method, Calendar fechaIni, Calendar fechaFin){
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.timelog as date), count(*) as neventos "
				+ " from DataServices v "
				+ "	where (v.timelog > :fechaIni "; 

		if(service!=null && !service.equals("undefined"))
			queryStr += " and v.service = :service ";
		
		if(method!=null && !method.equals("undefined"))
			queryStr += " and v.method = :method ";

		queryStr += " and v.timelog < :fechaFin ) "
				+ "  group by cast(v.timelog as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.timelog as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(service!=null && !service.equals("undefined"))
			query.setParameter("service", service);
		
		if(method!=null && !method.equals("undefined"))
			query.setParameter("method", method);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	private String obtainNameService(String nameOriginal){
		return null;
	}
}
