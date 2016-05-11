(function() {
	'use strict';

	angular.module('app').controller('JobController', JobController);

	JobController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'jobService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'jobs'];
	
	function JobController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , jobService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, jobs) {
	
		var vm = this;
		
		vm.jobs = jobs;
		
		vm.add = add;
		vm.edit = edit;
		vm.delet = delet;
		vm.executeJob = executeJob;
			
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.jobs);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('job.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('job.name')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('job.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                	   return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
	                           '   <i class="fa fa-edit"></i>' +
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
	                           '   <i class="fa fa-trash-o"></i>' +
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-info" data-ui-sref="manageConceptTransformation({idJob:'+ data.id + '})" >' +
	                           	$translate.instant('job.manageConceptTransformation') + 
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-success" data-ng-click="vm.executeJob(' + data.id + ')">' +
	                           $translate.instant('job.executeJob') + 
	                           '</button>';
		                   })
		                ];  
		
		
		  
		function add(){
			
			vm.infoAction = undefined;
		
			var modalInstance = $uibModal.open({
                templateUrl: './partials/job/modal-form.html',
                controller: 'JobModalController',
                scope: $scope,
                resolve: {
                	infoJob: function(){
                    	return null;
                    },
                    jobForm: function () {
                        return $scope.jobForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	jobService.getJobs().then(function(response) {
		     		vm.jobs = response;
	        		if (vm.dtInstance !== null){
	        			vm.dtInstance.reloadData();
	        		}
	        	});
    	        
            }, function (response) {
            	console.log('Modal dismissed at: ' + new Date());
            });
		}
		
		function edit (id) {
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/job/modal-form.html',
                controller: 'JobModalController',
                scope: $scope,
                resolve: {
                	infoJob: function(){
                		return jobService.getJob(id);
                    },
                    jobForm: function () {
                        return $scope.jobForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	jobService.getJobs().then(function(response) {
		     		vm.jobs = response;
	        		if (vm.dtInstance !== null){
	        			vm.dtInstance.reloadData();
	        		}
	        	});
    	        
            }, function () {
            	console.log('Modal dismissed at: ' + new Date());
            });
	    }

		function delet(id) {
	        
	    	vm.infoAction = undefined;
	    	
	        SweetAlert.swal({
	        	   title: $translate.instant('confirmDelete'),
	        	   text: $translate.instant('textDelete'),
	        	   type: "warning",
	        	   showCancelButton: true,
	        	   confirmButtonColor: "#DD6B55",
	        	   confirmButtonText: $translate.instant('aceptar'),
	        	   closeOnConfirm: true,
	        	   closeOnCancel: true 
	        	}, 
	        	function(isConfirm){
	        		if (isConfirm){
	        			jobService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		jobService.getJobs().then(function(response) {
		   	        			vm.jobs = response;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	}
	        );
	    }
		
		function executeJob(id){
			jobService.executeJob(id).then(function(response) {
				var idExecution = response.data.id;
				var status = response.data.status;
				
				//Lanzamos la tarea de ejecucion de un trabajo
				
				//Redirigimos a la pantalla con los mensajes de la ejecucion
				$state.go('showMessages', {idExecution: idExecution, status: status});
			});	
		}
		
	}
	
})();