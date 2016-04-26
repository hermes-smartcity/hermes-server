package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

/**
 * Clase generica con diferentes constantes usadas en la generacion/recuperacion del json
 *
 * Created by Leticia on 14/04/2016.
 */
public class ConstantsJSON {

    //URL servicio REST
    public static String REQUEST_SENSORS = "api/sensordata/sensors";
    public static String REQUEST_GPS = "api/sensordata/gps";

    //Parametros para el json de sensores
    public static String PARAM_USERID = "userID";
    public static String PARAM_TYPE = "typeSensor";
    public static String PARAM_FIRSTSEND = "firstSend";
    public static String PARAM_LASTSEND = "lastSend";
    public static String PARAM_SENSORDATA = "sensorData";
    public static String PARAM_TIMESTAMP = "timeStamp";
    public static String PARAM_VALUES = "values";

    //Parametros para el json de gps
    public static String PARAM_GPS = "gps";
    public static String PARAM_TIME = "time";
    public static String PARAM_LONGITUDE = "longitude";
    public static String PARAM_LATITUDE = "latitude";
    public static String PARAM_ALTITUDE = "altitude";
    public static String PARAM_SPEED = "speed";
    public static String PARAM_BEARING = "bearing";
    public static String PARAM_ACCURACY = "accuracy";
    public static String PARAM_PROVIDER = "provider";

}
