package es.udc.lbd.hermes.model.osmimport.importshapefile.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.FeatureComparators.Name;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.PropertyType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.udc.lbd.hermes.model.osmimport.dbattribute.DBAttribute;
import es.udc.lbd.hermes.model.osmimport.dbattribute.dao.DBAttributeDao;
import es.udc.lbd.hermes.model.osmimport.dbconcept.DBConcept;
import es.udc.lbd.hermes.model.osmimport.dbconcept.dao.DBConceptDao;
import es.udc.lbd.hermes.model.osmimport.dbconnection.DBConnection;
import es.udc.lbd.hermes.model.osmimport.dbconnection.dao.DBConnectionDao;
import es.udc.lbd.hermes.model.osmimport.importshapefile.dao.ImportShapefileDao;
import es.udc.lbd.hermes.model.testExitsTableQuery.dao.ExistTableQuery;
import es.udc.lbd.hermes.model.util.CifrarDescifrarUtil;
import es.udc.lbd.hermes.model.util.Unzipper;
import es.udc.lbd.hermes.model.util.exceptions.DatosShapefileException;
import es.udc.lbd.hermes.model.util.exceptions.TablaExisteException;
import es.udc.lbd.hermes.model.util.exceptions.ZipShapefileException;

@Service("importShapefileService")
@Transactional
public class ImportShapefileServiceImpl implements ImportShapefileService{

	@Autowired
	private DBAttributeDao dbAttributeDao;
		
	@Autowired
	private DBConnectionDao dbConnectionDao;
	
	@Autowired
	private DBConceptDao dbConceptDao;
	
	public SortedMap<String,Charset> recuperarCharsetPosibles(){
		return Charset.availableCharsets();	
	}
	
