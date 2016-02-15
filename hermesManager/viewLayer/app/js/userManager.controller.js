(function() {
	'use strict';

	angular.module('app').controller('UserManagerController', UserManagerController);

	UserManagerController.$inject = ['$scope', '$http', '$timeout', '$log', '$filter',
	                                 'userService', '$state'];

	function UserManagerController($scope, $http, $timeout, $log, $filter, userService, $state) {
	
	var vm = this;
	vm.showAdmins = showAdmins;
	vm.showUsers = showUsers;
	vm.deleteUser = deleteUser;
	vm.showAdmin = false;
	vm.showUser = true;
	vm.activeInput = 'Users';
	vm.users = [];
	vm.admins = [];
	vm.searchText ='';
	vm.searchTextAdmin='';
	
	vm.showUsers();
	
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
		vm.activeInput = 'Admins';
		$http.get(url_admins).success(function(data) {
			vm.admins = data;				
		});
		paginarUsuarios();	
	}
	
	function showUsers() {	
		vm.showAdmin = false;
		vm.showUser = true;
		vm.activeInput = 'Users';
		$http.get(url_users).success(function(data) {
			vm.users = data;					
		});
		paginarUsuarios();	
	}

	  
	  function paginarUsuarios() {
		  vm.currentPage = 1;
		  vm.pageSize = 10;
	  }
	 
	}
})();