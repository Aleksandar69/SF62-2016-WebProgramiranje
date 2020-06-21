package servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dao.FilmoviDAO;
import dao.ProjekcijeDAO;
import dao.SalaDAO;
import dao.TipProjekcijeDAO;
import model.Film;
import model.Projekcija;
import model.Sala;
import model.TipProjekcije;

/**
 * Servlet implementation class ProjekcijeServlet
 */
public class ProjekcijeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ProjekcijeDAO projekcijeDAO = null;
	FilmoviDAO filmoviDAO = null;
	SalaDAO salaDAO = null;
	TipProjekcijeDAO tipProjDAO = null;
	
	@Override
	public void init() throws ServletException {
		projekcijeDAO = new ProjekcijeDAO();
		filmoviDAO = new FilmoviDAO();
		salaDAO = new SalaDAO();
		tipProjDAO = new TipProjekcijeDAO();
	}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProjekcijeServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	private JSONObject ucitajDanasnjeProjekcije(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;

		ArrayList<JSONObject> projekcije = new ArrayList<JSONObject>();

		Film film = null;
		Sala sala = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date datum = new Date();
		String datumFormatiran = dateFormat.format(datum);

		ArrayList<Projekcija> listaProjekcija = projekcijeDAO.ucitajDanasnjeProjekcije(request, datumFormatiran);

		for (Projekcija p : listaProjekcija) {
			try {
				film = filmoviDAO.nadjiFilmPrekoIda(p.getFilm());
				sala = salaDAO.nadjiSaluPrekoId(p.getSala());
				JSONObject jsonObj = new JSONObject();

				if (film != null && sala != null) {
					jsonObj.put("id_projekcije", p.getId());
					jsonObj.put("id_filma", film.getID());
					jsonObj.put("naziv_filma", film.getNaziv());
					String datumProj = dateFormat1.format(p.getDatumiVrijemePrikazivanja());
					jsonObj.put("terminProjekcije", datumProj);
					jsonObj.put("tip_projekcije", p.getTipProjekcije());
					jsonObj.put("id_sale", sala.getId());
					jsonObj.put("naziv_sale", sala.getNaziv());
					jsonObj.put("cena", p.getCijenaKarte());

				}
				projekcije.add(jsonObj);
				status = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		res.put("status", status);
		res.put("listaProjekcija", projekcije);
		return res;

	}
	
	private JSONObject ucitajJednuProj(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		JSONObject projekcijaJSON = new JSONObject();
		
		boolean status = false;
		
		String message = "Greska";
		
		try {
			int id = Integer.valueOf(request.getParameter("idProjekcije"));
			Projekcija proj = projekcijeDAO.nadjiProjPoIdu(id);
			Sala sala = salaDAO.nadjiSaluPrekoId(Integer.valueOf(proj.getSala()));
			Film film = filmoviDAO.nadjiFilmPrekoIda(Integer.valueOf(proj.getFilm()));
			projekcijaJSON.put("id", proj.getId());
			projekcijaJSON.put("idFilma", film.getID());
			projekcijaJSON.put("nazivFilma", film.getNaziv());
			projekcijaJSON.put("tipProjekcije", proj.getTipProjekcije());
			projekcijaJSON.put("idSale", sala.getId());
			projekcijaJSON.put("nazivSale", sala.getId());
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String datum = dateFormat.format(proj.getDatumiVrijemePrikazivanja());
			projekcijaJSON.put("termin", datum);
			projekcijaJSON.put("cenaKarte", proj.getCijenaKarte());
			projekcijaJSON.put("status", proj.getStatus());
			int brojPreostalihKarata = proj.getMaksimumKarata() - proj.getProdaneKarte();
			projekcijaJSON.put("brojKarata", brojPreostalihKarata);
			message = "Projekcija ucitana";
			status = true;
			res.put("projekcija", projekcijaJSON);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		} 
		res.put("status",status);
		res.put("message", message);
		return res;
	}
	
	private JSONObject dodajProjekciju(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;
		
		String message = "Greska";
		String cenaKarte = request.getParameter("cenaKarte");
		String pocetakTer = request.getParameter("pocetakTermina");
		String idTip = request.getParameter("ID_Tipa");
		String idSala = request.getParameter("ID_Sale");
		String idFilm = request.getParameter("ID_Filma");
		String admin = (String) request.getSession().getAttribute("username");
		String ulogaAdmina = (String) request.getSession().getAttribute("uloga");
		
		try {
			Sala s = salaDAO.nadjiSaluPrekoId(Integer.valueOf(idSala));
			Film f = filmoviDAO.nadjiFilmPrekoIda(Integer.valueOf(idFilm));
			TipProjekcije tp = tipProjDAO.nadjiTipProjPoId(Integer.valueOf(idTip));
			
			boolean salaImaTip = false;
			for(TipProjekcije t : s.getTipProjekcije()) {
				if(t.getID() == tp.getID()) {
					salaImaTip = true;
				}
			}
			
			DateFormat formatDatum = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date pt = formatDatum.parse(pocetakTer);
			
			Calendar cal = Calendar.getInstance();
			Date datum = formatDatum.parse(pocetakTer);
			cal.setTime(datum);
			cal.add(Calendar.MINUTE, f.getTrajanje());
			datum = cal.getTime();
			String krajTermina = formatDatum.format(datum);
			
			//boolean zauzeto = salaDAO.slobodnaSalaZaTermin(idSala, pocetakTer, krajTermina);
			
			JSONObject provjeraSale = salaDAO.slobodnaSalaZaTermin(idSala, pocetakTer, krajTermina);
			
			boolean imaMjesta = (boolean) provjeraSale.get("status");
			message = (String) provjeraSale.get("message");
			status = imaMjesta;
			
			if(imaMjesta && f!=null && s!=null && salaImaTip) {
    			Projekcija projekcija = new Projekcija(1, Integer.valueOf(idFilm), tp.getNaziv(), Integer.valueOf(idSala), pt, Integer.valueOf(cenaKarte), admin, "Active", salaDAO.brMaksimalnoSjedista(idSala), 0);
    			status = projekcijeDAO.dodajProjekciju(projekcija, krajTermina);
    		}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("message", message);
		return res;
		
	}
	
	private JSONObject ucitajPodatkeZaProjekcije(HttpServletRequest request) {
		  JSONObject jsonObj = new JSONObject();
		  boolean status = false;
		  
		  String message = "Greska";
		  
		  if(((String) request.getSession().getAttribute("uloga")).equals("Admin")) {
			  try {
				  ArrayList<JSONObject> f = filmoviDAO.izlistajFilmove();
				  ArrayList<JSONObject> s = salaDAO.ucitajSveSale();
				  
				  jsonObj.put("filmovi", f);
				  jsonObj.put("sale", s);
				  status = true;
				  message = "Podaci ucitani";
			  }
			  catch(Exception e){
				  e.printStackTrace();
			  }
		  } else{
			  message = "Niste ulogovani kao administrator.";
		  }
			  jsonObj.put("status", status);
			  jsonObj.put("message", message);
			  return jsonObj;
		
	  }
	
	private JSONObject ucitajFilterProjekcije(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;
		String message = "Greska";
		
		try {
			ArrayList<JSONObject> filmovi = filmoviDAO.izlistajFilmove();
			ArrayList<JSONObject> sale = salaDAO.ucitajSveSale();
			
			res.put("filmovi", filmovi);
			res.put("sale", sale);
			
			message = "Podaci ucitani";
			status = true;			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		res.put("status", status);
		res.put("message", message);
		
		return res;
	}
	
	private JSONObject filtrirajProjekcije(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;
		String message = "Greska";
		
		ArrayList<JSONObject> listaProjJson = new ArrayList<JSONObject>();
		String filmID = request.getParameter("idFilma");
		String pocetakOd = request.getParameter("pocetak");
		String pocetakDo = request.getParameter("pocetakKraj");
		String salaID = request.getParameter("idSale");
		String oznakaTip = request.getParameter("oznakaTipa");
		String minCijena = request.getParameter("cenaMin");
		String maxCijena = request.getParameter("cenaMax");
		
		try {
			if(filmID == null || filmID == "" || filmID.isEmpty()) {
				filmID= "%%";
			}
			if(pocetakOd.length()!=16) {
        		pocetakOd = "2000-12-31 10:10";
			}
			if(pocetakDo.length() != 16) {
        		pocetakDo = "2100-12-31 14:02";
			}
			if(salaID == null || salaID.isEmpty() || salaID == "") {
				salaID = "%%";
			}
			if(oznakaTip == null || oznakaTip.isEmpty() || oznakaTip == "") {
				oznakaTip = "%%";
			}
			if(Integer.valueOf(minCijena) < 0) {
				minCijena = String.valueOf(0);
			}
			if(Integer.valueOf(maxCijena) < 0) {
				maxCijena = String.valueOf(0);
			}
			if(Integer.valueOf(minCijena) > Integer.valueOf(maxCijena)) {
				maxCijena = minCijena;
			}
			
			JSONObject projekcije = projekcijeDAO.filtrirajProjekcije(filmID, pocetakOd, pocetakDo, salaID, oznakaTip, minCijena, maxCijena);
			
			boolean pronadjenFilter = (boolean) projekcije.get("status");
			
			if(pronadjenFilter){
				ArrayList<Projekcija> projLista = (ArrayList<Projekcija>) projekcije.get("listaProjekcija");
			
				for(Projekcija projekcija : projLista) {
					JSONObject p = new JSONObject();
					
					Film film = filmoviDAO.nadjiFilmPrekoIda(projekcija.getFilm());
					Sala sala = salaDAO.nadjiSaluPrekoId(projekcija.getSala());
					
					p.put("ID", projekcija.getId());
					p.put("ID_Filma", projekcija.getFilm());
					p.put("Naziv_Filma", film.getNaziv());
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					String datum = dateFormat.format(projekcija.getDatumiVrijemePrikazivanja());
					p.put("Termin", datum);
					p.put("Sala", sala.getNaziv());
					p.put("TipProjekcije", projekcija.getTipProjekcije());
					p.put("Cena", projekcija.getCijenaKarte());
					listaProjJson.add(p);
				}
				res.put("lista", listaProjJson);
			}
			
		}catch(Exception e) {
			message = "Provjerite vas unos";
			status = false;
			e.printStackTrace();
		}
	  	if(listaProjJson.size()>0) {
    		status = true;
    		message = "Ucitano";
    	}
    	else {
    		message = "Nije pronadjena projekcija sa zadatim kriterijumima";
    	}
	  	
	  	res.put("status", status);
	  	res.put("message", message);
	  	
	  	return res;
	
	}
	
	private JSONObject obrisiProj(HttpServletRequest request) {
		JSONObject res = new JSONObject();
    	boolean status = false;
    	String message = "Greska";
    	
    	String id = request.getParameter("idProjekcije");
    	
		try {
    		status = projekcijeDAO.obrisiProjekciju(id);
    		if(status) {
    			message = "Projekcija uspjesno obrisana.";
    		}
	  	}catch(Exception e) {
    		e.printStackTrace();
    		status = false;
    		message = "Nije moguce izvrsiti tu operaciju";
    	}
    		
		res.put("status", status);
		res.put("message", message);
		
		return res;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		
		switch (action) {
		case "ucitajProjekcijeZaDanas":
			out.print(ucitajDanasnjeProjekcije(request));
			break;
		case "ucitajProjekciju":
			out.print(ucitajJednuProj(request));
			break;
		case "dodajProjekciju":
			out.print(dodajProjekciju(request));
			break;
		case "ucitajProjFilterInfo":
			out.print(ucitajFilterProjekcije(request));
			break;
		case "ucitajZaDodavanjeProjekcije":
			out.print(ucitajPodatkeZaProjekcije(request));
			break;
		case "filter":
			out.print(filtrirajProjekcije(request));
			break;
		case "obrisiProj":
			out.print(obrisiProj(request));
			break;
		}

	}

}
