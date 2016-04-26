package hermessensorcollector.lbd.udc.es.hermessensorcollector.structure;

import java.io.File;


import android.os.Environment;

/**
 * Implementacion de AlbumStorageDirFactory cuando el SDK Utilizado es menor de Froyo Version
 * 
 * @author Leticia Riestra Ainsua
 *
 */
public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/dcim/";
	
	@Override
	public File getMainStorageDir(){
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL 
		);
	}
	
	@Override
	public File getAlbumStorageDir(String albumName) {

		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL 
				+ CAMERA_DIR
				+ albumName
		);
	
	}
	
	public String dirStorage(){
		return CAMERA_DIR;
	}

	@Override
	public File getXMLStorageDir() {
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + XML_DIR
		);
	}
	
	public String getNameFileXML(){
		return NAME_FILE_XML;
	}
	
	@Override
	public File getJSONStorageDir() {
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + JSON_DIR
		);
	}
	
	public String getNameFileSensorJSON(){
		return NAME_FILE_JSON_SENSOR;
	}

	public String getNameFileGpsJSON(){
		return NAME_FILE_JSON_GPS;
	}
	
	public File getZipStorageDir(){
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + ZIP_DIR
		);
	}
	
	public String getNameFileSensorZip(){
		return NAME_FILE_ZIP_SENSOR;
	}

	public String getNameFileGpsZip(){
		return NAME_FILE_ZIP_GPS;
	}
	
	public File getLogStorageDir(){
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + LOG_DIR
		);
	}
	
	public String getNameFileLog(){
		return NAME_FILE_LOG;
	}
	
	public File getDataBaseStorageDir(){
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + DATABASE_DIR
		);
	}
	
	public String getNameFileDataBase(){
		return NAME_FILE_DATABASE;
	}
	
	public File getTempStorageDir(){
		return new File(
				Environment.getExternalStorageDirectory()
				+ DIRECTORIO_PRINCIPAL + TEMPORAL_DIR
		);
	}
	
	public String getNameFileZipTemp(){
		return NAME_FILE_ZIPTEMPORAL;
	}
}
