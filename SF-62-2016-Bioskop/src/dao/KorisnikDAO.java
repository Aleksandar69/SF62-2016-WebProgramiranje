package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
					+ "Where Username=? AND Password=? AND Status='Active'";
			
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
				JSONkor.put("uloga", kor.getUloga().toString());
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
	
	public boolean provjeriLoginInfo(String username, String password) {
		boolean status = false;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users WHERE Username=? AND Password=? AND Status='Active'";
			
			conn = ConnectionManager.getConnection();
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, username);
			stmnt.setString(2, password);
			
			rs= stmnt.executeQuery();
			
			if(rs.next()) {
				status = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
		ConnectionManager.close(conn, stmnt, rs);
		}
		return status;
		
	}
	
	
	public JSONObject getUserSessInfo(HttpServletRequest request) {
		JSONObject JsonObj = new JSONObject();
		boolean status = false;
		String username = (String) request.getSession().getAttribute("username");
		String password = (String) request.getSession().getAttribute("password");
		String uloga = (String) request.getSession().getAttribute("uloga");
		String id = (String) request.getSession().getAttribute("id1");
		String accStatus= (String) request.getSession().getAttribute("status");
		
		if(username!=null && !username.equals("") && provjeriLoginInfo(username, password)) {
			status  = true;
		}
		
		JsonObj.put("status", status);
		JsonObj.put("username", username);
		JsonObj.put("uloga", uloga);
		JsonObj.put("id1", id);
		
		return JsonObj;
		
	}
	
	public JSONObject logOut(HttpServletRequest request) {
		boolean status = false;
		String message = "Greska";
		
		try {
			request.getSession().removeAttribute("username");
			request.getSession().removeAttribute("uloga");
			request.getSession().removeAttribute("status");
			request.getSession().removeAttribute("id1");
			
			status = true;
			message = "Logged out";
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("message",message);
		return obj;
	}
	
	public JSONObject ucitajSveKorisnike() {
		JSONObject res = new JSONObject();
		ArrayList<JSONObject> korisnici = new ArrayList<JSONObject>();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		boolean status = false;
		
		try {
			conn= ConnectionManager.getConnection();
			
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users";
			
			stmnt = conn.prepareStatement(query);
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				status = true;
				
				int index = 1;
				
				String id = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum);


				Korisnik kor = new Korisnik(id, usernameFromDb, passwordFromDb,datumConverted, Uloga.valueOf(uloga), statusFromDb);
				
				JSONObject jsonObj = new JSONObject();
			
				jsonObj.put("ID", kor.getId());
				jsonObj.put("Username", kor.getKorIme());
				jsonObj.put("Password", kor.getLozinka());
				jsonObj.put("Datum", format.format(kor.getDatumRegistracije()));
				jsonObj.put("Uloga", kor.getUloga().toString());
				jsonObj.put("Status", kor.getStatus());
				korisnici.add(jsonObj);
			}
			res.put("status", status);
			res.put("lista", korisnici);
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return res;
	}
	
	}
