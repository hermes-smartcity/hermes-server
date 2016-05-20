package es.udc.lbd.hermes.model.osmimport.job.json;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class OSMParser {
	private ObjectMapper mapper;
	static Logger logger = Logger.getLogger(OSMParser.class);
	
	public OSMParser() {
		mapper = new ObjectMapper();
	    OSMTagDeserializer deserializer = new OSMTagDeserializer();  
	    SimpleModule module = new SimpleModule("OSMTagDeserializerModule", new Version(1, 0, 0, null, null, null));  
	    module.addDeserializer(Tags.class, deserializer);  
	    mapper.registerModule(module);
	}

	public Overpass parse(InputStream in) throws JsonMappingException, JsonParseException, IOException {
		return mapper.readValue(in, Overpass.class);
	}
	
	public Overpass parse(String str) throws JsonMappingException, JsonParseException, IOException {
		return mapper.readValue(str, Overpass.class);
	}
	
	public String prettyPrint(Overpass event) {
		String osmAsString;
		try {
			osmAsString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(event);
		} catch (JsonProcessingException e) {
			logger.error(e.getLocalizedMessage(), e);
			osmAsString = event.toString();
		}
		return osmAsString;		
	}
}
