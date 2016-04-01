(function() {
	'use strict';

	angular.module('app').controller('UserManagerController', UserManagerController);

	UserManagerController.$inject = ['$scope', '$http', '$timeout', '$log', '$filter',
	                                 'userService', '$state', '$rootScope', 'eventsService', 
	                                 'eventsType', 'usuarios' ,'measurementsType',
	                                 'totalMUsers', 'totalWebUsers', 'numberActiveUsers', 'eventsToday', 
	                                  'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                  'totalSTD', 'totalSLD', 'totalHRD', 'totalCD', '$translate'];

	function UserManagerController($scope, $http, $timeout, $log, $filter, userService, $state, $rootScope,
			eventsService, eventsType, usuarios, measurementsType, totalMUsers, totalWebUsers, numberActiveUsers, eventsToday, 
			eventoProcesado, totalL, totalDS, totalM, totalDF, totalSTD, totalSLD, totalHRD, totalCD,
			$translate) {
	
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
		paginarUsuarios();	
	}
	
	function showUsers() {	
		vm.showAdmin = false;
		vm.showUser = true;
		vm.activeInput = $translate.instant('user.usuarios');
		$http.get(url_users).success(function(data) {
			vm.users = data;					
		});
		paginarUsuarios();	
	}

	  
	function paginarUsuarios() {
		  vm.currentPage = 1;
		  vm.pageSize = 10;
	}
	 
	function arrancar() {
		eventsService.arrancar();
		$state.go('dashboard');
	}
	
	function parar() {
		eventsService.parar();
		$state.go('dashboard');
	}
	
	}
})();