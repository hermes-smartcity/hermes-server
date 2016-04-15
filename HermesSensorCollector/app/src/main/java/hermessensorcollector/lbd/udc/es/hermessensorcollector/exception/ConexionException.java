package hermessensorcollector.lbd.udc.es.hermessensorcollector.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Clase excepcion que representa un error grave durante la conexion
 *
 * @author Leticia Riestra Ainsua
 */
public class ConexionException extends ModelException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7169020583695337192L;
	/**
	 * Atributo que encapsula la excepcion real que se produjo.
	 */
	private Exception encapsulatedException;
	
	/**
	 * Constructor de clase indicando la excepcion real que se produjo.
	 */
	public ConexionException(Exception exception) {
		encapsulatedException = exception;
	}
	
	/**
	 * Metodo para obter el mensaje de la excepcion real que se produjo.
	 *
	 * @return <code>String</code>	el mensaje de la excepcion que se produjo realmente.
	 */
	public String getMessage() {
		return encapsulatedException.getMessage();
	}

	/**
	 * Metodo para obtener la excepcion real que se produjo.
	 *
	 * @return <code>Exception</code>	la excepcion que se produjo realmente.
	 */
	public Exception getEncapsulatedException() {
		return encapsulatedException;
	}
	
	/**
	 * Metodo para imprimir la traza del error
	 */
	public void printStackTrace() {
		printStackTrace(System.err);
	}
	
	/**
	 * Metodo para imprimir la traza del error
	 */
	public void printStackTrace(PrintStream printStream) {
		super.printStackTrace(printStream);
		printStream.println("***Information about encapsulated exception***");
		encapsulatedException.printStackTrace(printStream);
	}
	
	/**
	 * Metodo para imprimir la traza del error
	 */
	public void printStackTrace(PrintWriter printWriter) {
		super.printStackTrace(printWriter);
		printWriter.println("***Information about encapsulated exception***");
		encapsulatedException.printStackTrace(printWriter);
	}
}
