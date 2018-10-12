'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:CodeMapCtrl
 * @description # CodeMap Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller(
		'CodeMapSelectCtrl',
		function($rootScope, $scope, $http) {
			$scope.tableInited = false;
			
			var loadCodeMapSelectTable = function() {
				if ($scope.tableInited) {
					$('#codeMapSelectTable').bootstrapTable('refresh');
					return;
				}
				
				$scope.tableInited = true;
				$('#codeMapSelectTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('codemap/page/' + $scope.codeGroup, params.data).then(
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
				    		var html = '<input class="select" type="checkbox" data-id="'+value+'"';
							if ($.inArray(value, $scope.selectedCodeMap) != -1) {
								html += ' checked="checked"';
							}
							
							html += ' />';
							return html;
						}
				    }]
				});
				
				$('#codeMapSelectTable').on('load-success.bs.table', function () {
					$('#codeMapSelectTable tbody').on('change', '.select', function() {
						var code = $(this).data('id');	
						$scope.$emit("codemap_change", {'code':''+code, 'checked': $(this).prop('checked')});
					});
					
					$('#codeMapSelectModal #allcheck').prop('checked', false);
					
					$('#codeMapSelectModal #allcheck').change(function() {
						var checkVal = $(this).prop('checked');
						
						$('#codeMapSelectTable tbody .select').each(function(){
							$(this).prop("checked", checkVal);
							var code = $(this).data('id');	
							$scope.$emit("codemap_change", {'code':''+code, 'checked': checkVal});
						});
					});
				});
			}
			
			$('#codeMapSelectModal').on(
					'shown.bs.modal',
					function() {
						$scope.selectedCodeMap = $(
								'#codeMapSelectModal #selectedCodes').val()
								.split(',');
						loadCodeMapSelectTable();
					});

			$('#codeMapSelectModal').on('hidden.bs.modal', function() {
				$("body").addClass("modal-open");
			});

		});