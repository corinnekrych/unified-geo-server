
angular.module('unifiedgeoserver').controller('NewInstallationController', function ($scope, $location, locationParser, InstallationResource ) {
    $scope.disabled = false;
    $scope.$location = $location;
    $scope.installation = $scope.installation || {};
    

    $scope.save = function() {
        var successCallback = function(data,responseHeaders){
            var id = locationParser(responseHeaders);
            $location.path('/Installations/edit/' + id);
            $scope.displayError = false;
        };
        var errorCallback = function() {
            $scope.displayError = true;
        };
        InstallationResource.save($scope.installation, successCallback, errorCallback);
    };
    
    $scope.cancel = function() {
        $location.path("/Installations");
    };
});