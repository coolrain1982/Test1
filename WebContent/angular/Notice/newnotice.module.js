'use strict';

var newNotoceModule = angular.module('new-notice', ['chieffancypants.loadingBar', 'ngAnimate']);

newNotoceModule.config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = false;
});

newNotoceModule.controller("newNoticeController", ['$state', '$stateParams', '$modal',
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
    	    function($state, $stateParams, $modal, $scope, $http, $timeout, 
    	    		cfpLoadingBar, $location, $anchorScroll, commFunc) {
    	
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
    					window.location="/login.html";
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

newNotoceModule.controller("noticeController", ['$state', '$stateParams', '$modal',
	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll', 'commFunc',
	    function($state, $stateParams, $modal, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll, commFunc) {
	
	$scope.commFunc = commFunc;
	$scope.isModify = false;
	
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
				window.location="/login.html";
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
	
	//公告操作确认弹出窗口
	$scope.confirmDialog = $modal({
		scope : $scope,
		templateUrl : 'admin/Notice/confirm.html',
		show : false,
		animation: 'am-fade-and-slide-top',
		backdrop:'static',
		keyboard:false,
	});
	
	
	//置顶相关//////////////////////////////////////////////////////////////////////////////////////////////////
	//置顶操作
	$scope.noticeActionStart = function() {
		$scope.noticeOpError = null;
		$scope.doing = true;
		cfpLoadingBar.start();
	}
	
	$scope.noticeActionFinish = function() {
		$scope.doing = false;
		$scope.confirmDialog.hide();
		cfpLoadingBar.complete();
	}
	
	//取消对应操作
	$scope.confirmCancel = function() {
		$scope.confirmDialog.hide();
	}
	
	$scope.top = function(item, istop) {
		$scope.selectItem = item;
		if (istop) {
			$scope.confirmTitle = "确定将该条公告置顶吗？";
			$scope.settopaction="1";
		} else {
			$scope.confirmTitle = "确定将该条公告取消置顶吗？";
			$scope.settopaction="0";
		}
		//确认置顶或取消置顶
		$scope.confirmOK = function() {
		   $scope.noticeActionStart();
		   $http.get("admin/setNoticeTop.do",
				{ params:{
			        id: $scope.selectItem.id,
			        settopaction: $scope.settopaction,
			    }
			}).success(function(res) {
				$scope.noticeActionFinish();
				if (res && res.status == 1) {
					$state.go($state.current, {}, {reload:true});
				} else if (res && res.status == 0) {
					$scope.noticeOpError = res.error;
				} else {
					window.location="/login.html";
				}
			}).error(function() {
				$scope.noticeActionFinish();
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}
		
		$scope.confirmDialog.$promise.then($scope.confirmDialog.show);
	}
	
	//删除公告相关//////////////////////////////////////////////////////////////////
	$scope.delete = function(item, istop) {
		$scope.selectItem = item;
		$scope.confirmTitle = "确定删除该条公告吗？";
		//确认置顶或取消置顶
		$scope.confirmOK = function() {
		   $scope.noticeActionStart();
		   $http.get("admin/deleteNotice.do",
				{ params:{
			        id: $scope.selectItem.id,
			    }
			}).success(function(res) {
				$scope.noticeActionFinish();
				if (res && res.status == 1) {
					$state.go($state.current, {}, {reload:true});
				} else if (res && res.status == 0) {
					$scope.noticeOpError = res.error;
				} else {
					window.location="/login.html";
				}
			}).error(function() {
				$scope.noticeActionFinish();
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}
		$scope.confirmDialog.$promise.then($scope.confirmDialog.show);
	}
	
	//修改公告相关//////////////////////////////////////////////////////////////////
	$scope.modify = function(item, istop) {
		$scope.selectItem = item;
		$scope.isModify = true;
		
		$scope.modifySubmit = function() {
			$scope.modifyerror = null;
			$scope.confirmTitle = "确定提交该条公告的修改内容吗？";
			$scope.confirmDialog.$promise.then($scope.confirmDialog.show);
		}
		
		$scope.confirmOK = function() {			   
		   if (!$scope.selectItem.newcontent || $scope.selectItem.newcontent.length > 10*1000) {
			   $scope.modifyerror = "请正确输入公告内容，内容字数必须为1-10K之间";
			   $scope.confirmDialog.hide();
			   return;
		   }
		   
		   $scope.noticeActionStart();
		   
		   var fd = new FormData();
		   fd.append("id", $scope.selectItem.id);
		   fd.append("newcontent", $scope.selectItem.newcontent);
		   
		   $http({
				method:"POST",
				url:"admin/modifyNotice.do",
				data: fd,
				headers: {"content-type":undefined},
				transformRequest: angular.identity
			}).success(function(res) {
				$scope.noticeActionFinish();
				if (res && res.status == 1) {
					$state.go($state.current, {}, {reload:true});
				} else if (res && res.status == 0) {
					$scope.modifyerror = res.error;
				} else {
					window.location="/login.html";
				}
			}).error(function() {
				$scope.noticeActionFinish();
				alert("发生错误，请重新登录！");
				window.location.href = "logout";
			});
		}
	}
	
	$scope.cancelModify = function() {
		$scope.isModify = false;
	}
	
	$scope.isTop = function(item) {
		if (item.top && item.top > 0) {
			return "[置顶]";
		}
	}
}]);