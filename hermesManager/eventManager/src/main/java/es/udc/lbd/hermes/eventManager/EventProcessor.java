package es.udc.lbd.hermes.eventManager;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ChunkedInput;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.udc.lbd.hermes.eventManager.factory.EventFactory;
import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventParser;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.util.ReadPropertiesFile;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

public class EventProcessor extends Thread {

	private static final String URI = ReadPropertiesFile.getUrlEventos();
	private Logger logger = LoggerFactory.getLogger(getClass());
	private Client client;
	private EventParser eventParser = new EventParser();

	// Comportamiento del thread
	public void run() {
		escucharEventos();
	}

	// Escucha los eventos que se le envían en tiempo real
	public void escucharEventos() {
		long timeToWait = 10000;
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
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
			result = client.target(URI + lastEventId).request().accept("application/x-ldjson").get();
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
					estrategia.processEvent(event);
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
	
}