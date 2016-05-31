(function() {
	'use strict';

	angular.module('app').controller('ExecutionController', ExecutionController);

	ExecutionController.$inject = ['$scope', '$filter', '$http', '$translate', '$interval',
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'executionService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'executions', 'IntervalExecutions'];
	
	function ExecutionController($scope, $filter, $http, $translate, $interval, $state, 
			$rootScope, $q, $compile, $uibModal , executionService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, executions, IntervalExecutions) {
	
		var vm = this;
		
		vm.executions = executions;
		
		vm.delet = delet;
		vm.updateExecutions = updateExecutions;
	
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.executions);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('execution.id')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('execution.job')).renderWith(function(data,type,full) {
		                	   var texto = data.job.name;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn('status').withTitle($translate.instant('execution.status')),
		                   DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('execution.time')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('execution.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                       return '<button class="btn btn-danger" data-ng-show="\'' + data.status + '\' !== \'RUNNING\'" data-ng-click="vm.delet(' + data.id + ')">' +
		                           '   <i class="fa fa-trash-o"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-info" data-ui-sref="showMessages({idExecution:'+ data.id + ', status:\'' + data.status + '\'})" >' +
		                           '   <i class="fa fa-eye"></i>' +
		                           '</button>';
		                   })
		                ];  
		
		
		
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
	        			executionService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		executionService.getExecutions().then(function(response) {
		   	        			vm.executions = response;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
		
		function updateExecutions() {
			executionService.getExecutions().then(function(response) {
	   			vm.executions = response;
	   			if (vm.dtInstance !== null){
	   				vm.dtInstance.reloadData();
	   			}
	   		});	
		}
		
		IntervalExecutions.start(10, updateExecutions);
		
	}
	
})();