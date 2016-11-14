'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', [
    'ui.router'
]);

mainpageApp.config(['$locationProvider', function($locationProvider){
	$locationProvider.html5Mode(true);
}]);

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