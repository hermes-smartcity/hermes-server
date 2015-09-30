package es.enxenio.smart.citydriver.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;


public class JsonUtil {
	
	private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

	public static ObjectMapper getMapperToSerializar(){
		ObjectMapper mapper = new ObjectMapper();
		
		// Serialización
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Deserialización
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		mapper.setDateFormat(StdDateFormat.getISO8601Format(Calendar.getInstance().getTimeZone()));
		
		// Usar el timezone actual del servidor para codificar
		mapper.setTimeZone(Calendar.getInstance().getTimeZone());
		return mapper;
	}
	
	public static ObjectMapper getMapperToDeserializar(){
		ObjectMapper mapper = new ObjectMapper();
		
		// Serialización
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Deserialización
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		final DateFormat df = new ISO8601DateFormat();
		mapper.setDateFormat(df);
		
		// Usar el timezone actual del servidor para codificar
		mapper.setTimeZone(Calendar.getInstance().getTimeZone());
		return mapper;
	}
	
	public static ObjectMapper getMapperToConsultas(){
		ObjectMapper mapper = new ObjectMapper();
		
		// Serialización
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		// Deserialización
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// Mapear las fechas con formatio ISO8601
		String DF = "dd/MM/yyyy";
		final DateFormat df = new SimpleDateFormat(DF);
		mapper.setDateFormat(df);
		// Usar el timezone actual del servidor para codificar
		mapper.setTimeZone(Calendar.getInstance().getTimeZone());
		
		return mapper;
	}
	
	public static void escribirRespuestaJSON(HttpServletResponse response, String json) {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(json);
			out.close();
		} catch (IOException e) {
			log.error("Error escribiendo la respuesta.", e);
		}
	}

	/**
	 * Escapa un string json construido con jackson para que luego en la jsp se pueda
	 * 'imprimir' a una variable javascript y hacer un $.parseJSON(). Esto no haría falta
	 * si ese json se carga por ajax. El tema es los campos con comillas simples, dobles o
	 * saltos de linea, jackson lo hace correctamente, pero en la jspx hay varios
	 * problemas por pasarlo a un objeto js de esta forma:
	 * 
	 * var jsonValoracion = $.parseJSON('${jsonValoracion}');
	 * 
	 * Se podrían usar comillas simples, como en el ejemplo, o dobles, ya que el método
	 * escapa todas las comillas por si acaso en algún sitio se está usando el
	 * $.parseJSON("...");
	 * 
	 * 
	 * Se podría hacer de forma similar en la jsp:
	 * 
	 * <!-- 
	 * <c:set var="jsonValoracion" value="${fn:replace(jsonValoracion, '\\', '\\\\')}"/> 
	 * <c:set var="jsonValoracion" value="${fn:replace(jsonValoracion, '\'', '\\\'')}"/> 
	 * -->
	 * 
	 * 07/03/2014 Realmente no haría falta hacer un $.parseJSON en la jsp, y demás evitamos todos los replaces
	 * Salvo el 'prevent xss attack'
	 * 
	 */
	public static String escapeForJSP(String json) {
		if (StringUtils.isNotBlank(json)) {
			// \ por \\ - > casos \n\r...
			// ' que pueda haber como valores
			// " de json (las dobles como valores vienen ok de jackson)
			// json = json.replace("\\", "\\\\");
			// json = json.replace("'", "\\'");
			// json = json.replace("\"", "\\\"");

			// escape forward-slashes - 'prevent xss attack'
			// json = json.replace("</script>", "<\\/script>");
			// json = json.replace("/", "\\/");
			// http://benalpert.com/2012/08/03/preventing-xss-json.html
			// http://jfire.io/blog/2012/04/30/how-to-securely-bootstrap-json-in-a-rails-view/
			json = json.replace("</", "<\\/");
		}
		return json;
	}
	
}


