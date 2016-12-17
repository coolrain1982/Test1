'use strict';

angular.module('payment', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

angular.module('payment').controller("payController",[
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		$scope.idle = false;
		$scope.fail = false;
		$scope.orderItem = {}; 
		 
		$scope.orderId = $stateParams.id;
		if (!$scope.orderId || $scope.orderId < 0) {
			$scope.fail = true;
			$state.go('unpayOrder');
			return;
		}
		
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		
		$scope.start = function() {
			$scope.loadOrderPayment = $timeout(function() {
				cfpLoadingBar.start();
				$scope.idle = false;
			}, 2000);
		};
		
		$scope.complete = function () {
			if ($scope.loadOrderPayment) {
				$timeout.cancel($scope.loadOrderPayment);
			}
			cfpLoadingBar.complete();
			$scope.idle = true;
		};
		
		//查询订单付款信息
		$scope.start();
		$http.get("userorder/getPayment.do",
				{ params:{
					orderId: $scope.orderId,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				$scope.orderItem = res.order;
				$scope.fail = false;
			} else if (res && res.status == 0) {
				$scope.fail = true;
				$scope.failInfo = res.error;
			} else {
				window.location.href = "index.do";
			}
			$scope.complete();
		}).error(function(data) {
			$scope.complete();
			alert("发生错误，请重新登录网站！");
			window.location.href = "logout";
		});

}]);