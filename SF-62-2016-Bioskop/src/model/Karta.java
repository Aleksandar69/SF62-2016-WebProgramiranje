package model;

import java.util.Date;

public class Karta {

	private int id;
	private Projekcija projekcija;
	private Sjediste sjediste;
	private Date date;
	private String korisnikUsername;
	
	
	public Karta() {}
	
	public Karta(int id, Projekcija projekcija, Sjediste sjediste, Date date, String korisnikUsername) {
		super();
		this.id = id;
		this.projekcija = projekcija;
		this.sjediste = sjediste;
		this.date = date;
		this.korisnikUsername = korisnikUsername;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Projekcija getProjekcija() {
		return projekcija;
	}
	public void setProjekcija(Projekcija projekcija) {
		this.projekcija = projekcija;
	}
	public Sjediste getSjediste() {
		return sjediste;
	}
	public void setSjediste(Sjediste sjediste) {
		this.sjediste = sjediste;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getKorisnikUsername() {
		return korisnikUsername;
	}
	public void setKorisnikUsername(String korisnikUsername) {
		this.korisnikUsername = korisnikUsername;
	}
	
	
	
	
}
