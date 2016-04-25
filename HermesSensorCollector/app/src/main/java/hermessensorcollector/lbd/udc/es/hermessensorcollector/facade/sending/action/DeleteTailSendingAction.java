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

    private TailSending sending;

    public DeleteTailSendingAction(TailSending sending){
        this.sending = sending;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        TailSendingDataSource tailDataSource = new TailSendingDataSource(db);

        //Borramos de disco el zip
        File file = new File(sending.getRoutezip());
        file.delete();

        //Borramos todos los envios de la base de datos
        tailDataSource.deleteSending(sending.getId());

        return null;
    }
}
