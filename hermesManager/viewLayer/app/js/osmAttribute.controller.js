(function() {
	'use strict';

	angular.module('app').controller('OSMAttributeController', OSMAttributeController);

	OSMAttributeController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'osmAttributeService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idOsmConcept', 'osmattributes'];
	
	function OSMAttributeController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , osmAttributeService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idOsmConcept, osmattributes) {
	
		var vm = this;
		
		vm.idOsmConcept = parseInt(idOsmConcept);
		vm.osmattributes = osmattributes.data;
		
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
			dfd.resolve(vm.osmattributes);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('osmattribute.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('osmattribute.name')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('osmattribute.actions')).notSortable()
		                   .renderWith(function(data, type, full, meta) {
		                       return '<button class="btn btn-warning" data-ng-click="vm.edit(' + data.id + ')">' +
		                           '   <i class="fa fa-edit"></i>' +
		                           '</button>&nbsp;' +
		                           '<button class="btn btn-danger" data-ng-click="vm.delet(' + data.id + ')">' +
		                           '   <i class="fa fa-trash-o"></i>' +
		                           '</button>';
		                   })
		                ];  
		
		
		function add(){
			
			vm.infoAction = undefined;
			
			var modalInstance = $uibModal.open({
                templateUrl: './partials/osmattribute/modal-form.html',
                controller: 'OSMAttributeModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                    	return null;
                    },
                    idOsmConcept: function(){
                    	return vm.idOsmConcept;
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	osmAttributeService.getOsmAttributes(vm.idOsmConcept).then(function(response) {
		     		vm.osmattributes = response.data;
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
                templateUrl: './partials/osmattribute/modal-form.html',
                controller: 'OSMAttributeModalController',
                scope: $scope,
                resolve: {
                	infoAttribute: function(){
                		return osmAttributeService.getOsmAttribute(id);
                    },
                    idOsmConcept: function(){
                    	return vm.idOsmConcept;
                    },
                    attributeForm: function () {
                        return $scope.attributeForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	osmAttributeService.getOsmAttributes(vm.idOsmConcept).then(function(response) {
		     		vm.osmattributes = response.data;
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
	        			osmAttributeService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		osmAttributeService.getOsmAttributes(vm.idOsmConcept).then(function(response) {
		   	        			vm.osmattributes = response.data;
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