(function() {
	'use strict';

	angular.module('app').controller('SystemLogsController', SystemLogsController);

	SystemLogsController.$inject = ['$scope', '$filter', '$http'];

	function SystemLogsController($scope, $filter, $http) {
	
		var vm = this;
		
		vm.aplicarFiltros = aplicarFiltros;
		vm.onTimeSetStart = onTimeSetStart;
		vm.onTimeSetEnd = onTimeSetEnd;
		vm.showCalendarStart = false;
		vm.showCalendarEnd = false;
		
		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.errorTypeSelected = "WARN";

		// Inicializamos la fecha de inicio a la de ayer
		vm.startDate = new Date();
		vm.startDate.setDate(vm.startDate.getDate()-1);
		vm.endDate = new Date();
		
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
				paginarEventos();
			});
		}
		
		function onTimeSetStart() {
		    vm.showCalendarStart = !vm.showCalendarStart;
		}
		 
		function onTimeSetEnd() {
			    vm.showCalendarEnd = !vm.showCalendarEnd;
		}
		
		// Inicialmente sé que voy a pintar los WARN (la opción por defecto en el select)
		 vm.aplicarFiltros();
		 
		 function paginarEventos() {		  
			  vm.currentPage = 1;
			  vm.pageSize = 10;
		  }
	}
})();