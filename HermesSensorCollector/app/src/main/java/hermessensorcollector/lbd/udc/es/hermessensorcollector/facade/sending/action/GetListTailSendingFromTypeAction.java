package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action;

import android.database.sqlite.SQLiteDatabase;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.TailSendingDataSource;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.NonTransactionalPlainAction;


/**
 * Accion que se encarga de recuperar la lista de envios pendientes
 *
 * Created by Leticia on 14/04/2016.
 */
public class GetListTailSendingFromTypeAction implements NonTransactionalPlainAction {

    private String type;

    public GetListTailSendingFromTypeAction(String type){
        this.type = type;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        TailSendingDataSource tailDataSource = new TailSendingDataSource(db);

        return tailDataSource.getAllFromType(type);
    }

}
