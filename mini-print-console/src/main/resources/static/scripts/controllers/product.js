'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:ProductCtrl
 * @description # Product Controller of the sbAdminApp
 */

angular
		.module('sbAdminApp')
		.controller(
				'ProductCtrl', function($rootScope, $scope, $http, codeMapService,
						productService, producerService, Upload) {	
					$scope.upload = function (file) {
						if (file) {
							Upload.upload({
						        url: 'file/upload',
						        file: file
						    }).progress(function (evt) {
						    	$scope.progress = Math.min(100, parseInt(100.0 * 
	                                    evt.loaded / evt.total));
						    }).success(function (response, status, headers, config) {
						    	if (response.code == 0) {
						    		$scope.product.icon = response.data.file_path;
						    	} else {
						    		alert(response.msg);
						    	}
						    });
						}
				    };
				    
				    $scope.imageUpload = function(files, eid) {
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
                                		var editor = eid == 0 ? $scope.editor0: $scope.editor1;
                                		editor.summernote('insertImage', response.data.file_path);
                                } else {
                                    alert(response.msg);
                                }
                            }  
                        }); 
				    }
					
					var promise = codeMapService.load();
					promise.then(function(data) {
						$scope.productTypes = codeMapService
								.getGroupItems('PRODUCT_TYPE');
						$scope.belongTypes = codeMapService
						.getGroupItems('BELONG_TYPE');
						$scope.connTypes = codeMapService
								.getGroupItems('NETWORK_CONFIG_TYPE');
						$scope.networkTypes = codeMapService
								.getGroupItems('NETWORK_TYPE');
						$scope.verifyTypes = codeMapService
								.getGroupItems('VERIFY_TYPE');
						$scope.protocols = codeMapService
								.getGroupItems('NETWORK_PROTOCOL');
						$scope.dataProtocols = codeMapService
								.getGroupItems('DATA_PROTOCOL');
						$scope.statuses = codeMapService
								.getGroupItems('ENTITY_STATUS');
					}, function(data) {
						$scope.productTypes = {};
						$scope.belongTypes = {};
						$scope.connTypes = {};
						$scope.networkTypes = {};
						$scope.verifyTypes = {};
						$scope.protocols = {};
						$scope.dataProtocols = {};
						$scope.statuses = {};
					});
					
					producerService.loadProducers();
					
					//init product table
					$('#productTable').bootstrapTable({
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
					    		return codeMapService
								.getCodeText(
										'BELONG_TYPE',
										value);
					    	}
					    },{
					    	formatter:function(value,row,index){
					    		return codeMapService
								.getCodeText(
										'VERIFY_TYPE',
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
					    		if (row.status == 'n' || row.status == '0') {
									return '<a class="edit" data-id="'
									+ value
									+ '">编辑</a>  <a class="delete" data-id="'
									+ value
									+ '">删除</a>';
								}
																						
								return '<a class="edit" data-id="'
										+ value
										+ '">编辑</a>  <a class="delete" data-id="'
										+ value
										+ '">删除</a> <a class="feature" data-id="' + value
										+ '">配置</a> <a class="upgrade" data-id="' + value
										+ '" data-actions="'+row.actions+'">升级</a>';
					    	}
					    }]
					});
					
					$('#productTable').on('load-success.bs.table', function () {
						$('.delete').confirmation({
							onConfirm : function(event, element){
								var id = $(element).data('id');
								$http.get('product/delete/' + id).success(function(data) {
                                    productService.clear();
									$('#productTable').bootstrapTable('refresh');
								});
							}
						});
					});
					
					$('#productTable tbody').on('click', '.edit', function() {
						var id = $(this).data('id');
						$http.get('product/' + id).success(function(response) {
							if (response.code == 0) {
								$scope.error = false;
								$scope.progress = 0;
								$scope.product = response.data;
								$scope.producerName = producerService.getName($scope.product.producer_id);
								$scope.productForm.$setPristine();
								$('#productEditModal').modal('show');
								
								loadProductKey(id);
							} else {
								alert(response.msg);
							}
						});

					});
					
					var loadProductKey = function(id) {
						$http.get('product/key/' + id).success(function(response) {
							if (response.code == 0) {
								$scope.product.product_key = response.data;
							}
						});
					};
					
					$('#productTable tbody').on('click', '.feature', function() {
						var id = $(this).data('id');
						var featureModal = $('#productFeatureModal');
						featureModal.find('#productid').val(id);
						featureModal.modal('show');
					});
					
					$('#productTable tbody').on('click', '.upgrade', function() {
						var id = $(this).data('id');
						var actions = $(this).data('actions');
						var upgradeModal = $('#productUpgradeModal');
						upgradeModal.find('#productid').val(id);
						upgradeModal.find('#actions').val(actions);
						upgradeModal.modal('show');
					});

					$scope.createProduct = function() {
						$scope.product = {};
						$scope.error = false;
						$scope.progress = 0;
						$scope.producerName = '';
						$scope.productForm.$setPristine();
						$('#productEditModal').modal('show');
					};

					$scope.saveProduct = function() {
						$http.post('product/save', $scope.product).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										productService.clear();
										$('#productTable').bootstrapTable('refresh');
										$('#productEditModal').modal('hide');
									} else {
										$scope.error = true;
										$scope.errMsg = respData.msg;
									}
								});
					};
					//选择设备指令
					$scope.selectActions = function() {
						if ($scope.product.actions) {
							$('#codeMapSelectModal #selectedCodes').val(
									$scope.product.actions);
							$scope.actionArray = $scope.product.actions.split(',');
						} else {
							$('#codeMapSelectModal #selectedCodes').val('');
							$scope.actionArray = [];
						}

						$('#codeMapSelectModal').modal('show');
					}
					//选择供应商
					$scope.selectDeviceProducer = function() {
						$('#producerSelectModal #producerType').val('device');
						$('#producerSelectModal').modal('show');
					}
					
					$scope.codeMapSelectInit = function() {
						$scope.codeMapTitle = '选择设备指令集';
						$scope.codeGroup = 'DEVICE_ACTION';
					}
					
					$scope.$on("codemap_change", function(event, msg) {
						//再次传递给子级controller，解决平级不可传值问题
						$scope.$broadcast("bc_codemap_change", msg);
						
						if (!$scope.product || !$scope.actionArray) {
							return;
						}
						
						if (msg.checked) {
							$scope.actionArray.push(msg.code);
						} else {
							$scope.actionArray = _.without($scope.actionArray, msg.code);
						}
						
						$scope.product.actions = _.uniq($scope.actionArray).join(',');
						$scope.$apply();
					});
					
					$scope.$on("producer_change", function(event, msg) {
						if (!$scope.product) {
							return;
						}
						
						$scope.product.producer_id = msg.id;
						$scope.producerName = msg.name;
						$scope.$apply();
					});
 
				});