package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONException;
import org.json.JSONObject;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorDataJSON extends JSONObject {

    public SensorDataJSON(SensorDTO sensorDTO) throws InternalErrorException {
        try {

            this.put(ConstantsJSON.PARAM_TIMESTAMP, sensorDTO.getTimeStamp());
            this.put(ConstantsJSON.PARAM_VALUES, sensorDTO.getValues());

        } catch (JSONException e) {
            throw new InternalErrorException(e);
        }
    }
}
