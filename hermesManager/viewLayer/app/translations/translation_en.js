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
			"emailPasswordIncorrectos": "Incorrect Email/password",
			"en": " on ",
			"falloConEstado": " failed with status ",
			"numeroEventos": "Events number",
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
			},
			"dataSection":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"minimimSpeed": "Minimum Speed",
				"maximumSpeed": "Maximun Speed",
				"medianSpeed": "Median Speed",
				"averageSpeed": "Average Speed",
				"stdDevSpeed": "Std. Dev. Speed",
				"averageAcceleration": "Average Acceleration",
				"averageDeceleration": "Average Decceleration",
				"highAcceleration": "High Acceleration",
				"highDecceleration": "Hig Decceleration",
				"averageHeartRate": "Average Heart Rate",
				"stdDevHeartRate": "Std. Dev. Heart Rate"
			},
			"measurement":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"value": "Value",
				"speed": "Speed",
				"accuracy": "Accuracy"
			},
			"settings": {
				"titulo": "Settings",
				"numberError": "Only numbers",
				"required": "This field is required",
				"updateOk": "Settings updated" 
			},
			"systemLogs": {
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
				"smartdriver": "Smart Driver"
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
				"viewDetails": "View Details"
			},
			"vehicleLocation":{
				"noEvents": "There are not events to show",
				"userId": "UserId",
				"date": "Date",
				"time": "Time",
				"speed": "Speed",
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
			"smartdriver":{
				"filtros": "Filters",
				"filtrar": "Filter",
				"methods": "Methods",
				"results": "Results",
				"noResults": "There are not results",
				"linkId": "Identifier",
				"maxSpeed": "Maximium Speed",
				"linkName": "Name",
				"linkType": "Type",
				"position": "Position",
				"direction": "direction",
				"length": "Length",
				"previousposition": "Previous position",
				"selectSegment": "Select a segment"
			}
		});
	}

})();