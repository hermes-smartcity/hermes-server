package hermessensorcollector.lbd.udc.es.hermessensorcollector.sql;

/**
 * Una interface 'marker' para especificar acciones no transaccionales.
 * Una accion es no transaccional cuando no necesita agrupar todas sus operaciones
 * en la misma transaccion.
 *
 * @author Leticia Riestra Ainsua
 */
public interface NonTransactionalPlainAction extends PlainAction {}