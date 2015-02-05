angular.module('appMgr.headerControllers', [ 'appMgr.services' ]);

/*
 * Manage aplication list
 */
angular.module('appMgr.headerControllers').controller('headerController',
		[ '$scope', 'ip', function($scope, ip) {

			var init = function() {
				console.info("init Header start");
				 ip.get(
						function(data) {
							$scope.ipAddress = data.origin;
							console.info("found ip " + $scope.ipAddress);
						}, function(error) {
							alert("Could not find user" + error);
						});
						
							
				console.info("init Header complete");
			}

			init();

		} ]);
