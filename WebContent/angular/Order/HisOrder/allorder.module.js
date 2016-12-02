'use strict';

angular.module('all-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('all-order').
    controller("allOrderController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll) {
    	
    }]);