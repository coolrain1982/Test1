'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.
  module('image-upload').
  component('imageUpload', {
    templateUrl: 'angular/Image/upload.template.html',
    controller: ['$state', '$scope', '$http', function imgUploadController($state, $scope, $http) {
    	this.reader = new FileReader();
    	this.thumb = {};
    	this.thumb_default={
    			1111:{},
    	};
    	
    	$scope.$emit("thumb_change", this.thumb);
    	
    	this.index = 0;
    	
    	this.img_upload = function(files) {
    		this.fileError = "";
    		if (files[0].size > 300 * 1024) {
    			$scope.$apply(function() {
    				$scope.$ctrl.fileError = "上传的图片大小不能超过300k！";
    			});
    			
    			return;
    		}
    		
    		this.guid = (new Date()).valueOf();
    		this.reader.readAsDataURL(files[0]);
    		this.onloadFile = files[0];
    		this.reader.onload=function(ev) {
    			$scope.$apply(function() {
    				$scope.$ctrl.thumb[$scope.$ctrl.guid] = {
    					imgSrc : ev.target.result,	
    					left : ($scope.$ctrl.index ++) * 128 + 128,
    					file: $scope.$ctrl.onloadFile,
    				};
    			});
    		};
    		
//    		var data = new FormData();
//    		data.append('image', files[0]);
//    		data.append('guid', this.guid);
//    		$http({
//    			method:'post',
//    			url: $state.current.name + '/upload',
//    			data : data, 
//    			headers : {'Content-Type' : undefined}, 
//    			transformRequest: angular.identity
//    		}).success(function(data) {
//    			if (data.result_code='SUCCESS') {
//    				this.form.image[data.guid] = data.result_value;
//    				this.thumb[data.guid].status = 'SUCCESS';
//    			}
//    		});
    	};
    	
    	this.img_del = function(item) {
    		var tempIndex = 0;
    		var delp = 0;
    		for (var p in this.thumb) {
    			if (this.thumb[p].left != item.left) {
    				this.thumb[p].left = (tempIndex ++)*128+128;
    			} else {
    				delp = p;
    			}
    		}
    		
    		if (delp != 0) {
	    		delete this.thumb[delp];
	    		this.index --;
    		}
    	}
    }]
  });