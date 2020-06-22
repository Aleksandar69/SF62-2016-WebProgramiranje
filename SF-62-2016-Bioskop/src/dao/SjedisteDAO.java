package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.Karta;
import model.Projekcija;
import model.Sjediste;

public class SjedisteDAO {
	

	public ArrayList<Sjediste> slobondaSjedista(String idProj){
		
		ProjekcijeDAO projDAO = new ProjekcijeDAO();
		KartaDAO karteDAO = new KartaDAO();
		
	ArrayList<Sjediste> listaSlobodnihSjedista = new ArrayList<Sjediste>();
	
	Connection conn = null;
	PreparedStatement stmnt = null;
	ResultSet rs = null;
	
	try {
		
		conn = ConnectionManager.getConnection();
		
		String query = "SELECT ID FROM Projekcije WHERE ID = ?";
		
		stmnt = conn.prepareStatement(query);
		stmnt.setString(1, idProj);
		
		rs = stmnt.executeQuery();
		
		while(rs.next()) {
			int index = 1;
			
			String id1 = rs.getString(index++);
			Projekcija p = projDAO.nadjiProjPoIdu(Integer.valueOf(id1));
			
			int idSale = p.getSala();
			ArrayList<Sjediste> sjedista = izlistajSjedistaZaSalu(String.valueOf(idSale));
			ArrayList<Karta> karte = karteDAO.karteZaProj(idProj);
			
			for(Sjediste s : sjedista) {
				boolean slobodno = true;
				for(Karta k : karte) {
					if(String.valueOf(s.getRedniBroj()).equals(k.getSjediste())) {
						slobodno = false;
					}
				}
				if(slobodno) {
					listaSlobodnihSjedista.add(s);
				}
			}
			
		}
	}
	catch(Exception e) {
		e.printStackTrace();
	}
	finally {
		ConnectionManager.close( conn, stmnt, rs);
	}
	return listaSlobodnihSjedista;
	}
	
	public ArrayList<Sjediste> izlistajSjedistaZaSalu(String salaID){
		ArrayList<Sjediste> listaSjedista = new ArrayList<Sjediste>();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID,ID_Sale,Broj_Sedista FROM Sedista WHERE ID_Sale=?";
		
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, salaID);
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				String idSala = rs.getString(index++);
				String brSjedista = rs.getString(index++);
				
				Sjediste sjediste = new Sjediste(Integer.valueOf(brSjedista),Integer.valueOf(idSala));
				listaSjedista.add(sjediste);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return listaSjedista;
	}

	public Sjediste nadjiSjedistepoIDu(String sjedisteID) {
		Sjediste sjediste = null;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID,ID_Sale,Broj_Sedista FROM Sedista WHERE ID=?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, sjedisteID);
			
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				String idSala = rs.getString(index++);
				String sjedisteBr = rs.getString(index++);
				
				sjediste = new Sjediste(Integer.valueOf(sjedisteBr), Integer.valueOf(idSala));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return sjediste;
	}
	
	static String vratiSjedisteID(String salaID, String rBroj) {
		String idSjediste = "0";
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID FROM Sedista WHERE ID_Sale=? AND Broj_Sedista=?";
		
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, salaID);
			stmnt.setString(2, rBroj);
			
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				idSjediste = id;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return idSjediste;
		
	}
	
	public static boolean sjedisteSlobodno(String projekcijaID, String sjedisteID) {
		boolean status = true;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID FROM Karta WHERE ID_Projekcije=? AND ID_Sedista=?";
			
			stmnt = conn.prepareStatement(query);
			
			stmnt.setString(1, projekcijaID);
			stmnt.setString(2, sjedisteID);
			
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
				status = false;
			}
			
			
		}
		catch(Exception e) {
			e.printStackTrace();
			status = false;
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return status;
	}
	
	}
