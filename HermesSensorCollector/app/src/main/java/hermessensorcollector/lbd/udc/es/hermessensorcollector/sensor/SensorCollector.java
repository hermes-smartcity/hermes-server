package hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PowerManager;
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
import java.util.Calendar;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.MainActivity;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ZipErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.facade.FacadeSettings;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.ConstantsJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.JSONParser;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.SensorJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Compress;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Constants;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.DirectoryPaths;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.Parameter;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;

/**
 * Created by Leticia on 15/04/2016.
 */
public class SensorCollector implements SensorEventListener {

    private Double[] previousValues;

    private Queue<SensorDTO> valuesToSend = new ArrayDeque<SensorDTO>();

    private FacadeSettings facadeSettings;
    private Activity activity;
    private SensorManager mgr;
    private Sensor sensor;
    private int numValues;
    private String typeSensor;

    //static final int UPDATE_INTERVAL = 60000*5; //5 Minutos
    static final int UPDATE_INTERVAL = 60000*2; //5 Minutos
    private Timer timer = null;

    private PowerManager.WakeLock wakeLock;
    private int responseCode;

    //variable para saber cuando es el primer envio y el ultimo
    private Boolean firstSend = false;
    private Boolean lastSend = false;

    public SensorCollector(FacadeSettings facadeSetting, Activity activity, SensorManager mgr, Sensor sensor, int numValues, String typeSensor){
        this.facadeSettings = facadeSetting;
        this.activity = activity;
        this.mgr = mgr;
        this.sensor = sensor;
        this.numValues = numValues;
        this.typeSensor = typeSensor;

        //Inicializamos previousValues segun el numero de elementos del sensor
        previousValues = new Double[numValues];
        for (int i = 0; i < numValues; i++) {
            previousValues[i] = Double.MAX_VALUE;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == sensor.getType()) {

            for (int i = 0; (i < event.values.length); i++) {
                //Recuperamos el valor anterior
                Double previousValue = previousValues[i];
                float actualValueFloat = event.values[i];
                Double actualValue = Double.parseDouble(Float.toString(actualValueFloat));

                if (Math.abs( actualValue - previousValue) > sensor.getResolution()){
                    //Si alguno de ellos lo cumple, entonces:
                    //a - asignamos los valores a enviar
                    //b - actualizamos los valores anterior para todos
                    //c - salimos del bucle porque no queremos que se ejecute para los
                    //siguientes puesto que ya se ha hecho en los pasos a y b
                    asignarValoresEnviar(event);
                    actualizarValoresAnteriores(event.values);
                    break;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerSensorCollector(){
        mgr.registerListener(SensorCollector.this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterSensorCollector(){
        mgr.unregisterListener(SensorCollector.this);
    }

    private void asignarValoresEnviar(SensorEvent event){
        //long tiempo = event.timestamp;
        long tiempo = System.currentTimeMillis();
        float[] valores = event.values;

        SensorDTO sensorDTO = new SensorDTO(tiempo, valores);
        valuesToSend.add(sensorDTO);
    }

    private void actualizarValoresAnteriores(float[] newValues){
        for (int i = 0; (i < newValues.length); i++) {
            float newValue = newValues[i];
            previousValues[i] = Double.parseDouble(Float.toString(newValue));;
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
                SendInformationTask tarea = new SendInformationTask();
                tarea.execute();
            }
        },0, UPDATE_INTERVAL);

    }

    public void stopTask(){
        timer.cancel();
        timer = null;

        //indicamos que es el ultimo envio
        firstSend = false;
        lastSend = true;

        //Lanzamos la tarea por si quedaban cosas sin enviar
        SendInformationTask tarea = new SendInformationTask();
        tarea.execute();
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


    private String recuperarURLServicio(){
        String serviceUrl = null;

        try {
            //Recuperamos la lista de parametros que nos interesa
            List<String> paramBuscar = new ArrayList<String>();
            paramBuscar.add(Constants.SERVICE_URL);

            List<Parameter> listadoParam = facadeSettings.getListValueParameters(paramBuscar);

            for (int i=0;i<listadoParam.size();i++){
                Parameter param = listadoParam.get(i);
                if (param.getName().equals(Constants.SERVICE_URL)){
                    serviceUrl = param.getValue();
                    break;
                }
            }


        } catch (InternalErrorException e) {
            Log.e("MainActivity", "Error recuperando los parametros de la base de datos");
        }

        return serviceUrl;
    }
    private class SendInformationTask extends AsyncTask<Void, Void, Boolean> {

        DirectoryPaths rutasDirectorios = new DirectoryPaths(activity);
        File rutaZip = rutasDirectorios.getZipDir();
        File rutaJson = rutasDirectorios.getJSONDir();
        String jsonName = rutasDirectorios.getJSONName();
        String zipName = rutasDirectorios.getZipName();
        String terminacionJson = ".json";
        String rutaDirectorioZip;

        String nombre_fichero_json;
        String nombre_zip;


        @Override
        protected Boolean doInBackground(Void... params) {

            JSONParser jsonParser = new JSONParser();

            //Recuperamos la lista de elementos a enviar
            int numElementos = valuesToSend.size();

            //Si no hay elementos, no haremos nada  y tampoco cambiaremos
            //el valor del primer envio
            if (numElementos == 0){
                return false;
            }else{
                try {

                    List<SensorDTO> elementsToSend = new ArrayList<SensorDTO>();
                    for (int i = 0; i < numElementos; i++) {
                        SensorDTO e = valuesToSend.remove();
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
                    nombre_fichero_json = jsonName + "_" + String.valueOf(n1);
                    String fileName = directorio_json.getAbsolutePath() + File.separator +
                            nombre_fichero_json + terminacionJson;
                    String[] files = new String[1];
                    files[0] = fileName;

                    jsonParser.crearFileJSON(sensorJsonToSend, fileName);

                    //Creamos el zip en la ruta indicada
                    File directorio_zip = rutaZip;
                    nombre_zip = zipName + "_" + String.valueOf(n1);
                    rutaDirectorioZip = directorio_zip + File.separator + nombre_zip;
                    Compress c = new Compress(files, rutaDirectorioZip);
                    c.zip();

                } catch(InternalErrorException e) {
                    Log.e("SendInformationTask", "Problemas interno " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (ZipErrorException e) {
                    Log.e("SendInformationTask", "Problemas creanzo zip " + e.getMessage());
                    e.printStackTrace();
                    return false;
                } catch (IOException e) {
                    Log.e("SendInformationTask", "Problemas accediendo al fichero " + e.getMessage());
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
                    String serviceUrl = recuperarURLServicio();
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

                    Log.i("UPLOAD", "HTTP Response is: " + responseCode + ": " + responseMessage);

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
}
