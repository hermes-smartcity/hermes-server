(function() {
	'use strict';

	angular.module('app').controller('UserManagerController', UserManagerController);

	UserManagerController.$inject = ['$scope', '$http', '$timeout', '$log', '$filter',
	                                 'userService', '$state', '$rootScope', 'eventsService', 
	                                 'usuarios', 'eventsToday', 'eventoProcesado', 'statistics', '$translate',
	                                  'DTOptionsBuilder'];

	function UserManagerController($scope, $http, $timeout, $log, $filter, userService, $state, $rootScope,
			eventsService, usuarios, eventsToday, eventoProcesado, statistics,
			$translate, DTOptionsBuilder) {
	
	var vm = this;
	vm.showAdmins = showAdmins;
	vm.showUsers = showUsers;
	vm.deleteUser = deleteUser;
	vm.showAdmin = false;
	vm.showUser = true;
	vm.activeInput = $translate.instant('user.usuarios');
	vm.users = [];
	vm.admins = [];
	vm.searchText ='';
	vm.searchTextAdmin='';
	vm.arrancar = arrancar;
	vm.parar = parar;
	
	vm.eventsType = $rootScope.eventsType;
	vm.usuarios = usuarios;
	vm.measurementsType = $rootScope.measurementsType;
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
	
	vm.totalGoogleFit = vm.totalUL + vm.totalUA + vm.totalUDI + vm.totalUST + vm.totalUCE + vm.totalUHR + vm.totalUSL;
	vm.totalFitBit = vm.totalSTD + vm.totalSLD + vm.totalHRD;
	
	//Inicializar options de la tabla
	vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json");
	
	vm.showUsers();
	
	// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
	if($rootScope.hasRole('ROLE_ADMIN')){
		eventsService.getStateActualizado().then(getStateActualizadoComplete);	
	}
	
	function getStateActualizadoComplete(response) {				
		vm.active = response.data;
	}
	
	
	function deleteUser(usuario) {

		vm.error = null;
		var idUsers = -1;
		var idAdmins = -1;
		
		if(typeof vm.users != "undefined")
			idUsers = vm.users.indexOf(usuario);
		
		if(typeof vm.admins != "undefined")
			idAdmins = vm.admins.indexOf(usuario);
		
		userService.deleteUser(usuario.id).then(deleteUserComplete);	
		
		function deleteUserComplete(response) {
			
			if(idUsers!=-1)
				vm.users.splice(idUsers,1);
			
			if(idAdmins!=-1)
				vm.admins.splice(idAdmins,1);
			
			vm.infoCuenta = response.data;
		}
		
	}
	
	function showAdmins() {	
		vm.showAdmin = true;
		vm.showUser = false;
		vm.activeInput = $translate.instant('user.administradores'); 
		$http.get(url_admins).success(function(data) {
			vm.admins = data;				
		});
	}
	
	function showUsers() {	
		vm.showAdmin = false;
		vm.showUser = true;
		vm.activeInput = $translate.instant('user.usuarios');
		$http.get(url_users).success(function(data) {
			vm.users = data;					
		});	
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
	
	}
})();