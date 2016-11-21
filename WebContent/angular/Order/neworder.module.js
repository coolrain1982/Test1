'use strict';

angular.module('new-order', []);

angular.module('new-order').
    controller("newOrderController", ['$state', '$stateParams', '$scope', '$http', 
    	    function($state, $stateParams, $scope, $http) {
    	
    	$scope.neworder_error = "";
    	$scope.dollar = "7.100";
    	$scope.pond = "8.800";
    	
    	$scope.neworder = {
    		"descript":"",
    	    "link" : "",
    	    "quantity" : 0,
    	    "unit_price": 0,
    	    "unit_freight": 0,
    	    "unit_commision": 4.00,
    	    "fee_discount": 100,
    	};
    	
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
    	
    	//查询当前用户佣金折扣率
    	$http.get('user/getDiscount.do')
    	.success(function(data) {
    		if (data != "" && data.discount != null) {
    			$scope.neworder.fee_discount = data.discount;
    		}
    	})
    	.error(function(data){
    		//跳转到出错页面
//    		return;
    	});
    	
    	$scope.calc_commison = function() {
    		if ($scope.neworder.quantity > 0) {
    		    return $scope.neworder.unit_commision * $scope.neworder.quantity * $scope.neworder.fee_discount / 100;
    		} else {
    			return 0;
    		}
    	};
    	
    	$scope.calc_sum = function() {
    		var temp = $scope.calc_product_price()  + $scope.calc_product_freight() + $scope.calc_commison();
    		return parseFloat(temp).toFixed(2);
    	};
    	
    	$scope.calc_sum_rmb = function() {
    		return (($scope.calc_product_price()  + $scope.calc_product_freight()
    				 + $scope.calc_commison())*$scope.dollar).toFixed(2);
    	};
    	
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
    			fd.append("descript", $scope.neworder.descript);
    			fd.append("link", $scope.neworder.link);
    			fd.append("quantity", $scope.neworder.quantity);
    			fd.append("unit_price", $scope.neworder.unit_price);
    			fd.append("unit_freight", $scope.neworder.unit_freight);
    			for(var item in $scope.thumb) {
    				fd.append("files", $scope.thumb[item].file);
    			}
    			
    			$http({
    				method:"POST",
    				url:"userorder/neworder.do",
    				data: fd,
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				alert("success!");
    			}).error(function(data){
    				alert("发生错误，请刷新页面后重试！");
    			});
    		} else {
    			$scope.neworder_error = "请确保表单数据填写正确！";
    		}
    	};
    }]);