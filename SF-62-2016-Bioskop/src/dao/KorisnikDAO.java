package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;

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

			if (rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);

				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum);

				kor = new Korisnik(id, usernameFromDb, passwordFromDb, datumConverted, Uloga.valueOf(uloga),
						statusFromDb);

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

			} else {
				message = "Provjerite unijete podatke.";
			}
		} catch (Exception e) {
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

			rs = stmnt.executeQuery();

			if (rs.next()) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
		String accStatus = (String) request.getSession().getAttribute("status");

		if (username != null && !username.equals("") && provjeriLoginInfo(username, password)) {
			status = true;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject obj = new JSONObject();
		obj.put("status", status);
		obj.put("message", message);
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
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users";

			stmnt = conn.prepareStatement(query);

			rs = stmnt.executeQuery();

			while (rs.next()) {
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

				Korisnik kor = new Korisnik(id, usernameFromDb, passwordFromDb, datumConverted, Uloga.valueOf(uloga),
						statusFromDb);

				JSONObject jsonObj = napraviJsonObjekat(kor.getId(), kor.getKorIme(), kor.getLozinka(), kor.getDatumRegistracije(), kor.getUloga().toString(), kor.getStatus());
				korisnici.add(jsonObj);
			}
			res.put("status", status);
			res.put("lista", korisnici);
		}

		catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return res;
	}
	
	public JSONObject ucitajKorisnika(String id) {
		JSONObject res = new JSONObject();
		JSONObject korisnik = null;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		boolean status = false;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
					+ " WHERE ID = ?";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, id);
			
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
				status = true;
				
				int index = 1;
				
				String id1 = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);

				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum);

				Korisnik kor = new Korisnik(id1, usernameFromDb, passwordFromDb, datumConverted, Uloga.valueOf(uloga),
						statusFromDb);
				
				
				korisnik = napraviJsonObjekat(kor.getId(), kor.getKorIme(), kor.getLozinka(), kor.getDatumRegistracije(), kor.getUloga().toString(), kor.getStatus());
				
			}
			
			res.put("status", status);
			res.put("korisnik", korisnik);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return res;
	}
	
	private JSONObject napraviJsonObjekat(String id, String korIme, String lozinka, Date datum, String uloga, String status) {
		JSONObject film = new JSONObject();
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		
		film.put("ID", id);
		film.put("Username", korIme);
		film.put("Password", lozinka);
		film.put("Datum", format.format(datum));
		film.put("Uloga", uloga);
		film.put("Status", status);
		
		return film;
	}
	
	public boolean obrisiKorisnika(String id) {
		boolean status = false;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "UPDATE Users SET Status='Deleted' WHERE ID=?";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, id);
			
			int red = stmnt.executeUpdate();
			if(red > 0) {
				status = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
	}
	finally {
		ConnectionManager.close(conn, stmnt, null);
	}
		return status;
	}
	
	public Korisnik getUser(String id) {
		Korisnik kor = null;
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		try {
			conn= ConnectionManager.getConnection();
			
			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
					+ " WHERE ID = ?";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, id);
			
			rs = stmnt.executeQuery();
			
			if(rs.next()) {				
				int index = 1;
				
				String id1 = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum);

				kor = new Korisnik(id, usernameFromDb, passwordFromDb, datumConverted, Uloga.valueOf(uloga),
						statusFromDb);
				
				}	
	} catch(Exception e) {
		e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return kor;
	}
	
	public boolean promijeniSifru(HttpServletRequest request, String id, String novaSifra) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "UPDATE Users SET Password=? WHERE ID=? ;";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, novaSifra);
			stmnt.setString(2, id);
			
			int red = stmnt.executeUpdate();
			if(red > 0) {
				status = true;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
	}
	
	public boolean promijeniUlogu(HttpServletRequest request, String id, String novaUloga) {
		boolean status = false;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			if(!((String)request.getSession().getAttribute("uloga")).equals("Admin")){
				System.out.println("Ulogovani korisnik nije admin");
				throw new IOException(); 
			}
			if(!novaUloga.equals("Admin") && !novaUloga.equals("Korisnik")) {
				System.out.println("Unesite pravilan naziv uloge.");
				throw new IOException();
			}
			conn = ConnectionManager.getConnection();
			String query = "UPDATE Users SET Uloga=? WHERE ID=? ";
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, novaUloga);
			stmnt.setString(2, id);
			
			int red = stmnt.executeUpdate();
			
			if(red> 0) {
				status = true;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
	}
	
	public JSONObject filtrirajKorisnike(String korIme,String lozinka,String datum,String tipKor) {		
		JSONObject res = new JSONObject();
		ArrayList<JSONObject> korisnici = new ArrayList<JSONObject>();

		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		boolean status = false;

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID, Username,Password,DatumRegistracije,Uloga,Status FROM Users"
					+ " WHERE Username LIKE ? AND Password LIKE ? AND DatumRegistracije LIKE ? AND Uloga LIKE ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, "%" +korIme+ "%");
			stmnt.setString(2, "%"+lozinka+"%");
			stmnt.setString(3, "%"+datum+"%");
			stmnt.setString(4, "%"+tipKor+"%");
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				status = true;
				
				int index = 1;
				
				String id1 = rs.getString(index++);
				String usernameFromDb = rs.getString(index++);
				String passwordFromDb = rs.getString(index++);
				String datum2 = rs.getString(index++);
				String uloga = rs.getString(index++);
				String statusFromDb = rs.getString(index++);

				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date datumConverted = format.parse(datum2);

				Korisnik kor = new Korisnik(id1, usernameFromDb, passwordFromDb, datumConverted, Uloga.valueOf(uloga),
						statusFromDb);
				
				
				JSONObject korisnik = napraviJsonObjekat(kor.getId(), kor.getKorIme(), kor.getLozinka(), kor.getDatumRegistracije(), kor.getUloga().toString(), kor.getStatus());
				korisnici.add(korisnik);
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