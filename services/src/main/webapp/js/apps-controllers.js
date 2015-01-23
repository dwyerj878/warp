angular.module('appMgr.appControllers', ['appMgr.services']);

/*
 * Manage aplication list 
 */
angular.module('appMgr.appControllers').controller('AppsController',
		[ '$scope', 'Apps', function($scope, Apps) {

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
				Apps.save({
					name : $scope.newAppName
				}, function(data) {
					Apps.query(function(data) {
						console.log('saved');
						$scope.apps = data;
						$scope.newAppName = '';
					}, function(err) {
						alert('refresh failed');
					});
				}, function(data) {
					alert("save failed")
				})
			};

		} ]);


angular.module('appMgr.appControllers').controller(
		'AppDetailController',
		[ '$scope', '$routeParams', 'Apps',
				function($scope, $routeParams, Apps) {

					var init = function() {
						console.info("init AppDetailController start");
						$scope.appId = $routeParams.appId;
						Apps.get({
							appId : $scope.appId
						}, function(data) {
							$scope.app = data;
							var x = data;
							console.info("found app " + $scope.app.name);
						}, function(error) {
							alert("Could not find app" + error);
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

					$scope.saveApp = function() {
						if ($scope.app.id) {
							Apps.update({
								appId : $scope.app.id
							}, $scope.app, function(data) {
								$scope.app = data;
								var x = data;
								console.info("saved app " + $scope.app.name);
							}, function(error) {
								alert("Could not save app" + error.status);
							});

						} else {
							Apps.save($scope.app, function(data) {
								$scope.app = data;
								var x = data;
								console.info("Saved app " + $scope.app.name);
							}, function(error) {
								alert("Could not save app" + error.status);
							});
						}
					}

				} ]);

