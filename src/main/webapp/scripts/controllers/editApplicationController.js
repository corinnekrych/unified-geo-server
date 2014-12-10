

angular.module('unifiedgeoserver').controller('EditApplicationController', function($scope, $routeParams, $location, ApplicationResource, InstallationResource ) {
    var self = this;
    $scope.disabled = false;
    $scope.$location = $location;
    
    $scope.get = function() {
        var successCallback = function(data){
            self.original = data;
            $scope.application = new ApplicationResource(self.original);
            $scope.installations = InstallationResource.queryAll({applicationId: $scope.application.id});
        };
        var errorCallback = function() {
            $location.path("/Applications");
        };
        ApplicationResource.get({ApplicationId:$routeParams.ApplicationId}, successCallback, errorCallback);
    };

    $scope.isClean = function() {
        return angular.equals(self.original, $scope.application);
    };

    $scope.save = function() {
        var successCallback = function(){
            $scope.get();
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        };
        $scope.application.$update(successCallback, errorCallback);
    };

    $scope.cancel = function() {
        $location.path("/Applications");
    };

    $scope.remove = function() {
        var successCallback = function() {
            $location.path("/Applications");
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError=true;
        }; 
        $scope.application.$remove(successCallback, errorCallback);
    };
    
    
    $scope.get();
});