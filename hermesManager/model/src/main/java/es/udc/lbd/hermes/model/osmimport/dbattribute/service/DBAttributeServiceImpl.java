package es.udc.lbd.hermes.model.osmimport.dbattribute.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeDTO;
import es.udc.lbd.hermes.model.osmimport.dbattribute.dao.DBAttributeDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconcept.dao.DBConceptDao;

@Service("dbAttributeService")
@Transactional
public class DBAttributeServiceImpl implements DBAttributeService{

	@Autowired
	private DBAttributeDao dbAttributeDao;
	
	@Autowired
	private DBConceptDao dbConceptDao;
	
	@Transactional(readOnly = true)
	public List<DBAttribute> getAll(Long idDbConcept){
		return dbAttributeDao.getAll(idDbConcept);
	}
	
	public void delete(Long id){
		dbAttributeDao.delete(id);
	}
	
	public DBAttribute register(DBAttributeDTO dbAttributeDto){
		
		//Recuperamos el dbConcept
		DBConcept dbConcept = dbConceptDao.get(dbAttributeDto.getDbConcept());
		
		DBAttribute dbAttribute = new DBAttribute(dbAttributeDto.getId(), 
				dbAttributeDto.getAttributeName(), dbAttributeDto.getAttributeType(), dbConcept);
		
		dbAttributeDao.create(dbAttribute);
		
		return dbAttribute;
	}
	
	@Transactional(readOnly = true)
	public DBAttribute get(Long id){
		return dbAttributeDao.get(id);
	}

	public DBAttribute update(DBAttributeDTO dbAttributeDto, Long id){
		DBConcept dbConcept = dbConceptDao.get(dbAttributeDto.getDbConcept());
		
		DBAttribute dbAttribute = new DBAttribute(dbAttributeDto.getId(), 
				dbAttributeDto.getAttributeName(), dbAttributeDto.getAttributeType(), dbConcept);
		
		dbAttributeDao.update(dbAttribute);
		
		return dbAttribute;
	}
}
