(function() {
	'use strict';

	angular.module('app').directive('block', block);

	block.$inject = ['$compile'];

	function block($compile) {
		return {
			 restrict: 'E',
			    replace: true,
			    scope: {
			      block: '='
			    },
			    template: '<div>' +
			      '<p class="blocke"> {{block.title}} </p>' +
			      '<input class="childButton" ng-model="childName[$index]" type="text" placeholder="child name">' +
			      '<button class="childButton" ng-click="addChild($index)" >Add child to {{block.title}} </button>' +
			    '</div>',
			    link: function($scope, $element) {
			      if (angular.isArray($scope.block.childs) && $scope.block.childs.length > 0) {
			        $element.append('<block ng-repeat="childBlocks in block.childs" block="childBlocks"></block>');
			        $compile($element.contents().last())($scope);
			      }
			    }
		}
	}
})();