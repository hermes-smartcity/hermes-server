package hermessensorcollector.lbd.udc.es.hermessensorcollector;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.FacadeSendings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorCollector;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorInterface;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.LocationDTO;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.TailSending;

public class MainActivity2 extends AppCompatActivity implements SensorEventListener {

    static private final Logger LOG = LoggerFactory.getLogger(MainActivity2.class);

    private static final String TAG = "SensorView";

    private FacadeSettings facadeSettings;
    private FacadeSendings facadeSendings;

    // Sensor Manager (para los sensores)
    private SensorManager mgr;
    // Location Manager (para el gps)
    private LocationManager lmgr;

    //Objetos de la pantalla
    private TextView no_sensor_details;

    //Sensor collector
    private TextView textViewTituloSensorCollector;
    private LinearLayout sensor_collector_name_layout;
    private LinearLayout event_delay_layout;
    private LinearLayout event_sensor_stored_layout;
    private LinearLayout packets_sensor_stored_layout;

    // Position collector
    private TextView textViewTituloPositionCollector;
    private LinearLayout position_collector_name_layout;
    private LinearLayout event_position_stored_layout;
    private LinearLayout packets_position_stored_layout;
    private LinearLayout last_position_layout;
    private LinearLayout latitude_layout;
    private LinearLayout longitude_layout;
    private LinearLayout speed_layout;
    private LinearLayout accuracy_layout;


    // Objetos dinamicos de la pantalla
    private TextView event_delay;
    private TextView event_sensor_stored;
    private TextView packets_sensor_stored;

    private TextView position_collector_name;
    private TextView event_position_stored;
    private TextView packets_position_stored;
    private TextView last_position;
    private TextView latitude;
    private TextView longitude;
    private TextView speed;
    private TextView accuracy;

    private ArrayList<TextView> data_value_views;

    private Button buttonDataCollection;

    private int delay = -1;
    private int event_counter = 0;

    //The sensor to use
    private Sensor sensor;
    // The sensor interface to this sensor
    private SensorInterface si;
    //The sensor collector to shis sensor
    private SensorCollector sc;
    //The type of the sensor
    private String typeSensor;

    private static final int REQUEST_SETTING = 1;
    private static final int REQUEST_GPS = 2;
    private static final int REQUEST_SENDING = 3;

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    private Boolean isGPSEnabled = false;
    private Boolean isNetworkEnabled = false;
    private Boolean firstTime = true;

    private String providerChoosen = null; //para si gira la pantalla

    //Tiempo de esperar para hacer las peticiones periodicas para actualizar los datos de la pantalla
    private int UPDATE_INTERVAL = 10000; //10 segundos
    private Handler handlerSensor = new Handler();
    private Handler handlerPosition = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Recuperamos el objeto controller para pasarselo a la fachada
        ApplicationContext aController = (ApplicationContext) getApplicationContext();

        //Instanciamos la facada
        facadeSettings = new FacadeSettings(aController);
        facadeSendings =  new FacadeSendings(aController);

        // Acquire sensor manager
        mgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //Acquire location manager
        lmgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        //Recuperamos los objetos de la pantalla
        no_sensor_details = (TextView) findViewById(R.id.no_sensor_details);

        textViewTituloSensorCollector = (TextView) findViewById(R.id.textViewTituloSensorCollector);
        sensor_collector_name_layout = (LinearLayout) findViewById(R.id.sensor_collector_name_layout);
        event_delay_layout = (LinearLayout) findViewById(R.id.event_delay_layout);
        event_sensor_stored_layout = (LinearLayout) findViewById(R.id.event_sensor_stored_layout);
        packets_sensor_stored_layout = (LinearLayout) findViewById(R.id.packets_sensor_stored_layout);

        textViewTituloPositionCollector = (TextView) findViewById(R.id.textViewTituloPositionCollector);
        position_collector_name_layout = (LinearLayout) findViewById(R.id.position_collector_name_layout);
        event_position_stored_layout = (LinearLayout) findViewById(R.id.event_position_stored_layout);
        packets_position_stored_layout = (LinearLayout) findViewById(R.id.packets_position_stored_layout);
        last_position_layout = (LinearLayout) findViewById(R.id.last_position_layout);
        latitude_layout = (LinearLayout) findViewById(R.id.latitude_layout);
        longitude_layout = (LinearLayout) findViewById(R.id.longitude_layout);
        speed_layout = (LinearLayout) findViewById(R.id.speed_layout);
        accuracy_layout = (LinearLayout) findViewById(R.id.accuracy_layout);

