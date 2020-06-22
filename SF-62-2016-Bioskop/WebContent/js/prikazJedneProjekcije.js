var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");
var params = {
    'action' : "ucitajProjekciju",
    'idProjekcije' : id
}
$.post('ProjekcijeServlet',params,function(data){
    var odg = JSON.parse(data);
      $("#p_title").html("<a href='prikazFilma.html?id="+odg.projekcija.idFilma+"''>"+odg.projekcija.nazivFilma+"</a>");
      $("#p_tipProjekcije").text(odg.projekcija.tipProjekcije);
      $("#p_sala").text(odg.projekcija.nazivSale);
      $("#p_pocetak").text(odg.projekcija.termin);
      $("#p_cena").text(odg.projekcija.cenaKarte);
      $("#p_brojSlobodnih").text(odg.projekcija.brojKarata);
      $("#statusProj").text(odg.projekcija.status);

      if(odg.projekcija.brojKarata>0){
          var dugme = document.createElement('button');
          dugme.className="confirmbtn";
          dugme.innerText = "Kupi kartu";
          dugme.setAttribute('data-IDProjekcije',odg.projekcija.id);
          dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
          dugme.setAttribute('ID','kupitbtn');
          document.getElementById('dugmici').appendChild(dugme);
        }
      
      if(odg.projekcija.status=="Deleted"){
          $("#kupitbtn").remove();
        }
      
      if(localStorage['uloga']=="Admin" && odg.projekcija.status=="Active"){
          dugme = document.createElement('button');
          dugme.className="redbtn";
          dugme.innerText = "Obrisi";
          dugme.setAttribute('data-IDProjekcije',odg.projekcija.id);
          dugme.setAttribute('ID',"obrisibtn")
          dugme.style="font-size: 18px;margin: 0 auto;margin-top:10px;";
          document.getElementById('dugmici').appendChild(dugme);
        }
      if(Object.keys(odg).length==0){
          window.location.href="projekcije.html";
        }
      
      $("#kupitbtn").on('click',function(data){
          if(localStorage['status']=="false"){
            localStorage['poruka']="red|Ulogujte se da bi ste kupili kartu!";
            window.location.href="projekcije.html";
          }
          else{
            window.location.href="kupiKartu.html?id="+ this.getAttribute('data-IDProjekcije');
          }
        });
      
      $("#obrisibtn").on('click',function(){
          if(confirm("Da li ste sigurni da zelite da obrisete projekciju?")){

      		var params = {
					'action': "obrisiProj",
					'idProjekcije': id
				}
			$.post('ProjekcijeServlet', params, function(data) { 
					var res = JSON.parse(data);
					if(res.status){
						pushNotification('green', res.message);
						window.location.href="projekcije.html";
					}
					else{
						pushNotification('red', res.message);
					}

			});
      		
          }
        });
      
      var dateProjekcije = new Date(odg.projekcija.termin);
      var sada = new Date();
      if(sada>dateProjekcije){
        $("#statusProj").text("Pocelo/Zavrseno");
        $("#kupitbtn").remove();
        $("#p_brojSlobodnih").text("0");
      }

});