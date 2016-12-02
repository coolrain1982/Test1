'use strict';

angular.module('reject-order', ['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
        cfpLoadingBarProvider.includeSpinner = true;
});

angular.module('reject-order').
    controller("allOrderController", ['$state', '$stateParams', 
    	      '$scope', '$http', '$timeout', 'cfpLoadingBar','$location', '$anchorScroll',
    	    function($state, $stateParams, $scope, $http, $timeout, cfpLoadingBar, $location, $anchorScroll) {
    	
    }]);