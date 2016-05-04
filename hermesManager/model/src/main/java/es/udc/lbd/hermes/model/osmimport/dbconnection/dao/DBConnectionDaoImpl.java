package es.udc.lbd.hermes.model.osmimport.dbconnection.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.util.dao.GenericDaoHibernate;

@Repository
public class DBConnectionDaoImpl extends GenericDaoHibernate<DBConnection, Long> implements DBConnectionDao{

	@SuppressWarnings("unchecked")
	public List<DBConnection> getAll(){
		return getSession().createCriteria(this.entityClass).list();
	}
}
