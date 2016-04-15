package hermessensorcollector.lbd.udc.es.hermessensorcollector.sql;

/**
 * Una interface 'marker' para especificar acciones ordinarias transaccionales.
 * Una accion es transaccional cuando necesita que todas sus operaciones vayan
 * sobre la misma transaccion, de modo que si se produce algun error en alguna
 * de sus operaciones se hace roll back de todo.
 *
 * @author Leticia Riestra Ainsua
 */
public interface TransactionalPlainAction extends PlainAction {}