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
        //tiempo de espera para enviar eventos al servidor
        insertarWaitingTime(db);
        //metros entre listener del gps
        insertarMinimunDistance(db);
        //tiempo entre listener del gps
        insertarMinimunTime(db);
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
				+ " VALUES ('" + Constants.SERVICE_URL +"','http://192.168.1.100:8080/eventManager/')";

		db.execSQL(sqlInsert);
    }

    private void insertarWaitingTime(SQLiteDatabase db){

        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        String sqlInsert = "INSERT INTO " + TablesDB.TABLA_PARAMETERS + " ("
                + TablesDB.PARAM_COLUMNA_NAME + ","
                + TablesDB.PARAM_COLUMNA_VALUE + ")"
                + " VALUES ('" + Constants.WAITING_TIME +"','5')";

        db.execSQL(sqlInsert);
    }

    private void insertarMinimunDistance(SQLiteDatabase db){

        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        String sqlInsert = "INSERT INTO " + TablesDB.TABLA_PARAMETERS + " ("
                + TablesDB.PARAM_COLUMNA_NAME + ","
                + TablesDB.PARAM_COLUMNA_VALUE + ")"
                + " VALUES ('" + Constants.MINIMUM_DISTANCE +"','5')";

        db.execSQL(sqlInsert);
    }

    private void insertarMinimunTime(SQLiteDatabase db){

        if (db.isReadOnly()) {
            db = getWritableDatabase();
        }

        String sqlInsert = "INSERT INTO " + TablesDB.TABLA_PARAMETERS + " ("
                + TablesDB.PARAM_COLUMNA_NAME + ","
                + TablesDB.PARAM_COLUMNA_VALUE + ")"
                + " VALUES ('" + Constants.MINIMUM_TIME +"','1')";

        db.execSQL(sqlInsert);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {

        if (versionNueva > versionAnterior) {
            //tiempo de espera para enviar eventos al servidor
            insertarWaitingTime(db);
            //metros entre listener del gps
            insertarMinimunDistance(db);
            //tiempo entre listener del gps
            insertarMinimunTime(db);
        }
    }
}
