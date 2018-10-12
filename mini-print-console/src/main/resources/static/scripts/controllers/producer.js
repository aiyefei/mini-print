'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:ServiceProviderCtrl
 * @description # Service Provider Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'ProducerCtrl',
		function($rootScope, $scope, $http, codeMapService, producerService, Upload) {
			$scope.upload = function(file, type) {
				if (file) {
					Upload.upload({
						url : 'file/upload',
						file : file
					}).progress(
							function(evt) {
								var progress = Math.min(100, parseInt(100.0
										* evt.loaded / evt.total));
								if (type == 0)
									$scope.progress = progress;
								else
									$scope.progress1 = progress;
							}).success(function(response, status, headers, config) {
								if (response.code == 0) {
									var filePath = response.data.file_path;
									if (type == 0)
										$scope.producer.logo = filePath;
									else
										$scope.producer.licence = filePath;
								} else {
									alert(response.msg);
								}
					});
				}
			};

			var promise = codeMapService.load();
			promise.then(function(data) {
				$scope.producerTypes = codeMapService
						.getGroupItems('PRODUCER_TYPE');
				$scope.statuses = codeMapService
						.getGroupItems('ENTITY_STATUS');
			}, function(data) {
				$scope.producerTypes = {};
				$scope.statuses = {};
			});
			
			$('#producerTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('producer/page', params.data).then(
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
								'PRODUCER_TYPE',
								value);
			    	}
			    },{},{},{
			    	formatter:function(value,row,index){
			    		return codeMapService
						.getCodeText(
								'ENTITY_STATUS',
								value);
			    }
			    },{
			    	formatter:function(value,row,index){
			    		return '<a class="edit" data-id="'
						+ value
						+ '">编辑</a>  <a class="delete" data-id="'
						+ value
						+ '">删除</a>';
			    	}
			    }]
			});
			
			$('#producerTable').on('load-success.bs.table', function () {
				$('.delete').confirmation({
					onConfirm : function(event, element){
						var id = $(element).data('id');
						$http.get('producer/delete/' + id).success(function(data) {
							$('#producerTable').bootstrapTable('refresh');
						});
					}
				});
			});
			
			$('#producerTable tbody').on('click', '.edit', function() {
				var id = $(this).data('id');
				$http.get('producer/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.progress = 0;
						$scope.progress1 = 0;
						$scope.producer = response.data;
						$scope.producerForm.$setPristine();
						$('#producerEditModal').modal('show');
					} else {
						alert(response.msg);
					}
				});

			});
			
			$scope.createProducer = function() {
				$scope.producer = {};
				$scope.error = false;
				$scope.progress = 0;
				$scope.progress1 = 0;
				$scope.producerForm.$setPristine();
				$('#producerEditModal').modal('show');
			};

			$scope.saveProducer = function() {
				$http.post('producer/save', $scope.producer).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								producerService.clear();
								$('#producerTable').bootstrapTable('refresh');
								$('#producerEditModal').modal('hide');
							} else {
								$scope.error = true;
								$scope.errMsg = respData.msg;
							}
						});
			};

		});