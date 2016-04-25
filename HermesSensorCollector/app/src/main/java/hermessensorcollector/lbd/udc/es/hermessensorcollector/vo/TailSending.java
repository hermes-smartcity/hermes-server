package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import java.util.Date;

/**
 * Created by Leticia on 22/04/2016.
 */
public class TailSending {

    private Long id;
    private String type;
    private Date date;
    private String routezip;

    public TailSending(){}

    public TailSending(Long id, String type, Date date, String routezip) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.routezip = routezip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRoutezip() {
        return routezip;
    }

    public void setRoutezip(String routezip) {
        this.routezip = routezip;
    }
}
