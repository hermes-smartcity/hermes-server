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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.sending.FacadeSendings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.setting.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorCollector;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorInterface;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static private final Logger LOG = LoggerFactory.getLogger(MainActivity.class);

    private static final String TAG = "SensorView";

    private FacadeSettings facadeSettings;
    private FacadeSendings facadeSendings;

    // Sensor Manager (para los sensores)
    private SensorManager mgr;
    // Location Manager (para el gps)
    private LocationManager lmgr;

    //Objetos de la pantalla
    private TextView no_sensor_details;
    private ImageView sensor_icon;
    private LinearLayout sensor_name_layout;
    private LinearLayout sensor_vendor_layout;
    private LinearLayout sensor_version_layout;
    private LinearLayout sensor_type_layout;
    private LinearLayout sensor_power_layout;
    private LinearLayout sensor_range_layout;
    private LinearLayout sensor_resolution_layout;
    private LinearLayout layout_real_time_data;

    // Dynamic (real-time views)
    private TextView delay_view;
    private TextView event_count_view;
    private TextView timestamp_view;
    private TextView accuracy_view;

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
    String typeSensor;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        sensor_icon = (ImageView) findViewById(R.id.sensor_icon);
        sensor_name_layout = (LinearLayout) findViewById(R.id.sensor_name_layout);
        sensor_vendor_layout = (LinearLayout) findViewById(R.id.sensor_vendor_layout);
        sensor_version_layout = (LinearLayout) findViewById(R.id.sensor_version_layout);
        sensor_type_layout = (LinearLayout) findViewById(R.id.sensor_type_layout);
        sensor_power_layout = (LinearLayout) findViewById(R.id.sensor_power_layout);
        sensor_range_layout = (LinearLayout) findViewById(R.id.sensor_range_layout);
        sensor_resolution_layout = (LinearLayout) findViewById(R.id.sensor_resolution_layout);
        layout_real_time_data = (LinearLayout) findViewById(R.id.layout_real_time_data);

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
            sensor_icon.setVisibility(View.VISIBLE);
            sensor_name_layout.setVisibility(View.VISIBLE);
            sensor_vendor_layout.setVisibility(View.VISIBLE);
            sensor_version_layout.setVisibility(View.VISIBLE);
            sensor_type_layout.setVisibility(View.VISIBLE);
            sensor_power_layout.setVisibility(View.VISIBLE);
            sensor_range_layout.setVisibility(View.VISIBLE);
            sensor_resolution_layout.setVisibility(View.VISIBLE);
            layout_real_time_data.setVisibility(View.VISIBLE);

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

            // Initialize basic (static) sensor view
            loadStaticView();

            // Initialize real-time sensor view
            loadRealTimeView();

        }
        else {
            // No hay sensores de ese tipo asi que mostramos el mensaje de no hay sensores y ocultamos el resto
            no_sensor_details.setVisibility(View.VISIBLE);
            sensor_icon.setVisibility(View.INVISIBLE);
            sensor_name_layout.setVisibility(View.INVISIBLE);
            sensor_vendor_layout.setVisibility(View.INVISIBLE);
            sensor_version_layout.setVisibility(View.INVISIBLE);
            sensor_type_layout.setVisibility(View.INVISIBLE);
            sensor_power_layout.setVisibility(View.INVISIBLE);
            sensor_range_layout.setVisibility(View.INVISIBLE);
            sensor_resolution_layout.setVisibility(View.INVISIBLE);
            layout_real_time_data.setVisibility(View.INVISIBLE);
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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

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
        sc = new SensorCollector(facadeSettings, facadeSendings, MainActivity.this, mgr, sensor, si.getNumValues(), typeSensor, lmgr, provider);
    }

    // method to load in the static sensor information
    private void loadStaticView() {

        ImageView iv;
        TextView tv;

        // Load sensor icon
        iv = (ImageView) findViewById(R.id.sensor_icon);
        if (iv != null) {
            iv.setImageResource(si.getIcon());
        }
        // Load sensor name
        tv = (TextView) findViewById(R.id.sensor_name);
        if (tv != null) {
            tv.setText(si.sensor().getName());
        }
        // Load sensor vendor
        tv = (TextView) findViewById(R.id.sensor_vendor);
        if (tv != null) {
            tv.setText(si.sensor().getVendor());
        }
        // Load sensor version
        tv = (TextView) findViewById(R.id.sensor_version);
        if (tv != null) {
            tv.setText(((Integer) si.sensor().getVersion()).toString());
        }
        // Load sensor type
        tv = (TextView) findViewById(R.id.sensor_type);
        if (tv != null) {
            tv.setText(si.getType() + " ("
                    + ((Integer) si.sensor().getType()).toString() + ")");
        }
        // Load sensor power
        tv = (TextView) findViewById(R.id.sensor_power);
        if (tv != null) {
            tv.setText(((Float) si.sensor().getPower()).toString());
        }
        // Load sensor range
        tv = (TextView) findViewById(R.id.sensor_range);
        if (tv != null) {
            tv.setText(((Float) si.sensor().getMaximumRange()).toString());
        }
        // Load sensor range units
        tv = (TextView) findViewById(R.id.sensor_range_units);
        if (tv != null) {
            tv.setText(si.getUnits());
        }
        // Load sensor resolution
        tv = (TextView) findViewById(R.id.sensor_resolution);
        if (tv != null) {
            tv.setText(((Float) si.sensor().getResolution()).toString());
        }
        // Load sensor range units
        tv = (TextView) findViewById(R.id.sensor_resolution_units);
        if (tv != null) {
            tv.setText(si.getUnits());
        }

        return;
    }

    // load and configure dynamic sensor information
    private void loadRealTimeView() {

        delay_view = (TextView) findViewById(R.id.sensor_delay);
        event_count_view = (TextView) findViewById(R.id.sensor_events);
        timestamp_view = (TextView) findViewById(R.id.sensor_timestamp);
        accuracy_view = (TextView) findViewById(R.id.sensor_accuracy);

        accuracy_view.setText("Unknown");

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Sensor changed event fired, verify it is the type we are
        // handling currently.
        if (event.sensor.getType() == this.si.sensor().getType()) {

            // Increment event counter
            event_counter++;

            for (int i = 0; (i < event.values.length)
                    && (i < si.getNumValues()); i++) {

                // Update event count text view
                event_count_view.setText(((Integer) event_counter).toString());

                // Convert time stamp to seconds and update timestamp view
                Float ts = (float) event.timestamp / 1000000000;
                timestamp_view.setText(ts.toString());

                // Update accuracy text view
                accuracy_view.setText(SensorInterface
                        .accuracyToString((event.accuracy)));

                // Update data values text view
                TextView tv = data_value_views.get(i);
                tv.setText(String.valueOf(event.values[i]));

                // Write some info to the log about this sensor
                String text = String.format(
                        getString(R.string.sensor_data_format), i);
                text += String.format(getString(R.string.sensor_value_format),
                        si.getLabel(i), si.getUnits());
                text += String.valueOf(event.values[i]) + "\n";

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
            mgr.unregisterListener(MainActivity.this);

            // Register as a listener at set rate
            boolean result = mgr.registerListener(this, si.sensor(), d);
            if (result == true) {
                delay_view.setText(SensorInterface.delayToString(d));
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
