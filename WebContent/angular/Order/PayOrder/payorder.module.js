'use strict';

angular.module('pay-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

angular.module('pay-order').controller("payOrderController",[
	 '$state','$stateParams','$modal','orderTable','$scope','$http','$timeout',
	 'cfpLoadingBar','$location','$anchorScroll','commFunc',
		function($state, $stateParams, $modal, orderTable,
				$scope, $http, $timeout, cfpLoadingBar,
				$location, $anchorScroll, commFunc) {
		 
		orderTable.reset();
		$scope.orderTable = orderTable;
		$scope.commFunc = commFunc;
		$scope.doing = false;
		
		$scope.payBackToDetail = function() {
			orderTable.action1Dialog.hide();
			orderTable.detailDialog.$promise.then(orderTable.detailDialog.show);
		};

		switch ($state.current.name) {
		case "unpayOrder":
			orderTable.title = "待支付订单";
			orderTable.doUrl = "userorder/getOrder.do";
			orderTable.canPay = function(item) {
				if (item.status == 2) {
					return true;
				}
				return false;
			};
			
			orderTable.detailAction1Show = function(item) {
				if (item.status == 2) {
					return true;
				}
				return false;
			};
			orderTable.detailAction1Title = function(item) {
				if (item.status == 2) {
					return "支付";
				}
				return "";
			};
			
			orderTable.action1Dialog = $modal({
				scope : $scope,
				templateUrl : 'angular/Order/PayOrder/pay.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				backdrop:'static',
				keyboard:false,
			});
			
			orderTable.detailAction1 = function(item) {
				item.action1Error = "";
				orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
			};
			
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
		    	orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
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
					orderTable.action1Dialog.$promise.then(orderTable.action1Dialog.show);
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
				
				orderTable.action1Dialog.hide();
				item.payError = "";
				$scope.confirmTitle = "确定提交支付信息吗？";
				$scope.payConfirmDialog.$promise.then($scope.payConfirmDialog.show);
			}
			
			$scope.confirmOK = function(item) {
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
							$state.go($state.current, {}, {reload:true});
						}
						
					}).error(function(data) {
						$scope.payComplete(false);
						alert("发生错误，请重新登录！");
						window.location.href = "logout";
					});
			   }
			
			break;
		default:
			orderTable.title = "未知";
		}
		
		orderTable.icon = "icon_currency_alt";
		orderTable.queryStatus = 2;
	    

		}]);