package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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
			
			if(rs.next()) {
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
				s = new Sala(id1, naziv, listaTipovaProjekcija);
			}
			else {
				System.out.println("Nije pronadjena projekcija sa tim ID-om");
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close( conn, stmnt, rs);
		}
		return s;
	}
	
	
	
}
