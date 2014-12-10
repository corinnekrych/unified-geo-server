

angular.module('unifiedgeoserver').controller('EditInstallationController', function($scope, $routeParams, $location, InstallationResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.installation = new InstallationResource(self.original);
        };
        var errorCallback = function() {
            $location.path("/Installations");
        };
        InstallationResource.get({InstallationId:$routeParams.InstallationId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.installation);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.installation.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Installations");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Installations");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.installation.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});