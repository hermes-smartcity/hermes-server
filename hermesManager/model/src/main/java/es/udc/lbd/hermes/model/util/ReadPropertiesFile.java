package es.udc.lbd.hermes.model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebServiceImpl;

public class ReadPropertiesFile {

	public static final String MODEL_PROPERTIES = "model.properties";

	public static String getUrlViewLayer() {
		Properties prop = new Properties();
		prop = getProperties();
		return prop.getProperty("model.viewLayer.url");
	}

	public static Properties getProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = UsuarioWebServiceImpl.class.getClassLoader().getResourceAsStream(MODEL_PROPERTIES);
			if (input == null) {
				System.out.println("Sorry, unable to find " + MODEL_PROPERTIES);
				return null;
			}
			// load a properties file from class path, inside static method
			prop.load(input);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

}
