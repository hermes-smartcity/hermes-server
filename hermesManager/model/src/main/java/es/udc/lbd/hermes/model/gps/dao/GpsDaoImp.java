package es.udc.lbd.hermes.model.gps.dao;

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

import es.udc.lbd.hermes.model.gps.Gps;
import es.udc.lbd.hermes.model.gps.GpsDTO;
import es.udc.lbd.hermes.model.util.GeomUtil;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class GpsDaoImp extends GenericDaoHibernate<Gps, Long> implements GpsDao{

	public Long contarGpsLocations(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds){
		
		String queryStr =  "select count(*) from Gps where  within(position, :bounds) = true ";
		if(idUsuario!=null)
			queryStr += "and usuarioMovil.id = :idUsuario ";

		if(fechaIni!=null)
			queryStr += "and time > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and time < :fechaFin";

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
	public List<GpsDTO> obterGpsLocationWithLimit(Long idUsuario, Calendar fechaIni,
			Calendar fechaFin, Geometry bounds, int startIndex, Integer limit){
		
		List<GpsDTO> elementos = null;
		int significantDecimals = GeomUtil.computeSignificantDecimals(bounds);
				
		String queryStr = "select last(id) as id, last(time) as time, last(position) as position, "
				+ "(select sourceid from usuario_movil where id = last(idUsuarioMovil)) as \"userId\", "
				+ "last(accuracy) as accuracy, last(speed) as speed, " 
				+ "last(bearing) as bearing, last(altitude) as altitude, "
				+ "last(provider) as provider " 
				+ "from gps where st_within(position, :bounds) = true ";
		
		if(idUsuario!=null)
			queryStr += "and idUsuarioMovil = :idUsuario ";


		if(fechaIni!=null)
			queryStr += "and time > :fechaIni ";

		if(fechaFin!=null)
			queryStr += "and time < :fechaFin ";
		
		queryStr += "group by round(cast(st_x(position) as numeric), " + significantDecimals + "), round(cast(st_y(position) as numeric), " + significantDecimals + ") ";

		SQLQuery query = getSession().createSQLQuery(queryStr);
		query.addScalar("id", LongType.INSTANCE);
		query.addScalar("time", CalendarType.INSTANCE);
		query.addScalar("position", GeometryType.INSTANCE);		
		query.addScalar("userId", StringType.INSTANCE);
		query.addScalar("accuracy", DoubleType.INSTANCE);
		query.addScalar("speed", DoubleType.INSTANCE);
		query.addScalar("bearing", StringType.INSTANCE);
		query.addScalar("altitude", DoubleType.INSTANCE);
		query.addScalar("provider", StringType.INSTANCE);
		
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
	
		query.setResultTransformer(Transformers.aliasToBean(GpsDTO.class));
		
		elementos = query.list();
		return elementos;
	}
}
