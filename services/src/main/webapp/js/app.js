var appMgrApp = angular.module('appMgr', [ 'ngResource', 'ngRoute' , 'appMgr.appControllers', 'appMgr.userControllers', 'appMgr.headerControllers']);

appMgrApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/apps/:appId', {
		templateUrl : 'partials/app-detail.html',
		controller : 'AppDetailController'
	}).when('/apps', {
		templateUrl : 'partials/apps.html',
		controller : 'AppsController'
	}).when('/users/:userId', {
		templateUrl : 'partials/user-detail.html',
		controller : 'UserDetailController'
	}).when('/users', {
		templateUrl : 'partials/users.html',
		controller : 'UsersController'
	}).otherwise({
		redirectTo : '/apps'
	});
} ]);