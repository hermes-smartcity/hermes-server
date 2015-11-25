var app = angular.module('app', ['ui.bootstrap.datetimepicker']);

app.controller('FiltrosController', [ '$scope', '$http', '$timeout', '$log', '$filter',

function($scope, $http, $timeout, $log, $filter) {	
	
	var vm = this;
	vm.pintarMapaVehicleLocations = pintarMapaVehicleLocations;
	vm.pintarMapaDataSections = pintarMapaDataSections;
	vm.pintarMapaMeasurements = pintarMapaMeasurements;
	vm.pintarPuntos = pintarPuntos;
	vm.pintarLineas = pintarLineas;
	vm.aplicarFiltros = aplicarFiltros;
	
	var urlGetEvensType = "../dashboard/json/eventsType";
	$http.get(urlGetEvensType).success(function(data) {
		$scope.eventsType = data;

	});
	
	var urlGetUsuarios = "../dashboard/json/usuarios";
	$http.get(urlGetUsuarios).success(function(data) {
		$scope.usuarios = data;

	});
	
	// Inicializamos el filtro de event type para que inicialmente liste vehicle Locations
	$scope.eventTypeSelected = "VEHICLE_LOCATION";
	$scope.startDate = new Date();
	// Inicializamos la fecha de inicio a la de ayer 
	$scope.startDate.setDate($scope.startDate.getDate()-1);
	$scope.endDate = new Date();
    
	var map = L.map('map');
	var locations = L.layerGroup([]);
	map.addLayer(locations);
	
	map.fitBounds([
	               [ -180, -90],
	               [180,  90]
	           ]);
	
	L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
	
	map.on('dragend', aplicarFiltros);
	map.on('zoomend', aplicarFiltros);
	
	function aplicarFiltros() {
		var pos = $scope.eventTypeSelected.indexOf('_'); 
		var value = $scope.eventTypeSelected.substr(0, pos);
		value += $scope.eventTypeSelected.substr(pos+1, $scope.eventTypeSelected.length);
		value = angular.lowercase(value);

		if(angular.equals($scope.eventTypeSelected, "VEHICLE_LOCATION"))
			vm.pintarMapaVehicleLocations();
		else if(angular.equals($scope.eventTypeSelected, "DATA_SECTION"))
			vm.pintarMapaDataSections();
		else vm.pintarMapaMeasurements();
		
		
	};
	
	function infoPopup(eventId, timestamp) {
		var date = new Date(timestamp);
		var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
		var hourEvento = $filter('date')(date, 'HH:mm:ss');
		var datosEvento = L.DomUtil.create('datosEvento');

		datosEvento.innerHTML = '<b>EventId:</b> ' + eventId+' <b>Fecha:</b> '+dateEvento+'<br/><b>Hora:</b> '+hourEvento;
		return datosEvento;
	}
	
	function pintarPuntos(events) {
		var mystyles = {
				color: 'red',
				fillOpacity: 0.1
		};
		angular.forEach(events, function(value, key) {
			
			var info = infoPopup(value.eventId, value.timestamp);			
			//Convierto el punto que quiero pintar para tener su lat y log
			var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
			//Añado al mapa el punto
			var circle = L.circle(latlng, 5, mystyles).addTo(locations).bindPopup(info);
		});
	};

	function pintarLineas(events) {
		angular.forEach($scope.events, function(value, key) {
			
			var info = infoPopup(value.eventId, value.timestamp);
			
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
		if($scope.usuarioSelected!=null)
			url+="idUsuario="+ $scope.usuarioSelected.id+"&";		
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
		
		var urlGet = "../vehiclelocation/json/vehicleLocations?";		
		urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
		
		$http.get(urlGet).success(function(data) {
			$scope.events = data;		
			pintarPuntos($scope.events);
		
		});
	  };
	  
	  function pintarMapaDataSections() {
		  locations.clearLayers();
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "../datasection/json/dataSections?";
			urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(urlGet).success(function(data) {
				$scope.events = data;
				pintarLineas($scope.events);
			
			});
	  };
	  
	  
	  function pintarMapaMeasurements() {
		  locations.clearLayers();
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var urlGet = "../measurement/json/measurements?tipo="+$scope.eventTypeSelected+"&";
			urlGet+=prepararUrl(esLng, esLat, wnLng, wnLat);
			
			var mystyles = {
					color: 'red',
					fillOpacity: 0.1
			};
			
			$http.get(urlGet).success(function(data) {
				$scope.events = data;									
				pintarPuntos($scope.events);
			});
	  };
		  
	// Inicialmente sé que voy a pintar los vehicleLocation (la opción por defecto en el select)
	  vm.aplicarFiltros();

	} ]);