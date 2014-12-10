'use strict';

angular.module('unifiedgeoserver').filter('startFrom', function() {
    return function(input, start) {
        start = +start; //parse to int
        return input.slice(start);
    };
});