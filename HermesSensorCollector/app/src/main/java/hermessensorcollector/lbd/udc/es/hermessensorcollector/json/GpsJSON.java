package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.LocationDTO;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 21/04/2016.
 */
public class GpsJSON extends JSONObject {

    public GpsJSON(String userId, String provider, List<LocationDTO> gpss) throws InternalErrorException {
        try {

            this.put(ConstantsJSON.PARAM_USERID, userId);
            this.put(ConstantsJSON.PARAM_PROVIDER, provider);

            GpssDataJSON gpssJSON = new GpssDataJSON(gpss);
            this.put(ConstantsJSON.PARAM_GPS, gpssJSON);

        } catch (JSONException e) {
            throw new InternalErrorException(e);
        }
    }
}
