package es.udc.lbd.hermes.model.osmimport.dbconnection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.dao.DBConnectionDao;

@Service("dbConnectinService")
@Transactional
public class DBConnectionServiceImpl implements DBConnectionService{

	@Autowired
	private DBConnectionDao dbConnectionDao;
	
	@Transactional(readOnly = true)
	public List<DBConnection> getDBConnections(){
		return dbConnectionDao.getAll();
	}
	
	public void delete(Long id){
		dbConnectionDao.delete(id);
	}
	
	public DBConnection register(DBConnection dbConnection){
		dbConnectionDao.create(dbConnection);
		
		return dbConnection;
	}
	
	@Transactional(readOnly = true)
	public DBConnection get(Long id){
		return dbConnectionDao.get(id);
	}

	public DBConnection update(DBConnection dbConnection, Long id){
		dbConnectionDao.update(dbConnection);
		
		return dbConnection;
	}
	
	
}
