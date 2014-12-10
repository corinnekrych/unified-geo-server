'use strict';

angular.module('unifiedgeoserver',['ngRoute','ngResource'])
  .config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'views/landing.html',controller:'LandingPageController'})
      .when('/Applications',{templateUrl:'views/Application/search.html',controller:'SearchApplicationController'})
      .when('/Applications/new',{templateUrl:'views/Application/detail.html',controller:'NewApplicationController'})
      .when('/Applications/edit/:ApplicationId',{templateUrl:'views/Application/detail.html',controller:'EditApplicationController'})
      .when('/Installations',{templateUrl:'views/Installation/search.html',controller:'SearchInstallationController'})
      .when('/Installations/new',{templateUrl:'views/Installation/detail.html',controller:'NewInstallationController'})
      .when('/Installations/edit/:InstallationId',{templateUrl:'views/Installation/detail.html',controller:'EditInstallationController'})
      .otherwise({
        redirectTo: '/'
      });
  }])
  .controller('LandingPageController', function LandingPageController() {
  })
  .controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
  });
