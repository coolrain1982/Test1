'use strict';

angular.module('doing-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('doing-order').
    controller("doingOrderController", [
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		
		orderTable.title = "进行中订单";

		switch ($state.current.name) {
		case "doingOrder":
			orderTable.doUrl = "userorder/getOrder.do";
			break;
		case "csDoingOrder":
			orderTable.doUrl = "cs/getOrder.do";
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "arrow_carrot-2right_alt";
		orderTable.queryStatus = 4;
	    

		}]);