        buttonDataCollection = (Button) findViewById(R.id.buttonDataCollection);
        //Asociamos evento al boton
        buttonDataCollection.setOnClickListener(dataCollectionOnClickListener);

        Sensor typeLinearAcceleration = mgr.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor typeAccelerometer = mgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Informacion pasada el intent (si gira la pantalla)
        if (savedInstanceState!=null) {
            providerChoosen = savedInstanceState.getString(Constants.PROVIDER_CHOOSEN);
        }

        //Recuperamos la lista de sensores de tipo TYPE_LINEAR_ACCELERATION
        if (typeLinearAcceleration != null || typeAccelerometer != null){
            // Hay sensores de ese tipo asi que podremos mostrar los datos de la pantalla
            no_sensor_details.setVisibility(View.INVISIBLE);
            textViewTituloSensorCollector.setVisibility(View.VISIBLE);
            sensor_collector_name_layout.setVisibility(View.VISIBLE);
            event_delay_layout.setVisibility(View.VISIBLE);
            event_sensor_stored_layout.setVisibility(View.VISIBLE);
            packets_sensor_stored_layout.setVisibility(View.VISIBLE);
            textViewTituloPositionCollector.setVisibility(View.VISIBLE);
            position_collector_name_layout.setVisibility(View.VISIBLE);
            event_position_stored_layout.setVisibility(View.VISIBLE);
            packets_position_stored_layout.setVerticalGravity(View.VISIBLE);
            last_position_layout.setVisibility(View.VISIBLE);
            latitude_layout.setVisibility(View.VISIBLE);
            longitude_layout.setVisibility(View.VISIBLE);
            speed_layout.setVisibility(View.VISIBLE);
            accuracy_layout.setVisibility(View.VISIBLE);

            //Recuperamos el email del usuario
            obtainEmail();

            //Determinamos el sensor que usaremos
            getSensor(typeLinearAcceleration, typeAccelerometer);

            //Determinamos el sensor intefaz correspondiente
            getSensorInterface();

            //Determinamos la forma de obtener el gps que usaremos
            //y una vez determinado (dentro), crearemos la instancia
            //de sensorCollection (puesto que necesitamos saber el provider de gps
            //antes de crear la instancia de sensorcollection
            getGpsProvider();

            //Cargamos informacion de sensorCollector
            loadInfoSensorCollector();

            //Cargamos informacion de positionCollector
            loadInfoPositionCollector();

        }
        else {
            // No hay sensores de ese tipo asi que mostramos el mensaje de no hay sensores y ocultamos el resto
            no_sensor_details.setVisibility(View.VISIBLE);
            textViewTituloSensorCollector.setVisibility(View.INVISIBLE);
            sensor_collector_name_layout.setVisibility(View.INVISIBLE);
            event_delay_layout.setVisibility(View.INVISIBLE);
            event_sensor_stored_layout.setVisibility(View.INVISIBLE);
            packets_sensor_stored_layout.setVisibility(View.INVISIBLE);
            textViewTituloPositionCollector.setVisibility(View.INVISIBLE);
            position_collector_name_layout.setVisibility(View.INVISIBLE);
            event_position_stored_layout.setVisibility(View.INVISIBLE);
            packets_position_stored_layout.setVisibility(View.INVISIBLE);
            last_position_layout.setVisibility(View.INVISIBLE);
            latitude_layout.setVisibility(View.INVISIBLE);
            longitude_layout.setVisibility(View.INVISIBLE);
            speed_layout.setVisibility(View.INVISIBLE);
            accuracy_layout.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.action_settings:{
                Intent intent = new Intent(this,SettingsActivity.class);

                startActivityForResult(intent, REQUEST_SETTING);

                break;
            }

            case R.id.action_hanging_sendings:{
                Intent intent = new Intent(this,HangingSendingsActivity.class);

                startActivityForResult(intent, REQUEST_SENDING);

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveEmail(String emailDevice){
        if (emailDevice!=null){
            Utils.guardarNombreUsuario(this, emailDevice);
        }else{
            //Si no hay email, compruebo si en base de datos lo hay
            try {

                //Recuperamos la lista de parametros que nos interesa
                List<String> paramBuscar = new ArrayList<String>();
                paramBuscar.add(Constants.EMAIL);

                List<Parameter> listadoParam = facadeSettings.getListValueParameters(paramBuscar);
                String email = null;

                for (int i=0;i<listadoParam.size();i++){
                    Parameter param = listadoParam.get(i);
                    if (param.getName().equals(Constants.EMAIL)){
                        email = param.getValue();
                        break;
                    }
                }

                if (email != null) {
                    //Como lo hay, lo guardo en sesion
                    Utils.guardarNombreUsuario(this, email);
                }else{
                    //como no lo hay, lanzo el dialogo
                    FragmentManager fm = getSupportFragmentManager();
                    DialogFragment createDialog = DialogEmail.newInstance();
                    createDialog.setCancelable(false);
                    createDialog.show(getSupportFragmentManager(), Constants.TAG_DIALOG_EMAIL);
                }

            } catch (InternalErrorException e) {
                Log.e("MainActivity", "Error recuperando los parametros de la base de datos");
                LOG.error("MainActivity", "Error recuperando los parametros de la base de datos");
            }

        }
    }

    private void obtainEmail(){
        String email = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
                break;
            }
        }

        saveEmail(email);
    }

