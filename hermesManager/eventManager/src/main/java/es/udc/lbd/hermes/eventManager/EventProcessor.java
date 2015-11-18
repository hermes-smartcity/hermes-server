package es.udc.lbd.hermes.eventManager;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ChunkedInput;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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

	public EventProcessor () {
		logger.warn("EventProcessor created");
	}
	// Escucha los eventos que se le envían en tiempo real
	public void escucharEventos() {
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		EventoProcesado eventoProcesado = eventService.obterEventoProcesado();
		final Response response = establecerConexion(URI + eventoProcesado.getEventId(), "application/x-ldjson");
		logger.info("Escuchando eventos en tiempo real");
		procesarEventosEnviados(response);
	}

	// Comportamiento del thread
	public void run() {
		escucharEventos();

	}

	// Establecemos conexion con el cliente que nos envía los eventos
	private Response establecerConexion(String url, String cabecera) {
		final Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();

		final Response response = client.target(url).request().accept(cabecera).get();

		return response;
	}

	// Procesamos los eventos enviamos en formato JSON
	private void procesarEventosEnviados(Response response) {
		String chunk;

		final ChunkedInput<String> chunkedInput = response.readEntity(new GenericType<ChunkedInput<String>>() {
		});
		logger.info("Procesando eventos");
		EventParser eventParser = new EventParser();

		while ((chunk = chunkedInput.read()) != null && !Thread.currentThread().isInterrupted()) {

			try {
				Event event = eventParser.parse(chunk);
				procesarEvento(event);
			} catch (IOException e) {
				logger.error("Error convirtiendo chunk a JSON: " + e);
				e.printStackTrace();
			}

		}
		return;
	}

	// Almacenamos los diferentes tipos de eventos en la BD
	private void procesarEvento(Event event) {

		if (event.getEventType() != null) {
			EventType tipoEvento = EventType.getTipo((String) event.getEventType());
			EventStrategy estrategia = EventFactory.getStrategy(tipoEvento);
			if (estrategia != null) {
				estrategia.processEvent(event);
				logger.info("Guardado el evento con Event-Type: " + tipoEvento.getName());
			} else {
				EventParser parser = new EventParser();
				String eventAsString = parser.prettyPrint(event);
				logger.warn("EventType desconocido", eventAsString);
			}
		} else {
			logger.warn("EventType is null");
		}
		return;
	}
}