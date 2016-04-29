(function() {
	'use strict';

	angular.module('app').controller('GPSLocationController', GPSLocationController);

	GPSLocationController.$inject = ['$scope', 'usuarios', 'eventoProcesado', 'eventsToday', 'statistics', 
	                               '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope', '$state',
	                               'DTOptionsBuilder', 'DTColumnBuilder', '$translate', 'gpsLocationService', '$q'];

	function GPSLocationController($scope, usuarios, eventoProcesado, eventsToday, statistics, 
			$http, $timeout, $log, $filter, eventsService, $rootScope, $state, DTOptionsBuilder, DTColumnBuilder,
			$translate, gpsLocationService, $q) {

		var vm = this;
		vm.pintarMapa = pintarMapa;
		vm.pintarPuntos = pintarPuntos;

		vm.aplicarFiltros = aplicarFiltros;
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
		vm.totalUL = statistics.totalUserLocations;
		vm.totalUA = statistics.totalUserActivities;

		vm.mostrarMapa = mostrarMapa;
		vm.mostrarTabla = mostrarTabla;
		vm.showMap = true;
		vm.showTab = false;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		vm.activeInput = $translate.instant('gpsLocation.mapa');
		vm.arrancar = arrancar;
		vm.parar = parar;

		vm.datosPromise = datosPromise;
		vm.cargarListadoTabla = cargarListadoTabla;
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);		
		}

		function getStateActualizadoComplete(response) {				
			vm.active = response.data;		
		}
		
		vm.tabla = undefined;

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


		//Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};

		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('userId').withTitle($translate.instant('gpsLocation.userId')),
		                   DTColumnBuilder.newColumn('time').withTitle($translate.instant('gpsLocation.time')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('altitude').withTitle($translate.instant('gpsLocation.altitude')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('speed').withTitle($translate.instant('gpsLocation.speed')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('bearing').withTitle($translate.instant('gpsLocation.bearing')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('gpsLocation.accuracy')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('provider').withTitle($translate.instant('gpsLocation.provider'))
		                   ];

		
		
		function cargarListadoTabla(){
			vm.tabla = "./partials/gpsLocation/tabla.html";
		}

		function mostrarMapa() {	
			vm.showMap = true;
			vm.showTab = false;
			vm.activeInput = $translate.instant('gpsLocation.mapa');

			//Para evitar que se carguen las tablas de la parte Table
			vm.tabla = undefined;
		}

		function mostrarTabla() {	
			vm.showMap = false;
			vm.showTab = true;
			vm.activeInput = $translate.instant('gpsLocation.tabla');

			vm.cargarListadoTabla();
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


		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.events);

			return dfd.promise;
		}

		
		function aplicarFiltros() {
			vm.pintarMapa();
		}

		function infoPopup(userId, timestamp, eventAltitude, eventSpeed, eventBearing, eventAccuracy, eventProvider) {
			var date = new Date(timestamp);
			var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
			var hourEvento = $filter('date')(date, 'HH:mm:ss');
			
			var altitudeEvento = "";
			if (eventAltitude !== null){
				altitudeEvento = $filter('number')(eventAltitude, 2);	
			}
			
			var speedEvento = "";
			if (eventSpeed!==null){
				speedEvento = $filter('number')(eventSpeed, 2);	
			}
			
			var bearingEvento = "";
			if (eventBearing !== null){
				bearingEvento = $filter('number')(eventBearing, 2);	
			}
			
			var accuracyEvento = "";
			if (eventAccuracy !== null){
				accuracyEvento = $filter('number')(eventAccuracy, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('gpsLocation.userId') + ':</b> ' + userId + 
									'<br/><b>' + $translate.instant('gpsLocation.date') + ':</b> '+ dateEvento + 
									'<br/><b>' + $translate.instant('gpsLocation.time') + ':</b> '+ hourEvento +
									'<br/><b>' + $translate.instant('gpsLocation.altitude') + ':</b> '+altitudeEvento +
									'<br/><b>' + $translate.instant('gpsLocation.speed') + ':</b> '+speedEvento +  
									'<br/><b>' + $translate.instant('gpsLocation.bearing') + ':</b> '+bearingEvento + 
									'<br/><b>' + $translate.instant('gpsLocation.accuracy') + ':</b> '+accuracyEvento + 
									'<br/><b>' + $translate.instant('gpsLocation.provider') + ':</b> '+eventProvider;
			return datosEvento;
		}

	

		function pintarPuntos(events) {
			var mystyles = {
					color: 'red',
					fillOpacity: 0.5
			};

			markers.clearLayers();
			angular.forEach(events, function(value, key) {
				var info = infoPopup(value.userId.substring(0,10) + "...", value.time, value.altitude, value.speed, value.bearing, value.accuracy, value.provider);			
				//Convierto el punto que quiero pintar para tener su lat y log
				var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
				//Añado al mapa el punto
				markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
			});		
		}

		

		function pintarMapa() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;

			var url = url_gpslocation;

			gpsLocationService.recuperarDatosPeticion(url, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionMapaComplete(response) {
				vm.events = response.results;
				vm.totalResults = response.totalResults;
				vm.returnedResults = response.returnedResults;
				pintarPuntos(vm.events);

				vm.cargarListadoTabla();
			}
		}

		function onTimeSetStart() {
			vm.showCalendarStart = !vm.showCalendarStart;
		}

		function onTimeSetEnd() {
			vm.showCalendarEnd = !vm.showCalendarEnd;
		}

		// Inicialmente sé que voy a pintar los vehicleLocation (la opción por defecto en el select)
		vm.aplicarFiltros();

	}
})();