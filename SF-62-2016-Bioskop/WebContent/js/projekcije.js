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
      // if(localStorage['uloga']=="Admin"){
      //   var btn2 = document.createElement('button'); btn2.innerText = "Izmeni"; btn2.className="orangebutton izmeniprojbtn";btn2.setAttribute('data-idProjekcije',p.id_projekcije); td6.appendChild(btn2);
      //   var btn3 = document.createElement('button'); btn3.innerText = "Obrisi"; btn3.className="redbtn obrisiprojbtn"; btn3.setAttribute('data-idProjekcije',p.id_projekcije); td6.appendChild(btn3);
      // }
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
