(function() {
	'use strict';

	angular.module('app').controller('OSMConceptController', OSMConceptController);

	OSMConceptController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'osmConceptService', 'SweetAlert',  
	                                'DTOptionsBuilder', 'DTColumnBuilder', 
	                                'osmconcepts'];

	function OSMConceptController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , osmConceptService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, osmconcepts) {
	
		var vm = this;
		
		vm.osmconcepts = osmconcepts;
		
		vm.add = add;
		vm.edit = edit;
		vm.delet = delet;
		
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.osmconcepts);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('osmconcept.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('osmconcept.name')),
		                  
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('osmconcept.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                       return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
		                           '   <i class="fa fa-edit"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
		                           '   <i class="fa fa-trash-o"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-info" data-ui-sref="manageOsmAttributes({idOsmConcept:'+ data.id + '})" >' +
		                           	$translate.instant('osmconcept.manageAttributes') + 
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-info" data-ui-sref="manageOsmFilters({idOsmConcept:'+ data.id + '})" >' +
		                           	$translate.instant('osmconcept.manageFilters') + 
		                           '</button>';
		                   })
		                ];
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/osmconcept/modal-form.html',
                controller: 'OSMConceptModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                    	return null;
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	osmConceptService.getOsmConcepts().then(function(response) {
		     		vm.osmconcepts = response;
	        		if (vm.dtInstance !== null){
	        			vm.dtInstance.reloadData();
	        		}
	        	});
    	        
            }, function () {
            	console.log('Modal dismissed at: ' + new Date());
            });
		}
		
		function edit (id) {
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/osmconcept/modal-form.html',
                controller: 'OSMConceptModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                    	return osmConceptService.getOsmConcept(id);
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
	        	vm.infoAction = response;
                
	        	osmConceptService.getOsmConcepts().then(function(response) {
		     		vm.osmconcepts = response;
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
	        			osmConceptService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		osmConceptService.getOsmConcepts().then(function(response) {
		   	        			vm.osmconcepts = response;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
	}
})();