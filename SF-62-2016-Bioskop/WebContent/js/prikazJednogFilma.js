const url_string = window.location.href;
const url = new URL(url_string);
let id = url.searchParams.get("id");

ucitajFilm(id);

function ucitajFilm(idFilma){
	var params = {
			action:"ucitajFilm",
			filmID : idFilma
		}
	
	$.post("FilmoviServlet", params, function(data){
		let res = JSON.parse(data);
		if(res.status){
			var f = res.film;
			$("#f_title").text(f.Naziv);
			$("#f_reziser").text(f.Reziser);
			$("#f_glumci").text(f.Glumci.join(","));
			$("#f_trajanje").text(f.Trajanje);
			$("#f_zanrovi").text(f.Zanrovi.join(","));
			$("#f_distributer").text(f.Distributer);
			$("#f_zemlja").text(f.Zemlja_Porekla);
			$("#f_god").text(f.Godina_Proizvodnje);
			$("#f_opis").val(f.Opis);
			if(localStorage['status']!="false" && res.imaKarata){
				var dugme = document.createElement('button');
				dugme.className="confirmbtn";
				dugme.innerText = "Kupi kartu";
				dugme.setAttribute('data-IDFilma',res.film.ID);
				dugme.setAttribute('ID',"kupiKartubtn");
				dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
				document.getElementById('dugmici').appendChild(dugme);

				$("#kupiKartubtn").on('click',function(){
					localStorage['projekcija_film_id'] = $("#kupiKartubtn").attr('data-IDFilma');
					window.location.href="projekcije.html";
				})
			}
			if(localStorage['uloga']=="Admin"){
				var dugme = document.createElement('button');
				dugme.className="orangebutton";
				dugme.innerText = "Izmeni";
				dugme.setAttribute('data-IDFilma',res.film.ID);
				dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
				dugme.onclick=function(){window.location.href="dodajIzmijeniFilm.html?id="+res.film.ID}
				document.getElementById('dugmici').appendChild(dugme);

				dugme = document.createElement('button');
				dugme.className="redbtn";
				dugme.innerText = "Obrisi";
				dugme.setAttribute('data-IDFilma',res.film.ID);
				dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
				document.getElementById('dugmici').appendChild(dugme);
				
				$(".redbtn").on("click",function(){
					if(confirm("Da li ste sigurni da zelite da obrisete?")){
						var params = {
								action: "obrisiFilm",
								filmID: this.getAttribute('data-IDFilma')
							}
						$.post('FilmoviServlet', params, function(data) { 
								var res = JSON.parse(data);
								if(res.status){
									window.location.href="projekcije.html";
								}
								else{
									pushNotification('red',"Greska prilikom brisanja");
								}

						});
					}
				});
			}
			
		}

});
}
