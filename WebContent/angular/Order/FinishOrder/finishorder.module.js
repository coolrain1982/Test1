'use strict';

angular.module('finish-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('finish-order').
    controller("finishOrderController", [
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		
		orderTable.title = "已完成订单";

		switch ($state.current.name) {
		case "finishOrder":
			orderTable.doUrl = "userorder/getOrder.do";
			break;
		case "csFinishOrder":
			orderTable.doUrl = "cs/getOrder.do";
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_check_alt";
		orderTable.queryStatus = 20;
	    

		}]);