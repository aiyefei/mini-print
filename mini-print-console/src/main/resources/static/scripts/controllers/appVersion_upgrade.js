'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:SoftwareCtrl
 * @description # Software Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'AppVersionUpgradeCtrl',
		function($rootScope, $scope, $http, Upload) {

			$scope.tableInited = false;
			
			var loadSoftwareUpgradeTable = function(){
				if ($scope.tableInited) {
					$('#appVersionUpgradeTable').bootstrapTable('refresh', {
						url : 'appVersionUpgrade/upgrade/page/' + $scope.id
					});
					return;
				}
				
				$scope.tableInited = true;
				$('#appVersionUpgradeTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('appVersionUpgrade/upgrade/page/' + $scope.id, params.data).then(
                            function successCallback(response) {
                                var respData = response.data;
                                if (respData.code == 0) {
                                    params.success(respData.data);
                                } else {
                                    alert(respData.msg);
                                }
						    });
				    },
				    columns: [{},{},{},{},{},{}]
				});
			}
			
			$('#appVersionUpgradeTable').on('load-success.bs.table', function () {
				$('#appVersionUpgradeTable .delete').confirmation({
					onConfirm : function(event, element){
						var id = $(element).data('id');
						$http.get('appVersion/upgrade/delete/' + id).success(
						function(data) {
							$('#appVersionUpgradeTable').bootstrapTable('refresh');
						});
					}
				});
			});
			
			$('#appVersionUpgradeModal').on('shown.bs.modal', function () {
				var id = $('#id', this).val();
				$scope.id = id;
                loadSoftwareUpgradeTable();
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
                        }).success(function(response, status, headers, config) {
                        if (response.code == 0) {
                            var url = response.data.absolute_path;
                            $scope.appVersion.checksum = response.data.checksum;
                            $scope.appVersion.downloadUrl = url;
                        } else {
                            alert(response.msg);
                        }
                    });
                }
            };

			$scope.createAppVersionUpgrade = function() {
				$scope.appVersion = {};
				// $scope.appVersion.id = $scope.id;
				$scope.error = false;
				$scope.appVersionForm.$setPristine();

                $http.get('appVersion/getHighest/' + $scope.id).then(
                    function successCallback(response) {
                        var respData = response.data;
                        if (respData.code == 0) {
                            $("#iosHighestVersion").html(respData.data.iosHighestVersion);
                            $("#androidHighestVersion").html(respData.data.androidHighestVersion);
                            $("#typeName").html(respData.data.type);
                            $("#version_").html(respData.data.showVersion);
                            $scope.appVersion.type = respData.data.type;
                            $scope.appVersion.version = respData.data.showVersion;
                        } else {
                            $scope.error = true;
                            $scope.errMsg = respData.msg;
                        }
                    });

				$('#appVersionUpgradeEditModal').modal('show');
			}
			
			$scope.saveAppVersion = function() {
				$http.post('appVersion/save', $scope.appVersion).then(
					function successCallback(response) {
						var respData = response.data;
						if (respData.code == 0) {
                            $('#appVersionUpgradeTable').bootstrapTable('refresh');
                            $('#appVersionTable').bootstrapTable('refresh');
                            $('#appVersionUpgradeEditModal').modal('hide');
                            $('#appVersionUpgradeModal').modal('hide');
						} else {
							$scope.error = true;
							$scope.errMsg = respData.msg;
						}
					});
			};

		});