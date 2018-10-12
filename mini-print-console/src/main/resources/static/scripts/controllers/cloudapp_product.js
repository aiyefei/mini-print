'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:CloudAppProductCtrl
 * @description # CloudAppProduct Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'CloudAppProductCtrl',
		function($rootScope, $scope, $http, codeMapService, producerService) {
			codeMapService.load();
			producerService.loadProducers();

			$scope.tableInited = false;
			var loadCloudAppProductTable = function() {
				if ($scope.tableInited) {
					$('#cloudAppProductTable').bootstrapTable('refresh');
					return;
				}
				
				$scope.tableInited = true;
				$('#cloudAppProductTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('product/page', params.data).then(
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
				    		return codeMapService
							.getCodeText(
									'PRODUCT_TYPE',
									value);
				    	}
				    },{
				    	formatter:function(value,row,index){
				    		return producerService.getName(value);
				    	}
				    },{
				    	formatter:function(value,row,index){
				    		return codeMapService
							.getCodeText(
									'ENTITY_STATUS',
									value);
				    	}
				    },{
				    	formatter:function(value,row,index){
				    		if ($.inArray(value, $scope.selectProducts) != -1)
							{
								return '<input class="select" type="checkbox" checked="checked" data-id="' + value + '"/>';
							}
							return '<input class="select" type="checkbox" data-id="' + value +'" />';
				    	}
				    }]
				});
				
				$('#cloudAppProductTable').on('load-success.bs.table', function () {
					$('#cloudAppProductTable tbody').on('change', '.select', function() {
						var checkStatus = $(this).prop('checked');
						var productId = $(this).data('id');
						
						if (checkStatus) {
							saveCloudAppProduct($scope.appId, productId);
						} else {
							deleteCloudAppProduct($scope.appId, productId);
						}
					});
				});
			}
			
			
			$('#cloudAppProductModal').on('shown.bs.modal', function () {
				$scope.selectProducts = $scope.productIds != null ? $scope.productIds : [];	
				loadCloudAppProductTable();
			});
			
			var saveCloudAppProduct = function(appId, productId) {
				$http.get('cloudapp/product/bind?id=' + appId + '&product_id=' + productId).success(
						function(data) {});
			}
			
			var deleteCloudAppProduct = function(appId, productId) {
				$http.get('cloudapp/product/unbind?id=' + appId + '&product_id=' + productId).success(
						function(data) {});
			}
		});