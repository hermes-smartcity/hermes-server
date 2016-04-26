package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONException;
import org.json.JSONObject;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.LocationDTO;

/**
 * Created by Leticia on 21/04/2016.
 */
public class GpsDataJSON extends JSONObject {

    public GpsDataJSON(LocationDTO locationDTO) throws InternalErrorException {
        try {

            this.put(ConstantsJSON.PARAM_TIME, locationDTO.getTime());
            this.put(ConstantsJSON.PARAM_LONGITUDE, locationDTO.getLongitude());
            this.put(ConstantsJSON.PARAM_LATITUDE, locationDTO.getLatitude());
            this.put(ConstantsJSON.PARAM_ALTITUDE, locationDTO.getAltitude());
            this.put(ConstantsJSON.PARAM_SPEED, locationDTO.getSpeed());
            this.put(ConstantsJSON.PARAM_BEARING, locationDTO.getBearing());
            this.put(ConstantsJSON.PARAM_ACCURACY, locationDTO.getAccuracy());

        } catch (JSONException e) {
            throw new InternalErrorException(e);
        }
    }
}
