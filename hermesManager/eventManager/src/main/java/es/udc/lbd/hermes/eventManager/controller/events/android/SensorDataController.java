package es.udc.lbd.hermes.eventManager.controller.events.android;

import java.io.File;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;
import es.udc.lbd.hermes.model.sensordata.service.SensorDataService;

@RestController
@RequestMapping(value = "/api/sensordata")
public class SensorDataController  extends MainResource{

	@Autowired private SensorDataService sensorDataServicio;
	
	@RequestMapping(value = "/sensors", method = RequestMethod.POST)
    @ResponseBody
    public String uploadInformationSensor(@RequestParam("fileupload") MultipartFile fileupload) throws Exception {
	
		if (!fileupload.isEmpty()) {
			
			try {
				byte[] buf = new byte[1024];
				
				//Descomprimimos el zip
				//get the zip file content
		    	ZipInputStream zis = new ZipInputStream(fileupload.getInputStream());
		    	//get the zipped file list entry
		    	ZipEntry ze = null;
		    	
		    	while((ze=zis.getNextEntry())!=null){
		    		int n;
		    		
		    		//Solo procesamos si el archivo dentro del zip es .json
		    		String nameFile = ze.getName();
		    		
		    		String terminacion = nameFile.substring(nameFile.length()-4);
		    		
		    		if (terminacion.equals("json")){
		    			File fileJson = File.createTempFile("json", null);
			    		FileOutputStream fileoutputstream = new FileOutputStream(fileJson);
		    			
		    			while ((n = zis.read(buf, 0, 1024)) > -1) {
		                    fileoutputstream.write(buf, 0, n);
		                }
		    			
		    			fileoutputstream.flush();
		    			fileoutputstream.close();
		    			
		    			//Recuperamos el file json que viene en el zip
						ObjectMapper mapper = new ObjectMapper();

						//JSON from file to Object
						SensorsDataJson sensorsDataJson = mapper.readValue(fileJson, SensorsDataJson.class);

						sensorDataServicio.parserSensors(sensorsDataJson);
		    		}
		    		
		    	}
		     	   
		    

			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error while loading the file");
			}
		}
		
	                
        return null;
	}
	
}
