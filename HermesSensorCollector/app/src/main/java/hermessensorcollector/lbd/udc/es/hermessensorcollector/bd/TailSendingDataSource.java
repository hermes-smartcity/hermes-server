package hermessensorcollector.lbd.udc.es.hermessensorcollector.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

/**
 * Created by Leticia on 22/04/2016.
 */
public class TailSendingDataSource {

    private SQLiteDatabase db;

    private String[] columnasColaEnvio = { TablesDB.TAILSENDING_COLUMNA_ID,
            TablesDB.TAILSENDING_COLUMNA_TYPE,
            TablesDB.TAILSENDING_COLUMNA_DATE,
            TablesDB.TAILSENDING_COLUMNA_ROUTEZIP

    };

    public TailSendingDataSource(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Borrar todos los datos de la base de datos
     */
    public void deleteAll() {
        db.delete(TablesDB.TABLA_TAILSENDING, null, null);
    }

    /**
     * Borrar el envio indicada de la base de datos
     *
     * @param id Identificador del envio a eliminar
     */
    public void deleteSending(Long id) {
        db.delete(TablesDB.TABLA_TAILSENDING, TablesDB.TAILSENDING_COLUMNA_ID + " = " + id, null);
    }


    /**
     * Crear una entrada de cola de envio
     * @param date Fecha
     * @param routezip Ruta del zip
     * @return long identificador de la entrada creada
     */
    public long createTailSending(String type, Date date, String routezip) {

        ContentValues values = new ContentValues();

        Locale locale = Utils.getLocale();
        SimpleDateFormat dateFormat = new SimpleDateFormat(Utils.FECHA_SQLITE, locale);
        String fecha = dateFormat.format(date);

        values.put(TablesDB.TAILSENDING_COLUMNA_TYPE, type);
        values.put(TablesDB.TAILSENDING_COLUMNA_DATE, fecha);
        values.put(TablesDB.TAILSENDING_COLUMNA_ROUTEZIP, routezip);

        long id = db.insert(TablesDB.TABLA_TAILSENDING, null, values);

        return id;
    }

    /**
     * Listar todas las colas de envio de la base de datos
     *
     * @return List<TailSending> Listado con la informacion de cada una de las colas de envio
     * existentes
     */
    public List<TailSending> getAll() {

        List<TailSending> lista = new ArrayList<TailSending>();

        Cursor cursor = db.query(TablesDB.TABLA_TAILSENDING, columnasColaEnvio, null, null,
                null, null, TablesDB.TAILSENDING_COLUMNA_DATE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TailSending nuevo = cursorToTailSending(cursor);
            lista.add(nuevo);
            cursor.moveToNext();
        }

        cursor.close();

        return lista;
    }

    /**
     * Listar todas las colas de envio de la base de datos
     *
     * @param type Tipo de elemento a recuperar
     * @return List<TailSending> Listado con la informacion de cada una de las colas de envio
     * existentes
     */
    public List<TailSending> getAllFromType(String type) {

        List<TailSending> lista = new ArrayList<TailSending>();

        Cursor cursor = db.query(TablesDB.TABLA_TAILSENDING, columnasColaEnvio,
                TablesDB.TAILSENDING_COLUMNA_TYPE + "=?", new String[] { type },
                null, null, TablesDB.TAILSENDING_COLUMNA_DATE);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            TailSending nuevo = cursorToTailSending(cursor);
            lista.add(nuevo);
            cursor.moveToNext();
        }

        cursor.close();

        return lista;
    }

    /**
     * Cursor para movernos por la cola de envio y recuperar la informacion asociada a la misma
     * @param cursor Cursor a utilizar
     * @return TailSending Objeto TailSending con la informacion
     */
    private TailSending cursorToTailSending(Cursor cursor) {
        TailSending objeto = new TailSending();
        if (cursor.getCount()!=0){ //por si buscan un parametro que no existe
            objeto.setId(cursor.getLong(0));

            objeto.setType(cursor.getString(1));

            String fecha = cursor.getString(2);
            Locale locale = Utils.getLocale();
            Date fechaEnvio = Utils.obtenerDateSegunFormato(fecha,Utils.FECHA_SQLITE, locale );
            objeto.setDate(fechaEnvio);

            objeto.setRoutezip(cursor.getString(3));

            return objeto;
        }else{
            return null;
        }
    }
}
