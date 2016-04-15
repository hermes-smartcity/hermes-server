package hermessensorcollector.lbd.udc.es.hermessensorcollector.utils;

import android.app.Activity;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.R;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ZipErrorException;

public class Compress {

	private static final int BUFFER = 2048;

	private String[] _files;
	private String _zipFile;

	public Compress(String[] files, String zipFile) {
		_files = files;
		_zipFile = zipFile;
	}

	public Boolean zip() throws ZipErrorException {
		try {
			BufferedInputStream origin = null;
			FileOutputStream dest = new FileOutputStream(_zipFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));

			byte data[] = new byte[BUFFER];

			for (int i = 0; i < _files.length; i++) {
				Log.i("Compress", "Adding: " + _files[i]);

				FileInputStream fi = new FileInputStream(_files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(_files[i].substring(_files[i]
						.lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
			}

			out.close();
			
			return true;
			
		} catch (Exception e) {
			throw new ZipErrorException(e);
		}

	}
	

	public static void unzip(String zipFile, String location) throws ZipErrorException {
	    int size;
	    byte[] buffer = new byte[BUFFER];

	    try {
	    	
	    	if ( !location.endsWith("/") ) {
	            location += "/";
	        }
	        File f = new File(location);
	        if(!f.isDirectory()) {
	            f.mkdirs();
	        }
	        
	        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER));
	        
	        try {
	        	ZipEntry ze = null;
	        	while ((ze = zin.getNextEntry()) != null) {
	        		String path = location + ze.getName();
	        		File unzipFile = new File(path);

	        		if (ze.isDirectory()) {
	        			if(!unzipFile.isDirectory()) {
	        				unzipFile.mkdirs();
	        			}
	        		} else {
	        			// check for and create parent directories if they don't exist
	        			File parentDir = unzipFile.getParentFile();
	        			if ( null != parentDir ) {
	        				if ( !parentDir.isDirectory() ) {
	        					parentDir.mkdirs();
	        				}
	        			}

	        			// unzip the file
	        			FileOutputStream out = new FileOutputStream(unzipFile, false);
	        			BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER);
	        			try {
	        				while ( (size = zin.read(buffer, 0, BUFFER)) != -1 ) {
	        					fout.write(buffer, 0, size);
	        				}

	        				zin.closeEntry();
	        			}
	        			finally {
	        				fout.flush();
	        				fout.close();
	        			}
	        		}
	        	}
	        }finally {
	        	zin.close();
	        }


	    }catch (Exception e) {
	    	throw new ZipErrorException(e);
	    }
	}
	
	public static void unzipArchivosCifrados(String zipFile, String location, SecretKey skey) throws ZipErrorException {
	    int size;
	    byte[] buffer = new byte[BUFFER];

	    try {
	    	
	    	if ( !location.endsWith("/") ) {
	            location += "/";
	        }
	        File f = new File(location);
	        if(!f.isDirectory()) {
	            f.mkdirs();
	        }
	        
	        ZipInputStream zin;

	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        File fichero = new File(zipFile);
	        FileInputStream fis = new FileInputStream(fichero);
	        Cipher decipher = Cipher.getInstance("AES");
	        decipher.init(Cipher.DECRYPT_MODE, skey);
	        CipherInputStream cis = new CipherInputStream(fis, decipher);
	        int b;
	        byte[] d = new byte[8];
	        while ((b = cis.read(d)) != -1) {
	        	baos.write(d, 0, b);
	        }
	        baos.flush();
	        baos.close();
	        cis.close();
	        fis.close();

	        InputStream is = new ByteArrayInputStream(baos.toByteArray());
	        zin = new ZipInputStream(new BufferedInputStream(is, BUFFER));
        
	        try {
	        	ZipEntry ze = null;
	        	while ((ze = zin.getNextEntry()) != null) {
	        		String path = location + ze.getName();
	        		File unzipFile = new File(path);

	        		if (ze.isDirectory()) {
	        			if(!unzipFile.isDirectory()) {
	        				unzipFile.mkdirs();
	        			}
	        		} else {
	        			// check for and create parent directories if they don't exist
	        			File parentDir = unzipFile.getParentFile();
	        			if ( null != parentDir ) {
	        				if ( !parentDir.isDirectory() ) {
	        					parentDir.mkdirs();
	        				}
	        			}

	        			// unzip the file
	        			FileOutputStream out = new FileOutputStream(unzipFile, false);
	        			BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER);
	        			try {
	        				while ( (size = zin.read(buffer, 0, BUFFER)) != -1 ) {
	        					fout.write(buffer, 0, size);
	        				}

	        				zin.closeEntry();
	        			}
	        			finally {
	        				fout.flush();
	        				fout.close();
	        			}
	        		}
	        	}
	        }finally {
	        	zin.close();
	        }


	    }catch (Exception e) {
	    	throw new ZipErrorException(e);
	    }
	}
	
	public Boolean zipArchivosCifrados(Activity activity, SecretKey skey) throws ZipErrorException{
		try {

			FileOutputStream dest = new FileOutputStream(_zipFile);

			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));

			for (int i = 0; i < _files.length; i++) {
				Log.i("Compress", "Adding: " + _files[i]);

				File f = new File(_files[i]);

				BufferedInputStream origin = null;

				byte data[] = new byte[BUFFER];

				FileInputStream fi = new FileInputStream(_files[i]);
				origin = new BufferedInputStream(fi, BUFFER);
				ZipEntry entry = new ZipEntry(_files[i].substring(_files[i]
						.lastIndexOf("/") + 1));
				out.putNextEntry(entry);
				int count;
				while ((count = origin.read(data, 0, BUFFER)) != -1) {
					out.write(data, 0, count);
				}
				origin.close();
				
			}

			out.close();
			
			return true;
			
		} catch (Exception e) {
			throw new ZipErrorException(e);
		}

	}

}
