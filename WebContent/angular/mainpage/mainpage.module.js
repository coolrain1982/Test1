'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', ['ngAnimate', 'mgcrea.ngStrap',
    'ui.router', 'order-home', 'new-order', 'image-upload', 'order-table',
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
	    .state('newOrder', {
	    	url : '/newOrder',
	    	templateUrl: 'newOrder.html'
	    })
	    .state('unconfirmOrder', {
	    	url : '/unconfirmOrder',
	    	templateUrl: 'unconfirmOrder.html'
	    })
	    .state('unpayOrder', {
	    	url : '/unpayOrder',
	    	templateUrl: 'unpayOrder.html'
	    })
	    .state('doingOrder', {
	    	url : '/doingOrder',
	    	templateUrl: 'doingOrder.html'
	    })
	    .state('rejectOrder', {
	    	url : '/rejectOrder',
	    	templateUrl: 'rejectOrder.html'
	    })
	    .state('historyOrder', {
	    	url : '/historyOrder',
	    	templateUrl: 'historyOrder.html'
	    })
	    .state('allOrder', {
	    	url : '/allOrder',
	    	templateUrl: 'allOrder.html'
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
	
	$scope.formatDate = function(temp) {
		var temp = new Date(temp*1000);
		return temp.getFullYear() + "-" + this.foo((temp.getMonth() + 1), 2) + "-" +
		    this.foo(temp.getDate(), 2) + " " + this.foo(temp.getHours(),2) + ":" +
		       this.foo(temp.getMinutes(), 2)+ ":" + this.foo(temp.getSeconds(), 2);
	};
	
	$scope.foo = function(str,len) {
		for(var i = 0; i < len; i++) {
			str = '0' + str;
		}
		return str.substring(str.length-len, str.length);
	};
	
	$scope.getDate = function(str) {
		return str.substring(0, 10);
	};
	
	$scope.getTime = function(str) {
		return str.substring(11);
	};
});

mainpageApp.directive('tooltip', function(){
	return {
		restrict: 'A',
		link: function(scope, element, attrs) {
			$(element).hover(function() {
				    $(element).tooltip('show');
				}, function() {
					$(element).tooltip('hide');
				});
		}
	};
});
