'use strict';

var changePwdApp = angular.module('change-password', 
		['angular-md5','chieffancypants.loadingBar', 'ngAnimate']);

changePwdApp.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
});

changePwdApp.controller("changepasswordController", ['$scope', '$http', 'md5', '$timeout',
	     'cfpLoadingBar', '$state',	
	     function($scope, $http, md5, $timeout, cfpLoadingBar, $state) {
	
	$scope.sumbitToServer = false;
	$scope.user = {
		error:"",
	};
	
	$scope.start = function() {
		$scope.sumbitToServer = true;
		cfpLoadingBar.start();
	}
	
	$scope.complete = function() {
		cfpLoadingBar.complete();
		$scope.sumbitToServer = false;
	}
	
	$scope.changePwd_submit = function(valid) {
		$scope.user.error = "";

		if (!valid) {
			return;
		}
		
		if ($scope.user.password.length > 20 || $scope.user.password.length < 6) {
			$scope.user.error="请正确输入密码！";
			return;
		}
		
		if ($scope.user.newpassword != $scope.user.newpassword_confirm) {
			$scope.user.error="两次输入的新密码不一致！";
			return;
		}
		
		var fd = new FormData();
		fd.append("password", md5.createHash($scope.user.password));
		fd.append("newpassword", md5.createHash($scope.user.newpassword));
		
    	$scope.start();
		
		$http({
			method:"POST",
			url:"changepassword.do",
			data: fd,
			headers: {"content-type":undefined},
			transformRequest: angular.identity
		}).success(function(data) {
			if(data.status == 1) {
				$scope.isSuccess = true;	
			} else if(data.status == 0) {
				$scope.user.error = data.error; 
			} else {
				$state.go($state.current, {}, {reload:true});
			}
			$scope.complete();
		}).error(function(data){
	    	$scope.complete();
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});	
	}
	
	$scope.$watch('user.newpassword', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 20) {
	    		$scope.user.newpassword = newVal.substr(0, 20);
	    	}
	    }	
	});
	
	$scope.$watch('user.newpassword_confirm', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 20) {
	    		$scope.user.newpassword_confirm = newVal.substr(0, 20);
	    	}
	    }	
	});
}]);