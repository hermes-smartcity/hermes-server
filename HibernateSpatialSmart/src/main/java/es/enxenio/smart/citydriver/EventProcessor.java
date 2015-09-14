package es.enxenio.smart.citydriver;

import es.enxenio.smart.citydriver.factory.EventFactory;
import es.enxenio.smart.citydriver.strategy.EventStrategy;

import org.glassfish.jersey.client.ChunkedInput;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import es.enxenio.smart.citydriver.model.events.EventType;
import es.enxenio.smart.citydriver.model.events.eventoProcesado.EventoProcesado;
import es.enxenio.smart.citydriver.model.events.service.EventService;


//Contexto para el patron strategy
public class EventProcessor extends Thread {

	private static final String URI = "http://hermes1.gast.it.uc3m.es:9102/dbfeed-test/stream";
	private static final String URI_RECUPERAR_EVENTOS_PASADOS = "http://hermes1.gast.it.uc3m.es:9102/dbfeed/long-polling?last-seen=";
	ApplicationContext context = new FileSystemXmlApplicationContext("src\\main\\java\\es\\enxenio\\smart\\citydriver\\spring-config.xml");
	private Logger logger = LoggerFactory.getLogger(getClass());

	// Escucha los eventos pasados, le envian en un buffer todos los eventos durante el tiempo de desconexión
	public void escucharEventosPasados(){
		EventService eventService = (EventService) context.getBean("eventService");
		EventoProcesado eventoProcesado = eventService.obterEventoProcesado();
		
		if(eventoProcesado!=null){
			logger.info("Escuchando eventos pasados");			
			final Response response = establecerConexion(URI_RECUPERAR_EVENTOS_PASADOS+eventoProcesado.getEventId(), "application/json");	
			procesarEventosEnviadosPasados(response);
		}	
	}

	// Escucha los eventos que se le envían en tiempo real
	public void escucharEventos(){
		final Response response = establecerConexion(URI, "application/x-ldjson");
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
				obj = parser.parse(chunk);
				JSONObject eventoJSON = (JSONObject) obj;
				System.out.println("eventoJSON: "+eventoJSON);
				procesarEvento(eventoJSON);				
			} catch (ParseException e) {
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

		Object obj;
		try {
			chunk = chunkedInput.read();				
			obj = parser.parse(chunk);
			// Me envían un array JSON con todos los eventos pasados
			JSONArray eventosArray = (JSONArray) obj;
			
			for(int i=0;i<eventosArray.size();i++){
				JSONObject eventoJSON = (JSONObject) eventosArray.get(i);
				System.out.println("--eventoJSON: "+eventoJSON);
				//			procesarEvento(eventoJSON);
			}
		} catch (ParseException e) {
			logger.error("Error convirtiendo chunk a JSON: "+e);				
			e.printStackTrace();					
		}
		chunkedInput.close();
		
				return;
		}

		// Almacenamos los diferentes tipos de eventos en la BD
		private void procesarEvento(JSONObject eventoJSON){
			EventType tipoEvento = null;
			//		try {
			System.out.println("-------------- "+eventoJSON);
			tipoEvento = EventType.getTipo((String) eventoJSON.get("Event-Type"));
			System.out.println(" "+tipoEvento);
			EventStrategy estrategia = EventFactory.getEvent(tipoEvento);
			estrategia.processEvent(eventoJSON);
			logger.info("Guardado el evento con Event-Type: "+tipoEvento.getName());
			//		} catch (NullPointerException e) {
			//			logger.error("Excepción recuperando tipo de evento "+ e);	
			//		}		

		}
	}