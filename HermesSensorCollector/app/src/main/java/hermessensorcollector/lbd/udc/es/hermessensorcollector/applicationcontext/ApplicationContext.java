package hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.SQLiteHelper;


/**
 * Created by Leticia on 14/04/2016.
 */
public class ApplicationContext extends Application {

    private SQLiteDatabase database;

    /**
     * Metodo para recuperar una instancia de la base de datos unica para toda la aplicacion
     *
     * @return SQLiteDatabase Objeto con la conexion a la base de datos
     */
    public SQLiteDatabase getDataBase(){

        if (database==null){
			SQLiteHelper sqliteHelper = new SQLiteHelper(this);
            database = sqliteHelper.getWritableDatabase();
        }

        return database;
    }
}
