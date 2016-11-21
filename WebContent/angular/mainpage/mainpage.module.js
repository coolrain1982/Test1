'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', [
    'ui.router', 'order-home', 'new-order', 'image-upload'
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
	    .state('neworder', {
	    	url : '/neworder',
	    	templateUrl: 'neworder.html'
	    });
});

mainpageApp.controller("mainpageCtrl", function($location, $state, $stateParams, $scope, $http) {
	//取登陆用户
	$http.get('security/getLoginUserName.do')
	.success(function(data) {
		if (data == "") {
			$scope.loginUserName = "未知用户";
		} else {
			$scope.loginUserName = data.username;
		}
	})
	.error(function(data){
		$scope.loginUserName = "未知用户";
	});
});
