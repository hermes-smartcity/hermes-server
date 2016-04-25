package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Date;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.TailSendingDataSource;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.TransactionalPlainAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

/**
 * Accion para crear un envio pendiente
 *
 * Created by Leticia on 25/04/2016.
 */
public class CreateTailSendingAction implements TransactionalPlainAction {

    private Date date;
    private String routeZip;

    public CreateTailSendingAction(Date date, String routeZip){
        this.date = date;
        this.routeZip = routeZip;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        TailSendingDataSource tailDataSource = new TailSendingDataSource(db);

        tailDataSource.createTailSending(date, routeZip);

        return null;
    }
}
