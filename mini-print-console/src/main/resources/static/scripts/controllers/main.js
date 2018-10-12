'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:MainCtrl
 * @description # MainCtrl Controller of the sbAdminApp
 */
angular.module('sbAdminApp').controller(
		'MainCtrl',
		function($rootScope, $scope, $location, $cookieStore, $http,
				codeMapService, productService, producerService) {
			var user = $cookieStore.get('user');
			if (!user) {
				$location.path("/");
				return;
			}
			if (!$rootScope.user) {
				$rootScope.user = $cookieStore.get('user');
			}

			codeMapService.load();
			productService.loadProducts();
			producerService.loadProducers();
			
			$scope.changePassword = function() {
				$scope.error = false;
				$scope.pass = {};
				$('#passwordModal').modal('show');
			}

			$scope.savePassword = function() {
				$http.get('chpass', {params:$scope.pass}).then(function successCallback(response) {
					$('#passwordModal').modal('hide');
				}, function errorCallback(response) {
					$scope.error = true;
					$scope.errMsg = response.data.message || response.data;
				});
			}
		});
