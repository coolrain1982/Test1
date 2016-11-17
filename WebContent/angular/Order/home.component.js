'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.
  module('order-home').
  component('orderHome', {
    templateUrl: 'angular/Order/home.template.html',
    controller: ['$state', function HomeController($state) {
      //查询当前登陆用户所有的订单汇总信息
      this.wait_pay_count = 0;
      this.pending_count = 0;
      this.finish_count_cur_month = 0
    }]
  });