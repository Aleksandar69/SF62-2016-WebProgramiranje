package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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

				JSONObject jsonObj = napraviJSONObjekat(ID, Naziv, Reziser, Glumci, Zanrovi, Trajanje, Distributer,
						Zemlja_Porekla, Godina_Proizvodnje, Opis, status);

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

			String query = "SELECT ID,Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,Zemlja_Porekla,Godina_Proizvodnje,Opis,Status FROM Filmovi WHERE id = ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, filmId);
			rs = stmnt.executeQuery();

			if (rs.next()) {
				int index = 1;
				int ID = Integer.valueOf(rs.getString(index++));
				String Naziv = rs.getString(index++);
				String Reziser = rs.getString(index++);
				String Glumci = rs.getString(index++);
				String Zanrovi = rs.getString(index++);
				int Trajanje = Integer.valueOf(rs.getString(index++));
				String Distributer = rs.getString(index++);
				String Zemlja_Porijekla = rs.getString(index++);
				int Godina_Proizvodnje = rs.getShort(index++);
				String Opis = rs.getString(index++);
				String status = rs.getString(index++);

				JSONObject jsonObj = napraviJSONObjekat(ID, Naziv, Reziser, Glumci, Zanrovi, Trajanje, Distributer,
						Zemlja_Porijekla, Godina_Proizvodnje, Opis, status);

				if (status.equalsIgnoreCase("active")) {
					return jsonObj;
				}
				return null;
			} else {
				System.out.println("Neuspjesno ucitavanje filma");
			}
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return null;

	}

	public ArrayList<JSONObject> uzmiOdredjeneFilmove(String naziv1, int trajanje1, String zanrovi1, String opis1,
			String glumci1, String reziser1, String godina1, String distributer1, String zemlja1) throws SQLException {

		ArrayList<JSONObject> listaFilmova = new ArrayList<JSONObject>();

		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID, Naziv,Reziser,Glumci,Zanrovi,Trajanje,Distributer,,Godina_Proizvodnje,Opis,Status FROM Filmovi"
					+ " WHERE Naziv LIKE ? AND Reziser LIKE ? AND Glumci LIKE ? AND Zanrovi LIKE ? AND Trajanje>? AND Distributer LIKE ? AND  LIKE ? AND Godina_Proizvodnje LIKE ? AND Opis LIKE ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, "%" + naziv1 + "%");
			stmnt.setString(2, "%" + reziser1 + "%");
			stmnt.setString(3, "%" + glumci1 + "%");
			stmnt.setString(4, "%" + zanrovi1 + "%");
			stmnt.setInt(5, trajanje1);
			stmnt.setString(6, "%" + distributer1 + "%");
			stmnt.setString(7, "%" + zemlja1 + "%");
			stmnt.setString(8, "%" + godina1 + "%");
			stmnt.setString(9, "%" + opis1 + "%");

			rs = stmnt.executeQuery();

			while (rs.next()) {
				int index = 1;
				int ID = Integer.valueOf(rs.getString(index++));
				String Naziv = rs.getString(index++);
				String Reziser = rs.getString(index++);
				String Glumci = rs.getString(index++);
				String Zanrovi = rs.getString(index++);
				int Trajanje = Integer.valueOf(rs.getString(index++));
				String Distributer = rs.getString(index++);
				String Zemlja_Porijekla = rs.getString(index++);
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

				JSONObject jedanFilm = napraviJSONObjekat(ID, Naziv, Reziser, Glumci, Zanrovi, Trajanje, Distributer,
						Zemlja_Porijekla, Godina_Proizvodnje, Opis, status);
				listaFilmova.add(jedanFilm);
			}
			return listaFilmova;
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
	}

	public ArrayList<String> uzmiSveZanrove() {
		ArrayList<String> zanrovi = new ArrayList<String>();

		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT Zanr FROM Zanrovi";

			stmnt = conn.prepareStatement(query);

			rs = stmnt.executeQuery();

			while (rs.next()) {
				int index = 1;
				String zanr = rs.getString(index++);
				zanrovi.add(zanr);
			}

		} catch (Exception e) {
			System.out.println("puklo kod loadovanja zanrova");
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return zanrovi;
	}

	public JSONObject napraviJSONObjekat(int id, String naziv, String reziser, String glumci, String zanrovi,
			int trajanje, String distributer, String zemlja_porjekla, int godina_proizvodnje, String opis,
			String status) {
		ArrayList<Zanr> listaZanrova = new ArrayList<Zanr>();
		String[] Zanrs = zanrovi.split(";");
		if (Zanrs.length > 0) {
			for (String znr : Zanrs) {
				try {
					listaZanrova.add(Zanr.valueOf(znr));
				} catch (Exception e) {
					System.out.println("Greska kod Zanra u FilmoviDAO - " + e);
				}
			}
		} else {
			listaZanrova.add(Zanr.valueOf("Prazno"));
		}

		Film film = new Film(id, naziv, reziser, glumci, listaZanrova, trajanje, distributer, zemlja_porjekla,
				godina_proizvodnje, opis);

		JSONObject jsonObj = new JSONObject();
		jsonObj.put("ID", film.getID());
		jsonObj.put("Naziv", film.getNaziv());
		jsonObj.put("Reziser", film.getReziser());
		String[] glumciArr = film.getGlumci().split(";");
		ArrayList<String> glimciList = new ArrayList<String>();
		for (String glumac : glumciArr) {
			glimciList.add(glumac);
		}

		jsonObj.put("Glumci", glimciList);

		ArrayList<String> zanroviLista = new ArrayList<String>();
		for (Zanr z : film.getZanrovi()) {
			zanroviLista.add(z.toString());
		}

		jsonObj.put("Zanrovi", zanroviLista);
		jsonObj.put("Trajanje", film.getTrajanje());
		jsonObj.put("Distributer", film.getDistributer());
		jsonObj.put("Zemlja_Porekla", film.getZemljaPorjekla());
		jsonObj.put("Godina_Proizvodnje", film.getGodinaProizvodnje());
		jsonObj.put("Opis", film.getOpis());
		jsonObj.put("status", status);

		return jsonObj;
	}

	public static boolean logickoBrisanjeFilm(String id) {
		Connection conn = null;
		PreparedStatement stmnt = null;
		boolean status = false;

		try {
			conn = ConnectionManager.getConnection();

			String query = "UPDATE Filmovi SET Status='Deleted' WHERE ID = ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, id);

			int red = stmnt.executeUpdate();
			if (red > 0) {
				status = true;
			} else {
				System.out.println("Nije pronadjen red za update");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
	}
}