	public void importar(Long dbConnection, Long dbConcept, String dbConceptName, String dbConceptSchema, Boolean keepExistingData, Charset charset, MultipartFile file) throws ClassNotFoundException, SQLException, ZipShapefileException, TablaExisteException, DatosShapefileException, IllegalStateException, Exception{
		
		try {
			//Comprobar que el fichero tiene al menos el fichero .shp, el fichero .dbf y el fichero .pr
			File multiPartFile = multipartToFile(file);
			Boolean zipCorrecto = Unzipper.comprobarZipCorrecto(multiPartFile);
			
			if (!zipCorrecto){
				throw new ZipShapefileException(file.getName());
			}else{
				
				//Descomprimos el zip en una carpeta temporal
				Path carpetaTemporal = Unzipper.obtenerFicherosZip(multiPartFile);
				
				//Recuperamos cada uno de los ficheros .shp, .dbf, .prj
				File folder = new File(carpetaTemporal.toString());
		        File[] listOfFiles = folder.listFiles(); 

		        File shpFile = null;
		        File dbfFile = null;
		        File prjFile = null;
		        for (int i = 0; i < listOfFiles.length; i++)         {
		            if (listOfFiles[i].isFile())             {
		                String nombreFile = listOfFiles[i].getName();
		                if (nombreFile.endsWith(Unzipper.SHP_EXTENSION)){
		                	shpFile = listOfFiles[i];
		                }else if (nombreFile.endsWith(Unzipper.DBF_EXTENSION)){
		                	dbfFile = listOfFiles[i];
		                }else if (nombreFile.endsWith(Unzipper.PRJ_EXTENSION)){
		                	prjFile = listOfFiles[i];
		                }
		            }
		        }
				
		        
		        //Recuperamos la lista de atributos de las features indicados en el shapefile (para poder recuperar sus nombres)
		        //FileDataStore shpDataStore = FileDataStoreFinder.getDataStore(shpFile);
		        ShapefileDataStore shpDataStore = new ShapefileDataStore(shpFile.toURI().toURL());
		        shpDataStore.setCharset(charset);
		        
				String[] typeNamesShp = shpDataStore.getTypeNames();
				FeatureSource<SimpleFeatureType, SimpleFeature> featSourceShp = shpDataStore.getFeatureSource(typeNamesShp[0]);
				
				FeatureCollection<SimpleFeatureType, SimpleFeature> featuresShp = featSourceShp.getFeatures();
								
				//SimpleFeatureType schema = featSourceShp.getSchema();
				//String geomType = schema.getGeometryDescriptor().getType().getBinding().getName();
				
				Collection<Property> propertiesShp = null;
				FeatureIterator<SimpleFeature> iterator = featuresShp.features();
			    try {
			        while( iterator.hasNext() ){
			            SimpleFeature feature = iterator.next();
			            propertiesShp = feature.getProperties();
			            
						break;
			        }
			    }
			    finally {
			        iterator.close();
			    }
			    
			    //Una vez tenemos la lista de atributos, recuperamos sus nombres
			    List<String> nombresAtributosShp = new ArrayList<String>();
			    List<PropertyType> tiposAtributosShp = new ArrayList<PropertyType>();
			    Map<String, PropertyType> mapaAtributos = new HashMap<String, PropertyType>();
			    if (propertiesShp != null){
				    for(Property property : propertiesShp){		    	
				    	nombresAtributosShp.add(property.getName().getLocalPart().toLowerCase());		
				    	tiposAtributosShp.add(property.getType());
				    	
				    	mapaAtributos.put(property.getName().getLocalPart(), property.getType());
				    }
			    }
			    
			    //Recuperamos el datastore asociado a la conexion
			    DBConnection dbConnectionObject = dbConnectionDao.get(dbConnection);
			    JDBCDataStore dataStore = recuperarDataStore(dbConnectionObject);

				// Accedemos a la base de datos donde se quieren incorporar los datos
				if (dataStore == null) {
					throw new IllegalStateException("Datastore can not be null when writing");
				}
				
				//Si el usuario marcha el checkbox "[ ] Crear la tabla", la tabla no debe existir
			    String nombreTabla = null;
			    String esquemaTabla = null;
			    String nombreAtributoId = null;
				if (dbConceptName != null && dbConceptSchema != null){
					
					ExistTableQuery existTableQuery = new ExistTableQuery(dbConnectionObject);
					
					Boolean existe = existTableQuery.exitsSchemaTable(dbConceptSchema, dbConceptName);
						
					if (existe){
						throw new TablaExisteException(dbConceptSchema, dbConceptName);	
					}else{
						//Se crea la tabla con los mismos atributos que en el shapefile
						nombreAtributoId = "hermesId";
						ImportShapefileDao importShapefileDao = new ImportShapefileDao(dbConnectionObject);
						importShapefileDao.createTable(dbConceptName, dbConceptSchema, mapaAtributos, nombreAtributoId);
						
						//Asignamos nombreTabla para luego usarla en el datastore
						nombreTabla = dbConceptName;
						esquemaTabla = dbConceptSchema;
						
						dataStore.dispose();
						
						//Volvemos a recuperar el datastore para que tenga la tabla nueva creada
						dataStore = recuperarDataStore(dbConnectionObject);

						// Accedemos a la base de datos donde se quieren incorporar los datos
						if (dataStore == null) {
							throw new IllegalStateException("Datastore can not be null when writing");
						}
					}
					
				}else{
					//Si el usuario selecciona un DBConcept, el numero nombres de atributos del shapefile deben conincidir con el del DBConcept
					
					DBConcept dbConceptObject = dbConceptDao.get(dbConcept);
					List<DBAttribute> dbAttributes = dbAttributeDao.getAll(dbConcept);
					
					//Comparamos que los atributos del shapefile coinciden con los de la tabla en la que se quiere importar
				    //Los nombres de los atributos del shapefile traen solo los primeros X caracteres
					List<String> atributos = new ArrayList<String>();
					atributos.add(dbConceptObject.getGeomName());
					atributos.add(dbConceptObject.getOsmIdName());
					for (DBAttribute dbAttribute : dbAttributes) {
						String nombre = dbAttribute.getAttributeName();
						atributos.add(nombre);
					}
				    Boolean datosCoinciden = coincidenAtributos(nombresAtributosShp,atributos);
				    if (!datosCoinciden){
				    	//Mensaje de que los atributos no coinciden
						throw new DatosShapefileException();
				    }else{
				    	//Asignamos nombreTabla para luego usarla en el datastore
						nombreTabla = dbConceptObject.getTableName();
						esquemaTabla = dbConceptObject.getSchemaName();
						nombreAtributoId = dbConceptObject.getIdName();
				    }
				}
				
				//Si tenemos nombreTabla es porque o bien se creo o porque se tomo de dbconcept
				if (nombreTabla != null){
					dataStore.setDatabaseSchema(esquemaTabla);
					SimpleFeatureType schemaBD = dataStore.getSchema(nombreTabla);
				
					SimpleFeatureSource featureSource = dataStore.getFeatureSource(nombreTabla);
					
					Transaction transaction = new DefaultTransaction("remove_add");
				    try {
				    	
				    	//Si hay que borrar los datos antiguos
				    	if (!keepExistingData){
					    	FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(nombreTabla, transaction);
			
					    	SimpleFeature feature;
					    	try {
					    		while (writer.hasNext()) {
					    			feature = writer.next();
					    			System.out.println("Feature eliminada " + feature.getID());
					    			writer.remove(); // marking contents for removal
					    		}
					    	} finally {
					    		writer.close();
					    	}
				    	}
				    	
				    	//insertamos las nuevas filas
					    if (featureSource instanceof SimpleFeatureStore) {
							SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
							
							featureStore.setTransaction(transaction);

							DefaultFeatureCollection featureCollectionBD = construirFeaturesInsertar(dataStore, nombreAtributoId, nombreTabla, schemaBD, featuresShp);
							
							featureStore.addFeatures(featureCollectionBD);

						} 
					    
				    	transaction.commit();
				    	
				    } catch (Exception e) {
				    	transaction.rollback();
				    	throw e;				    	
				    } catch (Throwable eek) {
				    	transaction.rollback();
				    	throw new Exception();
				    } finally {
				    	transaction.close();
				    	dataStore.dispose();
				    }
				}
				
				if (folder!=null){
					folder.delete();
				}
				
				if (multiPartFile != null){
					multiPartFile.delete();
				}
				
			}
			
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (IOException e) {
			throw new Exception();
		} catch (Exception e) {
			throw e;
		}
	}
	
	private File multipartToFile(MultipartFile multipart) throws IllegalStateException, IOException {
		File convFile = new File(multipart.getOriginalFilename());
		convFile.createNewFile(); 
		FileOutputStream fos = new FileOutputStream(convFile); 
		fos.write(multipart.getBytes());
		fos.close(); 
		return convFile;
	}
	
	private Boolean coincidenAtributos(List<String> atributosShp, List<String> atributos){
		Boolean coinciden = true;
		//1 - El numero de atributos tiene que coincidir
		int numeroAtributosShapeFile = atributosShp.size();
		int numeroAtributosDBAttributes = atributos.size();
		
		if (numeroAtributosShapeFile != numeroAtributosDBAttributes){
			coinciden = false;
		}else{
			//Los nombres tienen que ser iguales
			for (String atributo : atributos) {
				//Si el atributo es el de geom, no lo validamos puesto que nosotros
				//siempre lo llamamos geom
				if (!atributo.toLowerCase().equals("geom")){
					if (!atributosShp.contains(atributo.toLowerCase())){
						coinciden = false;
						break;
					}
				}
			}
		}
		
		return coinciden;
	}
	
	private JDBCDataStore recuperarDataStore(DBConnection dbConnection) throws Exception{
		Properties params = new Properties();
		
		params.put("user", dbConnection.getUserDb());
		params.put("passwd", CifrarDescifrarUtil.descifra(dbConnection.getPassDb()));
		params.put("port", dbConnection.getPort());
		params.put("host", dbConnection.getHost());
		params.put("database", dbConnection.getDbName());
		params.put("dbtype", "postgis");
		
		DataStore dataStore = DataStoreFinder.getDataStore(params);
		
		if (dataStore instanceof JDBCDataStore){
			return (JDBCDataStore)dataStore;
		}else{
			throw new Exception();
		}
		
	}
	
	private DefaultFeatureCollection construirFeaturesInsertar(DataStore dataStore, String nombreAtributoId, String nombreTabla, SimpleFeatureType schemaBD, FeatureCollection<SimpleFeatureType, SimpleFeature> featuresShp){
		 
		//Cuando recuperemos los objetos del shapefile tenemos luego que insertarlos en el mismo orden que el esquemaBD
		//Como puede ser que no esten en el mismo orden hacemos un map para poder saber para cada atributo cual es la
		//posicion que ocupara
		//Para el caso de la geometria, como puede tener cualquier nombre lo que hacemos es mirar el tipo de la clase
		//Si es de tipo geometria lo metemos en el map con el nombre 'the_geom' que es la manera de llamar siempre
		//en los shapefiles a las geometrias
		Map<String,Integer> posicionesAtributos = new HashMap<String, Integer>();
		
		List<AttributeDescriptor> listaAttributeDescriptor = schemaBD.getAttributeDescriptors();
		for (int i=0;i<listaAttributeDescriptor.size();i++){
			AttributeDescriptor attributeDescriptor = listaAttributeDescriptor.get(i);
			
			String name = attributeDescriptor.getLocalName();
			AttributeType tipo = attributeDescriptor.getType();
			
			String nombreClase = tipo.getBinding().getName();
			
			//Descartamos el primarykey porque es autoincremental
			if (!name.toLowerCase().equals(nombreAtributoId)){
				if (nombreClase.contains("com.vividsolutions.jts.geom.")){
					posicionesAtributos.put("the_geom", i);
				}else{
					posicionesAtributos.put(name.toLowerCase(), i);
				}
			}
		}
		
		
		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection(nombreTabla,schemaBD);
		
		FeatureIterator<SimpleFeature> iterator = featuresShp.features();
		try{
			
			while( iterator.hasNext() ){
				SimpleFeature featureShp = iterator.next();
				List<AttributeDescriptor> atributos = featureShp.getFeatureType().getAttributeDescriptors();
				
				//Siempre le quitamos uno porque TODAS las tablas son autoincrement
				
				//Si los atributos de la feature contienen el atributo usado como primarykey,
				//ese valor no se insertara asi que el numero de atributos sera -1
				int numeroAtributos = 0;
				for (AttributeDescriptor attributo : atributos) {
					String name = attributo.getLocalName();
					
					if (!name.toLowerCase().equals(nombreAtributoId)){
						numeroAtributos++;
					}
				}
				Object[] objetos = new Object[numeroAtributos];
				
				for (AttributeDescriptor attributo : atributos) {
					String name = attributo.getLocalName();
					
					//El primarykey no se inserta
					if (!name.toLowerCase().equals(nombreAtributoId)){
						
						Object objeto = null;
						if (!featureShp.getAttribute(name).equals("")){
							objeto = featureShp.getAttribute(name);
						}
						
						//Recuperamos del map la posicion que tiene que ocupar
						//Nota - cuando es geometria en de nuestro map 
						if (posicionesAtributos.containsKey(name.toLowerCase())){
							Integer posicion = posicionesAtributos.get(name.toLowerCase());
							objetos[posicion] = objeto;
						}
					}
					
				}
				
				SimpleFeature simpleFeature = SimpleFeatureBuilder.build(schemaBD, objetos, null);
				featureCollection.add(simpleFeature);
			}
			
		}finally {
	    	iterator.close();
		}  
		return featureCollection;
	}
	
	
	/*private SimpleFeatureType renameGeomColumn(SimpleFeatureType featureType, String newGeomName) {
		SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
		tb.setName(featureType.getName());
		tb.setNamespaceURI(featureType.getName().getNamespaceURI());
		tb.setCRS(featureType.getCoordinateReferenceSystem()); // not interested in warnings from this simple method
		tb.addAll(featureType.getAttributeDescriptors());
		tb.setDefaultGeometry(newGeomName);
		return tb.buildFeatureType();
	}*/
	
	
}
