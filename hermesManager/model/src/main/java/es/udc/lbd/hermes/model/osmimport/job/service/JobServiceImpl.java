package es.udc.lbd.hermes.model.osmimport.job.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.PrecisionModel;

import es.udc.lbd.hermes.model.osmimport.attributemapping.AttributeMapping;
import es.udc.lbd.hermes.model.osmimport.attributemapping.dao.AttributeMappingDao;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.ConceptTransformation;
import es.udc.lbd.hermes.model.osmimport.concepttransformation.dao.ConceptTransformationDao;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttributeType;
import es.udc.lbd.hermes.model.osmimport.dbattribute.dao.DBAttributeDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconnection.dao.DBConnectionDao;
import es.udc.lbd.hermes.model.osmimport.execution.Execution;
import es.udc.lbd.hermes.model.osmimport.execution.ExecutionStatus;
import es.udc.lbd.hermes.model.osmimport.execution.dao.ExecutionDao;
import es.udc.lbd.hermes.model.osmimport.job.Job;
import es.udc.lbd.hermes.model.osmimport.job.JobDTO;
import es.udc.lbd.hermes.model.osmimport.job.dao.JobDao;
import es.udc.lbd.hermes.model.osmimport.job.json.Element;
import es.udc.lbd.hermes.model.osmimport.job.json.OSMParser;
import es.udc.lbd.hermes.model.osmimport.job.json.Overpass;
import es.udc.lbd.hermes.model.osmimport.message.Message;
import es.udc.lbd.hermes.model.osmimport.message.dao.MessageDao;
import es.udc.lbd.hermes.model.osmimport.osmconcept.OsmConcept;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilter;
import es.udc.lbd.hermes.model.osmimport.osmfilter.OsmFilterOperation;
import es.udc.lbd.hermes.model.osmimport.osmfilter.dao.OsmFilterDao;
import es.udc.lbd.hermes.model.testExitsTableQuery.dao.ExistTableQuery;
import es.udc.lbd.hermes.model.util.exceptions.OsmAttributeException;

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
	private DBAttributeDao dbAttributeDao;
	
	@Autowired
	private OsmFilterDao osmFilerDao;
	
	@Autowired
	private DBConnectionDao dbConnectionDao;
	
	@Autowired
	private AttributeMappingDao attributeMappingDao;
	
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
		OSMParser osmParser = new OSMParser(); 
		
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
				
				//si hay algun fallo importante, la ejecucion sera ERROR y no SUCCESS
				Boolean algunErrorImportante = false; 
								
				for (ConceptTransformation conceptTransformation : listaCT) {
					Calendar timestamp = Calendar.getInstance();
					
					Object [] parametros = new Object[] {conceptTransformation.getId()};
					String mensaje = messageSource.getMessage("executionjob.concepttransformation", parametros, locale);
					Message message = new Message(mensaje, timestamp, execution);
					messageDao.create(message);
					
					//Construimos la consulta a hacer
					String consulta = "http://overpass-api.de/api/interpreter?data=[out:json];node";
					
					//Si conceptTransformation no tiene geometria, lo cogemos del job
					Polygon geom = conceptTransformation.getBbox();
					if (geom == null){
						Job job = jobDao.get(idJob);
						geom = job.getBbox();
					}

					//El area no puede ser muy grande
					if (geom.getArea() > 1){
						timestamp = Calendar.getInstance();

						mensaje = messageSource.getMessage("executionjob.geom", null, locale);
						message = new Message(mensaje, timestamp, execution);
						messageDao.create(message);
							
						algunErrorImportante = true;
						
					}else{
						//Construimos la parte de la consulta relacionado con el boudingbox
						consulta = consulta + "(poly:'";
						Coordinate[] coordenadas = geom.getCoordinates();
						for(int i=0;i<coordenadas.length-1;i++){
							Coordinate coordinate = coordenadas[i];
						
							if (i != coordenadas.length){
								consulta = consulta + coordinate.y + " " + coordinate.x + " ";
							}else{
								consulta = consulta + coordinate.y + " " + coordinate.x;	
							}
							
						}
						consulta = consulta + "')";
						
						
						//Recuperamos la lista de osmfilters asociados al osmconcept del conceptransformation
						OsmConcept osmConcept = conceptTransformation.getOsmConcept();
						List<OsmFilter> listaOsmFilters = osmFilerDao.getAll(osmConcept.getId());
						
						//Si no hay filtro, tenemos que mirar que haya un boundingbox pequeño
						if (listaOsmFilters.size() > 0){
							//Construimos la parte de la consulta relacionada con los filtros
							for (OsmFilter osmFilter : listaOsmFilters) {
								String nombre = osmFilter.getName();
								OsmFilterOperation osmFilterOperation = osmFilter.getOperation();
								String operation = returnFilterOperation(osmFilterOperation);
								String value = osmFilter.getValue();
								
								consulta = consulta + "[\"" + nombre + "\"" + operation + "\"" + value + "\"]";
							}
						}else{
							timestamp = Calendar.getInstance();

							parametros = new Object[] {osmConcept.getName(), conceptTransformation.getId()};
							mensaje = messageSource.getMessage("executionjob.filters", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
						}
						
						//Anadimos el final de la consulta
						consulta = consulta + ";out;";
					}

					//Escribimos mensaje de empezar a procesar la consulta
					timestamp = Calendar.getInstance();

					parametros = new Object[] {consulta};
					mensaje = messageSource.getMessage("executionjob.consulta", parametros, locale);
					message = new Message(mensaje, timestamp, execution);
					messageDao.create(message);
					
					//cambiamos el enconding de la consulta para el httpclient
					consulta = encodingUrl(consulta);
									
					//Hacemos la peticion
					String peticionJson = httpClientGet(consulta, execution, locale);
					if (peticionJson != null){
					
						timestamp = Calendar.getInstance();
						mensaje = messageSource.getMessage("executionjob.consultaOk", null, locale);
						message = new Message(mensaje, timestamp, execution);
						messageDao.create(message);
						
						//Parseamos el string al objeto que corresponde
						try {
							Overpass ob = osmParser.parse(peticionJson);
							List<Element> elementos = ob.getElements();
							
							for (Element element : elementos) {
								DBConcept dbConcept = conceptTransformation.getDbConcept();
								List<AttributeMapping> attributeMappings = attributeMappingDao.getAll(conceptTransformation.getId());
								
								//Insertamos los datos en base de datos
								ExistTableQuery existTableQuery = new ExistTableQuery(dbConcept.getDbConnection());
								existTableQuery.insertarDBConcept(dbConcept, attributeMappings, element);
							}
							
							
							//Escribimos el numero de elementos encontrados en el osm
							timestamp = Calendar.getInstance();
							parametros = new Object[] {elementos.size()};
							mensaje = messageSource.getMessage("executionjob.numelementososm", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							
						} catch (JsonParseException e) {
							e.printStackTrace();

							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.jsonparseexception", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
							
						} catch (JsonMappingException e) {
							e.printStackTrace();
							
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.jsonmappingexception", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
							
						} catch (IOException e) {
							e.printStackTrace();
							
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.ioexception", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
							
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
					
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.classnotfound", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
							
						} catch (SQLException e) {
							e.printStackTrace();
							
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.sqlexception", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
						} catch (OsmAttributeException e) {
						
							e.printStackTrace();
							
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getTagName()};
							mensaje = messageSource.getMessage("executionjob.osmAttributeException", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
						} catch (Exception e) {
							e.printStackTrace();
							
							timestamp = Calendar.getInstance();
							parametros = new Object[] {e.getLocalizedMessage()};
							mensaje = messageSource.getMessage("executionjob.cifradoexception", parametros, locale);
							message = new Message(mensaje, timestamp, execution);
							messageDao.create(message);
							
							algunErrorImportante = true;
						}
						
					}else{
						timestamp = Calendar.getInstance();
						mensaje = messageSource.getMessage("executionjob.consultaError", null, locale);
						message = new Message(mensaje, timestamp, execution);
						messageDao.create(message);
						
						algunErrorImportante = true;
					}
				}
				
				
				//Al terminar, creamos mensaje indicando el numero de ConceptTransformation ejecutadas
				Calendar timestamp = Calendar.getInstance();

				Object [] parametros = new Object[] {listaCT.size()};
				String mensaje = messageSource.getMessage("executionjob.numbertransformations", parametros, locale);
				Message message = new Message(mensaje, timestamp, execution);
				messageDao.create(message);

				//Actualizamos el estado y la fecha de la ejecucion
				if (algunErrorImportante){
					execution.setStatus(ExecutionStatus.ERROR);	
				}else{
					execution.setStatus(ExecutionStatus.SUCCESS);
				}
				
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
		
		try{
			for (ConceptTransformation conceptTransformation : listado) {
				DBConcept dbConcept = conceptTransformation.getDbConcept();
				String schema = dbConcept.getSchemaName();
				String table = dbConcept.getTableName();
				
				if (schema == null){
					schema = "public";
				}
				
				ExistTableQuery existTableQuery = new ExistTableQuery(dbConcept.getDbConnection());
				
				Boolean existe = existTableQuery.exitsSchemaTable(schema, table);
				
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
							
							Boolean existeColumna = existTableQuery.existsColumnTable(schema, table, nombreColumna);
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
								
								Boolean existeTipoColumna = existTableQuery.existsTypeColumn(schema, table, nombreColumna, types);
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
						
						todasTablasColumnasExisten = true;
					}
				}
			}
			
		}catch(ClassNotFoundException e){
			todasTablasColumnasExisten = false;
			//Creamos el mensaje
			Calendar timestamp = Calendar.getInstance();
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("executionjob.classnotfound", parametros, locale);
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
			
			e.printStackTrace();
			
		}catch(SQLException e){
			todasTablasColumnasExisten = false;
			
			//Creamos el mensaje
			Calendar timestamp = Calendar.getInstance();
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("executionjob.sqlexception", parametros, locale);
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
			
			e.printStackTrace();
			
		}catch(Exception e){
			todasTablasColumnasExisten = false;
			
			//Creamos el mensaje
			Calendar timestamp = Calendar.getInstance();
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("executionjob.cifradoexception", parametros, locale);
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
			
			e.printStackTrace();
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
	
	private String returnFilterOperation(OsmFilterOperation osmFilterOperation){
		String operation = null;;
		switch (osmFilterOperation) {
		case EQUALS:
			operation = "=";
			break;

		case DIFFERENT:
			operation = "!=";
			break;
			
		case LIKE:
			operation = "~";
			break;
			
		case NOTLIKE:
			operation = "!~";
			break;
		}
		
		return operation;
	}
	
	private String httpClientGet(String consulta, Execution execution, Locale locale){
		StringBuffer salida = new StringBuffer();
		
		try {
			HttpClient httpClient = HttpClientBuilder.create().build();
			HttpGet getRequest = new HttpGet(consulta);

			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatusLine().getStatusCode());
			}

			salida.ensureCapacity(20000);
			BufferedReader br = new BufferedReader(
					new InputStreamReader((response.getEntity().getContent())));

			String output;
			while ((output = br.readLine()) != null) {
				salida.append(output);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			
			Calendar timestamp = Calendar.getInstance();
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("executionjob.clientprotocolexception", parametros, locale);
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
			
		} catch (IOException e) {
			e.printStackTrace();
			
			Calendar timestamp = Calendar.getInstance();
			Object [] parametros = new Object[] {e.getLocalizedMessage()};
			String mensaje = messageSource.getMessage("executionjob.ioexception", parametros, locale);
			Message message = new Message(mensaje, timestamp, execution);
			messageDao.create(message);
		}
		
		return salida.toString();
	}
	
	private String encodingUrl(String myURL){
		URI uri = null;
		try {
			URL url = new URL(myURL);
			String nullFragment = null;
			uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
			
		} catch (MalformedURLException e) {
			System.out.println("URL " + myURL + " is a malformed URL");
		} catch (URISyntaxException e) {
			System.out.println("URI " + myURL + " is a malformed URL");
		}
		
		return uri.toString();
	}
}
