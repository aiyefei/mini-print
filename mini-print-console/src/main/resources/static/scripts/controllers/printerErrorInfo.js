'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:PrinterErrorInfoCtrl
 * @description # PrinterErrorInfo Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'PrinterErrorInfoCtrl',
		function($rootScope, $scope, $http) {

            $("th").addClass("text-center");	// 文字居中
			$('#printerErrorInfoTable').bootstrapTable({
			    pagination : true,
			    sidePagination: 'server',
			    search: true,
			    ajax : function(params) {
			        $http.post('printerErrorInfo/page', params.data).then(
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
			    		return '<a style="cursor: pointer;" class="view" data-id="'
						+ value
						+ '">查看</a> <a style="cursor: pointer;" class="alarm" data-id="'
						+ value
						+ '">报警信息</a>';
			    	}
			    }]
			});

		});