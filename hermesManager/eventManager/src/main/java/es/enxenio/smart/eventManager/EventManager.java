package es.enxenio.smart.eventManager;

import java.util.concurrent.Semaphore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


// Singleton . Sera guardado en session. Desde aqui se llamara a eventProcessor, que escuchará y almacenará los eventos
@Component
public class EventManager{

	private final Semaphore semaphore = new Semaphore(1, true);
	// Antiguo eventManager convertido en Thread. Escuchará y almacenará los eventos
	@Autowired
	private Thread eventProcessor;

	public EventManager(){

	}
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void startEventProcessor(){
		this.eventProcessor =  new EventProcessor();
		// Comienzo a escuchar todos los eventos desde el ultimo que me enviaron
		this.eventProcessor.start();
		
	}

	public void stopEventProcessor() /*throws PararEventManagerException */{
		this.eventProcessor.interrupt();			
		this.eventProcessor = null;
	
	}

}