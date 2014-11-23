var appMgrApp = angular.module('appMgr', ['ngResource']);

appMgrApp.controller('appListController',['$scope', 'Apps',  
   function ($scope, Apps){
		console.info("init appListController start");
		$scope.newAppName = '';
		$scope.apps = Apps.query();
		console.info("init appListController complete");
		
		$scope.saveAppButtonDisabled = function() {
			return $scope.newAppName.length == 0;
		
		}
		
		$scope.saveNewApp = function() {
			console.log("save");
			Apps.save({name : $scope.newAppName});
			$scope.apps = Apps.query();
			
			$scope.newAppName = '';
			console.log('saved');
		}
		
		
  }]);


appMgrApp.factory('Apps', function($resource) {
	return $resource('http://127.0.0.1:8080/services/rest/apps/:id'); // Note the full endpoint address
});