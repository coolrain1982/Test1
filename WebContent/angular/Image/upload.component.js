'use strict';

// Register `phoneList` component, along with its associated controller and template
angular.
  module('image-upload').
  component('imageUpload', {
    templateUrl: 'angular/Image/upload.template.html',
    controller: ['$state', '$scope', '$http', function imgUploadController($state, $scope, $http) {
    	this.reader = new FileReader();
    	this.form = {
    			image: {},
    	};
    	this.thumb = {};
    	this.thumb_default={
    			1111:{},
    	};
    	
    	this.img_upload = function(files) {
    		this.guid = (new Date()).valueOf();
    		this.reader.readAsDataURL(files[0]);
    		this.reader.onload=function(ev) {
    			$scope.$apply(function() {
    				$scope.$ctrl.thumb[$scope.$ctrl.guid] = {
    					imgSrc : ev.target.result,	
    				};
    			});
    		};
    		
    		var data = new FormData();
    		data.append('image', files[0]);
    		data.append('guid', this.guid);
    		$http({
    			method:'post',
    			url: $state.current.name + '/upload',
    			data : data, 
    			headers : {'Content-Type' : undefined}, 
    			transformRequest: angular.identity
    		}).success(function(data) {
    			if (data.result_code='SUCCESS') {
    				this.form.image[data.guid] = data.result_value;
    				this.thumb[data.guid].status = 'SUCCESS';
    			}
    		});
    	};
    	
    	this.img_del = function(item) {
    		var guidArr = [];
    		for (var p in this.thumb) {
    			guidArr.push(p);
    		}
    		
    		delete this.thumb[item];
    		delete this.form.image[item];
    	}
    }]
  });