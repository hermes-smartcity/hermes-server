package es.udc.lbd.hermes.model.osmimport.job.json;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

@SuppressWarnings("serial")
public class OSMTagDeserializer extends StdDeserializer<Tags> {

	public OSMTagDeserializer() {
		super(Overpass.class);
	}


	@Override
	public Tags deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Tags result = new Tags();

		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode root = mapper.readTree(jp);		
		Iterator<Entry<String, JsonNode>> elementsIterator = root.fields();
		while (elementsIterator.hasNext()) {
			Entry<String, JsonNode> element = elementsIterator.next();
			String name = element.getKey();
			JsonNode value = element.getValue();
			result.crearAtributo(name, value.asText());
		}
		return result;
	}
}
