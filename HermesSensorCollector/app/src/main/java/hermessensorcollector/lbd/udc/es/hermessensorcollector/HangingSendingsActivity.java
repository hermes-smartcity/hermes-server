package hermessensorcollector.lbd.udc.es.hermessensorcollector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.adapter.SendingAdapter;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.FacadeSendings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;


public class HangingSendingsActivity extends AppCompatActivity {

    static private final Logger LOG = LoggerFactory.getLogger(HangingSendingsActivity.class);

    private FacadeSendings facadeSendings;

    private TextView textViewNoSending;
    private ListView listViewTailSending;
    private Button buttonDeleteAll;
    private Button cancelDeleteAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hanging_sendings);

        //Recuperamos el objeto controller para pasarselo a la fachada
        ApplicationContext aController = (ApplicationContext) getApplicationContext();

        //Instanciamos la facada
        facadeSendings = new FacadeSendings(aController);

        //Recuperamos los objetos de la pantalla
        textViewNoSending = (TextView) findViewById(R.id.textViewNoSending);
        listViewTailSending = (ListView) findViewById(R.id.listViewTailSending);
        buttonDeleteAll = (Button) findViewById(R.id.buttonDeleteAll);
        cancelDeleteAll = (Button) findViewById(R.id.cancelDeleteAll);

        //Creamos la lista de envios pendientes
        crearListaEnviosPendientes();

        //Asociamos el evento al pulsar cancelar
        cancelDeleteAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Cerramos la activity
                finish();
            }
        });

        //Asociamos el evento al pulsar eleminar
        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               borrarTodosEnvios();
            }
        });
    }

    private void crearListaEnviosPendientes(){
        try {

            //Recuperamos la lista
            List<TailSending> lista = facadeSendings.getListTailSending();

            //Comprobamos si hay datos para ver si mostramos el texto o la lista
            if (lista.size()>0){
                textViewNoSending.setVisibility(View.GONE);
                listViewTailSending.setVisibility(View.VISIBLE);
                buttonDeleteAll.setVisibility(View.VISIBLE);

                //Asignamos el adapter correspondiente
                final SendingAdapter sendingAdapter = new SendingAdapter(lista);
                listViewTailSending.setAdapter(sendingAdapter);

            }else{
                textViewNoSending.setVisibility(View.VISIBLE);
                listViewTailSending.setVisibility(View.GONE);
                buttonDeleteAll.setVisibility(View.GONE);
            }

        } catch (InternalErrorException e) {
            Log.e("HangingSendingsActivity", "Error recuperando la lista de envios pendientes", e);
            LOG.error("HangingSendingsActivity: Error recuperando la lista de envios pendientes", e);
        }
    }

    private void borrarTodosEnvios(){

        try {

            //Borramos los datos de envios pendientes
            facadeSendings.deleteAllSendings();

            //Mostramos mensaje toast diciendo que se ha actualizado la informacion
            Utils.crearMensajeToast(HangingSendingsActivity.this, getString(R.string.deleteOK));

            //Refrescamos la lista
            crearListaEnviosPendientes();

        } catch (InternalErrorException e) {
            Log.e("HangingSendingsActivity", "Error borrando la lista de envios pendientes", e);
            LOG.error("HangingSendingsActivity: Error borrando la lista de envios pendientes", e);
        }

    }
}
