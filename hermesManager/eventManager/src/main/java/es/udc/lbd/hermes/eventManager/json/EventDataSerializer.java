package es.udc.lbd.hermes.eventManager.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class EventDataSerializer extends StdSerializer<EventData> {
	private Map<Class<? extends EventData>, String> registry = new HashMap<Class<? extends EventData>, String>();
	
	
	public EventDataSerializer() {
		super(EventData.class);
		registerEventType(ZtreamyVehicleLocation.class, "Location");  
		registerEventType(ZtreamyHighSpeed.class, "High Speed");  
		registerEventType(ZtreamyHighAcceleration.class, "High Acceleration");  		
		registerEventType(ZtreamyHighDeceleration.class, "High Deceleration");  		
		registerEventType(ZtreamyHighHeartRate.class, "High Heart Rate");
		registerEventType(ZtreamyDriverFeatures.class, "Driver Features");
		registerEventType(ZtreamyDataSection.class, "Data Section");
		registerEventType(ZtreamyStepsData.class, "Steps Data");
		registerEventType(ZtreamyContextDataList.class, "Context Data");
		registerEventType(ZtreamySleepData.class, "Sleep Data");
		registerEventType(ZtreamyHeartRateData.class, "Heart Rate Data");
	}
	
	void registerEventType(Class<? extends EventData> eventTypeClass, String uniqueName) {
		registry.put(eventTypeClass, uniqueName);
	}
	

	@Override
	public void serialize(EventData value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
		String fieldName = registry.get(value.getClass());
		if (fieldName!=null) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode valueNode = mapper.valueToTree(value);
			gen.writeStartObject();
			gen.writeObjectField(fieldName, valueNode);
			gen.writeEndObject();
		}
	}
}
