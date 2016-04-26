package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONArray;
import org.json.JSONException;

import java.math.BigDecimal;

import hermessensorcollector.lbd.udc.es.hermessensorcollector.exception.InternalErrorException;
import hermessensorcollector.lbd.udc.es.hermessensorcollector.vo.SensorDTO;


/**
 * Created by Leticia on 19/04/2016.
 */
public class ValuesJSON extends JSONArray{

    public ValuesJSON(float[] values) throws InternalErrorException {

        for (float value: values) {
            BigDecimal valor = new BigDecimal(String.valueOf(value));
            this.put(valor);
        }
    }
}
