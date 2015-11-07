package es.udc.lbd.hermes.eventManager;

public class HelperEventManager {
	
	public static void startEventManager(EventManager eventManager) throws InterruptedException  {
		if(eventManager.getSemaphore().tryAcquire()){
			eventManager.startEventProcessor();
			eventManager.getSemaphore().release();	
		}
	}

	public static  void stopEventManager(EventManager eventManager) throws InterruptedException{		
		if(eventManager.getSemaphore().tryAcquire()){
			eventManager.stopEventProcessor();
			eventManager.getSemaphore().release();
		} //TODO else ENVIAR ALGO PARA DEVOLVER UN MENSAJE EN LA VISTA DE QUE NO COGI� EL SEMAFORO
	}

}
