'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterCtrl
 * @description # Printer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'AppVersionCtrl',
		function($rootScope, $scope, $http) {

            $("th").addClass("text-center");	// 文字居中
			$('#appVersionTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('appVersion/page', params.data).then(
						function successCallback(response) {
							var respData = response.data;
							if (respData.code == 0) {
								params.success(respData.data);
							} else {
								alert(respData.msg);
							}
						});
					},
			    columns: [{},{},{},{},{},{},{
			    	formatter:function(value,row,index){
                        return '<a style="cursor: pointer;" class="upgrade" data-id="'
                        + value
                        + '">升级</a> ';
			    	}
			    }]
			});

			$('#appVersionTable tbody').on('click', '.upgrade', function() {
                var id = $(this).data('id');
                var upgradeModal = $('#appVersionUpgradeModal');
                upgradeModal.find('#id').val(id);
                upgradeModal.modal('show');
			});
		});