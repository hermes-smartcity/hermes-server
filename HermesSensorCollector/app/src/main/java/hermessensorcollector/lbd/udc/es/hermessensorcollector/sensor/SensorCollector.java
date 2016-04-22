package hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ZipErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.ConstantsJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.GpsJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.JSONParser;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.SensorJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Compress;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.DirectoryPaths;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.LocationDTO;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;

/**
 * Created by Leticia on 15/04/2016.
 */
public class SensorCollector implements SensorEventListener, LocationListener {

    private float[] previousValues;

    private Queue<SensorDTO> valuesSensorToSend = new ArrayDeque<SensorDTO>();
    private Queue<LocationDTO> valuesLocationToSend = new ArrayDeque<LocationDTO>();

    private FacadeSettings facadeSettings;
    private Activity activity;
    private SensorManager mgr;
    private Sensor sensor;
    private int numValues;
    private String typeSensor;
    private LocationManager lmgr;
    private String provider;

    //URL del servidor
    private String SERVICE_URL = null;

    //Tiempo de esperar entre cada llamada al servidor
    //Le ponemos un tiempo por defecto por si no esta en el settings (que deberia)
    private int UPDATE_INTERVAL = 60000*5; //5 Minutos

    //The minimum distance to change updates in meters
    //Le ponemos un tiempo por defecto por si no esta en el settings (que deberia)
    private long MIN_DISTANCE_CHANGE_FOR_UPDATES = 5; // 5 meters

    //The minimum time beetwen updates in milliseconds
    //Le ponemos un tiempo por defecto por si no esta en el settings (que deberia)
    private long MIN_TIME_BW_UPDATES = 60000 * 1;  // 1 minute

    private Timer timer = null;

    private PowerManager.WakeLock wakeLock;
    private int responseCode;

    //variable para saber cuando es el primer envio y el ultimo
    private Boolean firstSend = false;
    private Boolean lastSend = false;

    public SensorCollector(FacadeSettings facadeSetting, Activity activity, SensorManager mgr, Sensor sensor,
                           int numValues, String typeSensor, LocationManager lmgr, String provider) {
        this.facadeSettings = facadeSetting;
        this.activity = activity;
        this.mgr = mgr;
        this.sensor = sensor;
        this.numValues = numValues;
        this.typeSensor = typeSensor;
        this.lmgr = lmgr;
        this.provider = provider;

        //Inicializamos previousValues segun el numero de elementos del sensor
        previousValues = new float[numValues];
        for (int i = 0; i < numValues; i++) {
            previousValues[i] = Float.MAX_VALUE;
        }

        //Al arrancar, obtenemos los parametros de servidor, tiempo de espera, distancia minima y tiempo minimo
        recuperarParametros();
    }

    private void recuperarParametros(){

        try {
            //Recuperamos la lista de parametros que nos interesa
            List<String> paramBuscar = new ArrayList<String>();
            paramBuscar.add(Constants.SERVICE_URL);
            paramBuscar.add(Constants.WAITING_TIME);
            paramBuscar.add(Constants.MINIMUM_DISTANCE);
            paramBuscar.add(Constants.MINIMUM_TIME);

            List<Parameter> listadoParam = facadeSettings.getListValueParameters(paramBuscar);

            for (int i=0;i<listadoParam.size();i++){
                Parameter param = listadoParam.get(i);
                if (param.getName().equals(Constants.SERVICE_URL)){
                    SERVICE_URL = param.getValue();
                }

                if (param.getName().equals(Constants.WAITING_TIME)){
                    //El tiempo viene en minutos asi que lo multiplicamos por 60000 para pasarlo a milisegundos
                    UPDATE_INTERVAL = 60000 * Integer.parseInt(param.getValue());
                }

                if (param.getName().equals(Constants.MINIMUM_DISTANCE)){
                    MIN_DISTANCE_CHANGE_FOR_UPDATES = Long.parseLong(param.getValue());
                }

                if (param.getName().equals(Constants.MINIMUM_TIME)){
                    //El tiempo viene en minutos asi que lo multiplicamos por 60000 para pasarlo a milisegundos
                    MIN_TIME_BW_UPDATES = 60000 * Long.parseLong(param.getValue());
                }

            }


        } catch (InternalErrorException e) {
            Log.e("MainActivity", "Error recuperando los parametros de la base de datos");
        }

    }

