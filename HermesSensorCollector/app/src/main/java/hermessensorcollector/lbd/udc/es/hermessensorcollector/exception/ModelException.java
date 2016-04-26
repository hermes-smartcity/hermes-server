package hermessensorcollector.lbd.udc.es.hermessensorcollector.exception;


/**
 * Clase excepcion que representa un error logico en el modelo. Esta clase es la raiz de todas
 * las excepcions del modelo.
 *
 * @author Leticia Riestra Ainsua
 */

@SuppressWarnings("serial")
public abstract class ModelException extends Exception {

    /**
     * Constructor de clase.
     */
    protected ModelException() {}

    /**
     * Constructor de clase indicando un mensaje que describe la excepcion producida.
     */
    protected ModelException(String message) {
        super(message);
    }

}