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
	eventsService.getStateActualizado().then(getStateActualizadoComplete);
	
	function getStateActualizadoComplete(response) {				
		vm.active = response.data;		
	}
	// Inicializamos el filtro de event type para que inicialmente liste vehicle Locations
	vm.eventTypeSelected = "VEHICLE_LOCATION";
	$scope.startDate = new Date();
	// Inicializamos la fecha de inicio a la de ayer 
	$scope.startDate.setDate($scope.startDate.getDate()-1);
	$scope.endDate = new Date();
	
	var map = L.map( 'map', {
		  minZoom: 2,
		  zoom: 2
		});
	var locations = L.layerGroup([]);
	
	var markers = new L.MarkerClusterGroup({
	  spiderfyDistanceMultiplier: 0.5,
	  disableClusteringAtZoom: 12,
	  maxClusterRadius: 20
	});

	map.addLayer(locations);
	
	map.fitBounds([
	               [ -180, -90],
	               [180,  90]
	           ]);
	
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
	
	map.on('dragend', aplicarFiltros);
	map.on('zoomend', aplicarFiltros);
//	map.on('zoomend', actualizarMaxClusterRadius);
	
//	actualizarMaxClusterRadius();
	
//	function actualizarMaxClusterRadius() {
//		var zoom = map.getZoom();
//		var radius = 15;
//		var distance = 0.25;
//		if(zoom < 12){
//			radius = 150 - zoom * 10;
//			distance = 1.5;
//		}
//		
//		markers = new L.MarkerClusterGroup({
//		      spiderfyDistanceMultiplier: distance,
//		      maxClusterRadius: radius,
//		});
//		
//		 var current1 = markers.getLayers(); //get current layers in markers
//	        map.removeLayer(markers); // remove markers from map
//	        markers.clearLayers(); // clear any layers in clusters just in case
//	        current1.forEach(function(item) {  //loop through the current layers and add them to clusters
//	        	markers.addLayer(item);
//	        });
//	        map.addLayer(markers);
//	}
	
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
		
	};
	
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
			var info = infoPopup(value.usuario.sourceId.substring(0,10) + "...", value.timestamp, value.tipo, value.value);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
//			var circle = L.circle(latlng, 5, mystyles).addTo(locations).bindPopup(info);
		});
		map.addLayer(markers);
		
	};

	function pintarLineas(events) {
		markers.clearLayers();
		angular.forEach(vm.events, function(value, key) {			
			var info = infoPopup(value.usuario.sourceId.substring(0,10) + "...", value.timestamp, value.tipo, value.value);
			
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
			}).addTo(map).addTo(locations).bindPopup(info);
			
		});
	};

	// Preparar las fechas para pasarlas como parametro a los controladores
	function prepararParametrosFechas(){
		var url = "";
		if($scope.startDate!=null){
			var _startDate = $filter('date')($scope.startDate, 'yyyy-MM-dd HH:mm:ss');
			url += "fechaIni="+ _startDate+"&";
		}
		if($scope.endDate!=null){
			var _endDate = $filter('date')($scope.endDate, 'yyyy-MM-dd HH:mm:ss');
			url += "fechaFin="+ _endDate+"&";
		}
		return url;
	}
	
	// Preparar la url que va a llamar al controlador. TODO falta buscar una manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
	function prepararUrl(esLng, esLat, wnLng, wnLat){		
		var url ="";
		if(vm.usuarioSelected!=null)
			url+="idUsuario="+ vm.usuarioSelected.id+"&";		
		url+=prepararParametrosFechas();		
		url+="wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;	
		return url;
	}
	
	function pintarMapaVehicleLocations() {
	    
		locations.clearLayers();
		var bounds = map.getBounds();				
		var esLng = bounds.getSouthEast().lng;
		var esLat = bounds.getSouthEast().lat;
		var wnLng = bounds.getNorthWest().lng;
		var wnLat = bounds.getNorthWest().lat;
		
		var urlGet = "api/vehiclelocation/json/vehicleLocations?";		
		urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
		
		$http.get(urlGet).success(function(data) {
			vm.events = data;		
			pintarPuntos(vm.events);
			paginarEventos();
		
		});
	  };
	  
	  function pintarMapaDataSections() {
		  	locations.clearLayers();
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "api/datasection/json/dataSections?";
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
	  };
	  
	  
	  function pintarMapaMeasurements() {
		  	locations.clearLayers();
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "api/measurement/json/measurements?tipo="+vm.eventTypeSelected+"&";
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
	  };
		  
	// Inicialmente sé que voy a pintar los vehicleLocation (la opción por defecto en el select)
	  vm.aplicarFiltros();
	  
	  function paginarEventos() {
		  
		  vm.currentPage = 1;

//		  vm.setPage = function (pageNo) {
//			  vm.currentPage = pageNo;
//		  };
		  vm.pageSize = 10;

	  }
	 
	}
})();