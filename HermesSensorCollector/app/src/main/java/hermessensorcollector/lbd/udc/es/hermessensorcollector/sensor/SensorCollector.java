package hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayDeque;
import java.util.Queue;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.MainActivity;

/**
 * Created by Leticia on 15/04/2016.
 */
public class SensorCollector implements SensorEventListener {

    private Double previousValue = Double.MAX_VALUE;
    private Queue<Double> valuesToSend = new ArrayDeque<Double>();

    private SensorManager mgr;
    private Sensor sensor;

    public SensorCollector(SensorManager mgr, Sensor sensor){
        this.mgr = mgr;
        this.sensor = sensor;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

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
}
