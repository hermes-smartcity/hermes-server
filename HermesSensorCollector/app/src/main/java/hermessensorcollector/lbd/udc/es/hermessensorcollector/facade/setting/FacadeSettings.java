package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.action.GetListValueParametersByNameAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.action.UpdateParametersSettingsAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.PlainActionProcessor;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;


/**
 * Fachada con los metodos relacionados con la pantalla de settings
 *
 * Created by Leticia on 14/04/2016.
 */
public class FacadeSettings {

    private ApplicationContext applicationContext;

    public FacadeSettings(ApplicationContext controller){
        this.applicationContext = controller;
    }

    public List<Parameter> getListValueParameters(List<String> names) throws InternalErrorException {
        List<Parameter> listadoParametro;
        try {
            GetListValueParametersByNameAction action = new GetListValueParametersByNameAction(names);

            SQLiteDatabase db = applicationContext.getDataBase();

            listadoParametro = (List<Parameter>) PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

        return listadoParametro;
    }


    public void updateParametersSettings(List<Parameter> list)throws InternalErrorException{

        try {
            UpdateParametersSettingsAction action = new UpdateParametersSettingsAction(list);

            SQLiteDatabase db = applicationContext.getDataBase();

            PlainActionProcessor.process(db,action);

        } catch (InternalErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalErrorException(e);
        }

    }
}
