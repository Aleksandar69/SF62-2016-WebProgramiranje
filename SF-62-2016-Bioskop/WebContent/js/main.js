function pushNotification(type, message) {
	var color = "";
	if (type == "green") {
		color = 'sucnotif';
	}
	if (type == "red") {
		color = 'dangnotif';
	}
	var notif = document.createElement('div');
	notif.className = "notification sakrivennotif " + color;
	notif.innerText = message;

	document.getElementById('notificationdiv').append(notif);
	setTimeout(function() {
		notif.classList.toggle('sakrivennotif')
	}, 100);

	var t1 = setTimeout(function() {
		notif.classList.toggle('sakrivennotif');
		setTimeout(function() {
			notif.remove();
		}, 500);
	}, 15000);
	notif.onclick = function() {
		clearTimeout(t1);
		notif.classList.toggle('sakrivennotif');
		setTimeout(function() {
			notif.remove();
		}, 500);
	}
}
//

$(document).ready(function() {
	//
	if ($("#nav_logout")) {
		$("#nav_logout").on('click', function() {
			var params = {
				'action' : "logOut"
			}
			$.post('KorisnikServlet', params, function(data) {
				var odg = JSON.parse(data);
				console.log(odg);
				if (odg.status) {
					window.location.href = "login.html";
				} else {
					pushNotification('red', odg.message);
				}
			});
		})
	}
})
