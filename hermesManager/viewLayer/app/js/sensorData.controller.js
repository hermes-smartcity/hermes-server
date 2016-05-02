(function() {
	'use strict';
	
	
	angular.module('app').controller('SensorDataController', SensorDataController);

	SensorDataController.$inject = ['$state', '$interval', '$scope', 'usuarios', 
	                                  '$http', '$timeout', '$log', '$filter', 'eventsService', '$rootScope',
	                                  'eventsToday', 'eventoProcesado', 'statistics', '$translate',
	                                  'sensorDataService'];

	function SensorDataController($state, $interval, $scope, usuarios, $http, $timeout, $log, $filter,
			eventsService, $rootScope, eventsToday, eventoProcesado, statistics,
			$translate, sensorDataService) {

		var vm = this;
		vm.aplicarFiltros = aplicarFiltros;
		vm.sensorsType = $rootScope.sensorsType;
		vm.usuarios = usuarios;
		vm.measurementsType = $rootScope.measurementsType;
		vm.arrancar = arrancar;
		vm.parar = parar;
		
		vm.pintarGrafico = pintarGrafico;
		
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.getLiveChartData = getLiveChartData;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		
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
		vm.recuperarYpintarSensores = recuperarYpintarSensores;
		
		// Gráfico con los datos de los sensores
		vm.chart = {};
		vm.chart.type = "AnnotationChart";
		vm.chart.options =  {
				 title: 'Data Sensor',
				 height: 500,
				 displayAnnotations: false,
				 displayAnnotationsFilter: true,
				 displayExactValues: true,
				 dateFormat: 'HH:mm:ss MMMM dd, yyyy',
				 displayRangeSelector: true
	    };
		vm.chart.data = {"cols": [	        {id: "fecha", label: "Fecha", type: "date"},
		                         	        {id: "ejeX", label: "Eje X", type: "number"},
		                         	        {id: "ejeY", label: "Eje Y", type: "number"},
		                         	        {id: "ejeZ", label: "Eje Z", type: "number"}
		                         	    ],  "rows": []};
		
		/* *  Prueba para elegir que tipo de gráfico escogemos * * */
		vm.chartLine = {};
		vm.chartLine.type = "LineChart";
		vm.chartLine.options =  {
				 title: 'Data Sensor',
				 height: 800,
				 width: 800,
				 displayExactValues: true,
				 explorer : { maxZoomOut:5, keepInBounds: true, maxZoomIn: 20},
				 dateFormat: 'HH:mm:ss MMMM dd, yyyy'
				
	    };
		vm.chartLine.data = {"cols": [	     {id: "fecha", label: "Fecha", type: "date"},
		                         	        {id: "ejeX", label: "Eje X", type: "number"},
		                         	        {id: "ejeY", label: "Eje Y", type: "number"},
		                         	        {id: "ejeZ", label: "Eje Z", type: "number"}
		                         	    ],  "rows": []};
		/* * *  */
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
	
		// Detecta el patrón de una fecha en la response y lo cambia por el formato new Date( ) para que la librería de angular puede pintar por fecha y distribuya los puntos de manera uniforme
		
		function convertDateStringsToDates(input) {
			var regexIso8601 = /^(\d{4}|\+\d{6})(?:-(\d{2})(?:-(\d{2})(?:T(\d{2}):(\d{2}):(\d{2})\.(\d{1,})(Z|([\-+])(\d{2}):(\d{2}))?)?)?)?$/;
		    // Ignore things that aren't objects.
		    if (typeof input !== "object") return input;

		    for (var key in input) {
		        if (!input.hasOwnProperty(key)) continue;

		        var value = input[key];
		        var match;
		        // Check for string properties which look like dates.
		        if (typeof value === "string" && (match = value.match(regexIso8601))) {
		            var milliseconds = Date.parse(match[0]);
		            if (!isNaN(milliseconds)) {
		            	var d = new Date();
		            	var n = d.getTimezoneOffset();
		            	milliseconds += (n * 60 * 1000);		            	
		                input[key] = new Date(milliseconds);
		            }
		        } else if (typeof value === "object") {
		            // Recurse into object
		            convertDateStringsToDates(value);
		        }
		    }
		}
		
		function recuperarYpintarSensores(urlGet){

			sensorDataService.getInfoSensoresPorDia(urlGet).then(getInfoSensoresPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getInfoSensoresPorDiaComplete(response) {
				convertDateStringsToDates(response);
				vm.rows = response.data.rows;

				vm.chart.data.rows = vm.rows;
				vm.chartLine.data.rows = vm.rows;
				
			}
			
		}
		
		// Inicializamos el filtro de sensor type para que inicialmente liste
		// TYPE_ACCELEROMETER
		vm.sensorTypeSelected = "TYPE_LINEAR_ACCELERATION";
		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la del mes anterior
		vm.startDate.setDate(vm.startDate.getDate() - 31);
		vm.endDate = new Date();
		
		  
		function getLiveChartData () {
		      if (vm.data.length === 0) {
		    	  vm.labels = [0];
		    	  vm.data[0] = [0];
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

		// Preparar las fechas para pasarlas como parametro a los controladores
		function prepararParametrosFechas() {
			var url = "";
			if (vm.startDate !== null) {
				var _startDate = $filter('date')(vm.startDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaIni=" + _startDate + "&";
			}
			if (vm.endDate !== null) {
				var _endDate = $filter('date')(vm.endDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "fechaFin=" + _endDate;
			}
			return url;
		}

		// Preparar la url que va a llamar al controlador. TODO falta buscar una
		// manera menos chapuza. y en el futuro se va a cambiar a hacer más REST
		function prepararUrl() {
			var url = "";
			if (typeof vm.usuarioSelected != 'undefined' && vm.usuarioSelected !== null)
				url += "idUsuario=" + vm.usuarioSelected.id + "&";
			url += prepararParametrosFechas();
			
			return url;
		}
		

		function pintarGrafico() {
			var url = url_infoPorDia;	
			url +="sensor="+vm.sensorTypeSelected+"&";
			url+=prepararUrl();
			vm.recuperarYpintarSensores(url);
		}
		
		function aplicarFiltros() {
			vm.pintarGrafico();
		}
		
		 function onTimeSetStart() {
			    vm.showCalendarStart = !vm.showCalendarStart;
		 }
		 
		 function onTimeSetEnd() {
			    vm.showCalendarEnd = !vm.showCalendarEnd;
		 }

		// Inicialmente sé que voy a pintar los type  linear acceleration (la opción por
		// defecto en el select)
		vm.aplicarFiltros();

	}
})();