package hermessensorcollector.lbd.udc.es.hermessensorcollector.sensor;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;

/**
 * Created by Leticia on 15/04/2016.
 */
public class SensorService extends Service {



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
