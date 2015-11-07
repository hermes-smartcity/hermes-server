package es.udc.lbd.hermes.eventManager.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.udc.lbd.hermes.eventManager.EventProcessor;

public class ReadPropertiesFile {

	public static final String EVENT_MANAGER_PROPERTIES = "eventManager.properties";

	public static String getUrlEventos() {
		Properties prop = new Properties();
		prop = getProperties();
		return prop.getProperty("eventManager.eventos.url");
	}

	public static String getUrlEventosPasados() {
		Properties prop = new Properties();
		prop = getProperties();
		return prop.getProperty("eventManager.eventosPasados.url");
	}

	public static Properties getProperties() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = EventProcessor.class.getClassLoader().getResourceAsStream(EVENT_MANAGER_PROPERTIES);
			if (input == null) {
				System.out.println("Sorry, unable to find " + EVENT_MANAGER_PROPERTIES);
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
