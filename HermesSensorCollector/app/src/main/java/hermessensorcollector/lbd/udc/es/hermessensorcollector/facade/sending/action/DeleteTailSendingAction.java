package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.TailSendingDataSource;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.TransactionalPlainAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

/**
 * Accion para borrar el envio de la base de datos
 * Created by Leticia on 25/04/2016.
 */
public class DeleteTailSendingAction implements TransactionalPlainAction {

    private Long id;

    public DeleteTailSendingAction(Long id){
        this.id = id;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        TailSendingDataSource tailDataSource = new TailSendingDataSource(db);

        //Recuperamos la lista de fotos a enviar (para poder tener la ruta del zip a borrar)
        List<TailSending> listado = tailDataSource.getAll();
        for (TailSending envio:listado) {
            String ruta = envio.getRoutezip();

            //Borramos todas las fotos del disco
            File file = new File(ruta);
            file.delete();
        }

        //Borramos todos los envios de la base de datos
        tailDataSource.deleteAll();

        return null;
    }
}
