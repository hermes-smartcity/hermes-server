(function() {
	'use strict';

	angular.module('app').controller('DataServicesController', DataServicesController);

	DataServicesController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                 '$state', '$rootScope', 'eventsService', 'services',
	                                 'totalMUsers', 'totalWebUsers', 
	                                 'numberActiveUsers', 'eventsToday', 
	                                 'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                 'totalSTD', 'totalSLD', 'totalHRD', 'totalCD', 'dataServicesService'];

	function DataServicesController($scope, $filter, $http, $translate, $state, 
			$rootScope, eventsService, services, totalMUsers, totalWebUsers, 
			numberActiveUsers, eventsToday, eventoProcesado, totalL, totalDS, totalM, totalDF, 
			totalSTD, totalSLD, totalHRD, totalCD, dataServicesService) {

		var vm = this;

		vm.aplicarFiltros = aplicarFiltros;

		// Inicializamos el filtro de error type para que inicialmente liste warning
		//vm.serviceSelected = "SMARTDRIVER";
		vm.serviceSelected = undefined;
		vm.methodSelected = undefined;

		vm.services = services;
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

		vm.recuperarYpintarPeticiones = recuperarYpintarPeticiones;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.getLiveChartData = getLiveChartData;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;

		vm.arrancar = arrancar;
		vm.parar = parar;

		vm.startDate = new Date();
		// Inicializamos la fecha de inicio a la del mes anterior
		vm.startDate.setDate(vm.startDate.getDate() - 31);
		vm.endDate = new Date();

		vm.changeMethod = changeMethod;
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}

		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}

		function changeMethod(){
			if (vm.serviceSelected !== undefined && vm.serviceSelected !== ""){
				dataServicesService.getMethods(vm.serviceSelected).then(function(response){
					vm.methods = response.data;
				});
			}else{
				vm.methods = undefined;
			}
		}
		
		function recuperarYpintarPeticiones(urlGet){

			dataServicesService.getPeticionesPorDia(urlGet).then(getPeticionesPorDiaComplete);
			// En cuanto tenga los eventos los pinto
			function getPeticionesPorDiaComplete(response) {

				vm.peticionesPorDia = response.data;
				vm.labels = vm.peticionesPorDia.fechas;
				vm.series = [ $translate.instant('numeroPeticiones')];
				vm.data = [vm.peticionesPorDia.nEventos];


				vm.onClick = function (points, evt) {
					console.log(points, evt);
				};

				// Si no hay eventos que cumplan los requisitos marcados en los filtros entonces se actualiza con el gráfico
				getLiveChartData();

			}

		}

		function getLiveChartData () {
			if (!vm.data[0].length) {
				vm.labels = [0];
				vm.data[0] = [0];
			}
		}
		
		// Preparar las fechas para pasarlas como parametro a los controladores
		function prepararParametrosFechas() {
			var url = "";
			if (vm.startDate !== null) {
				var _startDate = $filter('date')(vm.startDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "&fechaIni=" + _startDate;
			}
			if (vm.endDate !== null) {
				var _endDate = $filter('date')(vm.endDate,
						'yyyy-MM-dd HH:mm:ss');
				url += "&fechaFin=" + _endDate;
			}
			return url;
		}
		
		// Preparar la url que va a llamar al controlador
		function prepararUrl(){
			var url = url_peticionesPorDia;
			url += "service=" + vm.serviceSelected + "&method=" + vm.methodSelected;
			url += prepararParametrosFechas();
			
			return url;
		}


		function aplicarFiltros() {	
			var url = prepararUrl();
			vm.recuperarYpintarPeticiones(url);
		}

		function onTimeSetStart() {
			vm.showCalendarStart = !vm.showCalendarStart;
		}

		function onTimeSetEnd() {
			vm.showCalendarEnd = !vm.showCalendarEnd;
		}

		function arrancar() {
			eventsService.arrancar();
			$state.go('dashboard');
		}

		function parar() {
			eventsService.parar();
			$state.go('dashboard');
		}

		vm.aplicarFiltros();


	}
})();