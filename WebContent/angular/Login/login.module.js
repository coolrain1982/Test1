'use strict';

// Define the `loginApp` module
var loginApp = angular.module('loginApp', [
  // ...which depends on the `user` module
  'user', 'ui.router'
]);

loginApp.config(['$locationProvider', function($locationProvider){
	$locationProvider.html5Mode(true);
}]);

loginApp.controller("userController", function($location, $state, $stateParams, $scope, $http) {
	$scope.userInfo = {};
	$scope.errorLogin = null;
	
	if ($location.search().error) {
		$scope.errorLogin = "请输入正确的用户名和密码！";
	}
});