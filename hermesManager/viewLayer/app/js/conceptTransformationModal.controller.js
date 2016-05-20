(function() {
	'use strict';

	angular.module('app').controller('ConceptTransformationModalController', ConceptTransformationModalController);

	ConceptTransformationModalController.$inject = ['$scope', '$uibModalInstance', 'conceptForm', 
	                                       'infoConcept', 'idJob', 'osmconcepts', 'dbconcepts', 
	                                       'conceptTransformationService', '$translate'];

	function ConceptTransformationModalController($scope, $uibModalInstance, conceptForm, infoConcept, 
			idJob, osmconcepts, dbconcepts, conceptTransformationService, $translate) {
	
		$scope.form = {};
		
		$scope.idJob = idJob;
		$scope.osmconcepts = osmconcepts;
		$scope.dbconcepts = dbconcepts;
		
		$scope.nwLng = undefined;
		$scope.nwLat = undefined;
		$scope.seLng = undefined;
		$scope.seLat = undefined;
		
		//Si infoConcept no es undefined, es porque estamos editando
		if (infoConcept !== null){
			$scope.dbconcept = infoConcept.data.dbConcept.id;
			$scope.osmconcept = infoConcept.data.osmConcept.id;
			
			if (infoConcept.data.bbox !== null){
				$scope.nwLng = infoConcept.data.bbox.coordinates[0][0][0];
				$scope.nwLat = infoConcept.data.bbox.coordinates[0][0][1];
				$scope.seLng = infoConcept.data.bbox.coordinates[0][2][0];
				$scope.seLat = infoConcept.data.bbox.coordinates[0][2][1];	
			}
			
			//Titulo
			$scope.tituloPagina = $translate.instant('concepttransformation.edit');
		}else{
			//Titulo
			$scope.tituloPagina = $translate.instant('concepttransformation.create');
		}
		
		$uibModalInstance.rendered.then(function(){
			//mapa
			var map = L.map( 'mapModal', {
				  minZoom: 2,
				  zoom: 2
			});
			
			var markers = new L.MarkerClusterGroup({
				spiderfyDistanceMultiplier: 0.5,
				disableClusteringAtZoom: 12,
				maxClusterRadius: 20
			});

			
			var osmLayer = new L.TileLayer('http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png');
			var layerControl = L.control.layers(
					{'OpenStreetMap': osmLayer,
						'MapQuest Open': new L.TileLayer('http://otile{s}.mqcdn.com/tiles/1.0.0/map/{z}/{x}/{y}.jpg', {subdomains: '1234'}),
						'Stamen Toner Lite': new L.TileLayer('http://stamen-tiles-{s}.a.ssl.fastly.net/toner-lite/{z}/{x}/{y}.png'),
						'ESRI World Imagery': new L.TileLayer('http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}')
					}, {'Jobs': markers});
			map.addControl(layerControl);
			var scaleControl = new L.control.scale({'imperial': false});
			map.addControl(scaleControl);
			map.addLayer(osmLayer);
			map.addLayer(markers);
			
			map.fitBounds([[-180,-90],[180,90]]);
			
			//Anadimos la edit toolbar
			var drawnItems = new L.FeatureGroup();
			map.addLayer(drawnItems);	
			
			//Inicialiazmos un control con polygon
			var drawControl = new L.Control.Draw({
				draw: {
					polyline: false,
			        polygon: false,
			        rectangle: true,
			        circle: false,
			        marker: false
			    },
			    edit:  {
					featureGroup: drawnItems
				}
			});
			map.addControl(drawControl);
			
			map.on('draw:drawstart', function (e) {
			
				map.removeLayer(drawnItems);
				drawnItems = new L.FeatureGroup();
				map.addLayer(drawnItems);
				
				//borramos si hubiese pintado algun marker con el recorrido
				markers.clearLayers();
				
				$scope.limpiarParametrosMapa();
				
			});
			
			map.on('draw:created', function (e) {
			    var type = e.layerType,
			        layer = e.layer;

			    if (type === 'marker') {
			        layer.bindPopup('A popup!');
			    }

			    drawnItems.addLayer(layer);
			
			    $scope.asignarCoordenadasRectangulo(layer);
			});
			
			map.on('draw:edited', function (e) {
			    var layers = e.layers;
			    //Esto realmente solo se va a ejecutar una unica vez porque solo hay una capa
			    layers.eachLayer(function (layer) {
			    	$scope.asignarCoordenadasRectangulo(layer);
			    });
			});
			
			map.on('draw:deleted', function (e) {
				$scope.limpiarParametrosMapa();
			});
			
			
			//Despues de renderizar el mapa Si infoJob no es undefined, es porque estamos editando
			if (infoConcept !== null){
				//Pintamos en el mapa
				if (infoConcept.data.bbox !== null){
					$scope.pintarPoligono(markers, map, infoConcept.data.bbox.coordinates);	
				}
			}
			
		});
		
		$scope.mapPolygon = function (poly){
			return poly.map(function(line){
				return $scope.mapLineString(line);
			});
		};
		    
		$scope.mapLineString = function (line){
			return line.map(function(d){
				return [d[1],d[0]];
			});
		};
		    
		$scope.pintarPoligono = function (markers, map, coordinates) {
			markers.clearLayers();

			var polygon = L.polygon($scope.mapPolygon(coordinates), {color: '#f00', weight:'2px'}).addTo(map).addTo(markers);
			
		};
		
		$scope.submitForm = function () {
	        if ($scope.form.conceptForm.$valid) {
	            
	        	//Recuperamos el osmConcept/dbConcept asociada al id indicado
	        	var osmConcept = $scope.recuperarOsmConcept();
	        	var dbConcept = $scope.recuperarDbConcept();
	        	
	        	
	            if (infoConcept === null){
	            	//es un alta
	            	var conceptTransformationNueva = {id: null,
	            			job: $scope.idJob,
	            			dbConcept: dbConcept, 
	            			osmConcept: osmConcept,
	            			seLng: $scope.seLng,
	            			seLat: $scope.seLat, 
	            			nwLng: $scope.nwLng,
	            			nwLat: $scope.nwLat};	
     
	            	conceptTransformationService.register(conceptTransformationNueva).then(function(response){
				         $uibModalInstance.close(response.data);
				     });	
	            }else{
	            	//es una actualizacion
	            	var conceptTransformationEditar = {id: infoConcept.data.id, 
	            			job: $scope.idJob,
	            			dbConcept: dbConcept, 
	            			osmConcept: osmConcept,
	            			seLng: $scope.seLng,
	            			seLat: $scope.seLat, 
	            			nwLng: $scope.nwLng,
	            			nwLat: $scope.nwLat};	
	        		
	            	conceptTransformationService.edit(conceptTransformationEditar).then(function(response){
				        $uibModalInstance.close(response.data);
				     });
	        		
	            }
	            
	        } else {
	            console.log('conceptForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
	    };
	    
	    $scope.recuperarOsmConcept = function () {
	    	var idOsmConceptToFind = $scope.form.conceptForm.osmconcept.$viewValue;
	    	for(var i=0; i<$scope.osmconcepts.length;i++){
	    		var osmconcept = $scope.osmconcepts[i];
	    		if (idOsmConceptToFind === osmconcept.id){
	    			return osmconcept;
	    		}
	    	}
	    };
	    
	    $scope.recuperarDbConcept = function () {
	    	var idDbConceptToFind = $scope.form.conceptForm.dbconcept.$viewValue;
	    	for(var i=0; i<$scope.dbconcepts.length;i++){
	    		var dbconcept = $scope.dbconcepts[i];
	    		if (idDbConceptToFind === dbconcept.id){
	    			return dbconcept;
	    		}
	    	}
	    };
	    
	    $scope.limpiarParametrosMapa = function(){
			$scope.nwLng = undefined;
			$scope.nwLat = undefined;
			$scope.seLng = undefined;
			$scope.seLat = undefined;
		};
				
	    $scope.asignarCoordenadasRectangulo = function (layer){
	    	$scope.nwLng = layer.getLatLngs()[0].lng;
	    	$scope.nwLat = layer.getLatLngs()[0].lat;
			
	    	$scope.seLng = layer.getLatLngs()[2].lng;
	    	$scope.seLat = layer.getLatLngs()[2].lat;
		};
	}
})();