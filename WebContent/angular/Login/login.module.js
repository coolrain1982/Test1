'use strict';

// Define the `loginApp` module
var loginApp = angular.module('loginApp', ['angular-md5',
  
]);

loginApp.config(['$locationProvider', function($locationProvider){
	$locationProvider.html5Mode(true);
}]);

loginApp.controller("userController", function($location, $scope, $http, md5, $timeout) {
	
	$scope.sumbitToServer = false;
	$scope.user = {
		error:"",
	};
	$scope.errorLogin = null;
	
	if ($location.search().error) {
		$scope.errorLogin = "请输入正确的用户名和密码！";
	}
	
	$scope.getMD5Psw = function() {
		return md5.createHash($scope.pwd);
	}
	
	$scope.register = function() {
		window.location.href = "register.html";
	}
	
	$scope.toLogin = function() {
		window.location.href = "login.html";
	}
	
	$scope.start = function() {
		$scope.sumbitToServer = true;
	}
	
	$scope.complete = function() {
		$scope.sumbitToServer = false;
	}
	
	$scope.chartToCs = function() { 
		var win = window.open("http://wpa.qq.com/msgrd?v=3&uin=2789220168&site=qq&menu=yes");
		$timeout(function() {win.close();}, 5000);
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
		}).error(function(data) {
			$location.path("login.html?error=1");
		});
	}
	
	$scope.$watch('user.name', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 20) {
	    		$scope.user.name = newVal.substr(0, 20);
	    	}
	    }	
	});
	
	$scope.$watch('user.password', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 20) {
	    		$scope.user.password = newVal.substr(0, 20);
	    	}
	    }	
	});
	
	$scope.$watch('user.password_confirm', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 20) {
	    		$scope.user.password_confirm = newVal.substr(0, 20);
	    	}
	    }	
	});
	
	$scope.$watch('user.email', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 40) {
	    		$scope.user.email = newVal.substr(0, 20);
	    	}
	    }	
	});
	
	$scope.$watch('user.mobile', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal >= 100000000000) {
	    		$scope.user.mobile = oldVal;
	    	}
	    }	
	});
	
	$scope.$watch('user.qq', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal >= 10000000000000) {
	    		$scope.user.qq = oldVal;
	    	}
	    }	
	});
	
	$scope.welcomWord = "创建一个新账户";
	
	$scope.registerUser = function() {
		$scope.user.error = "";
		
		if (!$scope.newUser.$valid) {
			return;
		}
		
		//检查用户名是否合法
		if ($scope.user.name.length > 20 || $scope.user.name.length < 4) {
			$scope.user.error="用户名长度最少4位，最多20位！";
			return;
		}
		
		if ($scope.user.password.length > 20 || $scope.user.password.length < 6) {
			$scope.user.error="密码长度最少6位，最多20位！";
			return;
		}
		
		if ($scope.user.password != $scope.user.password_confirm) {
			$scope.user.error="两次输入的密码不一致！";
			return;
		}
		
		if ($scope.user.email.length > 40) {
			$scope.user.error="email长度不能超过40！";
			return;
		}
		
		if ($scope.user.mobile >= 100000000000) {
			$scope.user.error="手机号码不能超过11位！";
			return;
		}
		
		if ($scope.user.qq >= 10000000000000) {
			$scope.user.error="QQ号码不能超过13位！";
			return;
		}
		
		var fd = new FormData();
		fd.append("name", $scope.user.name);
		fd.append("password", md5.createHash($scope.user.password));
		fd.append("email", $scope.user.email);
		fd.append("mobile", $scope.user.mobile);
		fd.append("qq", $scope.user.qq);
		
    	$scope.start();
		
		$http({
			method:"POST",
			url:"register.do",
			data: fd,
			headers: {"content-type":undefined},
			transformRequest: angular.identity
		}).success(function(data) {
			if(data.status == 1) {
				$scope.isSuccess = true;	
				$scope.welcomWord = "创建新用户成功";
			} else if(data.status == 0) {
				$scope.user.error = data.error; 
			} else {
				
			}
			$scope.complete();
		}).error(function(data){
	    	$scope.complete();
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
});