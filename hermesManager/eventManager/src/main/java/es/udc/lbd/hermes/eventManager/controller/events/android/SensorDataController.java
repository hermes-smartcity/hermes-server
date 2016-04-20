package es.udc.lbd.hermes.eventManager.controller.events.android;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.udc.lbd.hermes.eventManager.util.Helpers;
import es.udc.lbd.hermes.eventManager.web.rest.MainResource;
import es.udc.lbd.hermes.model.events.ListaEventosYdias;
import es.udc.lbd.hermes.model.sensordata.SensorDataType;
import es.udc.lbd.hermes.model.sensordata.SensorsDataJson;
import es.udc.lbd.hermes.model.sensordata.service.SensorDataService;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.Rol;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.UsuarioWeb;
import es.udc.lbd.hermes.model.usuario.usuarioWeb.service.UsuarioWebService;

@RestController
@RequestMapping(value = "/api/sensordata")
public class SensorDataController  extends MainResource{

	@Autowired private SensorDataService sensorDataServicio;
	@Autowired private UsuarioWebService usuarioWebService;
	
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
	
	
	@RequestMapping(value="/json/infoPorDia", method = RequestMethod.GET)
	public ListaEventosYdias getInfoPorDia(
			@RequestParam(required = true) SensorDataType sensor,
			@RequestParam(value = "idUsuario", required = false) Long idUsuario,		
			@RequestParam(value = "fechaIni", required = true) String fechaIni,
			@RequestParam(value = "fechaFin", required = true) String fechaFin) {
		
		// Un usuario tipo consulta solo puede ver sus propios eventos
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UsuarioWeb usuario = (UsuarioWeb) usuarioWebService.loadUserByUsername(auth.getName());
		if(usuario.getRol().equals(Rol.ROLE_CONSULTA)){
			idUsuario = usuario.getUsuarioMovil().getId();			
		}
		Calendar ini = Helpers.getFecha(fechaIni);
		Calendar fin = Helpers.getFecha(fechaFin);
		//return measurementServicio.obterEventosPorDia(tipo, idUsuario, ini, fin);
		
		return null;
		
	}
}
