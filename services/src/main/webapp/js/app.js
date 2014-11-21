var appMgrApp = angular.module('appMgr', []);

appMgrApp.controller('appListController', function ($scope){
	$scope.apps = [{"id":"1"},
	               {"id":"21"}];
	
    $scope.countries = [
      {"name": "China", "population": 1359821000},
      {"name": "India", "population": 1205625000},
      {"name": "United States of America","population": 312247000}
    ];
  });

