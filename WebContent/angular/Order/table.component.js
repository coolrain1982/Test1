'use strict';

// Register `phoneList` component, along with its associated controller and
// template
angular.module('order-table').component('orderTable',{
	templateUrl : 'angular/Order/table.template.html',controller : ['$state','$scope','$modal','$http',
	function TableController($state, $scope, $modal,
			$http) {

		this.title = "";
		this.icon = "";
		this.queryStatus = -1;
		this.recordSize = 2;

		this.orderList = [];

		// this.orderList = [
		// {
		// order_id: 1,
		// discount: 80,
		// product_descript:"11111111",
		// link:"http://baidu.com",
		// product_photo_url:"@@LeiYu/2016/11/1479973445415_0.png@@LeiYu/2016/11/1479973445415_1.PNG",
		// audit_remark:"ok",
		// product_unit_price:1.01,
		// product_unit_freight:1.02,
		// product_unit_commission:5,
		// exchange_rate:7.0311,
		// paypal_fee:0.3,
		// paypal_rate:3.9,
		// product_quantity:2,
		// create_date: 1293072805,
		// status: 1,
		// },
		// {
		// order_id: 2,
		// discount: 80,
		// product_descript:"22222222",
		// link:"http://sohu.com/11111111111111111111111111111111111",
		// product_photo_url:"@@LeiYu/2016/11/1480077298963_0.PNG",
		// audit_remark:"ok",
		// product_unit_price:2.02,
		// product_unit_freight:2.03,
		// product_unit_commission:5,
		// exchange_rate:7.0311,
		// paypal_fee:0.3,
		// paypal_rate:3.9,
		// product_quantity:3,
		// create_date: 1293062805,
		// status: 3,
		// }
		// ];

		var modelDialog = $modal({
			scope : $scope,
			templateUrl : 'angular/Order/detail.template.html',
			show : false
		});

		this.getUrlList = function(temp) {
			var urlArray = temp.split("@@");
			var rtnArray = [];
			for ( var i in urlArray) {
				if (urlArray[i] != "") {
					rtnArray.push("/upload/img/"
							+ urlArray[i]);
				}
			}

			return rtnArray;
		}

		this.showDetail = function(item) {
			$scope.selectItem = item;
			modelDialog.$promise.then(modelDialog.show);
		};

		this.calcTotal = function(item) {
			var temp = (item.product_unit_price
					+ item.product_unit_freight + item.product_unit_commission
					* item.discount / 100)
					* item.product_quantity;
			return (temp
					* (1 + parseFloat(item.paypal_rate) / 100) + item.paypal_fee)
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

		this.getDate = function(str) {
			return str.substring(0, 10);
		};

		this.getTime = function(str) {
			return str.substring(11);
		};

		this.formatDate = function(temp) {
			var temp = new Date(temp * 1000);
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
		this.p_pernum = 10;

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
	    
	    this._get = function(page, size, callback) {
//			$http.get("userorder/getOrder.do", {params:{status:this.queryStatus}})
//			.success(function(res) {
//				if (res && res.status == 1) {
//					this.recordSize = res.count;
//					this.list = res.list;
//					this.p_current = page;
//					this.p_all_page = Math.ceil(this.count/ this.p_pernum);
//					reloadPno();
//					callback();
//				} else if (res && res.status == 0) {
//					this.pageError = res.error;
//				} else {
//					window.location.href = data;
//				}
//			}).error(function() {
//				alert("发生错误，请重新登录！");
//    			window.location.href = "logout";
//			});
			this.recordSize = 105;
			this.p_current = page;
			this.p_all_page = Math.ceil(this.recordSize/ this.p_pernum);
			this.reloadPno();
			callback();
		}
		
		this._get(this.p_current, this.p_pernum, function(){});
 
	    this.load_page = function(page){  
	        this._get(page,this.p_pernum,function(){ });  
	    };  
  


		// //////////////////////////////////////////////////////

		if ($state.current = "unconfirmOrder") {
			this.title = "待确认订单";
			this.icon = "icon_question_alt";
			this.queryStatus = 1;
		}
		;
	} ]
});