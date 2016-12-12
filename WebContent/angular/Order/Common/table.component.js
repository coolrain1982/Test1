'use strict';

// Register `phoneList` component, along with its associated controller and
// template
angular.module('order-table').component('orderTable',{
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

		this.modelDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/Common/detail.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});

		this.getUrlList = function(temp) {
			var urlArray = temp.split("@@");
			var rtnArray = [];
			for ( var i in urlArray) {
				if (urlArray[i] != "") {
					rtnArray.push("/MyWeb/upload/image/"
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
		
		this.showAction1 = function(item) {
			orderTable.detailAction1(item);
		}
		
		this.showAction2 = function(item) {
			orderTable.detailAction2(item);
		}

		this.getShowLink = function(temp) {
			if (temp.length > 20) {
				return temp.substring(0, 20) + "...";
			}
			return temp;
		};

		this.getStatusClass = function(temp) {
			switch (temp) {
			case 1:
				return "label-primary";
			case 2:
				return "label-info";
			case 3:
				return "label-danger";
			case 4:
				return "label-success";
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
				return "拒绝";
			case 4:
				return "已支付";
			case 10:
				return "已拨付";
			case 20:
				return "已完成";
			case 21:
				return "有退款";
			default:
				return "无效";
			}
		}
		
		this.getCursor = function(item) {
			if (orderTable.canClick(item)) {
				return "pointer";
			} else {
				return "default";
			}
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
				        status:orderTable.queryStatus,
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
					window.location.href = res;
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