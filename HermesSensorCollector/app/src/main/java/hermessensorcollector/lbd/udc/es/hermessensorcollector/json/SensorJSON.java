package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorJSON extends JSONObject {

    public SensorJSON(String userId, String type, Boolean firstSend, Boolean lastSend, List<SensorDTO> sensores) throws InternalErrorException {
        try {

            this.put(ConstantsJSON.PARAM_USERID, userId);
            this.put(ConstantsJSON.PARAM_TYPE, type);
            this.put(ConstantsJSON.PARAM_FIRSTSEND, firstSend);
            this.put(ConstantsJSON.PARAM_LASTSEND, lastSend);

            SensorsDataJSON sensoresJSON = new SensorsDataJSON(sensores);
            this.put(ConstantsJSON.PARAM_SENSORDATA, sensoresJSON);

        } catch (JSONException e) {
            throw new InternalErrorException(e);
        }
    }
}
