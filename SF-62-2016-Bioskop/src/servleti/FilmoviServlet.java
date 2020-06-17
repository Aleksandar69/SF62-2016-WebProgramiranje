package servleti;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dao.FilmoviDAO;
import model.Film;

/**
 * Servlet implementation class FilmoviServlet
 */
public class FilmoviServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private FilmoviDAO filmoviDAO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public FilmoviServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	  @Override public void init() throws ServletException { filmoviDAO = new
	  FilmoviDAO(); super.init(); }
	  
	  
	  private JSONObject ucitajFilmove() {
		  
			boolean status = false;
			
			System.out.println("U metodi UcitajFilmove");

			ArrayList<JSONObject> filmovi = new ArrayList<JSONObject>();
			try {
				
				filmovi = filmoviDAO.izlistajFilmove();
				if(filmovi.size() > 0) {
					status = true;
				}

			} catch (SQLException e) {
				System.out.println("Puklo u listanju filmova u servletu");
				e.printStackTrace();
			}
			
			JSONObject res = new JSONObject();
			
			res.put("status", status);
			res.put("filmovi", filmovi);
			
			return res;
	  }
	  
	  private JSONObject obrisiFilm(String id) {
		  boolean status = filmoviDAO.logickoBrisanjeFilm(id);
		  JSONObject res = new JSONObject();
		  
		  res.put("status", status);
		  
		  return res;
	  }
	  
	  private JSONObject ucitajJedanFilm(String idFilma) {
		  boolean status = false;
		  JSONObject res = new JSONObject();
		  System.out.println("U metodi ucitajJedanFilm()");
		  
		  JSONObject film = null;
		  
		  try {
			film = filmoviDAO.prikaziJedanFilm(idFilma);
			if( film!= null) {
				status = true;
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		  res.put("status", status);
		  res.put("film", film);
		  
		  return res;
	  }
	  
	  private JSONObject uzmiZanrove() {
		  JSONObject res = new JSONObject();
		  ArrayList<String> listaZanrova = filmoviDAO.uzmiSveZanrove();
		  boolean status = false;
		  if(listaZanrova.size()>0) {
			  status = true;
		  }
		  res.put("status", status);
		  res.put("zanrovi", listaZanrova);
		  return res;
	  }
	 
	  
	  private JSONObject filterFilm(HttpServletRequest request) {
		  String naziv = request.getParameter("naziv");
		  int  trajanje = 0;
		 	try {
	    		trajanje = Integer.valueOf(request.getParameter("trajanje"));
	    	}
	    	catch(Exception e) {
	    		System.out.println("Greska pri uzimanju trajanja koje nema vrijednost u filteru");
	    		e.printStackTrace();
	    	}
	    	System.out.println("posle exceptiona");

		 	String zanrovi = request.getParameter("zanr");
	    	String opis = request.getParameter("opis");
	    	String glumci = request.getParameter("glumci");
	    	String reziser = request.getParameter("reziser");
	    	String godina = request.getParameter("godina");
	    	String distributer = request.getParameter("distributer");
	    	String zemlja = request.getParameter("zemlja");
	    	Boolean status = false;
	    	ArrayList<JSONObject> filmovi = new ArrayList<JSONObject>();
	    	try {
	    		filmovi = filmoviDAO.uzmiOdredjeneFilmove(naziv,trajanje,zanrovi,opis,glumci,reziser,godina,distributer,zemlja);
	    	
	    		if(filmovi.size()>0) {
	    			status = true;
	    		}
	    	}
	    	catch (Exception e) {
	    		System.out.println("Puklo je ovde na ucitaj sve filmove.");
	    	}
	    	JSONObject res = new JSONObject();

		    res.put("status", status);
		    res.put("filmovi", filmovi);
		    
		    return res;
	  }

	  
	  private JSONObject izmeniFilm(HttpServletRequest request) {
	    	
	    	JSONObject res = new JSONObject();
		    res = filmoviDAO.izmeniFilm(request);
		    return res;
	    }
	  
	  private JSONObject dodajFilm(HttpServletRequest request) {
		  boolean status = filmoviDAO.dodajFilm(request);
		  JSONObject res = new JSONObject();
		  res.put("status", status);
		  
		  return res;
	  }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

    

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		String filmID = request.getParameter("filmID");
		
		switch(action) {
		case "ucitajFilmove":
			out.print(ucitajFilmove());
		break;
		case "ucitajFilm":
			out.print(ucitajJedanFilm(filmID));
			break;
		case "uzmiZanrove":
			out.print(uzmiZanrove());
			break;
		case "filterFilm":
			out.print(filterFilm(request));
			break;
		case "obrisiFilm":
			out.print(obrisiFilm(filmID));
			break;
		case "izmeniFilm":
			out.print(izmeniFilm(request));
			break;
		case "dodajFilm":
			out.print(dodajFilm(request));
			break;
		}
	}
}
