package hermessensorcollector.lbd.udc.es.hermessensorcollector.sql;

import android.database.sqlite.SQLiteDatabase;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;

/**
 * Clase utilidad que permite ejecutar acciones 'ordinarias'. La ejecucion de estas acciones sera
 * distinta segun se trate de una <code>TransactionalAction</code> o de una
 * <code>NonTransactionalAction</code>.
 *
 * @author Leticia Riestra Ainsua
 */
public final class PlainActionProcessor {

    /**
     * Constructor de clase.
     */
    private PlainActionProcessor() {}

    /**
     * Ejecuta una accion <b>no transaccional</b> sobre una <code>Connection</code> con los valores
     * por defecto.
     *
     * @param db	la <code>SQLiteDatabase</code> a utilizar para llevar a cabo la accion.
     * @param action		la accion no transaccional a ejecutar.
     *
     * @throws ModelException 			si se produce algun error logico (de modelo).
     * @throws InternalErrorException 	si se produce algun error grave.
     */
    public final static Object process(SQLiteDatabase db, NonTransactionalPlainAction action)
            throws ModelException, InternalErrorException {


        try {

			/* Se ejecuta la accion. */
            Object result = action.execute(db);

			/* Se devuelve el resultado. */
            return result;

        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

    }

    /**
     * Ejecuta una accion ordinaria transaccional sobre una conexion que tiene el nivel de
     * aislamento establecido a "transaction serializable". De este modo, si la accion lanza
     * una excepcion, se hara roll back de la transaccion.
     *
     * @param db	la <code>SQLiteDatabase</code> a utilizar para llevar a cabo la accion.
     * @param action		la accion transaccional a ejecutar.
     *
     * @throws ModelException 			si se produce algun error logico (de modelo).
     * @throws InternalErrorException 	si se produce algun error grave.
     */
    public final static Object process(SQLiteDatabase db, TransactionalPlainAction action)
            throws ModelException, InternalErrorException {

        try{
            db.beginTransaction(); //abrir la transaccion

			/* Se ejecuta la accion. */
            Object result = action.execute(db);

            db.setTransactionSuccessful(); //commit de los cambios

			/* Se devuelve el resultado. */
            return result;

        } catch (Exception e) {
            throw new InternalErrorException(e);

        } finally{
            db.endTransaction(); //cerrar transaccion
        }


    }

}