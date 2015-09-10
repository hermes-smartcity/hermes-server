
angular.module('directives', [])
.directive('confirmationNeeded', function () {
	return {
	    priority: -1,		
		link: function (scope, element, attr) {
			var msg = attr.confirmationNeeded || "¿Confirma que desea continuar?";
			var clickAction = attr.ngClick;
			element.bind('click',function (e) {
				if ( window.confirm(msg) ) {
					scope.$apply(clickAction);
				}else {
					 e.stopImmediatePropagation();
			         e.preventDefault();
				}
			});
		}
	};
})
.directive('integerValid', function () {
	return function (scope, element, attrs) {
	        element.bind("keydown keypress", function (e) {
	    		var charCode = (e.which) ? e.which : e.keyCode;
	    	    if (charCode != 46 && charCode != 37 && charCode != 39 && charCode > 31 && 
	    	    		(!(charCode >= 48 && charCode <= 57) && !(charCode >= 96 && charCode <=105))) {
	    	    	e.preventDefault();
	    	    } 
	        });
	 };
})
;

