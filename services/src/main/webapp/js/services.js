var appServices = angular.module('applicationService', ['ngResource']);

appServices.factory('applicationServices', 
		function($resource) {
			return  {
				getAllApps : function() {
					return AppWS.listAllApplications();
				}
			};
	});