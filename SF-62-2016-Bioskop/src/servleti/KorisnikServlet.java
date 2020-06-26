package servleti;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;


import dao.KorisnikDAO;

/**
 * Servlet implementation class KorisnikServlet
 */
public class KorisnikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	KorisnikDAO korisnikDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public KorisnikServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() throws ServletException {
		korisnikDAO = new KorisnikDAO();
		super.init();
	}

	private JSONObject login(HttpServletRequest request) {
		JSONObject res = korisnikDAO.login(request);
		return res;
	}

	private JSONObject getUserSessInfo(HttpServletRequest request) {
		return korisnikDAO.getUserSessInfo(request);
	}

	private JSONObject logOut(HttpServletRequest request) {
		return korisnikDAO.logOut(request);
	}

	private JSONObject ucitajSveKorisnike() {
		JSONObject res = new JSONObject();
		res = korisnikDAO.ucitajSveKorisnike();
		return res;
	}

	private JSONObject ucitajKorisnika(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		String id = request.getParameter("id");
		res = korisnikDAO.ucitajKorisnika(id);
		return res;
	}

	private JSONObject obrisiKorisnika(HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		boolean status = false;
		String message = "Korisnik ne moze biti obrisan";

		String uloga = (String) request.getSession().getAttribute("uloga");
		if (uloga.equals("Admin")) {
			String id1 = request.getParameter("idKorisnika");
			if (!((String) request.getSession().getAttribute("id1")).equals(id1)) {
				status = korisnikDAO.obrisiKorisnika(id1);
			}
			if (status) {
				message = "Korisnik uspjesno obrisan";
			}
		} else {
			message = "Nista ulogovani kao Admin";
		}
		jsonObj.put("status", status);
		jsonObj.put("message", message);
		return jsonObj;
	}

	private JSONObject editUser(HttpServletRequest request) {
		JSONObject resp = new JSONObject();
		boolean uloga = false;
		boolean sifra = false;

		if (((String) request.getSession().getAttribute("username"))
				.equals(korisnikDAO.getUser(request.getParameter("idKorisnika")).getKorIme())) {
			String idKorisnika = request.getParameter("idKorisnika");
			String novaSifra = request.getParameter("novasifra");
			sifra = korisnikDAO.promijeniSifru(request, idKorisnika, novaSifra);
		}
		if (((String) request.getSession().getAttribute("uloga")).equals("Admin")) {
			String idKorisnika = request.getParameter("idKorisnika");
			String novaUloga = request.getParameter("novaUloga");
			if (!((String) request.getSession().getAttribute("username"))
					.equals(korisnikDAO.getUser(idKorisnika).getKorIme())) {
				uloga = korisnikDAO.promijeniUlogu(request, idKorisnika, novaUloga);
			}
		}
		resp.put("promenjenaSifra", sifra);
		resp.put("promenjenaUloga", uloga);
		return resp;
	}

	private JSONObject filtrirajKorisnike(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		String username = request.getParameter("username");
		String tip = request.getParameter("tip");
		
		res = korisnikDAO.filtrirajKorisnike(username, tip);
		return res;
	}
	
	private JSONObject registracijaKorisnika(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		res = korisnikDAO.registrujKorisnika(request);
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
		String korisnikID = request.getParameter("korisnikID");

		if (action != null || request != null) {
			switch (action) {
			case "login":
				out.print(login(request));
				break;
			case "getSessionInfo":
				out.print(getUserSessInfo(request));
				break;
			case "logOut":
				out.print(logOut(request));
				break;
			case "ucitajSve":
				out.print(ucitajSveKorisnike());
				break;
			case "ucitajKorisnika":
				out.print(ucitajKorisnika(request));
				break;
			case "deleteUser":
				out.print(obrisiKorisnika(request));
				break;
			case "editUser":
				out.print(editUser(request));
				break;
			case "filter":
				out.print(filtrirajKorisnike(request));
				break;
			case "registracija":
				out.print(registracijaKorisnika(request));
				break;
		}
		}
	}



}
