package es.udc.lbd.hermes.eventManager.events.json;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventParser;

public class EventParserTest {

	@Test
	public void testVehicleLocation() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/vehiclelocation.json"));
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
			String prettyEvent = parser.prettyPrint(event);
			System.out.println(prettyEvent);
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
}
