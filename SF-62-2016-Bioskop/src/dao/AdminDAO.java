package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import model.Film;
import model.Karta;
import model.Projekcija;

public class AdminDAO {

	public ArrayList<JSONObject> napraviIzvjestaj(String period){
		ArrayList<JSONObject> lista = new ArrayList<JSONObject>();
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		FilmoviDAO filmdao = new FilmoviDAO();
		ProjekcijeDAO projdao = new ProjekcijeDAO();
		KartaDAO kartadao = new KartaDAO();
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID,Naziv FROM Filmovi WHERE Status='Active' ORDER BY Naziv ASC;";

			stmnt = conn.prepareStatement(query);
			rs= stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				
				String id = rs.getString(index++);
				Film film = filmdao.nadjiFilmPrekoIda(Integer.valueOf(id));
				ArrayList<Projekcija> projekcije = projdao.uzmiProjekcijeZaFilm(id, period); 
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("ID", id);
				jsonObj.put("Naziv", film.getNaziv());
				jsonObj.put("BrojProjekcija", projekcije.size());
				int brProdatihKarata = 0;
				int zarada = 0;
				
				for(Projekcija p : projekcije) {
					ArrayList<Karta> karte = kartadao.karteZaProj(String.valueOf(p.getId()));
					brProdatihKarata += karte.size();
					zarada += p.getCijenaKarte() * karte.size();
				}
				jsonObj.put("brojProdatihKarata", brProdatihKarata);
				jsonObj.put("zarada", zarada);
				lista.add(jsonObj);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return lista;
	}
	
	
	
}
