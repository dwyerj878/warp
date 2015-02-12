angular.module('appMgr.appControllers', ['appMgr.services', 'appMgr.dialogs']);

/*
 * Manage aplication list 
 */
angular.module('appMgr.appControllers').controller('AppsController',
		[ '$scope', 'Apps', 'errorDialog', '$location', function($scope, Apps, errorDialog, $location) {

			var init = function() {
				console.info("init AppsController start");
				$scope.newAppName = '';
				$scope.apps = Apps.query();
				console.info("init AppsController complete");
			}

			init();
			
			/*
			 * redirect to new app screen
			 */
			$scope.newApp = function() {				
				$location.path("/apps/new");						
			}

		} ]);


/*
 * Manage Application details
 */
angular.module('appMgr.appControllers').controller(
		'AppDetailController',
		[ '$scope', '$routeParams', 'Apps', 'successDialog', 'errorDialog', '$location',
				function($scope, $routeParams, Apps, successDialog, errorDialog, $location) {

					var init = function() {
						console.info("init AppDetailController start");
						
						// set available types
						// TODO - this should be data driven
						$scope.availableTypes = [
						"String",
						"Number",
						"Boolean"
                        ];
						
						// Check if this is a new app
						if ($routeParams.appId == "new")
							return;						
						
						// find app by id
						$scope.appId = $routeParams.appId;
						Apps.get({
							appId : $scope.appId
						}, function(data) {
							$scope.app = data;
							console.info("found app " + $scope.app.name);
						}, function(error) {
							errorDialog("Could not Find App", error);
						});

						$scope.newProp = {
							name : "",
							environment : "",
							type : "",
							value : ""
						}

						console.info("init AppDetailController complete");
					}

					init();
					
					
					/*
					 * Save property 
					 */
					$scope.saveProperty = function() {
						console.info("save property");
						$scope.newProp.appId = $scope.app.id;
						var existingProps = $scope.app.properties;
						existingProps[$scope.app.properties.length] = {
							name : $scope.newProp.name,
							environment : $scope.newProp.environment,
							type : $scope.newProp.type,
							value : $scope.newProp.value
						};
						$scope.newProp.name = "";
						$scope.newProp.type = "";
						$scope.newProp.value = "";

					}
					
					/*
					 * Delete property
					 */
					$scope.deleteProperty = function(name, environment) {
						console.info("delete property '" + name + "'");
						// find the index of the matching property
						remove = -1;
						for (var index = 0; index < $scope.app.properties.length; index++) {
							val = $scope.app.properties[index];
							if (val.name == name && val.environment == environment)
								remove = index;								
						}
						if (remove > -1)
							$scope.app.properties.splice(remove,1);					
					}
					
					/*
					 * Check that a property is complete and not a duplicate
					 */
					$scope.validProperty = function() {
						// check values are not empty
						if (!$scope.newProp.name) return false;
						if ($scope.newProp.name.length == 0) return false;
						
						if ($scope.newProp.environment.length == 0) return false;
						if (!$scope.newProp.type) return false;
						
						if (!$scope.newProp.type) return false;
						if ($scope.newProp.type.length == 0) return false;
						
						if (!$scope.newProp.value) return false;
						if ($scope.newProp.value.length == 0) return false;
						
						// check that this is not a duplicate
						for (var index = 0; index < $scope.app.properties.length; index++) {
							val = $scope.app.properties[index];
							if (val.name == $scope.newProp.name &&  val.environment == $scope.newProp.environment)
								return false;
						}				
						return true;
					}

					/*
					 * Save the application
					 */
					$scope.saveApp = function() {
						if ($scope.app.id) {
							Apps.update({
								appId : $scope.app.id
							}, $scope.app, function(data) {
								$scope.app = data;
								console.info("saved app " + $scope.app.name);
								successDialog('App Saved "' +  $scope.app.name + '"');								
								$location.path("/apps/"+$scope.app.id);								
							}, function(error) {
								errorDialog("Error Saving App", error);
							});

						} else {
							Apps.save($scope.app, function(data) {
								$scope.app = data;
								console.info("Saved app " + $scope.app.name);
								successDialog('App Saved "' +  $scope.app.name + '"');								
								$location.path("/apps/"+$scope.app.id);
							}, function(error) {
								errorDialog("Error Saving App", error);
							});
						}
					}

				} ]);

