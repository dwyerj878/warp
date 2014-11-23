var appMgrApp = angular.module('appMgr', []);

appMgrApp.controller('appListController',['$scope', 'applicationServices',  function ($scope, applicationService){
//	$scope.apps = [{"id":"1"},
//	               {"id":"21"}];
	$scope.apps = applicationService.getAllApps();
	console.log($scope.apps);
  }]);

appMgrApp.factory('applicationServices', 
		function() {
			return  {
				getAllApps : function() {
					return AppWS.listAllApplications();
				}
			};
	});

