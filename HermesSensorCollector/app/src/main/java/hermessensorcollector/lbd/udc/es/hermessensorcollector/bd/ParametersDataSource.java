package hermessensorcollector.lbd.udc.es.hermessensorcollector.bd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;


/**
 * Created by Leticia on 14/04/2016.
 */
public class ParametersDataSource {

    private SQLiteDatabase db;

    private String[] columnasParametros = { TablesDB.PARAM_COLUMNA_ID,
            TablesDB.PARAM_COLUMNA_NAME,
            TablesDB.PARAM_COLUMNA_VALUE
    };

    public ParametersDataSource(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * Borrar todos los parametros de la base de datos
     */
    public void deleteParameters() {

        db.delete(TablesDB.TABLA_PARAMETERS, null, null);

    }

    /**
     * Borrar el parametro indicada de la base de datos
     *
     * @param param Objeto Parameter a eliminar
     */
    public void deleteParameter(Parameter param) {

        long id = param.getId();
        db.delete(TablesDB.TABLA_PARAMETERS, TablesDB.PARAM_COLUMNA_ID + " = " + id,
                null);
    }

    /**
     * Borrar el parametro indicada de la base de datos a partir del identificador
     *
     * @param idParam Identificador del parametro a eliminar
     */
    public void deleteParameter(long idParam) {

        db.delete(TablesDB.TABLA_PARAMETERS, TablesDB.PARAM_COLUMNA_ID + " = " + idParam,
                null);
    }

    /**
     * Borrar el parametro indicado en la base de datos a partir del nombre
     *
     * @param name Nombre del parametro a eliminar
     */
    public void deleteParameter(String name) {

        db.delete(TablesDB.TABLA_PARAMETERS, TablesDB.PARAM_COLUMNA_NAME + " = '" + name + "'",
                null);
    }

    /**
     * Crear un nuevo parametro en la base de datos
     * @param name String con el nombre del campo
     * @param value String con el contenido del campo
     * @return long Identificador de la tupla insertada
     */
    public long createParameter(String name, String value) {

        ContentValues values = new ContentValues();
        values.put(TablesDB.PARAM_COLUMNA_NAME, name);
        values.put(TablesDB.PARAM_COLUMNA_VALUE, value);

        long id = db.insert(TablesDB.TABLA_PARAMETERS, null, values);

        return id;
    }

    /**
     * Recuperar el parametro a partir del identificador
     *
     * @param id Identificador del parametros
     * @return Parameter Objeto con toda la informacion relativa al parametro
     */
    public Parameter getParameter(int id) {

        Cursor cursor = db.query(TablesDB.TABLA_PARAMETERS, columnasParametros,
                TablesDB.PARAM_COLUMNA_ID + "=?", new String[] { String.valueOf(id) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Parameter param = null;
        if (cursor.getCount() > 0)
            param = cursorToParameter(cursor);

        cursor.close();

        return param;
    }

    /**
     * Recupera el valor de un parametro a partir del nombre del parametro
     * @param name String con el nombre del parametro a recuperar
     * @return Parameter Objeto parametro con la informacion asociada al mismo
     */
    public Parameter getValueParameter(String name){

        Cursor cursor = db.query(TablesDB.TABLA_PARAMETERS, columnasParametros,
                TablesDB.PARAM_COLUMNA_NAME + "=?", new String[] { String.valueOf(name) },
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Parameter param = null;
        if (cursor.getCount() > 0)
            param = cursorToParameter(cursor);

        cursor.close();

        return param;
    }


    /**
     * Listar todas los parametros de la base de datos
     *
     * @return List<Parameter> Listado con la informacion de cada una de los parametros
     * existentes
     */
    public List<Parameter> getAllParameters() {

        List<Parameter> listaParametro = new ArrayList<Parameter>();

        Cursor cursor = db.query(TablesDB.TABLA_PARAMETERS, columnasParametros, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Parameter nuevoParam = cursorToParameter(cursor);
            listaParametro.add(nuevoParam);
            cursor.moveToNext();
        }

        cursor.close();

        return listaParametro;
    }


    /**
     * Actualizar la informacion asociada al parametro
     * @param param Objeto Parametro con el contenido de la nueva informacion
     * @return int Identificador del parametro actualizada
     */
    public int updateParameter(Parameter param) {

        ContentValues values = new ContentValues();
        values.put(TablesDB.PARAM_COLUMNA_NAME, param.getName());
        values.put(TablesDB.PARAM_COLUMNA_VALUE, param.getValue());

        // Devuelve un entero con el ID de la tabla
        int id = db.update(TablesDB.TABLA_PARAMETERS, values,
                TablesDB.PARAM_COLUMNA_ID + " = ?",
                new String[] { String.valueOf(param.getId()) });

        return id;
    }

    /**
     * Actualizar el valor de un parametro
     *
     * @param value Nuevo valor a indicar para el parametro
     * @param idParam Identificador del parametro a actualizar
     *
     * @return int Identificador del parametro ctualizada
     */
    public int updateValueParameter(String value, long idParam) {

        ContentValues values = new ContentValues();
        values.put(TablesDB.PARAM_COLUMNA_VALUE, value);

        // Devuelve un entero con el ID de la tabla
        int id = db.update(TablesDB.TABLA_PARAMETERS, values,
                TablesDB.PARAM_COLUMNA_ID + " = ?",
                new String[] { String.valueOf(idParam) });

        return id;
    }

    /**
     * Actualizar el valor de un parametro
     * @param name Nombre del parametro a actualizar
     * @param value Nuevo valor a indicar para el parametro
     * @return int Identificador del parametro ctualizada
     */
    public int updateValueParameter(String name, String value) {

        ContentValues values = new ContentValues();
        values.put(TablesDB.PARAM_COLUMNA_VALUE, value);

        // Devuelve un entero con el ID de la tabla
        int id = db.update(TablesDB.TABLA_PARAMETERS, values,
                TablesDB.PARAM_COLUMNA_NAME + " = ?",
                new String[] { String.valueOf(name) });

        return id;
    }

    /**
     * Comprueba si existe ese parametros
     * @param name Nombre del parametro a coprobar
     * @return Boolean indicando si existe o no
     */
    public Boolean existParameter(String name){

        Cursor cursor = db.query(TablesDB.TABLA_PARAMETERS, columnasParametros,
                TablesDB.PARAM_COLUMNA_NAME + "=?", new String[] { String.valueOf(name) },
                null, null, null, null);

        int numero = cursor.getCount();
        cursor.close();

        if (numero!=0)
            return true;
        else
            return false;
    }

    /**
     * Cursor para movernos por el parametro y recuperar la informacion asociada a la misma
     * @param cursor Cursor a utilizar
     * @return Parameter Objeto Parametro con la informacion
     */
    private Parameter cursorToParameter(Cursor cursor) {
        Parameter param = new Parameter();
        if (cursor.getCount()!=0){ //por si buscan un parametro que no existe
            param.setId(cursor.getLong(0));
            param.setName(cursor.getString(1));
            param.setValue(cursor.getString(2));

            return param;
        }else{
            return null;
        }
    }
}

