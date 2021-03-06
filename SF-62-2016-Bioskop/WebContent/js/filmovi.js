var params = {
		action : "ucitajFilmove",
		filmId: "1"
}




$.post("FilmoviServlet", params, function(data){
	
	let res = JSON.parse(data);
	if(res.status){
		for(i=0; i<res.filmovi.length; i++){
			let film = res.filmovi[i];
			let tabelaFilm = document.getElementById('tabelaFilm');
			let tr = document.createElement("tr");
			tr.className = "item";
			tr.setAttribute("data-filmID", film.ID);
			
			if(localStorage['uloga']=="Admin"){
				dugmicitamoneki = "<span class='editMovie' data-movieID='"+film.ID+"'></span>" +
						"<span class='deleteMovie' data-movieID='"+film.ID+"'></span>";
			}
			else{
				dugmicitamoneki = "<span class='pogledajMovie' data-movieID='"+film.ID+"'></span>"
			}
			tr.innerHTML = "<td class='movie_name' data-filmid='"+film.ID+"'>"+film.Naziv+"</td><td>"+film.Trajanje+"</td><td>"+film.Zanrovi+"</td><td>"+film.Godina_Proizvodnje+"</td><td>"+film.Distributer+"</td><td>"+film.Zemlja_Porekla+"</td><td>"+dugmicitamoneki+"</td>";
			tabelaFilm.appendChild(tr);		
		}
		
		
		
		$(".pogledajMovie").on("click", function(){
			let id= this.getAttribute("data-movieID");
			if(id>0 && id!=null && id!=undefined){
				window.location.href="prikazFilma.html?id="+id;
			}
		});
		$(".movie_name").on("click", function(){
			var id = this.getAttribute('data-filmid');
			if(id>0 && id!=null && id!=undefined){
				window.location.href="prikazFilma.html?id="+id;
			}
		});
		/*$(".addMovie").on("click", function(){
			window.location.href="dodajIzmijeniFilm.html"
		});*/
		$(".deleteMovie").on("click",function(){
			if(confirm("Da li ste sigurni da zelite da obrisete?")){
				var params = {
						action: "obrisiFilm",
						filmID: this.getAttribute('data-movieID')
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
		$(".editMovie").on('click',function(){
			window.location.href="dodajIzmijeniFilm.html?id=" +this.getAttribute('data-movieID');;
		});
		
	}
});


var params = {
		action: "uzmiZanrove"
	}
	$.post('FilmoviServlet', params, function(data) {
		let res = JSON.parse(data);
		if(res.status){
			
			var divDugmici = document.getElementById("dugmiciFilm");
			var div = document.createElement("div");
			var button = document.createElement("BUTTON");
			button.className = "confirmbtn";
			button.textContent = "Dodaj";
			
			if(localStorage['uloga'] == 'Admin'){
				divDugmici.appendChild(div);
				div.appendChild(button)
				
				button.onclick = function(){
					window.location.href="dodajIzmijeniFilm.html";
				};
			}
			
			for(i=0;i<res.zanrovi.length;i++){
				let op = document.createElement('option');
				op.value=res.zanrovi[i];
				op.innerText = res.zanrovi[i];
				document.getElementById('f_zanrovi').append(op);
			}
		}

});

$("#filterBtnFilm").on("click", function(){
	
	
	var naziv_polje = $("#f_naziv").val();
	var trajanje_polje = $("#f_trajanje").val();
	var zanr_polje = $("#f_zanrovi").val();
	var godina_polje = $("#f_godina").val();
	var distributer_polje = $("#f_distributer").val();
	var zemlja_polje = $("#f_zemlja").val();
	

	
	zanr_polje.sort();
	zanr_polje = zanr_polje.join(";");
	

	
	var params = {
			action : "filterFilm",
			naziv : naziv_polje,
			trajanje : trajanje_polje,
			zanr : zanr_polje,
			opis : "",
			glumci : "",
			reziser : "",
			godina : godina_polje,
			distributer : distributer_polje,
			zemlja : zemlja_polje
	}

	
	$.post("FilmoviServlet", params, function(data){
		var res = JSON.parse(data);
		if(res.status){
			if(res.filmovi.length>0){
				$('tr').slice(2).remove();
				for(i=0;i<res.filmovi.length;i++){
				let film = res.filmovi[i];
				var tabela = document.getElementById('tabelaFilm');
				var tr = document.createElement('tr');
				tr.className="item";
				tr.setAttribute("data-FilmID", film.ID);
				if(localStorage['uloga']=="Admin"){
					dugmicitamoneki = "<span class='editMovie' data-movieID='"+film.ID+"'></span>" +
							"<span class='deleteMovie' data-movieID='"+film.ID+"'></span>";
									
				}
				else{
					dugmicitamoneki = "<span class='pogledajMovie' data-movieID='"+film.ID+"'></span>"
				}
				tr.innerHTML = "<td class='movie_name' data-filmid='"+film.ID+"'>"+film.Naziv+"</td><td>"+film.Trajanje+"</td><td>"+film.Zanrovi+"</td><td>"+film.Godina_Proizvodnje+"</td><td>"+film.Distributer+"</td><td>"+film.Zemlja_Porekla+"</td><td>"+dugmicitamoneki+"</td>";
				tabela.appendChild(tr);	
				}
				}
			$(".deleteMovie").on("click",function(){
				if(confirm("Da li ste sigurni da zelite da obrisete?")){
					var params = {
							action: "obrisiFilm",
							filmID: this.getAttribute('data-movieID')
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
			$(".editMovie").on('click',function(){
				window.location.href="dodajIzmijeniFilm.html?id=" +this.getAttribute('data-movieID');;
			});
			
			$(".pogledajMovie").on("click", function(){
				let id= this.getAttribute("data-movieID");
				if(id>0 && id!=null && id!=undefined){
					window.location.href="prikazFilma.html?id="+id;
				}
			});
			$(".movie_name").on("click", function(){
				var id = this.getAttribute('data-filmid');
				if(id>0 && id!=null && id!=undefined){
					window.location.href="prikazFilma.html?id="+id;
					//http://localhost:8080/SF-62-2016-Bioskop/
				}
			});
		}
		else{
			pushNotification('red',"Ne postoji film sa zadatim kriterijumom.");
		}

	});
});