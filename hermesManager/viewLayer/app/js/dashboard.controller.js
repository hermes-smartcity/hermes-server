(function() {
	'use strict';

	angular.module('app').controller('DashboardController', DashboardController);

	DashboardController.$inject = ['$scope', 'usuarios', 'eventoProcesado', 'eventsToday', 'statistics', 
	                               '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope', '$state',
	                               'DTOptionsBuilder', 'DTColumnBuilder', '$translate', 'dashboardService', '$q'];

	function DashboardController($scope, usuarios, eventoProcesado, eventsToday, statistics, 
			$http, $timeout, $log, $filter, eventsService, $rootScope, $state, DTOptionsBuilder, DTColumnBuilder,
			$translate, dashboardService, $q) {
	
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
	
	vm.datosPromise = datosPromise;
	vm.cargarListadoTabla = cargarListadoTabla;
	vm.recargarTabla = recargarTabla;
	vm.cargarColumnasTabla = cargarColumnasTabla;	
	
	// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
	if($rootScope.hasRole('ROLE_ADMIN')){
		eventsService.getStateActualizado().then(getStateActualizadoComplete);		
	}
	
	function getStateActualizadoComplete(response) {				
		vm.active = response.data;		
	}
	
	//Inicializar options de la tabla
	vm.dtInstance = null;
	
	//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
	vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise);
		
	vm.dtInstanceCallback = function(_dtInstance){
        vm.dtInstance = _dtInstance;
    };
        
    vm.dtColumnsVL  = [
		                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('vehicleLocation.userId')),
		                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('vehicleLocation.time')).renderWith(function(data, type, full) {
		                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                }),
		                DTColumnBuilder.newColumn('speed').withTitle($translate.instant('vehicleLocation.speed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('vehicleLocation.accuracy'))
		            ];
	
    vm.dtColumnsM  = [
		                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('measurement.userId')),
		                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('measurement.time')).renderWith(function(data, type, full) {
		                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                }),
		                DTColumnBuilder.newColumn('value').withTitle($translate.instant('measurement.value')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('speed').withTitle($translate.instant('measurement.speed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('measurement.accuracy'))
		            ];
    
    vm.dtColumnsDS  = [
		                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('dataSection.userId')),
		                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('dataSection.date')).renderWith(function(data, type, full) {
		                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                }),
		                DTColumnBuilder.newColumn('minSpeed').withTitle($translate.instant('dataSection.minimimSpeed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('maxSpeed').withTitle($translate.instant('dataSection.maximumSpeed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('medianSpeed').withTitle($translate.instant('dataSection.medianSpeed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('averageSpeed').withTitle($translate.instant('dataSection.averageSpeed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('standardDeviationSpeed').withTitle($translate.instant('dataSection.stdDevSpeed')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('averageAcceleration').withTitle($translate.instant('dataSection.averageAcceleration')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('averageDeceleration').withTitle($translate.instant('dataSection.averageDeceleration')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('numHighAccelerations').withTitle($translate.instant('dataSection.highAcceleration')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('numHighDecelerations').withTitle($translate.instant('dataSection.highDecceleration')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('averageHeartRate').withTitle($translate.instant('dataSection.averageHeartRate')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                }),
		                DTColumnBuilder.newColumn('standardDeviationHeartRate').withTitle($translate.instant('dataSection.stdDevHeartRate')).renderWith(function(data, type, full) {
		                    return $filter('number')(data, 2);   
		                })
		            ];
    
    vm.dtColumnsCD  = [
		                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('contextData.userId')),
		                DTColumnBuilder.newColumn('timeLog').withTitle($translate.instant('contextData.time')).renderWith(function(data, type, full) {
		                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                }),
		                DTColumnBuilder.newColumn('detectedActivity').withTitle($translate.instant('contextData.detectedActivity')),
		                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('contextData.accuracy'))
		            ];
    
    
	// Inicializamos el filtro de event type para que inicialmente liste vehicle Locations
	vm.eventTypeSelected = "VEHICLE_LOCATION";
	vm.listadoCarga = undefined;
	
	//Inicializamos dtcolumsn segund el tipo seleccionado
	vm.cargarColumnasTabla();
	
	
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

	
	function cargarColumnasTabla(){
		//Para mostrar la tabla correspondiente
		if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
			vm.dtColumns  = [
				                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('vehicleLocation.userId')),
				                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('vehicleLocation.time')).renderWith(function(data, type, full) {
				                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                }),
				                DTColumnBuilder.newColumn('speed').withTitle($translate.instant('vehicleLocation.speed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('vehicleLocation.accuracy'))
				            ];
			
		}else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
			
			vm.dtColumns  = [
				                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('dataSection.userId')),
				                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('dataSection.date')).renderWith(function(data, type, full) {
				                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                }),
				                DTColumnBuilder.newColumn('minSpeed').withTitle($translate.instant('dataSection.minimimSpeed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('maxSpeed').withTitle($translate.instant('dataSection.maximumSpeed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('medianSpeed').withTitle($translate.instant('dataSection.medianSpeed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('averageSpeed').withTitle($translate.instant('dataSection.averageSpeed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('standardDeviationSpeed').withTitle($translate.instant('dataSection.stdDevSpeed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('averageAcceleration').withTitle($translate.instant('dataSection.averageAcceleration')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('averageDeceleration').withTitle($translate.instant('dataSection.averageDeceleration')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('numHighAccelerations').withTitle($translate.instant('dataSection.highAcceleration')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('numHighDecelerations').withTitle($translate.instant('dataSection.highDecceleration')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('averageHeartRate').withTitle($translate.instant('dataSection.averageHeartRate')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('standardDeviationHeartRate').withTitle($translate.instant('dataSection.stdDevHeartRate')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                })
				            ];
			
		}else if(angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
			
			vm.dtColumns  = [
				                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('contextData.userId')),
				                DTColumnBuilder.newColumn('timeLog').withTitle($translate.instant('contextData.time')).renderWith(function(data, type, full) {
				                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                }),
				                DTColumnBuilder.newColumn('detectedActivity').withTitle($translate.instant('contextData.detectedActivity')),
				                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('contextData.accuracy'))
				            ];
			
		
		}else if(angular.equals(vm.eventTypeSelected, "HIGH_SPEED") || 
				angular.equals(vm.eventTypeSelected, "HIGH_ACCELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_DECELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_HEART_RATE")){
			
			vm.dtColumns  = [
				                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('measurement.userId')),
				                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('measurement.time')).renderWith(function(data, type, full) {
				                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                }),
				                DTColumnBuilder.newColumn('value').withTitle($translate.instant('measurement.value')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('speed').withTitle($translate.instant('measurement.speed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('measurement.accuracy'))
				            ];
			
		}else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
			
			vm.dtColumns  = [
				                DTColumnBuilder.newColumn('usuarioMovil.id').withTitle($translate.instant('measurement.userId')),
				                DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('measurement.time')).renderWith(function(data, type, full) {
				                    return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                }),
				                DTColumnBuilder.newColumn('value').withTitle($translate.instant('measurement.value')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('speed').withTitle($translate.instant('measurement.speed')).renderWith(function(data, type, full) {
				                    return $filter('number')(data, 2);   
				                }),
				                DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('measurement.accuracy'))
				            ];
			
		} 
	}
	
	function cargarListadoTabla(){
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
	
    
	function aplicarFiltros() {		
		var pos = vm.eventTypeSelected.indexOf('_'); 
		var value = vm.eventTypeSelected.substr(0, pos);
		value += vm.eventTypeSelected.substr(pos+1, vm.eventTypeSelected.length);
		value = angular.lowercase(value);

		if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
			vm.pintarMapaVehicleLocations();
		}else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
			vm.pintarMapaDataSections();
		}else if(angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
			vm.pintarMapaContextData();
		}else if(angular.equals(vm.eventTypeSelected, "HIGH_SPEED") || 
				angular.equals(vm.eventTypeSelected, "HIGH_ACCELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_DECELERATION") ||
				angular.equals(vm.eventTypeSelected, "HIGH_HEART_RATE")){
			vm.pintarMapaHigh();
		}else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
			vm.pintarMapaMeasurements();
		} else{
			console.log("No corresponde a ningún tipo --> En construcción");
		}
		
	}
	
	function recargarTabla(){
		
		if (vm.dtInstance !== null){
			if (vm.showTab === true){
				
				//Reasignamos el valor de dtcolumns
				//vm.cargarColumnasTabla();
				
				var nuevosDatos = datosPromise;
				
				//Asignamos el nuevo dato a donde corresponda
				if(angular.equals(vm.eventTypeSelected, "VEHICLE_LOCATION")){
					vm.dtInstance.changeData(nuevosDatos);
				}else if(angular.equals(vm.eventTypeSelected, "DATA_SECTION")){
					vm.dtInstance.changeData(nuevosDatos);
				}else if(angular.equals(vm.eventTypeSelected, "CONTEXT_DATA")){
					vm.dtInstance.changeData(nuevosDatos);
				}else if(angular.equals(vm.eventTypeSelected, "HIGH_SPEED") || 
						angular.equals(vm.eventTypeSelected, "HIGH_ACCELERATION") ||
						angular.equals(vm.eventTypeSelected, "HIGH_DECELERATION") ||
						angular.equals(vm.eventTypeSelected, "HIGH_HEART_RATE")){
					vm.dtInstance.changeData(nuevosDatos);	
				}else if(vm.measurementsType.indexOf(vm.eventTypeSelected) > -1){
					vm.dtInstance.changeData(nuevosDatos);	
				}
				
			}
		
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
			var info = infoPopupVehicleLocation(value.userId.substring(0,10) + "...", value.timestamp, value.speed, value.accuracy);			
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
	
	function datosPromise(){
		var dfd = $q.defer();		
		dfd.resolve(vm.events);
	
		return dfd.promise;
	}
	
	function pintarMapaVehicleLocations() {
	    
		var bounds = map.getBounds();				
		var esLng = bounds.getSouthEast().lng;
		var esLat = bounds.getSouthEast().lat;
		var wnLng = bounds.getNorthWest().lng;
		var wnLat = bounds.getNorthWest().lat;
		
		dashboardService.recuperarDatosPeticion(url_vehicleLocations, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaVehicleLocationsComplete);
		// En cuanto tenga los eventos los pinto
		function getPeticionMapaVehicleLocationsComplete(response) {
			vm.events = response.results;
			vm.totalResults = response.totalResults;
			vm.returnedResults = response.returnedResults;
			pintarPuntosVehicleLocation(vm.events);
			
			vm.recargarTabla();
		}
	  }
	  
	  function pintarMapaDataSections() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			dashboardService.recuperarDatosPeticion(url_dataSections, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaDataSectionsComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionMapaDataSectionsComplete(response) {
				vm.events = response.results;
				vm.totalResults = response.totalResults;
				vm.returnedResults = response.returnedResults;
				pintarLineasDataSection(vm.events);
				
				vm.recargarTabla();
			}
	  }
	  
	  
	  function pintarMapaMeasurements() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
			
			var url = url_measurements;
			url+='?tipo='+vm.eventTypeSelected+'&';
			
			dashboardService.recuperarDatosPeticion(url, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaMeasurementsComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionMapaMeasurementsComplete(response) {
				vm.events = response.results;
				vm.totalResults = response.totalResults;
				vm.returnedResults = response.returnedResults;
				pintarPuntos(vm.events);
				
				vm.recargarTabla();
			}
			
	  }
	  
	  function pintarMapaContextData() {
		    
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
		
			dashboardService.recuperarDatosPeticion(url_contextData, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaContextDataComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionMapaContextDataComplete(response) {
				vm.events = response.results;
				vm.totalResults = response.totalResults;
				vm.returnedResults = response.returnedResults;
				pintarPuntosContextData(vm.events);
				
				vm.recargarTabla();
			}
		  }
	  
	  function pintarMapaHigh() {
			var bounds = map.getBounds();				
			var esLng = bounds.getSouthEast().lng;
			var esLat = bounds.getSouthEast().lat;
			var wnLng = bounds.getNorthWest().lng;
			var wnLat = bounds.getNorthWest().lat;
					
			var url = url_measurements;
			url+='?tipo='+vm.eventTypeSelected+'&';
			
			dashboardService.recuperarDatosPeticion(url, esLng, esLat, wnLng, wnLat, vm.startDate, vm.endDate, vm.usuarioSelected).then(getPeticionMapaHighComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionMapaHighComplete(response) {
				vm.events = response.results;
				vm.totalResults = response.totalResults;
				vm.returnedResults = response.returnedResults;
				pintarPuntosHigh(vm.events);
				
				vm.recargarTabla();
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