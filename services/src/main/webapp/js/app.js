var appMgrApp = angular.module('appMgr', [ 'ngResource', 'ngRoute',
		'appMgr.appControllers', 'appMgr.userControllers','appMgr.loginControllers',
		'appMgr.headerControllers', 'appMgr.dialogs' ]);

appMgrApp.config([ '$routeProvider', function($routeProvider) {
	$routeProvider.when('/apps/:appId', {
		templateUrl : 'partials/app-detail.html',
		controller : 'AppDetailController',
		resolve: { loginRequired: loginRequired }
	}).when('/apps', {
		templateUrl : 'partials/apps.html',
		controller : 'AppsController',
		resolve: { loginRequired: loginRequired }
	}).when('/users/:userId', {
		templateUrl : 'partials/user-detail.html',
		controller : 'UserDetailController',
		resolve: { loginRequired: loginRequired }
	}).when('/users', {
		templateUrl : 'partials/users.html',
		controller : 'UsersController',
		resolve: { loginRequired: loginRequired }
	}).when('/login', {
		templateUrl : 'partials/login.html',
		controller : 'loginController'
	}).otherwise({
		redirectTo : '/apps'
	});
} ]);




appMgrApp.service('SessionService', function(){
    var userIsAuthenticated = false;

    this.setUserAuthenticated = function(value){
    	console.info("User authenticated :" + value);
    	
        userIsAuthenticated = value;
    };

    this.getUserAuthenticated = function(){
        return userIsAuthenticated;
    };
});

var loginRequired = function($location, $q, SessionService) {  
    var deferred = $q.defer();
    console.info("auth");
    if(! SessionService.getUserAuthenticated()) {
        deferred.reject()
        $location.path('/login');
    } else {
        deferred.resolve()
    }

    return deferred.promise;
}


