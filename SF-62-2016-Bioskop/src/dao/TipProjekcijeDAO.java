package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import model.TipProjekcije;

public class TipProjekcijeDAO {
	
 public TipProjekcije nadjiTipProjPoId(int id) {
	TipProjekcije tp = null;
	Connection conn = null;
	PreparedStatement stmnt = null;
	ResultSet rs = null;
	
	try {
		conn = ConnectionManager.getConnection();
		
		String query = "SELECT ID,Naziv FROM Tipovi_Projekcija WHERE ID = ?";

		stmnt = conn.prepareStatement(query);
		stmnt.setString(1, String.valueOf(id));
		
		rs = stmnt.executeQuery();
		
		if(rs.next()) {
			int index = 1;
			
			int id1 = Integer.valueOf(rs.getString(index++));
			String naziv = rs.getString(index++);
			tp = new TipProjekcije(id1, naziv);
			
		}
		else {
			System.out.println("Nije pronadjena projekcija sa dati id-om");
		}
	}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.close( conn, stmnt, rs);
		}
	return tp;
 }
 

}
