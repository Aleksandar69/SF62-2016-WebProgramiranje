package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.json.simple.JSONObject;


import model.Sala;
import model.TipProjekcije;

public class SalaDAO {

	public Sala nadjiSaluPrekoId(int id) {
		TipProjekcijeDAO tipProjDao = new TipProjekcijeDAO();

		Sala s = null;
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale WHERE ID = ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, String.valueOf(id));

			rs = stmnt.executeQuery();

			if (rs.next()) {
				int index = 1;

				int id1 = Integer.valueOf(rs.getString(index++));
				String naziv = rs.getString(index++);
				String[] id_tipova_proj = rs.getString(index++).split(";");
				ArrayList<TipProjekcije> listaTipovaProjekcija = new ArrayList<TipProjekcije>();

				for (String tip : id_tipova_proj) {
					TipProjekcije tp = tipProjDao.nadjiTipProjPoId(Integer.valueOf(tip));
					if (tp != null) {
						listaTipovaProjekcija.add(tp);
					}
				}
				s = new Sala(id1, naziv, listaTipovaProjekcija);
			} else {
				System.out.println("Nije pronadjena projekcija sa tim ID-om");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return s;
	}

	public ArrayList<JSONObject> ucitajSveSale(){
		TipProjekcijeDAO tipProjDao = new TipProjekcijeDAO();

		
		ArrayList<JSONObject> res = new ArrayList<JSONObject>();
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs= null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID,Naziv,ID_Tipova_Projekcija FROM Sale";

			
			stmnt = conn.prepareStatement(query);
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				
				int id1 = Integer.valueOf(rs.getString(index++));
				String naziv = rs.getString(index++);
				String [] id_tipova_proj = rs.getString(index++).split(";");
				ArrayList<TipProjekcije> listaTipovaProjekcija = new ArrayList<TipProjekcije>();
				
				for(String tip : id_tipova_proj) {
					TipProjekcije tp = tipProjDao.nadjiTipProjPoId(Integer.valueOf(tip));
					if(tp != null) {
						listaTipovaProjekcija.add(tp);
					}
				}
				
				Sala s = new Sala(id1, naziv, listaTipovaProjekcija);
				JSONObject sala = new JSONObject();
				sala.put("ID", s.getId());
				sala.put("Naziv", s.getNaziv());
				sala.put("MaksimumSedista", brMaksimalnoSjedista(String.valueOf(s.getId())));
				
				ArrayList<JSONObject> tp = new ArrayList<JSONObject>();
				
				for(TipProjekcije tipProj : s.getTipProjekcije()) {
					JSONObject tipJson = new JSONObject();
					tipJson.put("ID", tipProj.getID());
					tipJson.put("Naziv", tipProj.getNaziv());
					tp.add(tipJson);				
					}
				sala.put("listaTipova", tp);	
				res.add(sala);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return res;
	}

	public int brMaksimalnoSjedista(String idSale) {
		int br = 0;

		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getConnection();
			String query = "SELECT * FROM Sedista WHERE ID_Sale = ?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, String.valueOf(idSale));

			rs = stmnt.executeQuery();

			while (rs.next()) {
				br++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return br;
	}
	
	public JSONObject slobodnaSalaZaTermin(String idSale,String pocetakTermina,String krajTermina) {
		boolean status = true;
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		String message = null;
		JSONObject res = new JSONObject();
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT * FROM Projekcije WHERE ID_Sale=? AND (KrajTermina>? AND Termin<?)";
		
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, String.valueOf(idSale));
			stmnt.setString(2, pocetakTermina);
			stmnt.setString(3, krajTermina);
		
			rs=stmnt.executeQuery();
			
			if(rs.next()) {
				status = false;
				message = "Termin zauzet";
			}
			else{
				message = "Dodavanje uspjesno";
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		res.put("status", status);
		res.put("message", message);
		return res;
	}

}
