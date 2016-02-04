(function() {
	'use strict';

	angular.module('app').controller('UserManagerController', UserManagerController);

	UserManagerController.$inject = ['$scope', '$http', '$timeout', '$log', '$filter', 'userService'/*, 'users'*/,
	                                 'userRestService', '$resource', '$state'];

	function UserManagerController($scope, $http, $timeout, $log, $filter, userService/*, users*/, userRestService, 
			$resource, $state) {
	
	var vm = this;
//	vm.users = users;
	vm.showAdmins = showAdmins;
	vm.showUsers = showUsers;
	vm.deleteUser = deleteUser;
	vm.showAdmin = false;
	vm.showUser = true;
	
	vm.showUsers();
	
	function deleteUser(usuario) {

		vm.error = null;
		var idUsers = -1;
		var idAdmins = -1;
		if(typeof vm.users != "undefined")
			vm.users.indexOf(usuario);
		
		if(typeof vm.admins != "undefined")
			vm.admins.indexOf(usuario);
		
//		var idUsers = vm.users.indexOf(usuario);
//		var idAdmins = vm.admins.indexOf(usuario);
//		userRestService.delete({id: usuario.id});
		
	
		userRestService.$remove(function(){
			if(idUsers!=-1){
				vm.users.splice(idUsers,1);
				lista = vm.users;
			}
			if(idAdmins!=-1){
				vm.admins.splice(idAdmins,1);
			}
		});
		
		
	}
	
	function showAdmins() {	
		vm.showAdmin = true;
		vm.showUser = false;
		$http.get(url_admins).success(function(data) {
			vm.admins = data;
			paginarUsuarios();			
		});
	}
	
	function showUsers() {	
		vm.showAdmin = false;
		vm.showUser = true;
	
		$http.get(url_users).success(function(data) {
			vm.users = data;
			paginarUsuarios();			
		});
	}

	  
	  function paginarUsuarios() {		  
		  vm.currentPage = 1;
		  vm.pageSize = 10;
	  }
	 
	}
})();