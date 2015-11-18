package es.udc.lbd.hermes.eventManager;

import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


// Singleton . Sera guardado en session. Desde aqui se llamara a eventProcessor, que escuchará y almacenará los eventos
@Component
public class EventManager{

	private Logger logger = LoggerFactory.getLogger(getClass());
	private final Semaphore semaphore = new Semaphore(1, true);
	private EventProcessor eventProcessor;

	public EventManager(){
		logger.warn("EventManager created");
	}
	
	public void startEventProcessor() {
		
		if (semaphore.tryAcquire()) {
			if (eventProcessor == null) {
				eventProcessor = new EventProcessor();
				eventProcessor.start();				
				logger.warn("EventProcessor started");
				// TODO: Enviar mensaje de éxito hacia la vista					
			} else {
				logger.warn("EventProcessor was already running. It will not be started again.");
				//TODO: Enviar mensaje de error hacia la vista
			}
			semaphore.release();
		} else {
			logger.warn("Could not acquire Semaphore. Nothing done.");
			//TODO: Enviar mensaje de error hacia la vista
		}
	}

	public void stopEventProcessor() /*throws PararEventManagerException */{
		if (semaphore.tryAcquire()) {
			if (eventProcessor != null) {
				eventProcessor.interrupt();
				eventProcessor = null;				
				logger.warn("EventProcessor stopped");
				// TODO: Enviar mensaje de éxito hacia la vista
			} else {
				logger.warn("EventProcessor was not running. It will not be stopped.");
				//TODO: Enviar mensaje de error hacia la vista
			}
			semaphore.release();
		} else {
			logger.warn("Could not acquire Semaphore. Nothing done.");
			//TODO: Enviar mensaje de error hacia la vista
		}
	}	
}