    private void getSensor(Sensor typeLinearAcceleration, Sensor typeAccelerometer){

        if (typeLinearAcceleration != null){
            sensor = typeLinearAcceleration;
            typeSensor = "TYPE_LINEAR_ACCELERATION";
        }else{
            sensor = typeAccelerometer;
            typeSensor = "TYPE_ACCELEROMETER";
        }
    }

    private void getSensorInterface(){
        si = new SensorInterface(sensor);
    }

    private void showDialogGps(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getString(R.string.noGPS));
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton(getString(R.string.yesPlease), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Lanzamos el intent de configuracion de red
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_GPS);
            }
        });

        alertDialogBuilder.setNegativeButton(getString(R.string.noThanks),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // getting network status
                isNetworkEnabled = lmgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                //Creamos la instancia de sensorCollection
                createSensorCollection();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void getGpsProvider(){

        if (providerChoosen != null){
            //Si ya tiene uno elegido es porque giro la pantalla y no queremos volver a pedirle el dato
            if (providerChoosen.equals(LocationManager.GPS_PROVIDER))  {
                isGPSEnabled = true;
            }else{
                isNetworkEnabled = true;
            }

            //Creamos la instancia de sensorCollection
            createSensorCollection();
        }else{
            // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                // getting GPS status
                isGPSEnabled = lmgr.isProviderEnabled(LocationManager.GPS_PROVIDER);

                //Si esta habilitado no hacemos nada mas
                //Si no esta habilitado y es la primera vez, lanzamos el intent para solicitar permiso
                //Si no esta habilitado y no es la primera vez, comprobamos si tiene networkenabled
                if (isGPSEnabled){
                    //Creamos la instancia de sensorCollection
                    createSensorCollection();
                }else if (!isGPSEnabled && firstTime){
                    showDialogGps();
                }else if (!isGPSEnabled && !firstTime){
                    // getting network status
                    isNetworkEnabled = lmgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    //Creamos la instancia de sensorCollection
                    createSensorCollection();
                }


            }else{
                // One or both permissions are denied.
                // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
                // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COARSE_LOCATION);
                }

                // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
                // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    ActivityCompat.requestPermissions(this,
                            new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                            MY_PERMISSION_ACCESS_FINE_LOCATION);
                }
            }
        }

    }

    private void createSensorCollection(){

        String provider = null;
        if (isGPSEnabled || isNetworkEnabled){
            if (isGPSEnabled){
                provider = LocationManager.GPS_PROVIDER;
            }else{
                provider = LocationManager.NETWORK_PROVIDER;
            }
        }
        sc = new SensorCollector(facadeSettings, facadeSendings, MainActivity2.this, mgr, sensor, si.getNumValues(), typeSensor, lmgr, provider);
    }

    private void loadInfoSensorCollector(){
        // Escribimos la informacion estatica del sensor (nombre)
        loadStaticViewSensor();

        // Escribimos la informacion en tiempo real del sensor
        loadRealTimeViewSensor();

        //actualizar la informacion periodica relativa al sensor
        actualizarDatosPeriodicosSensor();
    }

    // method to load in the static sensor information
    private void loadStaticViewSensor() {

        TextView tv;

        // Nombre del sensor
        tv = (TextView) findViewById(R.id.sensor_collector_name);
        if (tv != null) {
            tv.setText(si.sensor().getName());
        }

        return;
    }

        // load and configure dynamic sensor information
    private void loadRealTimeViewSensor() {

        event_delay = (TextView) findViewById(R.id.event_delay);

        data_value_views = new ArrayList<TextView>();

        // reference the data values layout. This is a linear layout
        // with one or more additional linear layouts for each sensor value.
        LinearLayout ll = (LinearLayout) findViewById(R.id.data_values);

        // For each of the sensor's values, initialize the views
        // required to show each of them.
        for (int i = 0; i < si.getNumValues(); i++) {

            // Get a layout inflater from the system
            LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Get the layout for sensor values
            View v = vi.inflate(R.layout.sensor_values, ll, false);

            TextView tv;

            // Get a reference to the data array text view
            tv = (TextView) v.findViewById(R.id.data_array);
            if (tv != null) {
                // Set the data array text
                String text = String.format(
                        getString(R.string.sensor_data_format), i);
                tv.setText(text);
                // tv.setText("d[" + i + "] = ");
            }
            // Get a reference to the data label text view
            tv = (TextView) v.findViewById(R.id.data_label);
            if (tv != null) {
                // Set the label/units text
                String text = String.format(
                        getString(R.string.sensor_value_format),
                        si.getLabel(i), si.getUnits());
                tv.setText(text);
                // tv.setText(si.getLabel(i) + " (" + si.getUnits() + ") = ");
            }
            // Get a reference to the data value text view
            tv = (TextView) v.findViewById(R.id.data_value);
            if (tv != null) {
                // Set initial value
                tv.setText("No data!");
                // Add this view to the array of text views for data values
                data_value_views.add(tv);
            }
            ll.addView(v); // Add this view the parent layout

        }
        ;

        return;
    }

    private void actualizarDatosPeriodicosSensor(){

        event_sensor_stored = (TextView) findViewById(R.id.event_sensor_stored);
        packets_sensor_stored = (TextView) findViewById(R.id.packets_sensor_stored);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //Recuperamos nº eventos pendientes de meter en el json
                if (sc!=null) {
                    int numeroEnvios = sc.recuperarNumeroSensorToSend();
                    event_sensor_stored.setText(String.valueOf(numeroEnvios));
                }else{
                    event_sensor_stored.setText(getString(R.string.registeringProvider));
                }

                try{
                    List<TailSending> lista = facadeSendings.getListTailSendingFromType(Constants.TYPE_ZIP);
                    int numeroPaquetes = lista.size();
                    packets_sensor_stored.setText(String.valueOf(numeroPaquetes));

                } catch (InternalErrorException e) {
                    Log.e("MainActivity", "InternalErrorException " + e.getMessage());
                    LOG.error("MainActivity: InternalErrorException " + e.getMessage());
                    e.printStackTrace();
                }

                handlerSensor.postDelayed(this, UPDATE_INTERVAL);
            }
        };

        handlerSensor.postDelayed(runnable, 1);

    }

    private void loadInfoPositionCollector(){

        //actualizar la informacion periodica relativa a la posicion
        actualizarDatosPeriodicosPosition();
    }


    private void actualizarDatosPeriodicosPosition(){

        position_collector_name = (TextView) findViewById(R.id.position_collector_name);
        event_position_stored = (TextView) findViewById(R.id.event_position_stored);
        packets_position_stored = (TextView) findViewById(R.id.packets_position_stored);
        last_position = (TextView) findViewById(R.id.last_position);;
        latitude = (TextView) findViewById(R.id.latitude);;
        longitude = (TextView) findViewById(R.id.longitude);;
        speed = (TextView) findViewById(R.id.speed);;
        accuracy = (TextView) findViewById(R.id.accuracy);;

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {

                if (sc!=null) {
                    position_collector_name.setText(sc.recuperarProvider());

                    //Recuperamos nº eventos pendientes de meter en el json
                    int numeroEnvios = sc.recuperarNumeroPositionToSend();
                    event_position_stored.setText(String.valueOf(numeroEnvios));

                    LocationDTO lastLocation = sc.recuperarLastLocation();
                    if (lastLocation != null){
                        DecimalFormat sixDForm = new DecimalFormat("#.######");
                        DecimalFormat twoDForm = new DecimalFormat("#.##");

                        // Create a calendar object that will convert the date and time value in milliseconds to date.
                        SimpleDateFormat formatter = new SimpleDateFormat(Utils.FECHA_SQLITE);
                        String dateString = formatter.format(new Date(lastLocation.getTime()));
                        last_position.setText(dateString);

                        if (lastLocation.getLatitude() != null) {
                            latitude.setText(sixDForm.format(lastLocation.getLatitude()));
                        }

                        if (lastLocation.getLongitude() != null) {
                            longitude.setText(sixDForm.format(lastLocation.getLongitude()));
                        }

                        if (lastLocation.getSpeed() != null) {
                            speed.setText(twoDForm.format(lastLocation.getSpeed()));
                        }

                        if (lastLocation.getAccuracy() != null) {
                            accuracy.setText(twoDForm.format(lastLocation.getAccuracy()));
                        }
                    }else{
                        last_position.setText(getString(R.string.unknown));
                        latitude.setText(getString(R.string.unknown));
                        longitude.setText(getString(R.string.unknown));
                        speed.setText(getString(R.string.unknown));
                        accuracy.setText(getString(R.string.unknown));
                    }

                }else{
                    position_collector_name.setText(getString(R.string.registeringProvider));
                    event_position_stored.setText(getString(R.string.registeringProvider));

                    last_position.setText(getString(R.string.registeringProvider));
                    latitude.setText(getString(R.string.registeringProvider));
                    longitude.setText(getString(R.string.registeringProvider));
                    speed.setText(getString(R.string.registeringProvider));
                    accuracy.setText(getString(R.string.registeringProvider));
                }





                try{
                    List<TailSending> lista = facadeSendings.getListTailSendingFromType(Constants.TYPE_GPS);
                    int numeroPaquetes = lista.size();
                    packets_position_stored.setText(String.valueOf(numeroPaquetes));

                } catch (InternalErrorException e) {
                    Log.e("MainActivity", "InternalErrorException " + e.getMessage());
                    LOG.error("MainActivity: InternalErrorException " + e.getMessage());
                    e.printStackTrace();
                }

                handlerSensor.postDelayed(this, UPDATE_INTERVAL);
            }
        };

        handlerSensor.postDelayed(runnable, 1);

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sensor changed event fired, verify it is the type we are
        // handling currently.
        if (event.sensor.getType() == this.si.sensor().getType()) {

            DecimalFormat twoDForm = new DecimalFormat("#.##");

            // Increment event counter
            event_counter++;

            for (int i = 0; (i < event.values.length)
                    && (i < si.getNumValues()); i++) {

                // Update data values text view
                TextView tv = data_value_views.get(i);
                tv.setText(String.valueOf(event.values[i]));

                // Write some info to the log about this sensor
                String text = String.format(
                        getString(R.string.sensor_data_format), i);
                text += String.format(getString(R.string.sensor_value_format),
                        si.getLabel(i), si.getUnits());
                text += twoDForm.format(event.values[i]) + "\n";

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Called at the start of the visible lifetime.
    @Override
    public void onStart() {
        super.onStart();
        // Apply any required UI change now that the Activity is visible.
        Log.d(TAG, "onStart\n");
        LOG.debug(TAG + " onStart\n");
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume any paused UI updates, threads, or processes required
        // by the activity but suspended when it was inactive.
        Log.d(TAG, "onResume\n");
        LOG.debug(TAG + "onResume\n");

        // Register sensor listener at normal rate until changed
        registerSensorListener(SensorManager.SENSOR_DELAY_NORMAL, false);
    }

    // Called to save UI state changes at the
    // end of the active life cycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
        Log.d(TAG, "onSaveInstanceState\n");
        LOG.debug(TAG + "onSaveInstanceState\n");

        //Guardamos el tipo de provider a usar (por si giran la pantalla para
        //no volver a pedirlo
        if (isGPSEnabled) {
            savedInstanceState.putString(Constants.PROVIDER_CHOOSEN, LocationManager.GPS_PROVIDER);
        }else{
            savedInstanceState.putString(Constants.PROVIDER_CHOOSEN, LocationManager.NETWORK_PROVIDER);
        }
    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Log.d(TAG, "onRestoreInstanceState\n");
        LOG.debug(TAG + "onRestoreInstanceState\n");

        providerChoosen = savedInstanceState.getString(Constants.PROVIDER_CHOOSEN);
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart() {
        super.onRestart();
        // Load changes knowing that the activity has already
        // been visible within this process.
        Log.d(TAG, "onRestart\n");
        LOG.debug(TAG + "onRestart\n");
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause() {
        // Suspend UI updates, threads, or CPU intensive processes
        // that don’t need to be updated when the Activity isn’t
        // the active foreground activity.
        super.onPause();
        Log.d(TAG, "onPause\n");
        LOG.debug(TAG + "onPause\n");

        delay = -1;

        // Unregister ourself from sensor stream
        mgr.unregisterListener(this);
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop() {
        // Suspend remaining UI updates, threads, or processing
        // that aren’t required when the Activity isn’t visible.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        super.onStop();
        Log.d(TAG, "onStop\n");
        LOG.debug(TAG + "onStop\n");
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        //Cuando se para la aplicacion, tenemos que asegurarnos de
        //enviar el ultimo paquete
        if (sc!=null) {
            sc.stopTask();
        }

        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
        Log.d(TAG, "onDestroy\n");
        LOG.debug(TAG + "onDestroy\n");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case REQUEST_SETTING:{
                delay = -1;
                break;
            }

            case REQUEST_GPS:{
                firstTime = false;
                getGpsProvider();
            }

            case REQUEST_SENDING:{
                delay = -1;
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    isNetworkEnabled = true;
                } else {
                    // permission denied
                    isNetworkEnabled = false;
                }

                //volvemos a llamar a la pantalla para recuperar el provide
                getGpsProvider();

                break;
            }

            case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    isGPSEnabled = true;
                } else {
                    // permission denied
                    isGPSEnabled = false;
                }

                //volvemos a llamar a la pantalla para recuperar el provide
                getGpsProvider();

                break;
            }

        }
    }


    // Helper function to register and unregister a sensor listener
    private void registerSensorListener(int d, boolean notify) {

        // Check if we are changing the delay
        if (d != delay) {
            // Unregister anything which was previously registered
            mgr.unregisterListener(MainActivity2.this);

            // Register as a listener at set rate
            boolean result = mgr.registerListener(this, si.sensor(), d);
            if (result == true) {
                event_delay.setText(SensorInterface.delayToString(d));
                if (notify == true) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Changed event rate to "
                                    + SensorInterface.delayToString(d),
                            Toast.LENGTH_SHORT).show();
                }
                delay = d;
            }
        }

        return;
    }

    private Button.OnClickListener dataCollectionOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //Datos casos: si el texto del boton pone start o pone stop
            //a) Si el texto que pone en el boton es Start:
            // Cambiar el texto del boton por Stop
            // Registar el sensorcollector para que empieze a tomar datos.
            // Lanzar la tarea periodica que envia la informacion al servidor

            //b) Si el texto que pone en el boton es Stop
            // Cambiar el texto por Start
            // Deregistar el sensorcollector
            // Para la tarea periodica
            // Enviar el ultimo bloque de eventos capturados

            if (buttonDataCollection.getText().toString().equals(getString(R.string.start))){
                //Caso a
                buttonDataCollection.setText(R.string.stop);
                sc.registerSensorCollector();
                sc.launchTask();
            }else{
                //Caso b
                buttonDataCollection.setText(R.string.start);
                sc.unregisterSensorCollector();
                sc.stopTask();
            }
        }
    };
}
