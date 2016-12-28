'use strict';

// Define the `loginApp` module
var mainpageApp = angular.module('mainpageApp', ['ngAnimate', 'mgcrea.ngStrap', 'ngSanitize',
    'ui.router', 'order-home', 'new-order', 'image-upload', 'ui.slimscroll',
    'order-table', 'process-order', 'doing-order', 'new-notice', 'summernote', 
    'profile', 'admin-process-order', 'admin-doing-order', 'change-password',
    'base-data', 'user-man'
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
	    .state('processOrder', {
	    	url : '/processOrder',
	    	templateUrl: 'angular/Order/processOrder.html'
	    }).state('doingOrder', {
	    	url : '/doingOrder',
	    	templateUrl: 'angular/Order/DoingOrder/doingOrder.html'
	    })
	    .state('allOrder', {
	    	url : '/allOrder',
	    	templateUrl: 'angular/Order/HisOrder/allOrder.html'
	    }).state('adminProcessOrder', {
	    	url : '/adminProcessOrder',
	    	templateUrl: 'admin/processOrder.html'
	    }).state('adminDoingOrder', {
	    	url : '/adminDoingOrder',
	    	templateUrl: 'admin/doingOrder.html'
	    }).state('adminAllOrder', {
	    	url : '/adminAllOrder',
	    	templateUrl: 'admin/allOrder.html'
	    }).state('csOrder', {
	    	url : '/csOrder',
	    	templateUrl: 'cs/csOrder.html'
	    }).state('csRejectOrder', {
	    	url : '/csRejectOrder',
	    	templateUrl: 'cs/csRejectOrder.html'
	    }).state('csAllOrder', {
	    	url : '/csAllOrder',
	    	templateUrl: 'cs/allCsOrder.html'
	    }).state('profile', {
	    	url : '/profile',
	    	templateUrl: 'angular/User/profile.html'
	    }).state('changepassword', {
	    	url : '/changepassword',
	    	templateUrl: 'angular/User/changepassword.html'
	    }).state('exchangeMan', {
	    	url : '/exchangeMan',
	    	templateUrl: 'admin/Base/exchange.html'
	    }).state('commisionMan', {
	    	url : '/commisionMan',
	    	templateUrl: 'admin/Base/commision.html'
	    }).state('paypalMan', {
	    	url : '/paypalMan',
	    	templateUrl: 'admin/Base/paypal.html'
	    }).state('userInfoMan', {
	    	url : '/userInfoMan',
	    	templateUrl: 'admin/User/userinfo.html'
	    });
});

mainpageApp.controller("mainpageCtrl", function($location, $state, $stateParams, $scope, $http, $timeout) {
	
	$scope.userValid = false;
	
	//取登陆用户
	$http.get('security/getLoginUserName.do')
	.success(function(data) {
		if (data == "") {
			$scope.loginUserName = "未知用户";
		} else {
			$scope.loginUserName = data.username;
			if (data.authorities && data.authorities.length > 0) {
				switch (data.authorities[0].authority) {
				case "ROLE_USER": 
					$scope.loginUserRole = "VIP用户<b class='caret'></b>";
					break;
				case "ROLE_CS":
					$scope.loginUserRole = "客服<b class='caret'></b>";
					break;
				case "ROLE_ADMIN":
					$scope.loginUserRole = "管理员<b class='caret'></b>";
					break;
				}
			}
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
	
	$scope.chartToCs = function() { 
		var win = window.open("http://wpa.qq.com/msgrd?v=3&uin=2789220168&site=qq&menu=yes");
		$timeout(function() {win.close();}, 5000);
	}

	
	$state.go('notice', {}, {reload:true});
	
	$scope.goto = function(url) {
		$state.go(url, {}, {reload:true});
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
    this.showAction1 = function(item) {};
    this.showAction2 = function(item) {};
    this.isAdmin = false;
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
        this.showAction1 = function(item) {};
        this.showAction2 = function(item) {};
        this.isAdmin = false;
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
	
	this.calcTotal = function(item) {
		var temp = (item.product_unit_price
				+ item.product_unit_freight + item.product_unit_commission
				* item.discount / 100)
				* item.product_quantity;
		return (temp
				* (1 + parseFloat(item.paypal_rate) / 100) + item.paypal_fee).toFixed(2)
				* item.exchange_rate;
	};

	this.calcSum = function(item) {
		var temp = this.calcProductSum(item)
				* (1 + parseFloat(item.paypal_rate) / 100)
				+ item.paypal_fee;

		return temp;
	};

	this.calcProductSum = function(item) {
		var temp = (item.product_unit_price
				+ item.product_unit_freight + item.product_unit_commission
				* item.discount / 100)
				* item.product_quantity;

		return temp;
	};
	this.getSrvType = function(item) {
		switch (item.type) {
		case 1:
			return "只购买商品";
		case 2:
			return "购买商品+review";
		case 3:
			return "购买商品+review+feedback";
		default:
			return "无效";
		}
	}
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
