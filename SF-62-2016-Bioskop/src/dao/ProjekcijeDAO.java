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

import model.Projekcija;

public class ProjekcijeDAO {
	
	public ArrayList<Projekcija> uzmiProjekcijeZaFilm(String filmID, String termin){
		ArrayList<Projekcija> lista = new ArrayList<Projekcija>();
		Connection conn = ConnectionManager.getConnection();
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			String query = "SELECT ID FROM Projekcije WHERE Status='Active' AND ID_Filma=? AND (Termin BETWEEN ? AND ?)ORDER BY ID ASC;";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, filmID);
			stmnt.setString(2, termin);
			stmnt.setString(3, termin+" 23:59:59");
			rs = stmnt.executeQuery();
			while(rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				Projekcija proj = nadjiProjPoIdu(Integer.valueOf(id));
				lista.add(proj);
				
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
			stmnt.setString(2, datum + "23:59:59");
			rs = stmnt.executeQuery();

			while (rs.next()) {
				int index = 1;
				String id = rs.getString(index++);
				Projekcija proj = nadjiProjPoIdu(Integer.valueOf(id));

				if (proj != null) {
					projekcije.add(proj);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
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

			if (rs.next()) {
				int index = 1;
				int id1 = Integer.valueOf(rs.getString(index++));
				int idFilma = Integer.valueOf(rs.getString(index++));
				String tipProjekcije = rs.getString(index++);
				int idSale = Integer.valueOf(rs.getString(index++));
				String Termin = rs.getString(index++);
				Date datum = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(Termin);
				int cijenaKarte = Integer.valueOf(rs.getString(index++));
				String admin = rs.getString(index++);
				String statusdb = rs.getString(index++);
				String maksStr = rs.getString(index++);
				int maksKarata = Integer.valueOf(maksStr);
				String prodStr = rs.getString(index++);
				int prodaneKarte = Integer.valueOf(prodStr);

				projekcija = new Projekcija(id1, idFilma, tipProjekcije, idSale, datum, cijenaKarte, admin, statusdb,
						maksKarata, prodaneKarte);

				status = true;

			} else {
				System.out.println("Ne postoji projekcija sa tim id-om");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, rs);
		}
		return projekcija;
	}

	public boolean dodajProjekciju(Projekcija projekcija, String krajTermina) {
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

			if (red > 0) {
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionManager.close(conn, stmnt, null);
		}

		return status;
	}

	public JSONObject filtrirajProjekcije(String filmID,String pocetakOd,String pocetakDo,String salaID,String oznakaTip,String minCijena,String maxCijena) {
		JSONObject res =  new JSONObject();
		String message = "Greska";
		boolean status = false;
		
		ArrayList<Projekcija> listaProjekcija = new ArrayList<Projekcija>();
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "SELECT ID FROM Projekcije WHERE Status='Active' AND (Termin BETWEEN ? AND ?) AND ID_Filma LIKE ?"
					+ " AND ID_sale LIKE ? AND TipProjekcije LIKE ? AND CenaKarte BETWEEN ? AND ?  ORDER BY ID_Filma ASC,Termin ASC";

			
			stmnt = conn.prepareStatement(query);
			
			stmnt.setString(1, pocetakOd);
			stmnt.setString(2, pocetakDo);
			stmnt.setString(3, filmID);
			stmnt.setString(4, salaID);
			stmnt.setString(5, oznakaTip);
			stmnt.setString(6, minCijena);
			stmnt.setString(7, maxCijena);
			
			rs = stmnt.executeQuery();
			
			while(rs.next()) {
				int index = 1;
				
				String id = rs.getString(index++);
				Projekcija proj = nadjiProjPoIdu(Integer.valueOf(id));
				if(proj != null) {
					listaProjekcija.add(proj);
				}
				status = true;
				message = "Projekcije ucitane";
			}
			res.put("listaProjekcija", listaProjekcija);
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
	
	public boolean obrisiProjekciju(String projId) {
		boolean status = false;
		
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "UPDATE Projekcije SET Status='Deleted' WHERE ID=?";
			
			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, projId);
			
			int red = stmnt.executeUpdate();
			if(red > 0) {
				status = true;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
	}
	
	public boolean smanjiStanjeKarata(String projekcijaID) {
		boolean status = false;
		
		Connection conn = null;
		PreparedStatement stmnt = null;
		
		try {
			conn = ConnectionManager.getConnection();
			
			String query = "UPDATE Projekcije SET BrojProdanihKarata=BrojProdanihKarata+1 WHERE ID=?";

			stmnt = conn.prepareStatement(query);
			stmnt.setString(1, projekcijaID);
			
			int red =stmnt.executeUpdate();
			if(red > 0) {
				status = true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close(conn, stmnt, null);
		}
		return status;
		
	}

}
