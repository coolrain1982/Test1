'use strict';

angular.module('pay-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('pay-order').controller("payOrderController",[
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		
		$scope.backToDetail = function() {
			orderTable.action1Dialog.hide();
			orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
		};

		switch ($state.current.name) {
		case "unpayOrder":
			orderTable.title = "待支付订单";
			orderTable.doUrl = "userorder/getOrder.do";
			orderTable.canPay = function(item) {
				if (item.status == 2) {
					return true;
				}
				return false;
			};
			
			orderTable.detailAction1Show = function(item) {
				if (item.status == 2) {
					return true;
				}
				return false;
			};
			orderTable.detailAction1Title = function(item) {
				if (item.status == 2) {
					return "支付";
				}
				return "";
			};
			
			orderTable.action1Dialog = $modal({
				scope : $scope,
				templateUrl : 'angular/Order/PayOrder/pay.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				backdrop:'static',
				keyboard:false,
			});
			
			orderTable.detailAction1 = function(item) {
				item.action1Error = "";
				orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
			};
			
			$scope.calcPayMoney = function(item) {
				return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
			}
			
			$scope.calcWithdrawFee = function(item) {
				return (commFunc.calcTotal(item).toFixed(2) * 0.001).toFixed(2);
			}
			
			$scope.showHelp = function() {
				return "<p align='left'>1、手机支付宝打开【账单】" +
						"<br/>2、选中转款记录点击进入【账单详情】" +
						"<br/>3、点击【创建时间】后可以看到【订单号】</p>"
			}
			
			$scope.$watch('orderTable.selectItem.payInfo.sn', function(newVal, oldVal) {
	    	    if (newVal && newVal!=oldVal) {
	    	    	if (newVal.length >= 8) {
	    	    		$scope.orderTable.selectItem.payInfo.sn = newVal.substr(0, 8);
	    	    	}
	    	    }	
	    	    
	    	    if (newVal && newVal!=oldVal) {
	    	    	if (parseInt(newVal) != newVal) {
	    	    		$scope.orderTable.selectItem.payInfo.sn = oldVal
	    	    	}
	    	    }
	    	});
			
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_currency_alt";
		orderTable.queryStatus = 2;
	    

		}]);