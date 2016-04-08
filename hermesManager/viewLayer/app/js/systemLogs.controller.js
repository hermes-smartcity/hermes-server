(function() {
	'use strict';

	angular.module('app').controller('SystemLogsController', SystemLogsController);

	SystemLogsController.$inject = ['$scope', '$filter', '$http', 'DTOptionsBuilder', 'logService',
	                                '$state', '$rootScope', 'eventsService', 'eventsType', 'usuarios',
	                                'measurementsType', 'totalMUsers', 'totalWebUsers', 
	                                'numberActiveUsers', 'eventsToday', 
	                                'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                'totalSTD', 'totalSLD', 'totalHRD', 'totalCD'];

	function SystemLogsController($scope, $filter, $http, DTOptionsBuilder, logService, $state, 
			$rootScope, eventsService, eventsType, usuarios, measurementsType, totalMUsers, totalWebUsers, 
			numberActiveUsers, eventsToday, eventoProcesado, totalL, totalDS, totalM, totalDF, 
			totalSTD, totalSLD, totalHRD, totalCD) {
	
		var vm = this;
		
		vm.aplicarFiltros = aplicarFiltros;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		
		vm.deleteLog = deleteLog;
		
		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.errorTypeSelected = "WARN";

		// Inicializamos la fecha de inicio a la de ayer
		vm.startDate = new Date();
		vm.startDate.setDate(vm.startDate.getDate()-1);
		vm.endDate = new Date();
		
		//Inicializar options de la tabla
		vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json");
			
		vm.eventsType = eventsType;
		vm.usuarios = usuarios;
		vm.measurementsType = measurementsType;
		vm.totalMUsers = totalMUsers;
		vm.totalWebUsers = totalWebUsers;
		vm.numberActiveUsers = numberActiveUsers;
		vm.eventsToday = eventsToday;
		vm.eventoProcesado = eventoProcesado;
		vm.totalL = totalL;	
		vm.totalDS = totalDS;
		vm.totalM = totalM;
		vm.totalDF = totalDF;
		vm.totalSTD = totalSTD;
		vm.totalSLD = totalSLD;
		vm.totalHRD = totalHRD;
		vm.totalCD = totalCD;
		
		vm.arrancar = arrancar;
		vm.parar = parar;
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		// Preparar la url que va a llamar al controlador
		function prepararUrl(){
			var url = "";
			url+="level="+ vm.errorTypeSelected+"&";
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
		
		function aplicarFiltros() {		
			var url = url_systemLogs;
			url+=prepararUrl();
			
			$http.get(url).success(function(data) {
				vm.events = data;
			});
		}
		
		function onTimeSetStart() {
		    vm.showCalendarStart = !vm.showCalendarStart;
		}
		 
		function onTimeSetEnd() {
			 vm.showCalendarEnd = !vm.showCalendarEnd;
		}
		

		function deleteLog(log){
			var idLogs = -1;
			
			if(typeof vm.events != "undefined")
				idLogs = vm.events.indexOf(log);
			
			logService.deleteLog(log.id).then(deleteLogComplete);	
			
			function deleteLogComplete(response) {
				if(idLogs!=-1)
					vm.events.splice(idLogs,1);
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
		
		 // Inicialmente sé que voy a pintar los WARN (la opción por defecto en el select)
		 vm.aplicarFiltros();
		 

	}
})();