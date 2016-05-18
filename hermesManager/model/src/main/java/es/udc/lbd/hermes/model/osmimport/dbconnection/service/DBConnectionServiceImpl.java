package es.udc.lbd.hermes.model.osmimport.dbconnection.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.dao.DBConnectionDao;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;

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
		
		try {
			dbConnection.setPassDb(CifrarDescifrarUtil.cifra(dbConnection.getPassDb()));
			dbConnectionDao.create(dbConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dbConnection;
	}
	
	@Transactional(readOnly = true)
	public DBConnection get(Long id){
		return dbConnectionDao.get(id);
	}

	public DBConnection update(DBConnection dbConnection, Long id){
		try{
			//Comparamos si la contrasena indicada coincide con la anterior.
			//Si no ha cambiado, no se modifica la contraseña
			//Si ha cambiado, se modificada
			DBConnection dbConnectionDB = dbConnectionDao.get(dbConnection.getId());
			String passwordDB = dbConnectionDB.getPassDb();
			String passwordNew = dbConnection.getPassDb();
			
			if (passwordNew.equals(passwordDB)){
				//No ha cambiado, se mantiene la antigua (que ya estaba encriptada)
				dbConnection.setPassDb(dbConnectionDB.getPassDb());
			}else{
				//Se encripta la nueva
				dbConnection.setPassDb(CifrarDescifrarUtil.cifra(dbConnection.getPassDb()));
	
			}
			
			dbConnectionDao.update(dbConnection);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return dbConnection;
	}
	
	
}
