package hermessensorcollector.lbd.udc.es.hermessensorcollector;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.applicationcontext.ApplicationContext;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorCollector;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor.SensorInterface;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SensorEventListener {

    private static final String TAG = "SensorView";

    private FacadeSettings facadeSettings;

    // Sensor Manager
    private SensorManager mgr;

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

        // Acquire sensor manager
        mgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //Recuperamos el email del dispositivo
        getLoaderManager().initLoader(0, null, this);

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

            //Determinamos el sensor que usaremos
            getSensor(typeLinearAcceleration, typeAccelerometer);

            //Determinamos el sensor intefaz correspondiente
            getSensorInterface();

            //Creamos la instancia de sensorCollection
            createSensorCollection();

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,SettingsActivity.class);

            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(
                        ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY),
                ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?",
                new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<String> emails = new ArrayList<String>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            emails.add(data.getString(ProfileQuery.ADDRESS));
            // Potentially filter on ProfileQuery.IS_PRIMARY
            data.moveToNext();
        }

        if (emails.size()>0){
            Utils.guardarNombreUsuario(this, emails.get(0));
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
                    }
                }

                if (email != null) {
                    //Como lo hay, lo guardo en sesion
                    Utils.guardarNombreUsuario(this, email);
                }else{
                    //como no lo hay, lanzo el dialogo
                    FragmentManager fm = getSupportFragmentManager();
                    DialogFragment createDialog = DialogEmail.newInstance();
                    createDialog.show(getSupportFragmentManager(), Constants.TAG_DIALOG_EMAIL);
                }


            } catch (InternalErrorException e) {
                Log.e("MainActivity", "Error recuperando los parametros de la base de datos");
            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void getSensor(Sensor typeLinearAcceleration, Sensor typeAccelerometer){

        if (typeLinearAcceleration != null){
            sensor = typeLinearAcceleration;
        }else{
            sensor = typeAccelerometer;
        }
    }

    private void getSensorInterface(){
        si = new SensorInterface(sensor);
    }

    private void createSensorCollection(){
        sc = new SensorCollector(MainActivity.this, mgr, sensor, si.getNumValues());
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
                Log.d(TAG, text);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        // Resume any paused UI updates, threads, or processes required
        // by the activity but suspended when it was inactive.
        Log.d(TAG, "onResume\n");

        // Register sensor listener at normal rate until changed
        registerSensorListener(SensorManager.SENSOR_DELAY_NORMAL, false);
    }

    // Called after onCreate has finished, use to restore UI state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        Log.d(TAG, "onRestoreInstanceState\n");
    }

    // Called before subsequent visible lifetimes
    // for an activity process.
    @Override
    public void onRestart() {
        super.onRestart();
        // Load changes knowing that the activity has already
        // been visible within this process.
        Log.d(TAG, "onRestart\n");
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
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause() {
        // Suspend UI updates, threads, or CPU intensive processes
        // that don’t need to be updated when the Activity isn’t
        // the active foreground activity.
        super.onPause();
        Log.d(TAG, "onPause\n");

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
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy() {
        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
        Log.d(TAG, "onDestroy\n");
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
