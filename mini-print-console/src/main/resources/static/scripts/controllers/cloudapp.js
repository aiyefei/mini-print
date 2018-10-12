'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:CloudAppCtrl
 * @description # CloudApp Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'CloudAppCtrl',
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
								else if (type == 1)
									$scope.progress1 = progress;
								else if (type == 2)
									$scope.progress2 = progress;
								else if (type == 3)
									$scope.progress3 = progress;
								else if (type == 4)
									$scope.progress4 = progress;
								else if (type == 5)
									$scope.progress5 = progress;
							}).success(function(response, status, headers, config) {
								if (response.code == 0) {
									var filePath = response.data.file_path;
									if (type == 0)
										$scope.cloudapp.icon = filePath;
									else if (type == 1)
										$scope.cloudapp.screen_shot1 = filePath;
									else if (type == 2)
										$scope.cloudapp.screen_shot2 = filePath;
									else if (type == 3)
										$scope.cloudapp.screen_shot3 = filePath;
									else if (type == 4)
										$scope.cloudapp.screen_shot4 = filePath;
									else if (type == 5)
										$scope.cloudapp.screen_shot5 = filePath;
								} else {
									alert(response.msg);
								}
					});
				}
			};
			
			$scope.imageUpload = function(files) {
		    	var data = new FormData();  
                data.append("file", files[0]);  
                $.ajax({  
                    data: data,  
                    type: "POST",  
                    url: "file/upload",  
                    cache: false,  
                    contentType: false,  
                    processData: false,  
                    success: function(response) {
                        if (response.code == 0) {
                        	$scope.editor.summernote('insertImage', response.data.file_path);
                        } else {
                            alert(response.msg);
                        }
                    }  
                }); 
		    }

			var promise = codeMapService.load();
			promise.then(
					function(data) {
						$scope.belongTypes = codeMapService
								.getGroupItems('BELONG_TYPE');
						$scope.categories = codeMapService
								.getGroupItems('SERVICE_CATEGORY');
						$scope.statuses = codeMapService
								.getGroupItems('ENTITY_STATUS');
					}, function(data) {
						$scope.belongTypes = {};
						$scope.categories = {};
						$scope.statuses = {};
					});

			producerService.loadProducers();

			//init app table
			$('#cloudAppTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('cloudapp/page', params.data).then(
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
								'BELONG_TYPE',
								value);
			    	}
			    },{
			    	formatter:function(value,row,index){
			    		return codeMapService
						.getCodeText(
								'SERVICE_CATEGORY',
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
			    		var productIds = row.product_ids!= null ?row.product_ids:"";
			    	
			    		return '<a class="edit" data-id="'
						+ value
						+ '">编辑</a>  <a class="delete" data-id="'
						+ value
						+ '">删除</a> <a class="product" data-id="' + value
						+ '">产品配置</a>';
			    	}
			    }]
			});
			
			$('#cloudAppTable').on('load-success.bs.table', function () {
				$('#cloudAppTable .delete').confirmation({
					onConfirm : function(event, element){
						var id = $(element).data('id');
						$http.get('cloudapp/delete/' + id).success(
                                function(data) {
									$('#cloudAppTable').bootstrapTable('refresh');
                                });
					}
				});
			});
			
			$('#cloudAppTable tbody').on('click', '.edit', function() {
				var id = $(this).data('id');
				$http.get('cloudapp/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.progress = 0;
						$scope.progress1 = 0;
						$scope.progress2 = 0;
						$scope.progress3 = 0;
						$scope.progress4 = 0;
						$scope.progress5 = 0;
						$scope.cloudapp = response.data;
						$scope.producerName = producerService.getName($scope.cloudapp.producer_id);
						$scope.cloudAppForm.$setPristine();
						$('#cloudAppEditModal').modal('show');
						
						loadAppKey(id);
					} else {
						alert(response.msg);
					}
				});

			});
			
			var loadAppKey = function(id) {
				$http.get('cloudapp/key/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.cloudapp.app_key = response.data;
					}
				});
			};
			
			$('#cloudAppTable tbody').on('click', '.product', function() {
				var id = $(this).data('id');
				
				$http.get('cloudapp/' + id).success(function(response) {
					if (response.code == 0) {	
						$scope.appId = response.data.id;
						$scope.productIds = response.data.product_ids;
						
						var cloudAppProductModal = $('#cloudAppProductModal');
						cloudAppProductModal.modal('show');
					}
				});
			});
			
			$scope.selectAppProducer = function() {
				$('#producerSelectModal #producerType').val('service');
				$('#producerSelectModal').modal('show');
			}
			
			$scope.createCloudApp = function() {
				$scope.cloudapp = {};
				$scope.error = false;
				$scope.progress = 0;
				$scope.progress1 = 0;
				$scope.progress2 = 0;
				$scope.progress3 = 0;
				$scope.progress4 = 0;
				$scope.progress5 = 0;
				$scope.producerName = '';
				$scope.cloudAppForm.$setPristine();
				$('#cloudAppEditModal').modal('show');
			};

			$scope.saveCloudApp = function() {
				$http.post('cloudapp/save', $scope.cloudapp).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								$('#cloudAppTable').bootstrapTable('refresh');
								$('#cloudAppEditModal').modal('hide');
							} else {
								$scope.error = true;
								$scope.errMsg = respData.msg;
							}
						});
			};
			
			$scope.$on("producer_change", function(event, msg) {
				if (!$scope.cloudapp) {
					return;
				}
				
				$scope.cloudapp.producer_id = msg.id;
				$scope.producerName = msg.name;
				$scope.$apply();
			});

			$scope.selectActions = function() {
				if ($scope.cloudapp.actions) {
					$('#codeMapSelectModal #selectedCodes').val(
							$scope.cloudapp.actions);
					$scope.actionArray = $scope.cloudapp.actions.split(',');
				} else {
					$('#codeMapSelectModal #selectedCodes').val('');
					$scope.actionArray = [];
				}

				$('#codeMapSelectModal').modal('show');
			}
			
			$scope.codeMapSelectInit = function() {
				$scope.codeMapTitle = '选择应用指令集';
				$scope.codeGroup = 'CLOUDAPP_ACTION';
			}
			
			$scope.$on("codemap_change", function(event, msg) {
				//再次传递给子级controller，解决平级不可传值问题
				$scope.$broadcast("bc_codemap_change", msg);
				
				if (!$scope.cloudapp || !$scope.actionArray) {
					return;
				}
				
				if (msg.checked) {
					$scope.actionArray.push(msg.code);
				} else {
					$scope.actionArray = _.without($scope.actionArray, msg.code);
				}
				
				$scope.cloudapp.actions = _.uniq($scope.actionArray).join(',');
				$scope.$apply();
			});
		});