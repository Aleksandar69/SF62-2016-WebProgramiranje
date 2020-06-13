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
			
			dugmicitamoneki = "<span class='pogledajMovie' data-movieID='"+film.ID+"'></span>"
			
			tr.innerHTML = "<td class='movie_name' data-filmid='"+film.ID+"'>"+film.Naziv+"</td><td>"+film.Trajanje+"</td><td>"+film.Zanrovi+"</td><td>"+film.Godina_Proizvodnje+"</td><td>"+film.Distributer+"</td><td>"+film.Zemlja_Porekla+"</td><td>"+dugmicitamoneki+"</td>";
			tabelaFilm.appendChild(tr);	
		}
		$(".pogledajMovie").on("click", function(){
			let id= this.getAttribute("data-movieID");
			if(id>0 && id!=null && id!=undefined){
				window.location.href="prikazFilma.html?id="+id;
			}
		})
	}
});