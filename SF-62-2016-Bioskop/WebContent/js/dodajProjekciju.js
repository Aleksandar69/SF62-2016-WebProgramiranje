setInterval(function(){
	if(localStorage['uloga']!="Admin"){
		window.location.href="projekcije.html";
	}
},100);

var params = {
	    'action' : "ucitajZaDodavanjeProjekcije"
	}

$.post('ProjekcijeServlet',params,function(data){
    var odg = JSON.parse(data);
    console.log(odg);
    for(i=0;i<odg.filmovi.length;i++){
      var o = document.createElement('option');
      o.setAttribute('value',odg.filmovi[i].ID);
      o.setAttribute('duzinaFilma',odg.filmovi[i].Trajanje);
      o.innerText = odg.filmovi[i].Naziv +" ("+odg.filmovi[i].Trajanje+" minuta)";
      document.getElementById('ID_Filma').appendChild(o);
    }
    var o = document.createElement('option');
    o.setAttribute('value',null);
    o.setAttribute('tipovi',null);
    o.setAttribute('maxSedista',null);
    document.getElementById('ID_Sale').appendChild(o);

    for(i=0;i<odg.sale.length;i++){
      var o = document.createElement('option');
      o.setAttribute('value',odg.sale[i].ID);
      o.setAttribute('tipovi',JSON.stringify(odg.sale[i].listaTipova));
      o.setAttribute('maxSedista',odg.sale[i].MaksimumSedista);
      o.innerText = odg.sale[i].Naziv;
      document.getElementById('ID_Sale').appendChild(o);
    }
});

$("#ID_Sale").on('change',function(data){
	  var vrednost = $("#ID_Sale").val();
	  var tipovi = $('#ID_Sale').find(":selected").attr("tipovi");
	  var sedista = $('#ID_Sale').find(":selected").attr("maxSedista");
	  document.getElementById('maxBrojSedistaSpan').innerText="0";
	  $("#tipProjekcije").html("");
	  if(vrednost!=null && vrednost!=undefined && vrednost!="null"){
	    document.getElementById('maxBrojSedistaSpan').innerText=sedista;
	    tipovi = JSON.parse(tipovi);
	    for(i=0;i<tipovi.length;i++){
	      var tip = tipovi[i];
	      var o = document.createElement('option');
	      o.setAttribute('value',tip.ID);
	      o.innerText = tip.Naziv;
	      document.getElementById('tipProjekcije').appendChild(o);
	    }
	  }
	})

	
$("#savebtn").on('click',function(){
  var pocetak = $("#TerminPocetak").val();
  pocetak = pocetak.split("T").join(" ");
  var ID_Filma = $("#ID_Filma :selected").val();
  var ID_Sale = $("#ID_Sale").val();
  var ID_Tipa = $("#tipProjekcije").val();
  var cenaKarte = $("#cenaKarte").val();
  
  var proveraUnosa  = true;
  var msgunos = "Unexpected error";
  if(parseInt(cenaKarte)<=0 || cenaKarte==null || cenaKarte==undefined || cenaKarte=="" || ID_Filma==null || ID_Filma==undefined 
		  || ID_Sale==null || ID_Sale==undefined || ID_Tipa==null || ID_Tipa==undefined || pocetak ==null || pocetak.length==0 || pocetak==undefined){
    proveraUnosa = false;
    msgunos = "Niste popunili sva polja.";

  }
  
  if(proveraUnosa){
	    var params = {
	      'action' : "dodajProjekciju",
	      'pocetakTermina' : pocetak,
	      'cenaKarte' : cenaKarte,
	      'ID_Sale' : ID_Sale,
	      'ID_Tipa' : ID_Tipa,
	      'ID_Filma' : ID_Filma
	    }
	    $.post('ProjekcijeServlet',params,function(data){
	        var odg = JSON.parse(data);
	        if(odg.status){
	          localStorage['poruka']="green|Uspesno ste dodali projekciju!";
	          window.location.href="projekcije.html";
	        }
	        else{
	          pushNotification('red',odg.message);
	        }
	    });
	  }
	  else{
	    pushNotification('red',msgunos);
	  }
	})