var reviewmanModule = angular.module('review-man', [
		'chieffancypants.loadingBar', 'ngAnimate' ]);

reviewmanModule.config(function(cfpLoadingBarProvider) {
	cfpLoadingBarProvider.includeSpinner = false;
});

reviewmanModule.controller("reviewManController", [
		'$state',
		'$scope',
		'$modal',
		'$http',
		'cfpLoadingBar',
		'$timeout',
		'$popover',
		'orderTable',
		'commFunc',
		function($state, $scope, $modal, $http, cfpLoadingBar, $timeout,
				$popover, orderTable, commFunc) {

			$scope.showpage = true;
			$scope.commFunc = commFunc;
			$scope.reviewerror = null;
			$scope.isSuccess = false;
			$scope.search = {
				str : "",
			};
			
			switch ($state.current.name) {
			case "reviewInput":
				$scope.statetitle="数据录入";
				$scope.stateicon="fa fa-save";
				$scope.postURL = "admin/addReviewInfo.do";
				$scope.saveURL = "admin/saveReviewInfo.do";
				$scope.submitURL = "admin/submitReviewInfo.do";

				break;
			default:
				return;
			}
			
			$scope.createFD = function() {
				var fd = new FormData();
    			fd.append("orderid", $scope.curSubmitReview.orderid);
    			fd.append("reviewer",$scope.curSubmitReview.reviewer);
    			fd.append("sn", $scope.curSubmitReview.sn); 
    			fd.append("remark", $scope.curSubmitReview.remark);
    			if ($scope.curSubmitReview.id) {
    				fd.append("reviewid", $scope.curSubmitReview.id);
    			}
    			
    			return fd;
			}

			$scope.refresh = function() {
				$state.go($state.current, {}, {
					reload : true
				});
			}

			$scope.start = function() {
				cfpLoadingBar.start();
				$scope.showpage = false;
				$scope.doing = true;
				$scope.reviewerror = null;
			};

			$scope.complete = function(result) {
				cfpLoadingBar.complete();
				$scope.doing = false;
				$scope.showpage = true;
			};

			$scope.queryParams = {
				url : "admin/searchOrderByID.do",
				str : "",
			};

			$scope.searchOrder = function(event) {
				var keycode = window.event ? event.keyCode : event.which;
				if (keycode == 13) {
					$scope.search.str = $scope.search.str.replace(
							/(^\s*)|(\s*$)/g, "");
					if ($scope.search.str == "") {
						return;
					}
					$scope.queryParams.str = $scope.search.str;
					// 查询订单
					$scope.showReviewUI = false;
					$scope.start();
					$scope.orderList = null;
					$http.get($scope.queryParams.url, {
						params : {
							searchStr : $scope.queryParams.str,
						}
					}).success(function(res) {
						if (res && res.status == 1) {
							if (res.list.length == 0) {
								$scope.reviewerror = "未找到订单信息";
							}
							$scope.orderList = res.list;
						} else if (res && res.status == 0) {
							$scope.reviewerror = res.error;
						} else {
							window.location = "/login.html";
						}
						$scope.complete();
					}).error(function() {
						$scope.complete();
						alert("发生错误，请重新登录！");
						window.location.href = "logout";
					});
				}
			}
			
			$scope.modelDialog = $modal({
				scope : $scope,
				templateUrl : 'admin/Refund/detail.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
			});

			$scope.getUrlList = function(temp) {
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

			$scope.showDetail = function(item) {
				$scope.selectItem = item;
				orderTable.selectItem = item;
				orderTable.detailDialog = $scope.modelDialog;
				$scope.modelDialog.$promise.then($scope.modelDialog.show);
			};

			$scope.getShowLink = function(temp) {
				if (temp.length > 100) {
					return temp.substring(0, 100) + "...";
				}
				return temp;
			};

			$scope.getStatusClass = function(temp) {
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
					return "label-success";
				default:
					return "label-default";
				}
			}

			$scope.getStatus = function(temp) {
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
			
			$scope.getCursor = function(item) {
				if (orderTable.canClick(item)) {
					return "pointer";
				} else {
					return "default";
				}
			}
			
			$scope.getTypeDesc = function(item) {
				return commFunc.getSrvMode(item.find_product_mode) + "+" + commFunc.getSrvType(item);
			}
			
			//显示用户详细信息--------------------------------------------------------------------
			$scope.userInfoDialog = $modal({
				scope : $scope,
				templateUrl : 'angular/User/userInfo.template.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
			});
			
			$scope.showUserInfo = function(userName) {
				$scope.userInfoDialog.$promise.then($scope.userInfoDialog.show);
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
			
			//录入状态判断//////////////////////////////////////////////////////////////////////////
			$scope.canReview = function(item) {
				if (item.status == 7) {
					return true;
				}
				
				return false;
			}
			
			$scope.toReviewPage = function(item) {
				$scope.showReviewUI = true;
				$scope.selectItem = item;
				$scope.loadReviewInfo(item);
			}
			
			//显示review信息///////////////////////////////////////////////////////////////////////////////
			$scope.calcPayMoney = function(item) {
				return (commFunc.calcTotal(item).toFixed(2) * (1 + 0.001)).toFixed(2);
			}
			$scope.loadReviewInfo = function(item) { 
				if (item.reviewInfos) {
					return;
				}
				
				item.loadReviewInfoError = null;
				$http.get("reviewinfo/getReviewInfo.do",
					    { params:{
					        orderid: item.order_id,
					    }
				}).success(function(res) {
					if (res && res.status == 1) {
						item.reviewInfos = res.reviewInfos;
					} else if (res && res.status == 0) {
						item.loadReviewInfoError = res.error;
					} else {
						window.location="/login.html";
					}
			    }).error(function(data) {
					alert("发生错误，请重新登录！");
					window.location.href = "logout";
				});
			}
			
			//显示退款信息///////////////////////////////////////////////////////////////////////////////
			$scope.loadRefundInfo = function(item) { 
				if (item.refundInfos) {
					return;
				}
				$http.get("admin/getRefundInfoByOrderid.do",
					    { params:{
					        orderid: item.order_id,
					    }
				}).success(function(res) {
					if (res && res.status == 1) {
						item.loadRefundInfoError = null;
						item.refundInfos = res.refundInfos;
					} else if (res && res.status == 0) {
						item.loadRefundInfoError = res.error;
					} else {
						window.location="/login.html";
					}
			    }).error(function(data) {
					alert("发生错误，请重新登录！");
					window.location.href = "logout";
				});
			}
			
			$scope.getRefundType = function(type) {
				switch (type) {
				case 1:
					return "未留评退款";
				case 2:
					return "未完成退款";
				default:
					return "未知";
				}
			}
			
			//增加review信息//////////////////////////////////////////////////////////////////
			$scope.newreviewModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Review/newreview.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			$scope.reviewCancel = function() {
				$scope.newreviewModal.hide();
			}
			
			$scope.addReview = function(item) {
				$scope.newreview={
						sn:null,
						reviewer:null,
						remark:null,
						orderid:item.order_id,
						error:null,
				};
				$scope.newreviewModal.$promise.then($scope.newreviewModal.show);
			}
			
			//提交review信息//////////////////////////////////////////////////////////////
			$scope.newreviewConfirmModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Refund/confirm.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			
			$scope.showReviewConfirm = function(opReview, currentModal, isNew) {
				opReview.error = null;
				//检查信息是否填写完整
				if (!opReview.reviewer) {
					opReview.error = "请输入留评账号";
					return;
				}
				
				if (!opReview.sn) {
					opReview.error = "请输入亚马逊订单号";
					return;
				}
				
				if (!opReview.remark) {
					opReview.error = "请输入留评内容";
					return;
				}
				
				$scope.curSubmitReview = opReview;
				$scope.curModal = currentModal;
				if (isNew == 1) {
					$scope.postURL = "admin/addReviewInfo.do";
				} else {
					$scope.postURL = "admin/submitReviewInfo.do";
				}
								
				$scope.confirmTitle = "确定提交该Review信息吗?";
				currentModal.hide();
				$scope.newreviewConfirmModal.$promise.then($scope.newreviewConfirmModal.show);
			}
			
			$scope.confirmCancel = function() {
				$scope.newreviewConfirmModal.hide();
				$scope.curModal.$promise.then($scope.curModal.show);
			}
			
			$scope.confirmOK = function() {
				$scope.start();  			
    			$http({
    				method:"POST",
    				url: $scope.postURL,
    				data: $scope.createFD(),
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				$scope.newreviewConfirmModal.hide();
    				$scope.complete();
    				if(data.status == 1) {
    					if (!$scope.selectItem.reviewInfos) {
    						$scope.selectItem.reviewInfos = [];
    					} 
    					for(idx in $scope.selectItem.reviewInfos) {
    						if ($scope.selectItem.reviewInfos[idx].id == data.reviewinfo.id) {
    							$scope.selectItem.reviewInfos[idx] = data.reviewinfo;
    							return;
    						}
    					}
    					
    					$scope.selectItem.reviewInfos.push(data.reviewinfo);
    				} else if(data.status == 0) {
    					$scope.curSubmitReview.error = data.error;
    					$scope.curModal.$promise.then($scope.curModal.show);
    				} else {
    					window.location="/login.html";
    				}	
    			}).error(function(data){
    		    	$scope.complete();
    				alert("发生错误，请重新登录！");
    				window.location.href = "logout";
    			});
			}
			
			//保存review信息------------------------------------------------------
			$scope.savereviewConfirmModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Review/confirmSave.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			
			$scope.createSaveReviewFD = function() {  
				var fd = new FormData();
    			fd.append("orderid", $scope.currentOpReview.orderid);
    			fd.append("reviewer", $scope.currentOpReview.reviewer);
    			fd.append("sn", $scope.currentOpReview.sn); 
    			if ($scope.currentOpReview.remark) {
    				fd.append("remark", $scope.currentOpReview.remark);
    			}
    			if ($scope.currentOpReview.id) {
    				fd.append("reviewid", $scope.currentOpReview.id);
    			}
    			
    			return fd;
			}
			
			$scope.saveReview = function(opReview, currentModal, nextModal) {
				opReview.error = null;
				$scope.preModal = null;
				$scope.currentOpReview = null;
				//检查信息是否填写完整
				if (!opReview.reviewer) {
					opReview.error = "请输入留评账号";
					return;
				}
				
				if (!opReview.sn) {
					opReview.error = "请输入亚马逊订单号";
					return;
				}
				
				$scope.confirmSaveTitle = "确定保存该Review信息吗?";
				currentModal.hide();
				nextModal.$promise.then(nextModal.show);
				$scope.preModal = currentModal;
				$scope.currentOpReview = opReview;
			}
			
			$scope.confirmSaveCancel = function() {
				$scope.savereviewConfirmModal.hide();
				$scope.preModal.$promise.then($scope.preModal.show);
			}
			
			$scope.confirmSaveOK = function() {
				$scope.start();  			
    			$http({
    				method:"POST",
    				url: $scope.saveURL,
    				data: $scope.createSaveReviewFD(),
    				headers: {"content-type":undefined},
    				transformRequest: angular.identity
    			}).success(function(data) {
    				$scope.savereviewConfirmModal.hide();
    				$scope.complete();
    				if(data.status == 1) {
    					if (!$scope.selectItem.reviewInfos) {
    						$scope.selectItem.reviewInfos = [];
    					} 
    					for(idx in $scope.selectItem.reviewInfos) {
    						if ($scope.selectItem.reviewInfos[idx].id == data.reviewinfo.id) {
    							$scope.selectItem.reviewInfos[idx] = data.reviewinfo;
    							return;
    						}
    					}
    					
    					$scope.selectItem.reviewInfos.push(data.reviewinfo);
    				} else if(data.status == 0) {
    					$scope.currentOpReview.error = data.error;
    					$scope.preModal.$promise.then($scope.preModal.show);
    				} else {
    					window.location="/login.html";
    				}	
    			}).error(function(data){
    		    	$scope.complete();
    				alert("发生错误，请重新登录！");
    				window.location.href = "logout";
    			});
			}
			
			//更新review////////////////////////////////////////////////////////////////////////
			$scope.modifyreviewModal = $modal({
				scope : $scope,
				templateUrl : 'admin/Review/modifyreview.html',
				show : false,
				animation: 'am-fade-and-slide-top',
				keyboard:false,
				backdrop:'static',
			});
			
			$scope.updateReview = function(item, orderItem) {
				item.sn = item.a_No;
				item.reviewer = item.review_name;
				item.remark = item.review_remark;
				item.orderid = orderItem.order_id,
				$scope.reviewForModify = item;
				$scope.modifyreviewModal.$promise.then($scope.modifyreviewModal.show);
			}
			
			
		} ]);