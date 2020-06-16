package servleti;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import dao.KorisnikDAO;
import jdk.nashorn.internal.runtime.Undefined;

/**
 * Servlet implementation class KorisnikServlet
 */
public class KorisnikServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       KorisnikDAO korisnikDAO;
	
	
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
    
	/**
     * @see HttpServlet#HttpServlet()
     */
    public KorisnikServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String action = request.getParameter("action");
		String korisnikID = request.getParameter("korisnikID");
		
		if(action!= null || request != null) {
			switch(action) {
			case "login":
				out.print(login(request));
				break;
			case "getSessionInfo":
				out.print(getUserSessInfo(request));
			}
		}
	}

}
