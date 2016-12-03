'use strict';

// Define the `loginApp` module
var loginApp = angular.module('loginApp', ['angular-md5',
  
]);

loginApp.config(['$locationProvider', function($locationProvider){
	$locationProvider.html5Mode(true);
}]);

loginApp.controller("userController", function($location, $scope, $http, md5) {
	$scope.userInfo = {};
	$scope.errorLogin = null;
	
	if ($location.search().error) {
		$scope.errorLogin = "请输入正确的用户名和密码！";
	}
	
	$scope.getMD5Psw = function() {
		return md5.createHash($scope.pwd);
	}
	
	$scope.submit = function() {
		
		$http({
			method:"POST",
			url:"login",
			data: {username:$scope.name, password:$scope.getMD5Psw()},
			headers: {"content-type":'application/x-www-form-urlencoded'},
			transformRequest: function(obj) {
				var str=[];
				for(var p in obj) {
					str.push(encodeURIComponent(p)+ "=" + encodeURIComponent(obj[p]));
				}
				return str.join("&");
			},
		}).success(function(data) {
			window.location.href = data.page;
		});
	}
});