'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:DeviceCtrl
 * @description # Device Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'DeviceCtrl',
		function($rootScope, $scope, $http, codeMapService, productService) {
			
			$('#deviceTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('device/page', params.data).then(
							function successCallback(response) {
								var respData = response.data;
								if (respData.code == 0) {
									params.success(respData.data);
								} else {
									alert(respData.msg);
								}
					});
			    },
			    columns: [{},{},{
			    		formatter:function(value,row,index){
			    			return productService.getName(value);
			    		}
			    },{},{
			    		formatter:function(value,row,index){
			    			return codeMapService.getCodeText('TRUE_FALSE', value);
			    		}
			    },{
			    		formatter:function(value,row,index){
			    			return codeMapService.getCodeText('TRUE_FALSE', value);
			    		}
			    },{
			    	formatter:function(value,row,index){
			    		return '<a class="view" data-id="'
						+ value
						+ '">查看</a> <a class="alarm" data-id="'
						+ value
						+ '">报警信息</a>';
			    	}
			    }]
			});
			
			
			$('#deviceTable tbody').on('click', '.view', function() {
				var id = $(this).data('id');
				$http.get('device/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.device = response.data;
						$scope.device.product = productService.getProduct($scope.device.product_id);
						$scope.device.lastAccessTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.device.last_access_time));
						$scope.deviceForm.$setPristine();
						$('#deviceViewModal').modal('show');
					} else {
						alert(response.msg);
					}
				});

			});
			
			$('#deviceTable tbody').on('click', '.alarm', function() {
				var id = $(this).data('id');
				var deviceAlarmModal = $('#deviceAlarmModal');
				deviceAlarmModal.find('#deviceId').val(id);
				deviceAlarmModal.modal('show');
			});
		});