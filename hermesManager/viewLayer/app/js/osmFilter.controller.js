(function() {
	'use strict';

	angular.module('app').controller('OSMFilterController', OSMFilterController);

	OSMFilterController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'osmFilterService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idOsmConcept', 'osmoperations', 'osmfilters'];
	
	function OSMFilterController($scope, $filter, $http, $translate, $state, 
			$rootScope, $q, $compile, $uibModal , osmFilterService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idOsmConcept, osmoperations, osmfilters) {
	
		var vm = this;
		
		vm.idOsmConcept = parseInt(idOsmConcept);
		vm.osmoperations = osmoperations;
		vm.osmfilters = osmfilters.data;
		
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
			dfd.resolve(vm.osmfilters);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('osmfilter.id')),
		                   DTColumnBuilder.newColumn('name').withTitle($translate.instant('osmfilter.name')),
		                   DTColumnBuilder.newColumn('operation').withTitle($translate.instant('osmfilter.operation')),
		                   DTColumnBuilder.newColumn('value').withTitle($translate.instant('osmfilter.value')),
		                   DTColumnBuilder.newColumn(null).withTitle($translate.instant('osmfilter.actions')).notSortable()
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
                templateUrl: './partials/osmfilter/modal-form.html',
                controller: 'OSMFilterModalController',
                scope: $scope,
                resolve: {
                	infoFilter: function(){
                    	return null;
                    },
                    idOsmConcept: function(){
                    	return vm.idOsmConcept;
                    },
                    operations: function () {
                        return vm.osmoperations;
                    },
                    filterForm: function () {
                        return $scope.filterForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	osmFilterService.getOsmFilters(vm.idOsmConcept).then(function(response) {
		     		vm.osmfilters = response.data;
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
                templateUrl: './partials/osmfilter/modal-form.html',
                controller: 'OSMFilterModalController',
                scope: $scope,
                resolve: {
                	infoFilter: function(){
                		return osmFilterService.getOsmFilter(id);
                    },
                    idOsmConcept: function(){
                    	return vm.idOsmConcept;
                    },
                    operations: function () {
                        return vm.osmoperations;
                    },
                    filterForm: function () {
                        return $scope.filterForm;
                    }
                }
            });
	        
	        modalInstance.result.then(function (response) {
		     	vm.infoAction = response;
		     	
		     	osmFilterService.getOsmFilters(vm.idOsmConcept).then(function(response) {
		     		vm.osmfilters = response.data;
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
	        			osmFilterService.delet(id).then(function(response) {
		   	        		vm.infoAction = response.data;
		   	        		osmFilterService.getOsmFilters(vm.idOsmConcept).then(function(response) {
		   	        			vm.osmfilters = response.data;
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