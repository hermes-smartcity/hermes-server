package es.udc.lbd.hermes.eventManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.client.ChunkedInput;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import es.udc.lbd.hermes.eventManager.factory.EventFactory;
import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventParser;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.util.DeflateReaderInterceptor;
import es.udc.lbd.hermes.eventManager.util.ReadPropertiesFile;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

public class EventProcessor extends Thread {

	private static final String URI = ReadPropertiesFile.getUrlEventos();
	static Logger logger = Logger.getLogger(EventProcessor.class);
	private Client client;
	private EventParser eventParser = new EventParser();
	private Inflater inflater = new Inflater();

	// Comportamiento del thread
	public void run() {
		escucharEventos();
	}

	// Escucha los eventos que se le envían en tiempo real
	public void escucharEventos() {
		long timeToWait = 10000;
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
		client.property(ClientProperties.CONNECT_TIMEOUT, 1000 * 2); // Two seconds
	    client.property(ClientProperties.READ_TIMEOUT,    1000 * 60 * 15);  // Fifteen minutes 
		do {
			EventoProcesado eventoProcesado = eventService.obterEventoProcesado();
			Response response = establecerConexion(eventoProcesado.getEventId());			
			if (response != null) {
				logger.warn("Connection acquired!");
				timeToWait = 10000;
				procesaEventos(response);				
			}
			if (!Thread.currentThread().isInterrupted()) {
				waitBeforeReconnect(timeToWait);
				timeToWait *= 2;
			}
		} while (!Thread.currentThread().isInterrupted());
	}
	
	private Response establecerConexion(String lastEventId) {
		Response result = null;
		try {
			result = client.register(DeflateReaderInterceptor.class).target(URI + lastEventId).request().accept("application/x-ldjson").header(HttpHeaders.ACCEPT_ENCODING, "deflate").get();
		} catch (ProcessingException e) {
			logger.warn(e.getLocalizedMessage(), e);
		}
		return result;
	}
	
	private void procesaEventos(Response response) {
		String chunk;		
		ChunkedInput<String> chunkedInput = response.readEntity(new GenericType<ChunkedInput<String>>() {});
		while ((chunk = chunkedInput.read()) != null && !Thread.currentThread().isInterrupted()) {
				procesaUnEvento(chunk);
		}		
	}
	
	// Almacenamos los diferentes tipos de eventos en la BD
	private void procesaUnEvento(String chunk) {
		
		try {
			Event event = eventParser.parse(chunk);
			if (event.getEventType() != null) {
				EventType tipoEvento = EventType.getTipo((String) event.getEventType());
				EventStrategy estrategia = EventFactory.getStrategy(tipoEvento);
				if (estrategia != null) {
					try {
						estrategia.processEvent(event);
					} catch (ClassCastException e) {
						logger.error("Event-Type no coincide con el tipo especificado en el body\n"+chunk, e);
					}
					logger.info("Guardado el evento con Event-Type: " + tipoEvento.getName());
				} else {
					logger.warn("EventType desconocido: " + chunk);
				}
			} else {
				logger.warn("EventType is null: " + chunk);
			}
		} catch (IOException e) {
			logger.error("Error convirtiendo chunk a JSON", e);
			e.printStackTrace();
		}
	}
	
	private void waitBeforeReconnect(long timeToWait) {
		try {
			logger.warn("Connection failed. Trying again in " + timeToWait/1000.0 + " s");
			sleep(timeToWait);
		} catch (InterruptedException e) {
			// Interrupted. Do nothing
		}		
	}
	
	private byte[] decompress(byte[] compressed) throws DataFormatException, IOException {
		
		InputStream in = new InflaterInputStream(new ByteArrayInputStream(compressed));
		ByteArrayOutputStream out  = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
        int len;
        while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        byte[] result = out.toByteArray();
        in.close();
        out.close();
		return result;
	}
}