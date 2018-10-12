'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.service:CodeMapService
 * @description # codemap service of the sbAdminApp
 */

angular.module('sbAdminApp').service('codeMapService', function($http, $q) {
	var codemap = {};

	this.clear = function() {
		codemap = {};
	}

	this.getCodeMap = function() {
		return codemap;
	}

	this.load = function() {
		var deferred = $q.defer();

		if (codemap.length > 0) {
			deferred.resolve(codemap);
			return deferred.promise;
		}

		$http.get('codemap/all').then(function successCallback(response) {
			var respData = response.data;
			if (respData.code == 0) {
				codemap = {};
	
				angular.forEach(respData.data, function(data, index, array) {
					if (!codemap[data.code_group]) {
						codemap[data.code_group] = {};
					}
	
					codemap[data.code_group][data.code] = data.text;
				});
	
				deferred.resolve(codemap);
			}
		}, function errorCallback(response) {
			deferred.reject(response);
		});

		return deferred.promise;
	}

	this.getGroupItems = function(codegroup) {
		var list = [];

		var kv = codemap[codegroup];
		if (kv) {
			for ( var i in kv) {
				list.push({
					'code' : i,
					'text' : kv[i]
				});
			}
		}

		return list;
	}

	this.getCodeText = function(codegroup, code) {
		var kv = codemap[codegroup];
		if (kv) {
			var v = kv[code];
			if (v)
				return v;
		}

		return code;
	}
});