'use strict';

angular.module('all-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('all-order').
    controller("allOrderController", [
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		
		orderTable.title = "全部订单";

		switch ($state.current.name) {
		case "allOrder":
			orderTable.doUrl = "userorder/getOrder.do";
			break;
		case "csAllOrder":
			orderTable.doUrl = "cs/getOrder.do";
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_menu-circle_alt";
		orderTable.queryStatus = 99;
	    

		}]);