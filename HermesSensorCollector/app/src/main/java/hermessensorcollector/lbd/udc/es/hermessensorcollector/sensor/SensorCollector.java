package hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.MainActivity;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.ZipErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.JSONParser;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.json.SensorJSON;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Compress;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.DirectoryPaths;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.utils.Utils;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;

/**
 * Created by Leticia on 15/04/2016.
 */
public class SensorCollector implements SensorEventListener {

    private Double[] previousValues;

    private Queue<SensorDTO> valuesToSend = new ArrayDeque<SensorDTO>();

    private Activity activity;
    private SensorManager mgr;
    private Sensor sensor;
    private int numValues;

    private SendInformationTask tarea;

    public SensorCollector(Activity activity, SensorManager mgr, Sensor sensor, int numValues){
        this.activity = activity;
        this.mgr = mgr;
        this.sensor = sensor;
        this.numValues = numValues;

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
        long tiempo = event.timestamp;
        float[] valores = event.values;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(tiempo);

        SensorDTO sensorDTO = new SensorDTO(cal, valores);
        valuesToSend.add(sensorDTO);
    }

    private void actualizarValoresAnteriores(float[] newValues){
        for (int i = 0; (i < newValues.length); i++) {
            float newValue = newValues[i];
            previousValues[i] = Double.parseDouble(Float.toString(newValue));;
        }
    }

    public void launchTask(){
        tarea = new SendInformationTask();
        tarea.execute();
    }

    public void stopTask(){
        tarea.cancel(true);
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

    private class SendInformationTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {

            JSONParser jsonParser = new JSONParser();

            DirectoryPaths rutasDirectorios = new DirectoryPaths(activity);
            File rutaZip = rutasDirectorios.getZipDir();
            File rutaJson = rutasDirectorios.getJSONDir();
            String jsonName = rutasDirectorios.getJSONName();
            String zipName = rutasDirectorios.getZipName();
            String terminacionJson = ".json";

            try {
                //Borramos el directorio json y el zip
                inicializarDirectorioJSON(rutaJson);
                inicializarDirectorioZip(rutaZip);

                //Recuperamos la lista de elementos a enviar
                int numElementos = valuesToSend.size();
                List<SensorDTO> elementsToSend = new ArrayList<SensorDTO>();
                for (int i = 0; i < numElementos; i++) {
                    SensorDTO e = valuesToSend.remove();
                    elementsToSend.add(e);
                }

                //Recuperamos del shared preferences el email
                String nombreUsuario = Utils.recuperarNombreUsuario(activity);

                //Creamos el json a enviar
                SensorJSON sensorJsonToSend = new SensorJSON(nombreUsuario, elementsToSend);

                //Creamos un fichero json con el contenido de dicho json
                File directorio_json = rutaJson;
                String nombre_fichero_json = jsonName;
                String terminacion = terminacionJson;
                String fileName = directorio_json.getAbsolutePath() + File.separator +
                        nombre_fichero_json + terminacion;
                String[] files = new String[1];
                files[0] = fileName;

                jsonParser.crearFileJSON(sensorJsonToSend, fileName);

                //Creamos el zip en la ruta indicada
                File directorio_zip = rutaZip;
                String nombre_zip = zipName;
                String rutaDirectorioZip = directorio_zip + File.separator + nombre_zip;
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

            return true;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result){
                //Informamos de que el envio ha sido correcto
            }else{
                //Informamos de que ha habido algun error enviando los ficheros
            }
        }

        @Override
        protected void onCancelled() {
            //hacer algo??
            Utils.crearMensajeToast(activity, "Se ha cancelado la tarea");
        }
    }
}
