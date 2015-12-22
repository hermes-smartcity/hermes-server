/** Módulo con utilidades genereales para aplicaciones con Angular */
utilidades = angular.module('util', []);


utilidades.directive('navCollapse', function () {
	return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            var visible = false;

            element.on('show.bs.collapse', function () {
                visible = true;
            });

            element.on("hide.bs.collapse", function () {
                visible = false;
            });

            element.on('click', function (event) {
                if (visible && 'auto' == element.css('overflow-y') && $(event.target).attr('data-toggle')!="dropdown") {
                    element.collapse('hide');
                }
            });
        }
    };
});

// Recargamos la página cuando la sesión está caducada al hacer alguna petición AJAX
utilidades.factory('sessionRecoverer', ['$q', '$injector', function($q, $injector) {  
    var sessionRecoverer = {
        responseError: function(response) {
          
            if (response.status == 901) {
            	window.location.reload(false);
            }
            return $q.reject(response);
        }
    };
    return sessionRecoverer;
}]);