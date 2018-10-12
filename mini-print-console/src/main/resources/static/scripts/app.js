'use strict';
/**
 * @ngdoc overview
 * @name sbAdminApp
 * @description # sbAdminApp
 * 
 * Main module of the application.
 */
var sbAdminApp = angular
  .module('sbAdminApp', [
    'oc.lazyLoad',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'ngCookies',
    'ngFileUpload',
    'summernote',
  ])
  .config(['$stateProvider','$urlRouterProvider','$ocLazyLoadProvider','$httpProvider',function ($stateProvider,$urlRouterProvider,$ocLazyLoadProvider,$httpProvider) {
    
    $ocLazyLoadProvider.config({
      debug:false,
      events:true,
    });

    $urlRouterProvider.otherwise('/login');

    $stateProvider
      .state('main', {
        url:'/main',
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        resolve: {
            loadMyDirectives:function($ocLazyLoad){
                return $ocLazyLoad.load(
                {
                    name:'sbAdminApp',
                    files:[
                    'scripts/directives/header/header.js',
                    'scripts/directives/sidebar/sidebar.js',
                    'scripts/directives/equals.js',
                    'scripts/services/codemap.js',
                    'scripts/services/enumMap.js',
                    'scripts/services/producer.js',
                    'scripts/services/product.js',
                    'scripts/controllers/main.js'
                    ]
                });         
            }
        }
    }).state('login', {
    	 url:'/login',
         templateUrl: 'views/login.html',
    	 controller:'LoginCtrl',
         resolve: {
             loadMyFile:function($ocLazyLoad) {
               return $ocLazyLoad.load({
                   name:'sbAdminApp',
                   files:['scripts/controllers/login.js']
               });
             }
           }
    }).state('logout', {
    	url:'/logout',
    	controller:'LogoutCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:['scripts/controllers/logout.js']
              });
            }
          }
    }).state('main.home', {
    	url : '/home',
        templateUrl:'views/home.html',
    }).state('main.user', {
    	url : '/user',
        templateUrl:'views/user.html',
        controller:'UserCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:['scripts/controllers/user.js']
              });
            }
          }
    })
    .state('main.codemap', {
    	url : '/codemap',
        templateUrl:'views/codemap.html',
        controller:'CodeMapCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:['scripts/controllers/codemap.js']
              });
            }
          }
    }).state('main.producer', {
    	url : '/producer',
        templateUrl:'views/producer.html',
        controller:'ProducerCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:[
                     'scripts/controllers/producer.js'                       
                  ]
              });
            }
          }
    }).state('main.product', {
    	url : '/product',
        templateUrl:'views/product.html',
        controller:'ProductCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:[
                         'scripts/controllers/product.js',
                         'scripts/controllers/producer_select.js',
                         'scripts/controllers/product_feature.js',                         
                         'scripts/controllers/product_upgrade.js',
                         'scripts/controllers/codemap_select.js',                         
                  ]
              });
            }
          }
    }).state('main.cloudapp', {
    	url : '/cloudapp',
        templateUrl:'views/cloudapp.html',
        controller:'CloudAppCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:[
                         'scripts/controllers/cloudapp.js',
                         'scripts/controllers/cloudapp_product.js',                         
                         'scripts/controllers/producer_select.js', 
                      	'scripts/controllers/codemap_select.js',                            
                  ]
              });
            }
          }
    }).state('main.device', {
        url : '/device',
        templateUrl:'views/device.html',
        controller:'DeviceCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/device.js',
                        'scripts/controllers/device_alarm.js'
                    ]
                });
            }
        }
    }).state('main.printer', {
        url : '/printer',
        templateUrl:'views/printer.html',
        controller:'PrinterCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/printer.js'
                        // ,
                        // 'scripts/controllers/device_alarm.js'
                    ]
                });
            }
        }
    }).state('main.printerTask', {
        url : '/printerTask',
        templateUrl:'views/printerTask.html',
        controller:'PrinterTaskCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/printerTask.js'
                    ]
                });
            }
        }
    }).state('main.tmPrinter', {
        url : '/tmPrinter',
        templateUrl:'views/tmPrinter.html',
        controller:'TMPrinterCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/tmPrinter.js'
                    ]
                });
            }
        }
    }).state('main.printerErrorInfo', {
        url : '/printerErrorInfo',
        templateUrl:'views/printerErrorInfo.html',
        controller:'PrinterErrorInfoCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/printerErrorInfo.js'
                        // ,
                        // 'scripts/controllers/device_alarm.js'
                    ]
                });
            }
        }
    }).state('main.appVersion', {
        url : '/appVersion',
        templateUrl:'views/appVersion.html',
        controller:'AppVersionCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/appVersion.js',
                        'scripts/controllers/appVersion_upgrade.js'
                    ]
                });
            }
        }
    }).state('main.printerFirmware', {
        url : '/appVersion',
        templateUrl:'views/printerFirmware.html',
        controller:'PrinterFirmwareCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name:'sbAdminApp',
                    files:[
                        'scripts/controllers/printerFirmware.js'
                        // ,
                        // 'scripts/controllers/printerFirmware_upgrade.js'
                    ]
                });
            }
        }
    }).state('main.software', {
    	url : '/software',
        templateUrl:'views/software.html',
        controller:'SoftwareCtrl',
        resolve: {
            loadMyFile:function($ocLazyLoad) {
              return $ocLazyLoad.load({
                  name:'sbAdminApp',
                  files:[
                         'scripts/controllers/software.js',                         
                         'scripts/controllers/software_upgrade.js'                           
                  ]
              });
            }
          }
    });
    
    $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    $httpProvider.interceptors.push(function($q) {
        return {
            'responseError': function(rejection){
                if(rejection.status == 401){
                	window.location = "./";
        			return;
                }

                return $q.reject(rejection);
            }, 'response': function (response) {
            	if (response.status == 401) {
                	window.location = "./";
        			return;
            	}
            	return response || $q.when(response);
            }
        };
    });
  }]);

//禁止模板缓存  
sbAdminApp.run(function($rootScope, $templateCache) {  
    $rootScope.$on('$routeChangeStart', function(event, next, current) {  
        if (typeof(current) !== 'undefined'){  
            $templateCache.remove(current.templateUrl);  
        }
    });  
});