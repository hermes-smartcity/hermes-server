package hermessensorcollector.lbd.udc.es.hermessensorcollector.structure;

import java.io.File;

/**
 * Clase abstracta usada para recuperar la direccion donde se guardan las imagenes
 * tomadas por la aplicacion, archivo xml, archivo JSON y zip generado para el envio
 * 
 * @author Leticia Riestra Ainsua
 *
 */
public abstract class AlbumStorageDirFactory {
	
	//Atributos para la generacion de la carpeta padre
	public static final String DIRECTORIO_PRINCIPAL = "/MECO1401";
	public static final String DIRECTORIO_PICTURES = "/Pictures";

	//Atributos para la generacion del XML
	public static final String XML_DIR = "/xml/";
	public static final String NAME_FILE_XML = "metadatos";
	
	// Atributos para la generacion del json
	public static final String JSON_DIR = "/json/";
	public static final String NAME_FILE_JSON_SENSOR = "metadatosSensor";
	public static final String NAME_FILE_JSON_GPS = "metadatosGps";

	//Atributos para la generacion del zip
	public static final String ZIP_DIR = "/zip/";
	public static final String NAME_FILE_ZIP_SENSOR = "zipSensor";
	public static final String NAME_FILE_ZIP_GPS = "zipGps";
	
	//Atributos para la generacion del log
	public static final String LOG_DIR = "/LOG/";
	public static final String NAME_FILE_LOG = "log";

	//Atributos para la generacion de la base de datos
	public static final String DATABASE_DIR = "/DATABASE/";
	public static final String NAME_FILE_DATABASE = "HermesDb";

	//Atributos para la generacion del directorio temporal donde descargar el zip de
	//la peticion al servicio
	public static final String TEMPORAL_DIR = "/TEMPORAL/";
	public static final String NAME_FILE_ZIPTEMPORAL = "zipTemporal";

	//Metodo para el directorio principal
	public abstract File getMainStorageDir();
	
	//Metodos para el directorio con las fotos
	public abstract File getAlbumStorageDir(String albumName);
	public abstract String dirStorage();
	
	//Metodo para el directorio con el XML
	public abstract File getXMLStorageDir();
	public abstract String getNameFileXML();
	
	// Metodo para el directorio con el JSON
	public abstract File getJSONStorageDir();
	public abstract String getNameFileSensorJSON();
	public abstract String getNameFileGpsJSON();
		
	//Metodo para el directorio con el zip
	public abstract File getZipStorageDir();
	public abstract String getNameFileSensorZip();
	public abstract String getNameFileGpsZip();
	
	//Metodo para el directorio con el log
	public abstract File getLogStorageDir();
	public abstract String getNameFileLog();
	
	//Metodo para el directorio con la base de datos
	public abstract File getDataBaseStorageDir();
	public abstract String getNameFileDataBase();
	
	//Metodo para el directorio temporal
	public abstract File getTempStorageDir();
	public abstract String getNameFileZipTemp();
	
}
