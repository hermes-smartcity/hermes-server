package es.udc.lbd.hermes.model.events.log.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.events.log.Log;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;


@Repository
public class LogDaoImpl extends GenericDaoHibernate<Log, Long> implements
LogDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Log> obterLogs(String level, Calendar fechaIni, Calendar fechaFin, int startIndex, int count){

		List<Log> elementos = null;

		String queryStr =  "from Log where level = :level ";
		queryStr += "and dated > :fechaIni ";
		queryStr += "and dated < :fechaFin";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("level", level);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1 && count!=-1)
			query.setFirstResult(startIndex).setMaxResults(count);

		elementos = query.list();
		return elementos;
	}


}
