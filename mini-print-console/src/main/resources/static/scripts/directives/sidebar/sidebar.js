'use strict';

/**
 * @ngdoc directive
 * @name izzyposWebApp.directive:adminPosHeader
 * @description # adminPosHeader
 */

angular.module('sbAdminApp').directive('sidebar', [ '$location', function() {
	return {
		templateUrl : 'scripts/directives/sidebar/sidebar.html',
		restrict : 'E',
		replace : true,
		scope : {},
		controller : function($scope, $rootScope) {
			$scope.checkAuth = function(role) {
				var roleMatch = 0;
				angular.forEach($rootScope.user.authorities, function(data) {
					if (data.authority == role)
						roleMatch++;
				});
				return roleMatch > 0;
			};
		}
	};
} ]);
