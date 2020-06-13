package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import model.Korisnik;
import model.Uloga;

public class KorisnikDAO {

	public JSONObject login(HttpServletRequest request) {
		JSONObject res = new JSONObject();
		String message = "Greska";
		boolean status = false;
		Korisnik kor = null;
		JSONObject JSONkor = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Connection conn = null;
		PreparedStatement pstmnt = null;
		ResultSet rs = null;
		
		try {
			String query = "SELECT ID, Username, Password, DatumRegistracije,Uloga,Status FROM Users "
					+ "Where Username=? AND Password=? AND Status='Active";
			
			conn = ConnectionManager.getConnection();
			pstmnt = conn.prepareStatement(query);
			pstmnt.setString(1, username);
			pstmnt.setString(2, password);
			
			rs = pstmnt.executeQuery();
			
			if(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum);
				
				kor = new Korisnik(id, usernameFromDb, passwordFromDb,datumConverted, Uloga.valueOf(uloga), statusFromDb);
			
			
				JSONkor = new JSONObject();
				JSONkor.put("username", kor.getKorIme());
				JSONkor.put("uloga", kor.getUloga());
				JSONkor.put("status", kor.getStatus());
				status = true;
				request.getSession().setAttribute("username", kor.getKorIme());
				request.getSession().setAttribute("password", kor.getLozinka());
				request.getSession().setAttribute("uloga", kor.getUloga().toString());
				request.getSession().setAttribute("id1", kor.getId());
				request.getSession().setAttribute("status", kor.getStatus());
				message = "Uspjesno logovanje!";

			}
			else {
				message = "Provjerite unijete podatke.";
			}
		}
		catch(Exception e){
			System.out.println("Greska pri logovanju" + e);
			e.printStackTrace();
		}
		
		finally {
			ConnectionManager.close(conn, pstmnt, rs);
		}
		res.put("status", status);
		res.put("korisnik", JSONkor);
		res.put("message", message);
		return res;
		
	}
	
	
	public JSONObject getUserSessInfo(HttpServletRequest request) {
		JSONObject JsonObj = new JSONObject();
		Boolean status = false;
		String username = (String) request.getSession().getAttribute("username");
		String password = (String) request.getSession().getAttribute("password");
		String uloga = (String) request.getSession().getAttribute("uloga");
		String id = (String) request.getSession().getAttribute("id1");
		String accStatus= (String) request.getSession().getAttribute("status");
		
		JsonObj.put("status", status);
		JsonObj.put("username", username);
		JsonObj.put("uloga", uloga);
		JsonObj.put("id1", id);
		
		return JsonObj;
		
	}
}