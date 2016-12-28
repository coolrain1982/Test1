'use strict';

angular.module('admin-process-order', [ 'chieffancypants.loadingBar', 'ngAnimate' ])
		.config(function(cfpLoadingBarProvider) {
			cfpLoadingBarProvider.includeSpinner = false;
		});

angular.module('admin-process-order').controller("adminProcessOrderController",[
 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
 'cfpLoadingBar','$location','$anchorScroll','commFunc',
	function($state, $stateParams, $modal, orderTable,
			$scope, $http, $timeout, cfpLoadingBar,
			$location, $anchorScroll, commFunc) {
	
	//信息初始化------------------------------------------------------------------
	orderTable.reset();
	$scope.orderTable = orderTable;
	$scope.commFunc = commFunc;
	$scope.doing = false;
	orderTable.isAdmin = true;
	
	//整体页面相关------------------------------------------------------------------
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
	
	orderTable.detailAction1Show = function(item) {
		if (item.status == 1 || item.status==4) {
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
		default:
			return "";
		}
	};
	
	//审核订单相关-----------------------------------------------------------------------------
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
	
	$scope.auditBackToDetail = function() {
		$scope.auditDialog.hide();
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
	
	$scope.auditConfirmCancel = function() {
		if ($scope.doing) {
			return;
		}
		
		$scope.auditConfirmDialog.hide();
    	$scope.auditDialog.$promise.then($scope.auditDialog.show);
    }
	
	$scope.auditConfirmOK = function(item) {
		if ($scope.doing) {
			return;
		}
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
				$state.go($state.current, {}, {reload:true});
			}
		}).error(function(data) {
			$scope.auditComplete(false);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
	
	//审核支付信息相关---------------------------------------------------------------------
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
	
	$scope.auditPayBackToDetail = function() {
		$scope.auditPayDialog.hide();
		orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
	}
	
	$scope.calcPayMoney = function(item) {
		return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
	}
	
	$scope.checkAuditPayContent = function(item) {
		item.auditPayError = null;
		
		if (!item.auditPayContent || item.auditPayContent.length < 1) {
			item.auditPayError = "请输入审核意见!";
			return false;
		}
		
		if (item.auditPayContent.length > 100) {
			item.auditPayError = "审核意见不能超过100个字符!";
			return false;
		}
		
		return true;
	}
	
	$scope.showAuditPayPassConfirm = function(item) {
		if (!$scope.checkAuditPayContent(item)) {
			return;
		}
		
		$scope.auditPayDialog.hide();
		item.auditPayError = null;
		item.auditPayStatus = 5;
		$scope.auditPayConfirmTitle = "确定审核通过该订单的支付信息吗？";
		$scope.auditPayConfirmDialog.$promise.then($scope.auditPayConfirmDialog.show);
	}
	
	$scope.showAuditPayRejectConfirm = function(item) {
    	if (!$scope.checkAuditPayContent(item)) {
			return;
		}
    	$scope.auditPayDialog.hide();
    	item.auditPayError = null;
    	item.auditPayStatus = 6;
    	$scope.auditPayConfirmTitle = "确定拒绝该订单的支付信息吗？";
    	$scope.auditPayConfirmDialog.$promise.then($scope.auditPayConfirmDialog.show);
	}
	
	$scope.auditPayConfirmCancel = function() {
		if ($scope.doing) {
			return;
		}
		
		$scope.auditPayConfirmDialog.hide();
    	$scope.auditPayDialog.$promise.then($scope.auditPayDialog.show);
    }
	
	$scope.auditPayStart = function(item) {
    	item.auditPayError = "";
		$scope.doing = true;
		$scope.auditPayTimeout = $timeout(function() {
			cfpLoadingBar.start();}
		, 2000);
	}
	
	$scope.auditPayComplete = function(result) {
		if ($scope.auditPayTimeout) {
			$timeout.cancel($scope.auditPayTimeout);
		}
		cfpLoadingBar.complete();
		$scope.doing = false;
		$scope.auditPayConfirmDialog.hide();
		if (result) {
			$state.go($state.current, {}, {reload:true});
		} else {
			$scope.auditPayDialog.$promise.then($scope.auditPayDialog.show);
		}
	};
	
	$scope.auditPayConfirmOK = function(item) {
		if ($scope.doing) {
			return;
		}
		$scope.auditPayStart(item);
		$http.get("admin/auditPay.do",
				{  params:{
			    status: item.auditPayStatus,
			    orderid: item.order_id,
			    payinfoid: item.payinfo_id,
			    auditmark: item.auditPayContent
				}
		}).success(function(res) {
			if (res && res.status == 1) {
				//重新刷新当前的table
			$scope.auditPayComplete(true);
			} else if (res && res.status == 0) {
				item.auditPayError = res.error;
				$scope.auditPayComplete(false);
			} else {
				$scope.auditPayComplete(false);
				$state.go($state.current, {}, {reload:true});
			}
		}).error(function(data) {
			$scope.auditPayComplete(false);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
	
	//加载付款信息------------------------------------------------------------------
	$scope.loadPayInfo = function(item) { 
		if (item.payInfos) {
			return;
		}
		
		item.auditPayError = null;
		$http.get("payinfo/getPayInfo.do",
			    { params:{
			        orderid: item.order_id,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				item.payInfos = res.payInfo;
				if (item.payInfos.length > 0) {
					item.auditPayInfoRemark = item.payInfos[0].auditRemark;
					item.payinfo_id = item.payInfos[0].id;
				}
			} else if (res && res.status == 0) {
				item.auditPayError = res.error;
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
		if (item.status == 3 || item.status == 4 || item.status == 5 || item.status == 6) {
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
	$scope.showAuditPayInfoModal = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditPay/info.template.html',
		show : false,
		animation: 'am-fade-and-slide-top'
	});
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
			case 3:
				$scope.showAuditInfoModal.$promise.then($scope.showAuditInfoModal.show);
				return;
			case 4:
				$scope.showPayInfoModal.$promise.then($scope.showPayInfoModal.show);
				$scope.loadPayInfo(item);
				return;
			case 5:
			case 6:
				$scope.showAuditPayInfoModal.$promise.then($scope.showAuditPayInfoModal.show);
				$scope.loadPayInfo(item);
				return;
			default:
				return;
			}
		}
	}

	} ]);