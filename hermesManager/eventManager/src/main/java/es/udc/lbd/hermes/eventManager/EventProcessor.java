package es.udc.lbd.hermes.eventManager;


import org.glassfish.jersey.client.ChunkedInput;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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

//Contexto para el patron strategy
@Component
public class EventProcessor extends Thread {
	
	private static final String URI = ReadPropertiesFile.getUrlEventos();
	private static final String URI_RECUPERAR_EVENTOS_PASADOS = ReadPropertiesFile.getUrlEventosPasados();
	private Logger logger = LoggerFactory.getLogger(getClass());
	

	// Escucha los eventos pasados, le envian en un buffer todos los eventos durante el tiempo de desconexión
	public void escucharEventosPasados(){
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		EventoProcesado eventoProcesado = eventService.obterEventoProcesado();
		
		if(eventoProcesado!=null){			
			logger.info("Escuchando eventos pasados");			
			final Response response = establecerConexion(URI_RECUPERAR_EVENTOS_PASADOS+eventoProcesado.getEventId(),"application/json");
			System.out.println("-url- "+URI_RECUPERAR_EVENTOS_PASADOS+eventoProcesado.getEventId());
			procesarEventosEnviadosPasados(response);
		}
	}

	// Escucha los eventos que se le envían en tiempo real
	public void escucharEventos(){		
		EventService eventService = ApplicationContextProvider.getApplicationContext().getBean("eventService", EventService.class);
		EventoProcesado eventoProcesado = eventService.obterEventoProcesado();
		System.out.println("-url- "+URI);
		final Response response = establecerConexion(URI+eventoProcesado.getEventId(), "application/x-ldjson");
		System.out.println("-url 2- "+URI);
		logger.info("Escuchando eventos en tiempo real");	
		procesarEventosEnviados(response);
	}

	// Comportamiento del thread
	public void run() {	
//		escucharEventosPasados();	
		escucharEventos();
		
	}

	//Establecemos conexion con el cliente que nos envía los eventos
	private Response establecerConexion(String url, String cabecera){
		final Client client = ClientBuilder.newBuilder()
				.register(JacksonFeature.class).build();

		final Response response = client.target(url).request()
				.accept(cabecera).get();

		return response;
	}

	// Procesamos los eventos enviamos en formato JSON
	private void procesarEventosEnviados(Response response){
		String chunk;
		JSONParser parser = new JSONParser();

		final ChunkedInput<String> chunkedInput = response
				.readEntity(new GenericType<ChunkedInput<String>>() {
				});

		logger.info("Procesando eventos");

		while ((chunk = chunkedInput.read()) != null && !Thread.currentThread().isInterrupted()) {					
			Object obj;
			try {				
				System.out.println("chunk "+chunk);
				obj = parser.parse(chunk);
				JSONObject eventoJSON = (JSONObject) obj;
				InputStream is = new ByteArrayInputStream(eventoJSON.toString().getBytes());
				EventParser eventParser = new EventParser();
				Event event = eventParser.parse(is);				
				procesarEvento(event);				
			} catch (ParseException | IOException e) {
				logger.error("Error convirtiendo chunk a JSON: "+e);				
				e.printStackTrace();					
			}		
		}

		return;
	}

	private void procesarEventosEnviadosPasados(Response response){

		String chunk;
		JSONParser parser = new JSONParser();

		final ChunkedInput<String> chunkedInput = response
				.readEntity(new GenericType<ChunkedInput<String>>() { });

		chunk = chunkedInput.read();
		while ((chunk = chunkedInput.read()) != null && !Thread.currentThread().isInterrupted()) {	
			Object obj;
			try {
				
				obj = parser.parse(chunk);
				JSONObject eventoJSON = (JSONObject) obj;
				InputStream is = new ByteArrayInputStream(eventoJSON.toString().getBytes());
				EventParser eventParser = new EventParser();
				Event event = eventParser.parse(is);	
				procesarEvento(event);	
//				chunk = chunkedInput.read();				
//				obj = parser.parse(chunk);
//				System.out.println("-- "+chunk);
				// Me envían un array JSON con todos los eventos pasados
//				JSONArray eventosArray = (JSONArray) obj;
	//			for(int i=0;i<eventosArray.size();i++){
	//				JSONObject eventoJSON = (JSONObject) eventosArray.get(i);
	//				InputStream is = new ByteArrayInputStream(eventoJSON.toString().getBytes());
	//				EventParser eventParser = new EventParser();
	//				Event event = eventParser.parse(is);				
	//				procesarEvento(event);		
	//			}
			} catch (ParseException | IOException e) {
				logger.error("Error convirtiendo chunk a JSON: "+e);				
				e.printStackTrace();					
			}
		}
		chunkedInput.close();
		
				return;
		}

		// Almacenamos los diferentes tipos de eventos en la BD
		private void procesarEvento(Event event){			
			
			EventType tipoEvento = EventType.getTipo((String) event.getEventType());
			
			EventStrategy estrategia = EventFactory.getStrategy(tipoEvento);
			estrategia.processEvent(event);
			logger.info("Guardado el evento con Event-Type: "+tipoEvento.getName());
			

		}
	}