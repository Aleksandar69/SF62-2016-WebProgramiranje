package model;

import java.util.Date;

public class Karta {

	private int id;
	private String projekcijaID;
	private String oznakaSjediste;
	private Date datumProdaje;
	private String korisnikUsername;
	
	
	public Karta() {}
	
	public Karta(int id, String projekcijaID,String oznakaSjediste, Date date, String korisnikUsername) {
		super();
		this.id = id;
		this.projekcijaID = projekcijaID;
		this.oznakaSjediste = oznakaSjediste;
		this.datumProdaje = date;
		this.korisnikUsername = korisnikUsername;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProjekcija() {
		return projekcijaID;
	}
	public void setProjekcija(String projekcijaID) {
		this.projekcijaID = projekcijaID;
	}
	public String getSjediste() {
		return oznakaSjediste;
	}
	public void setSjediste(String sjedisteID) {
		this.oznakaSjediste = sjedisteID;
	}
	public Date getDate() {
		return datumProdaje;
	}
	public void setDate(Date date) {
		this.datumProdaje = date;
	}
	public String getKorisnikUsername() {
		return korisnikUsername;
	}
	public void setKorisnikUsername(String korisnikUsername) {
		this.korisnikUsername = korisnikUsername;
	}
	
	
	
	
}
