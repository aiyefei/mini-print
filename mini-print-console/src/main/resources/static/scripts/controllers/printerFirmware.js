'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterCtrl
 * @description # Printer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'PrinterFirmwareCtrl',
		function($rootScope, $scope, $http, Upload) {

			$("th").addClass("text-center");	// 文字居中
			$('#printerFirmwareTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('printerFirmware/page', params.data).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								params.success(respData.data);
							} else {
								alert(respData.msg);
							}
						});
					},
			    columns: [{},{},{},{},{},{},{},{},{
			    	formatter : function (value,row,index) {
			    		if (row.status == 1) {
							return '<span style="color: blue;">正常<span>';
						} else {
                            return '<span style="color: red;">禁用<span>';
						}
					}
				}
                // ,{
			    // 	formatter:function(value,row,index){
			    // 		return '<a style="cursor: pointer;" style="cursor: pointer;" class="upgrade" data-id="'
					// 	+ value
					// 	+ '">升级</a>';
			    // 	}
			    // }
			    ]
			});

            $scope.uploadUpgradeFile = function(file) {
                if (file) {
                    Upload.upload({
                        url : 'file/upload?md5=true',
                        file : file
                    }).progress(
                        function(evt) {
                            $scope.upgradeProgress = Math.min(100, parseInt(100.0
                                * evt.loaded / evt.total));
                        }).success(function(response) {
                        if (response.code == 0) {

                            $scope.printerFirmware.firmwareUrl = response.data.absolute_path;
                            $scope.printerFirmware.firmwareSize = response.data.size;

                        } else {
                            alert(response.msg);
                        }
                    });
                }
            };

            $scope.createPrinterFirmwareUpgrade = function() {
                $scope.printerFirmware = {};
                // $scope.appVersion.id = $scope.id;
                $scope.error = false;
                $scope.printerFirmwareForm.$setPristine();
                $('#printerFirmwareUpgradeModal').modal('show');
            }

            $scope.savePrinterFirmware = function() {
                $http.post('printerFirmware/save', $scope.printerFirmware).then(
                    function successCallback(response) {
                        var respData = response.data;
                        if (respData.code == 0) {
                            $('#printerFirmwareTable').bootstrapTable('refresh');
                            $('#printerFirmwareUpgradeModal').modal('hide');
                        } else {
                            $scope.error = true;
                            $scope.errMsg = respData.msg;
                        }
                    });
            };
		});