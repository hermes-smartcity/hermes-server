package es.udc.lbd.hermes.model.events.userlocations.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.hibernate.spatial.GeometryType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.CalendarType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.EventosPorDia;
import es.udc.lbd.hermes.model.events.GroupedDTO;
import es.udc.lbd.hermes.model.events.userlocations.UserLocationDTO;
import es.udc.lbd.hermes.model.events.userlocations.UserLocations;
import es.udc.lbd.hermes.model.util.GeomUtil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class UserLocationsDaoImpl extends GenericDaoHibernate<UserLocations, Long> implements UserLocationsDao{

	@SuppressWarnings("unchecked")
	public List<UserLocations> obterUserLocations(){
		return getSession().createCriteria(this.entityClass).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLocations> obterUserLocationsSegunUsuario(Long idUsuario){
		try {
			return getSession().createCriteria(this.entityClass).add(Restrictions.eq("usuarioMovil.id", idUsuario)).list();
		} catch (HibernateException e) {
			throw SessionFactoryUtils.convertHibernateAccessException(e);
		}	
	}
		
	public long contar(){
		return (Long) getSession()
				.createQuery("select count(*) from UserLocations")
				.uniqueResult();
	}
	
	public Long contarUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds){

		String queryStr =  "select count(*) from UserLocations where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin ";

		Query query = getSession().createQuery(queryStr);

		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		Long numero  = (Long) query.uniqueResult();

		return numero;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLocationDTO> obterUserLocationsWithLimit(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, Integer limit){

		List<UserLocationDTO> elementos = null;
		int significantDecimals = GeomUtil.computeSignificantDecimals(bounds);
		
		String queryStr = "select last(id) as id, last(startTime) as \"startTime\", "
				+ "last(endTime) as \"endTime\", last(position) as position, "
				+ "(select sourceid from usuario_movil where id = last(idUsuarioMovil)) as \"userId\", "
				+ "last(accuracy) as accuracy "
				+ "from userLocations where st_within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin ";
		queryStr += "group by round(cast(st_x(position) as numeric), " + significantDecimals + "), round(cast(st_y(position) as numeric), " + significantDecimals + ") ";

		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("startTime", CalendarType.INSTANCE);
		query.addScalar("endTime", CalendarType.INSTANCE);
		query.addScalar("position", GeometryType.INSTANCE);		
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("accuracy", DoubleType.INSTANCE);
		query.setParameter("bounds", bounds);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		if(startIndex!=-1)
            query.setFirstResult(startIndex);
     
		if(limit!=-1)                                
            query.setMaxResults(limit);

		query.setResultTransformer(Transformers.aliasToBean(UserLocationDTO.class));
		elementos = query.list();
		
		return elementos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EventosPorDia> eventosPorDia(Long idUsuario, Calendar fechaIni, Calendar fechaFin) {
		String queryStr = "select extract(day from generateddate) as dia, extract(month from generateddate) as mes, extract (year from generateddate) as anio, "
				+ "greatest(neventos, 0)  as neventos from "
				+ " (select cast(v.startTime as date), count(*) as neventos "
				+ " from UserLocations v "
				+ "	where (v.startTime > :fechaIni "; 

		if(idUsuario!=null)
			queryStr += " and v.idusuariomovil = :idUsuario ";

		queryStr += " and v.endTime < :fechaFin ) "
				+ "  group by cast(v.startTime as date)) as eventos right join "
				+ "(select cast(:fechaIni as date) + s AS generateddate from generate_series(0,(cast(:fechaFin as date) - cast(:fechaIni as date)),1) as s) as todoslosdias "
				+ " on cast(eventos.startTime as date) = generateddate order by anio, mes, dia";


		SQLQuery query = getSession().createSQLQuery(queryStr);

		if(idUsuario!=null)
			query.setParameter("idUsuario", idUsuario);

		query.setCalendar("fechaIni", fechaIni);
		query.setCalendar("fechaFin", fechaFin);

		query.setResultTransformer(Transformers.aliasToBean(EventosPorDia.class));
		return (List<EventosPorDia>) query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<UserLocations> obterUserLocations(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){

		List<UserLocations> elementos = null;

		String queryStr =  "from UserLocations where within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		queryStr += "and startTime > :fechaIni ";
		queryStr += "and endTime < :fechaFin";

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
	
	@SuppressWarnings("unchecked")
	public List<GroupedDTO> obterUserLocationsGrouped(Long idUsuario, Calendar fechaIni,Calendar fechaFin, Geometry bounds,int startIndex, Integer numberOfCells){
		List<GroupedDTO> elementos = null;
		
		String gridQuery = "select (ST_Dump(makegrid_2d(:bounds, :numberOfCells))).geom";
        String userLocationsQuery = "select * from userLocations where st_within(position, :bounds) and startTime > :fechaIni and endTime < :fechaFin";

        if(idUsuario!=null)
        	userLocationsQuery += " and idUsuarioMovil = :idUsuario";
	
        String queryStr = "select st_centroid(st_union(position)) as geom, count(*) as count " 
                + "from " 
                + "("+gridQuery+") as grid " 
                + "left join " 
                + "(" + userLocationsQuery + ") as userLocations " 
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
	
	public void delete(Long idUsuario, Calendar starttime){
		String queryStr =  "DELETE from UserLocations WHERE usuarioMovil.id = :idUsuario ";
		queryStr += "and extract(day from cast(starttime as date)) = extract(day from cast(:starttime as date)) ";
		
		Query query = getSession().createQuery(queryStr);

		query.setParameter("idUsuario", idUsuario);
		query.setCalendar("starttime", starttime);
		
		query.executeUpdate();
	}
}
