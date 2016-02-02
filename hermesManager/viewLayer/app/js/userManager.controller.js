(function() {
	'use strict';

	angular.module('app').controller('UserManagerController', UserManagerController);

	UserManagerController.$inject = ['$scope', '$http', '$timeout', '$log', '$filter', 'userService'/*, 'users'*/,
	                                 'userRestService', '$resource'];

	function UserManagerController($scope, $http, $timeout, $log, $filter, userService/*, users*/, userRestService, $resource) {
	
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
	
		userRestService.delete({id: usuario.id}, success, failure);
				
		var success = function (result) {
			var idx = vm.users.indexOf(usuario);
			vm.users.splice(idx, 1);
		};

		var failure = function (result) {
		    alert("Error: " + result);
		};
		
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