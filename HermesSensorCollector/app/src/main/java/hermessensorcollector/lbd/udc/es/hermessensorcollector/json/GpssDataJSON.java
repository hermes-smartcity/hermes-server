package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONArray;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.LocationDTO;


/**
 * Created by Leticia on 21/04/2016.
 */
public class GpssDataJSON extends JSONArray {

    public GpssDataJSON(List<LocationDTO> gpss)throws InternalErrorException {

        for (LocationDTO gps: gpss) {
            GpsDataJSON gpsJson = new GpsDataJSON(gps);
            this.put(gpsJson);
        }

    }
}
