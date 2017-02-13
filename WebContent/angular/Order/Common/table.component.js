'use strict';

var orderTableModule = angular.module('order-table',['chieffancypants.loadingBar', 'ngAnimate']);

orderTableModule.config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = false;
  });

// Register `phoneList` component, along with its associated controller and
// template
orderTableModule.component('orderTable',{
	templateUrl : 'angular/Order/Common/table.template.html',
	controller : ['$state','$scope','$modal','$http','cfpLoadingBar','$timeout','$popover','orderTable','commFunc',
	function TableController($state, $scope, $modal,$http, cfpLoadingBar, $timeout, $popover, orderTable, commFunc) {

		this.title = orderTable.title;
		this.icon = orderTable.icon;
		
		this.commFunc = commFunc;
		this.orderTable = orderTable;

		this.orderList = [];
		this.init = false;
	    
	    this.queryOrderid = -1;
	    this.tableShow = true;
	    
	    $scope.showuser = {};
	    $scope.existsuser = [];
	    $scope.existrefunds = {};
	    $scope.existreviews = {};

		this.modelDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/Common/detail.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			keyboard:false,
		});

		this.getUrlList = function(temp) {
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

		this.showDetail = function(item) {
			$scope.selectItem = item;
			orderTable.selectItem = item;
			orderTable.detailDialog = this.modelDialog;
			this.modelDialog.$promise.then(this.modelDialog.show);
		};

		this.getShowLink = function(temp) {
			if (temp.length > 100) {
				return temp.substring(0, 100) + "...";
			}
			return temp;
		};

		this.getStatusClass = function(temp) {
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
				return "label-warning";
			default:
				return "label-default";
			}
		}

		this.getStatus = function(temp) {
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
		
		this.getCursor = function(item) {
			if (this.canClick(item)) {
				return "pointer";
			} else {
				return "default";
			}
		}
		
		this.getTypeDesc = function(item) {
			return this.commFunc.getSrvMode(item.find_product_mode) + "+" + this.commFunc.getSrvType(item);
		}
		
		//显示用户详细信息--------------------------------------------------------------------
		this.userInfoDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/User/userInfo.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			keyboard:false,
		});
		
		this.showUserInfo = function(userName) {
			this.userInfoDialog.$promise.then(this.userInfoDialog.show);
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
		
		//显示退款信息///////////////////////////////////////////////
		this.refundInfoDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Refund/refundInfo.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			keyboard:false,
		});
		
		this.showRefundInfo = function(item, reload) {
			this.refundInfoDialog.$promise.then(this.refundInfoDialog.show);
			$scope.showrefunditem = item;
			$scope.showrefund = {};
			this.loadRefundInfo(item, reload);
		}
		
		this.loadRefundInfo = function(item, reload) { 
		    //不重载
			if (!reload) {
				//查找已有的数据中是否存在
				if ($scope.existrefunds[item.order_id]) {
					$scope.showrefund = $scope.existrefunds[item.order_id];
					return;
				}
			}
			
			$http.get("user/getRefundInfoByOrderid.do",
				    { params:{
				        orderid: item.order_id,
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					$scope.loadRefundInfoError = null;
					$scope.showrefund = res.refundInfos;
					$scope.existrefunds[item.order_id] = res.refundInfos;
				} else if (res && res.status == 0) {
					$scope.loadRefundInfoError = res.error;
				} else {
					window.location="/login.html";
				}
		    }).error(function(data) {
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}
		
		this.getRefundType = function(type) {
			switch (type) {
			case 1:
				return "未留评退款";
			case 2:
				return "未完成退款";
			default:
				return "未知";
			}
		}
		
		//显示review信息////////////////////////////////////////////
		this.reviewDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Review/review.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			keyboard:false,
		});
		
		this.showReview = function(item, reload) {
			this.reviewDialog.$promise.then(this.reviewDialog.show);
			$scope.showreviewitem = item;
			$scope.showreview = {};
			this.loadReview(item, reload);
		}
		
		this.canClick = function(item) {
			switch (item.status) {
			case 7:
				return true;
			default:
				return this.orderTable.canClick(item);
			}
		}
		
		this.statusClick = function(item) {
			switch (item.status) {
			case 7:
				this.showReview(item, false);
				return;
			default:
				this.orderTable.statusClick(item);
			}
		}
		
		this.loadReview = function(item, reload) { 
		    //不重载
			if (!reload) {
				//查找已有的数据中是否存在
				if ($scope.existreviews[item.order_id]) {
					$scope.showreview = $scope.existreviews[item.order_id];
					return;
				}
			}
			
			$http.get("user/getReviewByOrderid.do",
				    { params:{
				        orderid: item.order_id,
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					$scope.loadReviewError = null;
					$scope.showreview = res.reviewInfos;
					$scope.existreviews[item.order_id] = res.reviewInfos;
				} else if (res && res.status == 0) {
					$scope.loadReviewError = res.error;
				} else {
					window.location="/login.html";
				}
		    }).error(function(data) {
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}

		// 分页////////////////////////////////////////////////////
		this.count = 0;
		this.p_pernum = 6;

		this.p_current = 1;
		this.p_all_page = 0;
		this.pages = [];
		
		this.calculateIndexes = function (current, length, displayLength) {  
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
		
		this.p_index = function(){  
	        this.load_page(1);  
	    }  
 
	    this.p_last = function(){  
	        this.load_page(this.p_all_page);  
	    }  
	    
	    this.reloadPno = function(){  
	          this.pages=this.calculateIndexes(this.p_current,this.p_all_page,8);  
	    };  
	    
	    this.start = function() {
	    	this.timeout = $timeout(function() {
		    	$scope.$ctrl.tableShow = false;
				cfpLoadingBar.start();
				}, 2000);
		};
		
		this.complete = function () {
			if (this.timeout) {
				$timeout.cancel(this.timeout);
			}
			cfpLoadingBar.complete();
			this.tableShow = true;
		};
	    
	    this._get = function(page, size, orderid, callback) {
	    	if (this.init && this.p_current == page) {
	    		return;
	    	}
	    	
	    	this.init = true;
	    	this.start();
			$http.get(orderTable.doUrl,
					{ params:{
				        command:orderTable.queryCommand,
				        page: page,
				        size: size,
				        orderid: orderid,
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					$scope.$ctrl.recordSize = res.count;
					$scope.$ctrl.orderList = res.list;
					$scope.$ctrl.p_current = page;
					$scope.$ctrl.p_all_page = Math.ceil($scope.$ctrl.recordSize/ $scope.$ctrl.p_pernum);
					$scope.$ctrl.reloadPno();
					callback();
				} else if (res && res.status == 0) {
					$scope.$ctrl.pageError = res.error;
					$scope.$ctrl.recordSize = res.count;
				} else {
					window.location="/login.html";
				}
				$scope.$ctrl.complete();
			}).error(function() {
				$scope.$ctrl.complete();
				alert("发生错误，请重新登录！");
    			window.location.href = "logout";
			});
		}
 
	    this.load_page = function(page){  
	        this._get(page,this.p_pernum, this.queryOrderid,  function(){ });  
	    };  
		// //////////////////////////////////////////////////////
	 
		this._get(this.p_current, this.p_pernum, -1, function(){});
	} ]
});