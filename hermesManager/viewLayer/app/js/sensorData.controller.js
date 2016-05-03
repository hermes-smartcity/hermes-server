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
		
		var that = this;
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
		vm.seriesSelected = seriesSelected;
		
		// Gráfico con los datos de los sensores
		vm.chartAxis = {
			       ejeX : true,
			       ejeY : true,
			       ejeZ: true
			     };
		vm.chart = {};
		vm.chart.type = "AnnotationChart";
		vm.chart.options =  {
				 title: 'Data Sensor',
				 height: 500,				 
				 displayAnnotations: false,
				 displayLegendDots: true,
				 displayAnnotationsFilter: true,
				 displayExactValues: true,
				 legendPosition: 'newRow',
				 dateFormat: 'HH:mm:ss MMMM dd, yyyy',
				 explorer : { maxZoomOut:5, keepInBounds: true, maxZoomIn: 20},
				 displayRangeSelector: true,
			      "colors": ['#0000FF', '#009900', '#CC0000', '#DD9900'],
			      "defaultColors": ['#0000FF', '#009900', '#CC0000', '#DD9900'],
			      "isStacked": "true",
			      "fill": 20,
			      "vAxis": {
			        "title": "Data Sensor",
			        "gridlines": {
			          "count": 10
			        }
			      },
			      "hAxis": {
			        "title": "Date"
			      }
	    };
	
		vm.chart.view = {
	      columns: [0, 1, 2, 3]
	    };
		
		vm.chart.data = {"cols": [	        {id: "fecha", label: "Fecha", type: "date"},
		                         	        {id: "ejeX", label: "Eje X", type: "number"},
		                         	        {id: "ejeY", label: "Eje Y", type: "number"},
		                         	        {id: "ejeZ", label: "Eje Z", type: "number"}
		                         	    ],  "rows": []};
		
	
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
		
		function seriesSelected(selectedItem) {
			console.log(selectedItem);
//		      var col = selectedItem.column;
				var col = selectedItem;
		      //If there's no row value, user clicked the legend.
//		      if (selectedItem.row !== null) {
		        //If true, the chart series is currently displayed normally.  Hide it.
		        console.log(vm.chart.view.columns[col]);
		        if (vm.chart.view.columns[col] == col) {
		          //Replace the integer value with this object initializer.
		          vm.chart.view.columns[col] = {
		            //Take the label value and type from the existing column.
		            label: vm.chart.data.cols[col].label,
		            type: vm.chart.data.cols[col].type,
		            //makes the new column a calculated column based on a function that returns null, 
		            //effectively hiding the series.
		            calc: function() {
		              return null;
		            }
		          };
		          //Change the series color to grey to indicate that it is hidden.
		          //Uses color[col-1] instead of colors[col] because the domain column (in my case the date values)
		          //does not need a color value.
		          vm.chart.options.colors[col - 1] = '#CCCCCC';
		        }
		        //series is currently hidden, bring it back.
		        else {
		          console.log("Ran this.");
		          //Simply reassigning the integer column index value removes the calculated column.
		          vm.chart.view.columns[col] = col;
		          console.log(vm.chart.view.columns[col]);
		          //I had the original colors already backed up in another array.  If you want to do this in a more
		          //dynamic way (say if the user could change colors for example), then you'd need to have them backed
		          //up when you switch to grey.
		          vm.chart.options.colors[col - 1] = vm.chart.options.defaultColors[col - 1];
		        }
//		      }
        }
		
		function recuperarYpintarSensores(urlGet){

			sensorDataService.getInfoSensoresPorDia(urlGet).then(getInfoSensoresPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getInfoSensoresPorDiaComplete(response) {
				convertDateStringsToDates(response);
				vm.rows = response.data.rows;

				vm.chart.data.rows = vm.rows;				
			}
			
		}
		
		// Inicializamos el filtro de sensor type para que inicialmente liste
		// TYPE_ACCELEROMETER
		vm.sensorTypeSelected = "TYPE_LINEAR_ACCELERATION";
		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la del mes anterior
		vm.startDate.setMinutes(vm.startDate.getMinutes() - 5);
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