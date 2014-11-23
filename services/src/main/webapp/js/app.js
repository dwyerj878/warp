var appMgrApp = angular.module('appMgr', []);

appMgrApp.controller('appListController',['$scope', 'applicationServices',  function ($scope, applicationService){
	$scope.apps = [{"id":"1"},
	               {"id":"21"}];
	
  }]);

appMgrApp.factory('applicationServices', 
		function() {
			return  {
				getAllApps : function() {
					return AppWS.listAllApplications();
				}
			};
	});

