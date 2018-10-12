'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:CodeMapCtrl
 * @description # CodeMap Controller of the sbAdminApp
 */

angular
		.module('sbAdminApp')
		.controller(
				'CodeMapCtrl',
				function($rootScope, $scope, $http, codeMapService) {
					
					$('#codeMapTable').bootstrapTable({
					    pagination : true,
					    sidePagination: 'server',
					    search: true,
					    ajax : function(params) {
					        $http.post('codemap/page', params.data).then(
									function successCallback(response) {
										var respData = response.data;
										if (respData.code == 0) {
											params.success(respData.data);
										} else {
											alert(respData.msg);
										}
							});
					    },
					    columns: [{},{},{},{},{
					    	formatter:function(value,row,index){
					    		return '<a class="edit" data-id="'
								+ value
								+ '">编辑</a>  <a class="delete" data-id="'
								+ value
								+ '">删除</a>';
					    	}
					    }]
					});
					
					$('#codeMapTable').on('load-success.bs.table', function () {
						$('.delete').confirmation({
							onConfirm : function(event, element){
								var id = $(element).data('id');
								$http.get('codemap/delete/' + id).success(function(data) {
									$('#codeMapTable').bootstrapTable('refresh');
								});
							}
						});
					});
					
					$('#codeMapTable tbody').on('click', '.edit', function() {
						var id = $(this).data('id');
						$http.get('codemap/' + id).success(function(response) {
							if (response.code == 0) {
								$scope.error = false;
								$scope.codemap = response.data;
								$scope.codeMapForm.$setPristine();
								$('#codeMapEditModal').modal('show');
							} else {
								alert(response.msg);
							}
						});

					});

					$scope.createCodeMap = function() {
						$scope.codemap = {};
						$scope.error = false;
						$scope.codeMapForm.$setPristine();
						$('#codeMapEditModal').modal('show');
					};

					$scope.saveCodeMap = function() {
						$http.post('codemap/save', $scope.codemap).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										codeMapService.clear();
										$('#codeMapTable').bootstrapTable('refresh');
										$('#codeMapEditModal').modal('hide');
									} else {
										$scope.error = true;
										$scope.errMsg = respData.msg;
									}
								});
					};

				});