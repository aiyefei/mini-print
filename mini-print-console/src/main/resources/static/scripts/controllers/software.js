'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:SoftwareCtrl
 * @description # Software Controller of the sbAdminApp
 */

angular
		.module('sbAdminApp')
		.controller(
				'SoftwareCtrl', function($rootScope, $scope, $http) {	
					//init software table
					$('#softwareTable').bootstrapTable({
					    pagination : true,
					    sidePagination: 'server',
					    search: true,
					    ajax : function(params) {
					        $http.post('software/page', params.data).then(
									function successCallback(response) {
										var respData = response.data;
										if (respData.code == 0) {
											params.success(respData.data);
										} else {
											alert(respData.msg);
										}
							});
					    },
					    columns: [{},{},{},{
					    		formatter:function(value,row,index){
					    			return '<a class="edit" data-id="'
										+ value
										+ '">编辑</a> <a class="delete" data-id="'
										+ value
										+ '">删除</a> <a class="upgrade" data-id="' + value
										+ '">升级</a>';
					    		}
					    }]
					});
					
					$('#softwareTable').on('load-success.bs.table', function () {
						$('.delete').confirmation({
							onConfirm : function(event, element){
								var id = $(element).data('id');
								$http.get('software/delete/' + id).success(function(data) {
									$('#softwareTable').bootstrapTable('refresh');
								});
							}
						});
					});
					
					$('#softwareTable tbody').on('click', '.edit', function() {
						var id = $(this).data('id');
						$http.get('software/' + id).success(function(response) {
							if (response.code == 0) {
								$scope.error = false;
								$scope.software = response.data;
								$scope.softwareForm.$setPristine();
								$('#softwareEditModal').modal('show');
							} else {
								alert(response.msg);
							}
						});

					});
					
					$('#softwareTable tbody').on('click', '.upgrade', function() {
						var id = $(this).data('id');
						var actions = $(this).data('actions');
						var upgradeModal = $('#softwareUpgradeModal');
						upgradeModal.find('#softwareId').val(id);
						upgradeModal.modal('show');
					});

					$scope.createSoftware = function() {
						$scope.software = {};
						$scope.error = false;
						$scope.softwareForm.$setPristine();
						$('#softwareEditModal').modal('show');
					};

					$scope.saveSoftware = function() {
						$http.post('software/save', $scope.software).then(
							function successCallback(response) {
								var respData = response.data;
								if (respData.code == 0) {
									$('#softwareTable').bootstrapTable('refresh');
									$('#softwareEditModal').modal('hide');
								} else {
									$scope.error = true;
									$scope.errMsg = respData.msg;
								}
						});
					};
				});