package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.spi.DirStateFactory.Result;

import model.Karta;
import model.Projekcija;

public class KartaDAO {
	
	

	public ArrayList<Karta> karteZaProj(String projekcijaID) {
		ArrayList<Karta> listaKarata = new ArrayList<Karta>();

		SjedisteDAO sjedDAO = new SjedisteDAO();

		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;

		try {
			conn = ConnectionManager.getConnection();

			String query = "SELECT ID,ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik FROM Karta WHERE ID_Projekcije=?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, projekcijaID);

			rs = stmnt.executeQuery();

			while (rs.next()) {
				int index = 1;

				String id = rs.getString(index++);
				int kartaID = Integer.valueOf(id);
				String idProjekcija = rs.getString(index++);
				String idSjediste = rs.getString(index++);
				String sjedisteOznaka = String.valueOf(sjedDAO.nadjiSjedistepoIDu(idSjediste).getRedniBroj());
				String vrijemeProdaje = rs.getString(index++);
				DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date datum = dateForm.parse(vrijemeProdaje);
				String korisnik = rs.getString(index++);
				Karta karta = new Karta(kartaID, idProjekcija, sjedisteOznaka, datum, korisnik);
				listaKarata.add(karta);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return listaKarata;
	}

	public boolean kupiKartu(String projekcijaID, String sjedista, String korIme) {
		boolean status = true;
		ProjekcijeDAO projDAO = new ProjekcijeDAO();
		SjedisteDAO sjedDAO = new SjedisteDAO();
		
		try {
			Projekcija p = projDAO.nadjiProjPoIdu(Integer.valueOf(projekcijaID));
			String[] sjedistaArr = sjedista.split(";");
			ArrayList<String> sjedistaList = new ArrayList<String>();
			for(int i=0; i < sjedistaArr.length; i++) {
				String s = sjedistaArr[i];
				int val = Integer.valueOf(s);
				if((i+1) < sjedistaArr.length) {
					if(val != (Integer.valueOf(sjedistaArr[i+1]) -1) ||
							(val+1) != Integer.valueOf(sjedistaArr[i+1])) {
						System.out.println("Sjedista nisu susjedna");
						throw new Exception();
					}
				}
				sjedistaList.add(s);
			}
			
			for(String sjed : sjedistaList) {
				String sjedisteID = sjedDAO.vratiSjedisteID(String.valueOf(p.getSala()), sjed);
			
			if(!sacuvajKartuUBazu(projekcijaID, sjedisteID, korIme)) {
				status = false;
			}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return status;
		
	}

	public boolean sacuvajKartuUBazu(String projekcijaID, String sjedisteID, String kor) {
		boolean status = false;
		
		SjedisteDAO sjedDAO = new SjedisteDAO();
		ProjekcijeDAO projDAO = new ProjekcijeDAO();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			
			conn = ConnectionManager.getConnection();
			
			String query = "INSERT INTO Karta(ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik) VALUES(?,?,?,?)";
			
			stmnt = conn.prepareStatement(query);
			
			stmnt.setString(1, projekcijaID);
			stmnt.setString(2, sjedisteID);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String datum = dateFormat.format(new Date());
			stmnt.setString(3, datum);
			stmnt.setString(4, kor);
			
			int red = 0;
			
			if(sjedDAO.sjedisteSlobodno(projekcijaID, sjedisteID));
			red = stmnt.executeUpdate();
		
		
			if(red > 0) {
				status = true;
				projDAO.smanjiStanjeKarata(projekcijaID);
			}
		
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
	}
	
	public ArrayList<Karta> ucitajKarteZaKor(String korIme){
		ArrayList<Karta> listaKarata = new ArrayList<Karta>();
		
		SjedisteDAO sjedDAO = new SjedisteDAO();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID,ID_Projekcije,ID_Sedista,VremeProdaje,Korisnik FROM Karta WHERE Korisnik=?";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, korIme);
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				
				String id = rs.getString(index++);
				int kartaID = Integer.valueOf(id);
				String idProjekcija = rs.getString(index++);
				String idSjediste = rs.getString(index++);
				String sjedisteOznaka = String.valueOf(sjedDAO.nadjiSjedistepoIDu(idSjediste).getRedniBroj());
				String vrijemeProdaje = rs.getString(index++);
				DateFormat dateForm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date datum = dateForm.parse(vrijemeProdaje);
				String korisnik = rs.getString(index++);
				Karta karta = new Karta(kartaID, idProjekcija, sjedisteOznaka, datum, korisnik);
				listaKarata.add(karta);
			}

		}	catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return listaKarata;
	}

}
