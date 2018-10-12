'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:UserCtrl
 * @description # User Controller of the sbAdminApp
 */

angular
		.module('sbAdminApp')
		.controller(
				'UserCtrl',
				function($rootScope, $scope, $http, codeMapService) {
					var promise = codeMapService.load();
					promise.then(function(data) {
						$scope.roleTypes = codeMapService
								.getGroupItems('ROLE_TYPE');
						$scope.statuses = codeMapService
								.getGroupItems('ENTITY_STATUS');

					}, function(data) {
						$scope.roleTypes = {};
						$scope.statuses = {};
					});

					$('#userTable').bootstrapTable({
					    pagination : true,
					    sidePagination: 'server',
					    search: true,
					    ajax : function(params) {
					        $http.post('user/page', params.data).then(
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
										'ROLE_TYPE',
										value);
					    	}
					    },{
					    	formatter:function(value,row,index){
					    		return codeMapService
								.getCodeText(
										'ENTITY_STATUS',
										value);
					    }},{
					    	formatter:function(value,row,index){
					    		return '<a class="edit" data-id="'
								+ value
								+ '">编辑</a>  <a class="delete" data-id="'
								+ value
								+ '">删除</a>';
					    	}
					    }]
					});
					
					$('#userTable').on('load-success.bs.table', function () {
						$('.delete').confirmation({
							onConfirm : function(event, element){
								var id = $(element).data('id');
								$http.get('user/delete/' + id).success(function(data) {
									$('#userTable').bootstrapTable('refresh');
								});
							}
						});
					});
					
						
					$('#userTable tbody').on('click', '.edit', function() {
						var id = $(this).data('id');
						$http.get('user/' + id).success(function(response) {
							if (response.code == 0) {
								$scope.error = false;
								$scope.user = response.data;
								$scope.userForm.$setPristine();
								$('#userEditModal').modal('show');
							} else {
								alert(response.msg);
							}
						});

					});

					$scope.createUser = function() {
						$scope.user = {};
						$scope.error = false;
						$scope.userForm.$setPristine();
						$('#userEditModal').modal('show');
					};

					$scope.saveUser = function() {
						$http.post('user/save', $scope.user).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										$('#userTable').bootstrapTable('refresh');
										$('#userEditModal').modal('hide');
									}else {
										$scope.error = true;
										$scope.errMsg = respData.msg;
									}
								});
					};

				});