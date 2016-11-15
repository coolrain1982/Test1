'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', [
    'ui.router', 'order-home'
]);

mainpageApp.config(function($locationProvider, $stateProvider, $urlRouterProvider) {
//	$locationProvider.html5Mode(true);
	$stateProvider
	    .state('notice', {
	    	url : '/notice',
	    	templateUrl: 'notice.html'
	    })
	    .state('summary', {
	    	url : '/summary',
	    	templateUrl: 'summary.html'
	    })
	    .state('order.new', {
	    	url : '/neworder',
	    	templateUrl: 'neworder.html'
	    });
});

mainpageApp.controller("mainpageCtrl", function($location, $state, $stateParams, $scope, $http) {
	//取登陆用户
	$http.get('security/getLoginUserName.do')
	.success(function(data) {
		if (data == null) {
			$scope.loginUserName = "未知用户";
		} else {
			$scope.loginUserName = data.name;
		}
	})
	.error(function(data){});
});