'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:ProductCtrl
 * @description # Product Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'ProductFeatureCtrl',
		function($rootScope, $scope, $http, codeMapService) {
			var promise = codeMapService.load();
			promise.then(function(data) {
				$scope.featureTypes = codeMapService
						.getGroupItems('PRODUCT_FEATURE');
			}, function(data) {
				$scope.featureTypes = {};
			});
			
			$scope.tableInited = false;
			var loadProductFeatureTable = function() {
				if ($scope.tableInited) {
					$('#productFeatureTable').bootstrapTable('refresh', {
						url : 'product/' + $scope.productid
					});
					return;
				}
				
				$scope.tableInited = true;
				$('#productFeatureTable').bootstrapTable({
				    pagination : true,
				    //sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				    		$http.get('product/' + $scope.productid).success(function(response) {
							if (response.code == 0) {
								params.success(response.data.features);
							} else {
								alert(response.msg);
							}
						});
				    },
				    columns: [{
				    	formatter:function(value,row,index){
				    		return codeMapService
							.getCodeText(
									'PRODUCT_FEATURE',
									value);				    	
				    	}
				    },{},{
				    	formatter:function(value,row,index){
				    		return '<a class="edit" data-type="'
							+ row.feature_type
							+ '" data-value="'+row.feature_value+'">编辑</a>  <a class="delete" data-type="'
							+ row.feature_type
							+ '">删除</a>';
				    	}
				    }]
				});
				
				$('#productFeatureTable').on('load-success.bs.table', function () {
					$('#productFeatureTable tbody').on('click', '.edit', function() {
						$scope.error = false;
						$scope.productFeature = {};
						$scope.productFeature.feature_type= $(this).data('type');
						$scope.productFeature.feature_value= $(this).data('value');
						$scope.$apply();
						$scope.productFeatureForm.$setPristine();
						$('#productFeatureEditModal').modal('show');
					});
					
					$('#productFeatureTable .delete').confirmation({
						onConfirm : function(event, element){
							$scope.productFeature = {};
							$scope.productFeature.feature_type = $(element).data('type');
							
							$http.post('product/feature/delete/' + $scope.productid, $scope.productFeature).then(
							function successCallback(response) {
								var respData = response.data;
								if (respData.code == 0) {
									$('#productFeatureTable').bootstrapTable('refresh');
								} else {
									$scope.error = true;
									$scope.errMsg = respData.msg;
								}
							});
						
						}
					});
				});
			}
			
			
			$('#productFeatureModal').on('shown.bs.modal', function () {
				var id = $('#productid', this).val();	
				$scope.productid = id;
				loadProductFeatureTable();	
			});
			
			$scope.createProductFeature = function() {
				$scope.productFeature = {};
				$scope.error = false;
				$scope.productFeatureForm.$setPristine();
				$('#productFeatureEditModal').modal('show');
			}
			
			$scope.saveProductFeature = function() {
				$http.post('product/feature/save/' + $scope.productid, $scope.productFeature).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								$('#productFeatureTable').bootstrapTable('refresh');
								$('#productFeatureEditModal').modal('hide');
							} else {
								$scope.error = true;
								$scope.errMsg = respData.msg;
							}
				});
			}
		});