var params = {
		'action' : "getSessionInfo"
}

$.post('KorisnikServlet',params,function(data){
    var res = JSON.parse(data);
    localStorage['uloga']= res.uloga;
    localStorage['status'] = res.status;
    localStorage['username'] = res.username;
    localStorage['id1']=res.id1;


    if(res.status && res.uloga=="Korisnik" && res.username!=null){
        $("#nav_filmovi").show();
        $("#nav_repertoar").show();
        $("#nav_profil").show();
        $("#nav_logout").show();
      }
    
    if(res.status && res.uloga=="Admin" && res.username!=null){
        $(".nav-opcija").each(function(){this.style.display="inline"});
        $("#nav_login").hide();
      }
    if(!res.status){
        $("#nav_filmovi").show();
        $("#nav_repertoar").show();
        $("#nav_login").show();
        localStorage['uloga']= null;
        localStorage['status'] = false;
        localStorage['username'] = null;
        localStorage['id1']=null;
      }
    if(res.status){
        document.getElementById('nav_profil').href="prikazKorisnika.html?id="+res.id1;
      }
   });

if(localStorage['poruka']!=undefined || localStorage['poruka']!=null ){
	  var poruka = localStorage['poruka'].split("|");
	  var boja = poruka[0];
	  var msg = poruka[1];
	  pushNotification(boja,msg);
	  localStorage.removeItem('poruka');
	}
    