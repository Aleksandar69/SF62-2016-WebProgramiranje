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
import dao.ProjekcijeDAO;
import dao.SalaDAO;
import model.Film;
import model.Projekcija;
import model.Sala;

/**
 * Servlet implementation class ProjekcijeServlet
 */
public class ProjekcijeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ProjekcijeDAO projekcijeDAO = null;
	FilmoviDAO filmoviDAO = null;
	SalaDAO salaDAO = null;

	@Override
	public void init() throws ServletException {
		projekcijeDAO = new ProjekcijeDAO();
		filmoviDAO = new FilmoviDAO();
		salaDAO = new SalaDAO();
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

		}

	}

}
