(function() {
	'use strict';

	angular.module('app').factory('jobService', jobService);

	jobService.$inject = ['$http', '$log', '$q', '$localStorage'];

	function jobService($http, $log, $q, $localStorage) {
		var service = {
				getJobs: getJobs,
				delet: delet,
				register: register,
				edit: edit,
				getJob: getJob,
				executeJob: executeJob,
				launchExecutionJob: launchExecutionJob
		};

		return service;
				
		function getJobs() {
			return $http.get(url_jobs)
				.then(getJobsComplete)
				.catch(getJobsFailed);
			function getJobsComplete(response) {
				return response.data;
			}
			function getJobsFailed(error) {
				$log.error('XHR Failed for getJobs.' + error.data);
			}
		}
		
		function delet (id) {	
			 
	        var lang = $localStorage.hermesmanager.lang;
	       
			return $http({
				method : 'DELETE',
				url : url_delete_job + "/" + id,
				params: {"lang": lang}
			});
		}
		
		function register (job) {
			
			var lang = $localStorage.hermesmanager.lang;
		       
			return $http({
				method : 'POST',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_register_job,
				data : job,
				params: {"lang": lang}
			});
		}
		
		function edit (job) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'PUT',
				headers: { 
			        'Accept': 'application/json',
			        'Content-Type': 'application/json' 
			    },
				url : url_edit_job + "/" + job.id,
				data : job,
				params: {"lang": lang}
			});
		}
		
		function getJob (id) {		
			return $http({
				method : 'GET',
				url : url_get_job,
				params: {"id": id}
			});
		}
		
		function executeJob (id) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			return $http({
				method : 'POST',
				url : url_execute_job,
				params: {"id": id, "lang": lang}
			});
		}
		
		function launchExecutionJob (idJob, idExecution) {	
			
			var lang = $localStorage.hermesmanager.lang;
			
			$http({
				method : 'POST',
				url : url_launch_execute_job,
				params: {"idJob": idJob, "idExecution": idExecution, "lang": lang}
			});
		}
				
	}
})();