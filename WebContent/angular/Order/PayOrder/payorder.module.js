'use strict';

angular.module('pay-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('pay-order').
    controller("allOrderController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll) {
    	
    }]);