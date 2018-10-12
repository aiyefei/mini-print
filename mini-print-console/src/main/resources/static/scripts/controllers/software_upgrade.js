'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:SoftwareCtrl
 * @description # Software Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'SoftwareUpgradeCtrl',
		function($rootScope, $scope, $http, codeMapService, Upload) {
			var promise = codeMapService.load();
			promise.then(function(data) {
				$scope.upgradeTypes = codeMapService
						.getGroupItems('PRODUCT_UPGRADE_TYPE');
			}, function(data) {
				$scope.upgradeTypes = {};
			});
			
			
			$scope.tableInited = false;
			
			var loadSoftwareUpgradeTable = function(){
				if ($scope.tableInited) {
					$('#softwareUpgradeTable').bootstrapTable('refresh', {
						url : 'software/upgrade/page/' + $scope.softwareid
					});
					return;
				}
				
				$scope.tableInited = true;
				$('#softwareUpgradeTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('software/upgrade/page/' + $scope.softwareId, params.data).then(
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
				    },{},{
				    	formatter:function(value,row,index){
				    		return '<a class="delete" data-id="'
							+ value
							+ '">删除</a>';				    	
				    	}
				    }]
				});
			}
			
			$('#softwareUpgradeTable').on('load-success.bs.table', function () {
				$('#softwareUpgradeTable .delete').confirmation({
					onConfirm : function(event, element){
						var id = $(element).data('id');
						$http.get('software/upgrade/delete/' + id).success(
                                function(data) {
                					$('#softwareUpgradeTable').bootstrapTable('refresh');
                                });
					}
				});
			});
			
			$('#softwareUpgradeModal').on('shown.bs.modal', function () {
				var id = $('#softwareId', this).val();	
				$scope.softwareId = id;
				loadSoftwareUpgradeTable();	
			});

			$scope.createSoftwareUpgrade = function() {
				$scope.softwareUpgrade = {};
				$scope.softwareUpgrade.software_id = $scope.softwareId;
				$scope.error = false;
				$scope.softwareUpgradeForm.$setPristine();
				$('#softwareUpgradeEditModal').modal('show');
			}
			
			$scope.saveSoftwareUpgrade = function() {
				$http.post('software/upgrade/save', $scope.softwareUpgrade).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
            						$('#softwareUpgradeTable').bootstrapTable('refresh');
								$('#softwareUpgradeEditModal').modal('hide');
							} else {
								$scope.error = true;
								$scope.errMsg = respData.msg;
							}
						});
			};
			
		});