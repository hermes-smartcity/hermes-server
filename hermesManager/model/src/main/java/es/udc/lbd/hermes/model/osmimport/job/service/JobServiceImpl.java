package es.udc.lbd.hermes.model.osmimport.job.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.dao.ConceptTransformationDao;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeType;
import es.udc.lbd.hermes.model.osmimport.dbattribute.dao.DBAttributeDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.ExecutionStatus;
import es.udc.lbd.hermes.model.osmimport.execution.dao.ExecutionDao;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;
import es.udc.lbd.hermes.model.osmimport.job.dao.JobDao;
import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.dao.MessageDao;
import es.udc.lbd.hermes.model.testExitsTableQuery.dao.TestExitsTableQueryDao;

@Service("jobService")
@Transactional
public class JobServiceImpl implements JobService{

	@Autowired
	public MessageSource messageSource;
	
	@Autowired
	private JobDao jobDao;
	
	@Autowired
	private ExecutionDao executionDao;
	
	@Autowired
	private MessageDao messageDao;
	
	@Autowired
	private ConceptTransformationDao conceptTransformationDao;
	
	@Autowired
	private TestExitsTableQueryDao testExitsTableQueryDao;
		
	@Autowired
	private DBAttributeDao dbAttributeDao;
	
	@Transactional(readOnly = true)
	public List<Job> getAll(){
		return jobDao.getAll();
	}
	
	public void delete(Long id){
		jobDao.delete(id);
	}
	
	public void register(JobDTO jobDto){
		
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
		Polygon polygon = (Polygon)fact.toGeometry(new Envelope(jobDto.getSeLng(), jobDto.getNwLng(), jobDto.getSeLat(), jobDto.getNwLat()));
		
		Job job = new Job(jobDto.getName(), polygon);
		
		jobDao.create(job);
		
	}
	
	@Transactional(readOnly = true)
	public Job get(Long id){
		return jobDao.get(id);
	}

	public void update(JobDTO jobDto, Long id){
		
		GeometryFactory fact = new GeometryFactory(new PrecisionModel(), 4326);
		Polygon polygon = (Polygon)fact.toGeometry(new Envelope(jobDto.getSeLng(), jobDto.getNwLng(), jobDto.getSeLat(), jobDto.getNwLat()));
		
		Job job = new Job(jobDto.getId(), jobDto.getName(), polygon);
		
		jobDao.update(job);
		
	}
	
	public Execution createExecution(Long idJob, Locale locale){
		//Recuperamos el job
		Job job = jobDao.get(idJob);
		
		//Creamos el objeto execution
		Calendar timestamp = Calendar.getInstance();
		Execution execution =  new Execution(ExecutionStatus.RUNNING, timestamp, job);
		
		executionDao.create(execution);
		
		//Creamos el mensaje inicial asociado a la ejecucion
		String mensaje = messageSource.getMessage("executionjob.initiating", null, locale);
		Message message = new Message(mensaje, timestamp, execution);
		messageDao.create(message);
		
		return execution;
	}
	
	public void launchExecuteJob(Long idJob, Long idExecution, Locale locale){
		
		//Recuperamos la lista de conceptTransformations asociados al trabajo
		List<ConceptTransformation> listaCT = conceptTransformationDao.getAll(idJob);
		if (listaCT.size() == 0){
			//si no hay valores, insertamos mensaje informando de que no hay concepttransformations
			//cambiamos el estado de la ejecución (y la fecha)
			Calendar timestamp = Calendar.getInstance();
			Execution execution = executionDao.get(idExecution);
			
			//Creamos el mensaje
			String mensaje = messageSource.getMessage("executionjob.noconcepttransformation", null, locale);
			
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
			
			//Actualizamos el estado y la fecha de la ejecucion
			execution.setStatus(ExecutionStatus.SUCCESS);
			execution.setTimestamp(timestamp);
			executionDao.update(execution);
			
		}else{
			Execution execution = executionDao.get(idExecution);
			
			//Comprobar que todas las tablas con su esquema indicado en dbconcept existen
			Boolean todasTablasColumnasExisten = comprobarTablasColumnasDBConcepts(listaCT, execution, locale);
			
			//Si todas las tablas existen, comprobamos que los atributos de las tablas indicadas
			//en dbattribute existen
			if (todasTablasColumnasExisten){
				
				for (ConceptTransformation conceptTransformation : listaCT) {
					//Obtener de OpenStreetMap los datos descritos en el OSMConcept que caigan dentro del bbox
					//de ConceptTransformation (o de Job, en caso de no existir el de ConceptTransformation)
					Polygon geom = conceptTransformation.getBbox();
					if (geom == null){
						Job job = jobDao.get(idJob);
						geom = job.getBbox();
					}

					//TODO: obtener
					
				}
				
				//Al terminar, creamos mensaje indicando el numero de ConceptTransformation ejecutadas
				Calendar timestamp = Calendar.getInstance();

				Object [] parametros = new Object[] {listaCT.size()};
				String mensaje = messageSource.getMessage("executionjob.numbertransformations", parametros, locale);
				Message message = new Message(mensaje, timestamp, execution);
				messageDao.create(message);

				//Actualizamos el estado y la fecha de la ejecucion
				execution.setStatus(ExecutionStatus.SUCCESS);
				execution.setTimestamp(timestamp);
				executionDao.update(execution);
				
			}else{
				//Actualizamos el estado y la fecha de la ejecucion
				Calendar timestamp = Calendar.getInstance();
				execution.setStatus(ExecutionStatus.ERROR);
				execution.setTimestamp(timestamp);
				executionDao.update(execution);
			}
		}
	}
	
