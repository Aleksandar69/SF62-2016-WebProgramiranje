package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import model.Zanr;
import model.*;

public class FilmoviDAO {

	public ArrayList<JSONObject> izlistajFilmove() throws SQLException {

		Connection conn = null;
		PreparedStatement pstmnt = null;
		ResultSet rs = null;
		
		ArrayList<JSONObject> sviFilmovi = new ArrayList<JSONObject>();

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi";

			pstmnt = conn.prepareStatement(query);

			rs = pstmnt.executeQuery();
			

			while (rs.next()) {
				int index = 1;
				int ID = Integer.valueOf(rs.getString(index++));
				String Naziv = rs.getString(index++);
				String Reziser = rs.getString(index++);
				String Glumci = rs.getString(index++);
				String Zanrovi = rs.getString(index++);
				int Trajanje = Integer.valueOf(rs.getString(index++));
				String Distributer = rs.getString(index++);
				String Zemlja_Porekla = rs.getString(index++);
				int Godina_Proizvodnje = Integer.valueOf(rs.getString(index++));
				String Opis = rs.getString(index++);
				String status = rs.getString(index++);

				ArrayList<Zanr> Zanrovi_n = new ArrayList<Zanr>();
				String[] Zanrs = Zanrovi.split(";");
				if (Zanrs.length > 0) {
					for (String znr : Zanrs) {
						try {
							Zanrovi_n.add(Zanr.valueOf(znr));
						} catch (Exception e) {
							System.out.println("Greska kod Zanra u FilmoviDAO - " + e);
						}
					}
				} else {
					Zanrovi_n.add(Zanr.valueOf("Prazno"));
				}

				Film film = new Film(ID, Naziv, Reziser, Glumci, Zanrovi_n, Trajanje, Distributer, Zemlja_Porekla,
						Godina_Proizvodnje, Opis);
				
				JSONObject jsonObj = new JSONObject();
				
				jsonObj.put("ID", film.getID());
				jsonObj.put("Naziv", film.getNaziv());
				jsonObj.put("Reziser", film.getReziser());
				String[] glumciStrArr = film.getGlumci().split(";");
				ArrayList<String> glumci = new ArrayList<String>();
				for(String glumac : glumciStrArr) {
					glumci.add(glumac);
				}
				
				jsonObj.put("Glumci", glumci);
				
				ArrayList<String> zanrovi = new ArrayList<String>();
				for(Zanr zanr : film.getZanrovi()) {
					zanrovi.add(zanr.toString());
				}
				jsonObj.put("Zanrovi", zanrovi);
				jsonObj.put("Trajanje", film.getTrajanje());
				jsonObj.put("Distributer",film.getDistributer());
				jsonObj.put("ZemljaPorjekla", film.getZemljaPorjekla());
				jsonObj.put("Godina_Proizvodnje", film.getGodinaProizvodnje());
				jsonObj.put("Opis", film.getOpis());
				
				if(status.equalsIgnoreCase("active")) {
					sviFilmovi.add(jsonObj);
				}

			}
			return sviFilmovi;
		} finally {
			System.out.println("Konekcija se zatvara");
			close(conn, pstmnt, rs);
		}
	
	}

	private void close(Connection myConn, Statement myStmnt, ResultSet myRS) {
		try {
			if (myRS != null) {
				myRS.close();
			}
			if (myStmnt != null) {
				myStmnt.close();
			}
			if (myConn != null) {
				myConn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
