package model;

import java.util.Date;

public class Korisnik {
	
	private String korIme;
	private String lozinka;
	private Date datumRegistracije;
	private Uloga uloga;
	
	public Korisnik() {}
	
	public Korisnik(String korIme, String lozinka, Date datumRegistracije, Uloga uloga) {
		super();
		this.korIme = korIme;
		this.lozinka = lozinka;
		this.datumRegistracije = datumRegistracije;
		this.uloga = uloga;
	}

	public String getKorIme() {
		return korIme;
	}

	public void setKorIme(String korIme) {
		this.korIme = korIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public Date getDatumRegistracije() {
		return datumRegistracije;
	}

	public void setDatumRegistracije(Date datumRegistracije) {
		this.datumRegistracije = datumRegistracije;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}
	
	

}
