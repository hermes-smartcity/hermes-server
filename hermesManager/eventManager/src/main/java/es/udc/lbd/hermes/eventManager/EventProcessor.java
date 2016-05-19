package es.udc.lbd.hermes.eventManager;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
import es.udc.lbd.hermes.model.efficiencytest.service.EfficiencyTestService;
import es.udc.lbd.hermes.model.events.EventProcesor;
import es.udc.lbd.hermes.model.events.eventoProcesado.EventoProcesado;
import es.udc.lbd.hermes.model.events.service.EventService;
import es.udc.lbd.hermes.model.util.ApplicationContextProvider;

public class EventProcessor extends Thread {

	private static final String URI = ReadPropertiesFile.getUrlEventos();
	static Logger logger = Logger.getLogger(EventProcessor.class);
	private Client client;
	private EventParser eventParser = new EventParser();
	EfficiencyTestService efficiencyTestService = ApplicationContextProvider.getApplicationContext().getBean("efficiencyTestService", EfficiencyTestService.class);
	private ExecutorService processors;

	// Comportamiento del thread
	public void run() {
		processors = Executors.newFixedThreadPool(100);
		escucharEventos();
		processors.shutdown();
		try {
			processors.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
		} catch (InterruptedException e) {
			logger.error("ExecutorService was interrupted. Events may have been lost\n", e);
		}
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
		
		long startTime = System.nanoTime();
		long parseTime = 0;
		boolean result = false;
		String eventType = null;
		long eventTime = -1; 
		try {
			Event event = eventParser.parse(chunk);
			eventTime = event.getTimestamp().getTimeInMillis();
			parseTime = System.nanoTime() - startTime;
			if (!"ztreamy-command".equals(event.getSyntax())) {
				if (event.getEventData() != null) { 
					if (event.getEventType() != null) {
						eventType = event.getEventType();
						EventProcesor tipoEvento = EventProcesor.getTipo(eventType);
						EventStrategy estrategia = EventFactory.getStrategy(tipoEvento, event);
						if (estrategia != null) {
							processors.execute(estrategia);
							result = true;
						} else {
							logger.warn("EventType desconocido: " + chunk);
						}
					} else {
						logger.warn("EventType is null: " + chunk);
					}
				} else {
					logger.error("Error convirtiendo chunk a JSON: "+chunk);
				}
			} else {
				logger.info("Evento ignorado: " + chunk);
			}
		} catch (IOException e) {
			logger.error("Error convirtiendo chunk a JSON: "+chunk, e);
		}
		long totalTime = System.nanoTime() - startTime;
		long delay = eventTime != -1 ? System.currentTimeMillis() - eventTime : 0;
		efficiencyTestService.create("procesaUnEvento", (long)chunk.length(), Calendar.getInstance(), delay, parseTime, totalTime, result);
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