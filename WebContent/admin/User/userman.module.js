var usermanModule = angular.module('user-man',['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
  });

usermanModule.controller("userManController", ['$state','$scope','$modal',
	      '$http','cfpLoadingBar','$timeout','$popover','orderTable','commFunc',
	      function($state, $scope, $modal,$http, cfpLoadingBar, $timeout, $popover, orderTable, commFunc) {
	
	$scope.showpage = true;
	$scope.commFunc = commFunc;
	$scope.usermanerror = null;
	$scope.isSuccess = false;
	$scope.search = {
	    str: "",
	};
	
	$scope.refresh = function() {
		$state.go($state.current, {}, {reload:true});
	}
	
	$scope.start = function() {
		cfpLoadingBar.start();
		$scope.showpage = false;
		$scope.doing = true;
		$scope.userError = null;
	};
	
	$scope.complete = function (result) {
		cfpLoadingBar.complete();
		$scope.doing = false;
		$scope.showpage = true;
	};
	
	$scope.queryParams = {
	   url: "admin/getAllUser.do",
	   type: 3,
	   str: "",
	};
	
	$scope.getRole = function(user) {
		switch (user.role.toLowerCase()) {
		case "role_admin" :
			return "管理员";
		case "role_user":
			return "VIP用户";
		case "role_cs":
			return "客服";
		default:
			return "未知";
		} 
	}
	
	$scope.searchUser = function(event) {
		var keycode = window.event ? event.keyCode : event.which;
		if (keycode == 13) {
			$scope.search.str = $scope.search.str.replace(/(^\s*)|(\s*$)/g, "");
			if ($scope.search.str != $scope.queryParams.str) {
				$scope.queryParams.str = $scope.search.str;
				if ($scope.queryParams.str == "") {
					$scope.queryParams.type = 3;
				} else {
					$scope.queryParams.type = 9;
				}
				
				$scope.init = false;
				//加载默认数据
			    $scope._get(1, $scope.p_pernum, function(){}, $scope.queryParams);
			}
		}
	}
	
	$scope.backToUserInfo = function() {
		$scope.isSuccess = false;
		$scope.selectItem.discount = $scope.selectItem.newdiscount;
	}
	
	$scope.discountList = [];
	for(var i = 0; i <= 100;) {
		$scope.discountList.push(i);
		i += 5;
	}
	
	//弹出的修改折扣费率的页面
	$scope.changeDiscountDialog = $modal({
		scope : $scope,
		templateUrl : 'admin/User/changediscount.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	$scope.changeDiscount = function(item) {
		$scope.selectItem = item;
		$scope.changeDiscountError = null;
		$scope.changeDiscountDialog.$promise.then($scope.changeDiscountDialog.show);
	}
	
	//修改佣金折扣率确认按钮点击/////////////////////////////////////////////
	$scope.changeDiscountConfirmDialog = $modal({
		scope : $scope,
		templateUrl : 'admin/User/confirm.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	$scope.showConfirm = function() {
		$scope.changeDiscounterror = null;
		//检查填写是否正确
		if (!$scope.selectItem.newdiscount || $scope.selectItem.newdiscount < 0 ||
				$scope.selectItem.newdiscount > 100) {
			$scope.changeDiscounterror = "请选择正确的佣金折扣率！";
			return;
		}
		
		if ($scope.selectItem.discount + '%' == $scope.selectItem.newdiscount) {
			$scope.changeDiscounterror = "佣金折扣率未修改！";
			return;
		}
		
		$scope.changeDiscountDialog.hide();
		$scope.confirmTitle = "确定修改该用户的佣金折扣率为" + $scope.selectItem.newdiscount +"吗？";
		$scope.changeDiscountConfirmDialog.$promise.then($scope.changeDiscountConfirmDialog.show);
	}
	
	//修改佣金折扣率
	$scope.confirmOK = function() {
		if ($scope.doing) {
			return;
		}
		
		$scope.start();
		$scope.changeDiscounterror = null;
		
		var fd = new FormData();
		fd.append("name", $scope.selectItem.name);
		fd.append("discount", $scope.selectItem.newdiscount);
		
		$http({
			method:"POST",
			url: "admin/changediscount.do",
			data: fd,
			headers: {"content-type":undefined},
			transformRequest: angular.identity
		}).success(function(data) {
			
			$scope.changeDiscountConfirmDialog.hide();
			
			if(data.status == 1) {
		    	$scope.complete(1);
		    	$scope.isSuccess = true;
		    	$scope.selectItem.newdiscount = data.discount;
			} else if(data.status == 0) {
				$scope.changeDiscounterror = data.error; 
				$scope.changeDiscountDialog.$promise.then($scope.changeDiscountDialog.show);
		    	$scope.complete(2);
			} else {
				window.location.href = "logout";
			}
		}).error(function(data){
	    	$scope.complete(2);
			alert("系统发生错误，请重新登录！");
			window.location.href = "logout";
		});	
	}
	
	//取消佣金折扣率修改
	$scope.confirmCancel = function() {
		if ($scope.doing) {
			return;
		}
		
		$scope.changeDiscountConfirmDialog.hide();
		$scope.changeDiscountDialog.$promise.then($scope.changeDiscountDialog.show);
	}
	
	//分页查询通用函数////////////////////////////////////////////////////
	$scope.count = 0;
	$scope.p_pernum =6;

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
    
    $scope._get = function(page, size, callback, queryParams) {
    	if ($scope.init && $scope.p_current == page) {
    		return;
    	}
    	
    	$scope.init = true;
    	$scope.start();
		$http.get(queryParams.url,
				{ params:{
			        page      : page,
			        size      : size,
			        queryType : queryParams.type,
			        queryParam: queryParams.str,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				$scope.recordSize = res.count;
				$scope.userList = res.list;
				$scope.p_current = page;
				$scope.p_all_page = Math.ceil($scope.recordSize/ $scope.p_pernum);
				$scope.reloadPno();
				callback();
			} else if (res && res.status == 0) {
				$scope.userError = res.error;
				$scope.recordSize = res.count;
			} else {
				$state.go($state.current, {}, {reload:true});
			}
			$scope.complete(1);
		}).error(function() {
			$scope.complete(1);
			alert("发生错误，请重新登录！");
			window.location.href = "logout";
		});
	}

    $scope.load_page = function(page){  
        $scope._get(page,$scope.p_pernum, function(){ }, $scope.queryParams);  
    }; 
    
    ///////////////////////////////////////////////////////////////////////////
    //加载默认数据
    $scope._get($scope.p_current, $scope.p_pernum, function(){}, $scope.queryParams);
}]);