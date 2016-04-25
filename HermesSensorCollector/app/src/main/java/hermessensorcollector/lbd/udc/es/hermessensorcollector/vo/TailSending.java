package hermessensorcollector.lbd.udc.es.hermessensorcollector.vo;

import java.util.Date;

/**
 * Created by Leticia on 22/04/2016.
 */
public class TailSending {

    private Long id;
    private Date date;
    private String routezip;

    public TailSending(){}

    public TailSending(Long id, Date date, String routezip) {
        this.id = id;
        this.date = date;
        this.routezip = routezip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
