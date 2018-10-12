'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:LoginCtrl
 * @description # LoginCtrl Login Controller of the sbAdminApp
 */
angular.module('sbAdminApp').controller(
		'LoginCtrl',
		function($scope, $http, $location, $cookieStore) {
			var authenticate = function(credentials, callback) {
				var headers = credentials ? {
					authorization : "Basic "
							+ btoa(credentials.username + ":"
									+ credentials.password)
				} : {};

				$http.get('auth', {
					headers : headers
				}).then(function successCallback(response) {
					var respData = response.data;
					if (respData.code == 0) {
						$cookieStore.put('user', respData.data);
						$scope.authenticated = true;
					} else {
						$scope.authenticated = false;
					}

					callback && callback();
				}, function errorCallback(response) {
					callback && callback();
				});
			};

			$scope.credentials = $cookieStore.get('credentials');
			$cookieStore.remove('user');
			$scope.login = function() {
				authenticate($scope.credentials,
						function() {
							if ($scope.authenticated) {
								if ($scope.remember) {
									$cookieStore.put('credentials',
											$scope.credentials);
								}

								$location.path("/main/home");
								$scope.error = false;
							} else {
								$location.path("/login");
								$scope.error = true;
							}
						});
			};
		});
