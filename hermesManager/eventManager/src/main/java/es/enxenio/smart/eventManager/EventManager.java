package es.enxenio.smart.eventManager;

import java.util.concurrent.Semaphore;


// Singleton . Sera guardado en session. Desde aqui se llamara a eventProcessor, que escuchará y almacenará los eventos
public class EventManager{

	private final Semaphore semaphore = new Semaphore(1, true);
	// Antiguo eventManager convertido en Thread. Escuchará y almacenará los eventos
	private Thread eventProcessor;

	public EventManager(){

	}
	public Semaphore getSemaphore() {
		return semaphore;
	}

	public void startEventProcessor(){
		if(this.eventProcessor==null) {
			this.eventProcessor =  new Thread(new EventProcessor());
			// Comienzo a escuchar todos los eventos desde el ultimo que me enviaron
			this.eventProcessor.start();
		}
	}

	public void stopEventProcessor() /*throws PararEventManagerException */{
		if(this.eventProcessor!=null){
				this.eventProcessor.interrupt();			
				this.eventProcessor = null;
			
		} /* else throw new PararEventManagerException();*/	

	}

}