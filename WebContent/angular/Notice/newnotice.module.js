'use strict';

angular.module('new-notice', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('new-notice').
    controller("newNoticeController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll) {
    	
    	$scope.start = function() {
			cfpLoadingBar.start();
			$scope.newnotice_show = false;
		};
		
		$scope.complete = function () {
			cfpLoadingBar.complete();
			$scope.newnotice_show = true;
		};
		
		$scope.toView = function(id) {
			$location.hash(id);
			$anchorScroll();
		}
		
		$scope.options = {
		    height: 300,
		    toolbar: [
		    	['headline', ['style']],
		    	['style', ['bold', 'italic', 'underline', 'strikethrough', 'clear']],
		    	['para', ['paragraph']],
		    	['edit', ['undo', 'redo']],
		    	['fontname', ['fontname']],
		    	['textsize', ['fontsize']],
		    	['fontclr', 'color'],
		    	['alignment', 'ul', 'ol','paragraph','lineheight'],
		    	['table',['table']],
		    	['view', ['fullscreen', 'codeview']],
		    	['help', ['help']]
		    ],
		};
    	
    	$scope.isSuccess = false;
    	$scope.notice_error = "";
//    	$scope.dollar = 7.100;
    	$scope.newnotice_show = true;
    	
    	$scope.newnotice = {
        		title :"",
        	    content : "",
        	    status: 1,
        	};
    	
    	$scope.$watch('newnotice.title', function(newVal, oldVal) {
    	    if (newVal && newVal!=oldVal) {
    	    	if (newVal.length >= 200) {
    	    		$scope.newnotice.title = newVal.substr(0, 200);
    	    	}
    	    }	
    	});
    	
    	$scope.$watch('newnotice.content', function(newVal, oldVal) {
    	    if (newVal && newVal!=oldVal) {
    	    	if (newVal.length >= 1000) {
    	    		$scope.newnotice.content = newVal.substr(0, 1000);
    	    	}
    	    }	
    	});
    	
    	$scope.newnotice_submit=function(valid) {
    		$scope.notice_error = "";
    		if (valid) {
    			
    			var fd = new FormData();
    			fd.append("title", $scope.newnotice.title);
    			fd.append("content", $scope.newnotice.content);
    			fd.append("status", $scope.newnotice.status);
    			
    	    	$scope.start();
    	    	$scope.toView("main-content");
    			
    			$http({
    				method:"POST",
    				url:"admin/newnoticer.do",
    				data: fd,
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				if(data.status == 1) {
//    					$state.go("neworder_success", {orderid:data.orderid});
    					$scope.isSuccess = true;
        		    	$scope.complete();
    				} else if(data.status == 0) {
    					$scope.notice_error = "发布新公告失败：" + data.error; 
        		    	$scope.complete();
        		    	$scope.toView("new_notice_submit");
    				} else {
    					window.location.href = data;
    				}
    				
    			}).error(function(data){
    		    	$scope.complete();
    				alert("发生错误，请重新登录！");
    				window.location.href = "logout";
    			});
    		} else {
    			$scope.notice_error = "请确保数据填写正确！";
    		}
    	};
    }]);