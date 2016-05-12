(function() {
	'use strict';

	angular.module('app').controller('AttributeMappingController', AttributeMappingController);

	AttributeMappingController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'attributeMappingService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idConceptTransformation', 'idOsmConcept',
	                                'idDbConcept', 'attributemappings'];
	
	function AttributeMappingController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , attributeMappingService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idConceptTransformation, idOsmConcept,
			idDbConcept, attributemappings) {
	
		var vm = this;
				
		vm.idConceptTransformation = parseInt(idConceptTransformation);
		vm.idOsmConcept = parseInt(idOsmConcept);
		vm.idDbConcept = parseInt(idDbConcept);
		vm.attributemappings = attributemappings.data;
		
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
			dfd.resolve(vm.attributemappings);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('attributemapping.id')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('attributemapping.osmattribute')).renderWith(function(data,type,full) {
		                	   var texto = data.osmAttribute.name;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('attributemapping.dbattribute')).renderWith(function(data,type,full) {
		                	   var texto = data.dbAttribute.attributeName;
		                	   return texto;
		                   }),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('attributemapping.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                	   return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
	                           '   <i class="fa fa-edit"></i>' +
	                           '</button>&nbsp;' +
	                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
	                           '   <i class="fa fa-trash-o"></i>' +
	                           '</button>&nbsp';
		                   })
		                ];  
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/attributemapping/modal-form.html',
                controller: 'AttributeMappingModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                    	return null;
                    },
                    idConceptTransformation: function(){
                    	return vm.idConceptTransformation;
                    },
                	osmattributes: function () {
                		return attributeMappingService.getOsmAttributes(idOsmConcept);
                    },
                    dbattributes: function () {
                		return attributeMappingService.getDbAttributes(idDbConcept);
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	attributeMappingService.getAttributeMappings(vm.idConceptTransformation).then(function(response) {
		     		vm.attributemappings = response.data;
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
                templateUrl: './partials/attributemapping/modal-form.html',
                controller: 'AttributeMappingModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                		return attributeMappingService.getAttributeMapping(id);
                    },
                    idConceptTransformation: function(){
                    	return vm.idConceptTransformation;
                    },
                	osmattributes: function () {
                		return attributeMappingService.getOsmAttributes(idOsmConcept);
                    },
                    dbattributes: function () {
                		return attributeMappingService.getDbAttributes(idDbConcept);
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	attributeMappingService.getAttributeMappings(vm.idConceptTransformation).then(function(response) {
		     		vm.attributemappings = response.data;
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
	        			attributeMappingService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		attributeMappingService.getAttributeMappings(vm.idConceptTransformation).then(function(response) {
		   			     		vm.attributemappings = response.data;
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