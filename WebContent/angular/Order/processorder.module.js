'use strict';

var processOrderModule = angular.module('process-order', [ 'chieffancypants.loadingBar', 'ngAnimate' ]);
processOrderModule.config(function(cfpLoadingBarProvider) {
			cfpLoadingBarProvider.includeSpinner = false;
		});

processOrderModule.controller("processOrderController",[
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
		if (item.status == 2 || item.status==6 || item.status==20) {
			return true;
		}
		return false;
	};
	
	orderTable.detailAction1Title = function(item) {
		switch(item.status) {
		case 2:
		case 6:
			return "去支付";
		case 20:
			return "确认完成";
		default:
			return "";
		}
	};
	
	$scope.payDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/PayOrder/pay.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.payBackToDetail = function() {
		$scope.payDialog.hide();
		orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
	}
	
	$scope.calcPayMoney = function(item) {
		return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
	}
	
	$scope.calcWithdrawFee = function(item) {
		return (commFunc.calcTotal(item).toFixed(2) * 0.001).toFixed(2);
	}
	
	$scope.showHelp = function() {
		return "<p align='left'>1、手机支付宝打开【账单】" +
				"<br/>2、选中转款记录点击进入【账单详情】" +
				"<br/>3、点击【创建时间】后可以看到【订单号】</p>"
	}
	
	$scope.$watch('orderTable.selectItem.payInfo.sn', function(newVal, oldVal) {
	    if (newVal && newVal!=oldVal) {
	    	if (newVal.length >= 8) {
	    		$scope.orderTable.selectItem.payInfo.sn = newVal.substr(0, 8);
	    	}
	    }	
	    
	    if (newVal && newVal!=oldVal) {
	    	if (parseInt(newVal) != newVal) {
	    		$scope.orderTable.selectItem.payInfo.sn = oldVal;
	    	}
	    }
	});
	
	$scope.payConfirmDialog = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/PayOrder/confirm.template.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.confirmCancel = function() {
    	$scope.payDialog.$promise.then($scope.payDialog.show);
    }
	
	$scope.payStart = function(item) {
    	item.payError = "";
		$scope.doing = true;
		$scope.payTimeout = $timeout(function() {
			cfpLoadingBar.start();}
		, 2000);
	}
	
	$scope.payComplete = function(result) {
		if ($scope.payTimeout) {
			$timeout.cancel($scope.payTimeout);
		}
		cfpLoadingBar.complete();
		$scope.doing = false;
		$scope.payConfirmDialog.hide();
		if (result) {
			$state.go($state.current, {}, {reload:true});
		} else {
			$scope.payDialog.$promise.then($scope.payDialog.show);
		}
	};
	
	$scope.checkPayInfo = function(item) {
		item.payError = "";
		
		if (!item.payInfo || !item.payInfo.account || item.payInfo.account.length < 1) {
			item.payError = "请输入您的支付宝账号!";
			return false;
		}
		
		if (item.payInfo.account.length > 50) {
			item.payError = "支付宝账号不能超过50个字符!";
			return false;
		}
		
		if (!item.payInfo.sn || item.payInfo.sn < 1) {
			item.payError = "请输入订单号后8位数字!";
			return false;
		}
		
		if (!parseInt(item.payInfo.sn)|| parseInt(item.payInfo.sn)<=9999999 || 
				parseInt(item.payInfo.sn)>=100000000) {
			item.payError = "请正确输入订单号后8位数字!";
			return false;
		}
		
		return true;
	}
	
	$scope.showPayConfirm = function(item) {
		if (!$scope.checkPayInfo(item)) {
			return;
		}
		
		$scope.payDialog.hide();
		item.payError = "";
		$scope.payConfirmTitle = "确定提交支付信息吗？";
		$scope.payConfirmDialog.$promise.then($scope.payConfirmDialog.show);
	}
	
	$scope.payConfirmCancel = function() {
		$scope.payDialog.$promise.then($scope.payDialog.show);
    }
	
	$scope.payConfirmOK = function(item) {
		   $scope.payStart(item);
		   $http.get("userorder/payOrder.do",
					{ params:{
						orderId: item.order_id,
				        payaccount: item.payInfo.account,
				        paysn: item.payInfo.sn,
				        money:$scope.calcPayMoney(item)
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					//重新刷新当前的table
					$scope.payComplete(true);
				} else if (res && res.status == 0) {
					item.payError = res.error;
					$scope.payComplete(false);
				} else {
					$scope.payComplete(false);
					window.location="/login.html";
				}
				
			}).error(function(data) {
				$scope.payComplete(false);
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
				}
			} else if (res && res.status == 0) {
				item.auditPayError = res.error;
			} else {
				window.location="/login.html";
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
	
	$scope.showPayInfoModal = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/PayOrder/info.template.html',
		show : false,
		animation: 'am-fade-and-slide-top'
	});
	$scope.showAuditPayInfoModal = $modal({
		scope : $scope,
		templateUrl : 'angular/Order/AuditPay/info.template.html',
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
	
	//初始化相关信息
	switch ($state.current.name) {
	case "processOrder":
		orderTable.title = "待我处理的订单";
		orderTable.doUrl = "userorder/getProcessOrder.do";
		orderTable.icon = "icon_error-circle";
		orderTable.queryCommand = 1;
		
		break;
	case "allOrder":
		orderTable.title = "我所有的订单";
		orderTable.doUrl = "userorder/getAllOrder.do";
		orderTable.icon = "icon_menu-circle_alt";
		orderTable.queryCommand = 2;
		break;
	default:
		return;
	}
	
	orderTable.showAction1 = function(item) {
		switch(item.status) {
		case 2:
		case 6:
			item.payError = "";
			$scope.payDialog.$promise.then($scope.payDialog.show);
			return;
		case 20:
			return "已完成";
		default:
			return;
		}
	}   

	} ]);