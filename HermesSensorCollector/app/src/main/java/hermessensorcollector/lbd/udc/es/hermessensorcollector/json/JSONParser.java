package hermessensorcollector.lbd.udc.es.hermessensorcollector.json;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Leticia on 15/04/2016.
 */
public class JSONParser {

    /**
     * Crear un fichero json a partir del nombre y del objeto
     * @param obj JSONObject que queremos incluir en el fichero
     * @param fileName Nombre del fichero
     * @throws IOException
     */
    public void crearFileJSON(JSONObject obj, String fileName) throws IOException{

        try {

            FileWriter file = new FileWriter(fileName);
            file.write(obj.toString());
            file.flush();
            file.close();

        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
