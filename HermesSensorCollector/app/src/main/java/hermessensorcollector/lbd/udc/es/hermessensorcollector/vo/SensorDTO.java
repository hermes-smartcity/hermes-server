package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import java.util.Calendar;

/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorDTO {

    private Calendar timeStamp;
    private float[] values;

    public SensorDTO(){}

    public SensorDTO(Calendar timeStamp, float[] values) {
        this.timeStamp = timeStamp;
        this.values = values;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float[] getValues() {
        return values;
    }

    public void setValues(float[] values) {
        this.values = values;
    }
}
