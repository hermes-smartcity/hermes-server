package es.udc.lbd.hermes.model.osmimport.dbconcept.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconcept.dao.DBConceptDao;

@Service("dbConceptService")
@Transactional
public class DBConceptServiceImpl implements DBConceptService{

	@Autowired
	private DBConceptDao dbConceptDao;
	
	@Transactional(readOnly = true)
	public List<DBConcept> getAll(){
		return dbConceptDao.getAll();
	}
	
	public void delete(Long id){
		dbConceptDao.delete(id);
	}
	
	public DBConcept register(DBConcept dbConcept){
		dbConceptDao.create(dbConcept);
		
		return dbConcept;
	}
	
	@Transactional(readOnly = true)
	public DBConcept get(Long id){
		return dbConceptDao.get(id);
	}

	public DBConcept update(DBConcept dbConcept, Long id){
		dbConceptDao.update(dbConcept);
		
		return dbConcept;
	}
}
