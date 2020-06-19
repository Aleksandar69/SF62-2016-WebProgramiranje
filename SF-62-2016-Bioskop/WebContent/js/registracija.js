$("#registracija").on('click',function(){
	var user = $("#r_username").val();
	var pass1 = $("#r_pass1").val();
	var pass2 = $("#r_pass2").val();

	if(user.length>0 && pass1==pass2 && pass1.length>0){
		var params = {
				action: "registracija",
				username : user,
				password : pass1
			}
			$.post('KorisnikServlet', params, function(data) { 
				var odg = JSON.parse(data);
				if(odg.status){
					window.location.href="projekcije.html";
				}
				else{
					pushNotification('red',odg.message);
				}

		});
	}
	else{
		pushNotification('red',"Proverite unos");
		
	}
});
