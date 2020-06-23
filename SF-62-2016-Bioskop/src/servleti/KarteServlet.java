package servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dao.FilmoviDAO;
import dao.KartaDAO;
import dao.KorisnikDAO;
import dao.ProjekcijeDAO;
import dao.SalaDAO;
import dao.SjedisteDAO;
import model.Film;
import model.Karta;
import model.Korisnik;
import model.Projekcija;
import model.Sala;
import model.Sjediste;

/**
 * Servlet implementation class KarteServlet
 */
public class KarteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ProjekcijeDAO projDAO;
	SalaDAO salaDAO;
	FilmoviDAO filmDAO;
	SjedisteDAO sjedDAO;
	KartaDAO kartaDAO;
	KorisnikDAO korisnikDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KarteServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		projDAO = new ProjekcijeDAO();
		salaDAO = new SalaDAO();
		filmDAO = new FilmoviDAO();
		sjedDAO = new SjedisteDAO();
		kartaDAO = new KartaDAO();
		korisnikDAO = new KorisnikDAO();
		super.init();

	}

	private JSONObject kupiKartuPodaci(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;
		String message = "Greska";

		try {
			String id = request.getParameter("idProjekcije");
			Projekcija proj = projDAO.nadjiProjPoIdu(Integer.valueOf(id));

			if (proj != null) {
				if (proj.getDatumiVrijemePrikazivanja().compareTo(new Date()) > 0) {
					ArrayList<Sjediste> sjed = sjedDAO.slobondaSjedista(id);
					ArrayList<String> slobodnSjedista = new ArrayList<String>();
					for (Sjediste s : sjed) {
						slobodnSjedista.add(String.valueOf(s.getRedniBroj()));
					}
					Film film = filmDAO.nadjiFilmPrekoIda(proj.getFilm());

					if (film != null) {
						JSONObject filmovInfo = new JSONObject();

						filmovInfo.put("idProjekcije", proj.getId());
						filmovInfo.put("slobodnaSedista", slobodnSjedista);
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
						filmovInfo.put("termin", dateFormat.format(proj.getDatumiVrijemePrikazivanja()));
						filmovInfo.put("cenaKarte", proj.getCijenaKarte());
						filmovInfo.put("nazivFilma", film.getNaziv());
						filmovInfo.put("tipProjekcije", proj.getTipProjekcije());
						Sala sala = salaDAO.nadjiSaluPrekoId(proj.getSala());
						filmovInfo.put("idSale", sala.getId());
						filmovInfo.put("nazivSale", sala.getNaziv());
						filmovInfo.put("trajanje", film.getTrajanje());
						res.put("info", filmovInfo);
						message = "Ucitavanje uspjesno";
						status = true;

					} else {
						message = "Film nije pronadjen";
					}
				} else {
					message = "Projekcija u toku ili zavrsena";
				}
			} else {
				message = "Projekcija nije pronadjena";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.put("status", status);
		res.put("message", message);
		return res;
	}

	private JSONObject kupiKartu(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		boolean status = false;
		String message = "Greska";

		String korIme = (String) request.getSession().getAttribute("username");
		String projekcijaID = request.getParameter("id");
		String sjedista = request.getParameter("odabrana_sedista");
		if (korIme != null) {
			status = kartaDAO.kupiKartu(projekcijaID, sjedista, korIme);
			if (status) {
				message = "Kupovina uspjesna";
			}
		} else {
			message = "Morate biti ulogovani da biste obavili kupovinu";
		}
		res.put("status", status);
		res.put("message", message);

		return res;
	}

	private JSONObject ucitajKarteZaKor(HttpServletRequest request, String korIme) {
    	JSONObject res = new JSONObject();
    	boolean status = false;
    	String message = "Greska";
    	
    	try {
    		if(!korIme.equals((String) request.getSession().getAttribute("username"))
    				&& !((String) request.getSession().getAttribute("uloga")).equals("Admin")){
    				message = "Nije moguce dobiti kartu za ovog korisnika";
    				throw new Exception();
    		}
    		
    		ArrayList<Karta> karteLista = kartaDAO.ucitajKarteZaKor(korIme);
    		ArrayList<JSONObject> karteJSON = new ArrayList<JSONObject>();
    		
    		for(Karta k : karteLista) {
    			Projekcija proj = projDAO.nadjiProjPoIdu(Integer.valueOf(k.getProjekcija()));
    			Film film = filmDAO.nadjiFilmPrekoIda(proj.getFilm());
    			Sala sala = salaDAO.nadjiSaluPrekoId(proj.getSala());
    			status = true;
    			JSONObject jsonObj = new JSONObject();
    			jsonObj.put("ID", k.getId());
    			jsonObj.put("ID_filma", film.getID());
    			jsonObj.put("nazivFilma", film.getNaziv());
    			DateFormat dformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    			String datum = dformat.format(proj.getDatumiVrijemePrikazivanja());
    			jsonObj.put("Termin", datum);
    			jsonObj.put("ID_projekcije", proj.getId());
    			jsonObj.put("tipProjekcije", proj.getTipProjekcije());
    			jsonObj.put("sala", sala.getNaziv());
    			jsonObj.put("sediste", k.getSjediste());
    			jsonObj.put("cena", proj.getCijenaKarte());
    			status = true;
    			karteJSON.add(jsonObj);
    			
    		}
    		res.put("karte", karteJSON);
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    		status = false;
    	}
    	
    	res.put("status", status);
    	res.put("message", message);
    	return res;
    }
	
	private JSONObject obrisiKartu(HttpServletRequest request) {
		String uloga = (String) request.getSession().getAttribute("uloga");
		JSONObject res = new JSONObject();
		boolean status = false;
		String message = "";
		
		if(uloga=="Admin") {
			String idKarte = request.getParameter("idKarte");
			try {
				Karta karta = kartaDAO.nadjiKartuPrekoIDa(idKarte);
				Projekcija proj = projDAO.nadjiProjPoIdu(Integer.valueOf(karta.getProjekcija()));
				if(proj.getDatumiVrijemePrikazivanja().compareTo(new Date()) > 0) {
					status = kartaDAO.obrisiKartu(idKarte);
					if(status) {
						message = "Uspjesno ste obrisali kartu";
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				status = false;
				message = "Greska";
			}
		}
		else {
			message = "Niste ulogovani kao admin";
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
		String idKorisnik = request.getParameter("korisnikID");

		if (action != null && request != null) {
			switch (action) {
			case "kupiKartuInfo":
				out.print(kupiKartuPodaci(request));
				break;
			case "kupiKartu":
				out.print(kupiKartu(request));
				break;
			case "ucitajKarteKorisnika":
				Korisnik kor = korisnikDAO.getUser(request.getParameter("id"));
				String korIme = null;
				if(kor != null) {korIme = kor.getKorIme();}
				out.print(ucitajKarteZaKor(request, korIme));
				break;
			case "obrisiKartu":
				out.print(obrisiKartu(request));
			}
		}
	}

}
