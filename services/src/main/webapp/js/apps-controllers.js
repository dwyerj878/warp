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

			$scope.saveAppButtonDisabled = function() {
				return $scope.newAppName.length == 0;
			}

			
			$scope.newApp = function() {				
				$location.path("/apps/new");						
			}

		} ]);


angular.module('appMgr.appControllers').controller(
		'AppDetailController',
		[ '$scope', '$routeParams', 'Apps', 'successDialog', 'errorDialog', '$location',
				function($scope, $routeParams, Apps, successDialog, errorDialog, $location) {

					var init = function() {
						console.info("init AppDetailController start");
						
						if ($routeParams.appId == "new")
							return;						
						
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
					
					
					$scope.deleteProperty = function(name) {
						console.info("delete property '" + name + "'");
						for (var index = 0; index < $scope.app.properties.length; index++) {
							val = $scope.app.properties[index];
							if (val.name == name)
								remove = index;								
						}
						if (remove)
							$scope.app.properties.splice(remove,1);
					
					}

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

