'use strict';
/**
 * @ngdoc function
 * @name sbAdminApp.service:ProductService
 * @description # product service of the sbAdminApp
 */

angular.module('sbAdminApp').service('productService', function($http, $q) {
	var products = {};
	var activeProducts = {};

	this.clear = function() {
		products = {};
		activeProducts = {};
	}

	this.getAllProducts = function(bActive) {
		var list = [];

		var kv = bActive ? activeProducts : products;
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

	this.loadProducts = function() {
		var deferred = $q.defer();

		if (products.length > 0) {
			deferred.resolve(products);
			return deferred.promise;
		}

		$http.get('product/all').then(function successCallback(response) {
			var respData = response.data;

			if (respData.code == 0) {
				products = {};

				angular.forEach(respData.data, function(data, index, array) {
					var id = data.id;

					if (data.status != 0 && data.status != 'n')
						activeProducts[id] = data;

					products[id] = data;
				});

				deferred.resolve(products);
			}
		}, function errorCallback(response) {
			deferred.reject(response);
		});

		return deferred.promise;
	}

	this.getName = function(id) {
		var product = products[id];
		if (product)
			return product.name;
		return id;
	}
	
	this.getProduct = function(id) {
		return products[id];
	}
});