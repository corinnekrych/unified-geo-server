angular.module('unifiedgeoserver').factory('ApplicationResource', function($resource){
    var resource = $resource('rest/applications/:ApplicationId',{ApplicationId:'@id'},{'queryAll':{method:'GET',isArray:true},'query':{method:'GET',isArray:false},'update':{method:'PUT'}});
    return resource;
});