angular.module('appMgr.userControllers', ['appMgr.services', 'appMgr.dialogs']);
																	 
/*
 * Manager the user list 
 * 
 */
angular.module('appMgr.userControllers').controller(
		'UsersController',
		[ '$scope', '$location', 'Users', function($scope, $location, Users) {

			var init = function() {
				console.info("init UsersController start");
				$scope.users = Users.query();
				console.info("init UsersController complete");
			}

			init();

			$scope.newUser = function() {
				  $location.path("/users/new"); 
			};

		} ]);


/*
 * Manage the user details 
 */
angular.module('appMgr.userControllers').controller(
		'UserDetailController',
		[ '$scope', '$routeParams', 'Users', 'errorDialog', 'successDialog', '$location',
				function($scope, $routeParams, Users, errorDialog, successDialog, $location) {

					/*
					 * Init user view 
					 */
					var init = function() {
						console.info("init UserDetailController start (" + $routeParams.userId + ")");
						
						// check if user is new
						if ($routeParams.userId == "new")
							return;
						
						// get user details by id
						$scope.userId = $routeParams.userId;						
						Users.get({
							userId : $scope.userId
						}, function(data) {
							$scope.user = data;
							console.info("found user " + $scope.user.name);
						}, function(error) {
							errorDialog("Error Saving User", error);
						});

						console.info("init UserDetailController complete");
					}

					$scope.saveUser = function() {
						if ($scope.user.id) {
							Users.update({
								userId : $scope.user.id
							}, $scope.user, function(data) {
								$scope.user = data;
								console.info("saved user " + $scope.user.name);
								successDialog('User Saved');								
								$location.path("/users/"+$scope.user.id);
							}, function(error) {
								errorDialog("Error Saving User", error);
							});

						} else {
							Users.save($scope.user, function(data) {
								$scope.user = data;								
								console.info("Saved user " + $scope.user.name);
								successDialog('User Saved');								
								$location.path("/users/"+data.id);
							}, function(error) {
								errorDialog("Error Saving User", error);
							});
						}
					}
					
					init();

				} ]);
