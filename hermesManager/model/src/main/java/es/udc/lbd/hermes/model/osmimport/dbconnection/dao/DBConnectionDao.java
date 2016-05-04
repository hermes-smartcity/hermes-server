package es.udc.lbd.hermes.model.osmimport.dbconnection.dao;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.util.dao.GenericDao;

public interface DBConnectionDao extends GenericDao<DBConnection, Long>{

	public List<DBConnection> getAll();
}
