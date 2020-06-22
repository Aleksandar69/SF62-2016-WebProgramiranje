var url_string = window.location.href;
var url = new URL(url_string);
var id = url.searchParams.get("id");

var params = {
	    'action' : 'kupiKartuInfo',
	    'idProjekcije' : id
	}


$.post('KarteServlet',params,function(data){
    var odg = JSON.parse(data);
    if(odg.status){
    	 var k = odg.info;
         $("#film_title").text(k.nazivFilma);
         $("#tipProjekcije").text(k.tipProjekcije);
         $("#termin").text(k.termin);
         $("#sala").text(k.nazivSale);
         $("#cenaKarte").text(k.cenaKarte);
         if(k.slobodnaSedista.length<=0){
           localStorage['poruka']="red|Nema slobodnih sedista za tu projekciju";
           window.location.href="projekcije.html";
         }
         for(i=0;i<k.slobodnaSedista.length;i++){
           document.getElementById('sedista').value=k.slobodnaSedista[0];
           var o = document.createElement('option');
           o.setAttribute('value',k.slobodnaSedista[i]);
           o.innerText= k.slobodnaSedista[i];
           document.getElementById('sedista').appendChild(o);
         }
         $("#ukupnaCena").text($("#sedista").val().length*parseInt(k.cenaKarte));
         $("#sedista").on('change',function(){
           $("#ukupnaCena").text($("#sedista").val().length*parseInt(k.cenaKarte));
         });
       }
       else{
         localStorage['poruka']="red|"+odg.message;
         window.location.href="projekcije.html";
       }
   });

$("#kupibtn").on('click',function(){
	  var sedista = $("#sedista").val();
	  if(sedista.length==0){
	    pushNotification('red',"Morate da odaberete barem jedno sediste");
	  }
	  
	  else{
		    var status = true;
		    for(i=0;i<sedista.length;i++){
		      if(sedista[i+1]){
		        if(sedista[i]+1!=sedista[i+1] && sedista[i]!=sedista[i+1]-1){
		          status=false;
		        }
		      }
		    }
		    if(status){
		      var ajdi = id;
		      var odabrana_sedista = sedista.join(";");
		      var params = {
		        'action' : 'kupiKartu',
		        'id' : ajdi,
		        'odabrana_sedista' : odabrana_sedista
		      }
		      $.post('KarteServlet',params,function(data){
		        var odg = JSON.parse(data);
		        if(odg.status){
		          localStorage['poruka'] = "green|"+odg.message;
		          window.location.href="projekcije.html";
		        }
		        else{
		          pushNotification('red',odg.message);
		        }
		      });
		    }
		    else{
		      pushNotification('red','Sedista moraju biti jedno do drugog');
		    }
		  }
		});
