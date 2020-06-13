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
		}
	}

}
