package hermessensorcollector.lbd.udc.es.hermessensorcollector.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


import java.io.File;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.R;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.structure.AlbumStorageDirFactory;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.structure.BaseAlbumDirFactory;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.structure.FroyoAlbumDirFactory;


/**
 * Clase generica para recuperar nombres de las diferentes rutas: zip, json, xml, pictures de la aplicacion
 * que se usaran desde varias Activity/Fragment de la aplicacion
 * 
 * @author Leticia Riestra Ainsua
 *
 */
public class DirectoryPaths {

	private AlbumStorageDirFactory mAlbumStorageDirFactory;
	private Activity activity;

	public DirectoryPaths(Activity activity){
		this.activity = activity;
		
		 //Asociamos el album en funcion de la sdk
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
	}
	
	public AlbumStorageDirFactory recuperarAlbumStorageDirFactory(){
        return mAlbumStorageDirFactory;
	}
	
	/**
     * Recuperar el nombre del album
     * 
     * @return String con el nombre
     */
    private String getAlbumName() {
		return activity.getString(R.string.album_name);
	}
    
    /**
     * Recuperar el nombre principal del album donde estan las fotos incluyendo el nombre del album
     * @param nombreAlbumFotos
     * @return
     */
    /*public File getAlbumStorageDir(String nombreAlbumFotos){
    	return mAlbumStorageDirFactory.getAlbumStorageDir(nombreAlbumFotos);
    }*/
    
	/**
     * Recuperar el directorio donde se guardan las fotos
     * 
     * @return File Directorio donde se guardan las imagenes capturadas
     */
    public File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("DirectoryPaths", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(activity.getString(R.string.app_name),"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}
    
	/**
     * Recuperar el directorio donde se guarda el fichero JSON
     * 
     * @return File Directorio con la ruta donde se guarda el fichero
     */
    public File getJSONDir(){
    	File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getJSONStorageDir();

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("DirectoryPaths", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(activity.getString(R.string.app_name),"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
    	
    }
    
    /**
     * Recuperar el nombre del fichero JSON a generar para los sensores
     * 
     * @return String Nombre del fichero
     */
    public String getJSONSensorName(){
    	return mAlbumStorageDirFactory.getNameFileSensorJSON();
    }

	/**
	 * Recuperar el nombre del fichero JSON a generar para los gps
	 *
	 * @return String Nombre del fichero
	 */
	public String getJSONGpsName(){
		return mAlbumStorageDirFactory.getNameFileGpsJSON();
	}
    
    /**
     * Recuperar el nombre del fichero Zip a generar para los sensors
     * 
     * @return String Nombre del fichero
     */
    public String getZipSensorName(){
    	return mAlbumStorageDirFactory.getNameFileSensorZip();
    }

	/**
	 * Recuperar el nombre del fichero Zip a generar para los gps
	 *
	 * @return String Nombre del fichero
	 */
	public String getZipGpsName(){
		return mAlbumStorageDirFactory.getNameFileGpsZip();
	}


	/**
     * Recuperar el directorio donde se guarda el fichero ZIP
     * 
     * @return File Directorio con la ruta donde se guarda el fichero
     */
    public File getZipDir(){
    	File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory
					.getZipStorageDir();

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("DirectoryPaths", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(activity.getString(R.string.app_name),"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
    	
    }
   
    /**
     * Recuperar el directorio donde se guarda el fichero temporal ZIP
     * 
     * @return File Directorio con la ruta donde se guarda el fichero
     */
    public File getDirectorioTemporal(){
    	File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getTempStorageDir();
					
			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("DirectoryPaths", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(activity.getString(R.string.app_name),"External storage is not mounted READ/WRITE.");
		}

		return storageDir;
    	
    }
    /**
     * Recuperar el nombre del fichero Zip temporal a generar
     * 
     * @return String Nombre del fichero
     */
    public String getZipTempName(){
    	return mAlbumStorageDirFactory.getNameFileZipTemp();
    }
}
