package es.udc.lbd.hermes.model.osmimport.dbconnection.service;

import java.util.List;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;

public interface DBConnectionService {

	public List<DBConnection> getDBConnections();
	
	public void delete(Long id); 
	
	public DBConnection register(DBConnection dbConnection);
	
	public DBConnection get(Long id);
	
	public DBConnection update(DBConnection dbConnection, Long id);
	
}
