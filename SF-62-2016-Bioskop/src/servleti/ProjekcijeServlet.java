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
		}

	}

}
