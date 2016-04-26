package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import android.graphics.Point;

/**
 * Created by Leticia on 21/04/2016.
 */
public class LocationDTO {

    private long time;
    private Double longitude;
    private Double latitude;
    private Double altitude;
    private Double speed;
    private Double bearing;
    private Double accuracy;

    public LocationDTO(){}

    public LocationDTO(long time, Double longitude, Double latitude, Double altitude, Double speed, Double bearing,
                       Double accuracy) {
        this.time = time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.speed = speed;
        this.bearing = bearing;
        this.accuracy = accuracy;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double getBearing() {
        return bearing;
    }

    public void setBearing(Double bearing) {
        this.bearing = bearing;
    }

    public Double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Double accuracy) {
        this.accuracy = accuracy;
    }

}
