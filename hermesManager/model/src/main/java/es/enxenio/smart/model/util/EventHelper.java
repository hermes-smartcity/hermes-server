package es.enxenio.smart.model.util;

import java.util.Calendar;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;

import es.enxenio.smart.model.events.EventType;
import es.enxenio.smart.model.events.measurement.MeasurementType;
import es.enxenio.smart.model.usuario.Usuario;
import es.enxenio.smart.model.usuario.service.UsuarioService;


public class EventHelper {
	
	@Autowired
	private UsuarioService usuarioService;

	private Long idAutor;

	private Calendar timestamp;

	private String eventId;

	//Para los eventos data section iran a null
	private Point position;

	//Para los eventos data section iran a null
	private Double value;

	//Atributos para dataSection
	private int minHeartRate;

	private int maxBeatBeat;

	private int maxHeartRate;

	private Double standardDeviationSpeed;

	private int  minBeatBeat;

	private Double minSpeed;

	private Double averageSpeed;

	private Double standardDeviationBeatBeat;

	private Double heartRate;

	private Double medianSpeed;

	private Double standardDeviationHeartRate;

	private Double maxSpeed;

	private Double pke;

	private LineString roadSection;

	private int medianHeartRate;

	private Double meanBeatBeat;

	private int medianBeatBeat;

	// Solo para los eventos tipo High Heart Rate
	private String orientation;

	// Solo para measurement, distinguir al tipo al que pertenece
	private MeasurementType tipo;
	
	private Usuario usuario;

	public EventHelper() {

	}

	public Long getIdAutor() {
		return idAutor;
	}

	public void setIdAutor(Long idAutor) {
		this.idAutor = idAutor;
	}

