(function() {
	'use strict';

	angular.module('app').controller('JobModalController', JobModalController);

	JobModalController.$inject = ['$scope', '$uibModalInstance', 'jobForm', 'infoJob', 
	                              'jobService', 'SweetAlert', '$translate'];

	function JobModalController($scope, $uibModalInstance, jobForm, infoJob, jobService, SweetAlert, $translate) {
	
		$scope.form = {};
		
		$scope.nwLng = undefined;
		$scope.nwLat = undefined;
		$scope.seLng = undefined;
		$scope.seLat = undefined;
			
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
			if (infoJob !== null){
				//Pintamos en el mapa
				$scope.pintarPoligono(markers, map, infoJob.data.bbox.coordinates);
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
		
		//Despues de renderizar el mapa Si infoJob no es undefined, es porque estamos editando
		if (infoJob !== null){
			//Asignamos los valores
			$scope.name = infoJob.data.name;
			$scope.nwLng = infoJob.data.bbox.coordinates[0][0][0];
			$scope.nwLat = infoJob.data.bbox.coordinates[0][0][1];
			$scope.seLng = infoJob.data.bbox.coordinates[0][2][0];
			$scope.seLat = infoJob.data.bbox.coordinates[0][2][1];
			
			//Titulo
			$scope.tituloPagina = $translate.instant('job.edit');
		}else{
			//Titulo
			$scope.tituloPagina = $translate.instant('job.create');
		}
		
		$scope.submitForm = function () {
	        if ($scope.form.jobForm.$valid) {
	            
	        	//Nos aseguramos que haya indicado un rectangulo
	        	if ($scope.nwLng === undefined || $scope.nwLat === undefined || 
	        			$scope.seLng === undefined || $scope.seLat === undefined){
	        		SweetAlert.swal($translate.instant('job.selectBbox'));
				}else{
										
					if (infoJob === null){
		            	
		            	var jobNueva = {id: null,
		            			name: $scope.form.jobForm.name.$viewValue,
		            			seLng: $scope.seLng,
		            			seLat: $scope.seLat, 
		            			nwLng: $scope.nwLng,
		            			nwLat: $scope.nwLat
		            			};	
	     
		            	jobService.register(jobNueva).then(function(response){
					         $uibModalInstance.close(response.data);
					     });	
		            }else{
		            	//es una actualizacion
		            	var jobEditar = {id: infoJob.data.id, 
		            			name: $scope.form.jobForm.name.$viewValue, 
		            			seLng: $scope.seLng,
		            			seLat: $scope.seLat, 
		            			nwLng: $scope.nwLng,
		            			nwLat: $scope.nwLat
		            			};	
		        		
		            	jobService.edit(jobEditar).then(function(response){
					        $uibModalInstance.close(response.data);
					     });
		        		
		            }
		            
				}
				
	        	
	            
	        } else {
	            console.log('jobForm is not in scope');
	        }
	    };

	    $scope.cancel = function () {
	        $uibModalInstance.dismiss('cancel');
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
	  

		$uibModalInstance.closed.then(function() {
		    alert('modal has closed');
		});
		
		
	}
})();