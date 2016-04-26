package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONArray;

import java.util.List;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 14/04/2016.
 */
public class SensorsDataJSON extends JSONArray {

    public SensorsDataJSON(List<SensorDTO> sensors)throws InternalErrorException {

        for (SensorDTO sensor: sensors) {
            SensorDataJSON sensorDataJson = new SensorDataJSON(sensor);
            this.put(sensorDataJson);
        }

    }
}
