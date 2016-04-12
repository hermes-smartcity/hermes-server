(function() {
	'use strict';

	angular.module('app').controller('DashboardController', DashboardController);

	DashboardController.$inject = ['$scope', 'usuarios', 'eventoProcesado', 'eventsToday', 'statistics', 
	                               '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope', '$state',
	                               'DTOptionsBuilder', '$translate'];

	function DashboardController($scope, usuarios, eventoProcesado, eventsToday, statistics, 
			$http, $timeout, $log, $filter, eventsService, $rootScope, $state, DTOptionsBuilder, 
			$translate) {
	
	var vm = this;
	vm.pintarMapaVehicleLocations = pintarMapaVehicleLocations;
	vm.pintarMapaDataSections = pintarMapaDataSections;
	vm.pintarMapaMeasurements = pintarMapaMeasurements;
	vm.pintarMapaContextData = pintarMapaContextData;
	vm.pintarMapaHigh = pintarMapaHigh;
	vm.pintarPuntos = pintarPuntos;
	vm.pintarLineas = pintarLineas;
	vm.aplicarFiltros = aplicarFiltros;
	vm.eventsType = $rootScope.eventsType;
	vm.measurementsType = $rootScope.measurementsType;
	vm.usuarios = usuarios;
	vm.eventsToday = eventsToday;
	vm.eventoProcesado = eventoProcesado;
	
	vm.totalMUsers = statistics.contarUsuariosMovil;
	vm.totalWebUsers = statistics.contarUsuariosWeb;
	vm.numberActiveUsers = statistics.numberActiveUsers;
	vm.totalL = statistics.totalVLocations;	
	vm.totalDS = statistics.totalDataScts;
	vm.totalM = statistics.totalMeasurements;
	vm.totalDF = statistics.totalDriversF;
	vm.totalSTD = statistics.totalStepsData;
	vm.totalSLD = statistics.totalSleepData;
	vm.totalHRD = statistics.totalHeartRateData;
	vm.totalCD = statistics.totalContextData;
	
	vm.mostrarMapa = mostrarMapa;
	vm.mostrarTabla = mostrarTabla;
	vm.showMap = true;
	vm.showTab = false;
	vm.onTimeSetStart = onTimeSetStart;
	vm.onTimeSetEnd = onTimeSetEnd;
	vm.showCalendarStart = false;
	vm.showCalendarEnd = false;
	vm.activeInput = $translate.instant('dashboard.mapa');
	vm.arrancar = arrancar;
	vm.parar = parar;

	// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
	if($rootScope.hasRole('ROLE_ADMIN')){
		eventsService.getStateActualizado().then(getStateActualizadoComplete);		
	}
	
	function getStateActualizadoComplete(response) {				
		vm.active = response.data;		
	}
	
	//Inicializar options de la tabla
	vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json");
	
	// Inicializamos el filtro de event type para que inicialmente liste vehicle Locations
	vm.eventTypeSelected = "VEHICLE_LOCATION";
	vm.listadoCarga = undefined;
	
	vm.startDate = new Date();
	// Inicializamos la fecha de inicio a la de ayer 
	vm.startDate.setDate(vm.startDate.getDate()-1);
	vm.endDate = new Date();
	var map = L.map( 'map', {
		  minZoom: 2,
		  zoom: 2
		});
	
	var markers = new L.MarkerClusterGroup({
	  spiderfyDistanceMultiplier: 0.5,
	  disableClusteringAtZoom: 12,
	  maxClusterRadius: 20
	});
	
    var osmLayer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
    var layerControl = L.control.layers(
		{'OpenStreetMap': osmLayer,
        'MapQuest Open': new L.TileLayer('http://otile{s}.mqcdn.com/tiles/1.0.0/map/{z}/{x}/{y}.jpg', {subdomains: '1234'}),
        'Stamen Toner Lite': new L.TileLayer('http://stamen-tiles-{s}.a.ssl.fastly.net/toner-lite/{z}/{x}/{y}.png'),
        'ESRI World Imagery': new L.TileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}')
		}, {'Events': markers});
    map.addControl(layerControl);
    var scaleControl = new L.control.scale({'imperial': false});
    map.addControl(scaleControl);
    map.addLayer(osmLayer);
    map.addLayer(markers);
	map.fitBounds([[-180,-90],[180,90]]);
	
	map.on('dragend', aplicarFiltros);
	map.on('zoomend', aplicarFiltros);

	function mostrarMapa() {	
		vm.showMap = true;
		vm.showTab = false;
		vm.activeInput = $translate.instant('dashboard.mapa');
		
		//Para evitar que se carguen las tablas de la parte Table
		vm.listadoCarga = undefined;
	}
	
	function mostrarTabla() {	
		vm.showMap = false;
		vm.showTab = true;
		vm.activeInput = $translate.instant('dashboard.tabla');
		
		//Para mostrar la tabla correspondiente
		if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
			vm.listadoCarga = "./partials/vehicleLocation/vehicleLocationListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
			vm.listadoCarga = "./partials/dataSection/dataSectionListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
			vm.listadoCarga = "./partials/contextData/contextDataListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "HIGH_SPEED") || 
				angular.equals(vm.eventTypeSelected, "HIGH_ACCELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_DECELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_HEART_RATE")){
			vm.listadoCarga = "./partials/measurement/measurementListar.html";
		}else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
			vm.listadoCarga = "./partials/measurement/measurementListar.html";
		} else{
			vm.listadoCarga = undefined;
		}
	}
	
	function arrancar() {
		var resultado = {
				value : "Running",
				valueInt : 0
		};
		vm.active = resultado;
		eventsService.arrancar();
		
		$state.go('dashboard');
	}
	
	function parar() {
		var resultado = {
				value : "Stopped",
				valueInt : 0
		};
		vm.active = resultado;
		eventsService.parar();
		
		$state.go('dashboard');
	}
	
	function aplicarFiltros() {		
		var pos = vm.eventTypeSelected.indexOf('_'); 
		var value = vm.eventTypeSelected.substr(0, pos);
		value += vm.eventTypeSelected.substr(pos+1, vm.eventTypeSelected.length);
		value = angular.lowercase(value);

		if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
			vm.pintarMapaVehicleLocations();
			//vm.listadoCarga = "./partials/vehicleLocation/vehicleLocationListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
			vm.pintarMapaDataSections();
			//vm.listadoCarga = "./partials/dataSection/dataSectionListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
			vm.pintarMapaContextData();
			//vm.listadoCarga = "./partials/contextData/contextDataListar.html";
		}else if(angular.equals(vm.eventTypeSelected, "HIGH_SPEED") || 
				angular.equals(vm.eventTypeSelected, "HIGH_ACCELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_DECELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_HEART_RATE")){
			vm.pintarMapaHigh();
			//vm.listadoCarga = "./partials/measurement/measurementListar.html";
		}else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
			vm.pintarMapaMeasurements();
			//vm.listadoCarga = "./partials/measurement/measurementListar.html";
		} else{
			console.log("No corresponde a ningún tipo --> En construcción");
		}
	}
	
	function infoPopup(userId, timestamp, eventType, eventValue) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var datosEvento = L.DomUtil.create('datosEvento');

		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +' <br/><b>Date:</b> '+dateEvento+'<br/><b>Time:</b> '+hourEvento+'<br/><b>Type:</b> '+eventType+'<br/><b>Value:</b> '+eventValue;
		return datosEvento;
	}
	
	function infoPopupContextData(userId, timestamp, eventActivity, eventAccuracy) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var datosEvento = L.DomUtil.create('datosEvento');

		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +' <br/><b>Date:</b> '+dateEvento+'<br/><b>Time:</b> '+hourEvento+'<br/><b>Detected Activity:</b> '+eventActivity +'<br/><b>Accuracy:</b> '+eventAccuracy;
		return datosEvento;
	}
	
	function infoPopupVehicleLocation(userId, timestamp, eventSpeed, eventAccuracy) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var speedEvento = $filter('number')(eventSpeed, 2);
		var datosEvento = L.DomUtil.create('datosEvento');
		
		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +' <br/><b>Date:</b> '+dateEvento+'<br/><b>Time:</b> '+hourEvento+'<br/><b>Speed:</b> '+speedEvento +'<br/><b>Accuracy:</b> '+eventAccuracy;
		return datosEvento;
	}
	
	function infoPopupHigh(userId, timestamp, eventValue, eventSpeed, eventAccuracy) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var valueEvento = $filter('number')(eventValue, 2);
		var speedEvento = $filter('number')(eventSpeed, 2);
		var datosEvento = L.DomUtil.create('datosEvento');
				
		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +' <br/><b>Date:</b> '+dateEvento+'<br/><b>Time:</b> '+hourEvento+'<br/><b>Value:</b> '+valueEvento+'<br/><b>Speed:</b> '+speedEvento +'<br/><b>Accuracy:</b> '+eventAccuracy;
		return datosEvento;
	}
	
	function infoPopupDataSection(userId, timestamp, eventAccuracy, eventMinSpeed, eventMaxSpeed, eventMedianSpeed, eventAverageSpeed, eventAverageEr, eventAverageHearRate, eventStandardDeviationSpeed, eventStandardDeviationRr, eventStandardDeviationHeartRate, eventPke, eventNumHighAccelerations, eventNumHighDecelerations, eventAverageAcceleration, eventAverageDeceleration, eventRrSection ) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var minSpeedEvento = $filter('number')(eventMinSpeed, 2);
		var maxSpeedEvento = $filter('number')(eventMaxSpeed, 2);
		var medianSpeedEvento = $filter('number')(eventMedianSpeed, 2);
		var averageSpeedEvento = $filter('number')(eventAverageSpeed, 2);
		var averageErEvento = $filter('number')(eventAverageEr, 2);
		var averageHearRateEvento = $filter('number')(eventAverageHearRate, 2);
		var standardDeviationSpeedEvento = $filter('number')(eventStandardDeviationSpeed, 2);
		var standardDeviationRrEvento = $filter('number')(eventStandardDeviationRr, 2);
		var standardDeviationHeartRateEvento = $filter('number')(eventStandardDeviationHeartRate, 2);
		var pkeEvento = $filter('number')(eventPke, 2);
		var numHighAccelerationsEvento = $filter('number')(eventNumHighAccelerations, 2);
		var numHighDecelerationsEvento = $filter('number')(eventNumHighDecelerations, 2);
		var averageAccelerationEvento = $filter('number')(eventAverageAcceleration, 2);
		var averageDecelerationEvento = $filter('number')(eventAverageDeceleration, 2);
		
		var datosEvento = L.DomUtil.create('datosEvento');

		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +
								'<br/><b>Date:</b> '+dateEvento+
								'<br/><b>Time:</b> '+hourEvento+
								'<br/><b>Speed:</b>' +
								'<br/>Minimum: '+minSpeedEvento +'<br/>Maximum: '+maxSpeedEvento +
								'<br/>Median: '+medianSpeedEvento +'<br/>Average: '+averageSpeedEvento +
								'<br/>Std. Dev.:'+standardDeviationSpeedEvento + 
								'<br/><b>Acceleration:</b>'+
								'<br/>Average acceleration:'+averageAccelerationEvento +
								'<br/>Average deceleration:'+averageDecelerationEvento +
								'<br/>High accelerations:'+numHighAccelerationsEvento +
								'<br/>High decelerations:'+numHighDecelerationsEvento +
								'<br/><b>Heart rate:</b>'+
								'<br/>Average:'+averageHearRateEvento +
								'<br/>Std. Dev.:'+standardDeviationHeartRateEvento;
	
		return datosEvento;
	}
	
	function pintarPuntos(events) {
		var mystyles = {
				color: 'red',
				fillOpacity: 0.5
		};
		
		markers.clearLayers();
		angular.forEach(events, function(value, key) {
			var info = infoPopup(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.tipo, value.value);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
		});		
	}

	function pintarLineas(events) {
		markers.clearLayers();
		angular.forEach(vm.events, function(value, key) {			
			var info = infoPopup(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.tipo, value.value);
			
			//Almaceno array de puntos 
			var myLines = value.roadSection;
			var myStyle = {
					"color": "#ff7800",
					"weight": 4,
					"opacity": 0.65
			};
			var myLayer = L.geoJson().addTo(map);
			L.geoJson(myLines, {
				style: myStyle
			}).addTo(map).addTo(markers).bindPopup(info);
			
		});
	}

	function pintarPuntosVehicleLocation(events) {
		var mystyles = {
				color: 'red',
				fillOpacity: 0.5
		};
		
		markers.clearLayers();
		angular.forEach(events, function(value, key) {
			var info = infoPopupVehicleLocation(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.speed, value.accuracy);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
		});		
	}
	
	function pintarPuntosContextData(events) {
		var mystyles = {
				color: 'red',
				fillOpacity: 0.5
		};
		
		markers.clearLayers();
		angular.forEach(events, function(value, key) {
			var info = infoPopupContextData(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timeLog, value.detectedActivity, value.accuracy);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
		});		
	}
	
	function pintarPuntosHigh(events) {
		var mystyles = {
				color: 'red',
				fillOpacity: 0.5
		};
		
		markers.clearLayers();
		angular.forEach(events, function(value, key) {
			var info = infoPopupHigh(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.value, value.speed, value.accuracy);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
		});		
	}
	
	
	function pintarLineasDataSection(events) {
		markers.clearLayers();
		angular.forEach(vm.events, function(value, key) {			
			var info = infoPopupDataSection(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.accuracy, value.minSpeed , value.maxSpeed , value.medianSpeed , value.averageSpeed , value.averageRR , value.averageHeartRate , value.standardDeviationSpeed , value.standardDeviationRR , value.standardDeviationHeartRate , value.pke , value.numHighAccelerations , value.numHighDecelerations , value.averageAcceleration , value.averageDeceleration , value.rrSection);
			
			//Almaceno array de puntos 
			var myLines = value.roadSection;
			var myStyle = {
					"color": "#ff7800",
					"weight": 4,
					"opacity": 0.65
			};
			var myLayer = L.geoJson().addTo(map);
			L.geoJson(myLines, {
				style: myStyle
			}).addTo(map).addTo(markers).bindPopup(info);
			
		});
	}
	
	// Preparar las fechas para pasarlas como parametro a los controladores
	function prepararParametrosFechas(){
		var url = "";
		if(vm.startDate!==null){
			var _startDate = $filter('date')(vm.startDate, 'yyyy-MM-dd HH:mm:ss');
			url += "fechaIni="+ _startDate+"&";
		}
		if(vm.endDate!==null){
			var _endDate = $filter('date')(vm.endDate, 'yyyy-MM-dd HH:mm:ss');
			url += "fechaFin="+ _endDate+"&";
		}
		return url;
	}
	
	// Preparar la url que va a llamar al controlador. TODO falta buscar una manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
	function prepararUrl(esLng, esLat, wnLng, wnLat){		
		var url ="";
		if (typeof vm.usuarioSelected != 'undefined' && vm.usuarioSelected !== null)
			url+="idUsuario="+ vm.usuarioSelected.id+"&";		
		url+=prepararParametrosFechas();		
		url+="wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;	
		return url;
	}
	
	function pintarMapaVehicleLocations() {
	    
		var bounds = map.getBounds();				
		var esLng = bounds.getSouthEast().lng;
		var esLat = bounds.getSouthEast().lat;
		var wnLng = bounds.getNorthWest().lng;
		var wnLat = bounds.getNorthWest().lat;
		
		var url = url_vehicleLocations;
		url+=prepararUrl(esLng, esLat, wnLng, wnLat);
		
		$http.get(url).success(function(data) {
			vm.events = data.results;
			vm.totalResults = data.totalResults;
			vm.returnedResults = data.returnedResults;
			pintarPuntosVehicleLocation(vm.events);
			paginarEventos();
		
		});
	  }
	  
	  function pintarMapaDataSections() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var url = url_dataSections;
			url+=prepararUrl(esLng, esLat, wnLng, wnLat);
						
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			
			$http.get(url).success(function(data) {
				vm.events = data.results;	
				vm.totalResults = data.totalResults;
				vm.returnedResults = data.returnedResults;
				pintarLineasDataSection(vm.events);
				paginarEventos();			
			});
	  }
	  
	  
	  function pintarMapaMeasurements() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var url = url_measurements;
			url+='?tipo='+vm.eventTypeSelected+'&';
			url+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(url).success(function(data) {
				vm.events = data.results;	
				vm.totalResults = data.totalResults;
				vm.returnedResults = data.returnedResults;								
				pintarPuntos(vm.events);
				paginarEventos();
			});
	  }
	  
	  function pintarMapaContextData() {
		    
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var url = url_contextData;
			url+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			$http.get(url).success(function(data) {
				vm.events = data.results;	
				vm.totalResults = data.totalResults;
				vm.returnedResults = data.returnedResults;		
				pintarPuntosContextData(vm.events);
				paginarEventos();
			
			});
		  }
	  
	  function pintarMapaHigh() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var url = url_measurements;
			url+='?tipo='+vm.eventTypeSelected+'&';
			url+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(url).success(function(data) {
				vm.events = data.results;	
				vm.totalResults = data.totalResults;
				vm.returnedResults = data.returnedResults;											
				pintarPuntosHigh(vm.events);
				paginarEventos();
			});
	  }
	  
	 function onTimeSetStart() {
		    vm.showCalendarStart = !vm.showCalendarStart;
	 }
	 
	 function onTimeSetEnd() {
		    vm.showCalendarEnd = !vm.showCalendarEnd;
	 }
		  
	// Inicialmente sé que voy a pintar los vehicleLocation (la opción por defecto en el select)
	  vm.aplicarFiltros();
	  
	  function paginarEventos() {		  
		  vm.currentPage = 1;
		  vm.pageSize = 10;
	  }
	 
	}
})();