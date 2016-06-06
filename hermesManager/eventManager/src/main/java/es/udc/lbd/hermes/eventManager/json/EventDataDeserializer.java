package es.udc.lbd.hermes.eventManager.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class EventDataDeserializer extends StdDeserializer<EventData> {
	
	private static final long serialVersionUID = -4688388124135286016L;
	
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
		registerEventType("User Activities", ZtreamyUserActivityList.class);
		registerEventType("User Locations", ZtreamyUserLocationList.class);
		registerEventType("Full User Activities", ZtreamyUserActivityList.class);
		registerEventType("Full User Locations", ZtreamyUserLocationList.class);
	}

	void registerEventType(String uniqueName, Class<? extends EventData> eventTypeClass) {
		registry.put(uniqueName, eventTypeClass);
	}

	@Override
	public EventData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Class<? extends EventData> eventTypeClass = null;
		JsonNode eventTypeData = null;
		String eventTypeKey = "";

		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
		JsonNode root = mapper.readTree(jp);		
		Iterator<Entry<String, JsonNode>> elementsIterator = root.fields();
		while (elementsIterator.hasNext()) {
			Entry<String, JsonNode> element = elementsIterator.next();
			String name = element.getKey();
			if (registry.containsKey(name)) {
				eventTypeClass = registry.get(name);
				eventTypeData = element.getValue();
				eventTypeKey = element.getKey();
				break;
			}
		}
		if (eventTypeClass != null) {
			if (eventTypeData instanceof ArrayNode) {
				return null;
			} else {
				return mapper.treeToValue(eventTypeData, eventTypeClass);
			}
		} else {
			return null;
		}		
	}
}
