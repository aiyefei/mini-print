'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterCtrl
 * @description # Printer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'PrinterTaskCtrl',
		function($rootScope, $scope, $http, enumMapService) {

            $scope.taskStatusEnum = enumMapService.taskStatusEnum;
            $scope.printWayEnum = enumMapService.printWayEnum;
            $scope.duplexModeEnum = enumMapService.duplexModeEnum;
            $scope.printDirectionEnum = enumMapService.printDirectionEnum;
            $scope.taskTypeEnum = enumMapService.taskTypeEnum;

            $("th").addClass("text-center");	// 文字居中
			$('#printerTaskTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('printerTask/page', params.data).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								params.success(respData.data);
							} else {
								alert(respData.msg);
							}
						});
					},
			    columns: [{}, {}, {
                    formatter:function(value,row,index){
                    	if (value == 'P') {
                            return '<span style="color: blue">打印</span>';
						} else {
                            return '<span style="color: red">扫描</span>';
						}

                    }
				}, {}, {}, {}, {}, {
                    formatter:function(value,row,index){
                        return '<a class="view" data-id="'
                            + value
                            + '">查看</a> <a class="alarm" data-id="';
                    }
				}]
			});
			
			$('#printerTaskTable tbody').on('click', '.view', function() {
				var id = $(this).data('id');
				$http.get('printerTask/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.printerTask = response.data;
                        $scope.printerTask.convertStartTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.convertStartTime));
                        $scope.printerTask.convertEndTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.convertEndTime));
                        $scope.printerTask.dataStartTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.dataStartTime));
                        $scope.printerTask.dataEndTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.dataEndTime));
                        $scope.printerTask.errorTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.errorTime));
                        $scope.printerTask.taskStartTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.taskStartTime));
                        $scope.printerTask.taskEndTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printerTask.taskEndTime));
						$scope.printerTaskForm.$setPristine();
						$('#printerTaskViewModal').modal('show');
					} else {
						alert(response.msg);
					}
				});
			});
			
			$('#printerTaskTable tbody').on('click', '.delete', function() {
                $('.delete').confirmation({
                    onConfirm : function(event, element){
                        var id = $(element).data('id');
                        $http.get('printerTask/delete/' + id).success(function(data) {
                            $('#printerTaskTable').bootstrapTable('refresh');
                        });
                    }
                });
			});
		});