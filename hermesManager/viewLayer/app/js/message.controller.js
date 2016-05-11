(function() {
	'use strict';

	angular.module('app').controller('MessageController', MessageController);

	MessageController.$inject = ['$scope', '$filter', '$http', '$translate', '$interval', 
	                                '$state', '$rootScope', '$q', '$compile', '$uibModal',
	                                'messageService', 'SweetAlert',  'DTOptionsBuilder', 
	                                'DTColumnBuilder', 'idExecution', 'status', 'messages'];
	
	function MessageController($scope, $filter, $http, $translate, $interval, $state, 
			$rootScope, $q, $compile, $uibModal , messageService, SweetAlert, 
			DTOptionsBuilder, DTColumnBuilder, idExecution, status, messages) {
	
		var vm = this;
		
		vm.idExecution = idExecution;
		vm.status = status;
		vm.messages = messages.data;
		
		vm.updateMessages = updateMessages;
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
			dfd.resolve(vm.messages);

			return dfd.promise;
		}

		function createdRow(row, data, dataIndex) {
		    // Recompiling so we can bind Angular directive to the DT
		    $compile(angular.element(row).contents())($scope);
		}
		
		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('id').withTitle($translate.instant('message.id')),
		                   DTColumnBuilder.newColumn('timestamp').withTitle($translate.instant('message.time')).renderWith(function(data, type, full) {
		                	   return $filter('date')(data, 'dd/MM/yyyy HH:mm:ss');
		                   }),
		                   DTColumnBuilder.newColumn('text').withTitle($translate.instant('message.text'))
		                ];  
						
		function doTheBack(){
			window.history.back();
		}
		
		//Si el status de la ejecucion es running, tenemos que solicitar cada X segundos los mensajes
		//por si se van actualizando
		function updateMessages() {
			messageService.getMessages(vm.idExecution).then(function(response) {
	   			vm.messages = response.data;
	   			if (vm.dtInstance !== null){
	   				vm.dtInstance.reloadData();
	   			}
	   		});	
		}
		
		if (vm.status === 'RUNNING'){
			$interval(function() {
				vm.updateMessages();
			}, 1000*10); //1 segundo son 1000
		}
	}
	
})();