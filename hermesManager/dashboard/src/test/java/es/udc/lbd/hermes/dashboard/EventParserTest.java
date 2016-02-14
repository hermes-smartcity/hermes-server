package es.udc.lbd.hermes.dashboard;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import es.udc.lbd.hermes.eventManager.factory.EventFactory;
import es.udc.lbd.hermes.eventManager.json.Event;
import es.udc.lbd.hermes.eventManager.json.EventParser;
import es.udc.lbd.hermes.eventManager.strategy.EventStrategy;
import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.model.events.EventType;
import es.udc.lbd.hermes.model.events.dataSection.DataSection;
import es.udc.lbd.hermes.model.events.dataSection.service.DataSectionService;
import es.udc.lbd.hermes.model.events.vehicleLocation.service.VehicleLocationService;
import es.udc.lbd.hermes.model.usuario.Usuario;
import es.udc.lbd.hermes.model.usuario.service.UsuarioService;

@ContextConfiguration(locations = "classpath:application-context-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class EventParserTest {
	@Autowired private DataSectionService dataSectionServicio;
	@Autowired private UsuarioService usuarioService;
	

	@Test
    @Transactional
    @Rollback(true)	
	public void testVehicleLocation() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/vehiclelocation.json"));
			if (event.getEventType() != null) {
				EventType tipoEvento = EventType.getTipo((String) event.getEventType());
				EventStrategy estrategia = EventFactory.getStrategy(tipoEvento);
				if (estrategia != null) {
					try {
						estrategia.processEvent(event);
					} catch (ClassCastException e) {
						e.printStackTrace();
						Assert.fail(e.getLocalizedMessage());
					}
				} else {
					Assert.fail("EventType desconocido");
				}				
			} else {
				Assert.fail("EventType is null");
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
    @Transactional
    @Rollback(true)	
	public void testDataSection() {
		try {
			EventParser parser = new EventParser();
			Event event = parser.parse(this.getClass().getResourceAsStream("/datasection.json"));
			if (event.getEventType() != null) {
				EventType tipoEvento = EventType.getTipo((String) event.getEventType());
				EventStrategy estrategia = EventFactory.getStrategy(tipoEvento);
				if (estrategia != null) {
					try {
						estrategia.processEvent(event);
					} catch (Exception e) {
						e.printStackTrace();
						Assert.fail(e.getLocalizedMessage());
					}
					Usuario usuario = usuarioService.getBySourceId("950148977f923ae378fd573cfc96add7d51c965c9559b40cf531997d0ac6978f");
					Calendar ini = Helpers.getFecha("2015-06-08 00:00:00");
					Calendar fin = Helpers.getFecha("2015-06-09 00:00:00");
					List<DataSection> resultado = dataSectionServicio.obterDataSections(usuario.getId(), ini, fin, new Double(-180), new Double(-90), new Double(180), new Double(90));
					for (DataSection d:resultado) {
						Double[] accuracy = d.getAccuracy();
						System.out.print("[");
						for (int i=0; i<accuracy.length; i++) {
							if (accuracy[i] != null) {
								System.out.print(accuracy[i].doubleValue() + ", ");
							} else {
								System.out.print("null, ");
							}
						}
						System.out.println("]");
						
					}
				} else {
					Assert.fail("EventType desconocido");
				}
			} else {
				Assert.fail("EventType is null");
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
	
}
