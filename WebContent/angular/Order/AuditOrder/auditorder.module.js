'use strict';

angular.module('audit-order', [ 'chieffancypants.loadingBar', 'ngAnimate' ])
		.config(function(cfpLoadingBarProvider) {
			cfpLoadingBarProvider.includeSpinner = true;
		});

angular.module('audit-order').controller("auditOrderController",[
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
	}

	switch ($state.current.name) {
	case "unconfirmOrder":
		orderTable.title = "待确认订单";
		orderTable.doUrl = "userorder/getOrder.do";
		orderTable.canPay = function(item) {
			if (item.status == 2) {
				return true;
			}
			return false;
		};
		break;
	case "csOrder":
		orderTable.title = "待审核订单";
		orderTable.doUrl = "cs/getOrder.do";
		orderTable.canPay = function(item) {
			return false;
		};
		orderTable.detailAction1 = function(item) {};
		orderTable.detailAction1Show = function(item) {
			if (item.status == 1) {
				return true;
			}
			return false;
		};
		orderTable.detailAction1Title = function(item) {
			if (item.status == 1) {
				return "审核";
			}
			return "";
		};
		
		orderTable.action1Dialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/AuditOrder/audit.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		$scope.auditConfirmDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/Common/confirm.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		break;
	default:
		orderTable.title = "未知";
	}
	
	orderTable.icon = "icon_question_alt";
	orderTable.queryStatus = 1;
	
	$scope.checkAuditContent = function(item) {
		item.auditError = "";
		
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
		
		orderTable.action1Dialog.hide();
		item.auditError = "";
		item.auditStatus = 2;
		$scope.confirmTitle = "确定审核通过该订单吗？";
		$scope.auditConfirmDialog.$promise.then($scope.auditConfirmDialog.show);
	}
	
	$scope.showAuditRejectConfirm = function(item) {
    	if (!$scope.checkAuditContent(item)) {
			return;
		}
    	orderTable.action1Dialog.hide();
    	item.auditError = "";
    	item.auditStatus = 3;
    	$scope.confirmTitle = "确定拒绝该订单吗？";
    	$scope.auditConfirmDialog.$promise.then($scope.auditConfirmDialog.show);
	}
    
	$scope.confirmCancel = function() {
    	orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
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
			orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
		}
	};
	
	$scope.confirmOK = function(item) {
	   $scope.auditStart(item);
	   $http.get("cs/auditOrder.do",
				{ params:{
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
			
		}).error(function() {
			$scope.auditComplete(false);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
   }
    

	} ]);