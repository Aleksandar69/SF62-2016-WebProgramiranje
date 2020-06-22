var url_string = window.location.href;
var url = new URL(url_string);
var id1 = url.searchParams.get("id");

var params = {
    'action' : 'ucitajKarteKorisnika',
    'id' : localStorage['kartaKorisnik']
}

	$.post('KarteServlet',params,function(data){
	    var res = JSON.parse(data);
			if(res.status){
	      var stanje = false;
	      for(i=0;i<res.karte.length;i++){
	        var k = res.karte[i];

	        if(id1==k.ID+""){
	          stanje = true;
	          $("#film_title").text(k.nazivFilma);
	          $("#tipProjekcije").text(k.tipProjekcije);
	          $("#termin").text(k.Termin);
	          $("#sala").text(k.sala);
	          $("#cenaKarte").text(k.cena);
	          $("#Sediste").text(k.sediste);

	          if(localStorage['uloga']!="Admin"){
	            $("#deletebtn").remove();
	          }
	        }
	      }
	      if(!stanje){
	        localStorage['poruka']="red|Desila se greska2, molimo Vas da pokusate kasnije";
	  			window.location.href="index.html";
	      }
			}
			else{
				localStorage['poruka']="red|Desila se greska, molimo Vas da pokusate kasnije";
				window.location.href="index.html";
			}
	});
