'use strict';

// Register `phoneList` component, along with its associated controller and
// template
angular.module('order-table').component('orderTable',{
	templateUrl : 'angular/Order/table.template.html',
	controller : ['$state','$scope','$modal','$http','cfpLoadingBar','$timeout','$popover',
	function TableController($state, $scope, $modal,$http, cfpLoadingBar, $timeout, $popover) {

		this.title = "";
		this.icon = "";
		this.queryStatus = -1;

		this.orderList = [];
		this.init = false;
	    this.doUrl = "";
	    
	    this.operator = 1;
	    this.doingAudit = false;

		var modelDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/detail.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		var auditDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/audit.template.html',
			show : false,
			animation: 'am-fade-and-slide-top',
			backdrop:'static',
			keyboard:false,
		});
		
		var confirmDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/confirm.template.html',
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
			modelDialog.$promise.then(modelDialog.show);
		};
		
		this.showAudit = function(item) {
			item.auditError = "";
			auditDialog.$promise.then(auditDialog.show);
		}
		
		this.checkAuditContent = function(item) {
			item.auditError = "";
			
			if (!item.auditContent || item.auditContent.length < 1) {
				item.auditError = "请输入审核意见!";
				return false;
			}
			
			if (item.auditContent.length > 100) {
				item.auditError = "审核意见不能超过100个字符!";
				return false;
			}
			
			return true;
		}
		
		this.showPassConfirm = function(item) {
			if (!this.checkAuditContent(item)) {
				return;
			}
			
			auditDialog.hide();
			this.auditError = "";
			this.auditStatus = 2;
			this.confirmTitle = "确定审核通过该订单吗？";
			confirmDialog.$promise.then(confirmDialog.show);
		}
		
        this.showRejectConfirm = function(item) {
        	if (!this.checkAuditContent(item)) {
				return;
			}
			auditDialog.hide();
			this.auditError = "";
        	this.auditStatus = 3;
        	this.confirmTitle = "确定拒绝该订单吗？";
        	confirmDialog.$promise.then(confirmDialog.show);
		}
        
        this.confirmCancel = function() {
        	auditDialog.$promise.then(auditDialog.show);
        }

		this.calcTotal = function(item) {
			var temp = (item.product_unit_price
					+ item.product_unit_freight + item.product_unit_commission
					* item.discount / 100)
					* item.product_quantity;
			return (temp
					* (1 + parseFloat(item.paypal_rate) / 100) + item.paypal_fee).toFixed(2)
					* item.exchange_rate;
		};

		this.calcSum = function(item) {
			var temp = this.calcProductSum(item)
					* (1 + parseFloat(item.paypal_rate) / 100)
					+ item.paypal_fee;

			return temp;
		};

		this.calcProductSum = function(item) {
			var temp = (item.product_unit_price
					+ item.product_unit_freight + item.product_unit_commission
					* item.discount / 100)
					* item.product_quantity;

			return temp;
		};

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
		
		this.getSrvType = function(item) {
			switch (item.type) {
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

		this.getDate = function(str) {
			return str.substring(0, 10);
		};

		this.getTime = function(str) {
			return str.substring(11);
		};

		this.formatDate = function(temp) {
			var temp = new Date(temp);
			return temp.getFullYear()
					+ "-"
					+ this
							.foo((temp.getMonth() + 1),
									2) + "-"
					+ this.foo(temp.getDate(), 2) + " "
					+ this.foo(temp.getHours(), 2)
					+ ":"
					+ this.foo(temp.getMinutes(), 2)
					+ ":"
					+ this.foo(temp.getSeconds(), 2);
		};

		this.foo = function(str, len) {
			for (var i = 0; i < len; i++) {
				str = '0' + str;
			}
			return str.substring(str.length - len,
					str.length);
		};

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
				cfpLoadingBar.start();}
			, 2000);
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
			$http.get(this.doUrl,
					{ params:{
				        status:this.queryStatus,
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
	    
	   //审核用户订单相关///////////////////////////////////////////////////////	    
	    this.auditStart = function() {
	    	this.auditError = "";
			this.doingAudit = true;
			this.auditTimeout = $timeout(function() {
				cfpLoadingBar.start();}
			, 2000);
		}
		
		this.auditComplete = function(result) {
			if (this.auditTimeout) {
				$timeout.cancel(this.auditTimeout);
			}
			cfpLoadingBar.complete();
			this.doingAudit = false;
			if (result) {
				confirmDialog.hide();
				this.p_current = 1;
				this.init = false;
				this._get(this.p_current, this.p_pernum, -1, function(){});
			}
		};
		
	   this.confirmOK = function() {
		   this.auditStart();
		   $http.get("cs/auditOrder.do",
					{ params:{
				        status: this.auditStatus,
				        orderid: $scope.selectItem.order_id,
				        auditmark: $scope.selectItem.auditContent
				    }
			}).success(function(res) {
				if (res && res.status == 1) {
					//重新刷新当前的table
					$scope.$ctrl.auditComplete(true);
				} else if (res && res.status == 0) {
					$scope.$ctrl.auditError = res.error;
					$scope.$ctrl.auditComplete(false);
				} else {
					$scope.$ctrl.auditComplete(false);
					window.location.href = res;
				}
				
			}).error(function() {
				$scope.$ctrl.auditComplete(false);
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
       }
	   //////////////////////////////////////////////////////////////////
	   
	    
	    this.doUrl = "userorder/getOrder.do";
	    switch ($state.current.name) {
	        case "unpayOrder":
	        	this.title = "待支付订单";
			    this.icon = "icon_currency_alt";
			    this.queryStatus = 2; 
	        	break;
	        case "doingOrder":
	        	this.title = "进行中订单";
			    this.icon = "arrow_carrot-2right_alt";
			    this.queryStatus = 4;
	        	break;
	        case "rejectOrder":
	        	this.title = "待关闭订单";
			    this.icon = "icon_error-circle";
			    this.queryStatus = 3;
	        	break;
	        case "historyOrder":
	        	this.title = "已完成";
			    this.icon = "icon_check_alt";
			    this.queryStatus = 20;
	        	break;
	        case "allOrder":
	        	this.title = "全部订单";
			    this.icon = "icon_menu-circle_alt";
			    this.queryStatus = 99;
	        	break;
	        case "csOrder":
	        	this.title = "待审核订单";
			    this.icon = "icon_question_alt";
			    this.queryStatus = 1;
			    this.operator = 2;
			    this.doUrl = "cs/getOrder.do";
	        	break;
	        case "csRejectOrder":
	        	this.title = "待关闭订单";
			    this.icon = "icon_error-circle";
			    this.queryStatus = 3;
			    this.operator = 2;
			    this.doUrl = "cs/getOrder.do";
	        	break;
	        case "allCsOrder":
	        	this.title = "我处理的所有订单";
			    this.icon = "icon_menu-circle_alt";
			    this.queryStatus = 99;
			    this.operator = 2;
			    this.doUrl = "cs/getOrder.do";
	        	break;
			default:
				this.title = "待确认订单";
			    this.icon = "icon_question_alt";
			    this.queryStatus = 1;
			    break;
	    }
		
		this._get(this.p_current, this.p_pernum, -1, function(){});
	} ]
});