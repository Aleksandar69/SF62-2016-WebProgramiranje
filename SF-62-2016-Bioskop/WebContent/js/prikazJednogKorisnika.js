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
			window.location.href="projekcije.html";
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
		window.location.href="projekcije.html";
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
});

var params = {
	    'action' : 'ucitajKarteKorisnika',
	    'id' : id1
	}
	$.post('KarteServlet',params,function(data){
	    var res = JSON.parse(data);

			if(res.status){
				for(i=0;i<res.karte.length;i++){
					var k = res.karte[i];
					var tr = document.createElement('tr');
					var td1= document.createElement('td');
					td1.innerText=k.nazivFilma;
					td1.setAttribute('data-idFilma',k.ID_filma);
					td1.className="ticket_filmName";
					tr.appendChild(td1);

					var td2= document.createElement('td');
					td2.innerText=k.sediste;
					tr.appendChild(td2);

					var td3= document.createElement('td');
					td3.innerText=k.Termin;
					tr.appendChild(td3);

					var td4= document.createElement('td');
					td4.innerHTML="<button class='pogledajbtn confirmbtn' data-idKarte='"+k.ID+"' data-idKorisnika='"+id1+"'>Pogledaj Kartu</button>";
					td4.className="ticket_button";
					tr.appendChild(td4);

					document.getElementById('karteTable').appendChild(tr);
				}
				$(".pogledajbtn").on('click',function(){
					localStorage['kartaKorisnik']=this.getAttribute('data-idKorisnika');
					window.location.href="karta.html?id="+this.getAttribute('data-idKarte');
				})
			}
			else{
				localStorage['poruka']="red|Desila se greska, molimo Vas da pokusate kasnije";

			}
			if(res.karte.length==0){
				document.getElementById('karteTable').style.display="none";
				localStorage.removeItem('poruka');
				pushNotification("red","Ne postoje karte za tog korisnika.");
			}
	});

