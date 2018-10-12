'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterCtrl
 * @description # Printer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'TMPrinterCtrl',
		function($rootScope, $scope, $http, codeMapService) {

            $("th").addClass("text-center");	// 文字居中
			$('#tmPrinterTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('tmPrinter/page', params.data).then(
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
                    formatter:function(value, row, index){
                        if ('idle' == value) {
                            return '<span style="color: green;">就绪</span>';
                        } else if ('busy' == value) {
                            return '<span style="color: blue;">忙</span>';
                        } else {
                            return '<span style="color: red;">故障</span>';
						}
                    }
				},{
                    formatter:function(value, row, index){
                        if ('0' == value) {
                            return '<span style="color: darkslategrey;">离线</span>';
                        } else {
                            return '<span style="color: green;">在线</span>';
                        }
                    }
				},{},{},{
                    formatter:function(value, row, index){
                        if ('normal' == value) {
                            return '<span style="color: green;">正常</span>';
                        } else {
                            return '<span style="color: red;">异常</span>';
                        }
                    }
				},
					{
			    	formatter:function(value, row, index){
			    		if (null == value) {
                            return '<span style="color: blue;">未绑定</span>';
						} else {
                            return '<span style="color: green;">已绑定</span>';
						}
			    	}
			    }
			    ]
			});
			
			$('#tmPrinterTable tbody').on('click', '.view', function() {
				var id = $(this).data('id');
				$http.get('tmPrinter/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.printer = response.data;
						$scope.printer.createTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printer.createTime));
						$scope.printerForm.$setPristine();
						$('#tmPrinterViewModal').modal('show');
					} else {
						alert(response.msg);
					}
				});
			});
			
			$('#tmPrinterTable tbody').on('click', '.delete', function() {
                $('.delete').confirmation({
                    onConfirm : function(event, element){
                        var id = $(element).data('id');
                        $http.get('tmPrinter/delete/' + id).success(function(data) {
                            $('#tmPrinterTable').bootstrapTable('refresh');
                        });
                    }
                });

			});
		});