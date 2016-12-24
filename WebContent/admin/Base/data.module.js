'use strict';

var basedataModule = angular.module('base-data',['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
  });

basedataModule.controller("baseDataController", ['$state','$scope','$modal',
	      '$http','cfpLoadingBar','$timeout','$popover','orderTable','commFunc',
	      function($state, $scope, $modal,$http, cfpLoadingBar, $timeout, $popover, orderTable, commFunc) {
	
	$scope.showpage = true;
	$scope.commFunc = commFunc;
	
	$scope.start = function() {
		cfpLoadingBar.start();
		$scope.showpage = false;
	};
	
	$scope.complete = function () {
		cfpLoadingBar.complete();
		$scope.showpage = true;
	};
	
	$scope.getExchangeTypeName = function(item) {
		switch (item.type) {
		case 1:
			return "美元";
		default:
			return "未知";
		}
	}
	
	//根据不同基础数据初始化不同参数
	switch ($state.current.name) {
	case "exchangeMan" :
		$scope.getDataDo = "basedata/getExchange.do";
		
		break;
	case "commisionMan" :
		$scope.getDataDo = "basedata/getCommision.do";
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
		break;
	case "paypalMan" :
		$scope.getDataDo = "basedata/getPaypal.do";
		break;
	default :
		$scope.showpage = false;
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
				window.location.href = "logout";
			}
			$scope.complete();
		}).error(function() {
			$scope.complete();
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