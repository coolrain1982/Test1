'use strict';

angular.module('order-table',['chieffancypants.loadingBar', 'ngAnimate'])
    .config(function(cfpLoadingBarProvider) {
    cfpLoadingBarProvider.includeSpinner = true;
  });

//angular.module('order-table').controller('ModalInstanceCtrl', 
//		    ['$scope', '$modalInstance', function($scope, $modalInstance, item) {
//		    	$scope.item = item;
//	    	    $scope.cancel = function() {
//	    	        $modalInstance.dismiss('cancel');
//	    	    };
//}]);