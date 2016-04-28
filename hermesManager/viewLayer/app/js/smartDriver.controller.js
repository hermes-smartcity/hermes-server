(function() {
	'use strict';

	angular.module('app').controller('SmartDriverController', SmartDriverController);

	SmartDriverController.$inject = ['$scope', '$filter', '$http', '$translate', 
	                                '$state', '$rootScope', 'eventsService', 'services', 
	                                'types', 'dataSections', 'eventsToday', 
	                                'eventoProcesado', 'statistics', 'smartDriverService',
	                                'DTOptionsBuilder', 'DTColumnBuilder', '$q'];

	function SmartDriverController($scope, $filter, $http, $translate, $state, 
			$rootScope, eventsService, services, types, dataSections, eventsToday, eventoProcesado, 
			statistics, smartDriverService, DTOptionsBuilder, DTColumnBuilder, $q) {
	
		var vm = this;
		
		vm.aplicarFiltros = aplicarFiltros;
				
		// Inicializamos el filtro de error type para que inicialmente liste warning
		vm.serviceSelected = undefined;
		vm.methodSelected = undefined;
		
		vm.services = services;
		vm.types = types;
		vm.dataSections = dataSections;
		vm.eventsToday = eventsToday;
		vm.eventoProcesado = eventoProcesado;
		
		vm.totalMUsers = statistics.contarUsuariosMovil;
		vm.totalWebUsers = statistics.contarUsuariosWeb;
		vm.numberActiveUsers = statistics.numberActiveUsers;
		vm.totalL = statistics.totalVLocations;	
		vm.totalDS = statistics.totalDataScts;
		vm.totalM = statistics.totalMeasurements;
		vm.totalDF = statistics.totalDriversF;
		vm.totalSTD = statistics.totalStepsData;
		vm.totalSLD = statistics.totalSleepData;
		vm.totalHRD = statistics.totalHeartRateData;
		vm.totalCD = statistics.totalContextData;
		
		vm.arrancar = arrancar;
		vm.parar = parar;
		
		vm.previousLong = undefined;
		vm.previousLat = undefined;
		vm.currentLong = undefined;
		vm.currentLat = undefined;
		
		vm.fromLng = undefined;
		vm.fromLat = undefined;
		vm.toLng = undefined;
		vm.toLat = undefined;
				
		vm.changeMethod = changeMethod;
		vm.change = change;
		vm.changeToolbar = changeToolbar;
		vm.changeFilter = changeFilter;
		vm.limpiarParametrosMapa = limpiarParametrosMapa;
				
		vm.filtroConcreto = undefined;
		vm.resultadoConcreto = undefined;
		
		//Filtros de la seccion aggregateMeasurement
		vm.typeSelected = undefined;
		vm.pointLng = undefined;
		vm.pointLat = undefined;
		vm.daySelected = undefined;
		vm.timeSelected = undefined;
		vm.dataSectionSelected = undefined;
		
		vm.mostrarMapa = mostrarMapa;
		vm.mostrarTabla = mostrarTabla;
		vm.showMap = true;
		vm.showTab = false;
		vm.activeInput = $translate.instant('smartdriver.mapa');
		vm.tabla = undefined;
		
		vm.pintarLineas = pintarLineas;
		vm.datosPromise = datosPromise;
		vm.cargarListadoTabla = cargarListadoTabla;
		
		var hours = [];
	    for (var i = 0; i <= 23; i++) {
	    	hours.push(i);
	    }
	    vm.hours = hours;
	    
	    //Inicializar options de la tabla
		vm.dtInstance = null;

		//vm.dtOptions = DTOptionsBuilder.newOptions().withLanguageSource("./translations/datatables-locale_en.json").fromFnPromise(datosPromise);
		vm.dtOptions = DTOptionsBuilder.fromFnPromise(datosPromise);

		vm.dtInstanceCallback = function(_dtInstance){
			vm.dtInstance = _dtInstance;
		};

		vm.dtColumns  = [
		                   DTColumnBuilder.newColumn('linkId').withTitle($translate.instant('smartdriver.linkId')),
		                   DTColumnBuilder.newColumn('maxSpeed').withTitle($translate.instant('smartdriver.maxSpeed')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('linkName').withTitle($translate.instant('smartdriver.linkName')),
		                   DTColumnBuilder.newColumn('linkType').withTitle($translate.instant('smartdriver.linkType')),
		                   DTColumnBuilder.newColumn('length').withTitle($translate.instant('smartdriver.length')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   }),
		                   DTColumnBuilder.newColumn('cost').withTitle($translate.instant('smartdriver.cost')).renderWith(function(data, type, full) {
		                	   return $filter('number')(data, 2);   
		                   })
		                   ];
		
		function cargarListadoTabla(){
			vm.tabla = "./partials/smartdriver/tabla.html";
		}

		function datosPromise(){
			var dfd = $q.defer();		
			dfd.resolve(vm.events);

			return dfd.promise;
		}
		
		function mostrarMapa() {	
			vm.showMap = true;
			vm.showTab = false;
			vm.activeInput = $translate.instant('smartdriver.mapa');

			//Para evitar que se carguen las tablas de la parte Table
			vm.tabla = undefined;
		}

		function mostrarTabla() {	
			vm.showMap = false;
			vm.showTab = true;
			vm.activeInput = $translate.instant('smartdriver.tabla');

			vm.cargarListadoTabla();
		}


		
		//mapa
		var map = L.map( 'map', {
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
				}, {'Events': markers});
		map.addControl(layerControl);
		var scaleControl = new L.control.scale({'imperial': false});
		map.addControl(scaleControl);
		map.addLayer(osmLayer);
		map.addLayer(markers);
		
		map.fitBounds([[-180,-90],[180,90]]);

		//map.on('dragend', aplicarFiltros);
		//map.on('zoomend', aplicarFiltros);
			
		//Anadimos la edit toolbar
		// Initialise the FeatureGroup to store editable layers
		var drawnItems = new L.FeatureGroup();
		map.addLayer(drawnItems);	

		//Extendemos los dos circle para pintar los puntos de COMPUTE_ROUTE
		L.Draw.CircleOrigin = L.Draw.SimpleShape.extend({
			statics: {
				TYPE: 'circleOrigin'
			},

			options: {
				shapeOptions: {
					stroke: true,
					color: '#F30C28',
					weight: 4,
					opacity: 0.5,
					fill: true,
					fillColor: null, //same as color by default
					fillOpacity: 0.2,
					clickable: true
				}
			},

			initialize: function (map, options) {
				// Save the type so super can fire, need to do this as cannot do this.TYPE :(
				//this.type = L.Draw.Circle.TYPE;
				this.type = 'circleOrigin';

				L.Draw.SimpleShape.prototype.initialize.call(this, map, options);
			},

			_initialLabelText: $translate.instant('smartdriver.clickDrag'),

			_drawShape: function (latlng) {
				if (!this._shape) {
					this._shape = new L.Circle(this._startLatLng, this._startLatLng.distanceTo(latlng), this.options.shapeOptions);
					this._map.addLayer(this._shape);
				} else {
					this._shape.setRadius(this._startLatLng.distanceTo(latlng));
				}
			},

			_fireCreatedEvent: function () {
				var circle = new L.Circle(this._startLatLng, this._shape.getRadius(), this.options.shapeOptions);
				L.Draw.SimpleShape.prototype._fireCreatedEvent.call(this, circle);
			},

			_onMouseMove: function (e) {
				var latlng = e.latlng,
					radius;

				this._tooltip.updatePosition(latlng);
				if (this._isDrawing) {
					this._drawShape(latlng);

					// Get the new radius (rouded to 1 dp)
					radius = this._shape.getRadius().toFixed(1);

					this._tooltip.updateContent({
						text: $translate.instant('smartdriver.releaseMouse'),
						subtext: $translate.instant('smartdriver.radius') + radius + ' m'
					});
				}
			}
		});

		L.Draw.CircleDestiny = L.Draw.SimpleShape.extend({
			statics: {
				TYPE: 'circleDestiny'
			},

			options: {
				shapeOptions: {
					stroke: true,
					color: '#0C0CF3',
					weight: 4,
					opacity: 0.5,
					fill: true,
					fillColor: null, //same as color by default
					fillOpacity: 0.2,
					clickable: true
				}
			},

			initialize: function (map, options) {
				// Save the type so super can fire, need to do this as cannot do this.TYPE :(
				this.type = 'circleDestiny';

				L.Draw.SimpleShape.prototype.initialize.call(this, map, options);
			},

			_initialLabelText: $translate.instant('smartdriver.clickDrag'),

			_drawShape: function (latlng) {
				if (!this._shape) {
					this._shape = new L.Circle(this._startLatLng, this._startLatLng.distanceTo(latlng), this.options.shapeOptions);
					this._map.addLayer(this._shape);
				} else {
					this._shape.setRadius(this._startLatLng.distanceTo(latlng));
				}
			},

			_fireCreatedEvent: function () {
				var circle = new L.Circle(this._startLatLng, this._shape.getRadius(), this.options.shapeOptions);
				L.Draw.SimpleShape.prototype._fireCreatedEvent.call(this, circle);
			},

			_onMouseMove: function (e) {
				var latlng = e.latlng,
					radius;

				this._tooltip.updatePosition(latlng);
				if (this._isDrawing) {
					this._drawShape(latlng);

					// Get the new radius (rouded to 1 dp)
					radius = this._shape.getRadius().toFixed(1);

					this._tooltip.updateContent({
						text: $translate.instant('smartdriver.releaseMouse'),
						subtext: $translate.instant('smartdriver.radius') + radius + ' m'
					});
				}
			}
		});

		
		//Inicialiazmos un control vacio
		var drawControl = new L.Control.Draw({
			draw: {
				polyline: false,
		        polygon: false,
		        rectangle: false,
		        circle: false,
		        marker: false
		    },
		    edit: false
		});
		map.addControl(drawControl);
		
		map.on('draw:drawstart', function (e) {
			//Cuando pulsa en el boton de comenzar a dibujar, borramos todo lo que habia en el mapa
			//porque solo queremos que haya un segmento o un punto en el mapa de cada vez
			//Nota: cuando estamos con COMPUTE_ROUTE no podemos borrar puesto que necesitamos
			//tener los dos puntos activos
			var type = e.layerType; //este es el tipo que vamos a pintar
			if (type === 'circleOrigin' || type === 'circleDestiny'){
				//Si esta pintando origen y ya hay otro pintado, lo borramos
				if (type === 'circleOrigin' && vm.fromLat !== undefined && vm.fromLng !== undefined){
					map.removeLayer(drawnItems);
					drawnItems = new L.FeatureGroup();
					map.addLayer(drawnItems);
					
					vm.fromLat = undefined;
					vm.fromLng = undefined;
					vm.toLat = undefined;
					vm.toLng = undefined;
				}
				
				//Si esta pintando destino y ya hay otro pintado, lo borramos
				if (type === 'circleDestiny' && vm.toLat !== undefined && vm.toLng !== undefined){
					map.removeLayer(drawnItems);
					drawnItems = new L.FeatureGroup();
					map.addLayer(drawnItems);
					
					vm.fromLat = undefined;
					vm.fromLng = undefined;
					vm.toLat = undefined;
					vm.toLng = undefined;
				}
				
			}else{
				map.removeLayer(drawnItems);
				drawnItems = new L.FeatureGroup();
				map.addLayer(drawnItems);
			}
			
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
		    
		    switch (vm.methodSelected) {
		    case "GET_INFORMATION_LINK":
		    	asignarCoordenadasSegmento(layer);
		    	break;
		    case "AGGREGATE_MEASUREMENT":
		    	asignarCoordenadasPunto(layer);
		    	break;
		    case "COMPUTE_ROUTE":
		    	asignarCoordenadasPuntoOrigenDestino(layer, type);
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
			    case "COMPUTE_ROUTE":
			    	asignarCoordenadasPuntoOrigenDestino(layer, layer.layerType);
			    	break;
			    }
		    });
		});
		
		map.on('draw:deleted', function (e) {
			limpiarParametrosMapa();
		});
		
		// Si el usuario tiene rol admin se mostrará en dashoboard el estado de event manager. Ese apartado sin embargo no lo tiene el usuario consulta
		if($rootScope.hasRole('ROLE_ADMIN')){
			eventsService.getStateActualizado().then(getStateActualizadoComplete);	
		}
		
		function limpiarParametrosMapa(){
			vm.previousLong = undefined;
			vm.previousLat = undefined;
			vm.currentLong = undefined;
			vm.currentLat = undefined;
			
			vm.pointLng = undefined;
			vm.pointLat = undefined;
			
			vm.fromLat = undefined;
			vm.fromLng = undefined;
			vm.toLat = undefined;
			vm.toLng = undefined;
		}
		
		function getStateActualizadoComplete(response) {				
			vm.active = response.data;
		}
		
		
		function changeMethod(){
			if (vm.serviceSelected !== undefined && vm.serviceSelected !== null && vm.serviceSelected !== ""){
				smartDriverService.getMethods(vm.serviceSelected).then(function(response){
					vm.methods = response.data;
				});
			}else{
				vm.methods = undefined;
			}
			
			vm.methodSelected = undefined;
			vm.change();
		}
		
		function change(){
			vm.changeToolbar();
			vm.changeFilter();
			
			limpiarParametrosMapa();
			
			vm.events = undefined;
			vm.result = undefined;
			
			markers.clearLayers();
			
			map.removeLayer(drawnItems);
			drawnItems = new L.FeatureGroup();
			map.addLayer(drawnItems);
			
			vm.showMap = true;
			vm.showTab = false;
			//Para evitar que se carguen las tablas de la parte Table
			vm.tabla = undefined;
			
		}
		
		function changeToolbar(){

			//eliminamos el toolbar previo
			map.removeControl(drawControl);

			//eliminamos lo que habia pintado
			map.removeLayer(drawnItems);

			switch (vm.methodSelected) {
			case "GET_INFORMATION_LINK":
				// Initialise the draw control and pass it the FeatureGroup of editable layers
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: true,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('smartdriver.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('smartdriver.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});

				break;
			case "AGGREGATE_MEASUREMENT":
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
						        {
						        	enabled: false,
						            handler: new L.Draw.Polygon(map, this.options.polygon),
						            title: L.drawLocal.draw.toolbar.buttons.polygon
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Rectangle(map, this.options.rectangle),
						            title: L.drawLocal.draw.toolbar.buttons.rectangle
						        },
						        {
						        	enabled: false,
						            handler: new L.Draw.Marker(map, this.options.marker),
						            title: L.drawLocal.draw.toolbar.buttons.marker
						        },
						        {
						        	enabled: true,
						            handler: new L.Draw.Circle(map, this.options.circle),
						            title: L.drawLocal.draw.toolbar.buttons.circle
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('smartdriver.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('smartdriver.drawDestinyPoint')
						        }
						        ];
					}
				});
				
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: {
						featureGroup: drawnItems
					}
				});

				break;

			case "COMPUTE_ROUTE":

				map.addLayer(drawnItems);

				//Los incluimos en el toolbar
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
								{
									enabled: false,
								    handler: new L.Draw.Polygon(map, this.options.polygon),
								    title: L.drawLocal.draw.toolbar.buttons.polygon
								},
								{
									enabled: false,
								    handler: new L.Draw.Rectangle(map, this.options.rectangle),
								    title: L.drawLocal.draw.toolbar.buttons.rectangle
								},
								{
									enabled: false,
								    handler: new L.Draw.Marker(map, this.options.marker),
								    title: L.drawLocal.draw.toolbar.buttons.marker
								},
								{
									enabled: false,
								    handler: new L.Draw.Circle(map, this.options.circle),
								    title: L.drawLocal.draw.toolbar.buttons.circle
								},
						        {
						        	enabled: true,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('smartdriver.drawOriginPoint')
						        },
						        {
						        	enabled: true,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('smartdriver.drawDestinyPoint')
						        }
						        ];
					}
				});

				//El draw que ya existe lo usaremos para el punto de origen
				drawControl = new L.Control.Draw({
					draw: {
					},
					edit: false
				});

				
				break;

			default:
				L.DrawToolbar.include({
					getModeHandlers: function (map) {
						return [
								{
									enabled: false,
								    handler: new L.Draw.Polyline(map, this.options.polyline),
								    title: L.drawLocal.draw.toolbar.buttons.polyline
								},
								{
									enabled: false,
								    handler: new L.Draw.Polygon(map, this.options.polygon),
								    title: L.drawLocal.draw.toolbar.buttons.polygon
								},
								{
									enabled: false,
								    handler: new L.Draw.Rectangle(map, this.options.rectangle),
								    title: L.drawLocal.draw.toolbar.buttons.rectangle
								},
								{
									enabled: false,
								    handler: new L.Draw.Marker(map, this.options.marker),
								    title: L.drawLocal.draw.toolbar.buttons.marker
								},
								{
									enabled: false,
								    handler: new L.Draw.Circle(map, this.options.circle),
								    title: L.drawLocal.draw.toolbar.buttons.circle
								},
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleOrigin(map, this.options.circleOrigin),
						        	title: $translate.instant('smartdriver.drawOriginPoint')
						        },
						        {
						        	enabled: false,
						        	handler: new L.Draw.CircleDestiny(map, this.options.circleDestiny),
						        	title: $translate.instant('smartdriver.drawDestinyPoint')
						        }
						        ];
					}
				});
			
				drawControl = new L.Control.Draw({
					draw: {
				    },
				    edit: false
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
			case "COMPUTE_ROUTE":
				vm.filtroConcreto = undefined;
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
		
		function asignarCoordenadasPuntoOrigenDestino(layer, type){
			if (type === 'circleOrigin'){
				vm.fromLng = layer._latlng.lng;
				vm.fromLat = layer._latlng.lat;
			}else if (type === 'circleDestiny'){
				vm.toLng = layer._latlng.lng;
				vm.toLat = layer._latlng.lat;	
			}
			
		}
		
		function infoPopup(linkId, maxSpeed, linkName, linkType, length, cost) {
			
			var maxSpeedEvento = "";
			if (maxSpeed !== null){
				maxSpeedEvento = $filter('number')(maxSpeed, 2);	
			}
			
			var lenghtEvento = "";
			if (length !== null){
				lenghtEvento = $filter('number')(length, 2);	
			}
			
			var costEvento = "";
			if (cost !== null){
				costEvento = $filter('number')(cost, 2);	
			}
			
			var datosEvento = L.DomUtil.create('datosEvento');

			datosEvento.innerHTML = '<b>' + $translate.instant('smartdriver.linkId') + ':</b> ' + linkId +
				'<br/><b>' + $translate.instant('smartdriver.maxSpeed') + ':</b> '+maxSpeedEvento+
				'<br/><b>' + $translate.instant('smartdriver.linkName') + ':</b> '+linkName+
				'<br/><b>' + $translate.instant('smartdriver.linkType') + ':</b> '+linkType +
				'<br/><b>' + $translate.instant('smartdriver.length') + ':</b> '+lenghtEvento +
				'<br/><b>' + $translate.instant('smartdriver.cost') + ':</b> '+costEvento;
			
			return datosEvento;
		}
		
		function pintarLineas(events) {
			markers.clearLayers();
			angular.forEach(events, function(value, key) {			
				var info = infoPopup(value.linkId, value.maxSpeed, value.linkName, value.linkType,
									 value.length, value.cost);

				//Almaceno array de puntos 
				var myLines = value.geom_way;
				var myStyle = {
						"color": "#ff7800",
						"weight": 4,
						"opacity": 0.65
				};
				var myLayer = L.geoJson().addTo(map);
				L.geoJson(myLines, {
					style: myStyle
				}).addTo(map).addTo(markers).bindPopup(info);

			});
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
				
			  case "COMPUTE_ROUTE":
				  smartDriverService.getComputeRoute(vm.fromLat, vm.fromLng, vm.toLat, vm.toLng).then(getComputeRouteComplete);
					
					function getComputeRouteComplete(response) {
						
						vm.resultadoConcreto = undefined;
						
						vm.events = response.data;
						pintarLineas(vm.events);

						vm.cargarListadoTabla();
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
		
		function validacionComputeRoute(){
			var texto = "";
			if (vm.fromLng === undefined || vm.fromLat === undefined){
				texto = texto + $translate.instant('smartdriver.selectOriginPoint') + '\n';
			}
			
			if (vm.toLng === undefined || vm.toLat === undefined){
				texto = texto + $translate.instant('smartdriver.selectDestinyPoint') + '\n';
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
				  	
			  case "COMPUTE_ROUTE":
				  validacionComputeRoute();
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