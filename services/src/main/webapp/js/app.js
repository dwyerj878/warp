var appMgrApp = angular.module('appMgr', ['ngResource', 'ngRoute']);

appMgrApp.controller('AppsController',['$scope', 'Apps',  
   function ($scope, Apps){
	
		var init = function() {
			console.info("init AppsController start");
			$scope.newAppName = '';
			$scope.apps = Apps.query();
			console.info("init AppsController complete");
		}
		
		init();
		
		$scope.saveAppButtonDisabled = function() {
			return $scope.newAppName.length == 0;
		
		}
		
		$scope.saveNewApp = function() {
			console.log("save");
			Apps.save({name : $scope.newAppName}, function(data) {
				Apps.query(function(data){
					console.log('saved');
					$scope.apps = data;
					$scope.newAppName = '';    
				}, function(err){
					alert('refresh failed');
				});
			},
			function(data) {alert("save failed")})
		};
		
		
  }]
);


appMgrApp.controller('AppDetailController',['$scope', '$routeParams', 'Apps',  
  function ($scope, $routeParams, Apps){

	var init = function() {
		console.info("init AppDetailController start");
		$scope.appId = $routeParams.appId;
		Apps.get({appId : $scope.appId},
			function(data) {
				$scope.app = data;
				var x =data;
				console.info("found app " + $scope.app.name);
			}, 
			function (error) {
				alert("Could not find app" + err);
			}
		);
		
		
		console.info("init AppDetailController complete");
	}
	
	init();
	
	$scope.saveAppButtonDisabled = function() {
		return $scope.newAppName.length == 0;
	
	}
	
	$scope.saveApp = function() {
		console.log("save");
		Apps.save({name : $scope.newAppName}, function(data) {
			Apps.query(function(data){
				console.log('saved');
				$scope.apps = data;
				$scope.newAppName = '';    
			}, function(err){
				alert('refresh failed');
			});
		},
		function(data) {alert("save failed")})
   		};
   		
   		
     }]
);



appMgrApp.factory('Apps', function($resource) {
	return $resource('http://127.0.0.1:8080/services/rest/apps/:appId',  {appId:'@id'} ); // Note the full endpoint address
});

appMgrApp.config(['$routeProvider',
                    function($routeProvider) {
                      $routeProvider.
                      when('/apps/:appId', {
                          templateUrl: 'partials/app-detail.html',
                          controller: 'AppDetailController'
                        }).                      
                        when('/apps', {
                          templateUrl: 'partials/apps.html',
                          controller: 'AppsController'
                        }).
                        otherwise({
                          redirectTo: '/apps'
                        });
                    }]);