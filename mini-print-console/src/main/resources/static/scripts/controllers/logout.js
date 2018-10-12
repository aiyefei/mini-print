'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:LogoutCtrl
 * @description # Logout Controller of the sbAdminApp
 */
angular.module('sbAdminApp').controller('LogoutCtrl',
		function($cookieStore, $scope, $http, $location) {
			$cookieStore.remove('user');
			$http.get('logout');
			$location.path("/login");
		});
