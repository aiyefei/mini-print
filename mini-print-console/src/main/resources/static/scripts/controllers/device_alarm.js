'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:DeviceAlarmCtrl
 * @description # DeviceAlarm Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'DeviceAlarmCtrl',
		function($rootScope, $scope, $http, codeMapService) {
			$scope.tableInited = false;
			var loadDeviceAlarmTable = function() {
				if ($scope.tableInited) {
					$('#deviceAlarmTable').bootstrapTable('refresh', {
						url : 'device/alarm/page/' + $scope.deviceId
					});
					return;
				}
				
				$scope.tableInited = true;
				$('#deviceAlarmTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('device/alarm/page/' + $scope.deviceId, params.data).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										params.success(respData.data);
									} else {
										alert(respData.msg);
									}
						});
				    },
				    columns: [{},{}]
				});
			}
			
			$('#deviceAlarmModal').on('shown.bs.modal', function () {
				var id = $('#deviceId', this).val();	
				$scope.deviceId = id;
				loadDeviceAlarmTable();	
			});
			
		});