    public void registerSensorCollector() {
        //Registramos el listener de sensores
        mgr.registerListener(SensorCollector.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        //Registrar listener de gps
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            lmgr.requestLocationUpdates(provider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

        }


    }

    public void unregisterSensorCollector() {
        //Deregistrar el listener de sensores
        mgr.unregisterListener(SensorCollector.this);

        //Deregistrar el listener de gps
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lmgr.removeUpdates(this);
        }
    }


    private void asignarValoresEnviar(SensorEvent event){
        //long tiempo = event.timestamp;
        long tiempo = System.currentTimeMillis();
        float[] valores = event.values.clone();

        SensorDTO sensorDTO = new SensorDTO(tiempo, valores);
        valuesSensorToSend.add(sensorDTO);
    }

    private void asignarValoresLocationEnviar(Location location){

        if (location != null){
            long time = location.getTime();

            Double longitude = location.getLongitude();
            Double latitude = location.getLatitude();

            Double altitude = null;
            if (location.hasAltitude()){
                altitude = location.getAltitude();
            }

            Double speed =  null;
            if (location.hasSpeed()){
                speed = Double.parseDouble(Float.toString(location.getSpeed()));
            }

            Double bearing = null;
            if (location.hasBearing()){
                bearing = Double.parseDouble(Float.toString(location.getBearing()));
            }

            Double accuracy = null;
            if (location.hasAccuracy()){
                accuracy = Double.parseDouble(Float.toString(location.getAccuracy()));
            }

            LocationDTO locationDto = new LocationDTO(time, longitude, latitude, altitude, speed, bearing, accuracy);
            valuesLocationToSend.add(locationDto);
        }

    }

    private Timer getTimer(){
        if (timer == null){
            timer = new Timer();
        }else{
            return timer;
        }

        return timer;
    }

