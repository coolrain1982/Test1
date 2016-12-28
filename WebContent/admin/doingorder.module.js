'use strict';

angular.module('admin-doing-order', [ 'chieffancypants.loadingBar', 'ngAnimate' ])
		.config(function(cfpLoadingBarProvider) {
			cfpLoadingBarProvider.includeSpinner = false;
		});

angular.module('admin-doing-order').controller(
		"adminDoingOrderController",
		[		'$state',
				'$stateParams',
				'$modal',
				'orderTable',
				'$scope',
				'$http',
				'$timeout',
				'cfpLoadingBar',
				'$location',
				'$anchorScroll',
				'commFunc',
	function($state, $stateParams, $modal, orderTable, $scope,
			$http, $timeout, cfpLoadingBar, $location,
			$anchorScroll, commFunc) {
	
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		orderTable.isAdmin = true;
	
		orderTable.title = "进行中订单";
	
		orderTable.doUrl = "admin/getDoingOrder.do";
		orderTable.queryCommand = 3;
	
		orderTable.icon = "arrow_carrot-2right_alt";
		orderTable.queryStatus = 4;
		
		$scope.calcPayMoney = function(item) {
			return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
		}
		
		//加载付款信息------------------------------------------------------------------
		$scope.loadPayInfo = function(item) { 
			if (item.payInfos) {
				return;
			}
			
			$http.get("payinfo/getPayInfo.do",
				    { params:{
				        orderid: item.order_id,
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					item.payInfos = res.payInfo;
					if (item.payInfos.length > 0) {
						item.auditPayInfoRemark = item.payInfos[0].auditRemark;
					}
				} else if (res && res.status == 0) {
					
				} else {
					$state.go($state.current, {}, {reload:true});
				}
		    }).error(function(data) {
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}
		
		//详单表状态栏点击信息显示相关------------------------------------------------------------------
		orderTable.canClick = function(item) {
			if (item.status == 5 || item.status == 10) {
				return true;
			}
			return false;
		}
		
		$scope.showPayInfoModal = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/PayOrder/info.template.html',
			show : false,
			animation: 'am-fade-and-slide-top'
		});
		
		orderTable.statusClick = function(item) {
			if (orderTable.canClick(item)) {
				orderTable.selectItem = item;
				switch (item.status) {
				case 5:
				case 10:
					$scope.showPayInfoModal.$promise.then($scope.showPayInfoModal.show);
					$scope.loadPayInfo(item);
					return;
				default:
					return;
				}
			}
		}

} ]);