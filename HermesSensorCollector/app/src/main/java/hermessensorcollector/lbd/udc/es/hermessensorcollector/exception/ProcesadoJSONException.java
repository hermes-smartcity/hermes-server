package hermessensorcollector.lbd.udc.es.hermessensorcollector.exception;

/**
 * Clase para capturar errores durante el procesado del Json
 * 
 * @author Leticia Riestra Ainsua
 *
 */
public class ProcesadoJSONException extends ModelException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2438483268254774996L;
	
	private Exception encapsulatedException;
	
	public ProcesadoJSONException() {
	
	}
	
	public ProcesadoJSONException(Exception exception) {
		encapsulatedException = exception;
	}
	
	public String getMessage() {
		return encapsulatedException.getMessage();
	}
	
	public Exception getEncapsulatedException() {
		return encapsulatedException;
	}
	
}
