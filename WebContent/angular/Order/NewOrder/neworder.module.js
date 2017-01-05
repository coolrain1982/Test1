'use strict';

var neworderModule = angular.module('new-order', ['chieffancypants.loadingBar', 'ngAnimate']);


neworderModule.config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

neworderModule.controller("newOrderController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
    	    function($state, $stateParams, $scope, $http, $timeout,
    	    		  cfpLoadingBar, $location, $anchorScroll, commFunc) {
	
		$scope.commFunc = commFunc;
    	
    	$scope.start = function() {
			cfpLoadingBar.start();
			$scope.new_order_show = false;
		};
		
		$scope.complete = function () {
			if ($scope.loadNewOrder) {
				$timeout.cancel($scope.loadNewOrder);
			}
			cfpLoadingBar.complete();
			$scope.new_order_show = true;
		};
		
		$scope.toView = function(id) {
			$location.hash(id);
			$anchorScroll();
		}
		
		$scope.loadNewOrder = $timeout(function() {cfpLoadingBar.start();
			$scope.new_order_show = false;}, 2000);
    	
    	$scope.orderid = -1;
    	$scope.createdate = "2016-12-01 00:00:00";
    	$scope.isSuccess = false;
    	$scope.neworder_error = "";
//    	$scope.dollar = 7.100;
    	
    	$scope.commision = [];
    	
    	$scope.neworder = {
        		"asin": "",
    			"descript":"",
        	    "link" : "",
        	    "quantity" : 0,
        	    "unit_price": 0,
        	    "unit_freight": 0,
        	    "unit_commision": 5.00,
        	    "fee_discount": 100,
        	    "exchange" : 1,
        	    "srvtype" : 1,
        	    "srvmode": 1,
        	};
    	
    
    	
    	$scope.getLink = function() {
    		if ($scope.neworder.link == null) {
    			return false;
    		} else {
    			window.open($scope.neworder.link);
    		}
    	}
    	
    	$scope.neworder_reset = function() {
    		$scope.isSuccess = !$scope.isSuccess;
    		$scope.orderid = 1;
    	}
    	
    	$scope.$on("thumb_change", function(event,data) {
    		$scope.thumb = data;
    	});
    	
    	$scope.calc_product_price = function() {
    		if ($scope.neworder.unit_price > 0 && $scope.neworder.quantity > 0) {
    		    return $scope.neworder.unit_price * $scope.neworder.quantity;
    		} else {
    			return 0;
    		}
    	};
    	
    	$scope.calc_product_freight = function() {
    		if ($scope.neworder.unit_freight > 0 && $scope.neworder.quantity > 0) {
    		    return $scope.neworder.unit_freight * $scope.neworder.quantity;
    		} else {
    			return 0;
    		}
    	};
    	
    	$scope.calc_commison = function() {
    		if ($scope.neworder.quantity > 0) {
    		    return $scope.neworder.unit_commision * $scope.neworder.quantity * $scope.neworder.fee_discount / 100;
    		} else {
    			return 0;
    		}
    	};
    	
    	$scope.calc_product_sum = function() {
    		return $scope.calc_product_price()  + $scope.calc_product_freight() + $scope.calc_commison();
    	};
    	
    	$scope.calc_sum = function() {
    		return $scope.calc_product_sum() * (1 + $scope.paypal_fee_rate/100)  + $scope.paypal_fee;
    	};
    	
    	$scope.calc_sum_rmb = function() {
    		return $scope.calc_sum().toFixed(2)* $scope.dollar;
    	};
    	
    	$scope.calc_alipay = function() {
    		var temp = $scope.calc_sum_rmb()* 0.001;
    		if (temp < 0.1) {
    			temp = 0.1
    		}
    		
    		return temp;
    	};
    	
    	$scope.getCommision = function() {
    		for(var temp in $scope.commision) {
    			if ($scope.neworder.exchange == $scope.commision[temp].type &&
    					$scope.neworder.srvtype == $scope.commision[temp].srv_type &&
    					$scope.neworder.srvmode == $scope.commision[temp].srv_mode) {
    				$scope.neworder.unit_commision = $scope.commision[temp].fee;
    				return $scope.neworder.unit_commision;
    			}
    		}
    		//没有找到
    		$scope.neworder.unit_commision = "";
    		return "无对应业务服务费数据，请联系管理员";
    	}
    	
    	$scope.getSrvType = function(type) {
			switch (type) {
			case 1:
				return "只购买商品";
			case 2:
				return "购买商品+review";
			case 3:
				return "购买商品+review+feedback";
			default:
				return "无效";
			}
		}
    	
    	$scope.$watch('neworder.descript', function(newVal, oldVal) {
    	    if (newVal && newVal!=oldVal) {
    	    	if (newVal.length >= 140) {
    	    		$scope.neworder.descript = newVal.substr(0, 140);
    	    	}
    	    }	
    	});
    	
    	$scope.$watch('neworder.asin', function(newVal, oldVal) {
    	    if (newVal && newVal!=oldVal) {
    	    	if (newVal.length >= 10) {
    	    		$scope.neworder.asin = newVal.substr(0, 10);
    	    	}
    	    }	
    	});
    	
    	$scope.$watch('neworder.quantity', function(newVal, oldVal) {
    	    if (newVal && newVal!=oldVal) {
    	    	$scope.neworder.quantity = Math.round(newVal);
    	    }	
    	});
    	
    	$scope.neworder_submit=function(valid) {
    		$scope.neworder_error = "";
    		if (valid) {
    			var thumb_count = 0;
    			
    			for(var item in $scope.thumb) {
    				thumb_count ++;
    			}
    			if (thumb_count < 1 || thumb_count > 3) {
    				$scope.neworder_error = "上传截图个数最少1个，最多3个！";
    				return;
    			}
    			
    			var fd = new FormData();
    			fd.append("asin", $scope.neworder.asin);
    			fd.append("descript", $scope.neworder.descript);    			
    			fd.append("quantity", $scope.neworder.quantity);
    			fd.append("unit_price", $scope.neworder.unit_price);
    			fd.append("unit_freight", $scope.neworder.unit_freight);
    			fd.append("exchange", $scope.neworder.exchange);
    			fd.append("srvtype", $scope.neworder.srvtype);
    			fd.append("srvmode",$scope.neworder.srvmode);
    			
    			if ($scope.neworder.srvmode == 1) {
    				fd.append("link", $scope.neworder.link);
    			} else if ($scope.neworder.srvmode == 2) {	    			
	    			fd.append("keyword",$scope.neworder.keyword);
	    			fd.append("shopname",$scope.neworder.shopname);
	    			fd.append("pageidx",$scope.neworder.pageidx);
    			}
    			
    			for(var item in $scope.thumb) {
    				fd.append("files", $scope.thumb[item].file);
    			}
    			
    	    	$scope.start();
    	    	$scope.toView("main-content");
    			
    			$http({
    				method:"POST",
    				url:"userorder/neworder.do",
    				data: fd,
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				if(data.status == 1) {
//    					$state.go("neworder_success", {orderid:data.orderid});
    					$scope.isSuccess = true;
    					$scope.orderid = data.orderid;
    					$scope.createdate = data.createdate;
        		    	$scope.complete();
    				} else if(data.status == 0) {
    					$scope.neworder_error = "保存订单失败：" + data.error; 
        		    	$scope.complete();
        		    	$scope.toView("new_order_submit");
    				} else {
    					window.location="/login.html";
    				}
    				
    			}).error(function(data){
    				for(var i = 0; i < document.getElementsByName("new_order_row").length; i ++) {
    		    		document.getElementsByName("new_order_row")[i].style.display = "block";
    		    	}
    		    	$scope.complete();
    				alert("发生错误，请重新登录！");
    				window.location.href = "logout";
    			});
    		} else {
    			$scope.neworder_error = "请确保表单数据填写正确！";
    		}
    	};
    	
    	//查询基础配置数据
    	$http.get('basedata/neworder.do')
    	.success(function(data) {
    		if (data != "") {
    			if (data.exchange != null) {
    				$scope.dollar = data.exchange;
    			}
    			if (data.commision != null) {
    				for(var temp in data.commision) {
    					$scope.commision.push(data.commision[temp]);
    				}
    			}
    			if (data.paypal_fee != null) {
    				$scope.paypal_fee = data.paypal_fee;
    			}
    			if (data.paypal_fee_rate != null) {
    				$scope.paypal_fee_rate = data.paypal_fee_rate;
    			}
    		}
    	})
    	.error(function(data){
    		//跳转到出错页面
//    		return;
    		$scope.complete();
    	});
    	
    	//查询当前用户佣金折扣率
    	$http.get('user/getDiscount.do')
    	.success(function(data) {
    		$scope.complete();
    		if (data != "" && data.discount != null) {
    			$scope.neworder.fee_discount = data.discount;
    		}
    	})
    	.error(function(data){
    		//跳转到出错页面
//    		return;
    		$scope.complete();
    	});
    }]);