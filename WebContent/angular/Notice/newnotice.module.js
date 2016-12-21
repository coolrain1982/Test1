'use strict';

angular.module('new-notice', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

angular.module('new-notice').
    controller("newNoticeController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll, commFunc) {
    	
    	$scope.commFunc = commFunc;
    	
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
    	$scope.newnotice_show = true;
    	
    	$scope.newnotice = {
        		title :"",
        	    content : "",
        	    status: 1,
        	    error:  "",
        	    releaseDate: "",
        	    url:"",
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
    	
    	$scope.showNotice = function(url) {
    		if (url) {
    			window.open("/MeiYabuy/upload/notice" + url + ".html", $scope.newnotice.title,
    					"height=600,width=800, top=50%, left=50%");
    		}
    	}
    	
    	$scope.newnotice_submit=function(valid) {
    		$scope.newnotice.error = "";
    		if (valid) {
    			
    			var fd = new FormData();
    			fd.append("title", $scope.newnotice.title);
    			fd.append("content", $scope.newnotice.content);
    			fd.append("status", $scope.newnotice.status);
    			
    	    	$scope.start();
    	    	$scope.toView("main-content");
    			
    			$http({
    				method:"POST",
    				url:"admin/notice/release.do",
    				data: fd,
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				if(data.status == 1) {
    					$scope.newnotice.releaseDate = data.date;
    					$scope.newnotice.url = data.url;
    					$scope.isSuccess = true;
        		    	$scope.complete();
    				} else if(data.status == 0) {
    					$scope.newnotice.error = "发布新公告失败：" + data.error; 
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
    			$scope.newnotice.error = "请确保数据填写正确！";
    		}
    	};
    }]);

angular.module('new-notice').
controller("noticeController", ['$state', '$stateParams', 
	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll, commFunc) {
	
	$scope.commFunc = commFunc;
	
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
    
    $scope.start = function() {
    	$scope.timeout = $timeout(function() {
			cfpLoadingBar.start();
			}, 2000);
	};
	
	$scope.complete = function () {
		if ($scope.timeout) {
			$timeout.cancel($scope.timeout);
		}
		cfpLoadingBar.complete();
	};
    
    $scope._get = function(page, size, callback) {
    	if ($scope.init && $scope.p_current == page) {
    		return;
    	}
    	
    	$scope.init = true;
    	$scope.start();
		$http.get("notice/get.do",
				{ params:{
			        status:1,
			        page: page,
			        size: size,
			    }
		}).success(function(res) {
			if (res && res.status == 1) {
				$scope.recordSize = res.count;
				$scope.noticeList = res.list;
				$scope.p_current = page;
				$scope.p_all_page = Math.ceil($scope.recordSize/ $scope.p_pernum);
				$scope.reloadPno();
				callback();
			} else if (res && res.status == 0) {
				$scope.noticeError = res.error;
				$scope.recordSize = res.count;
			} else {
				window.location.href = res;
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
	// //////////////////////////////////////////////////////
 
	$scope._get($scope.p_current, $scope.p_pernum, function(){});
	
	$scope.showNotice = function(url) {
		
		var iWidth = 800;
		var iHeight=600;
		var iTop = (window.screen.availHeight-30-iHeight)/2;
		var iLeft = (window.screen.availWidth - 10 - iWidth) / 2;
		
		if (url) {
			window.open("/MeiYabuy/upload/notice" + url + ".html", url,
					"height=" + iHeight + ",width=" + iWidth+ ", top="+iTop+ ", left=" + iLeft);
		}
	}
}]);