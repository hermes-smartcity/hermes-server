var dataSectionApp = angular.module('dataSectionApp', ['ngRoute']);
var dataSectionApp = angular.module('dataSectionApp', []);


dataSectionApp.controller('DataSectionsController', [ '$scope', '$http',
                                     
	function($scope, $http) {
	
		$scope.getDataSections = function() {
			var idUsuario = getUrlParameter("idUsuario");
//			var urlGet = "json/dataSections";
//			if(idUsuario != null)
//				urlGet = "json/dataSectionsByUsuario?"+"idUsuario="+idUsuario;

			var map = L.map('map');
			var locations = L.layerGroup([]);
			map.addLayer(locations);
			
			map.fitBounds([
			               [ -180, -90],
			               [180,  90]
			           ]);
			L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
			
//			recuperarEventos(urlGet);
			onChangeBounds();
			map.on('dragend', onChangeBounds);
			map.on('zoomend', onChangeBounds);
			
			function onChangeBounds() {
				// Borramos los puntos cargamos con anterioridad
				locations.clearLayers();
				var bounds = map.getBounds();				
				var esLng = bounds.getSouthEast().lng;
				var esLat = bounds.getSouthEast().lat;
				var wnLng = bounds.getNorthWest().lng;
				var wnLat = bounds.getNorthWest().lat;
				var urlGet = "json/dataSectionsByBounds?wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;
				if(idUsuario != null)
					urlGet = "json/dataSectionsByUsuarioByBounds&bounds="+poligono+"?idUsuario="+idUsuario;
				recuperarEventos(urlGet);

			}
			
			function recuperarEventos(urlGet) {
				$http.get(urlGet).success(function(data) {
					$scope.dataSections = data;
			
					//Recorro el array geojson de cada uno de los dataSections
					angular.forEach($scope.dataSections, function(value, key) {
						//Parseo la fecha
						var timestamp = value.timestamp;
						var date = new Date(timestamp);
						var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
						$scope.bdatetime = datevalues;
						//Almaceno array de puntos 
						var myLines = value.roadSection;
						var myStyle = {
							    "color": "#ff7800",
							    "weight": 4,
							    "opacity": 0.65
							};
						var myLayer = L.geoJson().addTo(map);
						L.geoJson(myLines, {
						    style: myStyle
						}).addTo(map).addTo(locations).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime);
						/**/
					});
				});
				
			}
			
			
		}
		
	} ]);