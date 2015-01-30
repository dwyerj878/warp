angular.module('appMgr.services', []);

/*
 * Rest services for apps
 */
angular.module('appMgr.services').factory('Apps', function($resource) {
	return $resource('http://127.0.0.1:8080/services/rest/apps/:appId', {
		appId : '@id'
	}, {
		'update' : {
			method : 'PUT',
			params : {
				appId : '@id'
			}
		}
	}

	); // Note the full endpoint address
});

/*
 * rest services for users
 */
angular.module('appMgr.services').factory('Users', function($resource) {
	return $resource('http://127.0.0.1:8080/services/rest/users/:userId', {
		userId : '@id'
	},
	{
			'update' : {
			method : 'PUT',
			params : {
				userId : '@id'
			}
		}
	}
	
	); // Note the full endpoint address
});


/*
 * rest services for users
 */
angular.module('appMgr.services').factory('ip', function($resource) {
	return $resource('http://httpbin.org/ip' 
	); // Note the full endpoint address
});

