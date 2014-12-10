
angular.module('unifiedgeoserver').controller('NewApplicationController', function ($scope, $location, locationParser, ApplicationResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.application = $scope.application || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Applications/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        ApplicationResource.save($scope.application, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Applications");
    };
});