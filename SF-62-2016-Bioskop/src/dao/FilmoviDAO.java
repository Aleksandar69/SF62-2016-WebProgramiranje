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
				for (String glumac : glumciStrArr) {
					glumci.add(glumac);
				}

				jsonObj.put("Glumci", glumci);

				ArrayList<String> zanrovi = new ArrayList<String>();
				for (Zanr zanr : film.getZanrovi()) {
					zanrovi.add(zanr.toString());
				}
				jsonObj.put("Zanrovi", zanrovi);
				jsonObj.put("Trajanje", film.getTrajanje());
				jsonObj.put("Distributer", film.getDistributer());
				jsonObj.put("ZemljaPorjekla", film.getZemljaPorjekla());
				jsonObj.put("Godina_Proizvodnje", film.getGodinaProizvodnje());
				jsonObj.put("Opis", film.getOpis());

				if (status.equalsIgnoreCase("active")) {
					sviFilmovi.add(jsonObj);
				}

			}
			return sviFilmovi;
		} finally {
			System.out.println("Konekcija se zatvara");
			ConnectionManager.close(conn, pstmnt, rs);
		}
	}

	public JSONObject prikaziJedanFilm(String filmId) throws NumberFormatException, SQLException {
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query="SELECT ID,Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi WHERE id = ?";
		
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, filmId);
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
				int index = 1;
				int id= Integer.valueOf(rs.getString(index++));
				String naziv = rs.getString(index++);
				String reziser = rs.getString(index++);
				String glumci = rs.getString(index++);
				String zanrovi = rs.getString(index++);
				int trajanje = Integer.valueOf(rs.getString(index++));
				String distributer = rs.getString(index++);
				String zemlja_porjekla = rs.getString(index++);
				int godina_proizvodnje = rs.getShort(index++);
				String opis = rs.getString(index++);
				String status = rs.getString(index++);
				
				ArrayList<Zanr> zanroviList = new ArrayList<Zanr>();
				String[] zanroviArr = zanrovi.split(";");
				for(String z : zanroviArr) {
					try {
						zanroviList.add(Zanr.valueOf(z));
					}
					catch(Exception e) {
						System.out.println("Greska kod unosa zanra" + e);
						e.printStackTrace();
					}
				}
				
				Film film = new Film(id, naziv, reziser, glumci, zanroviList, trajanje,
						distributer, zemlja_porjekla,godina_proizvodnje, opis);
				
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("ID", film.getID());
			jsonObj.put("Naziv", film.getNaziv());
			jsonObj.put("Reziser", film.getReziser());
			String[] glumciArr = film.getGlumci().split(";");
			ArrayList<String> glimciList = new ArrayList<String>();
			for(String glumac : glumciArr) {
				glimciList.add(glumac);
			}
			
			jsonObj.put("Glumci", glimciList);
			
			ArrayList<String> zanroviLista = new ArrayList<String>();
			for(Zanr z : film.getZanrovi()) {
				zanroviLista.add(z.toString());
			}
			
			jsonObj.put("Zanrovi", zanroviLista);
			jsonObj.put("Trajanje",film.getTrajanje());
			jsonObj.put("Distributer", film.getDistributer());
			jsonObj.put("Zemlja_Porijekla", film.getZemljaPorjekla());
			jsonObj.put("Godina_Proizvodnje",film.getGodinaProizvodnje());
			jsonObj.put("Opis", film.getOpis());
			jsonObj.put("status", status);
			
			if(status.equalsIgnoreCase("active")) {
				return jsonObj;
			}
			return null;
			}else {
				System.out.println("Neuspjesno ucitavanje filma");
			}
			}finally{
				ConnectionManager.close(conn, stmnt, rs);
			}
			return null;
			
	}
}
