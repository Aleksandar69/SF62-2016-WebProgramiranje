var params = {
  'action': "ucitajProjekcijeZaDanas"
}
$.post('ProjekcijeServlet',params,function(data){
  var odg = JSON.parse(data);
  if(odg.status){
    for(i=0;i<odg.listaProjekcija.length;i++){
      var p = odg.listaProjekcija[i];
      var tr = document.createElement('tr');
      tr.setAttribute('data-idProjekcije',p.id_projekcije);
      tr.className="item";
      var td1 = document.createElement('td');
      td1.innerText = p.naziv_filma;
      td1.setAttribute('data-idFilma',p.id_filma);
      td1.className="proj_film_link";
      tr.appendChild(td1);

      var td2 = document.createElement('td');
      td2.innerText = p.terminProjekcije;
      tr.appendChild(td2);

      var td3 = document.createElement('td');
      td3.innerText = p.naziv_sale;
      tr.appendChild(td3);

      var td4 = document.createElement('td');
      td4.innerText = p.tip_projekcije;
      tr.appendChild(td4);

      var td5 = document.createElement('td');
      td5.innerText = p.cena;
      tr.appendChild(td5);


      var td6 = document.createElement('td');
      var btn1 = document.createElement('button'); btn1.innerText = "Pogledaj"; btn1.className="confirmbtn pogledajprojbtn"; btn1.setAttribute('data-idProjekcije',p.id_projekcije); td6.appendChild(btn1);

      tr.appendChild(td6);

      document.getElementById('tabelaProjekcija').appendChild(tr);

      $(".proj_film_link").on('click',function(data){
        window.location.href="prikazFilma.html?id="+this.getAttribute('data-idFilma');
      });
      $(".pogledajprojbtn").on('click',function(data){
        window.location.href="prikazProjekcije.html?id="+this.getAttribute('data-idProjekcije');
      })
    }
  }
  else{
    $("#tabelaProjekcija").hide();
    pushNotification('red','Za danasnji dan, nema projekcija.');
  }
})


var params = {
    'action' : 'ucitajProjFilterInfo'
}
$.post('ProjekcijeServlet',params,function(data){
    var odg = JSON.parse(data);
    if(odg.status){
      for(i=0;i<odg.filmovi.length;i++){
        var f = odg.filmovi[i];
        var o = document.createElement('option');
        o.setAttribute('data-IDFilma',f.ID);
        o.innerText= f.Naziv;
        document.getElementById('filter_Filmovi').appendChild(o);
      }
      for(i=0;i<odg.sale.length;i++){
        var s = odg.sale[i];
        var o = document.createElement('option');
        o.setAttribute('data-IDSale',s.ID);
        o.setAttribute('data-Tipovi',JSON.stringify(s.listaTipova));
        o.innerText= s.Naziv;
        document.getElementById('filter_Sale').appendChild(o);
      }
      $("#filter_Sale").on('change',function(){
        $("#filter_tipProjekcije").html("<option data-tipovi='[{\"ID\":\"null\",\"Naziv\":\"\"}]'></option>");
        var tips = JSON.parse($("#filter_Sale :selected").attr('data-tipovi'));
        for(i=0;i<tips.length;i++){

          var o = document.createElement('option');
          o.setAttribute('data-idtipa',tips[i].ID);
          o.setAttribute('value',tips[i].Naziv);
          o.innerText=tips[i].Naziv;
          document.getElementById('filter_tipProjekcije').appendChild(o);
        }
      })
      var x = localStorage['projekcija_film_id'];
      if(x!=null && x!=undefined && x!=""){
        localStorage.removeItem('projekcija_film_id');
        var opcije = document.getElementById('filter_Filmovi').getElementsByTagName('option');
        for(k=0;k<opcije.length;k++){
          if(opcije[k].getAttribute('data-idfilma')==x){
            opcije[k].selected = true;
            
            var now = new Date();
            now.setMinutes(now.getMinutes() - now.getTimezoneOffset());
            document.getElementById('f_pocetakOd').value = now.toISOString().slice(0,16);
            
          }
          else{
          }
        }
        document.getElementById('filterbtn').click();
      }
    }
    else{
      pushNotification('red',odg.message);
    }
});

$("#filterOpenBox").on('click',function(){
	  $(".filterBoxOption").each(function(data){
	    this.classList.toggle("hidden");
	  })
	})


$("#filterbtn").on('click',function(){
    var id_Filma = $("#filter_Filmovi :selected").attr('data-idfilma');
    var pocetak = $("#f_pocetakOd").val().split("T").join(" ");
    var pocetakKraj = $("#f_pocetakDo").val().split("T").join(" ");
    var idSale = $("#filter_Sale :selected").attr('data-IDSale');
    var oznakaTipa = $("#filter_tipProjekcije").val();
    var cenaMin = $("#f_cenaOd").val();
    var cenaMax = $("#f_cenaDo").val();

    var params = {
      'action' : 'filter',
      "idFilma" : id_Filma,
      "pocetak" : pocetak,
      "pocetakKraj" : pocetakKraj,
      "idSale" : idSale,
      "oznakaTipa" : oznakaTipa,
      "cenaMin" : cenaMin,
      "cenaMax" : cenaMax
    }
    $.post('ProjekcijeServlet',params,function(data){
      var odg = JSON.parse(data);
      if(odg.status){
    	  $("#tabelaProjekcija").show();
       $('tr').slice(1).remove();
        for(i=0;i<odg.lista.length;i++){
          var p = odg.lista[i];
          var tr = document.createElement('tr');
          tr.setAttribute('data-idProjekcije',p.ID);
          tr.className="item";
          var td1 = document.createElement('td');
          td1.innerText = p.Naziv_Filma;
          td1.setAttribute('data-idFilma',p.ID_Filma);
          td1.className="proj_film_link";
          tr.appendChild(td1);

          var td2 = document.createElement('td');
          td2.innerText = p.Termin;
          tr.appendChild(td2);

          var td3 = document.createElement('td');
          td3.innerText = p.Sala;
          tr.appendChild(td3);

          var td4 = document.createElement('td');
          td4.innerText = p.TipProjekcije;
          tr.appendChild(td4);

          var td5 = document.createElement('td');
          td5.innerText = p.Cena;
          tr.appendChild(td5);


          var td6 = document.createElement('td');
          var btn1 = document.createElement('button'); btn1.innerText = "Pogledaj"; btn1.className="confirmbtn pogledajprojbtn"; btn1.setAttribute('data-idProjekcije',p.ID); td6.appendChild(btn1);
          tr.appendChild(td6);
          document.getElementById('tabelaProjekcija').append(tr);
        }
        $(".pogledajprojbtn").on('click',function(data){
          window.location.href="prikazProjekcije.html?id="+this.getAttribute('data-idProjekcije');
        });
        $(".proj_film_link").on('click',function(data){
          window.location.href="prikazJednogFilma.html?id="+this.getAttribute('data-idFilma');
        });


      }
      else{
        pushNotification('red',odg.message);
      }
    })
});
