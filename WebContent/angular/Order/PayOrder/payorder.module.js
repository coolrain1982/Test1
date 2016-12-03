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
			
			orderTable.detailAction1 = function(item) {};
			
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_currency_alt";
		orderTable.queryStatus = 2;
	    

		}]);