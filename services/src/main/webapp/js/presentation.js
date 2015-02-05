angular.module('appMgr.dialogs', [ 'ngDialog' ]);

/*
 * Dialog popups
 */
angular.module('appMgr.dialogs').factory(
		'errorDialog',
		[ 'ngDialog', function(ngDialog) {
			return function(msg, error) {
				message = "<br><font color='red'>" +msg+"</font></br>";
				message += "<br>"+ error.status + "<br>";
				
				message += "<ul>";
				for (k in error.data)
					message += "<li>" + k + " : " + error.data[k] + "</li>";
				message += "</ul>";
				
				console.info(message);
				ngDialog.open({
				    template: message,
				    plain: true,
				    showClose: true,
			        closeByDocument: true,
			        closeByEscape: true,
			        className: 'ngdialog-theme-plain'
				});				
			};
		} ]);
