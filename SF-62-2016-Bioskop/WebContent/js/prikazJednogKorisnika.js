var url_string = window.location.href;
var url = new URL(url_string);
var id1 = url.searchParams.get("id");
var params = {
		action: "ucitajKorisnika",
		id: id1
	}
$.post('KorisnikServlet', params, function(data) { 
	var res = JSON.parse(data);
	if(res.status){
		if(localStorage['id1']!=id1 && localStorage['uloga']!=['Admin']){
			localStorage['poruka']="red|Ne mozete da vidite sadrzaj na toj stranici.";
			window.location.href="index.html";
		}
		$("#user_username").text(res.korisnik.Username);
		if(res.korisnik.Username==localStorage['username']){
			$("#user_pass").val(res.korisnik.Password);
		}
		else{
			$("#user_pass").prop('disabled',true);
			$("#user_pass").prop("readonly",true);
		}
		$("#user_registracija").text(res.korisnik.Datum);
		$("#user_ulogaselect").val(res.korisnik.Uloga);
		if(localStorage['uloga']!="Admin"){
			$("#user_ulogaselect").prop('disabled',true);
		}
		$("#user_status").text(res.korisnik.Status);
		if(localStorage['uloga']=="Admin" && res.korisnik.Status=="Active"){
			var dugme = document.createElement('button');
			dugme.className="redbtn margin0";
			dugme.innerText="Obrisi Korisnika";
			dugme.setAttribute("ID","deleteUser");
			dugme.setAttribute("data-IDUsera",res.korisnik.ID)
			document.getElementById('dugmici').appendChild(dugme);
			
			$("#deleteUser").on('click',function(){
				if(confirm("Da li sigurno zelite da obrisete ovog korisnika?")){
					var params = {
					    'action' : "deleteUser",
					    'idKorisnika' : this.getAttribute('data-IDUsera')
					}
					$.post('KorisnikServlet',params,function(data){
					    var res = JSON.parse(data);
							if(res.status){
								pushNotification('green',res.message);
							}
							else{
								pushNotification('red',res.message);
							}
					});
				}
			})

		}
	}
	else{
		window.location.href="index.html";
	}
});

$("#saveUser").on('click',function(){
	var novasifra = $("#user_pass").val();
	var novaUloga = $("#user_ulogaselect").val();

	var params  = {
			"action" : "editUser",
			"promenjenaSifra" : false,
			"novasifra" : novasifra,
			"promenjenaUloga" : false,
			"novaUloga" : novaUloga,
			"idKorisnika" : id1
		}
	$.post('KorisnikServlet',params,function(data){
		var res= JSON.parse(data);
		if(res.promenjenaSifra){
			pushNotification("green","Uspesno ste promenili sifru");
		}
		if(res.promenjenaUloga){
			pushNotification('green',"Uspesno ste promenili ulogu");
		}
		if(!res.promenjenaSifra && !res.promenjenaUloga){
			pushNotification('red',"Nije bilo moguce promeniti atribute korisnika.");
		}
	})
})


