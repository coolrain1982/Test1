'use strict';

angular.module('admin-process-order', [ 'chieffancypants.loadingBar', 'ngAnimate' ])
		.config(function(cfpLoadingBarProvider) {
			cfpLoadingBarProvider.includeSpinner = true;
		});

angular.module('admin-process-order').controller("adminProcessOrderController",[
 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
 'cfpLoadingBar','$location','$anchorScroll','commFunc',
	function($state, $stateParams, $modal, orderTable,
			$scope, $http, $timeout, cfpLoadingBar,
			$location, $anchorScroll, commFunc) {
	 
	orderTable.reset();
	$scope.orderTable = orderTable;
	$scope.commFunc = commFunc;
	$scope.doing = false;
	
	orderTable.detailAction1Show = function(item) {
		if (item.status == 1 || item.status==4 || item.status==5) {
			return true;
		}
		return false;
	};
	
	orderTable.detailAction1Title = function(item) {
		switch(item.status) {
		case 1:
			return "审核订单";
		case 4:
			return "审核支付信息";
		case 5:
			return "分配订单";
		default:
			return "";
		}
	};
	
	$scope.auditDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditOrder/audit.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.auditConfirmDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditOrder/confirm.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.auditPayDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditPay/audit.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.auditPayConfirmDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditPay/confirm.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.auditBackToDetail = function() {
		$scope.auditDialog.hide();
		orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
	}
	
	$scope.auditPayBackToDetail = function() {
		$scope.auditPayDialog.hide();
		orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
	}
	
	$scope.checkAuditContent = function(item) {
		item.auditError = null;
		
		if (!item.auditContent || item.auditContent.length < 1) {
			item.auditError = "请输入审核意见!";
			return false;
		}
		
		if (item.auditContent.length > 100) {
			item.auditError = "审核意见不能超过100个字符!";
			return false;
		}
		
		return true;
	}
	
	$scope.calcPayMoney = function(item) {
		return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
	}
	
	$scope.showAuditPassConfirm = function(item) {
		if (!$scope.checkAuditContent(item)) {
			return;
		}
		
		$scope.auditDialog.hide();
		item.auditError = null;
		item.auditStatus = 2;
		$scope.auditConfirmTitle = "确定审核通过该订单吗？";
		$scope.auditConfirmDialog.$promise.then($scope.auditConfirmDialog.show);
	}
	
	$scope.showAuditRejectConfirm = function(item) {
    	if (!$scope.checkAuditContent(item)) {
			return;
		}
    	$scope.auditDialog.hide();
    	item.auditError = null;
    	item.auditStatus = 3;
    	$scope.auditConfirmTitle = "确定拒绝该订单吗？";
    	$scope.auditConfirmDialog.$promise.then($scope.auditConfirmDialog.show);
	}
	
	$scope.auditConfirmCancel = function() {
    	$scope.auditDialog.$promise.then($scope.auditDialog.show);
    }
	
	$scope.auditStart = function(item) {
    	item.auditError = "";
		$scope.doing = true;
		$scope.auditTimeout = $timeout(function() {
			cfpLoadingBar.start();}
		, 2000);
	}
	
	$scope.auditComplete = function(result) {
		if ($scope.auditTimeout) {
			$timeout.cancel($scope.auditTimeout);
		}
		cfpLoadingBar.complete();
		$scope.doing = false;
		$scope.auditConfirmDialog.hide();
		if (result) {
			$state.go($state.current, {}, {reload:true});
		} else {
			$scope.auditDialog.$promise.then($scope.auditDialog.show);
		}
	};
	
	$scope.auditConfirmOK = function(item) {
		$scope.auditStart(item);
		$http.get("admin/auditOrder.do",
				{  params:{
			    status: item.auditStatus,
			    orderid: item.order_id,
			    auditmark: item.auditContent
				}
		}).success(function(res) {
			if (res && res.status == 1) {
				//重新刷新当前的table
			$scope.auditComplete(true);
			} else if (res && res.status == 0) {
				item.auditError = res.error;
				$scope.auditComplete(false);
			} else {
				$scope.auditComplete(false);
				window.location.href = res;
			}
		}).error(function(data) {
			$scope.auditComplete(false);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
	//加载付款信息
	$scope.loadPayInfo = function(item) { 
		item.payInfos = [];
		item.auditPayError = null;
		$http.get("payinfo/getPayInfo.do",
			    { params:{
			        orderid: item.order_id,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				item.payInfos = res.payInfo;
			} else if (res && res.status == 0) {
				item.auditPayError = res.error;
			} else {
				window.location.href = res;
			}
	    }).error(function(data) {
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
	
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
	orderTable.statusClick = function(item) {
		if (orderTable.canClick(item)) {
			orderTable.selectItem = item;
			$scope.showAuditInfoModal.$promise.then($scope.showAuditInfoModal.show);
		}
	}
	
	switch ($state.current.name) {
	case "adminProcessOrder":
		orderTable.title = "待处理订单";
		orderTable.doUrl = "admin/getProcessOrder.do";
		orderTable.icon = "icon_error-circle";
		orderTable.queryCommand = 1;
		
		break;
	case "adminAllOrder":
		orderTable.title = "所有订单";
		orderTable.doUrl = "admin/getAllOrder.do";
		orderTable.icon = "icon_menu-circle_alt";
		orderTable.queryCommand = 2;
		break;
	default:
		return;
	}
	
	orderTable.showAction1 = function(item) {
		switch(item.status) {
		case 1:
			item.auditError = null;
			$scope.auditDialog.$promise.then($scope.auditDialog.show);
			return;
		case 4:
			item.auditPayError = null;
			$scope.auditPayDialog.$promise.then($scope.auditPayDialog.show);
			$scope.loadPayInfo(item);
			return;
		case 5:
			
		default:
			return;
		}
	}   

	} ]);