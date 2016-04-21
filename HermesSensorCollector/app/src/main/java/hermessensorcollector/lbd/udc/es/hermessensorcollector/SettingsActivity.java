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
    private EditText editTextWaitingTime;
    private EditText editTextMinimumTime;
    private EditText editTextMinimumDistance;

    private Button buttonAceptarConfiguracion;
    private Button buttonCancelarConfiguracion;

    private FacadeSettings facadeSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Recuperamos los objeto de la vista
        editTextUrlServidor = (EditText) this.findViewById(R.id.editTextUrlServidor);
        editTextWaitingTime = (EditText) this.findViewById(R.id.editTextWaitingTime);
        editTextMinimumTime = (EditText) this.findViewById(R.id.editTextMinimumTime);
        editTextMinimumDistance = (EditText) this.findViewById(R.id.editTextMinimumDistance);

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
            paramBuscar.add(Constants.WAITING_TIME);
            paramBuscar.add(Constants.MINIMUM_DISTANCE);
            paramBuscar.add(Constants.MINIMUM_TIME);

            List<Parameter> listadoParam = facadeSettings.getListValueParameters(paramBuscar);
            String servidorURL = null;
            Integer waitingTime = null;
            Long minimumTime = null;
            Long minimumDistance = null;

            for (int i=0;i<listadoParam.size();i++){
                Parameter param = listadoParam.get(i);
                if (param.getName().equals(Constants.SERVICE_URL)){
                    servidorURL = param.getValue();
                }

                if (param.getName().equals(Constants.WAITING_TIME)){
                    waitingTime = Integer.parseInt(param.getValue());
                }

                if (param.getName().equals(Constants.MINIMUM_DISTANCE)){
                    minimumTime = Long.parseLong(param.getValue());
                }

                if (param.getName().equals(Constants.MINIMUM_TIME)){
                    minimumDistance = Long.parseLong(param.getValue());
                }
            }

            if (servidorURL != null) {
                editTextUrlServidor.setText(servidorURL);
            }

            if (waitingTime != null){
                editTextWaitingTime.setText(String.valueOf(waitingTime));
            }

            if (minimumTime != null){
                editTextMinimumTime.setText(String.valueOf(minimumTime));
            }

            if (minimumDistance != null){
                editTextMinimumDistance.setText(String.valueOf(minimumDistance));
            }


        } catch (InternalErrorException e) {
            e.printStackTrace();
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
                editTextWaitingTime.setError(null);
                editTextMinimumTime.setError(null);
                editTextMinimumDistance.setError(null);

                // Compramos que la url no es vacia
                String url = editTextUrlServidor.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    editTextUrlServidor.setError(getString(R.string.urlRequired));
                    focusView = editTextUrlServidor;
                    cancel = true;
                }

                //Comprobamos que waitingtime no es vacio y que es un numero (entero)
                String waitingTime = editTextWaitingTime.getText().toString();
                if (TextUtils.isEmpty(waitingTime)) {
                    editTextWaitingTime.setError(getString(R.string.waitingTimeRequired));
                    focusView = editTextWaitingTime;
                    cancel = true;
                }else{
                    try{
                        Integer.parseInt(waitingTime);
                    }catch(NumberFormatException e){
                        editTextWaitingTime.setError(getString(R.string.waitingTimeFormat));
                        focusView = editTextWaitingTime;
                        cancel = true;
                    }
                }

                //Comprobamos que miniumtime no es vacio y que es un numero (puede ser doble)
                String miniumTime = editTextMinimumTime.getText().toString();
                if (TextUtils.isEmpty(miniumTime)) {
                    editTextMinimumTime.setError(getString(R.string.minimumTimeRequired));
                    focusView = editTextMinimumTime;
                    cancel = true;
                }else{
                    try{
                        Double.parseDouble(miniumTime);
                    }catch(NumberFormatException e){
                        editTextMinimumTime.setError(getString(R.string.minimumTimeFormat));
                        focusView = editTextMinimumTime;
                        cancel = true;
                    }
                }

                //Comprobamos que miniumdistance no es vacio y que es un numero (puede ser doble)
                String minimumDistance = editTextMinimumDistance.getText().toString();
                if (TextUtils.isEmpty(minimumDistance)) {
                    editTextMinimumDistance.setError(getString(R.string.minimumDistanceRequired));
                    focusView = editTextMinimumDistance;
                    cancel = true;
                }else{
                    try{
                        Double.parseDouble(minimumDistance);
                    }catch(NumberFormatException e){
                        editTextMinimumDistance.setError(getString(R.string.minimumDistanceFormat));
                        focusView = editTextMinimumTime;
                        cancel = true;
                    }
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

        Parameter paramWaitingTime = new Parameter();
        paramWaitingTime.setName(Constants.WAITING_TIME);
        paramWaitingTime.setValue(editTextWaitingTime.getText().toString());

        Parameter paramMinimumTime = new Parameter();
        paramMinimumTime.setName(Constants.MINIMUM_TIME);
        paramMinimumTime.setValue(editTextMinimumTime.getText().toString());

        Parameter paramMinimumDistance = new Parameter();
        paramMinimumDistance.setName(Constants.MINIMUM_DISTANCE);
        paramMinimumDistance.setValue(editTextMinimumDistance.getText().toString());

        List<Parameter> listadoParametros = new ArrayList<Parameter>();
        listadoParametros.add(paramUrl);

        try {
            facadeSettings.updateParametersSettings(listadoParametros);
        } catch (InternalErrorException e) {
            Log.e("SettingsActivity", "Error actualizando la informacion de los parametros");
        }

    }


}
