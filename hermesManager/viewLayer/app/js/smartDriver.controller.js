(function() {
	'use strict';

	angular.module('app').controller('SmartDriverController', SmartDriverController);

	SmartDriverController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', 'eventsService', 'methods', 
	                                'types', 'dataSections', 'totalMUsers', 'totalWebUsers', 
	                                'numberActiveUsers', 'eventsToday', 
	                                'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                'totalSTD', 'totalSLD', 'totalHRD', 'totalCD', 'smartDriverService'];

	function SmartDriverController($scope, $filter, $http, $translate, $state, 
			$rootScope, eventsService, methods, types, dataSections, totalMUsers, totalWebUsers, 
			numberActiveUsers, eventsToday, eventoProcesado, totalL, totalDS, totalM, totalDF, 
			totalSTD, totalSLD, totalHRD, totalCD, smartDriverService) {
	
		var vm = this;
		
		vm.aplicarFiltros = aplicarFiltros;
				
		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.methodSelected = "GET_INFORMATION_LINK";
		
		vm.methods = methods;
		vm.types = types;
		vm.dataSections = dataSections;
		vm.totalMUsers = totalMUsers;
		vm.totalWebUsers = totalWebUsers;
		vm.numberActiveUsers = numberActiveUsers;
		vm.eventsToday = eventsToday;
		vm.eventoProcesado = eventoProcesado;
		vm.totalL = totalL;	
		vm.totalDS = totalDS;
		vm.totalM = totalM;
		vm.totalDF = totalDF;
		vm.totalSTD = totalSTD;
		vm.totalSLD = totalSLD;
		vm.totalHRD = totalHRD;
		vm.totalCD = totalCD;
		
		vm.arrancar = arrancar;
		vm.parar = parar;
		
		vm.previousLong = undefined;
		vm.previousLat = undefined;
		vm.currentLong = undefined;
		vm.currentLat = undefined;
		
		
		
		vm.change = change;
		vm.changeToolbar = changeToolbar;
		vm.changeFilter = changeFilter;
				
		vm.filtroConcreto = undefined;
		vm.resultadoConcreto = undefined;
		
		//Filtros de la seccion aggregateMeasurement
		vm.typeSelected = undefined;
		vm.pointLng = undefined;
		vm.pointLat = undefined;
		vm.daySelected = undefined;
		vm.timeSelected = undefined;
		vm.dataSectionSelected = undefined;
		
		var hours = [];
	    for (var i = 0; i <= 23; i++) {
	    	hours.push(i);
	    }
	    vm.hours = hours;
	    
		//mapa
		var map = L.map( 'map', {
			  minZoom: 2,
			  zoom: 2
		});
		

		var osmLayer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
		var layerControl = L.control.layers(
				{'OpenStreetMap': osmLayer,
					'MapQuest Open': new L.TileLayer('http://otile{s}.mqcdn.com/tiles/1.0.0/map/{z}/{x}/{y}.jpg', {subdomains: '1234'}),
					'Stamen Toner Lite': new L.TileLayer('http://stamen-tiles-{s}.a.ssl.fastly.net/toner-lite/{z}/{x}/{y}.png'),
					'ESRI World Imagery': new L.TileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}')
				});
		map.addControl(layerControl);
		var scaleControl = new L.control.scale({'imperial': false});
		map.addControl(scaleControl);
		map.addLayer(osmLayer);
		
		map.fitBounds([[-180,-90],[180,90]]);

		//map.on('dragend', aplicarFiltros);
		//map.on('zoomend', aplicarFiltros);
			
		//Anadimos la edit toolbar
		// Initialise the FeatureGroup to store editable layers
		var drawnItems = new L.FeatureGroup();
		map.addLayer(drawnItems);

		// Initialise the draw control and pass it the FeatureGroup of editable layers
		var drawControl = new L.Control.Draw({
			draw: {
		        polygon: false,
		        rectangle: false,
		        circle: false,
		        marker: false
		    },
		    edit: {
		        featureGroup: drawnItems
		    }
		});
		map.addControl(drawControl);
		
		
		map.on('draw:drawstart', function (e) {
			//Cuando pulsa en el boton de comenzar a dibujar, borramos todo lo que habia en el mapa
			//porque solo queremos que haya un segmento en el mapa de cada vez
			map.removeLayer(drawnItems);
			
			drawnItems = new L.FeatureGroup();
			map.addLayer(drawnItems);
		});
		
		map.on('draw:created', function (e) {
		    var type = e.layerType,
		        layer = e.layer;

		    if (type === 'marker') {
		        layer.bindPopup('A popup!');
		    }

		    drawnItems.addLayer(layer);
		    
		    switch (vm.methodSelected) {
		    case "GET_INFORMATION_LINK":
		    	asignarCoordenadasSegmento(layer);
		    	break;
		    case "AGGREGATE_MEASUREMENT":
		    	asignarCoordenadasPunto(layer);
		    	break;
		    }
		   
		});
		
		map.on('draw:edited', function (e) {
		    var layers = e.layers;
		    //Esto realmente solo se va a ejecutar una unica vez porque solo hay una capa
		    layers.eachLayer(function (layer) {
		    	switch (vm.methodSelected) {
			    case "GET_INFORMATION_LINK":
			    	asignarCoordenadasSegmento(layer);
			    	break;
			    case "AGGREGATE_MEASUREMENT":
			    	asignarCoordenadasPunto(layer);
			    	break;
			    }
		    });
		});
		
		map.on('draw:deleted', function (e) {
			vm.previousLong = undefined;
			vm.previousLat = undefined;
			 
			vm.currentLong = undefined;
			vm.currentLat = undefined;
		});
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function change(){
			vm.changeToolbar();
			vm.changeFilter();
		}
		
		function changeToolbar(){
			
			//eliminamos el toolbar previo
			map.removeControl(drawControl);
			
			//eliminamos lo que habia pintado
			map.removeLayer(drawnItems);
			
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				// Initialise the draw control and pass it the FeatureGroup of editable layers
					drawControl = new L.Control.Draw({
						draw: {
					        polygon: false,
					        rectangle: false,
					        circle: false,
					        marker: false
					    },
					    edit: {
					        featureGroup: drawnItems
					    }
					});
			    break;
			  case "AGGREGATE_MEASUREMENT":
				  drawControl = new L.Control.Draw({
						draw: {
							polyline: false,
					        polygon: false,
					        rectangle: false,
					        marker: false
					    },
					    edit: {
					        featureGroup: drawnItems
					    }
					});
			    break;
			  default:
				  drawControl = new L.Control.Draw({
					    edit: {
					        featureGroup: drawnItems
					    }
					});
			}
			
			map.addControl(drawControl);
			
		}
		
		function changeFilter(){
			
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				vm.filtroConcreto = undefined;
			    break;
			  case "AGGREGATE_MEASUREMENT":
				  vm.filtroConcreto = "./partials/smartdriver/filtrosAggregateMeasurement.html";
			    break;
			  default:
				  vm.filtroConcreto = undefined;
			}
			
		}
		
		
		function asignarCoordenadasSegmento(layer){
			 var numPuntosSegmento = layer._latlngs.length;
			 var primerPunto = layer._latlngs[0];
			 //de todos los puntos del segmento pintado, nos quedamos con el ultimo
			 //TODO: esto deberia de ser [1] en lugar del ultimo punto si conseguimos que solo
			 //pueda indicar uno
			 var ultimoPunto = layer._latlngs[numPuntosSegmento-1];
			 
			 vm.previousLong = primerPunto.lng;
			 vm.previousLat = primerPunto.lat;
			 
			 vm.currentLong = ultimoPunto.lng;
			 vm.currentLat = ultimoPunto.lat;
			 
		}
		
		function asignarCoordenadasPunto(layer){
			vm.pointLng = layer._latlng.lng;
			vm.pointLat = layer._latlng.lat;
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		function ejecutarPeticion(){
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				  	smartDriverService.getLinkInformation(vm.currentLong, vm.currentLat, vm.previousLong, vm.previousLat).then(getLinkInformationComplete);
					
					function getLinkInformationComplete(response) {
						vm.result = response.data;
						vm.resultadoConcreto = './partials/smartdriver/resultadosInformationLink.html';
					}
					
					break;
			  case "AGGREGATE_MEASUREMENT":
				  	smartDriverService.getAggregateMeasurement(vm.typeSelected, vm.pointLat, vm.pointLng, vm.daySelected, vm.timeSelected, vm.dataSectionSelected).then(getAggregateMeasurementComplete);
					
					function getAggregateMeasurementComplete(response) {
						vm.result = response.data;
						vm.resultadoConcreto = './partials/smartdriver/resultadosAggregateMeasurement.html';
					}
					
				  break;
				  
			  default:
				 break;
			}
		}
		
		function validacionLinkInformation(){
			if (vm.previousLong === undefined || vm.previousLat === undefined || 
				vm.currentLong === undefined || vm.currentLat === undefined){
				alert($translate.instant('smartdriver.selectSegment'));
			}else{
				ejecutarPeticion();
			}
		}
		
		function validacionAggregateMeasurement(){
			var texto = "";
			if (vm.typeSelected === undefined){
				texto = texto + $translate.instant('smartdriver.selectType') + '\n';
			}else{
				if (vm.typeSelected === 'DATA_SECTION' && vm.dataSectionSelected === undefined){
					texto = texto + $translate.instant('smartdriver.selectDataSection') + '\n';
				}
			}
			
			if (vm.pointLat === undefined || vm.pointLng === undefined){
				texto = texto + $translate.instant('smartdriver.selectPoint') + '\n';
			}
			
			if (vm.daySelected === undefined){
				texto = texto + $translate.instant('smartdriver.selectDay') + '\n'; 
			}
			
			if (vm.timeSelected === undefined){
				texto = texto + $translate.instant('smartdriver.selectTime') + '\n'; 
			}
			
			if (texto !== ""){
				alert(texto);
			}else{
				ejecutarPeticion();
			}
		}

		function aplicarFiltros() {	
			//Aplicamos el filtro que corresponda
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				  	validacionLinkInformation();
					break;
					
			  case "AGGREGATE_MEASUREMENT":
				  	validacionAggregateMeasurement();
				  	break;
			}	
		}
		
		function arrancar() {
			var resultado = {
					value : "Running",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.arrancar();
		}
		
		function parar() {
			var resultado = {
					value : "Stopped",
					valueInt : 0
			};
			vm.active = resultado;
			eventsService.parar();
		}
		
		//vm.aplicarFiltros();
		 

	}
})();