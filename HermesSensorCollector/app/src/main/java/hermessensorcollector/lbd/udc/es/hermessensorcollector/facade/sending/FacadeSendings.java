package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending;

import android.database.sqlite.SQLiteDatabase;

import java.util.Date;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action.CreateTailSendingAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action.DeleteAllSendingsAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action.DeleteTailSendingAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.action.GetListTailSendingAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.PlainActionProcessor;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

/**
 * Fachada con los metodos relacionados con la pantalla de sendings
 *
 * Created by Leticia on 25/04/2016.
 */
public class FacadeSendings {

    private ApplicationContext applicationContext;

    public FacadeSendings(ApplicationContext controller){
        this.applicationContext = controller;
    }

    public List<TailSending> getListTailSending() throws InternalErrorException {
        List<TailSending> listado;
        try {
            GetListTailSendingAction action = new GetListTailSendingAction();

            SQLiteDatabase db = applicationContext.getDataBase();

            listado = (List<TailSending>) PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        return listado;
    }

    public void deleteAllSendings()throws InternalErrorException{

        try {
            DeleteAllSendingsAction action = new DeleteAllSendingsAction();

            SQLiteDatabase db = applicationContext.getDataBase();

            PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

    }

    public void createTailSending(Date date, String routeZip)throws InternalErrorException{

        try {
            CreateTailSendingAction action = new CreateTailSendingAction(date, routeZip);

            SQLiteDatabase db = applicationContext.getDataBase();

            PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

    }

    public void deleteTailSending(Long id)throws InternalErrorException{

        try {
            DeleteTailSendingAction action = new DeleteTailSendingAction(id);

            SQLiteDatabase db = applicationContext.getDataBase();

            PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

    }
}
