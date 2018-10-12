'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.controller:ProducerCtrl
 * @description # Producer Controller of the sbAdminApp
 */

angular.module('sbAdminApp').controller('ProducerSelectCtrl',
		function($rootScope, $scope, $http) {
			$scope.tableInited = false;
			
			var loadProducerSelectTable = function() {
				if ($scope.tableInited) {
					$('#producerSelectTable').bootstrapTable('refresh');
					return;
				}
				
				$scope.tableInited = true;
				$('#producerSelectTable').bootstrapTable({
				    pagination : true,
				    sidePagination: 'server',
				    search: true,
				    ajax : function(params) {
				        $http.post('producer/page/' + $('#producerSelectModal #producerType').val(), params.data).then(
								function successCallback(response) {
									var respData = response.data;
									if (respData.code == 0) {
										params.success(respData.data);
									} else {
										alert(respData.msg);
									}
						});
				    },
				    columns: [{},{
				    	formatter:function(value,row,index){
							return '<input class="select" type="checkbox" data-id="'+value+'" data-name="'+row.name+'" />';
				    	}
				    }]
				});
				
				$('#producerSelectTable').on('load-success.bs.table', function () {
					$('#producerSelectTable tbody').on('change', '.select', function() {
						var id = $(this).data('id');	
						var name = $(this).data('name');	
						$scope.$emit("producer_change", {'id':''+id, 'name': name});
						
						$('#producerSelectModal').modal('hide');
					});
				});
			}

			$('#producerSelectModal').on('shown.bs.modal', function() {
				loadProducerSelectTable();
			});

			$('#producerSelectModal').on('hidden.bs.modal', function() {
				$("body").addClass("modal-open");
			});
		});