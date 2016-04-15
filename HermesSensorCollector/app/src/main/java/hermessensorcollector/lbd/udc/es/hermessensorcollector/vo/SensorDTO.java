package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import java.util.Calendar;

/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorDTO {

    private Calendar timeStamp;
    private Double valueX;
    private Double valueY;
    private Double valueZ;

    public SensorDTO(){}

    public SensorDTO(Calendar timeStamp, Double valueX, Double valueY, Double valueZ) {
        this.timeStamp = timeStamp;
        this.valueX = valueX;
        this.valueY = valueY;
        this.valueZ = valueZ;
    }

    public Calendar getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Calendar timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Double getValueX() {
        return valueX;
    }

    public void setValueX(Double valueX) {
        this.valueX = valueX;
    }

    public Double getValueY() {
        return valueY;
    }

    public void setValueY(Double valueY) {
        this.valueY = valueY;
    }

    public Double getValueZ() {
        return valueZ;
    }

    public void setValueZ(Double valueZ) {
        this.valueZ = valueZ;
    }
}
