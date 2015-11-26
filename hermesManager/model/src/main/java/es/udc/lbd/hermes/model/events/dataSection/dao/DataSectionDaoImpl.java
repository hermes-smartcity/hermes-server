package es.udc.lbd.hermes.model.events.dataSection.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;



@Repository
public class DataSectionDaoImpl extends GenericDaoHibernate<DataSection, Long> implements
DataSectionDao {
	
	@Override
	public List<DataSection> obterDataSections(Long idUsuario, Calendar fechaIni, Calendar fechaFin, Geometry bounds,
			int startIndex, int count){
		List<DataSection> elementos = null;
		
				String queryStr =  "from DataSection where within(roadSection, :bounds) = true ";
				
				if(idUsuario!=null)
					queryStr += "and usuario.id = :idUsuario ";
				
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
				
				elementos = query.list();
				return elementos;
	}
	
	@Override
	public long contar() {
		return (Long) getSession()
				.createQuery("select count(*) from DataSection")
				.uniqueResult();
	}

}
