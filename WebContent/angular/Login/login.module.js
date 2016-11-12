'use strict';

// Define the `loginApp` module
var loginApp = angular.module('loginApp', [
  // ...which depends on the `user` module
  'user'
]);

loginApp.controller("userController", function($scope, $http) {
	$scope.userInfo = {};
});