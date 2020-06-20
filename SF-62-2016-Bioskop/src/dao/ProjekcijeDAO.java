package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import model.Projekcija;

public class ProjekcijeDAO {

	public ArrayList<Projekcija> ucitajDanasnjeProjekcije(HttpServletRequest request, String datum) {
		boolean status = false;
		ArrayList<Projekcija> projekcije = new ArrayList<Projekcija>();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID FROM Projekcije  WHERE Status='Active' AND Termin BETWEEN ? AND ? ORDER BY ID_Filma ASC,Termin ASC ;";

			stmnt = conn.prepareStatement(query);
			
			stmnt.setString(1, datum);
			stmnt.setString(2, datum+ "23:59:59");
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				Projekcija proj = nadjiProjPoIdu(Integer.valueOf(id));
				
				
				
				if(proj != null) {
					projekcije.add(proj);
				}
					
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn,stmnt, rs);
		}
		return projekcije;

	}
	
	public Projekcija nadjiProjPoIdu(int id) {
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		boolean status = false;
		Projekcija projekcija = null;
		
		try {
			conn = ConnectionManager.getConnection();
			String query = "SELECT ID,ID_Filma,TipProjekcije,ID_Sale,Termin,CenaKarte,Administrator,Status,MaksimumKarata,BrojProdanihKarata FROM Projekcije  WHERE ID=?;";

			stmnt = conn.prepareStatement(query);
			stmnt.setInt(1, id);
			rs = stmnt.executeQuery();
			
			if(rs.next()) {
			int index = 1;
			int id1 = Integer.valueOf(rs.getString(index++));
			int idFilma = Integer.valueOf(rs.getString(index++));
			String tipProjekcije = rs.getString(index++);
			int idSale = Integer.valueOf(rs.getString(index++));
			String Termin = rs.getString(index++);
			Date datum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Termin);
			int cijenaKarte = Integer.valueOf(rs.getString(index++));
			String admin = rs.getString(index++);
			String statusdb = rs.getString(index++);;
			String maksStr = rs.getString(index++);
			int maksKarata = Integer.valueOf(maksStr);
			String prodStr = rs.getString(index++);
			int prodaneKarte = Integer.valueOf(prodStr);
			
			projekcija = new Projekcija(id1, idFilma, tipProjekcije, idSale, datum, cijenaKarte, admin, statusdb, maksKarata, prodaneKarte);
			
			status = true;
			
			}
			else {
				System.out.println("Ne postoji projekcija sa tim id-om");
			}
			
			}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return projekcija;
	}
	
	public boolean dodajProjekciju(Projekcija projekcija,String krajTermina) {
		boolean status = false;
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "INSERT INTO Projekcije(ID_Filma,TipProjekcije,ID_Sale,Termin,CenaKarte,Administrator,Status,MaksimumKarata,KrajTermina) VALUES (?,?,?,?,?,?,?,?,?)";

			stmnt = conn.prepareStatement(query);
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date datum = new Date();
			stmnt.setString(1, String.valueOf(projekcija.getFilm()));
			stmnt.setString(2, projekcija.getTipProjekcije());
			stmnt.setString(3, String.valueOf(projekcija.getSala()));
			
			Date datum1 = projekcija.getDatumiVrijemePrikazivanja();
			DateFormat dateFormatSaSatom = new SimpleDateFormat("yyyy-MM-dd HH:mm");      
			String pocetak = dateFormatSaSatom.format(datum1);
			
			stmnt.setString(4, String.valueOf(pocetak));
			stmnt.setString(5, String.valueOf(projekcija.getCijenaKarte()));
			stmnt.setString(6, projekcija.getUsernameAdministratora());
			stmnt.setString(7, "Active");
			stmnt.setString(8, String.valueOf(projekcija.getMaksimumKarata()));
			stmnt.setString(9, krajTermina);
			
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

}
