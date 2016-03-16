package es.udc.lbd.hermes.eventManager.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class EventDataDeserializer extends StdDeserializer<EventData> {
	private Map<String, Class<? extends EventData>> registry = new HashMap<String, Class<? extends EventData>>();

	public EventDataDeserializer() {
		super(EventData.class);
		registerEventType("Location", ZtreamyVehicleLocation.class);  
		registerEventType("High Speed", ZtreamyHighSpeed.class);  
		registerEventType("High Acceleration", ZtreamyHighAcceleration.class);  		
		registerEventType("High Deceleration", ZtreamyHighDeceleration.class);
		registerEventType("High Heart Rate", ZtreamyHighHeartRate.class);
		registerEventType("Driver Features", ZtreamyDriverFeatures.class);
		registerEventType("Data Section", ZtreamyDataSection.class);
		registerEventType("Steps Data", ZtreamyStepsData.class);
		registerEventType("Context Data", ZtreamyContextDataList.class);
		registerEventType("Sleep Data", ZtreamySleepData.class);
		registerEventType("Heart Rate Data", ZtreamyHeartRateData.class);
	}

	void registerEventType(String uniqueName, Class<? extends EventData> eventTypeClass) {
		registry.put(uniqueName, eventTypeClass);
	}

	@Override
	public EventData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Class<? extends EventData> eventTypeClass = null;
		JsonNode eventTypeData = null;

		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode root = mapper.readTree(jp);		
		Iterator<Entry<String, JsonNode>> elementsIterator = root.fields();
		while (elementsIterator.hasNext()) {
			Entry<String, JsonNode> element = elementsIterator.next();
			String name = element.getKey();
			if (registry.containsKey(name)) {
				eventTypeClass = registry.get(name);
				eventTypeData = element.getValue();
				break;
			}
		}
		if (eventTypeClass != null) {
			return mapper.treeToValue(eventTypeData, eventTypeClass);
		} else {
			return null;
		}		
	}
}
