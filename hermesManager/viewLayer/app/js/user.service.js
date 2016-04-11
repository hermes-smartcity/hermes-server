(function() {
	'use strict';

	angular.module('app').factory('userService', userService);

	userService.$inject = ['$http', '$log', '$q'];

	function userService($http, $log, $q) {
		var service = {
			getUser: getUser,
			registerUser: registerUser,
			registerAdmin: registerAdmin,
			editUser: editUser,
			deleteUser: deleteUser,
			getRoles: getRoles,
			getUsers : getUsers,
			getUserToModify: getUserToModify,
			getInfoCuenta: getInfoCuenta,
			renewToken: renewToken,
			changePassword: changePassword,
			getUserProfile: getUserProfile,
			getParametersStatistics: getParametersStatistics
		};

		return service;
	
		function getUser(url) {		
			return $http({
				method : 'GET',
				url : url
			});
		}
		
		function registerUser (user) {		
			return $http({
				method : 'POST',
				url : url_register_user,
				data : user
			});
		}
		
		function registerAdmin (user) {		
			return $http({
				method : 'POST',
				url : url_register_admin,
				data : user
			});
		}
		
		function editUser (user) {		
			return $http({
				method : 'PUT',
				url : url_edit_user+"/"+user.id,
				data : user
			});
		}

		function deleteUser (id) {		
			return $http({
				method : 'DELETE',
				url : url_delete_user+"/"+id
			});
		}
		
		// Listar roles
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
		
		// Listar usuarios ROL_CONSULTA
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
		
		// Opcion uno, asi, sino con then and catch
		function getUserToModify (id) {		
			return $http({
				method : 'GET',
				url : url_userToModify,
				params: {"id": id}
			});
		}
		
		// Activar cuenta
		function getInfoCuenta (email, hash) {		
			return $http({
				method : 'GET',
				url : url_infoCuenta+"?email="+email+"&hash="+hash
			});
		}
		
		function getParametersStatistics() {
			return $http.get(url_parameters_statistics)
				.then(getParametersStatisticsComplete)
				.catch(getParametersStatisticsFailed);
			function getParametersStatisticsComplete(response) {
				return response.data;
			}
			function getParametersStatisticsFailed(error) {
				$log.error('XHR Failed for getParametersStatistics.' + error.data);
			}
		}
		
		function renewToken (oldToken) {
			
			return $http({
				method : 'POST',
				url : url_renewToken,
				data: oldToken
			});
		}
		
		function changePassword (newPassword) {		
			return $http({
				method : 'POST',
				url : url_change_password,
				data : newPassword
			});
		}
		
		// Listar usuarios ROL_CONSULTA
		function getUserProfile() {
			return $http.get(url_user_profile).then(getUserProfileComplete);
			
			function getUserProfileComplete(response) {
				return response.data;
			}
		}
		
				
	}
})();