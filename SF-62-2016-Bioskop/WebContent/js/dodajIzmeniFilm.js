setInterval(function(){
	if(localStorage['uloga']!="Admin"){
		window.location.href="projekcije.html";
	}
},100);

$("#OnefilmDiv .row span").each(function(){
  this.style.margin="0 auto";
})

var params = {
		action: "uzmiZanrove"
	}

	$.post('FilmoviServlet', params, function(data) { 
		var res = JSON.parse(data);
		if(res.status){
			for(i=0;i<res.zanrovi.length;i++){
				if(res.zanrovi[i]!="Prazan"){
          var op = document.createElement('option');
  				op.value=res.zanrovi[i];
  				op.innerText = res.zanrovi[i];
  				document.getElementById('f_zanrovi').append(op);
        }
			}
		}

});


var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
if(id!=null){
  ucitajFilm(id);
}

function ucitajFilm(idFilma){
	var params = {
			action: "ucitajFilm",
			filmID: idFilma
		}
		$.post('FilmoviServlet', params, function(data) { 
			var res = JSON.parse(data);
			if(res.status){
				var f = res.film;
				$("#f_title").val(f.Naziv);
				$("#f_reziser").val(f.Reziser);
				$("#f_glumci").val(f.Glumci.join(";"));
				$("#f_trajanje").val(f.Trajanje);
				$("#f_zanrovi").val(f.Zanrovi);
				$("#f_distributer").val(f.Distributer);
				$("#f_zemlja").val(f.Zemlja_Porekla);
				$("#f_god").val(f.Godina_Proizvodnje);
				$("#f_opis").val(f.Opis);
				$("#deletebtn").show();
			}

	});
}



$("#savebtn").on('click',function(){
  var url_string = window.location.href;
  var url = new URL(url_string);
  var id = url.searchParams.get("id");
  
  var godina = $("#f_god").val();
 
  var title = $("#f_title").val();
  var duration = $("#f_trajanje").val();
  var zanr = $("#f_zanrovi").val().join(";");
  var opis = $("#f_opis").val();
  var glumci = $("#f_glumci").val();
  var reziser = $("#f_reziser").val();
  var godina = $("#f_god").val();
  var distributer = $("#f_distributer").val();
  var zemlja = $("#f_zemlja").val();
  
  if(title.length == 0 || duration.length == 0 || zanr.length == 0 || 
		  opis.length == 0 || glumci.length == 0 || reziser.lengt == 0 || godina.length == 0 || distributer.length == 0 ||
		  zemlja.length == 0){
	  alert("Niste unijeli sva polja!");
	  return;
  }
  
  
  if(id==null){
    var params = {
      'action' : 'dodajFilm',
      'naziv' : title,
      'trajanje' : duration,
      'zanr' : zanr,
      'opis' : opis,
      'glumci' : glumci,
      'reziser' : reziser,
      'godina' : godina,
      'distributer' : distributer,
      'zemlja' : zemlja
    }
    $.post('FilmoviServlet',params,function(data){
      var res = JSON.parse(data);
      if(res.status){
        localStorage['poruka']="green|Uspesno ste dodali film!";
        window.location.href="filmovi.html";
      }
      else{
        pushNotification('red','Nije bilo moguce dodati taj film. Molimo Vas da proverite unos.');
      }
    });
  }
	else{
		var params = {
      'action' : 'izmeniFilm',
			'id' : id,
		      'naziv' : title,
		      'trajanje' : duration,
		      'zanr' : zanr,
		      'opis' : opis,
		      'glumci' : glumci,
		      'reziser' : reziser,
		      'godina' : godina,
		      'distributer' : distributer,
		      'zemlja' : zemlja
    }
    $.post('FilmoviServlet',params,function(data){
      var res = JSON.parse(data);
      if(res.status){
        localStorage['poruka']="green|Uspesno ste izmenili film!";
        window.location.href="filmovi.html";
      }
      else{
        pushNotification('red','Nije bilo moguce dodati taj film. Molimo Vas da proverite unos.');
      }
    });
	}
});