    public void launchTask(){
        //indicamos que es el primer envio
        firstSend = true;
        lastSend = false;

        timer = getTimer();

        timer.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run() {
                SendInformationSensorTask tareaSensor = new SendInformationSensorTask();
                tareaSensor.execute();

                SendInformationGpsTask tareaGps = new SendInformationGpsTask();
                tareaGps.execute();
            }
        },0, UPDATE_INTERVAL);

    }

    public void stopTask(){
        if (timer!=null) {
            timer.cancel();
        }
        timer = null;

        //indicamos que es el ultimo envio
        firstSend = false;
        lastSend = true;

        //Lanzamos las tarea por si quedaban cosas sin enviar
        SendInformationSensorTask tarea = new SendInformationSensorTask();
        tarea.execute();

        SendInformationGpsTask tareaGps = new SendInformationGpsTask();
        tareaGps.execute();
    }

    /**
     * Metodo para inicializar el contenido de la carpeta json, es decir,
     * para borrar todos los archivos que hay dentro de ella
     */
    private void inicializarDirectorioJSON(File rutaJson){
        File rutaJSON = rutaJson;
        String [] ficheros = rutaJSON.list();
        if (ficheros!=null){
            for(int i=0;i<ficheros.length;i++){
                String ruta = ficheros[i]; //eliminamos los ficheros del directorio
                File file = new File(rutaJSON + File.separator + ruta);
                file.delete();
            }
        }
    }

    /**
     * Metodo para inicializar el contenido de la carpeta zip, es decir,
     * para borrar todos los archivos que hay dentro de ella
     */
    private void inicializarDirectorioZip(File rutaZip){
        File rutaZIP = rutaZip;
        String [] ficheros = rutaZIP.list();

        if (ficheros!=null){
            for(int i=0;i<ficheros.length;i++){
                String ruta = ficheros[i]; //eliminamos los ficheros del directorio
                File file = new File(rutaZIP + File.separator + ruta);
                file.delete();
            }
        }
    }

    /**
     * Metodo para borrar el archivo de la carpeta indicada
     * @param directory Ruta donde estan los json
     * @param nameFile Nombre del archivo a eliminar
     */
    private void deleteFile(File directory, String nameFile){
        File file = new File(directory + File.separator + nameFile);
        file.delete();
    }

    //Metodos debidos a sensorlistener
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == sensor.getType()) {

            for (int i = 0; (i < event.values.length); i++) {
                //Recuperamos el valor anterior
                float previousValue = previousValues[i];
                float actualValue = event.values[i];
                if (Math.abs( actualValue - previousValue) > sensor.getResolution()){
                    //Si alguno de ellos lo cumple, entonces:
                    //a - asignamos los valores a enviar
                    //b - actualizamos los valores anterior para todos
                    //c - salimos del bucle porque no queremos que se ejecute para los
                    //siguientes puesto que ya se ha hecho en los pasos a y b
                    asignarValoresEnviar(event);
                    previousValues = event.values.clone();
                    break;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //Metodos debidos a locationlistener
    @Override
    public void onLocationChanged(Location location) {
        asignarValoresLocationEnviar(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    //Tarea asincrona para enviar informacion de los sensores al servidor
    private class SendInformationSensorTask extends AsyncTask<Void, Void, Boolean> {

        DirectoryPaths rutasDirectorios = new DirectoryPaths(activity);
        File rutaZip = rutasDirectorios.getZipDir();
        File rutaJson = rutasDirectorios.getJSONDir();
        String jsonSensorName = rutasDirectorios.getJSONSensorName();
        String zipSensorName = rutasDirectorios.getZipSensorName();
        String terminacionJson = ".json";
        String rutaDirectorioZip;

        String nombre_fichero_json;
        String nombre_zip;


        @Override
        protected Boolean doInBackground(Void... params) {

            JSONParser jsonParser = new JSONParser();

            //Recuperamos la lista de elementos a enviar
            int numElementos = valuesSensorToSend.size();

            //Si no hay elementos, no haremos nada  y tampoco cambiaremos
            //el valor del primer envio
            if (numElementos == 0){
                return false;
            }else{
                try {

                    List<SensorDTO> elementsToSend = new ArrayList<SensorDTO>();
                    for (int i = 0; i < numElementos; i++) {
                        SensorDTO e = valuesSensorToSend.remove();
                        elementsToSend.add(e);
                    }

                    //Recuperamos del shared preferences el email
                    String nombreUsuario = Utils.recuperarNombreUsuario(activity);

                    //Creamos el json a enviar
                    SensorJSON sensorJsonToSend = new SensorJSON(nombreUsuario, typeSensor, firstSend, lastSend, elementsToSend);

                    //Creamos un numero aleatorio para concatenar al nombre
                    Random generator = new Random();
                    int n1 = Math.abs(generator.nextInt() % 6);

                    //Creamos un fichero json con el contenido de dicho json
                    File directorio_json = rutaJson;
                    nombre_fichero_json = jsonSensorName + "_" + String.valueOf(n1);
                    String fileName = directorio_json.getAbsolutePath() + File.separator +
                            nombre_fichero_json + terminacionJson;
                    String[] files = new String[1];
                    files[0] = fileName;

                    jsonParser.crearFileJSON(sensorJsonToSend, fileName);

                    //Creamos el zip en la ruta indicada
                    File directorio_zip = rutaZip;
                    nombre_zip = zipSensorName + "_" + String.valueOf(n1);
                    rutaDirectorioZip = directorio_zip + File.separator + nombre_zip;
                    Compress c = new Compress(files, rutaDirectorioZip);
                    c.zip();

                } catch(InternalErrorException e) {
                    Log.e("SensorTask", "Problemas interno " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (ZipErrorException e) {
                    Log.e("SensorTask", "Problemas creanzo zip " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    Log.e("SensorTask", "Problemas accediendo al fichero " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }

                //Enviamos el zip
                try {
                    HttpURLConnection conn = null;
                    DataOutputStream dOut = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBuffersize = 1*1024*1024;
                    File file = new File(rutaDirectorioZip);

                    FileInputStream fileIn = new FileInputStream(file);
                    //String serviceUrl = recuperarURLServicio();
                    String serviceUrl = SERVICE_URL;
                    URL url =  new URL(serviceUrl + ConstantsJSON.REQUEST_SENSORS);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                        conn.setRequestProperty("Connection", "close");
                    }

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("fileupload", rutaDirectorioZip);

                    dOut = new DataOutputStream(conn.getOutputStream());
                    dOut.writeBytes(twoHyphens + boundary + lineEnd);
                    dOut.writeBytes("Content-Disposition: form-data; name=\"fileupload\";filename=\"" + rutaDirectorioZip + "\"" + lineEnd);

                    dOut.writeBytes(lineEnd);

                    bytesAvailable = fileIn.available();
                    bufferSize = Math.min(bytesAvailable, maxBuffersize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileIn.read(buffer, 0, bufferSize);

                    while(bytesRead > 0)
                    {
                        dOut.write(buffer, 0, bufferSize);
                        bytesAvailable = fileIn.available();
                        bufferSize = Math.min(bytesAvailable, maxBuffersize);
                        bytesRead = fileIn.read(buffer, 0, bufferSize);
                    }

                    dOut.writeBytes(lineEnd);
                    dOut.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    responseCode = conn.getResponseCode();
                    String responseMessage = conn.getResponseMessage();

                    Log.i("UPLOAD SENSOR", "HTTP Response is: " + responseCode + ": " + responseMessage);

                    if(responseCode == 200) {
                        return true;
                    }

                    fileIn.close();
                    dOut.flush();
                    dOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

        }

        @Override
        protected void onPreExecute() {
            PowerManager pm = (PowerManager) activity.getApplicationContext().getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
            wakeLock.acquire();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            wakeLock.release();

            //Despues de lanzarla, indicamos que ya no es el primer envio
            //Si result es true es porque fue bien el envio y se cambia la variable de primer envio
            //Si result es false es porque hubo algo mal y no se cambia la variable de primer envio
            if (result) {
                firstSend = false;
                lastSend = false;
            }else{
                firstSend = true;
                lastSend = false;
            }

            //Borramos los archivos enviados de la tablet
            deleteFile(rutaJson, nombre_fichero_json + terminacionJson);
            deleteFile(rutaZip, nombre_zip);

        }

        @Override
        protected void onCancelled() {
            wakeLock.release();
        }
    }


    //Tarea asincrona para enviar informacion de los gps al servidor
    private class SendInformationGpsTask extends AsyncTask<Void, Void, Boolean> {

        DirectoryPaths rutasDirectorios = new DirectoryPaths(activity);
        File rutaZip = rutasDirectorios.getZipDir();
        File rutaJson = rutasDirectorios.getJSONDir();
        String jsonGpsName = rutasDirectorios.getJSONGpsName();
        String zipGpsName = rutasDirectorios.getZipGpsName();
        String terminacionJson = ".json";
        String rutaDirectorioZip;

        String nombre_fichero_json;
        String nombre_zip;


        @Override
        protected Boolean doInBackground(Void... params) {

            JSONParser jsonParser = new JSONParser();

            //Recuperamos la lista de elementos a enviar
            int numElementos = valuesLocationToSend.size();

            //Si no hay elementos, no haremos nada
            if (numElementos == 0){
                return false;
            }else{
                try {

                    List<LocationDTO> elementsToSend = new ArrayList<LocationDTO>();
                    for (int i = 0; i < numElementos; i++) {
                        LocationDTO e = valuesLocationToSend.remove();
                        elementsToSend.add(e);
                    }

                    //Recuperamos del shared preferences el email
                    String nombreUsuario = Utils.recuperarNombreUsuario(activity);

                    //Creamos el json a enviar
                    GpsJSON gpsJsonToSend = new GpsJSON(nombreUsuario, provider, elementsToSend);

                    //Creamos un numero aleatorio para concatenar al nombre
                    Random generator = new Random();
                    int n1 = Math.abs(generator.nextInt() % 6);

                    //Creamos un fichero json con el contenido de dicho json
                    File directorio_json = rutaJson;
                    nombre_fichero_json = jsonGpsName + "_" + String.valueOf(n1);
                    String fileName = directorio_json.getAbsolutePath() + File.separator +
                            nombre_fichero_json + terminacionJson;
                    String[] files = new String[1];
                    files[0] = fileName;

                    jsonParser.crearFileJSON(gpsJsonToSend, fileName);

                    //Creamos el zip en la ruta indicada
                    File directorio_zip = rutaZip;
                    nombre_zip = zipGpsName + "_" + String.valueOf(n1);
                    rutaDirectorioZip = directorio_zip + File.separator + nombre_zip;
                    Compress c = new Compress(files, rutaDirectorioZip);
                    c.zip();

                } catch(InternalErrorException e) {
                    Log.e("GpsTask", "Problemas interno " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (ZipErrorException e) {
                    Log.e("GpsTask", "Problemas creanzo zip " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    Log.e("GpsTask", "Problemas accediendo al fichero " + e.getMessage());
                    e.printStackTrace();
                    return false;
                }

                //Enviamos el zip
                try {
                    HttpURLConnection conn = null;
                    DataOutputStream dOut = null;
                    String lineEnd = "\r\n";
                    String twoHyphens = "--";
                    String boundary = "*****";
                    int bytesRead, bytesAvailable, bufferSize;
                    byte[] buffer;
                    int maxBuffersize = 1*1024*1024;
                    File file = new File(rutaDirectorioZip);

                    FileInputStream fileIn = new FileInputStream(file);
                    //String serviceUrl = recuperarURLServicio();
                    String serviceUrl = SERVICE_URL;
                    URL url =  new URL(serviceUrl + ConstantsJSON.REQUEST_GPS);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                        conn.setRequestProperty("Connection", "close");
                    }

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("fileupload", rutaDirectorioZip);

                    dOut = new DataOutputStream(conn.getOutputStream());
                    dOut.writeBytes(twoHyphens + boundary + lineEnd);
                    dOut.writeBytes("Content-Disposition: form-data; name=\"fileupload\";filename=\"" + rutaDirectorioZip + "\"" + lineEnd);

                    dOut.writeBytes(lineEnd);

                    bytesAvailable = fileIn.available();
                    bufferSize = Math.min(bytesAvailable, maxBuffersize);
                    buffer = new byte[bufferSize];
                    bytesRead = fileIn.read(buffer, 0, bufferSize);

                    while(bytesRead > 0)
                    {
                        dOut.write(buffer, 0, bufferSize);
                        bytesAvailable = fileIn.available();
                        bufferSize = Math.min(bytesAvailable, maxBuffersize);
                        bytesRead = fileIn.read(buffer, 0, bufferSize);
                    }

                    dOut.writeBytes(lineEnd);
                    dOut.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    responseCode = conn.getResponseCode();
                    String responseMessage = conn.getResponseMessage();

                    Log.i("UPLOAD GPS", "HTTP Response is: " + responseCode + ": " + responseMessage);

                    if(responseCode == 200) {
                        return true;
                    }

                    fileIn.close();
                    dOut.flush();
                    dOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

                return true;
            }

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {


            //Borramos los archivos enviados de la tablet
            deleteFile(rutaJson, nombre_fichero_json + terminacionJson);
            deleteFile(rutaZip, nombre_zip);

        }

        @Override
        protected void onCancelled() {
            wakeLock.release();
        }
    }
}
