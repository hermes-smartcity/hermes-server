package es.udc.lbd.hermes.model.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzipper {

	private final static int BUFFER_SIZE = 2048;
	private final static String ZIP_EXTENSION = ".zip";
	public final static String SHP_EXTENSION = ".shp";
	public final static String DBF_EXTENSION = ".dbf";
	public final static String PRJ_EXTENSION = ".prj";

	public static Path obtenerFicherosZip(File f) throws IOException{
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		byte[] buffer = new byte[BUFFER_SIZE];
		while (bis.read(buffer, 0, BUFFER_SIZE) != -1) {
			bos.write(buffer);
		}
		bos.flush();
		bos.close();
		bis.close();
		
		//Creamos una carpeta temporal donde descomprimiremos los ficheros
		Path carpetaTemporal = Files.createTempDirectory("Shapefiles_");
		
		unzipFile(baos, carpetaTemporal);

		return carpetaTemporal;
	}

	public static Boolean comprobarZipCorrecto(File f) throws IOException{
		
		Boolean tieneShp = false;
		Boolean tieneDbf = false;
		Boolean tienePrj = false;
		
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baos);
		byte[] buffer = new byte[BUFFER_SIZE];
		while (bis.read(buffer, 0, BUFFER_SIZE) != -1) {
			bos.write(buffer);
		}
		bos.flush();
		bos.close();
		bis.close();
		
		ZipInputStream inputStream = new ZipInputStream(
				new BufferedInputStream(new ByteArrayInputStream(
						baos.toByteArray())));
		ZipEntry entry;

		while ((entry = inputStream.getNextEntry()) != null) {
			
			if (entry.getName().toLowerCase().endsWith(SHP_EXTENSION)){
				tieneShp = true;
			}else if (entry.getName().toLowerCase().endsWith(DBF_EXTENSION)){
				tieneDbf = true;
			}else if (entry.getName().toLowerCase().endsWith(PRJ_EXTENSION)){
				tienePrj = true;
			}
		}
		
		if (tieneShp && tieneDbf && tienePrj){
			return true;
		}else{
			return false;
		}
	}
	
	public static List<ByteArrayOutputStream> unzip(ByteArrayOutputStream zippedFileOS) {
		try {

			ZipInputStream inputStream = new ZipInputStream(
					new BufferedInputStream(new ByteArrayInputStream(
							zippedFileOS.toByteArray())));
			ZipEntry entry;

			List<ByteArrayOutputStream> result = new ArrayList<ByteArrayOutputStream>();
			while ((entry = inputStream.getNextEntry()) != null) {
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				System.out.println("\tExtracting entry: " + entry);
				int count;
				byte data[] = new byte[BUFFER_SIZE];

				if (!entry.isDirectory()) {
					BufferedOutputStream out = new BufferedOutputStream(
							outputStream, BUFFER_SIZE);
					while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
						out.write(data, 0, count);
					}
					out.flush();
					out.close();
					// recursively unzip files
					if (entry.getName().toLowerCase().endsWith(ZIP_EXTENSION)) {
						result.addAll(unzip(outputStream));
					} else {
						
						result.add(outputStream);
					}
				}
			}
			inputStream.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static List<FileOutputStream> unzipFile(ByteArrayOutputStream zippedFileOS, Path carpetaTemporal) {
		try {

			ZipInputStream inputStream = new ZipInputStream(
					new BufferedInputStream(new ByteArrayInputStream(
							zippedFileOS.toByteArray())));
			ZipEntry entry;

			List<FileOutputStream> result = new ArrayList<FileOutputStream>();
			while ((entry = inputStream.getNextEntry()) != null) {
				
				System.out.println("\tExtracting entry: " + entry);
				int count;
				byte data[] = new byte[BUFFER_SIZE];

				if (!entry.isDirectory()) {
					
					FileOutputStream fos = new FileOutputStream(carpetaTemporal.toString() + File.separator + entry.getName());
					BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER_SIZE);
					while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
						dest.write(data, 0, count);
				    }
				    dest.flush();
				    dest.close();
				}
			}
			inputStream.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
