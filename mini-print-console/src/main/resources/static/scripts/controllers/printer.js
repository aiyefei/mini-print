'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterCtrl
 * @description # Printer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'PrinterCtrl',
		function($rootScope, $scope, $http, codeMapService) {

            $("th").addClass("text-center");	// 文字居中
			$('#printerTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('printer/page', params.data).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								params.success(respData.data);
							} else {
								alert(respData.msg);
							}
						});
					},
			    columns: [{},{},{},{},{},{},{},{},{},{
			    	formatter:function(value,row,index){
			    		return '<a style="cursor: pointer;" class="view" data-id="'
                            + value
                            + '">查看</a> ';
			    	}
			    }]
			});
			
			$('#printerTable tbody').on('click', '.view', function() {
				var id = $(this).data('id');
				$http.get('printer/' + id).success(function(response) {
					if (response.code == 0) {
						$scope.error = false;
						$scope.printer = response.data;
						$scope.printer.updateTime = TimeObjectUtil.longMsTimeConvertToDateTime(parseInt($scope.printer.updateTime));
						$scope.printerForm.$setPristine();
						$('#printerViewModal').modal('show');
					} else {
						alert(response.msg);
					}
				});
			});
			
			$('#printerTable tbody').on('click', '.delete', function() {
                $('.delete').confirmation({
                    onConfirm : function(event, element){
                        var id = $(element).data('id');
                        $http.get('printer/delete/' + id).success(function(data) {
                            $('#printerTable').bootstrapTable('refresh');
                        });
                    }
                });

			});
		});