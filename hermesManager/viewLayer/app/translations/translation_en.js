(function() {
	'use strict';

	angular.module('app').config(translateConfig);

	translateConfig.$inject = ['$translateProvider'];

	function translateConfig($translateProvider) {

		$translateProvider.translations('en', {
			"entrar": "Enter",
			"recordar": "Remember me",
			"registrarse": "Register",
			"password": "Password",
			"email": "Email",
			"borrar": "Delete",
			"volver": "Back",
			"editar": "Edit",
			"aceptar": "Ok",
			"cancelar": "Cancel",
			"emailPasswordIncorrectos": "Incorrect Email/password",
			"en": " on ",
			"falloConEstado": " failed with status ",
			"numeroEventos": "Events number",
			"numeroPeticiones": "Request number",
			"noResults": "There are no results",
			"noEvents": "There are not events to show",
			"noData": "There are no data",
			"confirmDelete": "Are you sure?",
			"textDelete": "Your will not be able to recover this item?",
			"back": "BACK",
			"contextData":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"detectedActivity": "Detected activity",
				"accuracy": "Accuracy"
			},
			"dashboard":{
				"titulo": "Dashboard",
				"mapa": "Map",
				"tabla": "Table",
				"heatMap": "Heat Map",
				"dbconcept": "DBConcept",
				"noFeatures": "No features in the selected DBConcept"
			},
			"dataSection":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"minimimSpeed": "Minimum Speed",
				"maximumSpeed": "Maximum Speed",
				"medianSpeed": "Median Speed",
				"averageSpeed": "Average Speed",
				"stdDevSpeed": "Std. Dev. Speed",
				"averageAcceleration": "Average Acceleration",
				"averageDeceleration": "Average Decceleration",
				"highAcceleration": "High Acceleration",
				"highDecceleration": "Hig Decceleration",
				"averageHeartRate": "Average Heart Rate",
				"stdDevHeartRate": "Std. Dev. Heart Rate",
				"minimum": "Minimum",
				"maximum": "Maximum",
				"median": "Median",
				"acceleration": "Acceleration",
				"hearRate": "Heart rate",
				"average": "Average",
				"stdDev": "Std. Dev.",
				"speed": "Speed"
			},
			"measurement":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"value": "Value",
				"speed": "Speed",
				"accuracy": "Accuracy",
				"type": "Type"
			},
			"settings": {
				"titulo": "Settings",
				"numberError": "Only numbers",
				"required": "This field is required",
				"updateOk": "Settings updated"
			},
			"systemLogs": {
				"titulo": "System logs",
				"filtros": "Filters",
				"tipoError": "Error Type:",
				"info": "INFO",
				"aviso": "WARNING",
				"error": "ERROR",
				"fechaInicio": "Start date:",
				"fechaFin": "End date:",
				"filtrar": "Filter",
				"cambiar": "Change",
				"noEvents": "There are not events to show",
				"idLog": "idLog",
				"dated": "Dated",
				"message": "Message"
			},
			"menu":{
				"perfilUsuario": "User Profile",
				"ajustes": "Settings",
				"cambiarContrasena": "Change password",
				"logout": "Logout",
				"hermesManager": "Hermes Manager",
				"toggleNavigation": "Toggle navigation",
				"dashboard": "Dashboard",
				"eventManager": "Event Manager",
				"userManager": "User Manager",
				"dataServices": "Data Services",
				"systemLogs": "System Logs",
				"hermesServices": "Hermes Services",
				"testServices": "Test services",
				"statistics": "Statistics",
				"dbConnections": "DB Connections",
				"dbConcepts": "DB Concepts",
				"osmConcepts": "OSM Concepts",
				"job": "Job",
				"administration": "Administration",
				"osmImport": "OSM Import",
				"jobs": "Jobs",
				"executions": "Executions",
				"importShapefiles": "Import shapefiles"
			},
			"filtros": {
				"titulo": "Filters",
				"todos": "Everyone",
				"usuario": "User:",
				"tipoEvento": "Event Type:",
				"fechaInicio": "Start date:",
				"fechaFin": "End date:",
				"cambiar": "Change",
				"filtrar": "Filter",
				"servicios": "Services",
				"operaciones": "Type of operations",
				"chooseService": "Choose a service first",
				"tipoSensor": "Sensor Type: ",
				"chooseOne": "Choose one",
				"measurementType": "Measurement type",
				"speedFactor": "Speed factor"
			},
			"resultados": {
				"titulo": "Results",
				"de": " of "
			},
			"estadisticas":{
				"eventManager": "Event Manager:",
				"eventsToday": "Events today:",
				"lastEvent": "Last event:",
				"iniciar": "Start",
				"parar": "Stop",
				"totalMobileUsers": "Total mobile users: ",
				"activeUsers": "Active users: ",
				"totalWebUsers": "Total web users:",
				"locations": "Locations:",
				"measurements": "Measurements:",
				"dataSections": "Data sections:",
				"driverFeatures": "Driver features:",
				"stepsData": "Steps Data:",
				"sleepData": "Sleep Data:",
				"heartRateData": "Heart Rate Data:",
				"contextData": "Context Data:",
				"viewDetails": "View Details",
				"userLocations": "User Locations",
				"userActivities": "User Activities",
				"userDistances": "User Distances",
				"userSteps": "User Steps",
				"userCaloriesExpended": "User Calories Expended",
				"userHeartRates": "User Heart Rates",
				"userSleep": "User Sleep",
				"fitBit": "Fit Bit",
				"googleFit": "Google Fit"
			},
			"vehicleLocation":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"speed": "Speed",
				"rr": "RR",
				"accuracy": "Accuracy"
			},
			"user":{
				"completarRegistro": "Complete record",
				"activarCuenta": "Activate account",
				"noAdminRegistrados": "There are no registered administrators",
				"email": "Email",
				"movil": "Mobile user",
				"acciones": "Actions",
				"cambiarContrasena": "Change Password",
				"viejaContrasena": "Old Password:",
				"nuevaContrasena": "New Password:",
				"repetirContrasena": "Repeat new Password:",
				"cambiar": "Change",
				"editar": "Edit",
				"rol": "Role",
				"contrasena": "Password",
				"usuario": "User",
				"todos": "Everyone",
				"registro": "Record",
				"registrar": "Register",
				"noUsuariosRegistrados": "There are no registered users",
				"editarUsuario": "Edit user",
				"eliminarUsuario": "Delete user",
				"usuariosWeb": "Web Users",
				"usuariosMoviles": "Mobiles Users",
				"verDetalles": "View Details",
				"future": "Future",
				"usuarios": "Users",
				"administradores": "Admins",
				"buscar": "Search...",
				"perfilUsuario": "User Profile",
				"oldPasswordNotCorrect": "The old password is not correct",
				"passwordsNotSame": "New passwords are not the same",
				"passwordOk": "Password changed satisfactory"
			},
			"hermesServices":{
				"filtros": "Filters",
				"filtrar": "Filter",
				"methods": "Methods",
				"results": "Results",
				"noResults": "There are no results",
				"linkId": "Identifier: ",
				"maxSpeed": "Maximum Speed: ",
				"linkName": "Name: ",
				"linkType": "Type: ",
				"position": "Position: ",
				"direction": "Direction: ",
				"length": "Length: ",
				"previousposition": "Previous position: ",
				"selectSegment": "Select a segment",
				"types": "Types",
				"time": "Time",
				"day": "Day",
				"selectType": "Select type",
				"selectPoint": "Select point",
				"selectDay": "Select day",
				"selectTime": "Select time",
				"lunes": "Monday",
				"martes": "Tuesday",
				"miercoles": "Wednesday",
				"jueves": "Thursday",
				"viernes": "Friday",
				"sabado": "Saturday",
				"domingo": "Sunday",
				"selectDataSection": "Select Data Section",
				"datasection": "Data Section",
				"numberOfValues": "Number of values: ",
				"maximium": "Maximum ",
				"minimium": "Minimum: ",
				"average": "Average: ",
				"standardDeviation": "Standard Deviation: ",
				"selectOriginPoint": "Select origin point",
				"selectDestinyPoint": "Select destiny point",
				"clickDrag": "Click and drag to draw circle.",
				"releaseMouse": "Release mouse to finish drawing.",
				"radius": "Radius: ",
				"drawOriginPoint": "Draw origin point",
				"drawDestinyPoint": "Draw destiny point",
				"mapa": "Map",
				"tabla": "Table",
				"cost": "Cost: ",
				"noData": "There are not data to show",
				"selectDates": "Select dates",
				"selectRectangle": "Select rectangle",
				"selectMeasurementType": "Select measurement type",
				"speedFactor": "Speed factor compulsory",
				"speedFactorNoNumber": "Speed factor has to be a positive number"
			},
			"userLocation":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"accuracy": "Accuracy"
			},
			"userActivity":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"name": "Name"
			},
			"driverFeatures":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"awakefor": "Awake for",
				"inbed": "In bed",
				"workingtime": "Working time",
				"lightsleep": "Light Sleep",
				"deepsleep": "Deep Sleep",
				"previousstress": "Previous Stress",
				"timestamp": "Time"
			},
			"sleepData":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"awakenings": "Awakenings",
				"minutesasleep": "Minutes a sleep",
				"minutesinbed": "Minutes in bed",
				"starttime": "Start time",
				"endtime": "End time",
				"eventid": "Event Id"
			},
			"stepData":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"steps": "Steps",
				"timelog": "Time log",
				"eventid": "Event Id"
			},
			"heartRateData":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"eventid": "Event id",
				"timelog": "Time log",
				"heartrate": "Heart rate"
			},
			"dbconnection":{
				"id": "ID",
				"name": "Name",
				"type": "Type",
				"host": "Host",
				"port": "Port",
				"dbname": "DB name",
				"userDb": "User",
				"passDb": "Password",
				"actions": "Actions",
				"noData": "There are not data to show",
				"create": "Create new DB Connection",
				"edit": "Edit DB Connection",
				"nameRequired": "Name required",
				"hostRequired": "Host required",
				"portRequired": "Port required",
				"dbnameRequired": "DB Name required",
				"typeRequired": "Type required",
				"userDbRequired": "User required",
				"passDbRequired": "Password required",
				"portFormat": "Port format incorrect. Use 4 numbers"
			},
			"dbconcept":{
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"schemaName": "Schema name",
				"tableName": "Table name",
				"idName": "Id name",
				"osmIdName": "OSMId name",
				"geomName": "Geom name",
				"manageAttributes": "Manage attributes",
				"dbconnection": "DBConnection",
				"create": "Create new DB Concept",
				"edit": "Edit DB Concept",
				"nameRequired": "Name required",
				"tableNameRequired": "Table name required",
				"osmIdNameRequired": "OSMId name required",
				"idNameRequired": "Id name required",
				"geomNameRequired": "Geom name required",
				"dbConnectionRequired": "DB Connection required"
			},
			"dbattribute":{
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"type": "Type",
				"create": "Create new DB Attribute",
				"edit": "Edit DB Attribute",
				"nameRequired": "Name required",
				"typeRequired": "Type required",
			},
			"osmconcept":{
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"manageFilters": "Manage filters",
				"manageAttributes": "Manage attributes",
				"create": "Create new OSM Concept",
				"edit": "Edit OSM Concept",
				"nameRequired": "Name required",
			},
			"osmattribute":{
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"create": "Create new OSM Attribute",
				"edit": "Edit OSM Attribute",
				"nameRequired": "Name required",
			},
			"osmfilter":{
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"operation": "Operation",
				"value": "Value",
				"create": "Create new OSM Filter",
				"edit": "Edit OSM Filter",
				"nameRequired": "Name required",
				"operationRequired": "Operation required",
				"valueRequired": "Value required",
			},
			"job":{
				"titulo": "Job",
				"actions": "Actions",
				"id": "ID",
				"name": "Name",
				"create": "Create new Job",
				"edit": "Edit Job",
				"nameRequired": "Name required",
				"bboxRequired": "Bbox required",
				"selectBbox": "Select bbox",
				"manageConceptTransformation": "Manage concept transformation",
				"executeJob": "Execute Job"
			},
			"concepttransformation":{
				"actions": "Actions",
				"id": "ID",
				"create": "Create new Concept Transformation",
				"edit": "Edit Concept Transformation",
				"osmconcept": "OSM Concept",
				"dbconcept": "DB Concept",
				"osmConceptRequired": "OSM Concept required",
				"dbConceptRequired": "DB Concept required",
				"manageAttributeMapping": "Manage attribute mapping",
			},
			"attributemapping":{
				"actions": "Actions",
				"id": "ID",
				"create": "Create new Attribute Mapping",
				"edit": "Edit Attribute Mapping",
				"osmattribute": "OSM Attribute",
				"dbattribute": "DB Attribute",
				"osmAttributeRequired": "OSM Attribute required",
				"dbAttributeRequired": "DB Attribute required"
			},
			"execution":{
				"actions": "Actions",
				"id": "ID",
				"job": "Job",
				"status": "Status",
				"time": "Time"
			},
			"message":{
				"id": "ID",
				"text": "Text",
				"time": "Time"
			},
			"importShapefiles":{
				"titulo": "Import Shapefile",
				"dbconnection": "DB Connection",
				"dbconcept": "DB Concept",
				"createTable": "Create Table",
				"tableName": "Name",
				"schemaName": "Schema",
				"selectFile": "Select File",
				"selectDbConcept": "Select DB Concept or mark the check 'Create table' and write a schema/name table",
				"import": "Import",
				"dbConnectionRequired": "DB Connection required",
				"dbConceptRequired": "DB Concept required",
				"fileRequired": "File required",
				"file": "File:",
				"keepExistingData":"Keep existing data",
				"charset": "Charset",
				"selectCharset": "Select Charset",
				"chooseFile":"..."
			},
			"userDistances":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"distance": "Distance"
			},
			"userSteps":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"steps": "Steps"
			},
			"userCaloriesExpended":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"calories": "Calories"
			},
			"userHeartRates":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"bmp": "bpm"
			},
			"userSleep":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"dateStart": "Date end",
				"timeStart": "Time start",
				"dateEnd": "Date end",
				"timeEnd": "Time end",
				"name": "Name"
			},
		});
	}

})();
