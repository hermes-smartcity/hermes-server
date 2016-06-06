package es.udc.lbd.hermes.model.osmimport.dbconcept.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Geometry;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.dao.DBAttributeDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconcept.dao.DBConceptDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.dao.DBConceptJDBCDao;
import es.udc.lbd.hermes.model.util.HelpersModel;

@Service("dbConceptService")
@Transactional
public class DBConceptServiceImpl implements DBConceptService{

	@Autowired
	private DBConceptDao dbConceptDao;
	
	@Autowired
	private DBAttributeDao dbAttributeDao;
	
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
	
	@Transactional(readOnly = true)
	public String recuperaGeojsonDdConcept(Long dbconceptId, Double nwLng, Double nwLat, Double seLng, Double seLat) throws Exception{
		
		Geometry polygon =  HelpersModel.prepararPoligono(nwLng, nwLat, seLng, seLat);
		DBConcept dbConcept = dbConceptDao.get(dbconceptId);
		
		//Recuperamos la lista de atributos asociados al dbconcept
		List<DBAttribute> dbAttributes = dbAttributeDao.getAll(dbConcept.getId());
		List<String> atributos = new ArrayList<String>();
		atributos.add(dbConcept.getOsmIdName());
		for (DBAttribute dbAttribute : dbAttributes) {
			String nombre = dbAttribute.getAttributeName();
			atributos.add(nombre);
		}
		
		DBConceptJDBCDao dbConceptJdbc = new DBConceptJDBCDao(dbConcept.getDbConnection());
		String geojson = dbConceptJdbc.consultarTabla(dbConcept.getSchemaName(), dbConcept.getTableName(), dbConcept.getGeomName(), polygon, atributos);
		
		return geojson;
	}
	
}
