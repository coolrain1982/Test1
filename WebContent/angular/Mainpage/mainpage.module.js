'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', ['ngAnimate', 'mgcrea.ngStrap',
    'ui.router', 'order-home', 'new-order', 'image-upload', 
    'order-table', 'audit-order', 'pay-order', 'reject-order', 'doing-order',
    'finish-order', 'all-order', 'new-notice', 'summernote'
]);

mainpageApp.config(function($locationProvider, $stateProvider, $urlRouterProvider) {
//	$locationProvider.html5Mode(true);
	$stateProvider
	    .state('notice', {
	    	url : '/notice',
	    	templateUrl: 'notice.html'
	    })
	    .state('releaseNotice', {
	    	url : '/releaseNotice',
	    	templateUrl: 'admin/releaseNotice.html'
	    })
	    .state('summary', {
	    	url : '/summary',
	    	templateUrl: 'summary.html'
	    })
	    .state('newOrder', {
	    	url : '/newOrder',
	    	templateUrl: 'angular/Order/NewOrder/newOrder.html'
	    })
	    .state('unconfirmOrder', {
	    	url : '/unconfirmOrder',
	    	templateUrl: 'angular/Order/AuditOrder/unconfirmOrder.html'
	    })
	    .state('unpayOrder', {
	    	url : '/unpayOrder',
	    	templateUrl: 'angular/Order/PayOrder/unpayOrder.html'
	    })
	    .state('doingOrder', {
	    	url : '/doingOrder',
	    	templateUrl: 'angular/Order/DoingOrder/doingOrder.html'
	    })
	    .state('rejectOrder', {
	    	url : '/rejectOrder',
	    	templateUrl: 'angular/Order/RejectOrder/rejectOrder.html'
	    })
	    .state('finishOrder', {
	    	url : '/finishOrder',
	    	templateUrl: 'angular/Order/FinishOrder/finishOrder.html'
	    })
	    .state('allOrder', {
	    	url : '/allOrder',
	    	templateUrl: 'angular/Order/HisOrder/allOrder.html'
	    })
	    .state('csOrder', {
	    	url : '/csOrder',
	    	templateUrl: 'cs/csOrder.html'
	    })
	    .state('csRejectOrder', {
	    	url : '/csRejectOrder',
	    	templateUrl: 'cs/csRejectOrder.html'
	    })
	    .state('csAllOrder', {
	    	url : '/csAllOrder',
	    	templateUrl: 'cs/allCsOrder.html'
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
	
	$scope.noticeSub = false;
	$scope.orderSub = false;
	
	$scope.showNoticeSub = function() {
		$scope.noticeSub = !$scope.noticeSub
	}
	
	$scope.showOrderSub = function() {
		$scope.orderSub = !$scope.orderSub;
	}
});

mainpageApp.service('orderTable', function() {
	this.title="";
	this.icon="";
	this.queryStatus=-1;
    this.doUrl = "";
    this.selectItem = null;
    this.canClick = function(item) {return false};
    this.canPay = function(item) {return false};
    this.detailDialog = null;
    this.detailAction1 = function(item) {};
    this.detailAction2 = function(item) {};
    this.detailAction1Show = function(item) {return false};
    this.detailAction1Title = function(item) {return ""};
    this.detailAction2Show = function(item) {return false};
    this.detailAction2Title = function(item) {return ""};
    this.statusClick = function(item) {};
    this.action1Dialog = null;
    this.reset = function() {
    	this.title="";
    	this.detailDialog = null;
    	this.icon="";
    	this.queryStatus=-1;
        this.doUrl = "";
        this.selectItem = null;
        this.canClick = function(item) {return false};
        this.canPay = function(item) {return false};
        this.detailAction1 = function(item) {};
        this.detailAction2 = function(item) {};
        this.detailAction1Show = function(item) {return false};
        this.detailAction1Title = function(item) {return ""};
        this.detailAction2Show = function(item) {return false};
        this.detailAction2Title = function(item) {return ""};
        this.statusClick = function(item) {};
        this.action1Dialog = null;
    }  
});

mainpageApp.service('commFunc', function() {
    this.getDate = function(str) {
		return str.substring(0, 10);
	};

	this.getTime = function(str) {
		return str.substring(11);
	};

	this.formatDate = function(temp) {
		var temp = new Date(temp);
		return temp.getFullYear()
				+ "-"
				+ this
						.foo((temp.getMonth() + 1),
								2) + "-"
				+ this.foo(temp.getDate(), 2) + " "
				+ this.foo(temp.getHours(), 2)
				+ ":"
				+ this.foo(temp.getMinutes(), 2)
				+ ":"
				+ this.foo(temp.getSeconds(), 2);
	};

	this.foo = function(str, len) {
		for (var i = 0; i < len; i++) {
			str = '0' + str;
		}
		return str.substring(str.length - len,
				str.length);
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
