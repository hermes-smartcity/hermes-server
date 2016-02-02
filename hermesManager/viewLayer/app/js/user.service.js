(function() {
	'use strict';

	angular.module('app').factory('userService', userService);

	userService.$inject = ['$http', '$log', '$q'];

	function userService($http, $log, $q) {
		var service = {
			getUser: getUser,
			registerUser: registerUser,
			getRoles: getRoles,
			getUsers : getUsers,
			getUserToModify: getUserToModify
		};

		return service;
	
		function getUser(url) {
		
			return $http({
				method : 'GET',
				url : url
			});
		}
		
		function registerUser (email, password, rol) {		
			return $http({
				method : 'POST',
				url : url_register_user,
				params: {"email": email, "password": password, "rol":rol}
			});
		}
		
		function getRoles() {
			return $http.get(url_roles)
				.then(getRolesComplete)
				.catch(getRolesFailed);
			function getRolesComplete(response) {
				return response.data;
			}
			function getRolesFailed(error) {
				$log.error('XHR Failed for getRoles.' + error.data);
			}
		}
		
		function getUsers() {
			return $http.get(url_users)
				.then(getUsersComplete)
				.catch(getUsersFailed);
			function getUsersComplete(response) {
				return response.data;
			}
			function getUsersFailed(error) {
				$log.error('XHR Failed for getUsers.' + error.data);
			}
		}
		
		// TODO no sé porque falla
//		function getUserToModify(id) {
//			return $http({
//				method : 'GET',
//				url : url_userToModify + "/" + id
//			});
//		}
		
		//Opcion uno, asi, sino con then and catch
		function getUserToModify (id) {		
			return $http({
				method : 'GET',
				url : url_userToModify,
				params: {"id": id}
			});
		}
	}
})();