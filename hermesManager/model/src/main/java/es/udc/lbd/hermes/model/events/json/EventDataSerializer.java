package es.udc.lbd.hermes.model.events.json;

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
		registerEventType(VehicleLocation.class, "Location");  
		registerEventType(HighSpeed.class, "High Speed");  
		registerEventType(HighAcceleration.class, "High Acceleration");  		
		registerEventType(HighDeceleration.class, "High Deceleration");  		
		registerEventType(HighHeartRate.class, "High Heart Rate");
		registerEventType(DriverFeatures.class, "Driver Features");
		registerEventType(DataSection.class, "Data Section");
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
