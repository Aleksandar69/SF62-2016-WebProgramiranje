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
			let f = res.film;
			$("#f_title").text(f.Naziv);
			$("#f_reziser").text(f.Reziser);
			$("#f_glumci").text(f.Glumci.join(","));
			$("#f_trajanje").text(f.Trajanje);
			$("#f_zanrovi").text(f.Zanrovi.join(","));
			$("#f_distributer").text(f.Distributer);
			$("#f_zemlja").text(f.Zemlja_Porijekla);
			$("#f_god").text(f.Godina_Proizvodnje);
			$("#f_opis").val(f.Opis);

		}
		
	});
}