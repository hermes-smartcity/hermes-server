package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.action;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.ParametersDataSource;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.TransactionalPlainAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;


/**
 * Accion que se encarga de actualizar la informacion de la lista de parametros
 *
 * Created by Leticia on 14/04/2016.
 */
public class UpdateParametersSettingsAction implements TransactionalPlainAction {

    private List<Parameter> list;

    public UpdateParametersSettingsAction(List<Parameter> list){
        this.list = list;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        ParametersDataSource parametroDataSource = new ParametersDataSource(db);

        for(int i=0;i<list.size();i++){
            Parameter param = list.get(i);

            //Si el parametro existe, es una actualizacion. Si no existe, es una insercion
            Parameter paramExiste = parametroDataSource.getValueParameter(param.getName());
            if (paramExiste!=null){
                parametroDataSource.updateValueParameter(param.getName(), param.getValue());
            }else{
                parametroDataSource.createParameter(param.getName(), param.getValue());
            }
        }

        return null;
    }
}