	private Boolean comprobarTablasColumnasDBConcepts(List<ConceptTransformation> listado, Execution execution, Locale locale){
		Boolean todasTablasColumnasExisten = true;
		
		for (ConceptTransformation conceptTransformation : listado) {
			DBConcept dbConcept = conceptTransformation.getDbConcept();
			String schema = dbConcept.getSchemaName();
			String table = dbConcept.getTableName();
			
			if (schema == null){
				schema = "public";
			}
			Boolean existe = testExitsTableQueryDao.exitsSchemaTable(schema, table);
			
			if (!existe){
				Calendar timestamp = Calendar.getInstance();
								
				//Creamos el mensaje
				Object [] parametros = new Object[] {conceptTransformation.getId(), dbConcept.getName(), schema, table};
				String mensaje = messageSource.getMessage("executionjob.schematable", parametros, locale);
				Message message = new Message(mensaje, timestamp, execution);
				messageDao.create(message);
				
				todasTablasColumnasExisten = false;
			}else{
				//Comprobamos que todos los atributos de esa tabla existen
				List<DBAttribute> columnas = dbAttributeDao.getAll(dbConcept.getId());
				
				if (columnas.size() > 0){
					for (DBAttribute column : columnas) {
						String nombreColumna = column.getAttributeName();
						
						Boolean existeColumna = testExitsTableQueryDao.existsColumnTable(schema, table, nombreColumna);
						if (!existeColumna){
							Calendar timestamp = Calendar.getInstance();
							
							//Creamos el mensaje
							Object [] parametros = new Object[] {nombreColumna, schema, table, dbConcept.getName(), conceptTransformation.getId()};
							String mensaje = messageSource.getMessage("executionjob.column", parametros, locale);
							Message message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							todasTablasColumnasExisten = false;
						}else{
							//Si la columna existe, comprobamos que el tipo indicado para ella es correcto
							DBAttributeType dbAttributeType = column.getAttributeType();
							List<String> types = returnTypeColum(dbAttributeType);
							
							Boolean existeTipoColumna = testExitsTableQueryDao.existsTypeColumn(schema, table, nombreColumna, types);
							if (!existeTipoColumna){
								Calendar timestamp = Calendar.getInstance();
								
								//Creamos el mensaje
								Object [] parametros = new Object[] {nombreColumna, schema, table, dbConcept.getName(), conceptTransformation.getId()};
								String mensaje = messageSource.getMessage("executionjob.typecolumn", parametros, locale);
								Message message = new Message(mensaje, timestamp, execution);
								messageDao.create(message);
								
								todasTablasColumnasExisten = false;
							}
						}
					}
				}else{
					Calendar timestamp = Calendar.getInstance();
					
					//Creamos el mensaje
					Object [] parametros = new Object[] {dbConcept.getName(), conceptTransformation.getId()};
					String mensaje = messageSource.getMessage("executionjob.noattributes", parametros, locale);
					Message message = new Message(mensaje, timestamp, execution);
					messageDao.create(message);
					
					todasTablasColumnasExisten = false;
				}
			}
		}
		
		return todasTablasColumnasExisten;
	}
	
	private List<String> returnTypeColum(DBAttributeType dbAttributeType){
		List<String> tipos = new ArrayList<String>();
		switch (dbAttributeType) {
		case DATE:
			tipos.add("timestamp without time zone");
			break;

		case NUMBER_LONG:
			tipos.add("bigint");
			break;
			
		case NUMBER_DOUBLE:
			tipos.add("double precision");
			break;
			
		case NUMBER_INT:
			tipos.add("integer");
			break;
			
		case NUMBER_FLOAT:
			tipos.add("real");
			break;
			
		case CHAR: 
			tipos.add("char");
			tipos.add("character varying");
			tipos.add("text");
			break;
			
		case BOOLEAN:
			tipos.add("boolean");
			break;
		
		}
		
		return tipos;
	}
}
