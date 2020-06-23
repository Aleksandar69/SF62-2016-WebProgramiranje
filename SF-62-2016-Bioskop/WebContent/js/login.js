$("#loginbutton").on("click", function(){
	let user = $("#input_username").val();
	let pass = $("#input_password").val();

	if(user.length > 0 && pass.length > 0){

		var params = {
				action: "login",
				username : user,
				password : pass
		}
	
		$.post("KorisnikServlet", params, function(data){
			let res = JSON.parse(data);
			if(res.status){
				window.location.href="projekcije.html";
			}
			else{
				pushNotification("red", res.message);
			}
		});
	}
	else{
		alert("Provjerite podatke!");
	}
})