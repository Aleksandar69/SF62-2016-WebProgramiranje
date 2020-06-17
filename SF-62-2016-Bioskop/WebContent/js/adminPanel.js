var params = {
		action: "ucitajSve"
	}

$.post('KorisnikServlet', params, function(data) { 
	var res = JSON.parse(data);
	if(res.status){
		var t = document.getElementById('Korisnici');

		for(i=0;i<res.lista.length;i++){
			var k = res.lista[i];
			var r = document.createElement('tr');
			r.className="item";
			r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td><td>"+k.Status+"</td>";
			t.appendChild(r);
		}
		$('.usernamelink').on('click',function(){
			window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
		});

	}
});

setInterval(function(){
	if(localStorage['uloga']!="Admin"){
		window.location.href="index.html";
	}
},100);

