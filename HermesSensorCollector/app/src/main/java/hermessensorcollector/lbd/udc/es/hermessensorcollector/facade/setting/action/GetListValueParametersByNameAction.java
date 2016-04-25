package hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.action;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.ParametersDataSource;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ModelException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sql.NonTransactionalPlainAction;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;


/**
 * Accion que se encarga de recuperar el parametro almacenado en la tabla de parametros
 *
 * Created by Leticia on 14/04/2016.
 */
public class GetListValueParametersByNameAction implements NonTransactionalPlainAction {

    private List<String> names;

    public GetListValueParametersByNameAction(List<String> names){
        this.names = names;
    }

    @Override
    public Object execute(SQLiteDatabase db) throws ModelException,
            InternalErrorException {

        List<Parameter> listadoParametros = new ArrayList<Parameter>();

        ParametersDataSource parametroDataSource = new ParametersDataSource(db);

        for(int i=0;i<names.size();i++){
            String nombre = names.get(i);
            Parameter param = parametroDataSource.getValueParameter(nombre);
            if (param!=null)
                listadoParametros.add(param);
        }

        return listadoParametros;
    }

}
