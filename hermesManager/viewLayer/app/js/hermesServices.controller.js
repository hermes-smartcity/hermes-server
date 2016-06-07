(function() {
	'use strict';

	angular.module('app').controller('HermesServicesController', HermesServicesController);

	HermesServicesController.$inject = ['$scope', '$filter', '$http', '$translate',
	                                '$state', '$rootScope', 'eventsService', 'services',
	                                'types', 'dataSections', 'eventsToday',
	                                'eventoProcesado', 'statistics', 'hermesServicesService',
	                                'DTOptionsBuilder', 'DTColumnBuilder', '$q', 'SweetAlert'];

	function HermesServicesController($scope, $filter, $http, $translate, $state,
			$rootScope, eventsService, services, types, dataSections, eventsToday,
			eventoProcesado, statistics, hermesServicesService, DTOptionsBuilder, DTColumnBuilder,
			$q, SweetAlert) {

		var vm = this;

		vm.aplicarFiltros = aplicarFiltros;

		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.serviceSelected = undefined;
		vm.methodSelected = undefined;

		vm.services = services;
		vm.types = types;
		vm.measurementTypes = $rootScope.measurementsType;
		vm.dataSections = dataSections;
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
		vm.totalUDI = statistics.totalUserDistances;
		vm.totalUST = statistics.totalUserSteps;
		vm.totalUCE = statistics.totalUserCaloriesExpended;
		vm.totalUHR = statistics.totalUserHeartRates;
		vm.totalUSL = statistics.totalUserSleep;

		vm.arrancar = arrancar;
		vm.parar = parar;

		vm.previousLong = undefined;
		vm.previousLat = undefined;
		vm.currentLong = undefined;
		vm.currentLat = undefined;

		vm.fromLng = undefined;
		vm.fromLat = undefined;
		vm.toLng = undefined;
		vm.toLat = undefined;

		vm.nwLng = undefined;
		vm.nwLat = undefined;
		vm.seLng = undefined;
		vm.seLat = undefined;

		vm.changeMethod = changeMethod;
		vm.change = change;
		vm.changeToolbar = changeToolbar;
		vm.changeFilter = changeFilter;
		vm.limpiarParametrosMapa = limpiarParametrosMapa;

		vm.filtroConcreto = undefined;
		vm.resultadoConcreto = undefined;

		//Filtros de la seccion aggregateMeasurement
		vm.typeSelected = undefined;
		vm.pointLng = undefined;
		vm.pointLat = undefined;
		vm.daySelected = undefined;
		vm.timeSelected = undefined;
		vm.dataSectionSelected = undefined;

		//Filtros para las fechas
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;

		//Filtros para el tipo de measurement
		vm.measurementTypeSelected = undefined;

		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la de ayer
		vm.startDate.setDate(vm.startDate.getDate()-1);
		vm.endDate = new Date();


		vm.mostrarMapa = mostrarMapa;
		vm.mostrarTabla = mostrarTabla;
		vm.showMap = true;
		vm.showTab = false;
		vm.showResult = true;
		vm.showSelectorMapTab = false;
		vm.activeInput = $translate.instant('hermesServices.mapa');
		vm.tabla = undefined;

		vm.pintarLineasDataSection = pintarLineasDataSection;
		vm.pintarLineasCommputeRoute = pintarLineasCommputeRoute;
		vm.pintarPuntosSimulateRoute = pintarPuntosSimulateRoute;
		vm.pintarPuntosVehicleLocation = pintarPuntosVehicleLocation;
		vm.pintarPuntosMeasurement = pintarPuntosMeasurement;
		vm.pintarLineasDataSection = pintarLineasDataSection;
		vm.pintarPuntosContextData = pintarPuntosContextData;
		vm.pintarPuntosUserLocation = pintarPuntosUserLocation;

		vm.datosPromise = datosPromise;
		vm.cargarListadoTabla = cargarListadoTabla;

		var hours = [];
	    for (var i = 0; i <= 23; i++) {
	    	hours.push(i);
	    }
	    vm.hours = hours;

	    //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};

		vm.dtColumnsCR  = [
		                   DTColumnBuilder.newColumn('linkId').withTitle($translate.instant('hermesServices.linkId')),
		                   DTColumnBuilder.newColumn('maxSpeed').withTitle($translate.instant('hermesServices.maxSpeed')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);
		                   }),
		                   DTColumnBuilder.newColumn('linkName').withTitle($translate.instant('hermesServices.linkName')),
		                   DTColumnBuilder.newColumn('linkType').withTitle($translate.instant('hermesServices.linkType')),
		                   DTColumnBuilder.newColumn('length').withTitle($translate.instant('hermesServices.length')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);
		                   }),
		                   DTColumnBuilder.newColumn('cost').withTitle($translate.instant('hermesServices.cost')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);
		                   })
		                   ];
		
		vm.dtColumnsVL  = [
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('vehicleLocation.userId')),
		                   DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('vehicleLocation.time')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('speed').withTitle($translate.instant('vehicleLocation.speed')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);
		                   }),
		                   DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('vehicleLocation.accuracy'))
		                   ];

		vm.dtColumnsM  = [
		                  DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('measurement.userId')),
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
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('dataSection.userId')),
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
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('contextData.userId')),
		                   DTColumnBuilder.newColumn('timeLog').withTitle($translate.instant('contextData.time')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('detectedActivity').withTitle($translate.instant('contextData.detectedActivity')),
		                   DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('contextData.accuracy'))
		                   ];

		vm.dtColumnsUL  = [
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userLocation.userId')),
		                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userLocation.timeStart')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userLocation.timeEnd')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('accuracy').withTitle($translate.instant('vehicleLocation.accuracy'))
		                   ];

		vm.dtColumnsUA  = [
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userActivity.userId')),
		                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userActivity.timeStart')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userActivity.timeEnd')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('userActivity.name'))
		                   ];

		vm.dtColumnsDF  = [
							DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('driverFeatures.userId')),
							DTColumnBuilder.newColumn('awakeFor').withTitle($translate.instant('driverFeatures.awakefor')),
							DTColumnBuilder.newColumn('inBed').withTitle($translate.instant('driverFeatures.inbed')),
							DTColumnBuilder.newColumn('workingTime').withTitle($translate.instant('driverFeatures.workingtime')),
							DTColumnBuilder.newColumn('lightSleep').withTitle($translate.instant('driverFeatures.lightsleep')),
							DTColumnBuilder.newColumn('deepSleep').withTitle($translate.instant('driverFeatures.deepsleep')),
							DTColumnBuilder.newColumn('previousStress').withTitle($translate.instant('driverFeatures.previousstress')),
							DTColumnBuilder.newColumn('timeStamp').withTitle($translate.instant('driverFeatures.timestamp')).renderWith(function(data, type, full) {
								return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				            })
		                   ];

		vm.dtColumnsHRD  = [
		                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('heartRateData.userId')),
		                   DTColumnBuilder.newColumn('eventId').withTitle($translate.instant('heartRateData.eventid')),
		                   DTColumnBuilder.newColumn('timeLog').withTitle($translate.instant('heartRateData.timelog')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('heartRate').withTitle($translate.instant('heartRateData.heartrate'))
		                   ];

		vm.dtColumnsSLD  = [
			               DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('sleepData.userId')),
			               DTColumnBuilder.newColumn('eventId').withTitle($translate.instant('sleepData.eventid')),
			               DTColumnBuilder.newColumn('awakenings').withTitle($translate.instant('sleepData.awakenings')),
			               DTColumnBuilder.newColumn('minutesAsleep').withTitle($translate.instant('sleepData.minutesasleep')),
			               DTColumnBuilder.newColumn('minutesInBed').withTitle($translate.instant('sleepData.minutesinbed')),
			               DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('sleepData.starttime')).renderWith(function(data, type, full) {
			            	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
			               }),
			               DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('sleepData.endtime')).renderWith(function(data, type, full) {
			            	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
			               })
			               ];

		vm.dtColumnsSTD  = [
				           DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('stepData.userId')),
				           DTColumnBuilder.newColumn('eventId').withTitle($translate.instant('stepData.eventid')),
				           DTColumnBuilder.newColumn('timeLog').withTitle($translate.instant('stepData.timelog')).renderWith(function(data, type, full) {
				          	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				           }),
				           DTColumnBuilder.newColumn('steps').withTitle($translate.instant('stepData.steps'))
				           ];

		vm.dtColumnsUDI  = [
			                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userDistances.userId')),
			                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userDistances.timeStart')).renderWith(function(data, type, full) {
			                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
			                   }),
			                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userDistances.timeEnd')).renderWith(function(data, type, full) {
			                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
			                   }),
			                   DTColumnBuilder.newColumn('distance').withTitle($translate.instant('userDistances.distance')).renderWith(function(data, type, full) {
			                	   return $filter('number')(data, 2);   
			                   })
			                   ];
			
			vm.dtColumnsUST  = [
				                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userSteps.userId')),
				                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userSteps.timeStart')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userSteps.timeEnd')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('steps').withTitle($translate.instant('userSteps.steps'))
				                   ];
			
			vm.dtColumnsUCE  = [
				                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userCaloriesExpended.userId')),
				                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userCaloriesExpended.timeStart')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userCaloriesExpended.timeEnd')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('calories').withTitle($translate.instant('userCaloriesExpended.calories')).renderWith(function(data, type, full) {
				                	   return $filter('number')(data, 2);   
				                   })
				                   ];
			
			vm.dtColumnsUHR  = [
				                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userHeartRates.userId')),
				                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userHeartRates.timeStart')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userHeartRates.timeEnd')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('bpm').withTitle($translate.instant('userHeartRates.bpm')).renderWith(function(data, type, full) {
				                	   return $filter('number')(data, 2);   
				                   })
				                   ];		
			
			vm.dtColumnsUSL  = [
				                   DTColumnBuilder.newColumn('usuarioMovil.sourceId').withTitle($translate.instant('userSleep.userId')),
				                   DTColumnBuilder.newColumn('startTime').withTitle($translate.instant('userSleep.timeStart')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('endTime').withTitle($translate.instant('userSleep.timeEnd')).renderWith(function(data, type, full) {
				                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
				                   }),
				                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('userSleep.name'))
				                   ];
			
		vm.errorPoint = false;
		vm.mensajeErrorPoint = undefined;
		
		function cargarListadoTabla(){
			
			switch (vm.methodSelected) {
		    case "GET_INFORMATION_LINK":
		    	vm.tabla = undefined;
		    	break;
		    case "AGGREGATE_MEASUREMENT":
		    	vm.tabla = undefined;
		    	break;
		    case "COMPUTE_ROUTE":
		    	vm.tabla = "./partials/hermesServices/tablaComputeRoute.html";
		    	break;
		    case "SIMULATE_ROUTE":
			  	vm.tabla = undefined;
			  	break;	
		    case "GET_VEHICLE_LOCATIONS":
		    	vm.tabla = "./partials/hermesServices/tablaGetVehicleLocations.html";
		    	break;
		    case "GET_MEASUREMENTS":
		    	vm.tabla = "./partials/hermesServices/tablaGetMeasurement.html";
		    	break;
		    case "GET_DATA_SECTIONS":
		    	vm.tabla = "./partials/hermesServices/tablaGetDataSections.html";
		    	break;
		    case "GET_CONTEXT_DATA":
		    	vm.tabla = "./partials/hermesServices/tablaGetContextData.html";
		    	break;
		    case "GET_DRIVER_FEATURE":
		    	vm.tabla = "./partials/hermesServices/tablaGetDriverFeatures.html";
		    	break;
		    case "GET_HEART_RATE_DATA":
		    	vm.tabla = "./partials/hermesServices/tablaGetHeartRateData.html";
		    	break;
		    case "GET_STEPS_DATA":
		    	vm.tabla = "./partials/hermesServices/tablaGetStepsData.html";
		    	break;
		    case "GET_SLEEP_DATA":
		    	vm.tabla = "./partials/hermesServices/tablaGetSleepData.html";
		    	break;
		    case "GET_CONTEXT_DATA":
		    	vm.tabla = "./partials/hermesServices/tablaGetContextData.html";
		    	break;
		    case "GET_USER_LOCATIONS":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserLocations.html";
		    	break;
		    case "GET_USER_ACTIVITIES":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserActivities.html";
		    	break;
		    case "GET_USER_DISTANCES":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserDistances.html";
		    	break;
		    case "GET_USER_STEPS":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserSteps.html";
		    	break;
		    case "GET_USER_CALORIES_EXPENDED":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserCaloriesExpended.html";
		    	break;
		    case "GET_USER_HEART_RATES":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserHeartRates.html";
		    	break;
		    case "GET_USER_SLEEP":
		    	vm.tabla = "./partials/hermesServices/tablaGetUserSleep.html";
		    	break;
		    }
			
		}

		function datosPromise(){
			//Como el filtro no se aplica nada mas entrar,
			//puede ser que vm.events sea undefined asi que 
			//lo inicilizamos
			if (vm.events === undefined){
				vm.events = [];
			}

			var dfd = $q.defer();
			dfd.resolve(vm.events);

			return dfd.promise;
		}

		function mostrarMapa() {
			vm.showMap = true;
			vm.showTab = false;
			mostrarResultados();
			vm.activeInput = $translate.instant('hermesServices.mapa');

			//Para evitar que se carguen las tablas de la parte Table
			vm.tabla = undefined;
		}

		function mostrarTabla() {
			vm.showMap = false;
			vm.showTab = true;
			mostrarResultados();
			vm.activeInput = $translate.instant('hermesServices.tabla');

			vm.cargarListadoTabla();
		}

		function mostrarResultados(){
			//Segun el metodo elegido querremos mostrar lo de resultados o no
			if (vm.methodSelected === 'COMPUTE_ROUTE' || vm.methodSelected === 'GET_VEHICLE_LOCATIONS' ||
				vm.methodSelected === 'GET_MEASUREMENTS' || vm.methodSelected === 'GET_DATA_SECTIONS' ||
				vm.methodSelected === 'GET_DRIVER_FEATURES' || vm.methodSelected === 'GET_STEPS_DATA' ||
				vm.methodSelected === 'GET_HEART_RATE_DATA' || vm.methodSelected === 'GET_SLEEP_DATA' ||
				vm.methodSelected === 'GET_CONTEXT_DATA' || vm.methodSelected === 'GET_USER_LOCATIONS' ||
				vm.methodSelected === 'GET_USER_ACTIVITIES' || vm.methodSelected === 'GET_USER_DISTANCES' ||
				vm.methodSelected === 'GET_USER_STEPS' || vm.methodSelected === 'GET_USER_CALORIES_EXPENDED' ||
				vm.methodSelected === 'GET_USER_HEART_RATES' || vm.methodSelected === 'GET_USER_SLEEP'){
				vm.showResult = false;
				
			}else{
				vm.showResult = true;
			}
		}
		
		//mapa
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

		//map.on('dragend', aplicarFiltros);
		//map.on('zoomend', aplicarFiltros);
			
		//Anadimos la edit toolbar
		// Initialise the FeatureGroup to store editable layers
		var drawnItems = new L.FeatureGroup();
		map.addLayer(drawnItems);	

		//Extendemos los dos circle para pintar los puntos de COMPUTE_ROUTE
		L.Draw.CircleOrigin = L.Draw.SimpleShape.extend({
			statics: {
				TYPE: 'circleOrigin'
			},

			options: {
				shapeOptions: {
					stroke: true,
					color: '#F30C28',
					weight: 4,
					opacity: 0.5,
					fill: true,
					fillColor: null, //same as color by default
					fillOpacity: 0.2,
					clickable: true
				}
			},

			initialize: function (map, options) {
				// Save the type so super can fire, need to do this as cannot do this.TYPE :(
				//this.type = L.Draw.Circle.TYPE;
				this.type = 'circleOrigin';

				L.Draw.SimpleShape.prototype.initialize.call(this, map, options);
			},

			_initialLabelText: $translate.instant('hermesServices.clickDrag'),

			_drawShape: function (latlng) {
				if (!this._shape) {
					this._shape = new L.Circle(this._startLatLng, this._startLatLng.distanceTo(latlng), this.options.shapeOptions);
					this._map.addLayer(this._shape);
				} else {
					this._shape.setRadius(this._startLatLng.distanceTo(latlng));
				}
			},

			_fireCreatedEvent: function () {
				var circle = new L.Circle(this._startLatLng, this._shape.getRadius(), this.options.shapeOptions);
				L.Draw.SimpleShape.prototype._fireCreatedEvent.call(this, circle);
			},

			_onMouseMove: function (e) {
				var latlng = e.latlng,
					radius;

				this._tooltip.updatePosition(latlng);
				if (this._isDrawing) {
					this._drawShape(latlng);

					// Get the new radius (rouded to 1 dp)
					radius = this._shape.getRadius().toFixed(1);

					this._tooltip.updateContent({
						text: $translate.instant('hermesServices.releaseMouse'),
						subtext: $translate.instant('hermesServices.radius') + radius + ' m'
					});
				}
			}
		});

		L.Draw.CircleDestiny = L.Draw.SimpleShape.extend({
			statics: {
				TYPE: 'circleDestiny'
			},

			options: {
				shapeOptions: {
					stroke: true,
					color: '#0C0CF3',
					weight: 4,
					opacity: 0.5,
					fill: true,
					fillColor: null, //same as color by default
					fillOpacity: 0.2,
					clickable: true
				}
			},

			initialize: function (map, options) {
				// Save the type so super can fire, need to do this as cannot do this.TYPE :(
				this.type = 'circleDestiny';

				L.Draw.SimpleShape.prototype.initialize.call(this, map, options);
			},

			_initialLabelText: $translate.instant('hermesServices.clickDrag'),

			_drawShape: function (latlng) {
				if (!this._shape) {
					this._shape = new L.Circle(this._startLatLng, this._startLatLng.distanceTo(latlng), this.options.shapeOptions);
					this._map.addLayer(this._shape);
				} else {
					this._shape.setRadius(this._startLatLng.distanceTo(latlng));
				}
			},

			_fireCreatedEvent: function () {
				var circle = new L.Circle(this._startLatLng, this._shape.getRadius(), this.options.shapeOptions);
				L.Draw.SimpleShape.prototype._fireCreatedEvent.call(this, circle);
			},

			_onMouseMove: function (e) {
				var latlng = e.latlng,
					radius;

				this._tooltip.updatePosition(latlng);
				if (this._isDrawing) {
					this._drawShape(latlng);

					// Get the new radius (rouded to 1 dp)
					radius = this._shape.getRadius().toFixed(1);

					this._tooltip.updateContent({
						text: $translate.instant('hermesServices.releaseMouse'),
						subtext: $translate.instant('hermesServices.radius') + radius + ' m'
					});
				}
			}
		});


		//Inicialiazmos un control vacio
		var drawControl = new L.Control.Draw({
			draw: {
				polyline: false,
		        polygon: false,
		        rectangle: false,
		        circle: false,
		        marker: false
		    },
		    edit:  {
				featureGroup: drawnItems
			}
		});
		map.addControl(drawControl);
		
		map.on('draw:drawstart', function (e) {
			//Cuando pulsa en el boton de comenzar a dibujar, borramos todo lo que habia en el mapa
			//porque solo queremos que haya un segmento o un punto en el mapa de cada vez
			//Nota: cuando estamos con poner los dos puntos (ej: compute_route) no podemos borrar 
			//puesto que necesitamos tener los dos puntos activos
			var type = e.layerType; //este es el tipo que vamos a pintar
			if (type === 'circleOrigin' || type === 'circleDestiny'){
				//Si esta pintando origen y ya hay otro pintado, lo borramos
				if (type === 'circleOrigin' && vm.fromLat !== undefined && vm.fromLng !== undefined){
					map.removeLayer(drawnItems);
					drawnItems = new L.FeatureGroup();
					map.addLayer(drawnItems);
					
					vm.fromLat = undefined;
					vm.fromLng = undefined;
					vm.toLat = undefined;
					vm.toLng = undefined;
				}
				
				//Si esta pintando destino y ya hay otro pintado, lo borramos
				if (type === 'circleDestiny' && vm.toLat !== undefined && vm.toLng !== undefined){
					map.removeLayer(drawnItems);
					drawnItems = new L.FeatureGroup();
					map.addLayer(drawnItems);
					
					vm.fromLat = undefined;
					vm.fromLng = undefined;
					vm.toLat = undefined;
					vm.toLng = undefined;
				}
				
			}else{
				map.removeLayer(drawnItems);
				drawnItems = new L.FeatureGroup();
				map.addLayer(drawnItems);
			}
			
			//borramos si hubiese pintado algun marker con el recorrido
			markers.clearLayers();
			
		});
		
		map.on('draw:created', function (e) {
		    var type = e.layerType,
		        layer = e.layer;

		    if (type === 'marker') {
		        layer.bindPopup('A popup!');
		    }

		    drawnItems.addLayer(layer);
		    
		    switch (vm.methodSelected) {
		    case "GET_INFORMATION_LINK":
		    	asignarCoordenadasSegmento(layer);
		    	break;
		    case "AGGREGATE_MEASUREMENT":
		    	asignarCoordenadasPunto(layer);
		    	break;
		    case "COMPUTE_ROUTE":
		    	asignarCoordenadasPuntoOrigenDestino(layer, type);
		    	break;
		    case "SIMULATE_ROUTE":
			  	asignarCoordenadasPuntoOrigenDestino(layer, type);
			  	break;
		    case "GET_VEHICLE_LOCATIONS":
		    	asignarCoordenadasRectangulo(layer);
		    	break;
		    case "GET_MEASUREMENTS":
		    	asignarCoordenadasRectangulo(layer);
		    	break;
		    case "GET_DATA_SECTIONS":
		    	asignarCoordenadasRectangulo(layer);
		    	break;
		    case "GET_CONTEXT_DATA":
		    	asignarCoordenadasRectangulo(layer);
		    	break;
		    case "GET_USER_LOCATIONS":
		    	asignarCoordenadasRectangulo(layer);
		    	break;
		    }
		});
		
		map.on('draw:edited', function (e) {
		    var layers = e.layers;
		    //Esto realmente solo se va a ejecutar una unica vez porque solo hay una capa
		    layers.eachLayer(function (layer) {
		    	switch (vm.methodSelected) {
			    case "GET_INFORMATION_LINK":
			    	asignarCoordenadasSegmento(layer);
			    	break;
			    case "AGGREGATE_MEASUREMENT":
			    	asignarCoordenadasPunto(layer);
			    	break;
			    case "COMPUTE_ROUTE":
			    	asignarCoordenadasPuntoOrigenDestino(layer, layer.layerType);
			    	break;
			    case "SIMULATE_ROUTE":
				  	asignarCoordenadasPuntoOrigenDestino(layer, layer.layerType);
				  	break;
			    case "GET_VEHICLE_LOCATIONS":
			    	asignarCoordenadasRectangulo(layer);
			    	break;
			    case "GET_MEASUREMENTS":
			    	asignarCoordenadasRectangulo(layer);
			    	break;
			    case "GET_DATA_SECTIONS":
			    	asignarCoordenadasRectangulo(layer);
			    	break;
			    case "GET_CONTEXT_DATA":
			    	asignarCoordenadasRectangulo(layer);
			    	break;
			    case "GET_USER_LOCATIONS":
			    	asignarCoordenadasRectangulo(layer);
			    	break;
			    }
		    });
		});
		
		map.on('draw:deleted', function (e) {
			limpiarParametrosMapa();
		});
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function limpiarParametrosMapa(){
			vm.previousLong = undefined;
			vm.previousLat = undefined;
			vm.currentLong = undefined;
			vm.currentLat = undefined;
			
			vm.pointLng = undefined;
			vm.pointLat = undefined;
			
			vm.fromLat = undefined;
			vm.fromLng = undefined;
			vm.toLat = undefined;
			vm.toLng = undefined;
			
			vm.nwLng = undefined;
			vm.nwLat = undefined;
			vm.seLng = undefined;
			vm.seLat = undefined;
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		
		function changeMethod(){
			if (vm.serviceSelected !== undefined && vm.serviceSelected !== null && vm.serviceSelected !== ""){
				hermesServicesService.getMethods(vm.serviceSelected).then(function(response){
					vm.methods = response.data;
				});
			}else{
				vm.methods = undefined;
			}
			
			vm.methodSelected = undefined;
			vm.change();
		}
		
		function change(){
			vm.changeToolbar();
			vm.changeFilter();
			
			limpiarParametrosMapa();
			
			vm.result = undefined;
			
			vm.errorPoint = false;
			vm.mensajeErrorPoint = undefined;
			
			markers.clearLayers();
			
			map.removeLayer(drawnItems);
			drawnItems = new L.FeatureGroup();
			map.addLayer(drawnItems);
			
			vm.showMap = true;
			vm.showTab = false;
			//Para evitar que se carguen las tablas de la parte Table
			vm.tabla = undefined;
			
			//Segun el metodo elegido querremos mostrar lo de resultados o no
			if (vm.methodSelected === 'COMPUTE_ROUTE' || vm.methodSelected === 'GET_VEHICLE_LOCATIONS' ||
				vm.methodSelected === 'GET_MEASUREMENTS' || vm.methodSelected === 'GET_DATA_SECTIONS' ||
				vm.methodSelected === 'GET_DRIVER_FEATURES' || vm.methodSelected === 'GET_STEPS_DATA' ||
				vm.methodSelected === 'GET_HEART_RATE_DATA' || vm.methodSelected === 'GET_SLEEP_DATA' ||
				vm.methodSelected === 'GET_CONTEXT_DATA' || vm.methodSelected === 'GET_USER_LOCATIONS' ||
				vm.methodSelected === 'GET_USER_ACTIVITIES' || vm.methodSelected === 'GET_USER_DISTANCES' ||
				vm.methodSelected === 'GET_USER_STEPS' || vm.methodSelected === 'GET_USER_CALORIES_EXPENDED' ||
				vm.methodSelected === 'GET_USER_HEART_RATES' || vm.methodSelected === 'GET_USER_SLEEP' ||
				vm.methodSelected === 'SIMULATE_ROUTE'){
				vm.showResult = false;
				vm.showSelectorMapTab = true;
			}else{
				vm.showResult = true;
				vm.showSelectorMapTab = false;
			}
			
		}
		
		function changeToolbar(){

			//eliminamos el toolbar previo
			map.removeControl(drawControl);

			//eliminamos lo que habia pintado
			map.removeLayer(drawnItems);

			switch (vm.methodSelected) {
			case "GET_INFORMATION_LINK":
				// Initialise the draw control and pass it the FeatureGroup of editable layers
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: true,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});

				break;
			case "AGGREGATE_MEASUREMENT":
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});

				break;

			case "COMPUTE_ROUTE":
			case "SIMULATE_ROUTE":
				//map.addLayer(drawnItems);

				//Los incluimos en el toolbar
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
								{
									enabled: false,
								    handler: new L.Draw.Polygon(map, this.options.polygon),
								    title: L.drawLocal.draw.toolbar.buttons.polygon
								},
								{
									enabled: false,
								    handler: new L.Draw.Rectangle(map, this.options.rectangle),
								    title: L.drawLocal.draw.toolbar.buttons.rectangle
								},
								{
									enabled: false,
								    handler: new L.Draw.Marker(map, this.options.marker),
								    title: L.drawLocal.draw.toolbar.buttons.marker
								},
								{
									enabled: false,
								    handler: new L.Draw.Circle(map, this.options.circle),
								    title: L.drawLocal.draw.toolbar.buttons.circle
								},
						        {
						        	enabled: true,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: true,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});

				//El draw que ya existe lo usaremos para el punto de origen
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: false
				});

				break;
			case "GET_VEHICLE_LOCATIONS":
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});
				
		    	break;
		    case "GET_MEASUREMENTS":
		    	L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});
				
		    	break;
		    case "GET_DATA_SECTIONS":
		    	L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});
				
		    	break;
		    case "GET_CONTEXT_DATA":
		    	L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});
				
		    	break;
		    case "GET_USER_LOCATIONS":
		    	L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});
				
		    	break;
		    	
			default:
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
								{
									enabled: false,
								    handler: new L.Draw.Polygon(map, this.options.polygon),
								    title: L.drawLocal.draw.toolbar.buttons.polygon
								},
								{
									enabled: false,
								    handler: new L.Draw.Rectangle(map, this.options.rectangle),
								    title: L.drawLocal.draw.toolbar.buttons.rectangle
								},
								{
									enabled: false,
								    handler: new L.Draw.Marker(map, this.options.marker),
								    title: L.drawLocal.draw.toolbar.buttons.marker
								},
								{
									enabled: false,
								    handler: new L.Draw.Circle(map, this.options.circle),
								    title: L.drawLocal.draw.toolbar.buttons.circle
								},
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('hermesServices.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('hermesServices.drawDestinyPoint')
						        }
						        ];
					}
				});
			
				drawControl = new L.Control.Draw({
					draw: {
				    },
				    edit:  {
						featureGroup: drawnItems
					}
				});
			}
			
			map.addControl(drawControl);

		}

		function changeFilter(){

			switch (vm.methodSelected) {
			case "GET_INFORMATION_LINK":
				vm.filtroConcreto = undefined;
				break;
			case "AGGREGATE_MEASUREMENT":
				vm.filtroConcreto = "./partials/hermesServices/filtrosAggregateMeasurement.html";
				break;
			case "COMPUTE_ROUTE":
				vm.filtroConcreto = undefined;
				break;
			case "SIMULATE_ROUTE":
				vm.filtroConcreto = undefined;
				break;
			case "GET_VEHICLE_LOCATIONS":
				vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_MEASUREMENTS":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosMeasurementFechas.html";
		    	break;
		    case "GET_DATA_SECTIONS":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_DRIVER_FEATURES":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_HEART_RATE_DATA":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_STEPS_DATA":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_SLEEP_DATA":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_CONTEXT_DATA":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_LOCATIONS":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_ACTIVITIES":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_DISTANCES":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_STEPS":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_CALORIES_EXPENDED":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_HEART_RATES":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
		    case "GET_USER_SLEEP":
		    	vm.filtroConcreto = "./partials/hermesServices/filtrosFechas.html";
		    	break;
			default:
				vm.filtroConcreto = undefined;
			}

		}
				
		function asignarCoordenadasSegmento(layer){
			 var numPuntosSegmento = layer._latlngs.length;
			 var primerPunto = layer._latlngs[0];
			 //de todos los puntos del segmento pintado, nos quedamos con el ultimo
			 //TODO: esto deberia de ser [1] en lugar del ultimo punto si conseguimos que solo
			 //pueda indicar uno
			 var ultimoPunto = layer._latlngs[numPuntosSegmento-1];
			 
			 vm.previousLong = primerPunto.lng;
			 vm.previousLat = primerPunto.lat;
			 
			 vm.currentLong = ultimoPunto.lng;
			 vm.currentLat = ultimoPunto.lat;
			 
		}
		
		function asignarCoordenadasPunto(layer){
			vm.pointLng = layer._latlng.lng;
			vm.pointLat = layer._latlng.lat;
		}
		
		function asignarCoordenadasPuntoOrigenDestino(layer, type){
			if (type === 'circleOrigin'){
				vm.fromLng = layer._latlng.lng;
				vm.fromLat = layer._latlng.lat;
			}else if (type === 'circleDestiny'){
				vm.toLng = layer._latlng.lng;
				vm.toLat = layer._latlng.lat;	
			}
		}
		
		function asignarCoordenadasRectangulo(layer){
			vm.nwLng = layer.getLatLngs()[0].lng;
			vm.nwLat = layer.getLatLngs()[0].lat;
			
			vm.seLng = layer.getLatLngs()[2].lng;
			vm.seLat = layer.getLatLngs()[2].lat;
		}
		
		function infoPopupComputeRoute(linkId, maxSpeed, linkName, linkType, length, cost) {
			
			var maxSpeedEvento = "";
			if (maxSpeed !== null){
				maxSpeedEvento = $filter('number')(maxSpeed, 2);	
			}
			
			var lenghtEvento = "";
			if (length !== null){
				lenghtEvento = $filter('number')(length, 2);	
			}
			
			var costEvento = "";
			if (cost !== null){
				costEvento = $filter('number')(cost, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('hermesServices.linkId') + ':</b> ' + linkId +
				'<br/><b>' + $translate.instant('hermesServices.maxSpeed') + ':</b> '+maxSpeedEvento+
				'<br/><b>' + $translate.instant('hermesServices.linkName') + ':</b> '+linkName+
				'<br/><b>' + $translate.instant('hermesServices.linkType') + ':</b> '+linkType +
				'<br/><b>' + $translate.instant('hermesServices.length') + ':</b> '+lenghtEvento +
				'<br/><b>' + $translate.instant('hermesServices.cost') + ':</b> '+costEvento;
			
			return datosEvento;
		}
		
		function infoPopupSimulateRoute(speed) {

			var speedEvento = "";
			if (speed !== null){
				speedEvento = $filter('number')(speed, 2);
			}
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('vehicleLocation.speed') + ':</b> '+speedEvento + '<br/><b>';
			return datosEvento;
		}
		
		
		function infoPopupVehicleLocation(userId, timestamp, eventSpeed, eventAccuracy) {
			var date = new Date(timestamp);
			var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
			var hourEvento = $filter('date')(date, 'HH:mm:ss');
			
			var speedEvento = "";
			if (eventSpeed !== null){
				speedEvento = $filter('number')(eventSpeed, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('vehicleLocation.userId') + ':</b> ' + userId +
				'<br/><b>' + $translate.instant('vehicleLocation.date') + ':</b> '+dateEvento+
				'<br/><b>' + $translate.instant('vehicleLocation.time') + ':</b> '+hourEvento+
				'<br/><b>' + $translate.instant('vehicleLocation.speed') + ':</b> '+speedEvento +
				'<br/><b>' + $translate.instant('vehicleLocation.accuracy') + ':</b> '+eventAccuracy;
			return datosEvento;
		}
		
		function infoPopupMeasurement(userId, timestamp, eventValue, eventSpeed, eventAccuracy) {
			var date = new Date(timestamp);
			var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
			var hourEvento = $filter('date')(date, 'HH:mm:ss');
			
			var valueEvento = "";
			if (eventValue !== null){
				valueEvento = $filter('number')(eventValue, 2);	
			}
			
			var speedEvento = "";
			if (eventSpeed !== null){
				speedEvento = $filter('number')(eventSpeed, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('measurement.userId') + ':</b> ' + userId +
				'<br/><b>' + $translate.instant('measurement.date') + ':</b> '+dateEvento+
				'<br/><b>' + $translate.instant('measurement.time') + ':</b> '+hourEvento+
				'<br/><b>' + $translate.instant('measurement.value') + ':</b> '+valueEvento+
				'<br/><b>' + $translate.instant('measurement.speed') + ':</b> '+speedEvento +
				'<br/><b>' + $translate.instant('measurement.accuracy') + ':</b> '+eventAccuracy;
			return datosEvento;
		}

		function infoPopupDataSection(userId, timestamp, eventAccuracy, eventMinSpeed, eventMaxSpeed, eventMedianSpeed, eventAverageSpeed, eventAverageEr, eventAverageHearRate, eventStandardDeviationSpeed, eventStandardDeviationRr, eventStandardDeviationHeartRate, eventPke, eventNumHighAccelerations, eventNumHighDecelerations, eventAverageAcceleration, eventAverageDeceleration, eventRrSection ) {
			var date = new Date(timestamp);
			var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
			var hourEvento = $filter('date')(date, 'HH:mm:ss');
			
			var minSpeedEvento = "";
			if (eventMinSpeed !== null){
				minSpeedEvento = $filter('number')(eventMinSpeed, 2);	
			}
			
			var maxSpeedEvento = "";
			if (eventMaxSpeed !== null){
				maxSpeedEvento = $filter('number')(eventMaxSpeed, 2);
			}
			
			var medianSpeedEvento = "";
			if (eventMedianSpeed !== null){
				medianSpeedEvento = $filter('number')(eventMedianSpeed, 2);
			}
			
			var averageSpeedEvento = "";
			if (eventAverageSpeed !== null){
				averageSpeedEvento = $filter('number')(eventAverageSpeed, 2);	
			}
			
			var averageErEvento = "";
			if (eventAverageEr !== null){
				averageErEvento = $filter('number')(eventAverageEr, 2);	
			}
			
			var averageHearRateEvento = "";
			if (eventAverageHearRate !== null){
				averageHearRateEvento = $filter('number')(eventAverageHearRate, 2);	
			}
			
			var standardDeviationSpeedEvento = "";
			if (eventStandardDeviationSpeed !== null){
				standardDeviationSpeedEvento = $filter('number')(eventStandardDeviationSpeed, 2);	
			}
			
			var standardDeviationRrEvento = "";
			if (eventStandardDeviationRr !== null){
				standardDeviationRrEvento = $filter('number')(eventStandardDeviationRr, 2);	
			}
			
			var standardDeviationHeartRateEvento = "";
			if (eventStandardDeviationHeartRate !== null){
				standardDeviationHeartRateEvento = $filter('number')(eventStandardDeviationHeartRate, 2);
			}
			
			var pkeEvento = "";
			if (eventPke !== null){
				pkeEvento = $filter('number')(eventPke, 2);				
			}
			
			var numHighAccelerationsEvento = "";
			if (eventNumHighAccelerations !== null){
				numHighAccelerationsEvento = $filter('number')(eventNumHighAccelerations, 2);
			}
			
			var numHighDecelerationsEvento = "";
			if (eventNumHighDecelerations !== null){
				numHighDecelerationsEvento = $filter('number')(eventNumHighDecelerations, 2);	
			}
			
			var averageAccelerationEvento = "";
			if (eventAverageAcceleration !== null){
				averageAccelerationEvento = $filter('number')(eventAverageAcceleration, 2);	
			}
			
			var averageDecelerationEvento = "";
			if (eventAverageDeceleration !== null){
				averageDecelerationEvento = $filter('number')(eventAverageDeceleration, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('dataSection.userId') + ':</b> ' + userId +
			'<br/><b>' + $translate.instant('dataSection.date') + ':</b> '+dateEvento+
			'<br/><b>' + $translate.instant('dataSection.time') + ':</b> '+hourEvento+
			'<br/><b>' + $translate.instant('dataSection.speed') + ':</b>' +
			'<br/>' + $translate.instant('dataSection.minimum') + ': '+minSpeedEvento +
			'<br/>' + $translate.instant('dataSection.maximum') + ': '+maxSpeedEvento +
			'<br/>' + $translate.instant('dataSection.median') + ': '+medianSpeedEvento +
			'<br/>' + $translate.instant('dataSection.average') + ': '+averageSpeedEvento +
			'<br/>' + $translate.instant('dataSection.stdDev') + ':'+standardDeviationSpeedEvento + 
			'<br/><b>' + $translate.instant('dataSection.acceleration') + ':</b>'+
			'<br/>' + $translate.instant('dataSection.averageAcceleration') + ':'+averageAccelerationEvento +
			'<br/>' + $translate.instant('dataSection.averageDeceleration') + ':'+averageDecelerationEvento +
			'<br/>' + $translate.instant('dataSection.highAcceleration') + ':'+numHighAccelerationsEvento +
			'<br/>' + $translate.instant('dataSection.highDecceleration') + ':'+numHighDecelerationsEvento +
			'<br/><b>' + $translate.instant('dataSection.hearRate') + ':</b>'+
			'<br/>' + $translate.instant('dataSection.average') + ':'+averageHearRateEvento +
			'<br/>' + $translate.instant('dataSection.stdDev') + ':'+standardDeviationHeartRateEvento;

			return datosEvento;
		}

		function infoPopupUserLocation(userId, startTime, endTime, eventAccuracy) {
			var dateStart = new Date(startTime);
			var dateStartEvento = $filter('date')(dateStart, 'yyyy-MM-dd');
			var hourStartEvento = $filter('date')(dateStart, 'HH:mm:ss');
			
			var dateEnd = new Date(endTime);
			var dateEndEvento = $filter('date')(dateEnd, 'yyyy-MM-dd');
			var hourEndEvento = $filter('date')(dateEnd, 'HH:mm:ss');
						
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('userLocation.userId') + ':</b> ' + userId +
				'<br/><b>' + $translate.instant('userLocation.dateStart') + ':</b> '+dateStartEvento+
				'<br/><b>' + $translate.instant('userLocation.timeStart') + ':</b> '+hourStartEvento+
				'<br/><b>' + $translate.instant('userLocation.dateEnd') + ':</b> '+dateEndEvento+
				'<br/><b>' + $translate.instant('userLocation.timeEnd') + ':</b> '+hourEndEvento+
				'<br/><b>' + $translate.instant('userLocation.accuracy') + ':</b> '+eventAccuracy;
			
			return datosEvento;
		}

		function infoPopupContextData(userId, timestamp, eventActivity, eventAccuracy) {
			var date = new Date(timestamp);
			var dateEvento = $filter('date')(date, 'yyyy-MM-dd');
			var hourEvento = $filter('date')(date, 'HH:mm:ss');
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('contextData.userId') + ':</b> ' + userId +
						'<br/><b>' + $translate.instant('contextData.date') + ':</b> '+dateEvento+
						'<br/><b>' + $translate.instant('contextData.time') + ':</b> '+hourEvento+
						'<br/><b>' + $translate.instant('contextData.detectedActivity') + ':</b> '+eventActivity +
						'<br/><b>' + $translate.instant('contextData.accuracy') + ':</b> '+eventAccuracy;
			return datosEvento;
		}
		
		function pintarLineasCommputeRoute(events) {
			markers.clearLayers();
			angular.forEach(events, function(value, key) {			
				var info = infoPopupComputeRoute(value.linkId, value.maxSpeed, value.linkName, value.linkType,
									 value.length, value.cost);

				//Almaceno array de puntos 
				var myLines = value.geom_way;
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
		
		function pintarPuntosSimulateRoute(events) {
			markers.clearLayers();
			angular.forEach(events, function(value, key) {
				var mystyles = {
						color: 'red',
						fillOpacity: 0.5
				};
				var info = infoPopupSimulateRoute(value.speed);
				var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
				markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
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

		function pintarPuntosMeasurement(events) {
			var mystyles = {
					color: 'red',
					fillOpacity: 0.5
			};

			markers.clearLayers();
			angular.forEach(events, function(value, key) {
				var info = infoPopupMeasurement(value.usuarioMovil.sourceId.substring(0,10) + "...", value.timestamp, value.value, value.speed, value.accuracy);			
				//Convierto el punto que quiero pintar para tener su lat y log
				var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
				//Añado al mapa el punto
				markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
			});		
		}
		
		function pintarLineasDataSection(events) {
			markers.clearLayers();
			angular.forEach(events, function(value, key) {			
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
		
		function pintarPuntosUserLocation(events) {
			var mystyles = {
					color: 'red',
					fillOpacity: 0.5
			};

			markers.clearLayers();
			angular.forEach(events, function(value, key) {
				var info = infoPopupUserLocation(value.usuarioMovil.sourceId.substring(0,10) + "...", value.startTime, value.endTime, value.accuracy);			
				//Convierto el punto que quiero pintar para tener su lat y log
				var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);
				//Añado al mapa el punto
				markers.addLayer(L.circle(latlng, 10, mystyles).bindPopup(info));
			});		
		}
		
		
		function ejecutarPeticion(){
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				  	hermesServicesService.getLinkInformation(vm.currentLong, vm.currentLat, vm.previousLong, vm.previousLat).then(getLinkInformationComplete);
					
					function getLinkInformationComplete(response) {
						vm.result = response.data;
						vm.resultadoConcreto = './partials/hermesServices/resultadosInformationLink.html';
					}
					
					break;
			  case "AGGREGATE_MEASUREMENT":
				  	hermesServicesService.getAggregateMeasurement(vm.typeSelected, vm.pointLat, vm.pointLng, vm.daySelected, vm.timeSelected, vm.dataSectionSelected).then(getAggregateMeasurementComplete);
					
					function getAggregateMeasurementComplete(response) {
						vm.result = response.data;
						vm.resultadoConcreto = './partials/hermesServices/resultadosAggregateMeasurement.html';
					}
					
				  break;
				
			  case "COMPUTE_ROUTE":
				  hermesServicesService.getComputeRoute(vm.fromLat, vm.fromLng, vm.toLat, vm.toLng).then(getComputeRouteComplete).catch(getComputeRouteFailed);
					
					function getComputeRouteComplete(response) {
						
						vm.resultadoConcreto = undefined;
						
						vm.events = response.data;
						pintarLineasCommputeRoute(vm.events);

						vm.cargarListadoTabla();
					}
					
					function getComputeRouteFailed(error) {
						vm.errorPoint = true;
						vm.mensajeErrorPoint = error.data.message;
					}
										
				  break;
				
			 case "SIMULATE_ROUTE":
					  hermesServicesService.getSimulateRoute(vm.fromLat, vm.fromLng, vm.toLat, vm.toLng).then(getSimulateRouteComplete).catch(getSimulateRouteFailed);

						function getSimulateRouteComplete(response) {
							vm.resultadoConcreto = undefined;
							vm.events = response.data;
							pintarPuntosSimulateRoute(vm.events);
						}

						function getSimulateRouteFailed(error) {
							vm.errorPoint = true;
							vm.mensajeErrorPoint = error.data.message;
						}

					  break;
					  	
			  case "GET_VEHICLE_LOCATIONS":
				  	hermesServicesService.getVehicleLocation(vm.startDate, vm.endDate, vm.seLng, vm.seLat, vm.nwLng, vm.nwLat).then(getVehicleLocationComplete);
					
					function getVehicleLocationComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						pintarPuntosVehicleLocation(vm.events);

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    case "GET_MEASUREMENTS":
			    	
			    	hermesServicesService.getMeasurement(vm.measurementTypeSelected, vm.startDate, vm.endDate, vm.seLng, vm.seLat, vm.nwLng, vm.nwLat).then(getMeasurementComplete);
					
					function getMeasurementComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						pintarPuntosMeasurement(vm.events);

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    case "GET_DATA_SECTIONS":
			    	
			    	hermesServicesService.getDataSection(vm.startDate, vm.endDate, vm.seLng, vm.seLat, vm.nwLng, vm.nwLat).then(getDataSectionComplete);
					
					function getDataSectionComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						pintarLineasDataSection(vm.events);

						vm.cargarListadoTabla();
						
					}
			    	
			    	break;
			    	
			    case "GET_DRIVER_FEATURES":
			    	
			    	hermesServicesService.getDriverFeatures(vm.startDate, vm.endDate).then(getDriverFeaturesComplete);
					
					function getDriverFeaturesComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    	
				case "GET_HEART_RATE_DATA":
						
					hermesServicesService.getHeartRateData(vm.startDate, vm.endDate).then(getHeartRateDataComplete);
					
					function getHeartRateDataComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
							    	
				case "GET_SLEEP_DATA":
					hermesServicesService.getSleepData(vm.startDate, vm.endDate).then(getSleepDataComplete);
					
					function getSleepDataComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
				case "GET_STEPS_DATA":
					hermesServicesService.getStepsData(vm.startDate, vm.endDate).then(getStepsDataComplete);
					
					function getStepsDataComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
					
			    case "GET_CONTEXT_DATA":
			    	
			    	hermesServicesService.getContextData(vm.startDate, vm.endDate, vm.seLng, vm.seLat, vm.nwLng, vm.nwLat).then(getContextDataComplete);
					
					function getContextDataComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						pintarPuntosContextData(vm.events);

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    case "GET_USER_LOCATIONS":
			    	
			    	hermesServicesService.getUserLocations(vm.startDate, vm.endDate, vm.seLng, vm.seLat, vm.nwLng, vm.nwLat).then(getUserLocationsComplete);
					
					function getUserLocationsComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						pintarPuntosUserLocation(vm.events);

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    	
			    case "GET_USER_ACTIVITIES":
			    	hermesServicesService.getUserActivities(vm.startDate, vm.endDate).then(getUserActivitiesComplete);
					
					function getUserActivitiesComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    	
			    case "GET_USER_DISTANCES":
			    	hermesServicesService.getUserDistances(vm.startDate, vm.endDate).then(getUserDistancesComplete);
					
					function getUserDistancesComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
			    	break;
			    	
			    case "GET_USER_STEPS":
			    	hermesServicesService.getUserSteps(vm.startDate, vm.endDate).then(getUserStepsComplete);
					
					function getUserStepsComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
			    	
					break;
					
			    case "GET_USER_CALORIES_EXPENDED":
			    	hermesServicesService.getUserCaloriesExpended(vm.startDate, vm.endDate).then(getUserCaloriesExpendedComplete);
					
					function getUserCaloriesExpendedComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
					
			    case "GET_USER_HEART_RATES":
			    	hermesServicesService.getUserHeartRates(vm.startDate, vm.endDate).then(getUserHeartRatesComplete);
					
					function getUserHeartRatesComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
					
			    case "GET_USER_SLEEP":
			    	hermesServicesService.getUserSleep(vm.startDate, vm.endDate).then(getUserSleepComplete);
					
					function getUserSleepComplete(response) {
						
						vm.events = response;
						vm.resultadoConcreto = undefined;
												
						markers.clearLayers();

						vm.cargarListadoTabla();
						
					}
					
					break;
					
			  default:
				 break;
			}
		}
		
		function validacionLinkInformation(){
			if (vm.previousLong === undefined || vm.previousLat === undefined || 
				vm.currentLong === undefined || vm.currentLat === undefined){
				SweetAlert.swal($translate.instant('hermesServices.selectSegment'));
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionAggregateMeasurement(){
			var texto = "";
			if (vm.typeSelected === undefined){
				texto = texto + $translate.instant('hermesServices.selectType') + '\n';
			}else{
				if (vm.typeSelected === 'DATA_SECTION' && vm.dataSectionSelected === undefined){
					texto = texto + $translate.instant('hermesServices.selectDataSection') + '\n';
				}
			}
			
			if (vm.pointLat === undefined || vm.pointLng === undefined){
				texto = texto + $translate.instant('hermesServices.selectPoint') + '\n';
			}
			
			if (vm.daySelected === undefined){
				texto = texto + $translate.instant('hermesServices.selectDay') + '\n'; 
			}
			
			if (vm.timeSelected === undefined){
				texto = texto + $translate.instant('hermesServices.selectTime') + '\n'; 
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
				
		function validacionComputeRoute(){
			var texto = "";
			if (vm.fromLng === undefined || vm.fromLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectOriginPoint') + '\n';
			}
			
			if (vm.toLng === undefined || vm.toLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectDestinyPoint') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}

		function validacionSimulateRoute(){
			var texto = "";
			if (vm.fromLng === undefined || vm.fromLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectOriginPoint') + '\n';
			}

			if (vm.toLng === undefined || vm.toLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectDestinyPoint') + '\n';
			}

			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetVehicleLocations(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
		
			if (vm.nwLng === undefined || vm.nwLat === undefined || 
					vm.seLng === undefined || vm.seLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectRectangle') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetMeasurements(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (vm.nwLng === undefined || vm.nwLat === undefined || 
					vm.seLng === undefined || vm.seLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectRectangle') + '\n';
			}
			
			if (vm.measurementTypeSelected === undefined){
				texto = texto + $translate.instant('hermesServices.selectMeasurementType') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetDataSections(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (vm.nwLng === undefined || vm.nwLat === undefined || 
					vm.seLng === undefined || vm.seLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectRectangle') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetDriverFeatures(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetHeartRateData(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetStepsData(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetSleepData(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetContextData(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (vm.nwLng === undefined || vm.nwLat === undefined || 
					vm.seLng === undefined || vm.seLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectRectangle') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserLocations(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (vm.nwLng === undefined || vm.nwLat === undefined || 
					vm.seLng === undefined || vm.seLat === undefined){
				texto = texto + $translate.instant('hermesServices.selectRectangle') + '\n';
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserActivities(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserDistances(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserSteps(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserCaloriesExpended(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserHeartRates(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionGetUserSleep(){
			var texto = "";
			if (vm.startDate === undefined || vm.endDate === undefined){
				texto = texto + $translate.instant('hermesServices.selectDates');
			}
			
			if (texto !== ""){
				SweetAlert.swal(texto);
			}else{
				ejecutarPeticion();
			}
		}
		
		function aplicarFiltros() {	
			
			vm.errorPoint = false;
			vm.mensajeErrorPoint = undefined;
			
			//Aplicamos el filtro que corresponda
			switch (vm.methodSelected) {
			case "GET_INFORMATION_LINK":
				validacionLinkInformation();
				break;

			case "AGGREGATE_MEASUREMENT":
				validacionAggregateMeasurement();
				break;

			case "COMPUTE_ROUTE":
				validacionComputeRoute();
				break;

			case "SIMULATE_ROUTE":
				validacionSimulateRoute();
				break;
				
			case "GET_VEHICLE_LOCATIONS":
				validacionGetVehicleLocations();
				break;

			case "GET_MEASUREMENTS":
				validacionGetMeasurements();
				break;

			case "GET_DATA_SECTIONS":
				validacionGetDataSections();
				break;

			case "GET_DRIVER_FEATURES":
				validacionGetDriverFeatures();
				break;

			case "GET_HEART_RATE_DATA":
				validacionGetHeartRateData();
				break;

			case "GET_STEPS_DATA":
				validacionGetStepsData();
				break;

			case "GET_SLEEP_DATA":
				validacionGetSleepData();
				break;

			case "GET_CONTEXT_DATA":
				validacionGetContextData();
				break;

			case "GET_USER_LOCATIONS":
				validacionGetUserLocations();
				break;

			case "GET_USER_ACTIVITIES":
				validacionGetUserActivities();
				break;
				
			case "GET_USER_DISTANCES":
				validacionGetUserDistances();
				break;
				
			case "GET_USER_STEPS":
				validacionGetUserSteps();
				break;
				
			case "GET_USER_CALORIES_EXPENDED":
				validacionGetUserCaloriesExpended();
				break;
				
			case "GET_USER_HEART_RATES":
				validacionGetUserHeartRates();
				break;
				
			case "GET_USER_SLEEP":
				validacionGetUserSleep();
				break;
			}	
		}
		
		function arrancar() {
			var resultado = {
					value : "Running",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.arrancar();
		}
		
		function parar() {
			var resultado = {
					value : "Stopped",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.parar();
		}
		
		function onTimeSetStart() {
			vm.showCalendarStart = !vm.showCalendarStart;
		}

		function onTimeSetEnd() {
			vm.showCalendarEnd = !vm.showCalendarEnd;
		}
		
		//vm.aplicarFiltros();
		 

	}
})();