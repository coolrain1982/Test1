'use strict';

var profileModule = angular.module('profile', ['chieffancypants.loadingBar', 'ngAnimate']);
profileModule.config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

profileModule.controller("profileController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll, commFunc) {
    	
    	$scope.user={};
    	$scope.profileError = "";
    	
    	$scope.start = function() {
    		$scope.updateUser = $timeout(function() {
    			cfpLoadingBar.start();
    			$scope.profile_show = false;
    		}, 2000);
		};
		
		$scope.complete = function () {
			if ($scope.updateUser) {
				$timeout.cancel($scope.updateUser);
			}
			cfpLoadingBar.complete();
			$scope.profile_show = true;
		};
		
		$scope.refresh = function() {
			$state.go($state.current, {}, {reload:true});
		}
		
		$scope.start();
    	
    	//查询出用户信息
    	$http.get('user/getDiscount.do')
    	.success(function(data) {
    		$scope.complete();
    		if (data.error) {
    			$scope.profileError = data.error;
    		} else {
    			$scope.user.name = data.name;
    			$scope.user.email = data.email;
    			$scope.user.oldEmail = data.email;
    			$scope.user.mobile = parseInt(data.mobile);
    			$scope.user.oldMobile = parseInt(data.mobile);
    			$scope.user.qq = parseInt(data.qq);
    			$scope.user.oldQQ = parseInt(data.qq);
    		}
    	})
    	.error(function(data){
    		$scope.complete();
    	});
    	
    	$scope.profile_submit = function(valid) {
    		
    		if (valid = false) {
    			return;
    		}
    		
    		if ($scope.user.email == $scope.user.oldEmail &&
    			$scope.user.mobile == $scope.user.oldMobile &&
    			$scope.user.qq == $scope.user.oldQQ) {
    			return;
    		}
    		
    		var fd = new FormData();
    		
    		if ($scope.user.email != $scope.user.oldEmail) {
    			if ($scope.user.email.length > 40) {
    				$scope.user.error="email长度不能超过40！";
    				return;
    			}
    			
    			fd.append("email", $scope.user.email);
    		}
    		

    		if ($scope.user.mobile != $scope.user.oldMobile) {
    			if ($scope.user.mobile >= 100000000000) {
    				$scope.user.error="手机号码不能超过11位！";
    				return;
    			}
    			
    			fd.append("mobile", $scope.user.mobile);
    		}
    		
    		if ($scope.user.qq != $scope.user.oldQQ) {
    			if ($scope.user.qq >= 10000000000000) {
    				$scope.user.error="QQ号码不能超过13位！";
    				return;
    			}
    			
    			fd.append("qq", $scope.user.qq);
    		}
    		
    		$scope.start();
    		
    		$http({
    			method:"POST",
    			url:"user/update.do",
    			data: fd,
    			headers: {"content-type":undefined},
    			transformRequest: angular.identity
    		}).success(function(data) {
    			if(data.status == 1) {
    				$scope.isSuccess = true;
    			} else if(data.status == 0) {
    				$scope.user.error = data.error; 
    			} else {
    				window.location="/login.html";
    			}
    			$scope.complete();
    		}).error(function(data){
    	    	$scope.complete();
    			alert("发生错误，请重新登录！");
    			window.location.href = "logout";
    		});	
    	}	
    }]);