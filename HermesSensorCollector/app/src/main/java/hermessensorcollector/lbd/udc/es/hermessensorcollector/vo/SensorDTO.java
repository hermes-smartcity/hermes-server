package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import java.util.Calendar;

/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorDTO {

    private long timeStamp;
    private float[] values;

    public SensorDTO(){}

    public SensorDTO(long timeStamp, float[] values) {
        this.timeStamp = timeStamp;
        this.values = values;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
