package model;

public class TipProjekcije {
	private int id;
	private String naziv;
	
	public TipProjekcije() {}
	
	public TipProjekcije(int id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public String getNaziv() {
		return naziv;
	}
	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}
	
	

}
