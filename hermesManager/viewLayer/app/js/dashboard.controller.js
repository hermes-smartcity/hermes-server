(function() {
	'use strict';

	angular.module('app').controller('DashboardController', DashboardController);

	DashboardController.$inject = ['$scope', 'eventsType', 'usuarios' ,'measurementsType', 
	                               'eventsToday', 'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF' ,
	                               '$http', '$timeout', '$log', '$filter', 'eventsService'];

	function DashboardController($scope, eventsType, usuarios, measurementsType,  
			eventsToday, eventoProcesado, totalL, totalDS, totalM, totalDF , $http, $timeout, $log, $filter, eventsService) {
		
	var vm = this;
	vm.pintarMapaVehicleLocations = pintarMapaVehicleLocations;
	vm.pintarMapaDataSections = pintarMapaDataSections;
	vm.pintarMapaMeasurements = pintarMapaMeasurements;
	vm.pintarPuntos = pintarPuntos;
	vm.pintarLineas = pintarLineas;
	vm.aplicarFiltros = aplicarFiltros;
	vm.eventsType = eventsType;
	vm.usuarios = usuarios;
	vm.measurementsType = measurementsType;
	vm.eventsToday = eventsToday;
	vm.eventoProcesado = eventoProcesado;
	vm.totalL = totalL;	
	vm.totalDS = totalDS;
	vm.totalM = totalM;
	vm.totalDF = totalDF;
	vm.mostrarMapa = mostrarMapa;
	vm.mostrarTabla = mostrarTabla;
	vm.showMap = true;
	vm.showTab = false;
	vm.onTimeSetStart = onTimeSetStart;
	vm.onTimeSetEnd = onTimeSetEnd;
	vm.showCalendarStart = false;
	vm.showCalendarEnd = false;
	
	eventsService.getStateActualizado().then(getStateActualizadoComplete);
	
	function getStateActualizadoComplete(response) {				
		vm.active = response.data;		
	}
	// Inicializamos el filtro de event type para que inicialmente liste vehicle Locations
	vm.eventTypeSelected = "VEHICLE_LOCATION";
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
	}
	
	function mostrarTabla() {	
		vm.showMap = false;
		vm.showTab = true;
	}
	
	function aplicarFiltros() {		
		var pos = vm.eventTypeSelected.indexOf('_'); 
		var value = vm.eventTypeSelected.substr(0, pos);
		value += vm.eventTypeSelected.substr(pos+1, vm.eventTypeSelected.length);
		value = angular.lowercase(value);

		if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION"))
			vm.pintarMapaVehicleLocations();
		else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION"))
			vm.pintarMapaDataSections();
		else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
			vm.pintarMapaMeasurements();
		} else console.log("No corresponde a ningún tipo --> En construcción");
		
	}
	
	function infoPopup(userId, timestamp, eventType, eventValue) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var datosEvento = L.DomUtil.create('datosEvento');

		datosEvento.innerHTML = '<b>UserId:</b> ' + userId +' <br/><b>Fecha:</b> '+dateEvento+'<br/><b>Hora:</b> '+hourEvento+'<br/><b>Tipo:</b> '+eventType+'<br/><b>Valor:</b> '+eventValue;
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
		
		var urlGet = "http://localhost:8080/eventManager/api/vehiclelocation/json/vehicleLocations?";		
		urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
		
		$http.get(urlGet).success(function(data) {
			vm.events = data;		
			pintarPuntos(vm.events);
			paginarEventos();
		
		});
	  }
	  
	  function pintarMapaDataSections() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "http://localhost:8080/eventManager/api/datasection/json/dataSections?";
			urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
						
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(urlGet).success(function(data) {
				vm.events = data;
				pintarLineas(vm.events);
				paginarEventos();			
			});
	  }
	  
	  
	  function pintarMapaMeasurements() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "http://localhost:8080/eventManager/api/measurement/json/measurements?tipo="+vm.eventTypeSelected+"&";
			urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(urlGet).success(function(data) {
				vm.events = data;									
				pintarPuntos(vm.events);
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