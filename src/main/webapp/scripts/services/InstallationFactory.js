angular.module('unifiedgeoserver').factory('InstallationResource', function($resource){
    var resource = $resource('rest/installations/:InstallationId',{InstallationId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});