package hermessensorcollector.lbd.udc.es.hermessensorcollector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;

public class SettingsActivity extends AppCompatActivity {

    //Objetos de la pantalla
    private EditText editTextUrlServidor;
    private Button buttonAceptarConfiguracion;
    private Button buttonCancelarConfiguracion;

    private FacadeSettings facadeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Recuperamos los objeto de la vista
        editTextUrlServidor = (EditText) this.findViewById(R.id.editTextUrlServidor);
        buttonAceptarConfiguracion = (Button) this.findViewById(R.id.buttonAceptarConfiguracion);
        buttonCancelarConfiguracion = (Button) this.findViewById(R.id.buttonCancelarConfiguracion);

        //Hacemos que el teclado este oculto por defecto
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //Recuperamos el objeto controller para pasarselo a la fachada
        ApplicationContext aController = (ApplicationContext) getApplicationContext();

        //Instanciamos la facada
        facadeSettings = new FacadeSettings(aController);

        //Recuperamos el parametro de service url para pintarlo en la pantalla
        try {

            //Recuperamos la lista de parametros que nos interesa
            List<String> paramBuscar = new ArrayList<String>();
            paramBuscar.add(Constants.SERVICE_URL);

            List<Parameter> listadoParam = facadeSettings.getListValueParameters(paramBuscar);
            String servidorURL = null;

            for (int i=0;i<listadoParam.size();i++){
                Parameter param = listadoParam.get(i);
                if (param.getName().equals(Constants.SERVICE_URL)){
                    servidorURL = param.getValue();
                }
            }

            if (servidorURL != null) {
                editTextUrlServidor.setText(servidorURL);
            }


        } catch (InternalErrorException e) {
            Log.e("SettingsActivity", "Error recuperando los parametros de la base de datos");
        }

        //Asociamos el evento al pulsar cancelar
        buttonCancelarConfiguracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Cerramos la activity
                finish();
            }
        });

        //Asociamos el evento al pulsar en el login
        buttonAceptarConfiguracion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean cancel = false;
                View focusView = null;

                editTextUrlServidor.setError(null);

                String url = editTextUrlServidor.getText().toString();

                // Compramos que la url no es vacia
                if (TextUtils.isEmpty(url)) {
                    editTextUrlServidor.setError(getString(R.string.urlRequired));
                    focusView = editTextUrlServidor;
                    cancel = true;
                }

                if (cancel) {
                    // Ha habido fallos; no continuamos con la configuracion y mostramos
                    // en el formulario el primer campo que tenga un error
                    focusView.requestFocus();
                } else {
                    //Actualizamos la informacion de configuracion generica
                    //Es decir, no tenemos en cuenta lo del usuario
                    actualizarParametrosConfiguracion();

                    //Mostramos mensaje toast diciendo que se ha actualizado la informacion
                    Utils.crearMensajeToast(SettingsActivity.this, getString(R.string.updateOK));

                    //Cerramos la activity
                    finish();
                }
            }
        });
    }

    private void actualizarParametrosConfiguracion(){
        //No hay ningun fallo asi que procedemos a actualizar los parametros de configuracion
        Parameter paramUrl = new Parameter();
        paramUrl.setName(Constants.SERVICE_URL);
        paramUrl.setValue(editTextUrlServidor.getText().toString());

        List<Parameter> listadoParametros = new ArrayList<Parameter>();
        listadoParametros.add(paramUrl);

        try {
            facadeSettings.updateParametersSettings(listadoParametros);
        } catch (InternalErrorException e) {
            Log.e("SettingsActivity", "Error actualizando la informacion de los parametros");
        }

    }


}
