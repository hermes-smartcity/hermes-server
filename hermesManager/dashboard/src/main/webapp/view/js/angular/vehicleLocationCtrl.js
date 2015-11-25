//var vehicleLocationApp = angular.module('vehicleLocationApp', ['ngRoute']);
//var vehicleLocationApp = angular.module('vehicleLocationApp', []);


app.controller('FiltrosController', [ '$scope', '$http',
                                     
	function($scope, $http) {	
	
		var urlGetEvensType = "../vehiclelocation/json/eventsType";
		$http.get(urlGetEvensType).success(function(data) {
			$scope.eventsType = data;
	
		});
		
		var urlGetUsuarios = "../vehiclelocation/json/usuarios";
		$http.get(urlGetUsuarios).success(function(data) {
			$scope.usuarios = data;
	
		});
		
		$scope.getVehicleLocations = function() {
			var idUsuario = getUrlParameter("idUsuario");
			/*var urlGet = "json/vehicleLocations";
			if(idUsuario != null)
				urlGet = "json/vehicleLocationsByUsuario?"+"idUsuario="+idUsuario;*/
			var map = L.map('map');
			
			var locations = L.layerGroup([]);
			map.addLayer(locations);
			
			map.fitBounds([
			               [ -180, -90],
			               [180,  90]
			           ]);
			L.tileLayer('http://{s}.tile.osm.org/{z}/{x}/{y}.png?{foo}', {foo: 'bar'}).addTo(map);
			onChangeBounds();
//			recuperarEventos(urlGet);
			
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
			
				var urlGet = "json/vehicleLocationsByBounds?wnLng="+wnLng+"&wnLat="+wnLat+"&esLng="+esLng+"&esLat="+esLat;
				if(idUsuario != null)
					urlGet = "json/vehicleLocationsByUsuarioByBounds&bounds="+poligono+"?idUsuario="+idUsuario;
				
				recuperarEventos(urlGet);
			}
			
			function recuperarEventos(urlGet) {
				var mystyles = {
					    color: 'red',
					    fillOpacity: 0.1
					};
				
				$http.get(urlGet).success(function(data) {
					$scope.vehicleLocations = data;
										
					var i = 0;
//					//Este es asincrono, se cargan a la vez, no espera q se cargue el anterior
					//Recorro el array geojson de cada uno de los vehicleLocations
					angular.forEach($scope.vehicleLocations, function(value, key) {
						//Parseo la fecha
						var timestamp = value.timestamp;
						var date = new Date(timestamp);
						var datevalues = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear();
						$scope.bdatetime = datevalues;
						//Convierto el punto que quiero pintar para tener su lat y log
						var latlng = L.latLng(value.position.coordinates[1], value.position.coordinates[0]);

						//Añado al mapa el punto
						var circle = L.circle(latlng, 5, mystyles).addTo(locations).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime);
//						L.marker(latlng).addTo(map).bindPopup('EventId: '+value.eventId+' Fecha: '+$scope.bdatetime);
					});
				});
			}
			
		}
		
	} ]);