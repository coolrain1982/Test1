'use strict';

var basedataModule = angular.module('base-data',['chieffancypants.loadingBar', 'ngAnimate']);

basedataModule.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
  });

basedataModule.controller("baseDataController", ['$state','$scope','$modal', '$location',
	      '$http','cfpLoadingBar','$timeout','$popover','orderTable','commFunc',
	      function($state, $scope, $modal, $location, $http, cfpLoadingBar, 
	    		   $timeout, $popover, orderTable, commFunc) {
	
	$scope.showpage = true;
	$scope.commFunc = commFunc;
	$scope.basedataerror = null;
	$scope.isSuccess = false;
	
	$scope.refresh = function() {
		$state.go($state.current, {}, {reload:true});
	}
	
	$scope.start = function() {
		cfpLoadingBar.start();
		$scope.showpage = false;
		$scope.doing = true;
	};
	
	$scope.complete = function (result) {
		cfpLoadingBar.complete();
		$scope.doing = false;
		$scope.showpage = true;
		if (result == 1) {
			$scope.confirmModal.hide();
			$scope.isSuccess = true;
		} else if (result == 2) {
			$scope.confirmCancel();
		}
	};
	
	$scope.getExchangeTypeName = function(item) {
		switch (item.type) {
		case 1:
			return "美元";
		default:
			return "未知";
		}
	}
	
	//显示确认弹出窗口
	$scope.confirmModal = $modal({
		scope : $scope,
		templateUrl : 'admin/Base/confirm.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.showConfirm = function() {
		$scope.basedataerror = null;
		if (!$scope.checkNewData()) {
			return;
		}
		$scope.newModal.hide();
		$scope.confirmModal.$promise.then($scope.confirmModal.show);
	}
	
	//确认取消
	$scope.confirmCancel = function() {
		$scope.confirmModal.hide();
		$scope.newModal.$promise.then($scope.newModal.show);
	}
	
	//提交数据到服务器保存
	$scope.confirmOK = function() {
		$scope.basedataerror = null;
		$scope.start();
		
		$http({
			method:"POST",
			url: $scope.postDataDo,
			data: $scope.getFormData(),
			headers: {"content-type":undefined},
			transformRequest: angular.identity
		}).success(function(data) {
			if(data.status == 1) {
		    	$scope.complete(1);
			} else if(data.status == 0) {
				$scope.basedataerror = data.error; 
		    	$scope.complete(2);
			} else {
     			window.location="/login.html";
			}
		}).error(function(data){
	    	$scope.complete(2);
			alert("系统发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}
	
	//根据不同基础数据初始化不同参数
	switch ($state.current.name) {
	case "exchangeMan" :
		$scope.getDataDo = "basedata/getExchange.do";
		$scope.postDataDo = "basedata/newExchange.do";
		$scope.newexchange = {
				type: 1,
		}
		$scope.newModal = $modal({
			scope : $scope,
			templateUrl : 'admin/Base/newexchange.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		$scope.addExchange = function() {
			$scope.basedataerror = null;
			$scope.newModal.$promise.then($scope.newModal.show);
		}
		
		$scope.confirmTitle = "数据增加后马上生效，确定增加汇率数据吗?";
		
		$scope.checkNewData = function() {
			if (!$scope.newexchange.rate || $scope.newexchange.rate <= 0) {
				$scope.basedataerror = "请输入正确的汇率值！";
				return false;
			}
			
			return true;
		}
		
		$scope.getFormData = function() {
			var fd = new FormData();
			fd.append("type", $scope.newexchange.type);
			fd.append("rate", $scope.newexchange.rate);
			
			return fd;
		}
		
		break;
	case "commisionMan" :
		$scope.getDataDo = "basedata/getCommision.do";
		$scope.postDataDo = "basedata/newCommision.do";
		$scope.newcommision = {
				srvtype: 1,
				srvmode: 1,
		};
		$scope.getSrvType = function(item) {
			switch (item.srv_type) {
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
		
		$scope.newModal = $modal({
			scope : $scope,
			templateUrl : 'admin/Base/newcommision.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		$scope.addCommision = function() {
			$scope.basedataerror = null;
			$scope.newModal.$promise.then($scope.newModal.show);
		}
		
		$scope.confirmTitle = "数据增加后马上生效，确定增加佣金数据吗?";
		
		$scope.checkNewData = function() {
			if (!$scope.newcommision.fee || $scope.newcommision.fee <= 0) {
				$scope.basedataerror = "请输入正确的佣金值！";
				return false;
			}
			
			return true;
		}
		
		$scope.getFormData = function() {
			var fd = new FormData();
			fd.append("srv_mode", $scope.newcommision.srvmode);
			fd.append("srv_type", $scope.newcommision.srvtype);
			fd.append("fee", $scope.newcommision.fee);
			
			return fd;
		}
		
		break;
	case "paypalMan" :
		$scope.getDataDo = "basedata/getPaypal.do";	
		$scope.postDataDo = "basedata/newPaypal.do";
		$scope.newpaypal = {};
		$scope.newModal = $modal({
			scope : $scope,
			templateUrl : 'admin/Base/newpaypal.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		$scope.addPaypal = function() {
			$scope.basedataerror = null;
			$scope.newModal.$promise.then($scope.newModal.show);
		}
		
		$scope.confirmTitle = "数据增加后马上生效，确定增加Paypal费用数据吗?";
		
		$scope.checkNewData = function() {
			if (!$scope.newpaypal.fee || $scope.newpaypal.fee < 0) {
				$scope.basedataerror = "请输入正确的Paypal费用值！";
				return false;
			}
			
			if (!$scope.newpaypal.feerate || $scope.newpaypal.feerate < 0 || $scope.newpaypal.feerate > 100) {
				$scope.basedataerror = "请输入正确的Paypal费率值！";
				return false;
			}
			
			return true;
		}
		
		$scope.getFormData = function() {
			var fd = new FormData();
			fd.append("fee", $scope.newpaypal.fee);
			fd.append("fee_rate", $scope.newpaypal.feerate);
			
			return fd;
		}
		
		break;
	default :
		$scope.showpage = false;
		$scope.newModal = null;
		$scope.addExchange = null;
		return;
	}
	
	// 分页////////////////////////////////////////////////////
	$scope.count = 0;
	$scope.p_pernum = 6;

	$scope.p_current = 1;
	$scope.p_all_page = 0;
	$scope.pages = [];
	
	$scope.calculateIndexes = function (current, length, displayLength) {  
	   var indexes = [];  
	   var start = Math.round(current - displayLength / 2);  
	   var end = Math.round(current + displayLength / 2);  
	    if (start <= 1) {  
	        start = 1;  
	        end = start + displayLength - 1;  
	       if (end >= length - 1) {  
	           end = length - 1;  
	        }  
	    }  
	    if (end >= length - 1) {  
	       end = length;  
	        start = end - displayLength + 1;  
	       if (start <= 1) {  
	           start = 1;  
	        }  
	    }  
	    for (var i = start; i <= end; i++) {  
	        indexes.push(i);  
	    }  
	    return indexes;  
	 };  
	
	$scope.p_index = function(){  
        $scope.load_page(1);  
    }  

    $scope.p_last = function(){  
        $scope.load_page($scope.p_all_page);  
    }  
    
    $scope.reloadPno = function(){  
          $scope.pages=$scope.calculateIndexes($scope.p_current,$scope.p_all_page,8);  
    };  
    
    $scope._get = function(page, size, callback) {
    	if ($scope.init && $scope.p_current == page) {
    		return;
    	}
    	
    	$scope.init = true;
    	$scope.start();
		$http.get($scope.getDataDo,
				{ params:{
			        page: page,
			        size: size,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				$scope.recordSize = res.count;
				$scope.dataList = res.list;
				$scope.p_current = page;
				$scope.p_all_page = Math.ceil($scope.recordSize/ $scope.p_pernum);
				$scope.reloadPno();
				callback();
			} else if (res && res.status == 0) {
				$scope.dataError = res.error;
				$scope.recordSize = res.count;
			} else {
				window.location="/login.html";
			}
			$scope.complete(3);
		}).error(function() {
			$scope.complete(3);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}

    $scope.load_page = function(page){  
        $scope._get(page,$scope.p_pernum,  function(){ });  
    };  
	
	//加载数据
    $scope._get($scope.p_current, $scope.p_pernum, function(){});
}]);