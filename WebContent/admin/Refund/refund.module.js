var refundmanModule = angular.module('refund-man', [
		'chieffancypants.loadingBar', 'ngAnimate' ]);

refundmanModule.config(function(cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = false;
});

refundmanModule.controller("refundManController", [
		'$state',
		'$scope',
		'$modal',
		'$http',
		'cfpLoadingBar',
		'$timeout',
		'$popover',
		'orderTable',
		'commFunc',
		function($state, $scope, $modal, $http, cfpLoadingBar, $timeout,
				$popover, orderTable, commFunc) {

			$scope.showpage = true;
			$scope.commFunc = commFunc;
			$scope.refunderror = null;
			$scope.isSuccess = false;
			$scope.search = {
				str : "",
			};
			
			switch ($state.current.name) {
			case "noreviewRefund":
				$scope.statetitle="未留评退款";
				$scope.refundtype = 1;
				$scope.stateicon="icon-noreview";
				$scope.quanityShow = "退回佣金数量";
				$scope.postURL = "admin/addNoreviewRefund.do";
				$scope.calcRefund = function() {
					return ($scope.selectItem.exchange_rate*
							$scope.selectItem.fee2*
							$scope.selectItem.discount/100*
							$scope.newrefund.quanity).toFixed(2);
				}
				
				$scope.calcUnitCommisionRefund = function(item) {
					return item.fee2*item.discount/100+'(原价'+item.fee2+',折扣'+item.discount+'%)'
				}
				
				$scope.canRefund = function(item) {
					if (item.status < 5) {
						return false;
					}
					
					if (item.type == 2) {
						return true;
					}
					
					return false;
				}

				break;
			case "uncompleteRefund":
				$scope.refundtype = 2;
				$scope.statetitle="未完成退款";
				$scope.stateicon="icon-uncomplete";
				$scope.quanityShow = "未完成数量";
				$scope.postURL = "admin/addUncompleteRefund.do";
				
				$scope.calcRefund = function() {
					return (($scope.selectItem.product_unit_price + 
							$scope.selectItem.product_unit_commission*$scope.selectItem.discount/100 +
							$scope.selectItem.product_unit_freight)*
							$scope.newrefund.quanity*$scope.selectItem.exchange_rate).toFixed(2);
				}
				
				$scope.canRefund = function(item) {
					if (item.status < 5) {
						return false;
					}
					
					return true;
				}
				
				$scope.calcUnitCommisionRefund = function(item) {
					return item.product_unit_commission*item.discount/100+
					   '(原价'+item.product_unit_commission+',折扣'+item.discount+'%)'
				}
				
				break;
			default:
				return;
			}
			
			$scope.createFD = function() {
				var fd = new FormData();
    			fd.append("orderid", $scope.selectItem.order_id);
    			fd.append("money", $scope.calcRefund());
    			fd.append("sn", $scope.newrefund.sn);
    			fd.append("payee", $scope.newrefund.account);
    			fd.append("remark", $scope.newrefund.remark);
    			
    			return fd;
			}

			$scope.refresh = function() {
				$state.go($state.current, {}, {
					reload : true
				});
			}

			$scope.start = function() {
				cfpLoadingBar.start();
				$scope.showpage = false;
				$scope.doing = true;
				$scope.refunderror = null;
			};

			$scope.complete = function(result) {
				cfpLoadingBar.complete();
				$scope.doing = false;
				$scope.showpage = true;
			};

			$scope.queryParams = {
				url : "admin/getRefundOrderById.do",
				str : "",
			};

			$scope.searchOrderForRefund = function(event) {
				var keycode = window.event ? event.keyCode : event.which;
				if (keycode == 13) {
					$scope.search.str = $scope.search.str.replace(
							/(^\s*)|(\s*$)/g, "");
					if ($scope.search.str == "") {
						return;
					}
					$scope.queryParams.str = $scope.search.str;
					// 查询订单
					$scope.showRefundUI = false;
					$scope.start();
					$scope.orderList = null;
					$http.get($scope.queryParams.url, {
						params : {
							searchStr : $scope.queryParams.str,
						}
					}).success(function(res) {
						if (res && res.status == 1) {
							$scope.orderList = res.list;
						} else if (res && res.status == 0) {
							$scope.refunderror = res.error;
						} else {
							window.location = "/login.html";
						}
						$scope.complete();
					}).error(function() {
						$scope.complete();
						alert("发生错误，请重新登录！");
						window.location.href = "logout";
					});
				}
			}

			// 返回订单列表
			$scope.backToOrderList = function() {
				$scope.isSuccess = false;
			}
			
			$scope.modelDialog = $modal({
				scope : $scope,
				templateUrl : 'admin/Refund/detail.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
			});

			$scope.getUrlList = function(temp) {
				var urlArray = temp.split("@@");
				var rtnArray = [];
				for ( var i in urlArray) {
					if (urlArray[i] != "") {
						rtnArray.push("/MeiYabuy/upload/image/"
								+ urlArray[i]);
					}
				}

				return rtnArray;
			}

			$scope.showDetail = function(item) {
				$scope.selectItem = item;
				orderTable.selectItem = item;
				orderTable.detailDialog = $scope.modelDialog;
				$scope.modelDialog.$promise.then($scope.modelDialog.show);
			};

			$scope.getShowLink = function(temp) {
				if (temp.length > 100) {
					return temp.substring(0, 100) + "...";
				}
				return temp;
			};

			$scope.getStatusClass = function(temp) {
				switch (temp) {
				case 1:
					return "label-default";
				case 2:
					return "label-info";
				case 3:
					return "label-danger";
				case 4:
					return "label-success";
				case 5:
					return "label-primary";
				case 6:
					return "label-danger";
				case 7:
					return "label-warning";
				case 10:
					return "label-info";
				case 20:
					return "label-success";
				case 21:
					return "label-success";
				default:
					return "label-default";
				}
			}

			$scope.getStatus = function(temp) {
				switch (temp) {
				case 1:
					return "待确认";
				case 2:
					return "待支付";
				case 3:
					return "订单拒绝";
				case 4:
					return "已支付";
				case 5:
					return "支付成功";
				case 6:
					return "支付失败";
				case 7:
					return "进行中";
				case 10:
					return "已拨付";
				case 20:
					return "待确认完成";
				case 21:
					return "已完成";
				default:
					return "无效";
				}
			}
			
			$scope.getCursor = function(item) {
				if (orderTable.canClick(item)) {
					return "pointer";
				} else {
					return "default";
				}
			}
			
			$scope.getTypeDesc = function(item) {
				return commFunc.getSrvMode(item.find_product_mode) + "+" + commFunc.getSrvType(item);
			}
			
			//显示用户详细信息--------------------------------------------------------------------
			$scope.userInfoDialog = $modal({
				scope : $scope,
				templateUrl : 'angular/User/userInfo.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
			});
			
			$scope.showUserInfo = function(userName) {
				$scope.userInfoDialog.$promise.then($scope.userInfoDialog.show);
				$scope.showuser = {};
				for(var temp in $scope.existsuser) {
					if ($scope.existsuser[temp].name == userName) {
						$scope.showuser = $scope.existsuser[temp];
						return;
					}
				}
				
				$scope.showuser.error = null;
				
				$http.get('user/getUserInfo.do',{ params:{
			        username:userName}}).success(function(data) {
		    		if (data.error) {
		    			$scope.showuser.error = data.error;
		    		} else {
		    			$scope.showuser = data.userInfo;
		    			$scope.existsuser.push(data.userInfo);
		    		}
		    	}).error(function(data){
			    	
		    	});	
			}
			
			//获取付款成功的账号信息，填入退款账户栏中作为默认值
			$scope.getPaySuccessAccount = function(item) {
				if (!item.payInfos) {
					return;
				}
				for (var idx in item.payInfos) {
					if (item.payInfos[idx].status == 2) {
						return item.payInfos[idx].payer;
					}
				}
				return;
			}
			
			//退款状态判断//////////////////////////////////////////////////////////////////////////			
			$scope.toRefundPage = function(item) {
				$scope.showRefundUI = true;
				$scope.selectItem = item;
				$scope.loadPayInfo(item);
				$scope.loadRefundInfo(item);
			}
			
			//显示退款流水号帮助tip/////////////////////////////////////////////////////////////////
			$scope.showHelp = function() {
				return "<p align='left'>1、手机支付宝打开【账单】" +
						"<br/>2、选中转款记录点击进入【账单详情】" +
						"<br/>3、点击【创建时间】后可以看到【订单号】</p>"
			}
			
			//显示付款信息///////////////////////////////////////////////////////////////////////////////
			$scope.calcPayMoney = function(item) {
				return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
			}
			$scope.loadPayInfo = function(item) { 
				if (item.payInfos) {
					return;
				}
				
				item.loadPayInfoError = null;
				$http.get("payinfo/getPayInfo.do",
					    { params:{
					        orderid: item.order_id,
					    }
				}).success(function(res) {
					if (res && res.status == 1) {
						item.payInfos = res.payInfo;
					} else if (res && res.status == 0) {
						item.loadPayInfoError = res.error;
					} else {
						window.location="/login.html";
					}
			    }).error(function(data) {
					alert("发生错误，请重新登录！");
					window.location.href = "logout";
				});
			}
			
			//显示退款信息///////////////////////////////////////////////////////////////////////////////
			$scope.loadRefundInfo = function(item) { 
				if (item.refundInfos) {
					return;
				}
				$http.get("admin/getRefundInfoByOrderid.do",
					    { params:{
					        orderid: item.order_id,
					    }
				}).success(function(res) {
					if (res && res.status == 1) {
						item.loadRefundInfoError = null;
						item.refundInfos = res.refundInfos;
					} else if (res && res.status == 0) {
						item.loadRefundInfoError = res.error;
					} else {
						window.location="/login.html";
					}
			    }).error(function(data) {
					alert("发生错误，请重新登录！");
					window.location.href = "logout";
				});
			}
			
			$scope.getRefundType = function(type) {
				switch (type) {
				case 1:
					return "未留评退款";
				case 2:
					return "未完成退款";
				default:
					return "未知";
				}
			}
			
			//增加退款信息//////////////////////////////////////////////////////////////////
			$scope.newrefundModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Refund/newrefund.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			$scope.refundCancel = function() {
				$scope.newrefundModal.hide();
			}
			
			$scope.addRefund = function(item) {
				$scope.newrefund={
						quanity:0, 
						account:$scope.getPaySuccessAccount(item),
				};
				$scope.newrefundModal.$promise.then($scope.newrefundModal.show);
			}
			
			//防止填入的退款数量比订单数量还大
			$scope.$watch('newrefund.quanity', function(newVal, oldVal) {
				if (!newVal && !oldVal) {
					return;
				}
				if (!newVal || newVal < 0) {
					$scope.newrefund.quanity = 0;
				}
				if (newVal > $scope.selectItem.product_quantity) {
					$scope.newrefund.quanity = $scope.selectItem.product_quantity;
				}
			});
			
			//确保流水号只有后8位
			$scope.$watch('newrefund.sn', function(newVal, oldVal) {
	    	    if (newVal && newVal!=oldVal) {
	    	    	if (newVal.length >= 8) {
	    	    		$scope.newrefund.sn = newVal.substr(0, 8);
	    	    	}
	    	    }	
	    	    
	    	    if (newVal && newVal!=oldVal) {
	    	    	if (parseInt(newVal) != newVal) {
	    	    		$scope.newrefund.sn = oldVal;
	    	    	}
	    	    }
	    	});
			
			//提交退款信息//////////////////////////////////////////////////////////////
			$scope.newrefundConfirmModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Refund/confirm.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			
			$scope.showRefundConfirm = function() {
				$scope.newrefund.error = null;
				//检查信息是否填写完整
				if (!$scope.newrefund.quanity || $scope.newrefund.quanity == 0) {
					$scope.newrefund.error = "退款佣金数量至少为1";
					return;
				}
				
				if (!$scope.newrefund.account) {
					$scope.newrefund.error = "请输入退款账号";
					return;
				}
				
				if (!$scope.newrefund.sn) {
					$scope.newrefund.error = "请输入退款流水号";
					return;
				}
				
				if (!$scope.newrefund.remark) {
					$scope.newrefund.error = "请输入备注";
					return;
				}
				
				$scope.confirmTitle = "确定提交该退款信息吗?";
				$scope.newrefundModal.hide();
				$scope.newrefundConfirmModal.$promise.then($scope.newrefundConfirmModal.show);
			}
			
			$scope.confirmCancel = function() {
				$scope.newrefundConfirmModal.hide();
				$scope.newrefundModal.$promise.then($scope.newrefundModal.show);
			}
			
			$scope.confirmOK = function() {
				$scope.start();  			
    			$http({
    				method:"POST",
    				url: $scope.postURL,
    				data: $scope.createFD(),
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				$scope.newrefundConfirmModal.hide();
    				$scope.complete();
    				if(data.status == 1) {
    					if (!$scope.selectItem.refundInfos) {
    						$scope.selectItem.refundInfos = [];
    					} 
    					$scope.selectItem.refundInfos.push(data.refundinfo);
    				} else if(data.status == 0) {
    					$scope.newrefund.error = data.error;
    					$scope.newrefundModal.$promise.then($scope.newrefundModal.show);
    				} else {
    					window.location="/login.html";
    				}	
    			}).error(function(data){
    		    	$scope.complete();
    				alert("发生错误，请重新登录！");
    				window.location.href = "logout";
    			});
			}
			
		} ]);