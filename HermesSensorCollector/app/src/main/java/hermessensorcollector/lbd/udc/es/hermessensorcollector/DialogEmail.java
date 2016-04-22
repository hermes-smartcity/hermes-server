package hermessensorcollector.lbd.udc.es.hermessensorcollector;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.bd.SQLiteHelper;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;


/**
 * A simple {@link Fragment} subclass.
 */
public class DialogEmail extends DialogFragment {

    static private final Logger LOG = LoggerFactory.getLogger(DialogEmail.class);

    private InputMethodManager imm;

    private FacadeSettings facadeSettings;

    //Objetos de la pantalla
    private EditText editTextEmail;
    private Button buttonAceptEmail;

    public DialogEmail() {
        // Required empty public constructor
    }

    public static DialogEmail newInstance() {
        return new DialogEmail();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.dialog_email, container, false);

        //para evitar que cierren la ventana sin  haber pulsado en ninguno de los botones
        getDialog().setCanceledOnTouchOutside(false);

        //para evitar que se abra el teclado
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //para que el teclado este oculto al abrirse el dialogo
        imm = (InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        //titulo al dialogo
        getDialog().setTitle(getString(R.string.writeEmail));

        //Recuperamos el objeto controller para pasarselo a la fachada
        ApplicationContext aController = (ApplicationContext) getActivity().getApplicationContext();

        //Instanciamos la facada
        facadeSettings = new FacadeSettings(aController);

        //Recuperamos los objeto de la vista
        editTextEmail = (EditText) v.findViewById(R.id.editTextEmail);
        buttonAceptEmail = (Button) v.findViewById(R.id.buttonAceptEmail);

        //Asociamos eventos a los botones
        buttonAceptEmail.setOnClickListener(customDialog_AceptarOnClickListener);

        return v;
    }

    private Button.OnClickListener customDialog_AceptarOnClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            boolean cancel = false;
            View focusView = null;

            editTextEmail.setError(null);

            String email = editTextEmail.getText().toString();

            // Compramos que la url no es vacia
            if (TextUtils.isEmpty(email)) {
                editTextEmail.setError(getString(R.string.emailRequired));
                focusView = editTextEmail;
                cancel = true;
            }else{
                //Validamos el email
                Boolean emailOk = android.util.Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText()).matches();

                if (!emailOk) {
                    editTextEmail.setError(getString(R.string.emailIncorrect));
                    focusView = editTextEmail;
                    cancel = true;
                }
            }

            if (cancel) {
                // Ha habido fallos; no continuamos con la configuracion y mostramos
                // en el formulario el primer campo que tenga un error
                focusView.requestFocus();
            } else {

                //Insertamos el email en BD
                insertEmail();

                //Lo guardamos en shared preferences
                Utils.guardarNombreUsuario(getActivity(), editTextEmail.getText().toString());

                //Cerramos el dialogo
                dismiss();
            }

        }

    };

    private void insertEmail(){

        Parameter paramUrl = new Parameter();
        paramUrl.setName(Constants.EMAIL);
        paramUrl.setValue(editTextEmail.getText().toString());

        List<Parameter> listadoParametros = new ArrayList<Parameter>();
        listadoParametros.add(paramUrl);

        try {
            facadeSettings.updateParametersSettings(listadoParametros);
        } catch (InternalErrorException e) {
            Log.e("DialogEmail", "Error insertando email");
            LOG.error("DialogEmail: Error insertando email");
        }

    }

}
