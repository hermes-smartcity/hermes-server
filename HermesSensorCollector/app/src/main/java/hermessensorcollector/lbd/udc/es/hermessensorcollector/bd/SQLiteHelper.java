package hermessensorcollector.lbd.udc.es.hermessensorcollector.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;

/**
 * Clase para la creacion de las tablas usadas en la base de datos
 *
 * Created by Leticia on 14/04/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper{

    private static int version = 1;
    private static String name = "HermesDb";
    private static CursorFactory factory = null;

    public SQLiteHelper(Context contexto) {
        super(contexto, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Sentencias de creacion de las tablas
        construirTablaParametros(db);

        //url del servidor
        insertarUrlService(db);
    }

    private void construirTablaParametros(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE " + TablesDB.TABLA_PARAMETERS
                + " (" + TablesDB.PARAM_COLUMNA_ID
                + " INTEGER PRIMARY KEY, "
                + TablesDB.PARAM_COLUMNA_NAME + " TEXT NOT NULL, "
                + TablesDB.PARAM_COLUMNA_VALUE + " TEXT NOT NULL )";

        db.execSQL(sqlCreate);

        Log.i(this.getClass().toString(), "Table " + TablesDB.TABLA_PARAMETERS + " created");

    }

    private void insertarUrlService(SQLiteDatabase db){

        if (db.isReadOnly()) {
			db = getWritableDatabase();
		}

        String sqlInsert = "INSERT INTO " + TablesDB.TABLA_PARAMETERS + " ("
			        + TablesDB.PARAM_COLUMNA_NAME + ","
				    + TablesDB.PARAM_COLUMNA_VALUE + ")"
				+ " VALUES ('" + Constants.SERVICE_URL +"','https://192.168.1.119:1401')";

		db.execSQL(sqlInsert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

    }
}
