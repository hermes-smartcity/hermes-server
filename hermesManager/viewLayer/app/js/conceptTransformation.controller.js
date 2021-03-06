(function() {
	'use strict';

	angular.module('app').controller('ConceptTransformationController', ConceptTransformationController);

	ConceptTransformationController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'conceptTransformationService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idJob', 'concepttransformations'];
	
	function ConceptTransformationController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , conceptTransformationService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idJob, concepttransformations) {
	
		var vm = this;
				
		vm.idJob = parseInt(idJob);
		vm.concepttransformations = concepttransformations.data;
		
		vm.add = add;
		vm.edit = edit;
		vm.delet = delet;
		vm.doTheBack = doTheBack;
		
		 //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise).withOption('createdRow', createdRow);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};
		
		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.concepttransformations);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('concepttransformation.id')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('concepttransformation.osmconcept')).renderWith(function(data,type,full) {
		                	   var texto = data.osmConcept.name;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('concepttransformation.dbconcept')).renderWith(function(data,type,full) {
		                	   var texto = data.dbConcept.name;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('concepttransformation.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                	   return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
	                           '   <i class="fa fa-edit"></i>' +
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
	                           '   <i class="fa fa-trash-o"></i>' +
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-info" data-ui-sref="manageAttributeMapping({idConceptTransformation:'+ data.id + ',idOsmConcept:'+ data.osmConcept.id + ',idDbConcept:'+ data.dbConcept.id + '})" >' +
	                           	$translate.instant('concepttransformation.manageAttributeMapping') + 
	                           '</button>';
		                   })
		                ];  
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/concepttransformation/modal-form.html',
                controller: 'ConceptTransformationModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                    	return null;
                    },
                    idJob: function(){
                    	return vm.idJob;
                    },
                	osmconcepts: function () {
                		return conceptTransformationService.getOsmConcepts();
                    },
                    dbconcepts: function () {
                		return conceptTransformationService.getDbConcepts();
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	conceptTransformationService.getConceptTransformations(vm.idJob).then(function(response) {
		     		vm.concepttransformations = response.data;
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
                templateUrl: './partials/concepttransformation/modal-form.html',
                controller: 'ConceptTransformationModalController',
                scope: $scope,
                resolve: {
                	infoConcept: function(){
                		return conceptTransformationService.getConceptTransformation(id);
                    },
                    idJob: function(){
                    	return vm.idJob;
                    },
                    osmconcepts: function () {
                		return conceptTransformationService.getOsmConcepts();
                    },
                    dbconcepts: function () {
                		return conceptTransformationService.getDbConcepts();
                    },
                    conceptForm: function () {
                        return $scope.conceptForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	conceptTransformationService.getConceptTransformations(vm.idJob).then(function(response) {
		     		vm.concepttransformations = response.data;
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
	        			conceptTransformationService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		conceptTransformationService.getConceptTransformations(vm.idJob).then(function(response) {
		   			     		vm.concepttransformations = response.data;
		   	        			if (vm.dtInstance !== null){
		   	        				vm.dtInstance.reloadData();
		   	        			}
		   	        		});
		   	        	});	
	        		}
	        	});
	        	             
	    }
		
		function doTheBack(){
			window.history.back();
		}
	}
	
})();