'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:ProductCtrl
 * @description # Product Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'ProductUpgradeCtrl',
		function($rootScope, $scope, $http, codeMapService, Upload) {
			var promise = codeMapService.load();
			promise.then(function(data) {
				$scope.upgradeTypes = codeMapService
						.getGroupItems('PRODUCT_UPGRADE_TYPE');
			}, function(data) {
				$scope.upgradeTypes = {};
			});
			
			$scope.uploadUpgradeFile = function(file) {
				if (file) {
					Upload.upload({
						url : 'file/upload?md5=true',
						file : file
					}).progress(
							function(evt) {
								$scope.upgradeProgress = Math.min(100, parseInt(100.0
										* evt.loaded / evt.total));
							}).success(function(response, status, headers, config) {
								if (response.code == 0) {
									$scope.productUpgrade.file_path = response.data.file_path;
									$scope.productUpgrade.checksum = response.data.checksum;
								} else {
									alert(response.msg);
								}
					});
				}
			};
			
			$scope.tableInited = false;
			
			var loadProductUpgradeTable = function(){
				if ($scope.tableInited) {
					$('#productUpgradeTable').bootstrapTable('refresh', {
						url : 'product/upgrade/page/' + $scope.productid
					});
					return;
				}
				
				$scope.tableInited = true;
				$('#productUpgradeTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('product/upgrade/page/' + $scope.productid, params.data).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										params.success(respData.data);
									} else {
										alert(respData.msg);
									}
						});
				    },
				    columns: [{},{
				    	formatter:function(value,row,index){
				    		return codeMapService
							.getCodeText(
									'PRODUCT_UPGRADE_TYPE',
									value);			    	
				    	}
				    },{},{
				    	formatter:function(value,row,index){
				    		return TimeObjectUtil.longMsTimeConvertToDateTime(parseInt(value));
				    	}
				    },{
				    	formatter:function(value,row,index){
				    		return '<a class="delete" data-id="'
							+ value
							+ '">删除</a>';				    	
				    	}
				    }]
				});
			}
			
			$('#productUpgradeTable').on('load-success.bs.table', function () {
				$('#productUpgradeTable .delete').confirmation({
					onConfirm : function(event, element){
						var id = $(element).data('id');
						$http.get('product/upgrade/delete/' + id).success(
                                function(data) {
                					$('#productUpgradeTable').bootstrapTable('refresh');
                                });
					}
				});
			});
			
			$('#productUpgradeModal').on('shown.bs.modal', function () {
				var id = $('#productid', this).val();	
				var actions = $('#actions', this).val();	
				$scope.productid = id;
				$scope.actions = actions;
				loadProductUpgradeTable();	
			});

			$scope.createProductUpgrade = function() {
				$scope.productUpgrade = {};
				$scope.productUpgrade.product_id = $scope.productid;
				$scope.productUpgrade.actions = $scope.actions;
				$scope.error = false;
				$scope.upgradeProgress = 0;
				$scope.productUpgradeForm.$setPristine();
				$('#productUpgradeEditModal').modal('show');
			}
			
			$scope.saveProductUpgrade = function() {
				$http.post('product/upgrade/save', $scope.productUpgrade).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
            					$('#productUpgradeTable').bootstrapTable('refresh');
								$('#productUpgradeEditModal').modal('hide');
							} else {
								$scope.error = true;
								$scope.errMsg = respData.msg;
							}
						});
			};
			
			$scope.selectActions = function() {
				if ($scope.productUpgrade.actions) {
					$('#codeMapSelectModal #selectedCodes').val($scope.productUpgrade.actions);
					$scope.actionArray = $scope.productUpgrade.actions.split(',');
				} else {
					$('#codeMapSelectModal #selectedCodes').val('');
					$scope.actionArray = [];
				}
				
				$('#codeMapSelectModal').modal('show');
			}
			
			$scope.$on("bc_codemap_change", function(event, msg) {
				if (!$scope.productUpgrade || !$scope.actionArray) {
					return;
				}
								
				if (msg.checked) {
					$scope.actionArray.push(msg.code);
				} else {
					$scope.actionArray = _.without($scope.actionArray, msg.code);
				}
				
				$scope.productUpgrade.actions = _.uniq($scope.actionArray).join(',');
				$scope.$apply();
			});
		});