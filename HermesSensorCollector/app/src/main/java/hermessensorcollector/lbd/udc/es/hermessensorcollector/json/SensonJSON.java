package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 14/04/2016.
 */
public class SensonJSON extends JSONObject {

    public SensonJSON(String userId, List<SensorDTO> sensores) throws InternalErrorException {
        try {

            this.put(ConstantsJSON.PARAM_USERID, userId);

            SensorsDataJSON sensoresJSON = new SensorsDataJSON(sensores);
            this.put(ConstantsJSON.PARAM_SENSORDATA, sensoresJSON);

        } catch (JSONException e) {
            throw new InternalErrorException(e);
        }
    }
}
