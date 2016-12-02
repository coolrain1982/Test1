'use strict';

angular.module('finish-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('finish-order').
    controller("finishOrderController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll) {
    	
    }]);