'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.service:ProducerService
 * @description # producer service of the sbAdminApp
 */

angular.module('sbAdminApp').service('producerService', function($http, $q) {
	var producers = {};
	var activeProducers = {};

	this.clear = function() {
		producers = {};
		activeProducers = {};
	}

	this.getAllProducers = function(bActive) {
		var list = [];

		var kv = bActive ? activeProducers : producers;
		if (kv) {
			for ( var i in kv) {
				list.push({
					'code' : i,
					'text' : kv[i].name
				});
			}
		}

		return list;
	}

	this.loadProducers = function() {
		var deferred = $q.defer();

		if (producers.length > 0) {
			deferred.resolve(producers);
			return deferred.promise;
		}

		$http.get('producer/all').then(function successCallback(response) {
			var respData = response.data;

			if (respData.code == 0) {
				producers = {};

				angular.forEach(respData.data, function(data, index, array) {
					var id = data.id;

					if (data.status != 0 && data.status != 'n')
						activeProducers[id] = data;

					producers[id] = data;
				});

				deferred.resolve(producers);
			}
		}, function errorCallback(response) {
			deferred.reject(response);
		});

		return deferred.promise;
	}

	this.getName = function(id) {
		var producer = producers[id];
		if (producer)
			return producers[id].name;
		return id;
	}
});