(function() {
	'use strict';

	angular.module('app').controller('SmartDriverController', SmartDriverController);

	SmartDriverController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', 'eventsService', 'methods',
	                                'measurementsType', 'totalMUsers', 'totalWebUsers', 
	                                'numberActiveUsers', 'eventsToday', 
	                                'eventoProcesado' ,'totalL', 'totalDS', 'totalM', 'totalDF', 
	                                'totalSTD', 'totalSLD', 'totalHRD', 'totalCD', 'smartDriverService'];

	function SmartDriverController($scope, $filter, $http, $translate, $state, 
			$rootScope, eventsService,methods, measurementsType, totalMUsers, totalWebUsers, 
			numberActiveUsers, eventsToday, eventoProcesado, totalL, totalDS, totalM, totalDF, 
			totalSTD, totalSLD, totalHRD, totalCD, smartDriverService) {
	
		var vm = this;
		
		vm.aplicarFiltros = aplicarFiltros;
				
		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.methodSelected = "GET_INFORMATION_LINK";
		
		vm.methods = methods;
		vm.measurementsType = measurementsType;
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
		    
		    //Asignamos las coordenadas seleccionadas
		    asignarCoordenadasSegmento(layer);
		});
		
		map.on('draw:edited', function (e) {
		    var layers = e.layers;
		    //Esto realmente solo se va a ejecutar una unica vez porque solo hay una capa
		    layers.eachLayer(function (layer) {
		    	asignarCoordenadasSegmento(layer);
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
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		// Preparar la url que va a llamar al controlador
		function prepararUrl(){
			var url = "";
			
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				  url = url_network_link;
				 
				  url+="currentLong="+vm.currentLong+"&currentLat="+vm.currentLat+"&previousLong="+vm.previousLong+"&previousLat="+vm.previousLat;
				  
			    break;
			 
			  default:
				  url = url_network_link;
			}
			
			return url;
		}
		
		function ejecutarPeticion(){
			switch (vm.methodSelected) {
			  case "GET_INFORMATION_LINK":
				  	smartDriverService.getLinkInformation(vm.currentLong, vm.currentLat, vm.previousLong, vm.previousLat).then(getLinkInformationComplete);
					
					function getLinkInformationComplete(response) {
						vm.result = data;
					}
					
			    break;
			 
			  default:
				 break;
			}
		}
		
		function aplicarFiltros() {	
			
			if (vm.previousLong === undefined || vm.previousLat === undefined || 
				vm.currentLong === undefined || vm.currentLat === undefined){
				alert($translate.instant('smartdriver.selectSegment'));
			}else{
				
				ejecutarPeticion();
				
				/*var url = prepararUrl();
				
				$http.get(url).success(function(data) {
					vm.result = data;
				});*/	
				
				
				
			}
			
		}
		
	
		
		function arrancar() {
			eventsService.arrancar();
			$state.go('dashboard');
		}
		
		function parar() {
			eventsService.parar();
			$state.go('dashboard');
		}
		
		//vm.aplicarFiltros();
		 

	}
})();