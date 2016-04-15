package hermessensorcollector.lbd.udc.es.hermessensorcollector.sql;

import android.database.sqlite.SQLiteDatabase;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;


/**
 * Una interface base de todas las acciones 'ordinarias'. Una accion 'ordinaria' es una accion
 * de negocio ejecutada con una clase Java ordinaria
 *
 * @author Leticia Riestra Ainsua
 */
public interface PlainAction {

    /**
     * Ejecuta todas las operaciones a realizar por la accion. Recibe la <code>SQLiteDatabase</code>
     * para poder agrupar dichas operaciones en la misma transaccion en el caso de acciones
     * transaccionales.
     *
     * @param db	la <code>SQLiteDatabase</code> a utilizar para llevar a cabo las
     *						operaciones de la accion.
     *
     * @throws ModelException 			si se produce algun error logico (de modelo).
     * @throws InternalErrorException 	si se produce algun error grave.
     */
    Object execute(SQLiteDatabase db) throws ModelException, InternalErrorException;

}
