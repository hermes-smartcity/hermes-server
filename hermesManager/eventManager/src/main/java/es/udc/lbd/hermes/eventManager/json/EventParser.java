package es.udc.lbd.hermes.eventManager.json;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class EventParser {
	private ObjectMapper mapper;
	static Logger logger = Logger.getLogger(EventParser.class);
	
	public EventParser() {
		mapper = new ObjectMapper();
	    EventDataDeserializer deserializer = new EventDataDeserializer();  
	    EventDataSerializer serializer = new EventDataSerializer();
	    SimpleModule module = new SimpleModule("EventDataDeserializerModule", new Version(1, 0, 0, null, null, null));  
	    module.addDeserializer(EventData.class, deserializer);  
	    module.addSerializer(EventData.class, serializer);
	    mapper.registerModule(module);
	}

	public Event parse(InputStream in) throws JsonMappingException, JsonParseException, IOException {
		return mapper.readValue(in, Event.class);
	}
	
	public Event parse(String str) throws JsonMappingException, JsonParseException, IOException {
		return mapper.readValue(str, Event.class);
	}
	
	public String prettyPrint(Event event) {
		String eventAsString;
		try {
			eventAsString= mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
		} catch (JsonProcessingException e) {
			logger.error(e.getLocalizedMessage(), e);
			eventAsString = event.toString();
		}
		return eventAsString;		
	}
}
