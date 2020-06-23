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
		window.location.href="projekcije.html";
	}
},100);

$("#filterUsers").on('click',function(){
	var params = {
    'action' : "filter",
    'username' : $("#f_username").val(),
    'password' : '',
    'datum' : '',
    'tip' : $("#Uloge").val()
}
	
	$.post('KorisnikServlet',params,function(data){
		var odg = JSON.parse(data);
		
		if(odg.status){
			pushNotification('green','Filtrirano!');
			var t = document.getElementById('Korisnici');
			$("#Korisnici").find("tr:gt(1)").remove();
			for(i=0;i<odg.lista.length;i++){
				var k = odg.lista[i];
				var r = document.createElement('tr');
				r.className="item";
				r.innerHTML="<td data-id='"+k.ID+"' class='usernamelink'>"+k.Username+"</td><td>"+k.Datum+"</td><td>"+k.Uloga+"</td><td>"+k.Status+"</td>";
				t.appendChild(r);
			}
			$('.usernamelink').on('click',function(){
				window.location.href="prikazKorisnika.html?id="+this.getAttribute('data-id');
			});
		}
		else{
			pushNotification('red','Ne postoje korisnici sa tim kriterijumima.');
		}
	});
	});

$(document).ready(function(){
	$("#izvestajbtn").on('click',function(){
		var vreme = $("#vreme").val();
		if(vreme.length==10){
				var params = {
		        'action' : 'izvestaj',
		        'datum' : vreme
		      }
		      $.post('AdminServlet',params,function(data){
						$("#izvestajTabela").find("tr:gt(1)").remove();
		        var odg = JSON.parse(data);
						for(i=0;i<odg.length;i++){
							var f = odg[i];
							var tr = document.createElement('tr');
							tr.className="item";

							var td1 = document.createElement('td');
							td1.innerText = f.Naziv;
							td1.setAttribute('data-IDFilma',f.ID);
							td1.className="izvestajlink";

							tr.appendChild(td1);

							var td2 = document.createElement('td');
							td2.innerText=f.BrojProjekcija;
							tr.appendChild(td2);

							var td3 = document.createElement('td');
							td3.innerText = f.brojProdatihKarata;
							tr.appendChild(td3);

							var td4 = document.createElement('td');
							td4.innerText = f.zarada;
							tr.appendChild(td4);



							document.getElementById('izvestajTabela').appendChild(tr);
						}
						$(".izvestajlink").on('click',function(){
							window.location.href="prikazFilma.html?id="+this.getAttribute('data-IDFilma');
						})
		      });
			};
		});

})
