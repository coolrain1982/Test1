'use strict';

angular.module('reject-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('reject-order').
    controller("rejectOrderController", [
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		
		orderTable.title = "待关闭订单";
		orderTable.canClick = function(item) {
			if (item.status == 3) {
				return true;
			}
			return false;
		}
		
		$scope.showAuditInfoModal = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/RejectOrder/rejectInfo.template.html',
			show : false,
			animation: 'am-fade-and-slide-top'
		});

		switch ($state.current.name) {
		case "rejectOrder":
			orderTable.doUrl = "userorder/getOrder.do";
			break;
		case "csRejectOrder":
			orderTable.doUrl = "cs/getOrder.do";
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_error-circle";
		orderTable.queryStatus = 3;
		
		orderTable.statusClick = function(item) {
			orderTable.selectItem = item;
			$scope.showAuditInfoModal.$promise.then($scope.showAuditInfoModal.show);
		}
	    
		}]);