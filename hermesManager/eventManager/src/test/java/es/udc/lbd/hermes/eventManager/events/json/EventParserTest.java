package es.udc.lbd.hermes.eventManager.events.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventData;
import es.udc.lbd.hermes.eventManager.json.EventDataArray;
import es.udc.lbd.hermes.eventManager.json.EventParser;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserActivity;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserCaloriesExpended;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserDistances;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserHeartRates;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserLocation;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserSleep;
import es.udc.lbd.hermes.eventManager.json.ZtreamyUserSteps;

public class EventParserTest {

	@Test
	public void testVehicleLocation() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/vehiclelocation.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testHighSpeed() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/highspeed.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testHighAcceleration() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/highacceleration.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testHighDeceleration() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/highdeceleration.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testHighHeartRate() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/highheartrate.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testDriverFeatures() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/driverfeatures.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testDataSection() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/datasection.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testStepsData() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/stepsdata.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testSleepData() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/sleepdata.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testHeartRateData() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/heartratedata.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testContextData() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/contextdata.json"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}

	@Test
	public void testUserActivities() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/useractivities.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserActivity) {
						Assert.assertEquals("unknown", ((ZtreamyUserActivity)eventData).getName());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testUserLocations() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/userlocations.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserLocation) {
						Assert.assertEquals(new Integer(30), ((ZtreamyUserLocation)eventData).getAccuracy());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	/*@Test
	public void testFullUserActivities() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/fulluseractivities.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserActivity) {
						Assert.assertEquals("unknown", ((ZtreamyUserActivity)eventData).getName());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testFullUserLocations() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/fulluserlocations.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserLocation) {
						Assert.assertEquals(new Integer(30), ((ZtreamyUserLocation)eventData).getAccuracy());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}*/
	
	/*@Test
	public void testUserDistances() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/userdistances.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserDistances) {
						Assert.assertEquals(new Float(5.3), ((ZtreamyUserDistances)eventData).getDistance());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testUserSteps() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/usersteps.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserSteps) {
						Assert.assertEquals(new Integer(20), ((ZtreamyUserSteps)eventData).getSteps());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testUserCaloriesExpended() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/usercaloriesexpended.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserCaloriesExpended) {
						Assert.assertEquals(new Float(5.3), ((ZtreamyUserCaloriesExpended)eventData).getCalories());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testUserHeartRates() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/userheartrates.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserHeartRates) {
						Assert.assertEquals(new Float(5.3), ((ZtreamyUserHeartRates)eventData).getBpm());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}
	
	@Test
	public void testUserSleep() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/usersleep.json"));			
			if (event.getEventData() instanceof EventDataArray) {
				EventDataArray eventDataArray = (EventDataArray)event.getEventData();
				for (EventData eventData : eventDataArray.getEvents()) {
					if (eventData instanceof ZtreamyUserSleep) {
						Assert.assertEquals("sleep.light", ((ZtreamyUserSleep)eventData).getName());
					}
				}
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getLocalizedMessage());
		}		
	}*/
	
}
