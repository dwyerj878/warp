angular.module('appMgr.loginControllers', ['appMgr.services', 'appMgr.dialogs']);
																	 
/*
 * Manager the user list 
 * 
 */
angular.module('appMgr.loginControllers').controller('loginController',
		[ '$scope', 'SessionService', 'errorDialog', '$location', function($scope, SessionService, errorDialog, $location) {

			var init = function() {
				console.info("init LoginController start");
				
				console.info("init LoginController complete");
			}

			init();
			
			$scope.login = function() {
				console.info("login");
  			  	if ($scope.userName && $scope.password)  {  			  		
  			  		SessionService.setUserAuthenticated(true);
  			  		$location.path('/apps');
				}
  			  	else 
  			  		errorDialog("Could not Login", {data : ["Invalid Credentials"]});
  			  	console.info(SessionService.getUserAuthenticated());
			}

		} ]);

