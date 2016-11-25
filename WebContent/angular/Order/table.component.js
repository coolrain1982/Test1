'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.
  module('order-table').
  component('orderTable', {
    templateUrl: 'angular/Order/table.template.html',
    controller: ['$state','$scope', function TableController($state, $scope) {
    	
    	this.title = "";
    	this.icon = "";
    	this.queryStatus = -1;
    	this.recordSize = 2;
    	
    	this.orderList = [
    		{
    			orderId : 1, orderDescript:"11111111",orderLink:"http://baidu.com", orderQuantity:3,
    			createDate: "1293072805", status: 0,
    		},
    		{
    			orderId : 2, orderDescript:"22222222",orderLink:"http://sohu.com", orderQuantity:4,
    			createDate: "1293092805", status: 1,
    		}
    	];
    	
    	this.showDetail = function(item) {
    		alert(item);
    	}
    	
    	this.formatDate = function(temp) {
    		var temp = new Date(temp*1000);
    		return temp.getFullYear() + "-" + (temp.getMonth() + 1) + "-" +
    		       temp.getDate() + " " + temp.getHours() + ":" +
    		       temp.getMinutes() + ":" + temp.getSeconds();
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
    	}
    	
    	this.getTime = function(str) {
    		return str.substring(11);
    	}
    	
        if ($state.current="unconfirmOrder") {
        	this.title = "待确认订单";
        	this.icon = "icon_question_alt";
        	this.queryStatus = 0;
        }  
    }]
  });