	public Calendar getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Calendar timestamp) {
		this.timestamp = timestamp;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public int getMinHeartRate() {
		return minHeartRate;
	}

	public void setMinHeartRate(int minHeartRate) {
		this.minHeartRate = minHeartRate;
	}

	public int getMaxBeatBeat() {
		return maxBeatBeat;
	}

	public void setMaxBeatBeat(int maxBeatBeat) {
		this.maxBeatBeat = maxBeatBeat;
	}

	public int getMaxHeartRate() {
		return maxHeartRate;
	}

	public void setMaxHeartRate(int maxHeartRate) {
		this.maxHeartRate = maxHeartRate;
	}

	public Double getStandardDeviationSpeed() {
		return standardDeviationSpeed;
	}

	public void setStandardDeviationSpeed(Double standardDeviationSpeed) {
		this.standardDeviationSpeed = standardDeviationSpeed;
	}

	public int getMinBeatBeat() {
		return minBeatBeat;
	}

	public void setMinBeatBeat(int minBeatBeat) {
		this.minBeatBeat = minBeatBeat;
	}

	public Double getMinSpeed() {
		return minSpeed;
	}

	public void setMinSpeed(Double minSpeed) {
		this.minSpeed = minSpeed;
	}

	public Double getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(Double averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public Double getStandardDeviationBeatBeat() {
		return standardDeviationBeatBeat;
	}

	public void setStandardDeviationBeatBeat(Double standardDeviationBeatBeat) {
		this.standardDeviationBeatBeat = standardDeviationBeatBeat;
	}

	public Double getHeartRate() {
		return heartRate;
	}

	public void setHeartRate(Double heartRate) {
		this.heartRate = heartRate;
	}

	public Double getMedianSpeed() {
		return medianSpeed;
	}

	public void setMedianSpeed(Double medianSpeed) {
		this.medianSpeed = medianSpeed;
	}

	public Double getStandardDeviationHeartRate() {
		return standardDeviationHeartRate;
	}

	public void setStandardDeviationHeartRate(Double standardDeviationHeartRate) {
		this.standardDeviationHeartRate = standardDeviationHeartRate;
	}

	public Double getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(Double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public Double getPke() {
		return pke;
	}

	public void setPke(Double pke) {
		this.pke = pke;
	}

	public LineString getRoadSection() {
		return roadSection;
	}

	public void setRoadSection(LineString roadSection) {
		this.roadSection = roadSection;
	}

	public int getMedianHeartRate() {
		return medianHeartRate;
	}

	public void setMedianHeartRate(int medianHeartRate) {
		this.medianHeartRate = medianHeartRate;
	}

	public Double getMeanBeatBeat() {
		return meanBeatBeat;
	}

	public void setMeanBeatBeat(Double meanBeatBeat) {
		this.meanBeatBeat = meanBeatBeat;
	}

	public int getMedianBeatBeat() {
		return medianBeatBeat;
	}

	public void setMedianBeatBeat(int medianBeatBeat) {
		this.medianBeatBeat = medianBeatBeat;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public MeasurementType getTipo() {
		return tipo;
	}

	public void setTipo(MeasurementType tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// Nos pasaran un evento y lo parsearemos
	// Info evento contendrá para cada tipo de evento su info relevante, diferente para cada tipo de evento
	public void prepararEventHelper(JSONObject evento, EventType tipoEvento, JSONObject infoEvento){		

		this.setTimestamp(Helpers.getFecha((String) evento.get("Timestamp")));
		this.setEventId((String) evento.get("Event-Id"));
		String sourceId = (String) evento.get("Source-Id");
		if(usuarioService.getBySourceId(sourceId)==null){
			Usuario usuario = new Usuario();
			usuario.setSourceId(sourceId);
			usuarioService.create(usuario);
			this.setUsuario(usuario);
		}
			
		
		switch (tipoEvento) {
		case VEHICLE_LOCATION: 
			this.setPosition((Point) prepararPunto(infoEvento));
			break;
		case HIGH_SPEED: case HIGH_ACCELERATION: case HIGH_DECELERATION:
			this.setPosition((Point) prepararPunto(infoEvento));
			this.setValue((Double) infoEvento.get("value"));
			this.setTipo(MeasurementType.getTipo(tipoEvento.getName()));

			break;
		case HIGH_HEART_RATE:
			this.setPosition((Point) prepararPunto(infoEvento));
			this.setValue((Double) infoEvento.get("value"));
			this.setOrientation((String) infoEvento.get("orientation"));
			this.setTipo(MeasurementType.HIGH_HEART_RATE);

			break;
		case DATA_SECTION:
			JSONObject valorObj = (JSONObject) infoEvento.get("Min Heart Rate");
			this.setMinHeartRate((int) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Max Beat-Beat");
			this.setMaxBeatBeat((int) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Standard Deviation Speed");
			this.setStandardDeviationSpeed((Double) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Min Beat-Beat");
			this.setMinBeatBeat((int) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Max Heart Rate");
			this.setMaxHeartRate((int) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Min Speed");
			this.setMinSpeed((Double) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Average Speed");
			this.setAverageSpeed((Double) valorObj.get("value"));		

			valorObj = (JSONObject) infoEvento.get("Standard Deviation Beat-Beat");
			this.setStandardDeviationBeatBeat((Double) valorObj.get("value"));		

			valorObj = (JSONObject) infoEvento.get("Heart Rate");
			this.setHeartRate((Double) valorObj.get("value")); 		

			valorObj = (JSONObject) infoEvento.get("Median Speed");
			this.setMedianSpeed((Double) valorObj.get("value"));	

			valorObj = (JSONObject) infoEvento.get("Standard Deviation Heart Rate");
			this.setStandardDeviationHeartRate((Double) valorObj.get("value"));	

			valorObj = (JSONObject) infoEvento.get("Max Speed");
			this.setMaxSpeed((Double) valorObj.get("value"));		

			valorObj = (JSONObject) infoEvento.get("PKE");
			this.setPke((Double) valorObj.get("value"));

			// El atributo "Road Section" contiene el array con los distintos puntos por los que pasa
			JSONObject objectoRoad = (JSONObject) infoEvento.get("Road Section ");
			// Recuperamos el array con los puntos
			JSONObject infoRoadSection = (JSONObject) objectoRoad.get("Road Section");
			JSONArray arrayPuntos = (JSONArray) infoRoadSection.get("roadSection");				
			this.setRoadSection((LineString) prepararRuta(arrayPuntos));

			valorObj = (JSONObject) infoEvento.get("Median Heart Rate");
			this.setMedianHeartRate((int) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Mean Beat-Beat");
			this.setMeanBeatBeat((Double) valorObj.get("value"));

			valorObj = (JSONObject) infoEvento.get("Median Beat-Beat");
			this.setMedianBeatBeat((int) valorObj.get("value"));
			break;
		default:
			break;
		}
		return;
	}
	
	private Geometry prepararPunto(JSONObject infoEvento){
		Double latitude = (Double) infoEvento.get("latitude");
		Double longitude = (Double) infoEvento.get("longitude");
		return Helpers.wktToGeometry("POINT("+ latitude.toString() + " "+ longitude.toString() + ")");
	}

	private Geometry prepararRuta(JSONArray arrayPuntos){
		Geometry ruta = null; 
		// Contruimos el array de puntos con linestring, primero el string con cada uno de los puntos (coordenadas latitud y longitud) que sacamos del objeto json
		String rutaStr="LINESTRING(";
		for(int i=0;i<arrayPuntos.size();i++){
			JSONObject puntoRuta = (JSONObject) arrayPuntos.get(i);
			rutaStr+=puntoRuta.get("latitud") + " " + puntoRuta.get("longitud") +" , ";
		}
		rutaStr = rutaStr.substring(0,rutaStr.length()-3)+")";
		// Lo convertimos a GIS
		ruta = Helpers.wktToGeometry(rutaStr);
		return ruta;
	}
}
